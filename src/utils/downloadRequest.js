import axios from 'axios';
import { getDvaApp } from '@@/plugin-dva/exports';
import { joinPath } from '@/utils/index';
import { message } from '_antd@4.21.5@antd';

const baseURL = '/dkserver-back';

// Excel 导出时 axios 不能配置拦截器,所以需要单独配置
function createDefaultRequest() {
  const instance = axios.create({
    baseURL,
    timeout: 1000 * 60 * 10,
    responseType: 'blob',
  });

  instance.interceptors.request.use((config) => {
    const state = getDvaApp()._store.getState();
    if (state.user && state.user.currentUser && state.user.currentUser.token) {
      config.headers['access-token'] = state.user.currentUser.token;
    }
    return config;
  });
  instance.interceptors.response.use(
    (response) => {
      const data = response.data;

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
      message.config({
        maxCount: 1,
      });
      let resultError = { response: r };
      if (r.response) {
        const response = r.response;

        if (response.status === 401 && response.data.code === 9100) {
          console.log('------before-----');
          message.error('登录状态已过期，请重新登录！');
          getDvaApp()._store.dispatch({
            type: 'user/logout',
            payload: {
              takeRouteInfo: true,
            },
          });
        }
        console.log(response);
        console.log('response.data ', response.data);
        if (response.data instanceof Blob) {
          if (response.status === 401) {
            console.log('------after-----');
            message.warn('登录状态已过期，请重新登录！');
            getDvaApp()._store.dispatch({
              type: 'user/logout',
              payload: {
                takeRouteInfo: true,
              },
            });
          }
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
