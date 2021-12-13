import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { vehicleListById } from '@/services/customer';
import { status } from '@/constants/user';

class VehicleTable extends Component {
  columns = [
    {
      title: 'VIN号',
      dataIndex: 'vin',
    },
    {
      title: '车型代码',
      dataIndex: 'modelCode',
      ellipsis: true,
    },
    {
      title: '车牌号',
      dataIndex: 'license',
      //文本溢出长度后显示省略
      ellipsis: true,
    },
    {
      title: '车主身份证号',
      dataIndex: 'ownerIdCard',
    },
    {
      title: '车主手机号',
      dataIndex: 'phone',
    },
    {
      title: '是否有效',
      dataIndex: 'isValid',
      ellipsis: true,
      render: (text) => {
        return status[text];
      },
    },
    {
      title: '车辆颜色',
      dataIndex: 'color',
      ellipsis: true,
    },
    {
      title: '蓝牙连接标识',
      dataIndex: 'bluetooth',
      ellipsis: true,
    },
  ];
  render() {
    const { userId } = this.props;
    return (
      <div>
        <EasyTable
          autoFetch
          source={vehicleListById}
          dataProp={'data'}
          name={'carsTypeDataTable'}
          rowKey={'id'}
          columns={this.columns}
          fixedParams={{ id: userId }}
        />
      </div>
    );
  }
}

export default VehicleTable;
