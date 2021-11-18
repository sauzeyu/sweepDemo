import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal, Tag } from 'antd';
import { isVaild, status } from '@/constants/user';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import { getCustomerList } from '@/services/customer';
import { Link } from 'umi';
import { DKState } from '@/constants/keys';
import DescriptionList from '@/components/DescriptionList';
import VehicleTable from '@/pages/Customer/Info/VehicleTable';
import EditForm from '@/pages/Cars/Vehicle/EditForm';
import DrawerConfirm from '@/components/DrawerConfirm';
@connect(({ customer, loading }) => ({
  customer,
  upserting: loading.effects['customer/upsert'],
}))
class DataTable extends Component {
  state = {
    editFormVisible: false,
    selectedRowKeys: [],
    showCarInfo: false,
    carInfo: {},
  };
  columns = [
    {
      title: '联系电话',
      dataIndex: 'phone',
      width: 150,
    },
    {
      title: '用户名',
      dataIndex: 'name',
      width: 110,
    },
    {
      title: '身份证号',
      dataIndex: 'idnum',
      width: 150,
    },
    {
      title: '是否有效',
      dataIndex: 'isvalid',
      width: 110,
      render: (text) => {
        return isVaild[text];
      },
    },
    {
      title: '手机设备指纹',
      dataIndex: 'devFp',
      width: 150,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 80,
      render: (text) => {
        return status[text];
      },
    },
    {
      title: '注册时间',
      dataIndex: 'registTime',
      width: 180,
    },
    {
      title: '操作',
      fixed: 'right',
      width: 270,
      render: (col) => {
        return (
          <div className={'link-group'}>
            <a onClick={() => this.carInfo(col)}>查看车辆</a>
            <a onClick={() => this.userInfo(col)}>查看钥匙</a>
            <a onClick={() => this.del(col)}>启用</a>
            <a className={'text-danger'} onClick={() => this.del(col)}>
              停用
            </a>
          </div>
        );
      },
    },
  ];
  carInfo = (col) => {
    this.setState({
      editFormVisible: true,
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
  handleRowSelectChange = (selectedRowKeys) => {
    this.setState({ selectedRowKeys });
  };
  handleTableChange = () => {
    // 切换分页会置空
    // this.setState({ selectedRowKeys: [] });
  };
  userInfo = () => {};
  render() {
    const { showCarInfo, carInfo = {} } = this.state;
    const { selectedRowKeys } = this.state;
    const { editFormVisible } = this.state;
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
          rowSelection={rowSelection}
          onChange={this.handleTableChange}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <div className={'btn-group'}>
              {selectedRowKeys.length > 0 && (
                <Button
                  type={'danger'}
                  onClick={() => this.del(selectedRowKeys)}
                >
                  删除用户
                </Button>
              )}
            </div>
          }
        />

        <DrawerConfirm
          title={'汽车'}
          width={650}
          visible={editFormVisible}
          onOk={this.onOk}
          onCancel={this.onCancel}
          destroyOnClose
        >
          <VehicleTable
            editFormRef={(ref) => (this.editForm = ref.form.current)}
            wrappedComponentRef={(ref) => (this.editFormWrapper = ref)}
          />
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
