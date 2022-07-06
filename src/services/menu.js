import request from '@/utils/request';

export async function selectMenuTree() {
  return request.get('dkmMenu/selectAll');
}

export async function selectMenuByRoleId(roleId) {
  return request.get('dkmMenu/selectMenuByRoleId?id=' + roleId);
}

export async function selectForPage(params) {
  return request.get('dkmMenu/selectForPage', { params });
}
