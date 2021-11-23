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

//删除车型
export async function delModeBatch(params) {
  return request.post(`DKLinkvehicle/api/deleteModel`, params);
}

// 获取汽车列表
export async function getVehicles(params) {
  return request.get('DKLinkvehicle/api/vehicleList', { params });
}

// 删除汽车
export async function deleteVehicle(params) {
  return request.post('DKLinkvehicle/api/delete', params);
}

// 添加车辆
export async function addVehicle(params) {
  return request.post('DKLinkvehicle/api/add', params);
}

// 通过车辆ID查询用户(车主)
export async function userListByVehicleId({ id }) {
  return request.get(`DKLinkvehicle/api/${id}/userListByVehicleId`);
}

// 通过车辆ID查询车辆的钥匙(列表)
export async function keyListById(id) {
  return request.get(`DKLinkvehicle/api/${id}/keyListById`);
}

// 停用/启用车辆
export async function stopVehicle(id) {
  return request.post(`DKLinkvehicle/api/stopVehicle?id=` + id);
}

// 吊销车辆
export async function discardVehicle(id) {
  return request.post(`DKLinkvehicle/api/discardVehicle?id=` + id);
}

//根据用户ID查询车辆
export async function vehicleListById(params) {
  return request.get(
    '/DKLinkuser/api/' + params.id + '/vehicleListById',
    params,
  );
}
