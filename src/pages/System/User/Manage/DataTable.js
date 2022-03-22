import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal, Tag, Select, notification } from 'antd';

import {
  getAdminList,
  getAdminRoleNameById,
  deleteById,
} from '@/services/admin';
import EditForm from '@/pages/System/User/Manage/EditForm';

export class DataTable extends Component {
  state = {
    userInfoVisible: false,
    userId: '',
    userInfo: {},
    roleList: [],
  };

  editUser = (user) => {
    let roleList;
    getAdminRoleNameById(user.id).then(
      (res) => {
        roleList = res.data;
        this.setState(
          {
            userInfoVisible: true,
          },
          () => {
            if (user) {
              user.roleList = roleList;
              this.editForm.setFieldsValue(user);
            }
          },
        );
      },
      (error) => {
        message.error('服务器繁忙,请稍后再试！');
      },
    );
  };
  deleteUser = (user) => {
    const that = this.dataTable;
    Modal.error({
      title: '删除管理员',
      content: `确定删除 ${user.username}？`,
      onOk: () => {
        return deleteById(user.id).then(() => {
          that.refresh();
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
  hideUserInfoModal = () => {
    this.setState({
      userInfoVisible: false,
    });
  };
  columns = [
    {
      title: '用户名',
      dataIndex: 'username',
    },
    {
      title: '额外信息',
      dataIndex: 'extraInfo',
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
        const editOrDeleteNotification = (type) => {
          notification[type]({
            message: '操作失败',
            description: '没有操作权限，请联系超级管理员！',
          });
        };
        const username = localStorage.getItem('last_username');
        const isSelf = username === col.username;
        return (
          <>
            <div className={'link-group'} hidden={isSelf}>
              <a
                onClick={() => {
                  this.editUser(col);
                }}
              >
                修改
              </a>
              <a
                onClick={() => this.deleteUser(col)}
                style={{ color: '#FF4D4F' }}
              >
                删除
              </a>
            </div>

            <div className={'link-group'} hidden={!isSelf}>
              <a
                onClick={() => editOrDeleteNotification('warning')}
                style={{ color: '#808080' }}
              >
                修改{' '}
              </a>
              <a
                onClick={() => editOrDeleteNotification('warning')}
                style={{ color: '#808080' }}
              >
                删除{' '}
              </a>
            </div>
          </>
        );
      },
    },
  ];

  render() {
    return (
      <div>
        <EasyTable
          autoFetch
          source={getAdminList}
          dataProp={'data'}
          name={'adminDataTable'}
          rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
        />
        <Modal
          title="修改用户"
          visible={this.state.userInfoVisible}
          onOk={this.onOk}
          onCancel={this.hideUserInfoModal}
          okText="确认"
          cancelText="取消"
        >
          <EditForm
            dataTableRef={this.dataTable}
            editFormRef={(ref) => (this.editForm = ref.form.current)}
          />
        </Modal>
      </div>
    );
  }
}

export default DataTable;
