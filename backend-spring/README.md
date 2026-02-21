# Backend Spring — One More Project

## 1. 프로젝트 개요

| 항목 | 내용 |
| --- | --- |
| **프로젝트명** | One More Project (backend-spring) |
| **역할** | 사용자 입력(이미지/영수증/텍스트)을 받아 AI로 분석하고, 레시피를 생성·제공하는 **API 서버** |
| **빌드** | Gradle (Java 21, Spring Boot 3.4.1) |
| **진입점** | `backendSpringApplication` (패키지: `com.board.one_more_project`) |

---

## 2. 기술 스택

- **Web**: Spring Web, Validation
- **DB**: Spring Data JPA, PostgreSQL (pgvector 사용)
- **문서**: SpringDoc OpenAPI 2.7.0 (Swagger UI)
- **AI 연동**
    - **외부 서버**: FastAPI(Python) — 이미지/영수증 분석, 레시피 생성
    - **로컬**: Spring AI + Ollama — 임베딩(bge-m3), 채팅(exaone3.5:2.4b)
- **기타**: Lombok, reactor-core, httpclient5, springboot3-dotenv

---

## 3. 아키텍처 요약

- **레이어**: Controller → Service → Repository / AiClientService
- **도메인**: `ingredient`, `recipe`, `preference`, `spice`
- **인프라**: AI 클라이언트(Real/Mock), 벡터 마이그레이션, RestTemplate 설정
- **공통**: 전역 예외 처리, CORS(WebConfig), 에러용 DTO

---

## 4. 도메인별 구조

### 4.1 재료 (Ingredient)

- **테이블**: `ingredients` (이름, `created_at`, `embedding` — Native Query로만 사용)
- **API**
    - `GET /api/ingredients` : 전체 재료 목록
    - `GET /api/ingredients/search?q=` : Ollama 임베딩 + pgvector 유사도 검색 (상위 10개)
- **구성**: Controller → IngredientService(Impl) → IngredientRepository, EmbeddingModel
- **비고**: 엔티티는 `domain.ingredient.dto` 패키지에 있음 (위치만 참고용).

### 4.2 조미료 (Spice)

- **테이블**: `spices` (이름, `created_at`, `embedding`)
- **API**
    - `GET /api/spices` : 전체 조미료
    - `GET /api/spices/search?q=` : 임베딩 기반 유사도 검색
- **구성**: SpiceController → SpiceService(Impl) → SpiceRepository, EmbeddingModel

### 4.3 취향 (Preference)

- **테이블**: `preferences` (category, name, `created_at`, `embedding`)
- **API**
    - `GET /api/preferences` : 취향 목록
    - `POST /api/preferences/analyze` : 선택 취향 → LLM(Exaone)으로 연관 키워드 생성 → 임베딩 검색으로 재료/조미료 추천 (RAG 스타일)
- **구성**: PreferenceController → PreferenceServiceImpl → PreferenceRepository, EmbeddingModel, ChatModel, IngredientRepository, SpiceRepository

### 4.4 레시피 (Recipe)

- **DB 없음**: 레시피는 파이썬 AI 서버 응답만 사용.
- **API**
    - `POST /api/recipe/analyze` (multipart)
        - `type=image` → 식재료 이미지 분석 (`/analyze-image-ingredients`)
        - `type=receipt` → 영수증 분석 (`/analyze-image-receipts`)
    - `POST /api/recipe/generate` (JSON)
        - `action`: `initial` / `basic` / `more` / `real` → 각각 다른 파이썬 엔드포인트 호출
- **검증**: RecipeValidator — 파일 최대 3장, 재료 필수, 취향 100개 이내 등
- **구성**: RecipeController → AiClientService(Real or Mock), RecipeValidator

---

## 5. AI 연동 구조

### 5.1 외부 FastAPI 서버 (이미지/레시피)

- **역할**: 영수증 OCR, 식재료 이미지 분석(YOLO 등), 레시피 생성(LLM).
- **설정**: `ai-server.url` (기본 `http://192.168.22.54:8000`), `.env`의 `AI_SERVER_URL`로 오버라이드.
- **구현**
    - **prod**: `RealAiClientService` — RestTemplate으로 위 URL에 POST (연결 5초, 읽기 120초 타임아웃).
    - **dev**: `MockAiClientService` — 파이썬 없이 고정 Mock 응답.
- **공통 응답 형식**: `{ "result": [ ... ] }` → `PythonResponseWrapper`로 역직렬화.
- **에러**: 연결/타임아웃·4xx·5xx 시 `AiServerException` → `GlobalExceptionHandler`에서 503 + `RecipeResponse` 형태로 메시지 반환.

### 5.2 로컬 Ollama (Spring AI)

- **용도**: 임베딩(bge-m3), 채팅(exaone3.5:2.4b).
- **설정**: `spring.ai.ollama.*`, `OLLAMA_URL`.
- **사용처**
    - 재료/조미료 의미 검색 (EmbeddingModel)
    - 취향 분석 및 추천 (ChatModel + EmbeddingModel + pgvector)
- **벡터 DB**: PostgreSQL pgvector. `ingredients`, `spices`, `preferences`에 `embedding` 컬럼 — Native Query로만 갱신/검색.
- **마이그레이션**: `POST /api/admin/migration/vectors` + `X-ADMIN-KEY` 헤더 → `VectorMigrationService.migrateAll()`로 전 테이블 임베딩 일괄 생성.

---

## 6. 설정 요약

- **프로파일**: `APP_ENV` (기본 `dev`). `dev` → Mock AI, `prod` → Real AI.
- **DB**: `DB_HOST`, `DB_USER`, `DB_PW` (기본 localhost/postgres/1234), `one-more-db`.
- **멀티파트**: 최대 15MB/파일, 76MB/요청.
- **JPA**: `ddl-auto=update`, SQL 포맷·로그 출력.
- **CORS**: `WebConfig`에서 `/**`에 대해 모든 origin/method/header 허용 (운영 시 도메인 제한 권장).

---

## 7. 글로벌 처리

- **예외**
    - `IllegalArgumentException` → 400 + 메시지.
    - `AiServerException` → 503 + 메시지.
    - 공통 응답 형식으로 `RecipeResponse` 재사용 (title/summary 등에 에러 문구).
- **에러 타입**: `AiServerException` (infrastructure 쪽에서 사용).

---

---

## 8. 디렉터리 구조 요약

```
backend/
├── backend-spring/
│   ├── build.gradle
│   ├── .env, application.properties, logback-spring.xml
│   └── src/main/
│       ├── java/.../
│       │   ├── backendSpringApplication.java
│       │   ├── domain/
│       │   │   ├── ingredient/     (Controller, Service, Repository, dto)
│       │   │   ├── preference/     (Controller, Service, Repository, Entity, DTO)
│       │   │   ├── recipe/         (Controller, Validator, Request/Response DTO)
│       │   │   └── spice/          (Controller, Service, Repository, Entity, Response)
│       │   ├── global/
│       │   │   ├── config/         (WebConfig; RestTemplateConfig는 infrastructure에 있으나 패키지는 global.config)
│       │   │   └── error/         (GlobalExceptionHandler, AiServerException)
│       │   └── infrastructure/
│       │       └── ai/            (AiClientService, Real/Mock, RestTemplateConfig, migration)
│       └── resources/
└── project_context_summary.txt, merge.py 등
```
