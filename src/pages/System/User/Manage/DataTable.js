import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import {
  Badge,
  Button,
  message,
  Modal,
  Tag,
  Select,
  notification,
  Tooltip,
} from 'antd';
import Authorized from '@/components/Authorized';
import {
  getAdminList,
  getAdminRoleNameById,
  deleteById,
} from '@/services/admin';
import EditForm from '@/pages/System/User/Manage/EditForm';
import ResetPasswordForm from '@/pages/System/User/Manage/ResetPasswordForm';
import { UserAddOutlined } from '@ant-design/icons';
import AddUserForm from '@/pages/System/User/Manage/AddUserForm';
import {
  SYSTEM_USER_DELETE,
  SYSTEM_USER_INSERT,
  SYSTEM_USER_UPDATE,
  SYSTEM_USER_RESET_PASSWORD,
} from '@/components/Authorized/AuthMap';
import { TableHeaderColumn } from '@/utils/TableHeaderColumn';
export class DataTable extends Component {
  state = {
    userInfoVisible: false,
    userPasswordInfoVisible: false,
    addUserVisible: false,
    userId: '',
    userInfo: {},
    roleList: [],
    click: {},
  };

  columns = [
    {
      title: '序号',
      width: 80,
      render: (text, record, index) => {
        return TableHeaderColumn(text, record, index, this.dataTable);
      },
    },

    {
      title: '用户名',
      dataIndex: 'username',
    },
    {
      title: '描述',
      dataIndex: 'extraInfo',
      width: 300,
      ellipsis: {
        showTitle: false,
      },
      render: (text) => (
        <Tooltip placement="topLeft" title={text}>
          {text}
        </Tooltip>
      ),
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
    },
    {
      title: '编辑时间',
      dataIndex: 'updateTime',
    },
    {
      title: '编辑者',
      dataIndex: 'updator',
    },
    {
      title: '操作',
      fixed: 'right',
      render: (col) => {
        return (
          <>
            <div className={'link-group'}>
              <Authorized route={SYSTEM_USER_UPDATE}>
                <a
                  onClick={() => {
                    this.editUser(col);
                  }}
                >
                  修改
                </a>
              </Authorized>
              <Authorized route={SYSTEM_USER_DELETE}>
                <a
                  onClick={() => this.deleteUser(col)}
                  style={{ color: '#FF4D4F' }}
                >
                  删除
                </a>
              </Authorized>

              <Authorized route={SYSTEM_USER_RESET_PASSWORD}>
                <a
                  onClick={() => {
                    this.resetPassword(col);
                  }}
                >
                  重置密码
                </a>
              </Authorized>
            </div>
          </>
        );
      },
    },
  ];

  editUser = (user) => {
    let roleList;
    getAdminRoleNameById(user.id).then(
      (res) => {
        // if (res?.data) {
        roleList = res?.data?.id;
        // }
        this.setState(
          {
            userInfoVisible: true,
          },
          () => {
            if (user) {
              // user.roleId
              if (roleList) {
                user.roleId = roleList;
              }
              // this.editForm.current?.setFieldsValue(user);
              this.editForm?.setFieldsValue(user);
            }
          },
        );
      },
      (error) => {
        message.error('服务器繁忙,请稍后再试！');
      },
    );
  };
  resetPassword = (user) => {
    let roleList;
    getAdminRoleNameById(user.id).then(
      (res) => {
        // if (res?.data) {
        roleList = res?.data?.id;
        // }
        this.setState(
          {
            userPasswordInfoVisible: true,
          },
          () => {
            if (user) {
              // user.roleId
              if (roleList) {
                user.roleId = roleList;
              }
              // this.editForm.current?.setFieldsValue(user);
              this.resetPasswordForm?.setFieldsValue(user);
            }
          },
        );
      },
      (error) => {
        message.error('服务器繁忙,请稍后再试！');
      },
    );
  };

  addUserFormOk = () => {
    this.addForm.submit();
    this.setState({ addUserVisible: false });
  };

  reload = () => {
    this.dataTable.reload();
  };
  deleteUser = (user) => {
    const that = this.dataTable;
    Modal.confirm({
      title: '删除管理员',
      content: `确定删除 ${user.username}？`,
      onOk: () => {
        return deleteById(user.id).then(() => {
          that.reload();
        });
      },
      okText: '删除',
    });
  };

  onOk = () => {
    this.setState({
      userInfoVisible: false,
    });
    this.editForm.submit();
  };
  onCancel = () => {
    this.setState({
      userInfoVisible: false,
      addUserVisible: false,
    });
    this.editForm?.resetFields();
  };

  onResetPasswordOk = () => {
    this.setState({
      userPasswordInfoVisible: false,
    });
    this.resetPasswordForm.submit();
  };
  onResetPasswordCancel = () => {
    this.setState({
      userPasswordInfoVisible: false,
    });
    this.resetPasswordForm?.resetFields();
  };

  render() {
    const { searchFormValue } = this.state.addUserVisible;
    const onClick = (value) => {};

    const onFinishFailed = (value) => {
      this.setState({ addUserVisible: value });
    };
    const editFinishFailed = (value) => {
      this.setState({ userInfoVisible: value });
      this.setState({ userInfoVisible: value });
    };
    const resetPasswordFinishFailed = (value) => {
      this.setState({ userPasswordInfoVisible: value });
      this.setState({ userPasswordInfoVisible: value });
    };
    return (
      <>
        <EasyTable
          autoFetch
          source={getAdminList}
          dataProp={'data'}
          name={'adminDataTable'}
          rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <Authorized route={SYSTEM_USER_INSERT}>
              <Button
                type={'primary'}
                icon={<UserAddOutlined />}
                onClick={() => {
                  this.setState({
                    addUserVisible: true,
                  });
                }}
              >
                新增用户
              </Button>
            </Authorized>
          }
        />
        <Modal
          title="修改用户"
          visible={this.state.userInfoVisible}
          onOk={this.onOk}
          onCancel={this.onCancel}
          okText="确认"
          cancelText="取消"
        >
          <EditForm
            dataTableRef={this.dataTable}
            editFormRef={(ref) => (this.editForm = ref.form.current)}
            finish={editFinishFailed}
          />
        </Modal>

        <Modal
          title="重置密码"
          visible={this.state.userPasswordInfoVisible}
          onOk={this.onResetPasswordOk}
          onCancel={this.onResetPasswordCancel}
          okText="确认"
          cancelText="取消"
        >
          <ResetPasswordForm
            dataTableRef={this.dataTable}
            resetPasswordRef={(ref) =>
              (this.resetPasswordForm = ref.form.current)
            }
            finish={resetPasswordFinishFailed}
          />
        </Modal>

        <Modal
          title="新增用户"
          visible={this.state.addUserVisible}
          onOk={this.addUserFormOk}
          // footer={null}
          onCancel={this.onCancel}
          okText="确认"
          cancelText="取消"
          afterClose={this.reload}
        >
          <AddUserForm
            // toChildren={this.state.addUserVisible}
            // searchFormValue={searchFormValue}
            title="测试组件"
            click={onClick}
            finish={onFinishFailed}
            dataTableRef={this.dataTable}
            addFormEef={(ref) => (this.addForm = ref.form.current)}
          />
        </Modal>
      </>
    );
  }
}

export default DataTable;
