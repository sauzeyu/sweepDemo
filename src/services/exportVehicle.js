import request from '@/utils/downloadRequest';

export async function exportVehicle(params) {
  return request.post('/dkmVehicle/downloadDkmVehicle', params);
}
