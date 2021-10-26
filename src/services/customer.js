import request from '@/utils/request';
//获取客户列表
export async function getCustomerList(params) {
  return request.get('DKLinkuser/api/userList', { params });
}
