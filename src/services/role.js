import request from '@/utils/request';

//获取管理员列表
export async function getRoleList(params) {
  return request.get('dkmRole/selectForPage', { params });
}

export async function updateRoleById(role) {
  return request.post('dkmRole/updateRoleById', role);
}

export async function deleteById(id) {
  return request.post('dkmRole/deleteById?id=' + id);
}
