INSERT INTO concert (id, concert_id, name, created_at, modified_at)
VALUES (1, 1, 'Rock Festival 2025', 1736294400000, 1736294400000); -- 2025-01-08

INSERT INTO concert_schedule (id, concert_id, start_at, end_at, created_at, modified_at)
VALUES (1, 1, 1736294400000, 1736305200000, 1736294400000, 1736294400000); -- 2025-01-08

INSERT INTO concert_seat (id, schedule_id, seat_number, status, created_at, modified_at)
VALUES (1, 1, 1, 'AVAILABLE', 1736294400000, 1736294400000),   -- 2025-01-08
       (2, 1, 2, 'UNAVAILABLE', 1736294400000, 1736294400000), -- 2025-01-08
       (3, 1, 3, 'UNAVAILABLE', 1736294400000, 1736294400000), -- 2025-01-08
       (4, 1, 4, 'UNAVAILABLE', 1736294400000, 1736294400000), -- 2025-01-08
       (5, 1, 5, 'UNAVAILABLE', 1736294400000, 1736294400000), -- 2025-01-08
       (6, 1, 6, 'AVAILABLE', 1736294400000, 1736294400000); -- 2025-01-08
