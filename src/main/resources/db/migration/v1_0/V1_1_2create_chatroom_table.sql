CREATE TABLE IF NOT EXISTS chat_room (
    id BIGSERIAL PRIMARY KEY,               -- 자동 증가하는 ID
    chat_room_id VARCHAR(255) NOT NULL UNIQUE, -- 채팅방 고유 ID (예: "1_user1_user2")
    item_id BIGINT NOT NULL,                -- 상품 ID
    user1 VARCHAR(255) NOT NULL,         -- 첫 번째 사용자 ID (정렬된 값)
    user2 VARCHAR(255) NOT NULL,         -- 두 번째 사용자 ID
    created_at TIMESTAMP DEFAULT now()      -- 채팅방 생성 시간
);

-- 테이블 코멘트
COMMENT ON TABLE chat_room IS '1:1 채팅방 정보 테이블';

-- 컬럼 코멘트
COMMENT ON COLUMN chat_room.id IS '기본 키 (자동 증가)';
COMMENT ON COLUMN chat_room.chat_room_id IS '채팅방 고유 ID (예: itemId_user1_user2)';
COMMENT ON COLUMN chat_room.item_id IS '상품 ID';
COMMENT ON COLUMN chat_room.user1_id IS '첫 번째 사용자 ID (작은 ID)';
COMMENT ON COLUMN chat_room.user2_id IS '두 번째 사용자 ID';
COMMENT ON COLUMN chat_room.created_at IS '채팅방 생성 시간';
