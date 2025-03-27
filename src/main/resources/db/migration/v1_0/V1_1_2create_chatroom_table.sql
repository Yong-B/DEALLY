    id BIGSERIAL PRIMARY KEY,         -- 자동 증가하는 ID
    chat_room_id BIGINT NOT NULL,     -- 채팅방 ID (상품 ID)
    sender_id VARCHAR(255) NOT NULL,  -- 보낸 사람 ID
    receiver_id VARCHAR(255) NOT NULL, -- 받는 사람 ID
    created_at TIMESTAMP DEFAULT now(), -- 채팅방 생성 시간
    CONSTRAINT unique_chat_room UNIQUE (chat_room_id) -- chatRoomId를 고유 제약
);


-- 테이블 코멘트
COMMENT ON TABLE chatroom IS '1:1 채팅방 정보 테이블';

-- 컬럼 코멘트
COMMENT ON COLUMN chatroom.id IS '기본 키 (자동 증가)';
COMMENT ON COLUMN chatroom.chatroom_id IS '채팅방 ID (상품 ID)';
COMMENT ON COLUMN chatroom.sender_id IS '메시지를 보낸 사용자 ID';
COMMENT ON COLUMN chatroom.receiver_id IS '메시지를 받은 사용자 ID';
COMMENT ON COLUMN chatroom.message IS '메시지 내용';
COMMENT ON COLUMN chatroom.timestamp IS '메시지를 보낸 시간';