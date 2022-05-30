import request from '@/utils/downloadRequest';

//获取管理员列表
export async function exportPhoneCalibration(params) {
  return request.post(
    '/dkmPhoneCalibrationData/downloadCalibrationExcel',
    params,
  );
}
