CREATE TABLE IF NOT EXISTS upload_file (
    id              BIGSERIAL PRIMARY KEY,
    upload_file_name VARCHAR(255) NOT NULL, -- 사용자가 업로드한 파일명
    store_file_name VARCHAR(255) NOT NULL, -- 서버에서 저장된 파일명 (UUID 적용)
    item_id         BIGINT NOT NULL, -- 상품 ID (외래키)
    
    CONSTRAINT fk_upload_file_item FOREIGN KEY (item_id) 
        REFERENCES item(id) ON DELETE CASCADE
);

-- 테이블 코멘트
COMMENT ON TABLE upload_file IS '상품 이미지 업로드 파일 정보';

-- 컬럼 코멘트
COMMENT ON COLUMN upload_file.upload_file_name IS '사용자가 업로드한 파일명';
COMMENT ON COLUMN upload_file.store_file_name IS '서버에 저장된 파일명 (UUID)';
COMMENT ON COLUMN upload_file.item_id IS '상품 ID (item 테이블의 외래키)';
