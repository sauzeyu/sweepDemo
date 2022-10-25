// 所有的界面操作元素权限在这里配置常量，菜单的无需配置，菜单是动态渲染的。 这里只配置页面内(如按钮、链接、文字)等元素的权限

// ------ 系统功能 ------
export const SYSTEM_USER_MANAGE = '/sys/userManage';
export const SYSTEM_USER_INSERT = '/sys/userManage/insert';
export const SYSTEM_USER_UPDATE = '/sys/userManage/update';
export const SYSTEM_USER_DELETE = '/sys/userManage/delete';
export const SYSTEM_USER_MANAGE_TABLE = '/sys/userManage/table';
export const SYSTEM_USER_MANAGE_SELECT = '/sys/userManage/select';

export const SYSTEM_USER_ROLES = '/sys/userRoles';
export const SYSTEM_ROLE_INSERT = '/sys/userRoles/insert';
export const SYSTEM_ROLE_UPDATE = '/sys/userRoles/update';
export const SYSTEM_ROLE_DELETE = '/sys/userRoles/delete';
export const SYSTEM_USER_ROLES_TABLE = '/sys/userRoles/table';
export const SYSTEM_USER_ROLES_SELECT = '/sys/userRoles/select';

export const SYSTEM_USER_RESOURCE = '/sys/userResource';
export const SYSTEM_USER_RESOURCE_TABLE = '/sys/userResource/table';
export const SYSTEM_USER_RESOURCE_SELECT = '/sys/userResource/select';

// ------ 钥匙管理 ------
export const KEYS_INFO = '/keys/info';
export const KEYS_INFO_EXPORT = '/keys/info/export';
export const KEYS_INFO_REVOKE = '/keys/info/revoke';
export const KEYS_INFO_THAW = '/keys/info/thaw';
export const KEYS_INFO_FREEZE = '/keys/info/freeze';
export const KEYS_INFO_TABLE = '/keys/info/table';
export const KEYS_INFO_SELECT = '/keys/info/select';

// ------ 车辆管理 ------

export const CARS_PHONE = '/cars/phone';
export const CARS_PHONE_UPDATE = '/cars/phone/update';
export const CARS_PHONE_EXPORT = '/cars/phone/export';
export const CARS_PHONE_IMPORT = '/cars/phone/import';
export const CARS_PHONE_TEMPLATE = '/cars/phone/template';
export const CARS_PHONE_TABLE = '/cars/phone/table';
export const CARS_PHONE_SELECT = '/cars/phone/select';

export const CARS_BLUETOOTH = '/cars/bluetooth';
export const CARS_BLUETOOTH_EXPORT = '/cars/bluetooth/export';
export const CARS_BLUETOOTH_TABLE = '/cars/bluetooth/table';
export const CARS_BLUETOOTH_SELECT = '/cars/bluetooth/select';

export const CARS_VEHICLE = '/cars/vehicle';
export const CARS_VEHICLE_EXPORT = '/cars/vehicle/export';
export const CARS_VEHICLE_TABLE = '/cars/vehicle/table';
export const CARS_VEHICLE_SELECT = '/cars/vehicle/select';

export const CARS_VEHICLE_CALIBRATION = '/cars/vehicleCalibration';
export const CARS_VEHICLE_CALIBRATION_TABLE = '/cars/vehicleCalibration/table';
export const CARS_VEHICLE_CALIBRATION_UPDATE = '/cars/phone/update';
export const CARS_VEHICLE_CALIBRATION_EXPORT = '/cars/phone/export';
export const CARS_VEHICLE_CALIBRATION_IMPORT = '/cars/phone/import';
export const CARS_VEHICLE_CALIBRATION_TEMPLATE = '/cars/phone/template';

// ------ 售后换件管理 ------
export const AFTERMARKET_REPLACEMENT_INFO = '/aftermarketReplacement/info';
export const AFTERMARKET_REPLACEMENT_INFO_EXPORT =
  '/aftermarketReplacement/info/export';
export const AFTERMARKET_REPLACEMENT_INFO_TABLE =
  '/aftermarketReplacement/info/table';
export const AFTERMARKET_REPLACEMENT_INFO_SELECT =
  '/aftermarketReplacement/info/select';
// ------ 日志管理 ------
export const LOG_USE = '/log/use';
export const LOG_USE_EXPORT = '/log/use/export';
export const LOG_USE_TABLE = '/log/use/table';
export const LOG_USE_SELECT = '/log/use/select';

// ------ 页面权限 ------
export const DASHBOARD_ZIPKIN = '/dashboard/zipkin';
export const DASHBOARD_ADMIN = '/dashboard/admin';
export const DASHBOARD_ADMIN_INFO = '/dashboard/admin/info';
export const DASHBOARD_ZIPKIN_INFO = '/dashboard/zipkin/info';

export const STATISTICS = '/statistics';
export const STATISTICS_TOP = '/statistics/top';
export const STATISTICS_MIDDLE = '/statistics/middle';
export const STATISTICS_BOTTOM = '/statistics/bottom';
