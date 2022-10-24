import request from '@/utils/request';

//获取车型列表
export async function getCarModes(params) {
  return request.get('dkmModel/selectAll', { params });
}

//新增车型
export async function addCarModes(data) {
  return request.post('dkmModel/insert', data);
}

//修改车型
export async function updateCarModes(data) {
  return request.post('dkmModel/updateById', data);
}

// 分页查询蓝牙设备
export async function getBluetooth(params) {
  return request.get('dkmBluetooths/selectForPage', { params });
}

// 删除蓝牙设备
export async function delBluetooth(hwDeviceSn) {
  return request.post('dkmBluetooths/deleteById?hwDeviceSn=' + hwDeviceSn);
}

// 获取汽车列表
export async function getVehicles(params) {
  return request.get('dkmVehicle/selectForPage', { params });
}

// 删除汽车
// export async function deleteVehicle(params) {
//   return request.post('DKLinkVehicle/api/delete', params);
// }
// 编辑车辆
export async function updateVehicle(params) {
  return request.post('dkmVehicle/updateById', params);
}

// 添加车辆
export async function addVehicle(params) {
  return request.post('dkmVehicle/insert', params);
}

// 通过车主电话查询用户信息
export async function selectUserByVehicleId(params) {
  return request.get('dkmVehicle/selectUserByVehicleId', { params });
}

// 通过车辆 id 分页查询钥匙列表
export async function keyListById(params) {
  return request.get('dkmKey/selectForPageByVehicleId', { params });
}

// 停用/启用车辆
export async function stopVehicle(id) {
  return request.post('dkmVehicle/updateStateById?id=' + id);
}

// 吊销车辆
export async function discardVehicle(id) {
  return request.post('dkmVehicle/updateValidById?id=' + id);
}

// 获取手机标定数据列表
export async function getPhone(params) {
  return request.get('dkmPhoneCalibrationData/selectForPage', { params });
}

// 获取车辆标定数据列表
export async function getVehicle(params) {
  return request.get('dkmVehicleCalibrationData/selectForPage', { params });
}

// 修改手机标定数据列表
export async function updatePhone(params) {
  return request.post('dkmPhoneCalibrationData/updateById', params);
}

// 通过车辆 id 分页查询钥匙列表
export async function keyLifecycleList(params) {
  return request.get('dkmKeyLifecycle/selectForPageByKeyId', { params });
}
// 通过车辆 id 分页查询钥匙列表
export async function keyUseListById(params) {
  return request.get('dkmKeyUseLog/selectForPage', { params });
}
