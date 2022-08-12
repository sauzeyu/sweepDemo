import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { selectAftermarketReplacementByVin } from '@/services/aftermarketReplacement';
class HistoryBluetooth extends Component {
  columns = [
    {
      title: '车辆vin号',
      dataIndex: 'vin',
      width: 150,
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
      title: '旧蓝牙设备序列号',
      dataIndex: 'oldBluetoothSn',
      width: 110,
    },
    {
      title: '新蓝牙设备序列号',
      dataIndex: 'newBluetoothSn',
      width: 150,
    },
    {
      title: '换件时间',
      dataIndex: 'replacementTime',
      width: 110,
    },
  ];

  render() {
    const { vin } = this.props;
    return (
      <div>
        <EasyTable
          autoFetch
          source={selectAftermarketReplacementByVin}
          dataProp={'data'}
          name={'historyAftermarketDataTable'}
          rowKey={'id'}
          columns={this.columns}
          fixedParams={{ vin: vin }}
        />
      </div>
    );
  }
}

export default HistoryBluetooth;
