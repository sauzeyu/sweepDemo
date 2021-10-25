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
          {
            path: '/keys/logs',
            title: '钥匙使用日志',
            component: './Keys/Logs',
          },
        ],
      },
    ],
  },
  { component: '404' },
];
