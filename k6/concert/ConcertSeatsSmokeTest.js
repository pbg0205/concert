import http from 'k6/http';
import {check, sleep} from 'k6';
import { getToken } from '../tokens/QueueTokenIssueSmokeTest.js'; // token.js에서 getToken 함수 불러오기

export default function () {
  // 토큰 발급
  let token = getToken();

  sleep(10);

  // 첫 번째 API 호출
  let res1 = getAvailableDates(token);
  check(res1, { 'status code 200': (r) => r.status === 200 });

}

function getAvailableDates(token) {
  let headers = {
    'QUEUE-TOKEN': `${token}`,
    'Content-Type': 'application/json',
  };

  return http.get('http://localhost:8080/api/concert/1/seats', {headers});
}
