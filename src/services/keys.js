import request from '@/utils/request';

// 获取钥匙信息
export async function getKeysList(params) {
  return request.get('dkmKey/selectForPage', { params });
}

// 获取钥匙使用日志
export async function getKeysLogsList(params) {
  return request.get('dkmKeyLog/selectForPage', { params });
}

// 启用停用钥匙
export async function enableKey(params) {
  return request.post('dkmKey/updateStateById?', params);
}

// 吊销钥匙
export async function revokeKey(data) {
  return request.post('dkmKey/updateStateForRevokeById?id=' + data.id);
}

//通过id查询用户信息
export async function selectUserById(id) {
  return request.get('dkmUser/selectById', { params: { id: id } });
}

//通过id查询车辆信息
export async function selectVehicleById(id) {
  return request.get('dkmVehicle/selectById', { params: { id: id } });
}

export async function getKeyErrorLogList(params) {
  return request.get('dkmKeyErrorLog/selectForPage', { params });
}
