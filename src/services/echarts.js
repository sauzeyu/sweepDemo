import request from '@/utils/request';
// 获取用户总数
export async function selectTotalUser() {
  return request.get('echarts/selectUserTotal');
}
// 获取车辆总数
export async function selectTotalVehicle() {
  return request.get('echarts/selectVehicleTotal');
}
// 获取钥匙总数
export async function selectTotalKey() {
  return request.get('echarts/selectKeyTotal');
}
// 获取昨日新增人数
export async function selectYesterdayRegisterUser() {
  return request.get('echarts/selectYesterdayRegisterUserTotal');
}
// 获取车辆工厂分布图列表
export async function selectVehicleByFactory() {
  return request.get('echarts/selectVehicleByFactory');
}
// 查询最近七日用户-钥匙-车辆新增数据
export async function selectLastWeekTotal() {
  return request.get('echarts/selectLastWeekTotal');
}
