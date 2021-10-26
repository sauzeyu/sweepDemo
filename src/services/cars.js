import request from '@/utils/request';
//获取车型列表
export async function getCarModes(params) {
  return request.get('DKLinkvehicle/api/vehicleModelList', { params });
}

// 获取汽车列表
export async function getVehicles(params) {
  return request.get('DKLinkvehicle/api/vehicleList', { params });
}
