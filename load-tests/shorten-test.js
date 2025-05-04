// load-test.js
import http from 'k6/http';
import { check } from 'k6';

export const options = {
  scenarios: {
    constant_request_rate: {
      executor: 'constant-arrival-rate',
      rate: 83, // 5000 RPM
      timeUnit: '1s',
      duration: '1m',
      preAllocatedVUs: 100,
      maxVUs: 200,
    },
  },
};

export default function () {
  const res = http.post('https://urlshortener-a9ft.onrender.com/shorten', JSON.stringify({
    longUrl: 'https://example.com'
  }), {
    headers: { 'Content-Type': 'application/json' },
  });

  check(res, {
    'status is 201': (r) => r.status === 201,
  });
}
