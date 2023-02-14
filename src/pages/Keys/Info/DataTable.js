import { getDvaApp } from '@@/plugin-dva/exports';
import React, { Component, useImperativeHandle, useState } from 'react';
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
import {
  DKState,
  KeySource,
  KeyState,
  KeyType,
  KeyResource,
  KeyClassification,
} from '@/constants/keys';

import { exportStatus } from '@/constants/export';
import { keyLifecycleList, keyUseListById } from '@/services/cars';
import { useMemo } from 'react';
import {
  KEYS_INFO_EXPORT,
  KEYS_INFO_FREEZE,
  KEYS_INFO_REVOKE,
  KEYS_INFO_THAW,
  LOG_USE,
} from '@/components/Authorized/AuthMap';
import { exportKey } from '@/services/exportKey';
import { downloadExcel } from '@/services/downloadExcel';
import { TableHeaderColumn } from '@/utils/TableHeaderColumn';

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
      // wrappedComponentRef={(ref) => (this.checkKeyUseLogTable = ref)}
    />
  );
};

const SubTable = (props) => {
  // console.log('that  ', props.that);
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
      title: '操作来源',
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
        wrappedComponentRef={(ref) => {
          props.that.state.subTableRefList['subTable_' + props.id] = ref;
          props.that.setState({
            subTableRefList: props.that.state.subTableRefList,
          });
        }}
      />
    </Card>
  );
};

@connect(({ keysManage, loading }) => ({
  keysManage,
}))
class DataTable extends Component {
  // constructor(props) {
  //   super(props)
  //   this.childRef = React.createRef();
  // }
  state = {
    subTableRefList: [],
    carInfo: {},
    applyStartTime: '',
    applyEndTime: '',
    dkState: 1,
    keyResource: 1,
    vin: '',
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
      title: '钥匙类型',
      width: 100,
      dataIndex: 'parentId',
      render: (text) => {
        return KeyType(text);
      },
    },
    {
      title: '用户id',
      ellipsis: {
        showTitle: false,
      },
      dataIndex: 'userId',
      render: (text) => (
        <Tooltip placement="topLeft" title={text}>
          {text}
        </Tooltip>
      ),
    },
    {
      title: '车辆标识符',
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
      width: 100,
      dataIndex: 'dkState',
      render: (text) => {
        return DKState[text];
      },
    },

    {
      title: '生效时间',
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
      title: '失效时间',
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
      title: '钥匙分类',
      width: 100,
      dataIndex: 'keyClassification',
      render: (text) => {
        return KeyClassification(text);
      },
    },
    // {
    //   title: '周期时长',
    //   width: 100,
    //   dataIndex: 'period',
    //   render: (col) => this.time(col),
    // },
    {
      title: '钥匙来源',
      dataIndex: 'keyResource',
      render: (text) => {
        return KeyResource(text);
      },
    },
    // {
    //   title: '终端ID',
    //   dataIndex: 'accountIdHash',
    //   width: 180,
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
      title: '操作',
      fixed: 'right',
      width: 240,
      render: (col, record, index) => {
        let isDisable = col.dkState === 3;
        let disableStyle = {};

        if (col.dkState === 5 || col.dkState === 4 || col.dkState === 0) {
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
                onClick={() => this.enableKey(col, false, index)}
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
        console.log('this.props', this.props);
        return this.props
          .dispatch({
            type: 'keysManage/enableKey',
            payload: {
              keyId: col.id,
              dkState: isEnableKey ? 1 : 3,
              userId: col.userId,
              vin: col.vin,
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
              this.state.subTableRefList['subTable_' + col.id]?.reload();
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
            payload: {
              userId: col.userId,
              vin: col.vin,
            },
          })
          .then(
            () => {
              message.success('操作成功');
              this.dataTable.reload();
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
  confirmExportExcel = () => {
    console.log('this.props.searchFormValues;', this.props.searchFormValues);
    const {
      applyTime,
      dkState,
      keyClassification,
      keyResource,
      keyType,
      userId,
      valFromTime,
      valToTime,
      vin,
    } = this.props.searchFormValues;
    let applyStartTime;
    let applyEndTime;
    let valFromStartTime;
    let valFromEndTime;
    let valToStartTime;
    let valToEndTime;
    let dkStateEnum = [];
    let keyResourceEnum;
    let keyClassificationEnum = [];
    let keyTypeEnum = [];

    if (applyTime) {
      applyStartTime = moment(applyTime[0])?.format('YYYY-MM-DD');
      applyEndTime = moment(applyTime[1])?.add(1, 'days').format('YYYY-MM-DD');
    }

    if (valFromTime) {
      valFromStartTime = moment(valFromTime[0])?.format('YYYY-MM-DD');
      valFromEndTime = moment(valFromTime[1])
        ?.add(1, 'days')
        .format('YYYY-MM-DD');
    }

    if (valToTime) {
      valToStartTime = moment(valToTime[0])?.format('YYYY-MM-DD');

      valToEndTime = moment(valToTime[1])?.add(1, 'days').format('YYYY-MM-DD');
    }

    if (dkState != null) {
      for (let i = 0; i < dkState.length; i++) {
        dkStateEnum.push(DKState[dkState[i]]);
      }
    }
    if (keyResource != null) {
      keyResourceEnum = KeyResource(keyResource);
    }

    if (keyClassification != null) {
      for (let i = 0; i < keyClassification.length; i++) {
        keyClassificationEnum.push(KeyClassification(keyClassification[i]));
      }
    }

    if (keyType != null) {
      for (let i = 0; i < keyType.length; i++) {
        keyTypeEnum.push(KeyType(keyType[i]));
      }
    }

    Modal.confirm({
      title: '确定导出钥匙信息?',

      content: (
        <>
          申请时间:&nbsp;
          {applyStartTime} ~ {applyEndTime}
          <br />
          钥匙状态:&nbsp;
          {dkStateEnum}
          <br />
          钥匙来源:&nbsp;
          {keyResourceEnum}
          <br />
          生效时间:&nbsp;
          {valFromStartTime} ~ {valFromEndTime}
          <br />
          钥匙分类:&nbsp;
          {keyClassificationEnum}
          <br />
          车辆标识符:&nbsp;
          {vin}
          <br />
          失效时间:&nbsp;
          {valToStartTime} ~ {valToEndTime}
          <br />
          钥匙类型:&nbsp;
          {keyTypeEnum}
          <br />
          用户id：&nbsp;
          {userId}
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

    // console.log('this.props.searchFormValues;', this.props.searchFormValues);
    const {
      applyTime,
      dkState,
      keyClassification,
      keyResource,
      keyType,
      userId,
      valFromTime,
      valToTime,
      vin,
    } = this.props.searchFormValues;
    let fileName = '钥匙信息.xlsx';
    let param = new URLSearchParams();
    let tempKeyType;
    if (keyType && keyType?.length > 0) {
      tempKeyType = keyType.reduce((prev, current) => prev + current);
    }
    if (userId) {
      param.append('userId', userId);
    }
    if (vin) {
      param.append('vin', vin);
    }
    if (keyClassification) {
      if (keyClassification?.length < 1) {
      } else {
        param.append('keyClassification', keyClassification);
      }
    }

    if (tempKeyType) {
      param.append('keyType', tempKeyType);
    }
    if (dkState) {
      if (dkState?.length < 1) {
      } else {
        param.append('dkState', dkState);
      }
    }
    if (keyResource) {
      if (keyResource?.length < 1) {
      } else {
        param.append('keyResource', keyResource);
      }
    }

    if (applyTime) {
      const applyStartTime = moment(applyTime[0])?.format('YYYY-MM-DD');
      param.append('applyStartTime', applyStartTime);

      const applyEndTime = moment(applyTime[1])
        ?.add(1, 'days')
        .format('YYYY-MM-DD');
      param.append('applyEndTime', applyEndTime);
    }
    if (valFromTime) {
      const valFromStartTime = moment(valFromTime[0])?.format('YYYY-MM-DD');
      param.append('valFromStartTime', valFromStartTime);

      const valFromEndTime = moment(valFromTime[1])
        ?.add(1, 'days')
        .format('YYYY-MM-DD');
      param.append('valFromEndTime', valFromEndTime);
    }
    if (valToTime) {
      const valToStartTime = moment(valToTime[0])?.format('YYYY-MM-DD');
      param.append('valToStartTime', valToStartTime);

      const valToEndTime = moment(valToTime[1])
        ?.add(1, 'days')
        .format('YYYY-MM-DD');
      param.append('valToEndTime', valToEndTime);
    }

    if (creator) {
      param.append('creator', creator);
    }

    exportKey(param).then((res) => {
      let blob = null;
      let link = null;
      if (res.data) {
        blob = new Blob([res.data]);

        link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = fileName;
      }
      if (blob) {
        window.URL.revokeObjectURL(link?.href);
        message.info('正在导出钥匙信息，详情在历史导出列表查看');
      }
    });
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

  render() {
    let creator = getDvaApp()._store.getState().user.currentUser.username;
    this.state.applyStartTime = moment(this.defaultStartTime()[0])?.format(
      'YYYY-MM-DD 00:00:00',
    );
    this.state.applyEndTime = moment(this.defaultStartTime()[1])
      ?.add(1, 'd')
      ?.format('YYYY-MM-DD 00:00:00');
    return (
      <div>
        <EasyTable
          scroll={{ x: '1200px' }}
          // autoFetch
          source={getKeysList}
          fixedParams={{
            // applyStartTime: this.state.applyStartTime,
            // applyEndTime: this.state.applyEndTime,
            dkState: this.state.dkState,
            keyResource: this.state.keyResource,
          }}
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
                  onClick={() => this.confirmExportExcel()}
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
          expandIconColumnIndex={11}
          expandable={{
            expandedRowRender: (record) => {
              record.that = this;
              return <SubTable {...record} />;
            },
            expandIcon: (props) => {
              return (
                <a
                  onClick={(e) => {
                    props.onExpand(props.record, e);
                  }}
                >
                  生命周期
                  {props.expanded ? <UpOutlined /> : <DownOutlined />}
                </a>
              );
            },
            // onExpand : (props) => {
            //   return (
            //     <a onClick={(e) => props.onExpand(props.record, e)}>
            //     </a>
            //   );
            // },
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
