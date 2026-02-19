--1.  pgvector 확장 프로그램 활성화
CREATE EXTENSION IF NOT EXISTS vector;

-- 2-0. 사용자 테이블 (앱 기기별 UUID, 로그인 없음)
-- 앱이 로컬에 저장한 UUID를 서버로 보내고, 없으면 INSERT 후 재방문 시 기존 행으로 인식
CREATE TABLE users (
    device_uuid VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2-1. 취향 마스터 테이블 (표준 이름)
CREATE TABLE preferences (
    id SERIAL PRIMARY KEY,
    category VARCHAR(20) NOT NULL,   -- 'STYLE', 'TASTE', 'CONDITION'
    name VARCHAR(50) NOT NULL UNIQUE,-- '매운 맛', '단 맛', '포만감'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    embedding vector(1024)
);

-- 선호도 벡터 검색용 HNSW 인덱스
CREATE INDEX idx_preferences_embedding ON preferences
USING hnsw (embedding vector_cosine_ops);
-- WITH (m = 16, ef_construction = 64) -> HNSW 설정 디폴트 값

-- 2-2. 재료 마스터 테이블 (표준 이름)
CREATE TABLE ingredients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    embedding vector(1024)
);

-- 재료 벡터 검색용 HNSW 인덱스
CREATE INDEX idx_ingredients_embedding ON ingredients
    USING hnsw (embedding vector_cosine_ops);
-- WITH (m = 16, ef_construction = 64) -> HNSW 설정 디폴트 값

-- 2-3. 조미료 마스터 테이블 (표준 이름)
CREATE TABLE spices (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    embedding vector(1024)
);

-- 조미료 벡터 검색용 HNSW 인덱스
CREATE INDEX idx_spices_embedding ON spices
    USING hnsw (embedding vector_cosine_ops);
-- WITH (m = 16, ef_construction = 64) -> HNSW 설정 디폴트 값

-- 레시피 상세 저장 (유저별 "만든 레시피 다시 보기"용)
-- RecipeResponse와 동일 구조: ingredients/more/recipe/tip 는 JSON 문자열로 저장
CREATE TABLE recipes (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(device_uuid) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    summary TEXT,
    ingredients_list TEXT,
    more_list TEXT,
    recipe TEXT,
    tips TEXT,
    image_url TEXT,
    reference_link TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_recipes_user_id ON recipes(user_id);

