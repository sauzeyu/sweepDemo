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
      title: '车型名称',
      dataIndex: 'modelName',
    },
    {
      title: '车辆VIN',
      dataIndex: 'vin',
    },
    {
      title: '用户名称',
      dataIndex: 'userName',
    },
    {
      title: '类型',
      dataIndex: 'type',
    },
    {
      title: '结果',
      dataIndex: 'result',
    },
    {
      title: '失败原因',
      dataIndex: 'reason',
    },
    {
      title: '日期',
      dataIndex: 'date',
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
