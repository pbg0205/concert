-- 유저 샘플데이터
INSERT INTO user (id, name, alt_id, created_at, modified_at)
VALUES (1, 'user01', UNHEX(REPLACE('01943b62-8fed-7ea1-9d56-085529e28b11', '-', '')), 1736174400, 1736174400),
       (2, 'user02', UNHEX(REPLACE('01943b62-be7d-7e75-9db8-6ccccebf5d64', '-', '')), 1736174400, 1736174400);
