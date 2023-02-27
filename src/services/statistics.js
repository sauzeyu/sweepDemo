import request from '@/utils/request';

export async function selectTotal() {
  return request.get('dkmStatistics/selectVehicleAndKeyAndKeyLogTotal');
}
export async function keyStatistics() {
  return request.get('dkmStatistics/keyStatistics');
}
export async function keyUseTimeStatistics() {
  return request.get('dkmStatistics/keyUseTimeStatistics');
}
export async function keyErrorTimeStatistics() {
  return request.get('dkmStatistics/keyErrorTimeStatistics');
}
export async function vehicleStatistics() {
  return request.get('dkmStatistics/vehicleStatistics');
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

export async function selectKeyErrorLogByAllPhoneBrand() {
  return request.get('dkmStatistics/selectKeyErrorLogByAllPhoneBrand');
}
export async function selectKeyErrorLogByPhoneBrand(params) {
  return request.get('dkmStatistics/selectKeyErrorLogByPhoneBrand', { params });
}

export async function selectForExpiration() {
  return request.get('dkmSystemConfigurationExpired/selectForExpiration');
}
