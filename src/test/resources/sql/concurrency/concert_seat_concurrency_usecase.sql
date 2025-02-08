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
