import request from '@/utils/request';
//获取车型列表
export async function getCarModes(params) {
  return request.get('DKLinkvehicle/api/vehicleModelList', { params });
}

//新增车型
export async function addCarModes(data) {
  return request.post('DKLinkvehicle/api/addModel', data);
}
//修改车型
export async function updateCarModes(data) {
  return request.post(`DKLinkvehicle/api/editModel`, data);
}
//删除车型
export async function delCarModes(id) {
  return request.post(`DKLinkvehicle/api/delete`, [{ id }]);
}

// 获取汽车列表
export async function getVehicles(params) {
  return request.get('DKLinkvehicle/api/vehicleList', { params });
}
// 删除汽车列表
export async function deleteVehicle(params) {
  return request.get('DKLinkvehicle/api/deleteModel', { params });
}
