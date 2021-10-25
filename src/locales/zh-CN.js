import app from './zh-CN/app';
import route from './zh-CN/route';
import validator from './zh-CN/validator';
import common from './zh-CN/common';
import component from './zh-CN/component';
import modePermissions from './zh-CN/models/permissions';
import modeSystem from './zh-CN/models/system';
import modeTask from './zh-CN/models/task';
import pageChangePassword from './zh-CN/pages/changePassword';
import pageException from './zh-CN/pages/exception';
import pageLogin from './zh-CN/pages/login';
import pageSystem from './zh-CN/pages/system';

export default {
  ...app,
  ...route,
  ...validator,
  ...common,
  ...component,
  ...modePermissions,
  ...modeSystem,
  ...modeTask,
  ...pageChangePassword,
  ...pageException,
  ...pageLogin,
  ...pageSystem,
};
