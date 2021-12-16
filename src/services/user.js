import request from '../utils/request';
import { PermissionsUtil, joinPath } from '../utils';
import { getPublicPath } from '@/utils';
import menu from '@/models/menu.js';
import Cookies from 'js-cookie';

export async function login(params) {
  return request.post('/dkserver/login', params).then(decorateUserInfo);
}

export async function logout(params) {
  // return request.post('signOut', params);
  // window.location.href = '/user/login';
  localStorage.removeItem('user-key');
  Cookies.remove('user-key');
  return null;
}

export async function modifyPassword(params) {
  return request.post('modifyPwd', params);
}

function decorateUserInfo(data) {
  const userInfo = {
    ...data.user,
    token: data.token,
  };
  userInfo.avatar = getPublicPath('/img/avatar.png');
  data.menus = menu;
  const menus = PermissionsUtil.structureByDNA(data.menus);
  _initMenus(menus);
  userInfo.menus = menus;
  return userInfo;
}

function _initMenus(menus, parent) {
  for (let i = 0; i < menus.length; i++) {
    let menu = menus[i];
    // if(menu.status === 2)continue;
    extendAttributes(menu);
    if (menu.children) {
      _initMenus(menu.children, menu);
    }
  }

  function extendAttributes(menu) {
    menu.isDir = menu.type == 2;
    menu.isMenu = menu.type == 0;
    menu.isAction = menu.type == 1;
    menu.route = parent
      ? joinPath('/', parent.route, menu.href)
      : joinPath('/', menu.href);
    menu.id = menu.id || Date.now();
  }
}
