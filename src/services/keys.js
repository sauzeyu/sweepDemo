import request from '@/utils/request';

// 获取省市区信息
export async function getKeysList(params) {
  return request.get('DKLinkkey/api/keyList', { params });
}
