import request from '@/utils/downloadRequest';

export async function exportReplacement(params) {
  return request.post(
    '/dkmAftermarketReplacement/downloadAftermarketReplacement',
    params,
  );
}
