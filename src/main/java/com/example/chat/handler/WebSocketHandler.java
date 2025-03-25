package com.example.chat.handler;

import com.example.chat.controller.dto.ChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    // 현재 연결된 세션들
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, String> params = getQueryParams(session);

        Long chatRoomId = Long.valueOf(params.get("chatRoomId"));    // itemId
        Long senderId = Long.valueOf(params.get("senderId"));        // 로그인 유저
        Long receiverId = Long.valueOf(params.get("receiverId"));    // 상품 등록자

        log.info("✅ WebSocket 연결됨 - chatRoomId: {}, senderId: {}, receiverId: {}",
                chatRoomId, senderId, receiverId);

        // 채팅방 세션 관리
        chatRoomSessionMap.putIfAbsent(chatRoomId, new HashSet<>());
        chatRoomSessionMap.get(chatRoomId).add(session);
    }

    // 소켓 통신 시 메세지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("💬 수신한 메시지: {}", payload);

        // 페이로드 -> ChatMessageDto 변환
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
        Long chatRoomId = chatMessageDto.getChatRoomId();

        // 채팅방 세션 확인
        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            log.warn("⚠️ 채팅방 없음: {}", chatRoomId);
            return;
        }

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
                .filter(session -> !getQueryParams(session).get("senderId").equals(String.valueOf(chatMessageDto.getSenderId())))
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
}
