import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import { vehicleListById } from '@/services/cars';
import DataTable from './DataTable';

class VehicleTable extends Component {
  constructor(props) {
    super(props);
  }
  state = {
    id: this.props.userId,
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
  ];
  render() {
    console.log(this.props);
    return (
      <div>
        <EasyTable
          autoFetch
          source={vehicleListById}
          dataProp={'data'}
          name={'carsTypeDataTable'}
          fixedParams={{ id: this.state.id }}
          rowKey={'id'}
          columns={this.columns}
        />
      </div>
    );
  }
}

export default VehicleTable;
