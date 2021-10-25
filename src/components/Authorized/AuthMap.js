// 所有的界面操作元素权限在这里配置常量，菜单的无需配置，菜单是动态渲染的。 这里只配置页面内(如按钮、链接、文字)等元素的权限

// ------ 系统功能 ------
export const SYSTEM_USER_ADD = '/sys/userManage/add';
export const SYSTEM_USER_UPDATE = '/sys/userManage/update';
export const SYSTEM_USER_RESET_PWD = '/sys/userManage/resetPwd';
export const SYSTEM_USER_STATUS = '/sys/userManage/status';
export const SYSTEM_USER_DELETE = '/sys/userManage/delete';
export const SYSTEM_ROLE_ADD = '/sys/userRoles/add';
export const SYSTEM_ROLE_UPDATE = '/sys/userRoles/update';
export const SYSTEM_ROLE_DELETE = '/sys/userRoles/delete';
export const SYSTEM_LOG_AUDIT = '/sys/logs/audit';
export const SYSTEM_LOG_FILE = '/sys/logs/file';

// ------ 零件管理 ------
export const PARTS_TYPE_INSERT = '/parts/type/insert';
export const PARTS_TYPE_DEL = '/parts/type/del';
export const PARTS_TYPE_UPDATE = '/parts/type/update';
export const PARTS_INFO_INSERT = '/parts/info/insert';
export const PARTS_INFO_DEL = '/parts/info/del';

export const DEVICE_CHANNEL = '/parts/info/channel';
export const DEVICE_BLACKLIST = '/parts/info/blacklist';
export const DEVICE_PARAM = '/parts/info/param';

/* 需要和后端接口名称一致 修改 */
export const PARTS_INFO_UPDATE = '/parts/info/update';
export const PARTS_VERSION_DEL = '/parts/version/del';
export const PARTS_VERSION_UPDATE = '/parts/version/update';
export const PARTS_DIFFVER_DEL = '/parts/diffVer/del';
export const PARTS_UPPKG_UPDATE = '/parts/upPkg/update';
export const PARTS_UPPKG_DEL = '/parts/upPkg/del';

// ------ 汽车管理 ------
export const CAR_TYPE_ADD = '/cars/type/add';
export const CAR_TYPE_UPDATE = '/cars/type/update';
export const CAR_PART_DEL = '/cars/parts/del';
export const CAR_PART_ADD = '/cars/parts/add';
export const CAR_VEHICLE_ADD = '/cars/vehicle/add';
export const CAR_VEHICLE_IMPORT = '/cars/vehicle/add';
export const CAR_VEHICLE_UPDATE = '/cars/vehicle/update';
export const CAR_VEHICLE_DETAILS = '/cars/vehicle/details';

// ------ 升级管理 ------
export const UPGRADE_STRATEGY_ADD = '/upgrade/strategy/add';
export const UPGRADE_STRATEGY_DEL = '/upgrade/strategy/del';
export const UPGRADE_STRATEGY_REVIEW = '/upgrade/strategyReview/review';
export const UPGRADE_UPTASK_SUBTASK_DETAIL =
  '/upgrade/upTasks/:id/subTask/:subTaskId';
export const UPGRADE_UPTASK_SUBTASK_CANCEL =
  '/upgrade/upTasks/:id/subTask/:subTaskId/cancel';
export const UPGRADE_UPTASKS_REVIEW = '/upgrade/upTasksReview/review';

// ------ 安全测试用例 ------
export const SECURITYTESTCASE_DOWNLOAD = '/securityTestCase/download';
export const SECURITYTESTCASE_DEL = '/securityTestCase/del';
export const SECURITYTESTCASE_ADD = '/securityTestCase/add';
export const SECURITYTESTCASE_UPDATE = '/securityTestCase/update';
