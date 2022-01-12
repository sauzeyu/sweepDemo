import request from '@/utils/request';
// 获取使用日志信息
export async function getUseLogList(params) {
  return request.get('useLog/selectForPage', { params });
}
// 获取异常日志信息
export async function getErrorLogList(params) {
  return request.get('exceptionLog/selectForPage', { params });
}
