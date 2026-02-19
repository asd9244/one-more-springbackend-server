-- preferences 테이블에 'STYLE' 데이터 대량 추가
INSERT INTO preferences (category, name) VALUES

-- 1. 국가/지역별 상세 (Global)
('STYLE', '한식'),
('STYLE', '중식'),
('STYLE', '일식'),
('STYLE', '태국 요리'),
('STYLE', '베트남 요리'),
('STYLE', '인도 요리'),
('STYLE', '태국 요리'),
('STYLE', '인도 요리'),
('STYLE', '멕시코 요리'),
('STYLE', '이탈리아 요리'),
('STYLE', '프랑스 요리'),
('STYLE', '스페인 요리'),
('STYLE', '미국식 요리'),
('STYLE', '독일 요리'),
('STYLE', '터키 요리'),
('STYLE', '그리스 요리'),
('STYLE', '대만 요리'),
('STYLE', '홍콩 요리'),
('STYLE', '러시아 요리'),

-- 2. 분위기 및 식사 형태 (Mood & Type)
('STYLE', '분식'),            -- 떡볶이, 순대 등
('STYLE', '가정식'),          -- 집밥 스타일
('STYLE', '브런치'),          -- 가벼운 아점
('STYLE', '패스트푸드'),
('STYLE', '길거리 음식'),      -- 붕어빵, 타코야끼 등
('STYLE', '파인다이닝'),       -- 고급 코스 요리
('STYLE', '뷔페/샐러드바'),
('STYLE', '펍/안주'),         -- 술과 곁들이는 요리
('STYLE', '도시락'),
('STYLE', '디저트/베이커리'),
('STYLE', '편의점 꿀조합'),    -- 마크정식 등

-- 3. 특수 식단 및 건강 (Diet & Health)
('STYLE', '비건/채식'),
('STYLE', '키토/저탄고지'),
('STYLE', '할랄 푸드'),
('STYLE', '다이어트 식단'),
('STYLE', '고단백 식단'),
('STYLE', '저염식'),
('STYLE', '보양식'),
('STYLE', '환자식/죽'),

-- 4. 상황별 (Situation)
('STYLE', '캠핑 요리'),
('STYLE', '파티 음식'),
('STYLE', '명절 음식'),
('STYLE', '자취 요리'),        -- 간단하고 저렴한
('STYLE', '밀키트/간편식')

ON CONFLICT (name) DO NOTHING; -- 중복 시 에러 없이 스킵



-- preferences 테이블에 'TASTE' 데이터 대량 추가 (총 35종)
INSERT INTO preferences (category, name) VALUES

-- 1. 기본 미각 (Basic)
('TASTE', '맛있게'),
('TASTE', '단 맛'),
('TASTE', '짠 맛'),
('TASTE', '신 맛'),
('TASTE', '쓴 맛'),
('TASTE', '감칠맛'),
('TASTE', '고소한 맛'),

-- 2. 맵기의 디테일 (Spiciness) - 한국인 필수!
('TASTE', '매운 맛'),       -- Standard Spicy
('TASTE', '매콤한 맛'),     -- Mildly Spicy (기분 좋은 매운맛)
('TASTE', '얼큰한 맛'),     -- Spicy & Savory (국물 요리)
('TASTE', '칼칼한 맛'),     -- Sharp Spicy (고춧가루/청양고추 느낌)
('TASTE', '알싸한 맛'),     -- Pungent (마늘/파/와사비)
('TASTE', '화끈한 맛'),     -- Very Spicy (불닭 급)

-- 3. 식감 관련 (Texture) - 맛의 일부로 인식됨
('TASTE', '바삭한 맛'),
('TASTE', '촉촉한 맛'),
('TASTE', '쫄깃한 맛'),
('TASTE', '부드러운 맛'),
('TASTE', '아삭한 맛'),
('TASTE', '꾸덕한 맛'),     -- 로제 소스, 그릭요거트 등
('TASTE', '탱글한 맛'),

-- 4. 풍미 및 농도 (Flavor & Richness)
('TASTE', '담백한 맛'),
('TASTE', '느끼한 맛'),     -- 가끔은 치즈 듬뿍 느끼한 게 땡길 때
('TASTE', '기름진 맛'),
('TASTE', '진한 맛'),       -- 깊은 국물 맛
('TASTE', '삼삼한 맛'),     -- 자극적이지 않은 맛
('TASTE', '크리미한 맛'),
('TASTE', '불맛'),         -- 직화/스모키
('TASTE', '훈제향'),

-- 5. 복합적인 맛 (Complex)
('TASTE', '새콤달콤한 맛'),
('TASTE', '단짠단짠'),      -- Sweet & Salty
('TASTE', '맵단 맛'),       -- Spicy & Sweet (떡볶이 등)
('TASTE', '겉바속촉'),
('TASTE', '시원한 맛'),     -- Cool or Refreshing Broth
('TASTE', '개운한 맛'),
('TASTE', '깔끔한 맛'),
('TASTE', '자극적인 맛')

ON CONFLICT (name) DO NOTHING;

-- [데이터 이동] 잘못된 STYLE 데이터를 CONDITION으로 수정
UPDATE preferences
SET category = 'CONDITION'
WHERE name IN (
    -- 건강/식단
    '비건/채식', '키토/저탄고지', '할랄 푸드', '다이어트 식단',
    '고단백 식단', '저염식', '보양식', '환자식/죽',
    -- 상황/편의
    '캠핑 요리', '파티 음식', '명절 음식', '자취 요리',
    '밀키트/간편식', '편의점 꿀조합'
);

-- [데이터 추가] CONDITION 카테고리 보강
INSERT INTO preferences (category, name) VALUES
-- 신체 상태
('CONDITION', '해장용'),        -- 술 마신 다음 날
('CONDITION', '술안주/야식'),    -- 술과 함께
('CONDITION', '속편한/소화잘되는'), -- 아침이나 아플 때
('CONDITION', '스트레스 해소'),   -- 매운거 땡길 때
('CONDITION', '입맛 돋우는'),     -- 상큼한게 필요할 때

-- 식사량/인원
('CONDITION', '1인분/혼밥'),
('CONDITION', '대용량/손님초대'),
('CONDITION', '가벼운 한끼'),

-- 조리 환경
('CONDITION', '초스피드/5분컷'),
('CONDITION', '원팬/설거지최소'),  -- 귀찮을 때 최고
('CONDITION', '냉장고 파먹기'),    -- 재료 처리용
('CONDITION', '도시락 반찬')

ON CONFLICT (name) DO NOTHING; -- 이름 중복 시 에러 없이 넘어감
