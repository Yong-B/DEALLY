package com.example.chat.handler;

import com.example.chat.controller.dto.ChatMessageDto;
import com.example.chat.domain.ChatRoom;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.MessageRepository;
import com.example.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    // 현재 연결된 세션들
    private final Map<String, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, String> params = getQueryParams(session);

        String chatRoomId = params.get("chatRoomId");    // itemId_buyerId 형식
        String senderId = params.get("senderId");        // 로그인 유저
        String receiverId = params.get("receiverId");    // 상품 등록자
        String chattingId = chatRoomId + "_" + senderId + "_" + receiverId;
        
        log.info("✅ WebSocket 연결됨 - chatRoomId: {}, senderId: {}, receiverId: {}",
                chatRoomId, senderId, receiverId);

        ChatRoom chatRoom;
        if (senderId.equals(receiverId)) {
            chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId)
                    .orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));
            receiverId = chatRoom.getSenderId();  // 🔥 receiverId를 DB에 저장된 senderId로 변경
            log.info("🔄 receiverId 업데이트: {}", receiverId);
        } else {
            String finalReceiverId = receiverId;
            chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId)
                    .orElseGet(() -> createChatRoom(chatRoomId, senderId, finalReceiverId));
        }

        // 채팅방 세션 관리
        chatRoomSessionMap.putIfAbsent(chatRoomId, new HashSet<>());
        chatRoomSessionMap.get(chatRoomId).add(session);
    }

    // 소켓 통신 시 메세지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        // 페이로드 -> ChatMessageDto 변환
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
        String chatRoomId = chatMessageDto.getChatRoomId();

        // 채팅방 존재 여부 확인
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다: " + chatRoomId));

        // 🔹 senderId와 receiverId가 같다면, DB에서 실제 저장된 senderId를 receiverId로 업데이트
        if (chatMessageDto.getSenderId().equals(chatMessageDto.getReceiverId())) {
            chatMessageDto.setReceiverId(chatRoom.getSenderId());
            log.info("🔄 메시지 receiverId 업데이트: {}", chatMessageDto.getReceiverId());
        }

        log.info("💬 수신한 메시지: {}", mapper.writeValueAsString(chatMessageDto));

        // 채팅방 세션 확인
        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            log.warn("⚠️ 채팅방 없음: {}", chatRoomId);
            return;
        }

        // 메시지 DB 저장
        chatService.saveMessage(chatMessageDto);

        // 1:1 채팅에서 수신자만 메시지 수신
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);
        sendMessageToReceiver(chatMessageDto, chatRoomSession);
    }

    // 소켓 연결 끊김 처리
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("❌ WebSocket 연결 끊김: {}", session.getId());
        removeClosedSession(session);
    }

    // ====== 채팅 관련 메소드 ======

    // 쿼리 파라미터 파싱 메소드
    private Map<String, String> getQueryParams(WebSocketSession session) {
        Map<String, String> params = new HashMap<>();
        String query = session.getUri().getQuery();

        if (query != null) {
            for (String param : query.split("&")) {
                String[] kv = param.split("=");
                if (kv.length > 1) {
                    params.put(kv[0], kv[1]);
                }
            }
        }
        return params;
    }

    // 닫힌 세션 정리
    private void removeClosedSession(WebSocketSession closedSession) {
        chatRoomSessionMap.values().forEach(sessions -> sessions.removeIf(sess -> sess.equals(closedSession)));
    }

    private void sendMessageToReceiver(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream()
                .filter(session -> {
                    Map<String, String> params = getQueryParams(session);
                    String sessionSenderId = params.get("senderId");
                    String sessionReceiverId = params.get("receiverId");

                    // 🔹 senderId와 receiverId가 같다면, DB에서 실제 저장된 senderId를 receiverId로 업데이트
                    if (sessionSenderId.equals(sessionReceiverId)) {
                        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatMessageDto.getChatRoomId())
                                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다"));
                        sessionReceiverId = chatRoom.getSenderId();
                        log.info("🔄 세션 receiverId 업데이트: {}", sessionReceiverId);
                    }

                    return !sessionSenderId.equals(chatMessageDto.getSenderId());
                })
                .forEach(session -> sendMessage(session, chatMessageDto));
    }

    // 메시지 전송 메소드
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error("⚠️ 메시지 전송 중 에러 발생: {}", e.getMessage(), e);
        }
    }
    private ChatRoom createChatRoom(String chatRoomId, String senderId, String receiverId) {
        // 새로운 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .receiverId(receiverId)
                .createdAt(LocalDateTime.now())  // 채팅방 생성 시간
                .build();

        // 채팅방 저장
        return chatRoomRepository.save(chatRoom);
    }
} 