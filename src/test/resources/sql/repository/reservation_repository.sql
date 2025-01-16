INSERT INTO reservation (id, user_id, seat_id, alt_id, status, created_at, modified_at)
VALUES (1, 12345, 67890, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')),
        'PENDING', 1924935600000, 1924935600000); -- created_at, modified_at : 2030-12-31 19:00:00 UTC

