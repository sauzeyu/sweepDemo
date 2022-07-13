import request from '@/utils/downloadRequest';

export async function downloadExcel(params) {
  return request.post('/dkmKeyLogHistoryExport/downloadExcel', params);
}
