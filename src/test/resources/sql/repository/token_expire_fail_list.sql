INSERT INTO queue_token_expire_outbox (id, topic, type, payload, payment_id, created_at, modified_at)
VALUES (1, 'token.expire', 'SEND_FAIL', '{userId : 1, paymentAltId: "01952216-2bd8-7162-8cc4-458c8baaf6e0"}',
        UNHEX(REPLACE('01952216-2bd8-7162-8cc4-458c8baaf6e0', '-', '')),
        1924905600000, 1924905600000); -- created_at, modified_at : 2030-12-31 14:00:00	 UTC

INSERT INTO queue_token_expire_outbox (id, topic, type, payload, payment_id, created_at, modified_at)
VALUES (2, 'token.expire', 'SEND_FAIL', '{userId : 2, paymentAltId: "01952221-6049-731d-9616-8fc3447b160b"}',
        UNHEX(REPLACE('01952221-6049-731d-9616-8fc3447b160b', '-', '')),
        1924909200000, 1924909200000); -- created_at, modified_at : 2030-12-31 15:00:00 UTC

INSERT INTO queue_token_expire_outbox (id, topic, type, payload, payment_id, created_at, modified_at)
VALUES (3, 'token.expire', 'SEND_FAIL', '{userId : 3, paymentAltId: "01952222-4007-7bcf-8e7b-ccf5b52b0d5b"}',
        UNHEX(REPLACE('01952222-4007-7bcf-8e7b-ccf5b52b0d5b', '-', '')),
        1924912800000, 1924912800000); -- created_at, modified_at : 2030-12-31 16:00:00 UTC

INSERT INTO queue_token_expire_outbox (id, topic, type, payload, payment_id, created_at, modified_at)
VALUES (4, 'token.expire', 'SEND_FAIL', '{userId : 4, paymentAltId: "01952223-078e-783e-81f0-44d343f339e0"}',
        UNHEX(REPLACE('01952223-078e-783e-81f0-44d343f339e0', '-', '')),
        1924916400000, 1924916400000); -- created_at, modified_at : 2030-12-31 17:00:00 UTC

INSERT INTO queue_token_expire_outbox (id, topic, type, payload, payment_id, created_at, modified_at)
VALUES (5, 'token.expire', 'SEND_FAIL', '{userId : 5, paymentAltId: "01952223-eec2-73cc-9923-a5d059daff44"}',
        UNHEX(REPLACE('01952223-eec2-73cc-9923-a5d059daff44', '-', '')),
        1924920000000, 1924920000000); -- created_at, modified_at : 2030-12-31 18:00:00 UTC

INSERT INTO queue_token_expire_outbox (id, topic, type, payload, payment_id, created_at, modified_at)
VALUES (6, 'token.expire', 'SEND_FAIL', '{userId : 6, paymentAltId: "01952225-23af-7aee-a075-7b007d7a2303"}',
        UNHEX(REPLACE('01952225-23af-7aee-a075-7b007d7a2303', '-', '')),
        1924923600000, 1924923600000); -- created_at, modified_at : 2030-12-31 19:00:00 UTC

