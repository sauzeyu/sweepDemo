import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { vehicleListById } from '@/services/customer';
import { status } from '@/constants/user';
import { windowType, sunroofType, color } from '@/constants/cars';
import { Badge, Tag, Space, Typography, Divider } from 'antd';
import { KeyType } from '@/constants/keys';

class VehicleTable extends Component {
  columns = [
    {
      title: 'VIN号',
      dataIndex: 'vin',
      width: 200,
    },
    {
      title: '车牌号',
      dataIndex: 'license',
      width: 200,
    },
    {
      title: '用车类型',
      dataIndex: 'vehicleType',
      width: 100,
    },
  ];

  render() {
    const { user } = this.props;
    return (
      <div>
        <EasyTable
          autoFetch
          source={vehicleListById}
          dataProp={'data'}
          name={'vehicleDataTable'}
          rowKey={'id'}
          columns={this.columns}
          fixedParams={{ id: user?.id }}
          renderHeader={(title, extra, page) => {
            let total = page.total;
            total = '共 ' + total + ' 条记录';
            return (
              <>
                <p>
                  <Badge color="pink" text="用户姓名：" />
                  {user?.username}
                  <Divider type="vertical" />
                  <Badge color="green" text="电话：" />
                  {user?.phone}
                </p>
                <div>
                  <Badge color="blue" text={total} />
                </div>
              </>
            );
          }}
        />
      </div>
    );
  }
}

export default VehicleTable;
