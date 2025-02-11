import {check, sleep} from 'k6';
import {issueQueueToken} from '../api/queue/QueueTokenIssueApi.js';
import {getAvailableSeatsResBodyJson} from "../api/concert/ConcertApi.js"; // token.js에서 getToken 함수 불러오기

export default function () {
  // 토큰 발급
  let queueTokenResponse = issueQueueToken('0194c534-f4b5-7c63-bfd5-ae9f34f04d01');

  let queueToken = JSON.parse(queueTokenResponse.body).data.token;

  sleep(10);

  // 첫 번째 API 호출
  let response = getAvailableSeatsResBodyJson(queueToken, 1, 1);
  check(response, { 'status code 200': (r) => r.status === 200 });
}
