import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { connect } from 'dva';

import { getKeyLogList, checkKeyUseLog } from '@/services/keys';

import {
  Button,
  Modal,
  Row,
  Col,
  Table,
  Space,
  Tag,
  Popover,
  message,
} from 'antd';
import { DownloadOutlined, CloudDownloadOutlined } from '@ant-design/icons';
import Authorized from '@/components/Authorized';
import { getKeyLogList } from '@/services/keys';
import { Button } from 'antd';
import { DownloadOutlined } from '@ant-design/icons';
import { keyLogFlag, keyLogFlagBadge } from '@/constants/keys';
import { LOG_USE_EXPORT } from '@/components/Authorized/AuthMap';
import DescriptionList from '@/components/DescriptionList';
import { exportKeyUseLog } from '@/services/exportKeyUseLog';
import { exportStatus } from '@/constants/export';
import { getDvaApp } from '@@/plugin-dva/exports';
import moment from 'moment';
import { downloadExcel } from '@/services/downloadExcel';
const { Description } = DescriptionList;
const download = (col) => {
  // debugger;
  console.log(col);
  let param = new URLSearchParams();
  let fileName = col.missionName;
  if (fileName) {
    param.append('fileName', fileName);
  }

  downloadExcel(param).then((res) => {
    let blob = new Blob([res.data]);
    let link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = fileName + '.xlsx';
    link.click();
    window.URL.revokeObjectURL(link.href);
  });
};
const SubTable = () => {
  const columns1 = [
    // {
    //   title: '序号',
    //   width: 80,
    //   render: (text, record, index) => {
    //     let currentIndex = this.checkKeyUseLogTable?.state?.currentIndex;
    //     let currentPageSize = this.checkKeyUseLogTable?.state?.currentPageSize;
    //     return (currentIndex - 1) * currentPageSize + (index + 1);
    //   },
    // },
    {
      title: '导出状态',
      dataIndex: 'exportStatus',
      render: (text) => {
        return exportStatus[text];
      },
    },
    {
      title: '任务名称',
      dataIndex: 'missionName',
    },
    {
      title: '任务创建时间',
      dataIndex: 'createTime',
    },
    {
      title: '操作人',
      dataIndex: 'creator',
    },
    {
      title: '操作',
      dataIndex: '',
      render: (col) => {
        return (
          <div className={'link-group'}>
            <Popover title="" trigger="click">
              <a onClick={() => download(col)}>下载</a>
            </Popover>
          </div>
        );
      },
    },
  ];

  // console.log("props ",props);
  return (
    <EasyTable
      rowKey={'id'}
      scroll={{ x: '800px' }}
      autoFetch
      source={checkKeyUseLog}
      dataProp={'data'}
      name={'checkKeyUseLogTable'}
      columns={columns1}
      // wrappedComponentRef={(ref) => (this?.checkKeyUseLogTable = ref)}
    />
  );
};

@connect(({ keysManage, loading }) => ({
  keysManage,
}))
class DataTable extends Component {
  columns = [
    {
      title: '序号',
      width: 80,
      render: (text, record, index) => {
        let currentIndex = this.dataTable?.state?.currentIndex;
        let currentPageSize = this.dataTable?.state?.currentPageSize;
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
      title: '手机品牌',
      dataIndex: 'phoneBrand',
    },
    {
      title: '手机型号',
      dataIndex: 'phoneModel',
    },
    {
      title: '车辆品牌',
      dataIndex: 'vehicleBrand',
    },
    {
      title: '车辆型号',
      dataIndex: 'vehicleModel',
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

  onCancel = () => {
    this.setState({
      showUserInfo: false,
    });
  };
  openModalExport = () => {
    this.setState({
      showUserInfo: true,
    });
  };

  exportExcel = () => {
    let creator = getDvaApp()._store.getState().user.currentUser.username;
    let vin = this.props.searchFormValues[0];
    let phoneBrand = this.props.searchFormValues[1];
    let phoneModel = this.props.searchFormValues[2];
    let statusCode = this.props.searchFormValues[3];
    let startTime = this.props.searchFormValues[4];
    let flag = this.props.searchFormValues[5];
    let userId = this.props.searchFormValues[6];
    let vehicleBrand = this.props.searchFormValues[7];
    let vehicleModel = this.props.searchFormValues[8];
    let fileName = '钥匙记录.xlsx';
    let param = new URLSearchParams();

    if (vin && vin.value) {
      param.append('vin', vin.value);
    }
    if (phoneBrand && phoneBrand.value) {
      param.append('phoneBrand', phoneBrand.value);
    }
    if (phoneModel && phoneModel.value) {
      param.append('phoneModel', phoneModel.value);
    }
    if (statusCode && statusCode.value) {
      param.append('statusCode', statusCode.value);
    }

    if (startTime && startTime.value[0]) {
      const beginTime = moment(startTime.value[0]).format('YYYY-MM-DD');
      param.append('startTime', beginTime);
    }
    if (startTime && startTime.value[1]) {
      const endTime = moment(startTime.value[1]).format('YYYY-MM-DD');
      param.append('endTime', endTime);
    }
    if (flag && flag.value) {
      param.append('flag', flag.value);
    }
    if (vehicleModel && vehicleModel.value) {
      param.append('vehicleModel', vehicleModel.value);
    }
    if (vehicleBrand && vehicleBrand.value) {
      param.append('vehicleBrand', vehicleBrand.value);
    }
    if (creator) {
      param.append('creator', creator);
    }

    exportKeyUseLog(param).then((res) => {
      let blob = new Blob([res.data]);

      let link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = fileName;
      // link.click();
      window.URL.revokeObjectURL(link.href);
    });
    message.info('正在导出');
  };

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
            <Authorized route={LOG_USE_EXPORT}>
              <div className={'btn-group'}>
                <Button
                  type={'ghost'}
                  size={'large'}
                  icon={<DownloadOutlined />}
                  onClick={() => this.exportExcel()}
                >
                  导出钥匙使用记录
                </Button>
                <Button
                  onClick={this.openModalExport}
                  type={'ghost'}
                  size={'large'}
                  icon={<DownloadOutlined />}
                >
                  历史导出列表
                </Button>
              </div>
            </Authorized>
          }
        />

        <Modal
          footer={null}
          title={'历史导出列表'}
          visible={this.state?.showUserInfo}
          destroyOnClose={true}
          onCancel={this.onCancel}
          width={1000}
        >
          <SubTable />
        </Modal>
      </div>
    );
  }
}

export default DataTable;
