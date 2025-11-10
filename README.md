### 1. 대기열 기반 콘서트 예매 프로젝트

대기열을 기반으로 콘서트 좌석 조회, 예약, 결제하는 프로젝트 입니다.

<br>

### 2. 실행 방법

1. infra : Application, MySQL, Redis 컨테이너를 `local` 환경에서 실행합니다.
2. monitoring : Prometheus, Grafana 기반으로 `local` 환경에서 모니터링 기능을 제공합니다.
    - admin/admin 계정을 통해 로그인하여 접근이 가능합니다.

```bash
docker-compose -f ./docker/docker-compose.yml up
```

<br>

### 3. 개발 환경

1. Language : Java 17
2. Framework : Spring Boot 3.4
3. Database : MySQL 8.0
4. Cache : Redis 7.4
5. Test : JUnit 5 + AssertJ

<br>

### 4. Concert documentation

1. [비즈니스 플로우](https://github.com/pbg0205/concert/wiki/(00)-%EB%B9%84%EC%A6%88%EB%8B%88%EC%8A%A4-%EC%A0%84%EC%B2%B4-%ED%94%8C%EB%A1%9C%EC%9A%B0)
2. [마일스톤](https://github.com/pbg0205/concert/wiki/(01)-%EB%A7%88%EC%9D%BC%EC%8A%A4%ED%86%A4)
3. [요구사항 정의](https://github.com/pbg0205/concert/wiki/(02)-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EC%A0%95%EC%9D%98)
4. [시퀀스 다이어그램](https://github.com/pbg0205/concert/wiki/(03)-%EC%8B%9C%ED%80%80%EC%8A%A4-%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8)
5. [ERDiagram](https://github.com/pbg0205/concert/wiki/(04)-ERD)
6. [상태 전이 다이어그램](https://github.com/pbg0205/concert/wiki/(05)-%EC%83%81%ED%83%9C-%EC%A0%84%EC%9D%B4-%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8)
7. [패키지 구성 및 의존성 구조](https://github.com/pbg0205/concert/wiki/(06)-%ED%8C%A8%ED%82%A4%EC%A7%80-%EA%B5%AC%EC%84%B1-%EB%B0%8F-%EC%9D%98%EC%A1%B4%EC%84%B1-%EA%B5%AC%EC%A1%B0)
8. [API 명세](https://github.com/pbg0205/concert/wiki/(07)-API-%EB%AA%85%EC%84%B8)
9. [Swagger 스크린샷 첨부 및 실행 방법](https://github.com/pbg0205/concert/wiki/(08)-swagger-%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7-%EB%B0%8F-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%8B%A4%ED%96%89-%EB%B0%A9%EB%B2%95)
10. [동시성 제어 보고서](https://github.com/pbg0205/concert/wiki/(09)-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4-%EB%B3%B4%EA%B3%A0%EC%84%9C)
11. [캐싱 검토 보고서](https://github.com/pbg0205/concert/wiki/(10)-%EC%BA%90%EC%8B%9C-%EB%B3%B4%EA%B3%A0%EC%84%9C)
12. [인덱스 적용 보고서](https://github.com/pbg0205/concert/wiki/(11)-%EC%9D%B8%EB%8D%B1%EC%8A%A4-%EA%B0%9C%EC%84%A0-%EB%B3%B4%EA%B3%A0%EC%84%9C)
13. [서비스 구조 개선 검토 보고서](https://github.com/pbg0205/concert/wiki/(12)-%EA%B2%B0%EC%A0%9C-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4-%EA%B5%AC%EC%A1%B0-%EA%B0%9C%EC%84%A0-%EB%B3%B4%EA%B3%A0%EC%84%9C)
14. [HikariCP 개선 보고서](https://github.com/pbg0205/concert/wiki/(12)-%EA%B2%B0%EC%A0%9C-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4-%EA%B5%AC%EC%A1%B0-%EA%B0%9C%EC%84%A0-%EB%B3%B4%EA%B3%A0%EC%84%9C)
15. [시나리오 테스트 및 진단 보고서](https://github.com/pbg0205/concert/wiki/(14)-%EC%8B%9C%EB%82%98%EB%A6%AC%EC%98%A4-%ED%85%8C%EC%8A%A4%ED%8A%B8)
16. [장애 대응 보고서](https://github.com/pbg0205/concert/wiki/(15)-%EC%9E%A5%EC%95%A0-%EB%8C%80%EC%9D%91-%EB%B3%B4%EA%B3%A0%EC%84%9C)
