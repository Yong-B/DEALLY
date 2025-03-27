CREATE TABLE IF NOT EXISTS message (
    id BIGSERIAL PRIMARY KEY,           -- 메시지 ID
    chat_room_id BIGINT NOT NULL,       -- 채팅방 ID (외래키)
    sender_id VARCHAR(255) NOT NULL,    -- 메시지를 보낸 사람 ID
    message TEXT NOT NULL,              -- 메시지 내용
    timestamp TIMESTAMP DEFAULT now(),  -- 메시지 보낸 시간
    CONSTRAINT fk_chat_room FOREIGN KEY (chat_room_id) REFERENCES chat_room(id) ON DELETE CASCADE -- 외래키 제약
);