import request from '@/utils/request';
//获取客户列表
export async function getCustomerList(params) {
  return request.get('DKLinkuser/api/userList', { params });
}

//根据用户id查车辆
// export async function getCustomerList(params) {
//   return request.get('DKLinkuser/api/userList', { params });
// }
