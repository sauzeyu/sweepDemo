import request from '@/utils/downloadRequest';

export async function exportKeyUseLog(params) {
  return request.post('/dkmKeyLog/downloadKeyLogExcel', params);
}
export async function getAllStatusCode(params) {
  return request.get('/dkmKeyLog/selectAllCode', params);
}
