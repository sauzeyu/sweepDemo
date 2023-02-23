import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import Authorized from '@/components/Authorized';
import { Badge, Button, message, Modal, Tag, Tooltip } from 'antd';
import {
  CloudUploadOutlined,
  InboxOutlined,
  PlusCircleOutlined,
  RightSquareTwoTone,
  DownloadOutlined,
} from '@ant-design/icons';
import { connect } from 'dva';
import { TableHeaderColumn } from '@/utils/TableHeaderColumn';

import { getBluetoothList } from '@/services/aftermarketReplacement';

import DescriptionList from '@/components/DescriptionList';
import { color, sunroofType, windowType } from '@/constants/cars';
import DrawerConfirm from '@/components/DrawerConfirm';
import HistoryBluetooth from './HistoryBluetooth';
import { selectVehicleByVin } from '@/services/aftermarketReplacement';
import { getDvaApp } from '@@/plugin-dva/exports';
import importExcel from '@/utils/importExcel';
import { AFTERMARKET_REPLACEMENT_INFO_EXPORT } from '@/components/Authorized/AuthMap';

import { exportReplacement } from '@/services/exportReplacement';
import moment from 'moment';

const { Description } = DescriptionList;

@connect(({ customer, loading }) => ({
  customer,
}))
class DataTable extends Component {
  state = {
    carInfoVisible: false,
    carInfo: {},
    historyAftermarketDataTableVisible: false,
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
      title: '车辆vin号',
      dataIndex: 'vin',
      width: 150,
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
      title: '旧蓝牙设备序列号',
      dataIndex: 'oldBluetoothSn',
      width: 150,
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
      title: '新蓝牙设备序列号',
      dataIndex: 'newBluetoothSn',
      width: 150,
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
      title: '换件时间',
      dataIndex: 'replacementTime',
      width: 110,
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
    //   title: '操作',
    //   fixed: 'right',
    //   width: 270,
    //   render: (col) => {
    //     return (
    //       <div className={'link-group'}>
    //         <a onClick={() => this.historyAftermarketDataTable(col)}>
    //           换件记录
    //         </a>
    //         {/*<a onClick={() => this.keysInfo(col)}>查看钥匙</a>*/}
    //       </div>
    //     );
    //   },
    // },
  ];
  historyAftermarketDataTable = (col) => {
    this.setState({
      vin: col.vin,
      historyAftermarketDataTableVisible: true,
    });
  };
  carInfo = (col) => {
    this.setState({
      carInfoVisible: true,
      carInfo: {},
    });
    let params = new URLSearchParams();
    params.append('vin', col.vin);
    selectVehicleByVin(params).then(
      (res) => {
        this.setState({ carInfo: res.data });
      },
      (err) => {
        message.error(err.message);
      },
    );
  };
  onCancel = () => {
    this.setState({
      carInfoVisible: false,
      historyAftermarketDataTableVisible: false,
    });
  };
  confirmExportExcel = () => {
    const { vin, startTime } = this.props.searchFormValues;
    let startTime1;
    let endTime;
    if (startTime) {
      startTime1 = moment(startTime[0]).format('YYYY-MM-DD');
      endTime = moment(startTime[1]).add(1, 'days').format('YYYY-MM-DD');
    }
    Modal.confirm({
      title: '确定导出换件信息?',

      content: (
        <>
          车辆vin号:&nbsp;
          {vin}
          <br />
          换件时间:&nbsp;
          {startTime1} ~ {endTime}
        </>
      ),

      onOk: () => {
        return this.exportExcel();
      },
      okText: '导出',
    });
  };
  exportExcel = () => {
    const { vin, startTime } = this.props.searchFormValues;
    let fileName = '换件信息.xlsx';
    let param = new URLSearchParams();
    if (vin) {
      param.append('vin', vin);
    }

    if (startTime) {
      let startTime1 = moment(startTime[0]).format('YYYY-MM-DD');
      let endTime = moment(startTime[1]).add(1, 'days').format('YYYY-MM-DD');
      if (startTime1) {
        param.append('startTime', startTime1);
      }
      if (endTime) {
        param.append('endTime', endTime);
      }
    }

    exportReplacement(param).then((res) => {
      let blob = new Blob([res.data]);
      let link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = fileName;
      link.click();
      window.URL.revokeObjectURL(link.href);
    });
  };

  render() {
    const { carInfoVisible, carInfo = {} } = this.state;
    return (
      <div>
        <EasyTable
          autoFetch
          source={getBluetoothList}
          dataProp={'data'}
          name={'aftermarketReplacementDataTable'}
          // rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <Authorized route={AFTERMARKET_REPLACEMENT_INFO_EXPORT}>
              <div className={'btn-group'}>
                <Button
                  type={'ghost'}
                  size={'large'}
                  icon={<DownloadOutlined />}
                  onClick={() => this.confirmExportExcel()}

                  // onClick={() => this.exportExcel()}
                >
                  导出换件信息
                </Button>
              </div>
            </Authorized>
          }
        />
        <Modal
          footer={null}
          title={'车辆信息'}
          visible={carInfoVisible}
          onCancel={this.onCancel}
          destroyOnClose={true}
        >
          <DescriptionList col={1}>
            <Description term={'工厂编号'}>{carInfo?.factoryNo}</Description>
            {/*<Description term={'车窗类型'}>*/}
            {/*  {windowType[carInfo?.windowType]}*/}
            {/*</Description>*/}
            {/*<Description term={'车辆颜色'}>{color[carInfo?.color]}</Description>*/}
            {/*<Description term={'天窗类型'}>*/}
            {/*  {sunroofType[carInfo?.sunroofType]}*/}
            {/*</Description>*/}
            <Description term={'现蓝牙设备序列号'}>
              <Tag color="blue">{carInfo?.hwDeviceSn}</Tag>
            </Description>
            <Description term={'创建时间'}>{carInfo?.createTime}</Description>
          </DescriptionList>
        </Modal>

        <DrawerConfirm
          title={'换件记录'}
          width={1000}
          visible={this.state.historyAftermarketDataTableVisible}
          onOk={this.onCancel}
          confirmLoading={false}
          onCancel={this.onCancel}
          hiddenOk={true}
          destroyOnClose
        >
          <HistoryBluetooth
            vin={this.state.vin}
            editFormRef={(ref) => (this.editForm = ref.form.current)}
            wrappedComponentRef={(ref) => (this.editFormWrapper = ref)}
          />
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
