// 所有的界面操作元素权限在这里配置常量，菜单的无需配置，菜单是动态渲染的。 这里只配置页面内(如按钮、链接、文字)等元素的权限

// ------ 系统功能 ------
export const SYSTEM_USER_INSERT = '/sys/userManage/insert';
export const SYSTEM_USER_UPDATE = '/sys/userManage/update';
export const SYSTEM_USER_DELETE = '/sys/userManage/delete';
export const SYSTEM_ROLE_INSERT = '/sys/userRoles/insert';
export const SYSTEM_ROLE_UPDATE = '/sys/userRoles/update';
export const SYSTEM_ROLE_DELETE = '/sys/userRoles/delete';

// ------ 钥匙管理 ------
export const KEY_INFO_EXPORT = '/keys/info/export';
export const KEY_INFO_REVOKE = '/keys/info/revoke';
export const KEY_INFO_THAW = '/keys/info/thaw';
export const KEY_INFO_FREEZE = '/keys/info/freeze';

// ------ 车辆管理 ------
export const CAR_PHONE_UPDATE = '/cars/phone/update';
export const CAR_PHONE_EXPORT = '/cars/phone/export';
export const CAR_PHONE_IMPORT = '/cars/phone/import';
export const CAR_PHONE_TEMPLATE = '/cars/phone/template';

// ------ 售后换件管理 ------
export const AFTERMARKET_REPLACEMENT_INFO_EXPORT =
  '/aftermarketReplacement/info/export';
// ------ 日志管理 ------
export const LOG_USE_EXPORT = '/log/use/export';

// ------ 安全测试用例 ------
export const SECURITYTESTCASE_DOWNLOAD = '/securityTestCase/download';
export const SECURITYTESTCASE_DEL = '/securityTestCase/del';
export const SECURITYTESTCASE_ADD = '/securityTestCase/add';
export const SECURITYTESTCASE_UPDATE = '/securityTestCase/update';
