import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import { keyListById } from '@/services/cars';
import { DKState, KeysState, Permit } from '@/constants/keys';

@connect(({ carsType, loading }) => ({
  carsType,
  upserting: loading.effects['carsType/upsert'],
}))
class VehicleTable extends Component {
  state = {
    idList: [],
  };
  columns = [
    {
      title: '手机设备指纹',
      dataIndex: 'devFp',
      width: 260,
    },
    {
      title: '电话',
      dataIndex: 'phone',
      width: 130,
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
      title: '失效时间',
      dataIndex: 'valTo',
      width: 400,
    },
    {
      title: '授权权限值',
      dataIndex: 'permit',
      width: 300,
      render: (text) => {
        return Permit[text];
      },
    },
    {
      title: '附加信息',
      dataIndex: 'serverPersonal',
      width: 200,
    },
    {
      title: '操作状态',
      dataIndex: 'status',
      width: 200,
      render: (text) => {
        return KeysState[text];
      },
    },
    {
      title: '预配对值',
      dataIndex: 'pp',
      width: 200,
    },
  ];

  render() {
    const { selectedCarId } = this.props;
    return (
      <div>
        <EasyTable
          autoFetch
          source={() => {
            return keyListById(selectedCarId);
          }}
          dataProp={'data'}
          name={'carsTypeDataTable'}
          rowKey={'id'}
          columns={this.columns}
        />
      </div>
    );
  }
}

export default VehicleTable;
