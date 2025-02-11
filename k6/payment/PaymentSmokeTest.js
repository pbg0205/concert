import {check, sleep} from 'k6';
import {reservation} from "../api/reservation/ReservationApi.js";
import {payment} from "../api/payment/PaymentApi.js";
import {issueQueueToken} from "../api/queue/QueueTokenIssueApi.js";

export default function() {
  // 대기열 토큰 발급
  let queueTokenResponse = issueQueueToken('0194c534-f4b5-7c63-bfd5-ae9f34f04d01');
  let queueToken = JSON.parse(queueTokenResponse.body).data.token;

  sleep(10);

  let reservationResponse = reservation(queueToken, 1);
  let paymentId = JSON.parse(reservationResponse.body).data.paymentId;

  sleep(1);

  let response = payment(queueToken, paymentId);
  check(response, { 'status code 200': (r) => r.status === 200 });
}
