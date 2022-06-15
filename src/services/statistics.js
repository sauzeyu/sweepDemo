import request from '@/utils/request';

export async function selectTotal() {
  return request.get('dkmStatistics/selectTotal');
}
export async function selectKeyLogByMonth() {
  return request.get('dkmStatistics/selectKeyLogByMonth');
}
export async function selectKeyUseLogByTime(params) {
  return request.get('dkmStatistics/selectKeyUseLogByTime', { params });
}
export async function selectKeyErrorLogByTime(params) {
  return request.get('dkmStatistics/selectKeyErrorLogByTime', { params });
}
