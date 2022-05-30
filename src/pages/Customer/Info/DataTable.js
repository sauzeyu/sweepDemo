import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { connect } from 'dva';

import { getCustomerList } from '@/services/customer';
import VehicleTable from '@/pages/Customer/Info/VehicleTable';
import KeysTable from '@/pages/Customer/Info/KeysTable';
import DrawerConfirm from '@/components/DrawerConfirm';

@connect(({ customer, loading }) => ({
  customer,
  upserting: loading.effects['customer/upsert'],
}))
class DataTable extends Component {
  state = {
    carFormVisible: false,
    keysFormVisible: false,
    showCarInfo: false,
    user: {},
  };
  columns = [
    {
      title: '姓名',
      dataIndex: 'username',
      width: 110,
    },
    {
      title: '电话',
      dataIndex: 'phone',
      width: 150,
    },
    {
      title: '操作',
      fixed: 'right',
      width: 270,
      render: (col) => {
        return (
          <div className={'link-group'}>
            <a onClick={() => this.carInfo(col)}>查看车辆</a>
            <a onClick={() => this.keysInfo(col)}>查看钥匙</a>
          </div>
        );
      },
    },
  ];
  carInfo = (col) => {
    this.setState({
      carFormVisible: true,
      user: col,
    });
  };
  keysInfo = (col) => {
    this.setState({
      keysFormVisible: true,
      user: col,
    });
  };
  onCancel = () => {
    this.setState({
      carFormVisible: false,
      keysFormVisible: false,
    });
  };
  onCancelUserInfo = () => {
    this.setState({
      carFormVisible: false,
      keysFormVisible: false,
    });
  };

  render() {
    const { carFormVisible } = this.state;
    const { keysFormVisible } = this.state;
    return (
      <div>
        <EasyTable
          autoFetch
          source={getCustomerList}
          dataProp={'data'}
          name={'customerDataTable'}
          rowKey={'id'}
          columns={this.columns}
          onChange={this.handleTableChange}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
        />

        <DrawerConfirm
          title={'车辆信息'}
          width={800}
          visible={carFormVisible}
          onCancel={this.onCancel}
          onOk={this.onCancel}
          //隐藏确定按钮
          hiddenOk={true}
          destroyOnClose
        >
          <VehicleTable
            user={this.state.user}
            editFormRef={(ref) => (this.editForm = ref.form.current)}
            wrappedComponentRef={(ref) => (this.editFormWrapper = ref)}
          />
        </DrawerConfirm>

        <DrawerConfirm
          title={'钥匙信息'}
          width={1300}
          visible={keysFormVisible}
          onCancel={this.onCancel}
          hiddenOk={true}
          destroyOnClose
        >
          <KeysTable
            user={this.state.user}
            editFormRef={(ref) => (this.editForm = ref.form.current)}
            wrappedComponentRef={(ref) => (this.editFormWrapper = ref)}
          />
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
