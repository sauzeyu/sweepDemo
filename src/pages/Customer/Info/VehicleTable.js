import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import { getCarModes } from '@/services/cars';

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
      title: '车型名称',
      dataIndex: 'name',
      ellipsis: true,
    },
    {
      title: '代码',
      dataIndex: 'code',
      ellipsis: true,
    },
    {
      title: 'VIN号匹配规则',
      dataIndex: 'vinMatch',
    },
    {
      title: '备注',
      dataIndex: 'remark',
      ellipsis: true,
    },
    {
      title: '操作',
      render: (col) => {
        return (
          <div className={'link-group'}>
            <a onClick={() => this.del(col)}>删除</a>
            <a onClick={() => this.upsert(col)}>编辑</a>
          </div>
        );
      },
    },
  ];
  render() {
    return (
      <div>
        <EasyTable
          autoFetch
          source={getCarModes}
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
