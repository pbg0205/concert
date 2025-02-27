import http from 'k6/http';
import {check, sleep} from 'k6';
import { issueQueueToken } from '../api/queue/QueueTokenIssueApi.js';
import {getAvailableDates} from "../api/concert/ConcertApi.js";

export default function () {
  // 대기열 토큰 발급
  let queueTokenResponse = issueQueueToken('0194c534-f4b5-7c63-bfd5-ae9f34f04d01');
  let queueToken = JSON.parse(queueTokenResponse.body).data.token;

  sleep(10);

  // 첫 번째 API 호출
  let response = getAvailableDates(queueToken);
  check(response, { 'status code 200': (r) => r.status === 200 });
}
