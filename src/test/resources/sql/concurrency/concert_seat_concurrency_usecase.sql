-- 유저 샘플 데이터
INSERT INTO user (id, name, alt_id, created_at, modified_at)
VALUES (1, 'user01', UNHEX(REPLACE('01943b62-8fed-7ea1-9d56-085529e28b11', '-', '')), 1736174400, 1736174400),
       (2, 'user02', UNHEX(REPLACE('01943b62-be7d-7e75-9db8-6ccccebf5d64', '-', '')), 1736174400, 1736174400),
       (3, 'user03', UNHEX(REPLACE('019481de-abf7-7d20-a109-2d491cc0cf7c', '-', '')), 1736174400, 1736174400),
       (4, 'user04', UNHEX(REPLACE('019481de-d9de-716c-90ef-583decf7226d', '-', '')), 1736174400, 1736174400),
       (5, 'user05', UNHEX(REPLACE('019481de-f39b-7ac8-b8d4-f8195f4d14b9', '-', '')), 1736174400, 1736174400),
       (6, 'user05', UNHEX(REPLACE('01948211-83d3-77c7-bbcb-db573f21689e', '-', '')), 1736174400, 1736174400),
       (7, 'user05', UNHEX(REPLACE('01948211-aa74-7aad-bebc-f62331e1c299', '-', '')), 1736174400, 1736174400),
       (8, 'user05', UNHEX(REPLACE('01948211-c7d4-7a85-a6a4-7e3c17c0f64e', '-', '')), 1736174400, 1736174400),
       (9, 'user05', UNHEX(REPLACE('01948211-dcc4-7e1d-b75e-e1db6e982eb8', '-', '')), 1736174400, 1736174400),
       (10, 'user05', UNHEX(REPLACE('01948211-f1a1-7f8a-a618-7a85cc4201ea', '-', '')), 1736174400, 1736174400);

-- 유저 잔고 샘플데이터
INSERT INTO user_balance (id, user_id, point, created_at, modified_at)
VALUES (1, 1, 1000, 1672972800, 1672972800),
       (2, 2, 1000, 1672972800, 1672972800),
       (3, 3, 1000, 1672972800, 1672972800),
       (4, 4, 1000, 1672972800, 1672972800),
       (5, 5, 1000, 1672972800, 1672972800),
       (6, 6, 1000, 1672972800, 1672972800),
       (7, 7, 1000, 1672972800, 1672972800),
       (8, 8, 1000, 1672972800, 1672972800),
       (9, 9, 1000, 1672972800, 1672972800),
       (10, 10, 1000, 1672972800, 1672972800);

-- 토큰 아이디 (모두 활성화 토큰으로 설정)
INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10001, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f77e', '-', '')), 1, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00

INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10002, UUID_TO_BIN(REPLACE('019481e4-cb8e-7a71-8e38-f3eb3d02a058', '-', '')), 2, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00

INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10003, UUID_TO_BIN(REPLACE('019481e4-f587-7707-8270-593b547d0c4f', '-', '')), 3, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00

INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10004, UUID_TO_BIN(REPLACE('019481e5-1b7b-77ab-880f-1eb7b59de0e5', '-', '')), 4, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00

INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10005, UUID_TO_BIN(REPLACE('019481e6-2fc4-786d-a9f5-87bb05390423', '-', '')), 5, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00

INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10006, UUID_TO_BIN(REPLACE('019481e7-f4d8-7109-8bcf-ccd4b212c3de', '-', '')), 6, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00

INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10007, UUID_TO_BIN(REPLACE('019481e8-15f6-7968-842a-1be0099d0fea', '-', '')), 7, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00

INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10008, UUID_TO_BIN(REPLACE('019481e8-49d7-7dc3-9c59-244cfe0b2722', '-', '')), 8, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00

INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10009, UUID_TO_BIN(REPLACE('019481e8-61ea-7f92-a601-647b2f0317d4', '-', '')), 9, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00

INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10010, UUID_TO_BIN(REPLACE('019481e8-7b6d-778e-9c0c-9afbdbb0c7ff', '-', '')), 10, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00


-- 콘서트 샘플데이터
INSERT INTO concert (id, concert_id, name, created_at, modified_at)
VALUES
    (1, 1, 'Rock Festival 2025', 1736294400000, 1736294400000);

-- 콘서트 일정 데이터
INSERT INTO concert_schedule (id, concert_id, start_at, end_at, created_at, modified_at)
VALUES
    -- Rock Festival 2025 스케줄
    (1, 1, 1736294400000, 1736305200000, 1736294400000, 1736294400000); -- 2025-01-08 18:00 ~ 21:00

-- 콘서트 좌석 데이터
INSERT INTO concert_seat (id, schedule_id, seat_number, status, created_at, modified_at, version)
VALUES (1, 1, 1, 'AVAILABLE', 1736294400000, 1736294400000, 1);
