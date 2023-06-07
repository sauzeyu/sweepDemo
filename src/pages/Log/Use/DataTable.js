import React, { Component, useState } from 'react';
import EasyTable from '@/components/EasyTable';
import { connect } from 'dva';
import { Code } from '@/constants/statusCode';
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
  Tooltip,
} from 'antd';
import {
  DownloadOutlined,
  CloudDownloadOutlined,
  UpOutlined,
  DownOutlined,
  BarsOutlined,
} from '@ant-design/icons';
import Authorized from '@/components/Authorized';
import {
  keyLogFlag,
  keyLogFlagBadge,
  QuickFlag,
  SimplekeyLogFlag,
} from '@/constants/keys';
import { LOG_USE_EXPORT } from '@/components/Authorized/AuthMap';
import DescriptionList from '@/components/DescriptionList';
import { exportKeyUseLog } from '@/services/exportKeyUseLog';
import { exportStatus } from '@/constants/export';
import { getDvaApp } from '@@/plugin-dva/exports';
import moment from 'moment';
import { downloadExcel } from '@/services/downloadExcel';
import { TableHeaderColumn } from '@/utils/TableHeaderColumn';

const { Description } = DescriptionList;
const dateFormat = 'YYYY/MM/DD';

const download = (col) => {
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
  // @ts-ignore
  const [loadings, setLoadings] = useState([]);

  const enterLoading = (index, text) => {
    download(text);
    setLoadings((prevLoadings) => {
      const newLoadings = [...prevLoadings];
      newLoadings[index] = true;
      return newLoadings;
    });
    setTimeout(() => {
      setLoadings((prevLoadings) => {
        const newLoadings = [...prevLoadings];
        newLoadings[index] = false;
        return newLoadings;
      });
    }, 3000);
  };
  const columns1 = [
    // {
    //   title: '序号',
    //   width: 80,
    //   render: (text, record, index) => {
    //     let currentIndex = this.checkKeyUseLogTable?.state?.currentIndex;
    //     let currentPageSize = this.checkKeyUseLogTable?.state?.currentPageSize;
    //     return (currentIndex - 1) * currentPageSize + (index + 1);
    //
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
      ellipsis: true,
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
      render: (text, record, index) => {
        return (
          <div className={'link-group'}>
            {/*<Popover title="" trigger="click">*/}
            <Button
              icon={<DownloadOutlined />}
              loading={loadings[index]}
              onClick={() => enterLoading(index, text)}
            >
              下载
            </Button>
            {/*</Popover>*/}
          </div>
        );
      },
    },
  ];

  let creator = getDvaApp()._store.getState().user.currentUser.username;
  return (
    <EasyTable
      rowKey={'id'}
      scroll={{ x: '800px' }}
      autoFetch
      source={checkKeyUseLog}
      fixedParams={{ creator: creator, type: 0 }}
      dataProp={'data'}
      name={'checkKeyUseLogTable'}
      columns={columns1}
      // wrappedComponentRef={(ref) => (this?.checkKeyUseLogTable = ref)}
    />
  );
};

@connect(({ keysLog, loading }) => ({
  keysLog,
}))
class DataTable extends Component {
  state = {
    // startTime:'',
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
      title: '用户id',
      dataIndex: 'userId',
      ellipsis: {
        showTitle: false,
      },
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
    // {
    //   title: '车型',
    //   dataIndex: 'vehicleType',
    // },
    // {
    //   title: '车辆品牌',
    //   dataIndex: 'vehicleBrand',
    // },
    {
      title: '车辆型号',
      dataIndex: 'vehicleModel',
    },

    {
      title: '日志类型',
      dataIndex: 'quickFlag',
      render: (text) => {
        if (text === 1) {
          return <Tag color="green">普通</Tag>;
        } else {
          return <Tag color="blue">快连</Tag>;
        }
        // return QuickFlag[text];
      },
    },

    {
      title: '操作时间',
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
    // {
    //   title: '操作码',
    //   dataIndex: 'statusCode',
    //   ellipsis: {
    //     showTitle: false,
    //   },
    //   render: (text) => (
    //     <Tooltip placement="topLeft" title={text}>
    //       {text}
    //     </Tooltip>
    //   ),
    // },
    {
      title: '操作类型',
      dataIndex: 'operationType',
    },
    {
      title: '操作结果',
      dataIndex: 'flag',
      render: (text) => {
        return keyLogFlagBadge[text];
      },
    },
    {
      title: '故障类别',
      dataIndex: 'errorType',
      render: (text) => {
        if (text === '01') {
          return <Tag color="blue">控车</Tag>;
        } else if (text === '02') {
          return <Tag color="green">蓝牙连接</Tag>;
        } else if (text === '03') {
          return <Tag color="yellow">钥匙管理</Tag>;
        }
        // return QuickFlag[text];
      },
    },
    {
      title: '失败原因',
      dataIndex: 'errorReason',
      ellipsis: {
        showTitle: false,
      },
      render: (text) => (
        <Tooltip placement="topLeft" title={text}>
          {text}
        </Tooltip>
      ),
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

  confirmExportExcel = () => {
    let creator = getDvaApp()._store.getState().user.currentUser.username;

    const {
      startTime,
      phoneBrand,
      phoneModel,
      statusCode,
      vin,
      flag,
      userId,
      vehicleBrand,
      vehicleModel,
      vehicleType,
    } = this.props.searchFormValues;
    let beginTime;
    let endTime;
    let statusCodeEnmu = [];
    let flagEnmu;
    if (startTime?.length === 2) {
      beginTime = moment(startTime[0]).format('YYYY-MM-DD');
      endTime = moment(startTime[1]).format('YYYY-MM-DD');
    }
    if (statusCode) {
      for (let i = 0; i < Code.length; i++) {
        for (let index = 0; index < statusCode.length; index++) {
          if (statusCode[index] == Code[i][0]) {
            statusCodeEnmu.push(Code[i][1]);
          }
        }
      }
    }

    if (flag != null) {
      flagEnmu = SimplekeyLogFlag[flag];
    }
    Modal.confirm({
      title: '确定导出钥匙信息?',

      content: (
        <>
          操作时间:&nbsp;
          {beginTime} ~ {endTime}
          <br />
          手机品牌:&nbsp;
          {phoneBrand}
          <br />
          手机型号:&nbsp;
          {phoneModel}
          <br />
          操作类型:&nbsp;
          {statusCodeEnmu.map((statusCode) => statusCode + ' ')}
          <br />
          车辆vin号:&nbsp;
          {vin}
          <br />
          操作结果:&nbsp;
          {flagEnmu}
          <br />
          用户id:&nbsp;
          {userId}
          <br />
          {/* 车辆品牌:&nbsp;
          {vehicleBrand}
          <br /> */}
          车辆型号:&nbsp;
          {vehicleModel}
          <br />
          {/* 车型&nbsp;
          {vehicleType} */}
          <br />
        </>
      ),

      onOk: () => {
        return this.exportExcel();
      },
      okText: '导出',
    });
  };
  exportExcel = () => {
    let creator = getDvaApp()._store.getState().user.currentUser.username;

    const {
      startTime,
      phoneBrand,
      phoneModel,
      statusCode,
      vin,
      flag,
      userId,
      vehicleBrand,
      vehicleModel,
      vehicleType,
      // quickFlag,
    } = this.props.searchFormValues;

    let fileName = '钥匙记录.xlsx';
    let param = new URLSearchParams();

    if (vin) {
      param.append('vin', vin);
    }
    if (phoneBrand) {
      param.append('phoneBrand', phoneBrand);
    }
    if (phoneModel) {
      param.append('phoneModel', phoneModel);
    }
    if (statusCode) {
      if (statusCode.length > 0) {
        param.append('statusCode', statusCode);
      }
    }

    if (startTime?.length === 2) {
      const beginTime = moment(startTime[0]).format('YYYY-MM-DD 00:00:00');

      param.append('startTime', beginTime);

      const endTime = moment(startTime[1])
        .add(1, 'days')
        .format('YYYY-MM-DD 00:00:00');
      param.append('endTime', endTime);
    }
    if (flag == 0 || flag == 1) {
      param.append('flag', flag);
    }
    // if (quickFlag == 0 || quickFlag == 1) {
    //   param.append('quickFlag', quickFlag);
    // }
    if (userId) {
      param.append('userId', userId);
    }
    if (vehicleModel) {
      param.append('vehicleModel', vehicleModel);
    }
    if (vehicleBrand) {
      param.append('vehicleBrand', vehicleBrand);
    }
    if (vehicleType) {
      param.append('vehicleType', vehicleType);
    }
    if (creator) {
      param.append('creator', creator);
    }

    exportKeyUseLog(param).then((res) => {
      let blob = null;
      if (res.data) {
        blob = new Blob([res.data]);

        let link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = fileName;
        // link.click();
        window.URL.revokeObjectURL(link.href);
      }
      if (blob) {
        message.info('正在导出钥匙日志信息，详情在历史导出列表查看');
      }
    });
  };
  reload = () => {
    this.dataTable.reload();
  };
  defaultStartTime = () => {
    let defaultTime = [];

    let now = new Date(); //当前日期
    let nowMonth = now.getMonth(); //当前月
    let nowYear = now.getFullYear(); //当前年
    //本月的开始时间
    let monthStartDate = new Date(nowYear, nowMonth, 1);
    //本月的结束时间
    let monthEndDate = new Date(nowYear, nowMonth + 1, 0);

    defaultTime[0] = monthStartDate;
    defaultTime[1] = monthEndDate;
    return defaultTime;
  };

  formatDateTime(date) {
    if (date == '' || !date) {
      return '';
    }
    // var date = new Date(date);
    let y = date.getFullYear();
    let m = date.getMonth() + 1;
    m = m < 10 ? '0' + m : m;
    let d = date.getDate();
    d = d < 10 ? '0' + d : d;

    return y + '-' + m + '-' + d + ' 00:00:00';
  }

  clearEndTime = () => {
    this.setState({
      endTime: null,
    });
    this.state.endTime = null;
  };
  initEndTime = () => {
    let defaultStartTimeElement = this.defaultStartTime()[1];
    let end = this.formatDateTime(defaultStartTimeElement);
    this.setState({
      endTime: end,
    });
  };

  render() {
    return (
      <div>
        <EasyTable
          scroll={{ x: '1200px' }}
          // autoFetch
          source={getKeyLogList}
          dataProp={'data'}
          name={'keyUseLogDataTable'}
          columns={this.columns}
          // fixedParams={{ startTime: startTime, endTime: endTime }}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          // onDataLoaded={this.clearEndTime()}
          extra={
            <Authorized route={LOG_USE_EXPORT}>
              <div className={'btn-group'}>
                <Button
                  type={'ghost'}
                  size={'large'}
                  icon={<DownloadOutlined />}
                  onClick={() => this.confirmExportExcel()}
                  // onClick={() => this.exportExcel()}
                >
                  导出钥匙使用记录
                </Button>
                <Button
                  onClick={this.openModalExport}
                  type={'ghost'}
                  size={'large'}
                  icon={<BarsOutlined />}
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
