INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10001, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f77e', '-', '')), 1001, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00

INSERT INTO concert_seat (id, schedule_id, seat_number, price, status, created_at, modified_at)
VALUES (67890,1, 1, 3000, 'AVAILABLE', 1738268400000, 1738268400000);

INSERT INTO reservation (id, user_id, seat_id, alt_id, status, created_at, modified_at)
VALUES (12345, 1, 67890, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')),
        'PENDING', 1924935600000, 1924935600000); -- created_at, modified_at : 2030-12-31 19:00:00 UTC

INSERT INTO payment (id, alt_id, reservation_id, status, created_at, modified_at)
VALUES (1, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')), 12345, 'PENDING', 1767188400000,
        1767188400000); -- 2025.12.31T19:00:00 UTC

-- 유저 샘플데이터
INSERT INTO user (id, name, alt_id, created_at, modified_at)
VALUES (1, 'user01', UNHEX(REPLACE('01943b62-8fed-7ea1-9d56-085529e28b11', '-', '')), 1736174400, 1736174400);

-- 유저 잔고 샘플데이터
INSERT INTO user_balance (id, user_id, point, created_at, modified_at)
VALUES (1, 1, 5000, 1672972800, 1672972800);

