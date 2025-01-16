INSERT INTO payment (id, alt_id, reservation_id, status, created_at, modified_at)
VALUES (1, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')), 12345, 'PENDING', 1767188400000,
        1767188400000); -- 2025.12.31T19:00:00 UTC
