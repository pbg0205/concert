INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (10001, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f77e', '-', '')), 1001, 'PROCESSING',
        1893456000000); --2030.01.01T00:00:00
