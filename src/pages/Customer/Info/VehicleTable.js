import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { vehicleListById } from '@/services/customer';
import { status } from '@/constants/user';
import { checkResult, windowType, sunroofType, color } from '@/constants/cars';
class VehicleTable extends Component {
  columns = [
    {
      title: 'VIN号',
      dataIndex: 'vin',
      width: 200,
    },
    {
      title: '发动机号',
      dataIndex: 'ven',
      width: 200,
    },
    {
      title: '车身颜色',
      dataIndex: 'color',
      width: 100,
      render: (text) => {
        return color[text];
      },
    },
    {
      title: '天窗类型',
      dataIndex: 'sunroofType',
      width: 120,
      render: (text) => {
        return sunroofType[text];
      },
    },
    {
      title: '后备门类型',
      dataIndex: 'trunkType',
      width: 150,
    },
    {
      title: '车窗类型',
      dataIndex: 'windowType',
      width: 120,
      render: (text) => {
        return windowType[text];
      },
    },
    {
      title: '检测结果',
      dataIndex: 'checkResult',
      width: 100,
      render: (text) => {
        return checkResult[text];
      },
    },
    {
      title: '蓝牙编号',
      dataIndex: 'bleNo',
      width: 150,
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
          name={'vehicleDataTable'}
          rowKey={'id'}
          columns={this.columns}
          fixedParams={{ id: userId }}
        />
      </div>
    );
  }
}

export default VehicleTable;
