import { check, sleep } from 'k6';
import http from 'k6/http';

// CSV 파일을 읽고 파싱하는 함수
function loadCSV() {
  const file = open('../user_alt_id.csv');  // CSV 파일을 읽기
  const lines = file.split('\n');  // 각 줄을 구분자로 나누기
  const data = [];

  // 첫 번째 줄은 헤더이므로 건너뛰고 나머지 데이터 처리
  for (let i = 1; i < lines.length; i++) {
    const line = lines[i].trim();  // 빈 줄을 처리하기 위해 trim() 사용
    if (line) {
      const fields = line.split(',');  // 쉼표로 구분된 항목을 배열로 변환
      data.push({
        alt_id: fields[0],  // 첫 번째 열은 alt_id
      });
    }
  }
  return data;  // 데이터를 반환
}

// 사용자 데이터를 로드
const altIds = loadCSV();

// k6 부하 테스트 옵션 설정
export let options = {
  rps: 10,
  iterations: 100,
  vus: 100,
  duration: '10s',
};

export default function () {
  const userIndex = __VU - 1;
  const user = altIds[userIndex];

  const url = 'http://localhost:8080/api/queue/token/issue';

  // 요청 바디 준비 (alt_id를 요청 바디에 포함)
  const payload = JSON.stringify({
    userId: user.alt_id,
  });

  // HTTP POST 요청 보내기
  const response = http.post(url, payload, {
    headers: { 'Content-Type': 'application/json' },
  });

  // 응답 체크 (상태 코드 200)
  check(response, {
    'status code == 200': (r) => r.status === 200,
  });

}
