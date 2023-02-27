import request from '../utils/request';

export async function getLogs(params) {
  return request.get('sysLog', { params });
}

export async function auditLog(params) {
  return request.post('sysLog/audit?number=2000', params);
}

export async function putFile(params) {
  return request.post('sysLog/delete?number=2000', params);
}

export async function saveOrUpdateConfigExpired(params) {
  return request.post(
    'dkmSystemConfigurationExpired/saveOrUpdateConfigExpired',
    params,
  );
}

export async function selectForLast() {
  return request.get('dkmSystemConfigurationExpired/selectForLast');
}
