import request from '@/utils/request';

//获取管理员列表
export async function getAdminList(params) {
  return request.get('dkmAdmin/selectForPage', { params });
}

export async function getAdminRoleNameById(id) {
  return request.get('dkmAdmin/selectRoleNameListById', { params: { id: id } });
}
export async function getAllRole() {
  return request.get('dkmAdmin/selectAllRole');
}
export async function updateAdminById(params) {
  return request.post('dkmAdmin/updateAdminById', params);
}
export async function deleteById(id) {
  return request.post('dkmAdmin/deleteById?id=' + id);
}
