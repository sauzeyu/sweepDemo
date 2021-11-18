'use strict';
const Menus = require('./menu_copy.json');
const fs = require('fs');
const uid = require('uuid');

export const mockUser = {
  token: 125544777,
  user: {
    id: 10000,
    changedPWD: 1,
    loginName: 'admin',
    name: '超级管理员',
    sex: '男',
    tel: '13888888882',
    email: 'admin@cssa.com',
  },
  menus: Menus,
};
export const mockUserList = {
  total: 8,
  pageIndex: 1,
  pageSize: 20,
  users: [
    {
      id: 1,
      loginName: 'test',
      name: 'test',
      sex: 'F',
      tel: '13776055179',
      email: 'b@qq.com',
      isvalid: 1,
      isSetValidDate: 0,
      validDate: null,
      roleNames: ['超级管理员'],
    },
    {
      id: 3,
      loginName: 'admin',
      name: '系统管理员',
      sex: 'F',
      tel: '13711112345',
      email: 'admin@vecentek.com',
      isvalid: 1,
      isSetValidDate: null,
      validDate: null,
      roleNames: ['超级管理员'],
    },
    {
      id: 11,
      loginName: 'kang',
      name: '宋糠',
      sex: 'F',
      tel: null,
      email: null,
      isvalid: 1,
      isSetValidDate: 0,
      validDate: null,
      roleNames: ['超级管理员'],
    },
    {
      id: 12,
      loginName: 'shenliang',
      name: '沈亮',
      sex: 'F',
      tel: null,
      email: null,
      isvalid: 1,
      isSetValidDate: 0,
      validDate: null,
      roleNames: ['超级管理员'],
    },
    {
      id: 13,
      loginName: 'wangcj',
      name: 'wangcj',
      sex: 'F',
      tel: null,
      email: null,
      isvalid: 1,
      isSetValidDate: 0,
      validDate: null,
      roleNames: ['超级管理员'],
    },
  ],
};
export const mockRoleList = [
  {
    id: 100019,
    remark: '调试用的',
    name: '开发调试',
  },
  {
    id: 100016,
    remark: '商户角色',
    name: '商户',
  },
];

export default {
  'POST /dkserver-back/login': (req, res) => {
    const body = req.body;
    // if(body.username === 'admin' && body.password==="645c9d6e956246fd1d9f217ef7c55f95"){
    res.send(mockUser);
    // }else {
    //     res.status(500).send({
    //         "errorCode": 300100,
    //         "errorMsg": "用户名或密码错误。",
    //         "requestId": "05181013091106903591"
    //     })
    // }
  },
  // 'POST /dkserver-back/signOut': {
  //   success: true,
  // },
  'POST /dkserver-back/modifyPwd': (req, res) => {
    const body = req.body;
    if (body.oldPwd !== '645c9d6e956246fd1d9f217ef7c55f95') {
      res.status(500).send({
        errorCode: 300101,
        errorMsg: '原密码错误。',
        requestId: '28382947981221',
      });
    } else {
      res.end();
    }
  },
  'GET /dkserver-back/users': (req, res) => {
    let users = mockUserList.users;
    if ('keywords' in req.query) {
      users = users.filter(
        (item) => req.query.keywords.indexOf(item.name) !== -1,
      );
    }
    if ('isvalid' in req.query) {
      users = users.filter((item) => item.isvalid == req.query.isvalid);
    }
    res.send({
      ...mockUserList,
      users,
    });
  },
  'GET /dkserver-back/user/:id': (req, res) => {
    let user = mockUserList.users;
    user = user.filter((item) => {
      return req.params.id == item.id;
    });
    res.send({
      user: user[0],
    });
  },
  'GET /dkserver-back/roles': (req, res) => {
    res.send({ roles: mockRoleList });
  },
  'GET /dkserver-back/role/:id': (req, res) => {
    res.send(mockRoleList.find((item) => item.id == req.params.id));
  },
  'GET /dkserver-back/menus': { menus: Menus },
  'POST /dkserver-back/resource/addOrUpdate': (req, res) => {
    const list = req.body.list;
    list.map((item) => {
      if (!item.id) {
        item.id = (Math.random() * 100000).toFixed(0);
      }
    });
    fs.writeFile(
      require('path').join(__dirname, 'menu.json'),
      JSON.stringify(list),
      (err) => {
        if (err) {
          res.send({
            code: 'EB3119',
            data: [],
            info: '菜单写入失败',
            msg: '菜单写入失败',
            psn: '05181013091106902591',
            status: -99,
            total: 0,
          });
        } else {
          res.send({ code: 0, data: [], msg: '操作成功' });
        }
      },
    );
  },
};
