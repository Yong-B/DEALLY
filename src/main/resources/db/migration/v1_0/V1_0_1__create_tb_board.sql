CREATE TABLE IF NOT EXISTS item (
    id          BIGSERIAL,
    item_name    VARCHAR(255),
    price       NUMERIC(10, 2),
    quantity    INT,

    CONSTRAINT pk_item PRIMARY KEY (id)
);

-- 테이블 코멘트
COMMENT ON TABLE item IS '상품 정보';

-- 컬럼 코멘트
COMMENT ON COLUMN item.item_name   IS '상품 이름';
COMMENT ON COLUMN item.price      IS '상품 가격';
COMMENT ON COLUMN item.quantity   IS '상품 수량';
