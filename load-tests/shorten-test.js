import http from 'k6/http';
import { check, group } from 'k6';

export const options = {
  scenarios: {
    constant_request_rate: {
      executor: 'constant-arrival-rate',
      rate: 84, // 5000 RPM
      timeUnit: '1s',
      duration: '1m',
      preAllocatedVUs: 100,
      maxVUs: 200,
    },
  },
};

export function setup() {
  const payload = JSON.stringify({ longUrl: 'https://example.com' });
  const headers = { 'Content-Type': 'application/json' };
  const res = http.post('http://localhost:8080/shorten', payload, { headers });

  check(res, {
    'shortCode creado': (r) => r.status === 201,
  });

  const body = res.json();
  return { shortCode: body.shortCode };
}

export default function (data) {
  const shortCode = data.shortCode;
  const BASE_URL = 'http://localhost:8080';

  group('GET /{shortCode}', () => {
    const res = http.get(`${BASE_URL}/${shortCode}`, { redirects: 0 });
    check(res, { 'status is 302': (r) => r.status === 302 });
  });

  group('PUT /shorten/{shortCode}', () => {
    const res = http.put(`${BASE_URL}/shorten/${shortCode}`, JSON.stringify({
      longUrl: 'https://example.com/updated',
      isActive: true,
    }), {
      headers: { 'Content-Type': 'application/json' },
    });
    check(res, { 'status is 204': (r) => r.status === 204 });
  });

  group('GET /{shortCode}/stats', () => {
    const res = http.get(`${BASE_URL}/${shortCode}/stats`);
    check(res, { 'status is 200': (r) => r.status === 200 });
  });
}
