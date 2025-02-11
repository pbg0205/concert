import http from 'k6/http';

export function getAvailableSeatsResBodyJson(token, concertId, concertScheduleId) {
  let headers = {
    'QUEUE-TOKEN': token,
    'Content-Type': 'application/json',
  };

  const url = `http://localhost:8080/api/concert/${concertId}/concertSchedule/${concertScheduleId}/seats`;
  return http.get(url, {headers});
}

export function getAvailableDates(token) {
  let url = 'http://localhost:8080/api/concert/1/available-dates?page=1';

  let requestHeaders = {
    'QUEUE-TOKEN': `${token}`,
    'Content-Type': 'application/json',
  };

  return http.get(url, {headers : requestHeaders});
}
