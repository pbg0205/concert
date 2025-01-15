INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10001, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f77e', '-', '')), 1001, 'PROCESSING',
        1924935600000);
--2030.12.31T19:00 UTC

-- Insert concert 데이터
INSERT INTO concert (id, concert_id, name, created_at, modified_at)
VALUES (1, 1, 'Rock Festival 2025', 1924935600000, 1924935600000); --2030.12.31T19:00 UTC

INSERT INTO concert_schedule (id, concert_id, start_at, end_at, created_at, modified_at)
VALUES (1, 1, 1924935600000, 1924935600000, 1924935600000, 1924935600000); --2030.12.31T19:00 UTC


INSERT INTO concert_seat (id, schedule_id, seat_number, status, created_at, modified_at)
VALUES (1, 1, 1, 'AVAILABLE', 1924935600000, 1924935600000); --2030.12.31T19:00 UTC
