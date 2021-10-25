import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { getUsers } from '@/services/system';
import {
  SYSTEM_USER_ADD,
  SYSTEM_USER_DELETE,
  SYSTEM_USER_RESET_PWD,
  SYSTEM_USER_STATUS,
  SYSTEM_USER_UPDATE,
} from '@/components/Authorized/AuthMap';
import { Badge, Button, message, Modal, Popconfirm } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import Authorized from '@/components/Authorized';
import { FormattedMessage } from 'umi';
import EditForm from './EditForm';
import DrawerConfirm from '@/components/DrawerConfirm';
import { connect } from 'dva';
import { md5 } from '@/utils';
import moment from 'moment';
import { Sex } from '@/constants/user';

@connect(({ userManage, loading }) => ({
  userManage,
  upserting: loading.effects['userManage/upsertUser'],
}))
class Table extends Component {
  state = {
    userEditFormVisible: false,
  };
  columns = [
    {
      title: '登录名/姓名',
      dataIndex: 'loginName',
      render: (loginName, user) => {
        return (
          <div>
            <div>
              <Authorized route={SYSTEM_USER_UPDATE} noMatch={loginName}>
                <a onClick={() => this.showUserEditModal(user)}>{loginName}</a>
              </Authorized>
            </div>
            <div className={'text-muted'}>{user.name}</div>
          </div>
        );
      },
    },
    {
      title: '联系电话/电子邮件',
      dataIndex: 'tel',
      render(tel, user) {
        return (
          <div>
            <div>{tel}</div>
            <div className={'text-muted'}>{user.email}</div>
          </div>
        );
      },
    },
    {
      title: '角色',
      dataIndex: 'roleNames',
      render(roleNames) {
        let text = '';
        roleNames.map((v) => (text += '  ' + v));
        return <div>{text.slice(1)}</div>;
      },
    },
    {
      title: '操作',
      render: (val, user) => {
        return (
          <div className={'link-group'}>
            <Authorized route={SYSTEM_USER_RESET_PWD}>
              <Popconfirm
                title={
                  <FormattedMessage
                    id={'Page.system.users.edit.resetPasswordConfirm'}
                    values={{ account: user.username }}
                  />
                }
                onConfirm={() => this.handleResetPwd(user)}
              >
                <a>
                  <FormattedMessage id={'Page.system.users.resetPassword'} />
                </a>
              </Popconfirm>
            </Authorized>
            <Authorized route={SYSTEM_USER_DELETE}>
              <Popconfirm
                title={
                  <FormattedMessage
                    id={'Page.system.users.edit.deleteConfirm'}
                    values={{ account: user.username }}
                  />
                }
                onConfirm={() => this.handleDel(user)}
              >
                <a>
                  <FormattedMessage id={'Common.message.delete'} />
                </a>
              </Popconfirm>
            </Authorized>
          </div>
        );
      },
    },
  ];
  handleResetPwd = (user) => {
    this.props
      .dispatch({
        type: 'userManage/resetUserPassword',
        payload: {
          id: user.id,
          password: md5(user.loginName + '111111'),
        },
      })
      .then(
        () => {
          message.success('密码已经重置为"111111"');
        },
        (e) => {
          Modal.error({
            title: '操作失败',
            content: e.message,
          });
        },
      );
  };
  handleDel = (user) => {
    this.props
      .dispatch({
        type: 'userManage/deleteUser',
        payload: {
          id: user.id,
        },
      })
      .then(
        (res) => {
          message.success('操作成功');
          this.refresh();
        },
        (e) => {
          message.error(e.message);
        },
      );
  };
  showUserEditModal = (user) => {
    this.setState({ userEditFormVisible: true }, () => {
      if (user) {
        this.props
          .dispatch({
            type: 'userManage/getUserDetail',
            payload: user.id,
          })
          .then((res) => {
            this.editForm.setFieldsValue(res.user);
          });
      }
    });
  };
  closeUserEditModal = () => {
    this.setState({ userEditFormVisible: false });
  };
  upsertUser = () => {
    this.editForm
      .validateFields()
      .then((values) => {
        this.props
          .dispatch({
            type: 'userManage/upsertUser',
            payload: values,
          })
          .then(
            (res) => {
              if (res !== false) {
                this.closeUserEditModal();
                this.refresh();
              }
            },
            (err) => {
              Modal.error({
                title: '错误',
                content: err.message,
              });
            },
          );
      })
      .catch((errors) => {
        if (errors) return;
      });
  };

  refresh() {
    this.dataTable.refresh();
  }

  render() {
    const { userEditFormVisible } = this.state;
    const { upserting = false } = this.props;
    return (
      <div>
        <EasyTable
          name={'userManageDataTable'}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          columns={this.columns}
          rowKey={(record, index) => index}
          source={getUsers}
          autoFetch
          dataProp={'users'}
          extra={
            <Authorized route={SYSTEM_USER_ADD}>
              <Button
                type="primary"
                onClick={() => this.showUserEditModal(null)}
              >
                <PlusOutlined />
                <FormattedMessage id={'Common.message.add'} />
              </Button>
            </Authorized>
          }
        />
        <DrawerConfirm
          width={500}
          destroyOnClose
          title={'用户信息'}
          visible={userEditFormVisible}
          onCancel={this.closeUserEditModal}
          confirmLoading={upserting}
          onOk={this.upsertUser}
        >
          <EditForm
            wrappedComponentRef={(ins) => (this.editForm = ins.form.current)}
          />
        </DrawerConfirm>
      </div>
    );
  }
}

export default Table;
