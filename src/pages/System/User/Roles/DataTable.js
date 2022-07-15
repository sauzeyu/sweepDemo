import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import Authorized from '@/components/Authorized';
import { Badge, Button, message, Modal, Tag, Select, notification } from 'antd';

import EditForm from './EditForm';
import { getRoleList, deleteById } from '@/services/role';
import { selectMenuByRoleId } from '@/services/menu';
import {
  SYSTEM_ROLE_DELETE,
  SYSTEM_ROLE_INSERT,
  SYSTEM_ROLE_UPDATE,
  SYSTEM_USER_INSERT,
  SYSTEM_USER_UPDATE,
} from '@/components/Authorized/AuthMap';
import { UsergroupAddOutlined } from '@ant-design/icons';
import AddForm from '@/pages/System/User/Roles/AddForm';

export class DataTable extends Component {
  state = {
    editRoleVisible: false,
    addRoleVisible: false,
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
        editRoleVisible: true,
      },
      () => {
        this.editForm?.setFieldsValue(role);
      },
    );
  };

  deleteUser = (role) => {
    const that = this.dataTable;
    Modal.confirm({
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

  onEditFormOk = () => {
    this.editForm?.submit();
    this.setState({
      editRoleVisible: false,
    });
  };
  onAddFormOk = () => {
    this.addForm?.submit();
    this.setState({
      addRoleVisible: false,
    });
  };

  onCancel = () => {
    this.setState({
      editRoleVisible: false,
      addRoleVisible: false,
    });
    this.addForm?.resetFields();
  };
  columns = [
    {
      title: '序号',
      width: 80,
      render: (text, record, index) => {
        let currentIndex = this.dataTable?.state?.currentIndex;
        let currentPageSize = this.dataTable?.state?.currentPageSize;
        return (currentIndex - 1) * currentPageSize + (index + 1);
      },
    },
    {
      title: '角色名称',
      dataIndex: 'roleName',
    },
    {
      title: '角色代码',
      dataIndex: 'code',
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
      title: '创建者',
      dataIndex: 'creator',
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
              <Authorized route={SYSTEM_ROLE_UPDATE}>
                <a
                  onClick={() => {
                    this.editRole(col);
                    this.editForm?.resetFields();
                  }}
                >
                  修改
                </a>
              </Authorized>
              <Authorized route={SYSTEM_ROLE_DELETE}>
                <a
                  onClick={() => this.deleteUser(col)}
                  style={{ color: '#FF4D4F' }}
                >
                  删除
                </a>
              </Authorized>
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
          extra={
            <Authorized route={SYSTEM_ROLE_INSERT}>
              <Button
                type={'primary'}
                icon={<UsergroupAddOutlined />}
                onClick={() => {
                  this.addForm?.resetFields();
                  this.setState({
                    addRoleVisible: true,
                  });
                }}
              >
                新增角色
              </Button>
            </Authorized>
          }
        />
        <Modal
          // destroyOnClose={true}

          title="修改角色"
          visible={this.state.editRoleVisible}
          onOk={this.onEditFormOk}
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
        <Modal
          destroyOnClose={true}
          title={'新增角色'}
          visible={this.state.addRoleVisible}
          onOk={this.onAddFormOk}
          onCancel={this.onCancel}
          okText="确认"
          cancelText="取消"
        >
          <AddForm
            dataTableRef={this.dataTable}
            editFormRef={(ref) => (this.addForm = ref.form.current)}
          />
        </Modal>
      </div>
    );
    console.log(this.dataTable);
  }
}

export default DataTable;
