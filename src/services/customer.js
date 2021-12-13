import request from '@/utils/request';

//获取客户列表
export async function getCustomerList(params) {
  return request.get('dkmUser/selectForPage', { params });
}

export async function stopDkUser(id) {
  return request.post('dkmUser/updateValidById?id=' + id);
}

//新增客户
export async function addDkCustomer(data) {
  return request.post('dkmUser/insert', data);
}

//修改客户
export async function editDkCustomer(data) {
  return request.post('dkmUser/updateById', data);
}

//删除客户
export async function deleteDkCustomer(data) {
  return request.post('DKLinkUser/api/deleteUser', data);
}

//根据用户ID查询所有车辆
export async function vehicleListById(params) {
  return request.get('dkmVehicle/selectForPageByUserId', { params });
}

//根据用户ID查询所有钥匙
export async function keyListByUserId(params) {
  return request.get('dkmKey/selectForPageByUserId', { params });
}

//根据用户id查车辆
// export async function getCustomerList(params) {
//   return request.get('DKLinkUser/api/userList', { params });
// }
