import http from 'k6/http';

// 대기열 토큰 발급
export function issueQueueToken(useId) {
  let url = 'http://localhost:8080/api/queue/token/issue';

  let requestHeader = {'Content-Type': 'application/json'};

  let requestBody = JSON.stringify({
    userId: useId,
  });

  return http.post(url, requestBody, {headers : requestHeader});
}
