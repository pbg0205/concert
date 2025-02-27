import {check, sleep, group} from 'k6';
import {issueQueueToken} from "../api/queue/QueueTokenIssueApi.js";
import {loadCSV} from '../base/UserSampleReader.js';
import {getAvailableDates, getAvailableSeatsResBodyJson} from "../api/concert/ConcertApi.js";
import {reservation} from "../api/reservation/ReservationApi.js";
import {payment} from "../api/payment/PaymentApi.js";

// 사용자 데이터를 로드
const altIds = loadCSV();

// k6 부하 테스트 옵션 설정
export let options = {
  setupTimeout: '5m',
  scenarios: {
    constant_load: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '10s', target: 100 },
        { duration: '30s', target: 50 },
        { duration: '20s', target: 10 },
      ]
    },
  },
};

export function setup() {
  let tokens = [];

  for (let userIndex = 0; userIndex < 1000; userIndex++) {
    const user = altIds[userIndex];
    const userId = user.alt_id

    let queueTokenResponse = issueQueueToken(userId);
    let queueToken = JSON.parse(queueTokenResponse.body).data.token;

    if (queueTokenResponse.status === 200) {
      tokens.push(queueToken);
    }
  }

  sleep(10);

  console.log(`Generated ${tokens.length} tokens`);

  return tokens;
}


export default function (tokens) {
  const currentRequests = __VU * (__ITER + 1);

  const userIndex = currentRequests % altIds.length;
  const queueToken = tokens[userIndex];

  console.log("userIndex: " + userIndex);

  // (1) 콘서트 스케줄 조회하기
  group('콘서트 스케줄 조회 API', function () {
      let response = getAvailableDates(queueToken);
      // 응답 체크 (상태 코드 200)
      check(response, {
        '[concert schedule]status code == 200': (r) => r.status === 200,
      });
    }
  )

  // (2) )콘서트 좌석 조회하기
  group('콘서트 좌석 조회 API', function () {
      let response = getAvailableSeatsResBodyJson(queueToken, 1, 1);
      check(response, {
        '[concert seat]status code == 200': (r) => r.status === 200,
      });
    }
  )

  // (3) 좌석 예약하기
  const seatNumber = currentRequests % 100 + 1;
  const successfulReservationResponse = {};

  console.log("seatNumber: " + seatNumber);

  group('예약 API', function () {
    let response = reservation(queueToken, seatNumber);

    // 응답 체크 (상태 코드 200)
    if (response.status === 200) {
      let reservationData = JSON.parse(response.body).data;

      successfulReservationResponse.queueToken = queueToken;
      successfulReservationResponse.paymentKey = reservationData.paymentId;

      check(response, {
        '[reservation] status code == 200': (r) => r.status === 200,
      });
    }

  })

  // (4) 결제하기
  group('결제 API', function () {
    if (Object.keys(successfulReservationResponse).length === 0) {
      return;
    }

    let response = payment(successfulReservationResponse.queueToken, successfulReservationResponse.paymentKey);

    check(response, {
      '[payment] status code == 200': (r) => r.status === 200,
    });
  })
};
