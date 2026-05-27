import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    vus: 300,
    duration: '30s',
    thresholds: {
      http_req_duration: ['p(99)<500'],
      http_req_failed: ['rate<0.01']
    }
};

export default function () {
  const res = http.get(
    'http://localhost:8080/api/v1/flights/calendar?origin=DEL&destination=BKK&month=2026-05&currency=MYR',
    {
      timeout: '30s',
      headers: {
        Accept: 'application/json',
      },
    }
  );

  if (res) {
    console.log(` STATUS=${res.status}  >>>>>>>> BODY=${res.body} >>>>>> ERROR=${res.error}`);
  }

  sleep(0.01);
}