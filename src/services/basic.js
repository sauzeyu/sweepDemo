import request from '../utils/request';

// 获取省市区信息
export async function getAreas(params) {
  return request.get('areas', { params });
}
