import { getCaptcha, login, modifyPassword } from '../services/user';
import { getPublicPath, md5 } from '@/utils';
import { history } from 'umi';
import pathToRegexp from 'path-to-regexp';
import Cookies from 'js-cookie';
import { logout } from '@/services/user';

// 用户存储cookie区域， 用户实际信息存储在localStorage内，在cookie内保存一个key来对应storage里面的内容
class UserCookieStore {
  storePrefix = '';
  userMarkPrefix = '@user_';
  Storage = localStorage;
  cookieKey = 'user-key';
  constructor(storePrefix) {
    /* 初始化时值为ssc */
    this.storePrefix = storePrefix;
  }
  set(data) {
    this.clear();
    if (!data) return;
    const storeKey = this.getRealKey() + Date.now();
    this.Storage.setItem(storeKey, window.JSON.stringify(data));
    Cookies.set(this.cookieKey, storeKey, { path: '/' });
  }
  get() {
    const storeKey = Cookies.get(this.cookieKey);
    if (!storeKey) return null;
    let user = this.Storage.getItem(storeKey);
    if (!user) return null;
    return window.JSON.parse(user);
  }
  getRealKey() {
    return this.storePrefix + this.userMarkPrefix;
  }
  clear() {
    const regx = new RegExp('^(' + this.getRealKey() + ')');
    for (let i = 0; i < this.Storage.length; i++) {
      const key = this.Storage.key(i);
      if (regx.test(key)) {
        this.Storage.removeItem(key);
      }
    }
  }
}

/**
 *
 */
const userCookieStore = new UserCookieStore('ssc');
const localUser = getUserFromStore();
/**
 * namespace 表示在全局 state 上的 key
 * state 是初始值，在这里是空数组
 * reducers 等同于 redux 里的 reducer，接收 action，同步更新 state
 */
export default {
  namespace: 'user',
  state: {
    currentUser: localUser,
  },
  effects: {
    /**
     * payload: 提交的表单数据
     * cal:     回调函数  执行异步函数
     * put:     发出一个 Action，类似于 dispatch
     * select:  用于从 state 里获取数据。
     */
    *login({ payload }, { call, put }) {
      // md5 加密密码
      payload.password = SignPassword(payload.password);
      // 调用 services的login 函数 目的:    携带提交的字段发起请求
      const response = yield call(login, payload); // 等待响应结果 赋值给 response变量
      const AuthUser = extUserAuthMap(response);
      yield put({
        type: 'changeStatus',
        payload: {
          currentUser: AuthUser,
        },
      });
      return AuthUser;
    },
    *logout({ payload }, { put, call, select }) {
      const id = yield select((state) => state.user.currentUser.id);
      yield call(logout, { id });
      yield put({
        type: 'changeStatus',
        payload: {
          currentUser: null,
        },
      });
      let query = {};
      if (payload && payload.takeRouteInfo) {
        query.r = window.location.href;
      }
      history.push({
        pathname: '/user/login',
        query,
      });
    },
    *modifyPassword({ payload }, { call, select }) {
      const params = payload.params;
      params.id = yield select((state) => state.user.currentUser.id);
      params.oldPwd = SignPassword(params.password);
      params.newPwd = SignPassword(params.new_password);
      yield call(modifyPassword, params);
    },
  },

  reducers: {
    changeStatus(state, { payload }) {
      if ('currentUser' in payload) storeUser(payload.currentUser);
      return {
        ...state,
        ...payload,
      };
    },
  },
};

export const SignPassword = (password) => {
  return md5(password);
};

function storeUser(user) {
  console.log('store-user', user);
  if (user) {
    user = { ...user };
    delete user.routeMap;
    delete user.dnaMap;
    userCookieStore.set(user);
  } else {
    userCookieStore.clear();
  }
}

function getUserFromStore() {
  const user = userCookieStore.get();
  if (!user) return null;
  return extUserAuthMap(user);
}

function extUserAuthMap(user) {
  // user是登陆请求获取的结果
  const routeMap = {},
    dnaMap = {};
  const menus = user.menus || [];
  // 处理菜单 将嵌套格式的router扁平化
  /* 例如: 
    { route: /parts,
      children:[ 
          {route:/parts/1,children:[{route:/parts/1/1.1}]},
      ]
    } 
    ==>
    /parts: { children:[  {route:/parts/1,children:[{route:/parts/1/1.1}]}] },
    /parts/1: { children:[{route:/parts/1/1.1}] },
    /parts/1/1.1: {},
    */
  walkMenu(menus);

  function walkMenu(menus) {
    // 遍历menus
    menus.map((menu) => {
      routeMap[menu.route] = menu;
      dnaMap[menu.dna] = menu;
      menu.pathRegexp = pathToRegexp(menu.route);
      if (menu.children) {
        walkMenu(menu.children);
      }
    });
  }
  user.routeMap = routeMap;
  user.dnaMap = dnaMap;
  user.avatar = createAvatar(user.name || '');
  return user;
}

// 根据字符串生成头像
export function createAvatar(name) {
  let avatar = {
    src: getPublicPath('/img/avatar.png'),
    name,
  };
  let nameChar = '';
  if (/.*[\u4e00-\u9fa5]+.*$/.test(name)) {
    //如果是中文名字，取后面最多两位
    nameChar = name.substr(name.length > 2 ? name.length - 2 : name.length - 1);
  } else {
    // 英文名字取前面2位
    nameChar = name.substr(0, 2);
  }
  if (nameChar) {
    avatar = {
      content: nameChar,
      bgColor: getColorByName(name),
      name,
    };
  }
  return avatar;
  function getColorByName(name) {
    if (!name) return '#cccccc';
    let str = '';
    for (let i = 0; i < name.length; i++) {
      str += parseInt(name[i].charCodeAt(0), 10).toString(16);
    }
    const color = str.slice(1, 4);
    if (color.length < 3) return '#c5c5c5';
    return '#' + color;
  }
}
