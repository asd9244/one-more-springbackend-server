# Backend Spring — One More Project

Spring Boot 기반 API 서버. 클라이언트 요청을 받아 PostgreSQL + pgvector로 마스터 데이터를 제공하고, 외부 AI 서버(FastAPI)와 로컬 Ollama(Spring AI)를 사용해 이미지/영수증 분석·레시피 생성·취향 기반 추천을 처리합니다.

---

## 기술 스택 (실제 사용 중)

| 구분 | 기술 |
|------|------|
| 언어 / 런타임 | Java 21 |
| 프레임워크 | Spring Boot 3.4.1 |
| 빌드 | Gradle |
| DB | PostgreSQL + pgvector (Extension) |
| ORM | Spring Data JPA |
| API 문서 | SpringDoc OpenAPI 2.7.0 (Swagger UI) |
| AI 연동 | Spring AI (Ollama) — 임베딩 `bge-m3`, 채팅 `exaone3.5:2.4b` |
| 외부 연동 | RestTemplate → FastAPI AI 서버 (이미지/영수증 분석, 레시피 생성) |
| 기타 | Lombok, springboot3-dotenv, Apache HttpClient 5, JUnit 5 |

---

## 프로젝트 내 데이터 흐름

1. 클라이언트 (Expo 앱)에서 입력 수집: 텍스트, 취향 선택, 식재료 이미지 또는 영수증 이미지.
2. Spring Backend에서 요청 수신 후:
   - 이미지/영수증 → FastAPI AI 서버로 전송 (`type=image` / `type=receipt` 구분).
   - 재료/조미료 목록·검색·취향 분석 → PostgreSQL + Ollama(임베딩/채팅) 사용.
3. AI 서버 (Python)에서:
   - 이미지: YOLO 등으로 객체 인식 후 텍스트로 변환 → LLM으로 레시피/재료 정보 생성.
   - 영수증: OCR로 텍스트 추출 → LLM으로 재료·수량 파싱.
   - 레시피 생성: 재료·조미료·취향을 받아 LLM으로 레시피 생성 (initial / basic / more / real).
4. Python 서버가 결과를 Spring으로 반환 (`{ "result": [ ... ] }`).
5. Spring이 클라이언트에 JSON 응답으로 전달.

---

## 환경 설정 및 실행

### 필요 환경

- Java 21  
- PostgreSQL (pgvector 확장 설치)  
- (선택) Ollama 로컬 실행 — 재료/조미료 검색, 취향 추천, 벡터 마이그레이션에 사용  
- (선택) AI 서버(FastAPI) — 이미지/영수증 분석·레시피 생성용 (`prod` 프로파일 시 필수)

### 설정 파일

- `src/main/resources/application.properties`  
  - 서버 포트, DB URL, JPA, AI 서버 URL, Ollama URL 등.
- `.env` (프로젝트 루트)  
  - `APP_ENV`, `DB_HOST`, `DB_USER`, `DB_PW`, `AI_SERVER_URL`, `OLLAMA_URL`, `ADMIN_SECRET_KEY` 등.

### 프로파일

- `dev` (기본): `MockAiClientService` — AI 서버 없이 Mock 응답.
- `prod`: `RealAiClientService` — 실제 FastAPI AI 서버 호출.

### 실행

```bash
# backend-spring 디렉터리에서
./gradlew bootRun
```

- Swagger UI: `http://localhost:8080/swagger-ui.html`  
- API Docs: `http://localhost:8080/api-docs`

---

## API 개요

| 구분 | 경로 | 설명 |
|------|------|------|
| Master | `GET /api/ingredients`, `GET /api/ingredients/search?q=` | 재료 목록 / 임베딩 검색 |
| Master | `GET /api/spices`, `GET /api/spices/search?q=` | 조미료 목록 / 임베딩 검색 |
| Master | `GET /api/preferences`, `POST /api/preferences/analyze` | 취향 목록 / 취향 기반 재료·조미료 추천 (RAG) |
| Recipe | `POST /api/recipe/analyze` (multipart, `type=image\|receipt`) | 이미지 또는 영수증 분석 |
| Recipe | `POST /api/recipe/generate` (JSON, `action=initial\|basic\|more\|real`) | 레시피 생성 |
| Admin | `POST /api/admin/migration/vectors` (Header: `X-ADMIN-KEY`) | 전체 벡터(임베딩) 마이그레이션 |

