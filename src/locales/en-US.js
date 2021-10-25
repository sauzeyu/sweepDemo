import app from './en-US/app';
import route from './en-US/route';
import validator from './en-US/validator';
import common from './en-US/common';
import component from './en-US/component';
import modePermissions from './en-US/models/permissions';
import modeSystem from './en-US/models/system';
import modeTask from './en-US/models/task';
import pageChangePassword from './en-US/pages/changePassword';
import pageException from './en-US/pages/exception';
import pageLogin from './en-US/pages/login';
import pageSystem from './en-US/pages/system';

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
