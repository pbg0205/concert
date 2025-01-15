INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (1001, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f77e', '-', '')), 1001, 'COMPLETED',
        1736251200000), --2025.01.07T12:00:00
       (1002, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f78f', '-', '')), 1002, 'PROCESSING',
        1736251230000); --2025.01.07T12:00:30
