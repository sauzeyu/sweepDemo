import request from '@/utils/downloadRequest';

export async function exportKey(params) {
  return request.post('/dkmKey/downloadKeyExcel', params);
}
