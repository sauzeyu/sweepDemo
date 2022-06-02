import request from '@/utils/request';
// 获取用户总数
export async function selectTotalUser() {
  return request.get('dkmStatistics/selectUserTotal');
}
// 获取车辆总数
export async function selectTotalVehicle() {
  return request.get('dkmStatistics/selectVehicleTotal');
}
// 获取钥匙总数
export async function selectTotalKey() {
  return request.get('dkmStatistics/selectKeyTotal');
}
// 获取昨日新增人数
export async function selectYesterdayRegisterUser() {
  return request.get('dkmStatistics/selectYesterdayRegisterUserTotal');
}
// 获取昨日新增钥匙数量
export async function selectYesterdayRegisterKey() {
  return request.get('dkmStatistics/selectYesterdayRegisterKey');
}
// 获取车辆工厂分布图列表
export async function selectVehicleByFactory() {
  return request.get('dkmStatistics/selectVehicleByFactory');
}
// 查询最近七日用户-钥匙-车辆新增数据
export async function selectLastWeekTotal() {
  return request.get('dkmStatistics/selectLastWeekTotal');
}

export async function selectTotal() {
  return request.get('dkmStatistics/selectTotal');
}
export async function selectKeyLogByMonth() {
  return request.get('dkmStatistics/selectKeyLogByMonth');
}
