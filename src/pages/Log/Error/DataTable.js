import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { connect } from 'dva';
import { getKeyLogList } from '@/services/keys';

import { Popover, Tooltip, Typography } from 'antd';
import { TableHeaderColumn } from '@/utils/TableHeaderColumn';

@connect(({ keysLog, loading }) => ({
  keysLog,
}))
class DataTable extends Component {
  state = {
    endTime: '',
    flag: true,
  };
  columns = [
    {
      title: '序号',
      width: 80,
      render: (text, record, index) => {
        return TableHeaderColumn(text, record, index, this.dataTable);
      },
    },
    {
      title: '车辆vin号',
      dataIndex: 'vin',
      ellipsis: {
        showTitle: false,
      },
      render: (text) => (
        <Tooltip placement="topLeft" title={text}>
          {text}
        </Tooltip>
      ),
    },
    {
      title: '用户ID',
      dataIndex: 'userId',
      ellipsis: {
        showTitle: false,
      },
      render: (text) => (
        <Tooltip placement="topLeft" title={text}>
          {text}
        </Tooltip>
      ),
    },
    {
      title: '手机品牌',
      dataIndex: 'phoneBrand',
    },
    {
      title: '手机型号',
      dataIndex: 'phoneModel',
      ellipsis: {
        showTitle: false,
      },
      render: (text) => (
        <Tooltip placement="topLeft" title={text}>
          {text}
        </Tooltip>
      ),
    },
    {
      title: '业务类型',
      dataIndex: 'vehicleType',
    },
    {
      title: '故障原因',
      dataIndex: 'vehicleBrand',
    },
    {
      title: '日志记录方',
      dataIndex: 'vehicleModel',
    },
    {
      title: '记录时间',
      dataIndex: 'operateTime',
      ellipsis: {
        showTitle: false,
      },
      render: (text) => (
        <Tooltip placement="topLeft" title={text}>
          {text}
        </Tooltip>
      ),
    },
    {
      title: '操作',
      fixed: 'right',
      width: 300,
      render: () => {
        return (
          <Popover
            content={
              <>
                <Typography.Title
                  level={5}
                  style={{
                    margin: 0,
                  }}
                >
                  解决方案
                </Typography.Title>
                <p />
                <p>1：联系云端人员，确认该VIN号车辆是否下线</p>
                <p>2：查看手机日志，确认上传的VIN号是否匹配</p>
              </>
            }
            title=""
            trigger="click"
          >
            <a>详情</a>
          </Popover>
        );
      },
    },
  ];

  onCancel = () => {
    this.setState({
      showUserInfo: false,
    });
  };

  reload = () => {
    this.dataTable.reload();
  };

  render() {
    return (
      <>
        <EasyTable
          scroll={{ x: '1200px' }}
          source={getKeyLogList}
          dataProp={'data'}
          name={'keyErrorLogDataTable'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          rowKey={'id'}
        />
      </>
    );
  }
}

export default DataTable;
