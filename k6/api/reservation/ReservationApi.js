import http from 'k6/http';

// 토큰을 발급하는 함수
export function reservation(token, seatId) {
  let url = 'http://localhost:8080/api/concert/seats/reservation';

  let requestHeader = {
    'Content-Type': 'application/json',
    'QUEUE-TOKEN' : token
  };

  let requestBody = JSON.stringify({
    seatId: seatId,
  });

  return http.post(url, requestBody, {headers: requestHeader});
}
