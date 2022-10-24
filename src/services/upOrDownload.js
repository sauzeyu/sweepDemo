import request from '@/utils/downloadRequest';

export async function exportPhoneCalibration(params) {
  return request.post(
    '/dkmPhoneCalibrationData/downloadCalibrationExcel',
    params,
  );
}

export async function exportVehicleCalibration(params) {
  return request.post(
    '/dkmVehicleCalibrationData/downloadCalibrationExcel',
    params,
  );
}
