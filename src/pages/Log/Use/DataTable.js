import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { connect } from 'dva';

import { getKeyLogList } from '@/services/keys';
import { Button } from 'antd';
import { DownloadOutlined } from '@ant-design/icons';
import { keyLogFlag, keyLogFlagBadge } from '@/constants/keys';

@connect(({ keysManage, loading }) => ({
  keysManage,
}))
class DataTable extends Component {
  columns = [
    {
      title: '序号',
      width: 80,
      render: (text, record, index) => {
        let currentIndex = this.keyLifecycleDataTable?.state?.currentIndex;
        let currentPageSize =
          this.keyLifecycleDataTable?.state?.currentPageSize;
        return (currentIndex - 1) * currentPageSize + (index + 1);
      },
    },
    {
      title: '车辆vin号',
      dataIndex: 'vin',
    },
    {
      title: '用户id',
      dataIndex: 'userId',
    },
    {
      title: '手机型号',
      dataIndex: 'phoneModel',
    },
    {
      title: '手机品牌',
      dataIndex: 'phoneBrand',
    },
    {
      title: '操作时间',
      dataIndex: 'operateTime',
    },
    {
      title: '操作类型',
      dataIndex: 'statusCode',
    },
    {
      title: '操作结果',
      dataIndex: 'flag',
      render: (text) => {
        return keyLogFlagBadge[text];
      },
    },
    {
      title: '失败原因',
      dataIndex: 'errorReason',
    },
  ];

  render() {
    return (
      <div>
        <EasyTable
          rowKey={'id'}
          scroll={{ x: '1200px' }}
          autoFetch
          source={getKeyLogList}
          dataProp={'data'}
          name={'keyErrorLogDataTable'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <div className={'btn-group'}>
              <Button type={'ghost'} size={'large'} icon={<DownloadOutlined />}>
                导出钥匙使用记录
              </Button>
            </div>
          }
        />
      </div>
    );
  }
}

export default DataTable;
