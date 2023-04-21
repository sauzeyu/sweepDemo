import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { connect } from 'dva';

import { Popover, Tooltip, Typography } from 'antd';
import { TableHeaderColumn } from '@/utils/TableHeaderColumn';
import { selectForPage } from '@/services/diagnosis';

@connect(({ Diagnosis, loading }) => ({
  Diagnosis,
}))
class DataTable extends Component {
  state = {
    businessList: [],
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
      dataIndex: 'functionalAbnormality',
      ellipsis: {
        showTitle: false,
      },
      render: (functionalAbnormality) => (
        <Tooltip placement="topLeft" title={functionalAbnormality.business}>
          {functionalAbnormality.business}
        </Tooltip>
      ),
    },
    {
      title: '故障原因',
      dataIndex: 'functionalAbnormality',
      ellipsis: {
        showTitle: false,
      },
      render: (functionalAbnormality) => (
        <Tooltip placement="topLeft" title={functionalAbnormality.fault}>
          {functionalAbnormality.fault}
        </Tooltip>
      ),
    },
    {
      title: '日志记录方',
      dataIndex: 'functionalAbnormality',
      ellipsis: {
        showTitle: false,
      },
      render: (functionalAbnormality) => (
        <Tooltip placement="topLeft" title={functionalAbnormality.source}>
          {functionalAbnormality.source}
        </Tooltip>
      ),
    },
    {
      title: '记录时间',
      dataIndex: 'time',
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
      render: (col) => {
        let solutionList = col?.functionalAbnormality?.solution;
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
                {solutionList &&
                  solutionList instanceof Array &&
                  solutionList?.map((item, index) => {
                    return (
                      <p key={index}>
                        {index + 1}：{item}
                      </p>
                    );
                  })}
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

  selectBusiness = () => {
    this.props
      .dispatch({
        type: 'Diagnosis/selectBusiness',
        payload: {},
      })
      .then((res) => {
        if (res && res.code === 200) {
          this.setState({
            businessList: res.data,
          });
        }
      });
  };

  componentWillMount() {
    this.selectBusiness();
  }

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
          source={selectForPage}
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
