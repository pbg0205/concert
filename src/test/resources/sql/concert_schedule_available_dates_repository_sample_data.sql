-- Insert concert 데이터
INSERT INTO concert (id, concert_id, name, created_at, modified_at)
VALUES
    (1, 1, 'Rock Festival 2025', 1736294400000, 1736294400000), -- 2025-01-08
    (2, 2, 'Jazz Night 2025', 1736380800000, 1736380800000);   -- 2025-01-09

-- Insert concert_schedule 데이터
-- Rock Festival 2025 스케줄 (25개)
INSERT INTO concert_schedule (id, concert_id, start_at, end_at, created_at, modified_at)
VALUES
    (1, 1, 1736294400000, 1736305200000, 1736294400000, 1736294400000), -- 2025-01-08
    (2, 1, 1736380800000, 1736391600000, 1736294400000, 1736294400000), -- 2025-01-09
    (3, 1, 1736467200000, 1736478000000, 1736294400000, 1736294400000), -- 2025-01-10
    (4, 1, 1736553600000, 1736564400000, 1736294400000, 1736294400000), -- 2025-01-11
    (5, 1, 1736640000000, 1736650800000, 1736294400000, 1736294400000), -- 2025-01-12
    (6, 1, 1736726400000, 1736737200000, 1736294400000, 1736294400000), -- 2025-01-13
    (7, 1, 1736812800000, 1736823600000, 1736294400000, 1736294400000), -- 2025-01-14
    (8, 1, 1736899200000, 1736910000000, 1736294400000, 1736294400000), -- 2025-01-15
    (9, 1, 1736985600000, 1736996400000, 1736294400000, 1736294400000), -- 2025-01-16
    (10, 1, 1737072000000, 1737082800000, 1736294400000, 1736294400000), -- 2025-01-17
    (11, 1, 1737158400000, 1737169200000, 1736294400000, 1736294400000), -- 2025-01-18
    (12, 1, 1737244800000, 1737255600000, 1736294400000, 1736294400000), -- 2025-01-19
    (13, 1, 1737331200000, 1737342000000, 1736294400000, 1736294400000), -- 2025-01-20
    (14, 1, 1737417600000, 1737428400000, 1736294400000, 1736294400000), -- 2025-01-21
    (15, 1, 1737504000000, 1737514800000, 1736294400000, 1736294400000), -- 2025-01-22
    (16, 1, 1737590400000, 1737601200000, 1736294400000, 1736294400000), -- 2025-01-23
    (17, 1, 1737676800000, 1737687600000, 1736294400000, 1736294400000), -- 2025-01-24
    (18, 1, 1737763200000, 1737774000000, 1736294400000, 1736294400000), -- 2025-01-25
    (19, 1, 1737849600000, 1737860400000, 1736294400000, 1736294400000), -- 2025-01-26
    (20, 1, 1737936000000, 1737946800000, 1736294400000, 1736294400000), -- 2025-01-27
    (21, 1, 1738022400000, 1738033200000, 1736294400000, 1736294400000), -- 2025-01-28
    (22, 1, 1738108800000, 1738119600000, 1736294400000, 1736294400000), -- 2025-01-29
    (23, 1, 1738195200000, 1738206000000, 1736294400000, 1736294400000), -- 2025-01-30
    (24, 1, 1738281600000, 1738292400000, 1736294400000, 1736294400000), -- 2025-01-31
    (25, 1, 1738368000000, 1738378800000, 1736294400000, 1736294400000); -- 2025-02-01

-- Jazz Night 2025 스케줄 (25개)
INSERT INTO concert_schedule (id, concert_id, start_at, end_at, created_at, modified_at)
VALUES
    (26, 2, 1736380800000, 1736391600000, 1736380800000, 1736380800000), -- 2025-01-09
    (27, 2, 1736467200000, 1736478000000, 1736380800000, 1736380800000), -- 2025-01-10
    (28, 2, 1736553600000, 1736564400000, 1736380800000, 1736380800000), -- 2025-01-11
    (29, 2, 1736640000000, 1736650800000, 1736380800000, 1736380800000), -- 2025-01-12
    (30, 2, 1736726400000, 1736737200000, 1736380800000, 1736380800000), -- 2025-01-13
    (31, 2, 1736812800000, 1736823600000, 1736380800000, 1736380800000), -- 2025-01-14
    (32, 2, 1736899200000, 1736910000000, 1736380800000, 1736380800000), -- 2025-01-15
    (33, 2, 1736985600000, 1736996400000, 1736380800000, 1736380800000), -- 2025-01-16
    (34, 2, 1737072000000, 1737082800000, 1736380800000, 1736380800000), -- 2025-01-17
    (35, 2, 1737158400000, 1737169200000, 1736380800000, 1736380800000), -- 2025-01-18
    (36, 2, 1737244800000, 1737255600000, 1736380800000, 1736380800000), -- 2025-01-19
    (37, 2, 1737331200000, 1737342000000, 1736380800000, 1736380800000), -- 2025-01-20
    (38, 2, 1737417600000, 1737428400000, 1736380800000, 1736380800000), -- 2025-01-21
    (39, 2, 1737504000000, 1737514800000, 1736380800000, 1736380800000), -- 2025-01-22
    (40, 2, 1737590400000, 1737601200000, 1736380800000, 1736380800000), -- 2025-01-23
    (41, 2, 1737676800000, 1737687600000, 1736380800000, 1736380800000), -- 2025-01-24
    (42, 2, 1737763200000, 1737774000000, 1736380800000, 1736380800000), -- 2025-01-25
    (43, 2, 1737849600000, 1737860400000, 1736380800000, 1736380800000), -- 2025-01-26
    (44, 2, 1737936000000, 1737946800000, 1736380800000, 1736380800000), -- 2025-01-27
    (45, 2, 1738022400000, 1738033200000, 1736380800000, 1736380800000), -- 2025-01-28
    (46, 2, 1738108800000, 1738119600000, 1736380800000, 1736380800000), -- 2025-01-29
    (47, 2, 1738195200000, 1738206000000, 1736380800000, 1736380800000), -- 2025-01-30
    (48, 2, 1738281600000, 1738292400000, 1736380800000, 1736380800000), -- 2025-01-31
    (49, 2, 1738368000000, 1738378800000, 1736380800000, 1736380800000), -- 2025-02-01
    (50, 2, 1738454400000, 1738465200000, 1736380800000, 1736380800000); -- 2025-02-02
