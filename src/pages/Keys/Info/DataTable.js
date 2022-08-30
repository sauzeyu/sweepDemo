import { getDvaApp } from '@@/plugin-dva/exports';
import React, { Component, useState } from 'react';
import EasyTable from '@/components/EasyTable';
import Authorized from '@/components/Authorized';
import moment from 'moment';
import {
  Badge,
  Button,
  message,
  Modal,
  Tag,
  Popover,
  Card,
  Tooltip,
} from 'antd';
import {
  DownloadOutlined,
  DownOutlined,
  UpOutlined,
  BarsOutlined,
} from '@ant-design/icons';
import { connect } from 'dva';
import {
  getKeysList,
  selectVehicleById,
  checkKeyUseLog,
} from '@/services/keys';
import { DKState, KeySource, KeyState, KeyType } from '@/constants/keys';
import { exportStatus } from '@/constants/export';
import { keyLifecycleList, keyUseListById } from '@/services/cars';
import { useMemo } from 'react';
import {
  KEYS_INFO_EXPORT,
  KEYS_INFO_FREEZE,
  KEYS_INFO_REVOKE,
  KEYS_INFO_THAW,
} from '@/components/Authorized/AuthMap';
import { exportKey } from '@/services/exportKey';
import { downloadExcel } from '@/services/downloadExcel';

const download = (col) => {
  console.log(col);
  debugger;
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

const SubTable2 = () => {
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
      ellipsis: true,
      render: (text) => (
        <Tooltip placement="topLeft" title={text}>
          {text}
        </Tooltip>
      ),
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

  // console.log("props ",props);
  let creator = getDvaApp()._store.getState().user.currentUser.username;
  return (
    <EasyTable
      rowKey={'id'}
      scroll={{ x: '800px' }}
      autoFetch
      source={checkKeyUseLog}
      fixedParams={{ creator: creator, type: 1 }}
      dataProp={'data'}
      name={'checkKeyUseLogTable'}
      columns={columns1}
      // wrappedComponentRef={(ref) => (this?.checkKeyUseLogTable = ref)}
    />
  );
};
const SubTable = (props) => {
  const dataList = React.useRef();

  const columns = [
    {
      title: '操作时间',
      dataIndex: 'createTime',
      width: 350,
    },
    {
      title: '操作状态',
      dataIndex: 'keyStatus',
      width: 160,
      render: (text) => {
        return KeyState[text];
      },
    },
    {
      title: '吊销来源',
      dataIndex: 'keySource',
      width: 160,
      render: (text) => {
        return KeySource[text];
      },
    },
  ];
  const name = useMemo(() => `carSubModelsDataTable_${props.id}`, [props.id]);
  return (
    <Card>
      <EasyTable
        columns={columns}
        rowKey={'id'}
        source={keyLifecycleList}
        name={name}
        autoFetch
        fixedParams={{ keyId: props.id }}
        renderHeader={() => null}
        wrappedComponentRef={() => {
          return dataList;
        }}
      />
    </Card>
  );
};

@connect(({ keysManage, loading }) => ({
  keysManage,
}))
class DataTable extends Component {
  state = {
    carInfo: {},
  };

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
      title: '钥匙类型',
      dataIndex: 'parentId',
      render: (text) => {
        return KeyType(text);
      },
    },
    {
      title: '用户id',
      dataIndex: 'userId',
    },
    {
      title: '车辆vin号',
      dataIndex: 'vin',
      width: 180,
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
      title: '钥匙状态',
      dataIndex: 'dkState',
      render: (text) => {
        return DKState[text];
      },
    },
    {
      title: '钥匙开始时间',
      dataIndex: 'valFrom',
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
      title: '钥匙结束时间',
      dataIndex: 'valTo',
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
      title: '申请时间',
      dataIndex: 'applyTime',
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
      title: '周期时长',
      dataIndex: 'period',
      render: (col) => this.time(col),
    },
    {
      title: '操作',
      fixed: 'right',
      width: 240,
      render: (col) => {
        let isDisable = col.dkState === 3;
        let disableStyle = {};

        if (col.dkState === 5 || col.dkState === 4) {
          disableStyle = {
            onClick: () => {},
            style: { opacity: 0.2, cursor: 'not-allowed', color: 'gray' },
          };
        }
        return (
          <div className={'link-group'}>
            <Popover
              content={
                <>
                  <p>车辆vin号：{col?.vin}</p>
                  <p>车辆型号：{this.state.carInfo?.vehicleModel}</p>
                  <p>车辆品牌：{this.state.carInfo?.vehicleBrand}</p>
                </>
              }
              title=""
              trigger="click"
            >
              <a onClick={() => this.carInfo(col)}>查看车辆</a>
            </Popover>
            <Authorized route={KEYS_INFO_THAW}>
              <a
                onClick={() => this.enableKey(col, true)}
                hidden={!isDisable}
                {...disableStyle}
              >
                解冻
              </a>
            </Authorized>
            <Authorized route={KEYS_INFO_FREEZE}>
              <a
                className={'text-danger'}
                onClick={() => this.enableKey(col, false)}
                hidden={isDisable}
                {...disableStyle}
              >
                冻结
              </a>
            </Authorized>
            <Authorized route={KEYS_INFO_REVOKE}>
              <a
                className={'text-danger'}
                onClick={() => this.revokeKey(col)}
                hidden={isDisable}
                {...disableStyle}
              >
                吊销
              </a>
            </Authorized>
          </div>
        );
      },
    },
  ];

  time = (value) => {
    let zone = '';
    let time = [];
    let day = parseInt(value / 60 / 24);
    let hour = parseInt((value / 60) % 24);
    let min = parseInt(value % 60);
    time[0] = day > 0 ? day : 0;
    time[1] = hour > 0 ? hour : 0;
    time[2] = min > 0 ? parseFloat(min) : 0;
    if (time[0] != 0) {
      zone += time[0] + '天';
    }
    if (time[1] != 0) {
      zone += time[1] + '时';
    }
    if (time[2] != 0) {
      zone += time[2] + '分';
    }
    return zone;
  };
  carInfo = (col) => {
    this.setState({
      carInfo: {},
    });
    selectVehicleById(col.vehicleId).then(
      (res) => {
        this.setState({ carInfo: res.data });
      },
      (err) => {
        message.error(err.message);
      },
    );
  };
  enableKey = (col, isEnableKey) => {
    let txt = isEnableKey ? '解冻' : '冻结';
    Modal.confirm({
      title: txt + '钥匙',
      content: `确定${txt}钥匙？`,
      onOk: () => {
        return this.props
          .dispatch({
            type: 'keysManage/enableKey',
            payload: {
              keyId: col.id,
              dkState: isEnableKey ? 1 : 3,
              userId: col.userId,
            },
          })
          .then(
            (res) => {
              if (res.code === 200) {
                message.success(res.msg);
              } else {
                message.error(res.msg);
              }
              this.dataTable.reload();
            },
            (err) => {
              message.error(err.message);
            },
          );
      },
    });
  };
  revokeKey = (col) => {
    Modal.confirm({
      title: '吊销钥匙',
      content: '确定吊销钥匙?',
      onOk: () => {
        this.props
          .dispatch({
            type: 'keysManage/revokeKey',
            payload: col.id,
          })
          .then(
            () => {
              message.success('操作成功');
              this.dataTable.refresh();
            },
            (err) => {
              message.error(err.message);
            },
          );
      },
    });
  };
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

    console.log('this.props.searchFormValues ', this.props.searchFormValues);
    let obj = Object.keys(this.props.searchFormValues);
    console.log('obj ', obj);

    let applyTime = this.props.searchFormValues[0];
    let vin = this.props.searchFormValues[1];
    let periodMin = this.props.searchFormValues[2];
    let periodMax = this.props.searchFormValues[3];
    let periodUnit = this.props.searchFormValues[4];

    let userId = this.props.searchFormValues[5];

    let valFromTime = this.props.searchFormValues[6];

    let valToTime = this.props.searchFormValues[7];

    let keyType = this.props.searchFormValues[8];

    let dkState = this.props.searchFormValues[9];

    let fileName = '钥匙信息.xlsx';
    let param = new URLSearchParams();

    if (keyType?.value && keyType.value.length > 0) {
      keyType.value = keyType.value.reduce((prev, current) => prev + current);
    }
    if (dkState?.value && dkState.value.length > 0) {
      dkState.value = dkState.value.toString();
    }
    console.log('periodMin', periodMin);
    console.log('periodMax', periodMax);

    if (userId?.value) {
      param.append('userId', userId.value);
    }
    if (vin?.value) {
      param.append('vin', vin.value);
    }
    if (periodMin?.value) {
      param.append('periodMin', periodMin.value);
    }
    if (periodMax?.value) {
      param.append('periodMax', periodMax.value);
    }

    if (periodUnit?.value) {
      param.append('periodUnit', periodUnit.value);
    }
    if (keyType?.value) {
      param.append('keyType', keyType.value);
    }
    if (dkState?.value) {
      param.append('dkState', dkState.value);
    }

    if (applyTime?.value) {
      const applyStartTime = moment(applyTime?.value[0])?.format('YYYY-MM-DD');
      param.append('applyStartTime', applyStartTime);

      const applyEndTime = moment(applyTime?.value[1])
        ?.add(1, 'days')
        .format('YYYY-MM-DD');
      param.append('applyEndTime', applyEndTime);
    }
    if (valFromTime?.value) {
      const valFromStartTime = moment(valFromTime?.value[0])?.format(
        'YYYY-MM-DD',
      );
      param.append('valFromStartTime', valFromStartTime);

      const valFromEndTime = moment(valFromTime?.value[1])
        ?.add(1, 'days')
        .format('YYYY-MM-DD');
      param.append('valFromEndTime', valFromEndTime);
    }
    if (valToTime?.value) {
      const valToStartTime = moment(valToTime?.value[0])?.format('YYYY-MM-DD');
      param.append('valToStartTime', valToStartTime);

      const valToEndTime = moment(valToTime?.value[1])
        ?.add(1, 'days')
        .format('YYYY-MM-DD');
      param.append('valToEndTime', valToEndTime);
    }

    if (creator) {
      param.append('creator', creator);
    }

    exportKey(param).then((res) => {
      console.log('res.data', res.data);
      let blob = new Blob([res.data]);

      let link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = fileName;
      // link.click();
      window.URL.revokeObjectURL(link.href);
    });
    message.info('正在导出钥匙信息，详情在历史导出列表查看');
  };
  render() {
    let creator = getDvaApp()._store.getState().user.currentUser.username;
    return (
      <div>
        <EasyTable
          scroll={{ x: '1200px' }}
          autoFetch
          source={getKeysList}
          fixedParams={{ creator: creator, type: 1 }}
          dataProp={'data'}
          name={'keysDataTable'}
          rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <Authorized route={KEYS_INFO_EXPORT}>
              <div className={'btn-group'}>
                <Button
                  type={'ghost'}
                  size={'large'}
                  icon={<DownloadOutlined />}
                  onClick={() => this.exportExcel()}
                >
                  导出钥匙信息
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
          columnWidth={120}
          expandIconAsCell={false}
          expandIconColumnIndex={10}
          expandable={{
            expandedRowRender: (record) => {
              return <SubTable {...record} />;
            },
            expandIcon: (props) => {
              return (
                <a onClick={(e) => props.onExpand(props.record, e)}>
                  生命周期 {props.expanded ? <UpOutlined /> : <DownOutlined />}
                </a>
              );
            },
          }}
        />
        <Modal
          footer={null}
          title={'历史导出列表'}
          visible={this.state?.showUserInfo}
          destroyOnClose={true}
          onCancel={this.onCancel}
          width={1000}
        >
          <SubTable2 />
        </Modal>
      </div>
    );
  }
}

export default DataTable;
