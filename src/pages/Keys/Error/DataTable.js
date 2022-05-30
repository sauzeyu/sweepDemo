import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { connect } from 'dva';

import { getKeyErrorLogList } from '@/services/keys';

@connect(({ keysManage, loading }) => ({
  keysManage,
}))
class DataTable extends Component {
  columns = [
    {
      title: '车辆vin号',
      dataIndex: 'vin',
      width: 300,
    },
    {
      title: '操作时间',
      dataIndex: 'operateTime',
      width: 300,
    },
    {
      title: '故障类型',
      dataIndex: 'statusCode',
      width: 300,
    },
    {
      title: '错误原因',
      dataIndex: 'errorReason',
      width: 300,
    },
    {
      title: '操作',
      fixed: 'right',
      width: 300,
      render: (col) => {
        return (
          <div className={'link-group'}>
            <a className={'text-danger'} onClick={() => this.revokeKey(col)}>
              删除
            </a>
          </div>
        );
      },
    },
  ];

  render() {
    return (
      <div>
        <EasyTable
          rowKey={'id'}
          scroll={{ x: '1200px' }}
          autoFetch
          source={getKeyErrorLogList}
          dataProp={'data'}
          name={'keyErrorLogDataTable'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
        />
      </div>
    );
  }
}

export default DataTable;
