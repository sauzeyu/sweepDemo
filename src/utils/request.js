import axios from 'axios';
import { joinPath } from '@/utils/index';
import { getDvaApp } from 'umi';
const baseURL = '/dkserver-back';

function createDefaultRequest() {
  const instance = axios.create({
    baseURL,
    timeout: 1000 * 60 * 10,
  });
  instance.interceptors.request.use((config) => {
    const state = getDvaApp()._store.getState();
    if (state.user && state.user.currentUser && state.user.currentUser.token) {
      config.headers['dkserver-user'] = state.user.currentUser.token;
      config.headers['token'] = 'vecentek';
    }
    return config;
  });
  instance.interceptors.response.use(
    (response) => {
      const data = response.data;
      if (response.status === 401) {
        // window.location.href = '/user/login';
      }
      if (data.success === false || data.success === 0) {
        return Promise.reject({
          message: data.message || '未知错误',
          code: data.code,
          response,
          data,
          type: 'RequestError',
        });
      }
      return response.data;
    },
    (r) => {
      let resultError = { response: r };
      if (r.response) {
        const response = r.response;
        if (typeof response.data === 'object') {
          resultError = {
            response,
            code: response.data.errorCode,
            message: response.data.errorMsg || response.data.message,
            requestId: response.data.requestId,
          };
          if (response.data.status === 401) {
            getDvaApp()._store.dispatch({
              type: 'user/logout',
              payload: {
                takeRouteInfo: true,
              },
            });
          }
        } else {
          const status = response.status;
          if (status === 401) {
            getDvaApp()._store.dispatch({
              type: 'user/logout',
              payload: {
                takeRouteInfo: true,
              },
            });
          }
          resultError = {
            message: `[${status}]${response.statusText}`,
            code: status,
          };
        }
        resultError.response = response;
      }
      resultError.type = 'RequestError';
      return Promise.reject(resultError);
    },
  );
  instance.getAbsUrl = function (url) {
    return joinPath(baseURL, url);
  };
  return instance;
}

export default createDefaultRequest();
