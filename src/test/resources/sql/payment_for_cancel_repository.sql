-- 2025.12.31T19:00:00 UTC
INSERT INTO payment (id, alt_id, reservation_id, status, created_at, modified_at)
VALUES (1, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')), 12345, 'COMPLETED', 1767188400000,
        1767188400000),
       (2, UNHEX(REPLACE('01944c8b-fab1-72e2-a742-94368e9b2bc2', '-', '')), 12346, 'COMPLETED', 1767188400000,
        1767188400000),
       (3, UNHEX(REPLACE('01944c8c-1664-72b2-b059-2e8ec0ffdd01', '-', '')), 12347, 'PENDING', 1767188400000,
        1767188400000),
       (4, UNHEX(REPLACE('01944c8c-3896-767b-9bac-07a3ab0709bc', '-', '')), 12348, 'PENDING', 1767188400000,
        1767188400000),
       (5, UNHEX(REPLACE('01944c8c-59a4-7e5b-9cdb-660b6e450fa6', '-', '')), 12349, 'PENDING', 1767188400000,
        1767188400000),
       (6, UNHEX(REPLACE('01944c8c-910a-7611-93b9-fdfb1957b8bb', '-', '')), 12350, 'PENDING', 1767188400000,
        1767188400000);
