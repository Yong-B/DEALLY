CREATE TABLE IF NOT EXISTS item (
    id          BIGSERIAL PRIMARY KEY,
    item_name   VARCHAR(255) NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    user_id     VARCHAR(255) NOT NULL, -- 회원 ID (등록자 정보)
    status      VARCHAR(20) DEFAULT 'AVAILABLE', -- 상품 상태 (예: AVAILABLE, RESERVED, SOLD)
    buyer_id     VARCHAR(255) NULL,
    description TEXT NULL
);

-- 테이블 코멘트
COMMENT ON TABLE item IS '상품 정보';

-- 컬럼 코멘트
COMMENT ON COLUMN item.item_name IS '상품 이름';
COMMENT ON COLUMN item.price IS '상품 가격';
COMMENT ON COLUMN item.user_id IS '상품 등록자 (회원 ID)';
COMMENT ON COLUMN item.status IS '상품 상태 (AVAILABLE: 판매 중, RESERVED: 예약 중, SOLD: 거래 완료)';
COMMENT ON COLUMN item.description IS '상품 설명';
