import request from '@/utils/request';

//获取客户列表
export async function getBluetoothList(params) {
  return request.get('dkmAftermarketReplacement/selectForPage', { params });
}
export async function selectAftermarketReplacementByVin(params) {
  return request.get('dkmAftermarketReplacement/selectByVin', { params });
}
export async function selectVehicleByVin(params) {
  return request.get('dkmAftermarketReplacement/selectVehicleByVin', {
    params,
  });
}
