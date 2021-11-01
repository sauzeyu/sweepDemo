import request from '@/utils/request';

// 获取钥匙信息
export async function getKeysList(params) {
  return request.get('DKLinkkey/api/keyList', { params });
}
// 获取钥匙使用日志
export async function getKeysLogsList(params) {
  return request.get('DKLinkkey/api/keyLogList', { params });
}
// 启用停用钥匙
export async function enableKey(params) {
  return request.get('DKLinkkey/api/enableKey', { params });
}
// 启用停用钥匙
export async function revokeKey(params) {
  return request.get('DKLinkkey/api/revokeKey', { params });
}
