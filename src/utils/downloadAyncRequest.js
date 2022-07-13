import axios from 'axios';
import { getDvaApp } from '@@/plugin-dva/exports';
import { joinPath } from '@/utils/index';

// Excel 导出时 axios 不能配置拦截器,所以需要单独配置
function createDefaultRequest() {
  const instance = axios.create({
    timeout: 1000 * 60 * 10,
    responseType: 'blob',
    headers: {
      'access-token': getDvaApp()._store.getState().user.currentUser.token,
    },
  });

  instance.getAbsUrl = function (url) {
    return joinPath('', url);
  };
  return instance;
}

export default createDefaultRequest();
