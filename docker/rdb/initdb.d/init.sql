# 사용자 생성
CREATE USER 'exporter'@'%' IDENTIFIED BY 'concert2025';

# 생성한 유저에게 데이터를 가져오기 위한 적절한 권한을 부여해줍시다.
GRANT PROCESS, REPLICATION CLIENT, SELECT ON *.* TO 'exporter'@'%';
