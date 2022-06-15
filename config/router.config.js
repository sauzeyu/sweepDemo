// component 是相对于 src/pages 目录的
export default [
  // user
  {
    path: '/user',
    component: '../layouts/UserLayout',
    routes: [
      // { path: '/user', redirect: '/user/login' },
      {
        path: '/user/login',
        component: './User/Login',
        title: '登录',
      },
      {
        //umi 的权限路由是通过配置路由的 Routes 属性来实现。约定式的通过 yaml 注释添加，配置式的直接配上即可。
        wrappers: ['@/router-interceptor/authorized/Logged'], // 权限控制
        routes: [
          {
            path: '/user/changePassword',
            component: './User/ChangePassword',
            title: '修改密码',
          },
        ],
      },
    ],
  },
  {
    path: '/exception',
    routes: [
      { path: '/exception/404', component: './Exception/404' },
      { path: '/exception/403', component: './Exception/403' },
      { path: '/exception/500', component: './Exception/500' },
    ],
  },
  {
    path: '/',
    component: '../layouts/BasicLayout',
    wrappers: [
      '@/router-interceptor/authorized/Logged',
      '@/router-interceptor/authorized/Authed',
    ],
    routes: [
      {
        path: '/sys',
        title: '系统管理',
        routes: [
          {
            path: '/sys/userManage',
            title: '用户管理',
            component: './System/User/Manage',
          },
          {
            path: '/sys/userRoles',
            title: '角色权限管理',
            component: './System/User/Roles',
          },
          {
            path: '/sys/logs',
            title: '操作日志',
            component: './System/Logs',
          },
          {
            path: '/sys/userResource',
            component: './System/User/Resource',
          },
        ],
      },
      {
        path: '/dashboard',
        title: '仪表盘',
        routes: [
          {
            path: '/dashboard/admin',
            title: 'SpringBootAdmin',
            component: './Dashboard/Admin',
          },
          {
            path: '/dashboard/zipkin',
            title: 'Zipkin',
            component: './Dashboard/Zipkin',
          },
        ],
      },
      {
        path: '/statistics',
        component: './Statistics',
        title: '数据统计',
      },
      {
        path: '/keys',
        title: '钥匙管理',
        routes: [
          {
            path: '/keys/info',
            title: '钥匙信息',
            component: './Keys/Info',
          },
        ],
      },
      {
        path: '/cars',
        title: '汽车管理',
        routes: [
          {
            path: '/cars/bluetooth',
            title: '蓝牙管理',
            component: './Cars/Bluetooth',
          },
          {
            path: '/cars/vehicle',
            title: '汽车信息',
            component: './Cars/Vehicle',
          },
          {
            path: '/cars/phone',
            title: '手机标定数据管理',
            component: './Cars/Phone',
          },
        ],
      },
      {
        path: '/aftermarketReplacement',
        title: '售后换件管理',
        routes: [
          {
            path: '/aftermarketReplacement/info',
            title: '换件信息',
            component: './AftermarketReplacement/Info',
          },
        ],
      },
      {
        path: '/log',
        title: '日志管理',
        routes: [
          {
            path: '/log/use',
            title: '使用日志',
            component: './Log/Use',
          },
        ],
      },
    ],
  },
  { component: '404' },
];
