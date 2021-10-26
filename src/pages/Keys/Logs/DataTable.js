import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import { getKeysLogsList } from '@/services/keys';
import { Link } from 'umi';

@connect(({ keysLogs, loading }) => ({
  keysLogs,
  // upserting: loading.effects['keysLogs/upsert'],
}))
class DataTable extends Component {
  state = {};
  columns = [
    {
      title: 'VIN',
      dataIndex: 'vin',
    },
  ];

  render() {
    return (
      <div>
        <EasyTable
          autoFetch
          source={getKeysLogsList}
          dataProp={'data'}
          name={'keysLogsDataTable'}
          rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
        />
      </div>
    );
  }
}

export default DataTable;
