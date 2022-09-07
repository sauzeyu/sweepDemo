import axios from 'axios';
import { getDvaApp } from '@@/plugin-dva/exports';
import { joinPath } from '@/utils/index';

// Excel 导出时 axios 不能配置拦截器,所以需要单独配置
function createDefaultRequest() {
  const instance = axios.create({
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
  instance.getAbsUrl = function (url) {
    return joinPath('', url);
  };
  return instance;
}

export default createDefaultRequest();
