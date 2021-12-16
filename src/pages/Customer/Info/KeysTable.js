import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { keyListByUserId } from '@/services/customer';
import { status } from '@/constants/user';
import {
  analyzePermissions,
  DKState,
  KeysState,
  Permit,
} from '@/constants/keys';

class VehicleTable extends Component {
  columns = [
    {
      title: '手机设备指纹',
      dataIndex: 'phoneFingerprint',
      width: 250,
    },
    {
      title: '数字钥匙ID',
      dataIndex: 'keyOwnerId',
      width: 220,
    },
    {
      title: '具体状态',
      dataIndex: 'dkState',
      width: 200,
      render: (text) => {
        return DKState[text];
      },
    },
    {
      title: '生效时间',
      dataIndex: 'valFrom',
      width: 200,
    },
    {
      title: '失效时间',
      dataIndex: 'valTo',
      width: 200,
    },
    {
      title: '授权权限',
      dataIndex: 'permissions',
      width: 300,
      render: (text) => {
        return analyzePermissions(text);
      },
    },
    {
      title: '附加信息',
      dataIndex: 'serverPersonal',
      width: 200,
    },
    {
      title: '申请时间',
      dataIndex: 'applyTime',
      width: 200,
    },
    {
      title: '操作状态',
      dataIndex: 'keyStatus',
      width: 200,
      render: (text) => {
        return KeysState[text];
      },
    },
    {
      title: '预配对值',
      dataIndex: 'pp',
    },
  ];

  render() {
    const { userId } = this.props;
    return (
      <div>
        <EasyTable
          autoFetch
          source={keyListByUserId}
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
