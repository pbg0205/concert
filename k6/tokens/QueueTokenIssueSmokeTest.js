import http from 'k6/http';

// 토큰을 발급하는 함수
export function getToken() {
  let res = http.post('http://localhost:8080/api/queue/token/issue', JSON.stringify({
    userId: '0194c534-f4b5-7c63-bfd5-ae9f34f04d01',
  }), {
    headers: { 'Content-Type': 'application/json' }
  });

  // 응답 본문을 JSON으로 파싱
  let body = JSON.parse(res.body);

  // token_id 추출 및 반환
  return body.data.token;
}
