import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal, Tag, Select, notification } from 'antd';

import EditForm from './EditForm';
import { getRoleList, deleteById } from '@/services/role';
import { selectMenuByRoleId } from '@/services/menu';

export class DataTable extends Component {
  state = {
    userInfoVisible: false,
    userId: '',
    userInfo: {},
    roleList: [],
    selectKeys: [],
  };

  editRole = (role) => {
    selectMenuByRoleId(role.id).then((res) => {
      this.setState({
        selectKeys: res.data,
      });
    });
    this.setState(
      {
        userInfoVisible: true,
      },
      () => {
        this.editForm.setFieldsValue(role);
      },
    );
  };

  deleteUser = (role) => {
    const that = this.dataTable;
    Modal.error({
      title: '删除角色',
      content: `确定删除 ${role.roleName}？`,
      onOk: () => {
        return deleteById(role.id).then(
          (res) => {
            message.success(res.msg);
            that.refresh();
          },
          (res) => {
            message.error(res.msg);
          },
        );
      },
      okText: '删除',
    });
  };

  onOk = () => {
    console.log('this.editForm ', this.editForm);
    this.setState({
      userInfoVisible: false,
    });

    this.editForm.submit();
  };

  onCancel = () => {
    this.setState({
      userInfoVisible: false,
    });
    this.editForm.resetFields();
  };
  columns = [
    {
      title: '角色代码',
      dataIndex: 'code',
    },
    {
      title: '角色名称',
      dataIndex: 'roleName',
    },
    {
      title: '角色描述',
      dataIndex: 'intro',
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
              <a
                onClick={() => {
                  this.editRole(col);
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
          source={getRoleList}
          dataProp={'data'}
          name={'roleDataTable'}
          rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
        />
        <Modal
          // destroyOnClose={true}
          title="修改用户"
          visible={this.state.userInfoVisible}
          onOk={this.onOk}
          onCancel={this.onCancel}
          okText="确认"
          cancelText="取消"
        >
          <EditForm
            keyList={this.state.selectKeys}
            dataTableRef={this.dataTable}
            editFormRef={(ref) => (this.editForm = ref.form.current)}
          />
        </Modal>
      </div>
    );
  }
}

export default DataTable;
