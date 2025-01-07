INSERT INTO queue_token (id, token_id, user_id, status, expired_at)
VALUES (1, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f77e', '-', '')), 1, 'WAITING', 0),
       (2, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f78f', '-', '')), 2, 'PROCESSING',
        1704759600), -- 현재 시간 + 3분
       (3, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f79d', '-', '')), 3, 'COMPLETED',
        1704756000), -- 현재 시간 - 10분
       (4, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f7ab', '-', '')), 4, 'WAITING', 0),
       (5, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f7bb', '-', '')), 5, 'PROCESSING',
        1704759300), -- 현재 시간 + 4분
       (6, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f7c4', '-', '')), 6, 'COMPLETED',
        1704755400), -- 현재 시간 - 15분
       (7, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f7d2', '-', '')), 7, 'WAITING', 0),
       (8, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f7e1', '-', '')), 8, 'PROCESSING',
        1704758700), -- 현재 시간 + 2분
       (9, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f7f5', '-', '')), 9, 'COMPLETED',
        1704755100), -- 현재 시간 - 20분
       (10, UUID_TO_BIN(REPLACE('01b8f8a1-6f8c-7b6e-87c3-234a3c15f804', '-', '')), 10, 'WAITING', 0);
