CREATE TABLE IF NOT EXISTS item (
    id          BIGSERIAL PRIMARY KEY,
    item_name   VARCHAR(255) NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    quantity    INT NOT NULL,
    user_id     BIGINT NOT NULL -- 회원 ID (등록자 정보)
    
);

-- 테이블 코멘트
COMMENT ON TABLE item IS '상품 정보';

-- 컬럼 코멘트
COMMENT ON COLUMN item.item_name   IS '상품 이름';
COMMENT ON COLUMN item.price       IS '상품 가격';
COMMENT ON COLUMN item.quantity    IS '상품 수량';
COMMENT ON COLUMN item.user_id     IS '상품 등록자 (회원 ID)';
