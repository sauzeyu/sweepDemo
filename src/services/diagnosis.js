import request from '@/utils/request';

export async function selectRecentLogs(params) {
  return request.get('/dkmStatistics/selectRecentLogs', { params });
}

export async function selectForPage(params) {
  return request.get('/dkmStatistics/selectForPage', { params });
}

export async function selectBusiness(params) {
  return request.get('/dkmStatistics/selectBusiness', { params });
}

export async function selectTodayLogsCount(params) {
  return request.get('/dkmStatistics/selectTodayLogsCount', { params });
}

export async function selectFaultLogs(params) {
  return request.get('/dkmStatistics/selectFaultLogs', { params });
}

export async function selectPhoneBrandLogs(params) {
  return request.get('/dkmStatistics/selectPhoneBrandLogs', { params });
}
