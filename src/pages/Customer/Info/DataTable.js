import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal, Tag } from 'antd';
import { isVaild, realName, status } from '@/constants/user';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';

import { getCustomerList, stopDkUser } from '@/services/customer';
import VehicleTable from '@/pages/Customer/Info/VehicleTable';
import KeysTable from '@/pages/Customer/Info/KeysTable';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';

@connect(({ customer, loading }) => ({
  customer,
  upserting: loading.effects['customer/upsert'],
}))
class DataTable extends Component {
  state = {
    carFormVisible: false,
    keysFormVisible: false,
    selectedRowKeys: [],
    showCarInfo: false,
    editFormVisible: false,
    userId: '',
  };
  columns = [
    {
      title: '电话',
      dataIndex: 'phone',
      width: 150,
    },
    {
      title: '姓名',
      dataIndex: 'username',
      width: 110,
    },
    {
      title: '身份证',
      dataIndex: 'idCard',
      width: 150,
    },
    {
      title: '是否有效',
      dataIndex: 'isValid',
      width: 110,
      render: (text) => {
        return isVaild[text];
      },
    },
    {
      title: '指纹',
      dataIndex: 'phoneFingerprint',
      width: 150,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 200,
      render: (text) => {
        return realName[text];
      },
    },
    {
      title: '操作',
      fixed: 'right',
      width: 270,
      render: (col) => {
        const flag = col.isValid === 0;
        return (
          <div className={'link-group'}>
            <a onClick={() => this.carInfo(col)}>查看车辆</a>
            <a onClick={() => this.keysInfo(col)}>查看钥匙</a>
            <a onClick={() => this.editUser(col)}>编辑</a>

            <a
              onClick={() => {
                return stopDkUser(col.id).then(() => {
                  //改变状态
                  col.isValid = col.isValid ^ 1 ^ 0;
                  //刷新组件
                  this.dataTable.refresh();
                });
              }}
              style={{ color: flag ? '#1890FF' : '#FF4D4F' }}
            >
              {flag ? '启用' : '停用'}
            </a>
          </div>
        );
      },
    },
  ];
  carInfo = (col) => {
    this.setState({
      carFormVisible: true,
      userId: col.id,
    });
  };
  editUser = (col) => {
    this.setState(
      {
        editFormVisible: true,
        selectedUser: col,
      },
      () => {
        if (col) {
          this.editForm.setFieldsValue(col);
        }
      },
    );
  };
  keysInfo = (col) => {
    this.setState({
      keysFormVisible: true,
      userId: col.id,
    });
  };
  del = (model) => {
    Modal.confirm({
      title: '确定删除？',
      onOk: () => {
        return this.props
          .dispatch({
            type: 'customer/delete',
            payload: items,
          })
          .then(
            () => {
              this.setState({ selectedRowKeys: [] });
              this.dataTable.refresh();
            },
            (err) => {
              message.error(err.message);
            },
          );
      },
    });
  };
  onCancel = () => {
    this.setState({
      carFormVisible: false,
      keysFormVisible: false,
      editFormVisible: false,
    });
  };
  onCancelUserInfo = () => {
    this.setState({
      carFormVisible: false,
      keysFormVisible: false,
      editFormVisible: false,
    });
    this.editForm.resetFields();
  };
  onOk = () => {
    this.editForm
      .validateFields()
      .then((values) => {
        this.props
          .dispatch({
            type: 'customer/upsert',
            payload: values,
          })
          .then(
            () => {
              this.setState({ editFormVisible: false });
              this.dataTable.refresh();
              //清除 editForm 中的字段
              this.editForm.resetFields();
            },
            (err) => {
              Modal.error({
                title: err.message,
              });
            },
          );
      })
      .catch((errors) => {
        if (errors) return;
      });
  };
  handleRowSelectChange = (selectedRowKeys) => {
    this.setState({ selectedRowKeys });
  };
  handleTableChange = () => {
    // 切换分页会置空
    // this.setState({ selectedRowKeys: [] });
  };

  render() {
    const { selectedRowKeys } = this.state;
    const { carFormVisible } = this.state;
    const { keysFormVisible } = this.state;
    const rowSelection = {
      selectedRowKeys,
      onChange: this.handleRowSelectChange,
    };
    return (
      <div>
        <EasyTable
          autoFetch
          source={getCustomerList}
          dataProp={'data'}
          name={'customerDataTable'}
          rowKey={'id'}
          columns={this.columns}
          // rowSelection={rowSelection}
          onChange={this.handleTableChange}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <div className={'btn-group'}>
              {/*selectedRowKeys.length > 0 && (
                <Button
                  type={'danger'}
                  onClick={() => this.del(selectedRowKeys)}
                >
                  删除用户
                </Button>
              )*/}
              <Button
                onClick={() => this.editUser()}
                type={'primary'}
                icon={<PlusCircleOutlined />}
              >
                新增用户
              </Button>
            </div>
          }
        />

        <DrawerConfirm
          title={'车辆信息'}
          width={1000}
          visible={carFormVisible}
          onCancel={this.onCancel}
          onOk={this.onCancel}
          //隐藏确定按钮
          hiddenOk={true}
          destroyOnClose
        >
          <VehicleTable
            userId={this.state.userId}
            editFormRef={(ref) => (this.editForm = ref.form.current)}
            wrappedComponentRef={(ref) => (this.editFormWrapper = ref)}
          />
        </DrawerConfirm>

        <DrawerConfirm
          title={'钥匙信息'}
          width={1500}
          visible={keysFormVisible}
          onCancel={this.onCancel}
          onOk={this.onCancel}
          destroyOnClose
        >
          <KeysTable
            userId={this.state.userId}
            editFormRef={(ref) => (this.editForm = ref.form.current)}
            wrappedComponentRef={(ref) => (this.editFormWrapper = ref)}
          />
        </DrawerConfirm>

        <DrawerConfirm
          title={'用户信息'}
          width={600}
          visible={this.state.editFormVisible}
          onOk={this.onOk}
          onCancel={this.onCancelUserInfo}
        >
          <EditForm editFormRef={(ref) => (this.editForm = ref.form.current)} />
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
