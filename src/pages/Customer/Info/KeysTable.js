import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { keyListByUserId } from '@/services/customer';
import { status } from '@/constants/user';
import { analyzePermissions, DKState, KeyType } from '@/constants/keys';
import { Tag } from 'antd';

class VehicleTable extends Component {
  columns = [
    {
      title: '钥匙类型',
      dataIndex: 'parentId',
      render: (text) => {
        return KeyType(text);
      },
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
      title: '钥匙权限',
      dataIndex: 'permissions',
      width: 300,
      render: (text) => {
        return analyzePermissions(text);
      },
    },
    {
      title: '申请时间',
      dataIndex: 'applyTime',
      width: 200,
    },
  ];

  render() {
    const { user } = this.props;
    return (
      <div>
        <EasyTable
          autoFetch
          source={keyListByUserId}
          dataProp={'data'}
          name={'keysDataTable'}
          rowKey={'id'}
          columns={this.columns}
          fixedParams={{ id: user?.id }}
        />
      </div>
    );
  }
}

export default VehicleTable;
