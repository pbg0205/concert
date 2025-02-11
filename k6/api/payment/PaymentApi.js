import http from 'k6/http';

// 토큰을 발급하는 함수
export function payment(token, paymentId) {
  let url = 'http://localhost:8080/api/payments';

  let requestHeader = {
    'Content-Type': 'application/json',
    'QUEUE-TOKEN' : token
  };

  let requestBody = JSON.stringify({
    paymentId: paymentId
  });

  return http.post(url, requestBody, {headers: requestHeader});
}
