# Concert documentation

## [1. Business Flow](https://github.com/pbg0205/concert/issues/2)

<img width="700" alt="flow_chart_concert" src="https://github.com/user-attachments/assets/95875b23-73fc-41f1-bc2c-7623aa94bd37" />

<br>

## [2. Milestone](https://docs.google.com/spreadsheets/d/1F1CJ5Mjd31iMWgo82KW-EIPwzsjAE8RtcF9B1CvQzLc/edit?gid=1991800281#gid=1991800281)

<img width="700" alt="image" src="https://github.com/user-attachments/assets/3fbc1bca-a390-4928-b12b-5c95f3684566" />

<br>

## 3. API 명세
1. [유저 대기열 토큰 발급 API](https://github.com/pbg0205/concert/issues/1)
2. [예약 가능 날짜 조회 API](https://github.com/pbg0205/concert/issues/5)
3. [예약 가능 날짜 좌석 조회 API](https://github.com/pbg0205/concert/issues/6)
4. [좌석 예약 API](https://github.com/pbg0205/concert/issues/7)
5. 사용자 포인트 충전/조회
    1. [사용자 포인트 충전 API](https://github.com/pbg0205/concert/issues/8)
    2. [사용자 포인트 조회 API](https://github.com/pbg0205/concert/issues/10)
6. [결제 요청 API](https://github.com/pbg0205/concert/issues/9)

<br>

## [4. State-Transition Diagram](https://github.com/pbg0205/concert/issues/3)
### (1) 예약 상태 다이어그램

<img width="612" alt="image" src="https://github.com/user-attachments/assets/e79d3cf6-cceb-4728-acfc-03bce4f6d0f6" />

### (2) 결제 상태 다이어그램

<img width="611" alt="image" src="https://github.com/user-attachments/assets/f0b0ae83-3f05-4d7b-b694-fd7ccdab474e" />

<br>

## [5. Sequence Diagram](https://github.com/pbg0205/concert/issues/4)

### 1. 유저 토큰 발급

```mermaid
sequenceDiagram
    actor client
    participant queue as queue
    
    client ->> queue: 토큰 등록 요청
    queue ->> user: 회원 가입 사용자 검증
    user -->> queue: 회원 가입 사용자 검증 완료
    alt 토큰 존재X
        queue ->> queue: 토큰 생성
    else 토큰 존재O
        queue ->> queue: 대기열 토큰 반환
    end
    queue -->> client: 토큰 반환
```

### 2. 예약 가능 날짜 조회

```mermaid
sequenceDiagram
    actor client
    
    client ->> queue: 예약 가능 날짜 조회 요청
    queue ->> reservation: 예약 가능 날짜 목록 조회
    reservation -->> client: 예약 가능 날짜 목록 조회
```

### 3. 예약 가능 날짜 좌석 조회

```mermaid
sequenceDiagram
    actor client
    
    client ->> queue: 예약 가능 날짜 조회 요청
    queue ->> reservation: 예약 가능 날짜 좌석 목록 조회
    reservation -->> client: 예약 가능 날짜 좌석 목록 조회
```


### 4. 좌석 예약 요청

```mermaid
sequenceDiagram
    actor client
    
    client ->> queue: 예약 가능 날짜 조회 요청
    queue ->> reservation: 좌석 예약 요청
    reservation ->> payment: 결제 식별자 생성 요청
    payment -->> reservation: 결제 식별자 생성 응답
    payment -->> payment: 결제 히스토리 저장
    reservation -->> reservation: 예약 히스토리 저장
    reservation -->> queue: 예약 가능 좌석 요청
    queue -->> client: 좌석 예약 요청 완료
```

### 5. 사용자 포인트 충전

```mermaid
sequenceDiagram
    actor client
    
    client ->> payment(user-point): 예약 가능 날짜 조회 요청
    payment(user-point) -->> client: 사용자 포인트 충전 완료
```

### 6. 결제 요청

```mermaid
sequenceDiagram
    actor client
    
    client ->> queue: 예약 가능 날짜 조회 요청
    queue ->> payment: 결제 요청
    payment ->> reservation: 예약 완료 요청
    reservation -->> reservation: 예약 히스토리 저장
    payment -->> payment: 결제 히스토리 저장
    reservation -->> queue: 예약 완료 응답
    queue -->> client: 결제 완료 반환
```
