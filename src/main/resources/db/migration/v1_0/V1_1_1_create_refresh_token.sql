CREATE TABLE IF NOT EXISTS refreshtoken (
    id          BIGSERIAL PRIMARY KEY,
    login_id    VARCHAR(255) NOT NULL,
    refresh     TEXT NOT NULL,
    expiration  VARCHAR(255) NOT NULL
);

-- 테이블 코멘트
COMMENT ON TABLE refreshtoken IS '리프레시 토큰 정보';

-- 컬럼 코멘트
COMMENT ON COLUMN refreshtoken.login_id   IS '로그인 ID';
COMMENT ON COLUMN refreshtoken.refresh    IS '리프레시 토큰';
COMMENT ON COLUMN refreshtoken.expiration IS '토큰 만료 시간';
