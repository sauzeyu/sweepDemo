import request from '@/utils/downloadRequest';

export async function exportBluetooth(params) {
  return request.post('/dkmBluetooths/downloadDkmBluetooths', params);
}
