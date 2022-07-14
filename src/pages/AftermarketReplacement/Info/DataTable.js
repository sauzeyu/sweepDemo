import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal, Tag } from 'antd';
import {
  CloudUploadOutlined,
  InboxOutlined,
  PlusCircleOutlined,
  RightSquareTwoTone,
  DownloadOutlined,
} from '@ant-design/icons';
import { connect } from 'dva';

import { getBluetoothList } from '@/services/aftermarketReplacement';

import DescriptionList from '@/components/DescriptionList';
import { color, sunroofType, windowType } from '@/constants/cars';
import DrawerConfirm from '@/components/DrawerConfirm';
import HistoryBluetooth from './HistoryBluetooth';
import { selectVehicleByVin } from '@/services/aftermarketReplacement';
import { getDvaApp } from '@@/plugin-dva/exports';
import importExcel from '@/utils/importExcel';
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
        let currentIndex = this.dataTable?.state?.currentIndex;
        let currentPageSize = this.dataTable?.state?.currentPageSize;
        return (currentIndex - 1) * currentPageSize + (index + 1);
      },
    },
    {
      title: '车辆vin号',
      dataIndex: 'vin',
      width: 150,
    },
    {
      title: '旧蓝牙设备序列号',
      dataIndex: 'oldBluetoothSn',
      width: 110,
    },
    {
      title: '新蓝牙设备序列号',
      dataIndex: 'newBluetoothSn',
      width: 150,
    },
    {
      title: '换件时间',
      dataIndex: 'replacementTime',
      width: 110,
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
  exportExcel = () => {
    let vin = this.props.searchFormValues[0];
    let replacementTime = this.props.searchFormValues[1]?.value;
    let fileName = '换件信息.xlsx';
    let param = new URLSearchParams();
    if (vin && vin.value) {
      param.append('vin', vin.value);
    }

    if (replacementTime) {
      let startTime = moment(replacementTime[0]).format('YYYY-MM-DD');
      let endTime = moment(replacementTime[1])
        .add(1, 'days')
        .format('YYYY-MM-DD');
      if (startTime && startTime.value) {
        param.append('startTime', startTime.value);
      }
      if (endTime && endTime.value) {
        param.append('endTime', endTime.value);
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
          rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <div className={'btn-group'}>
              <Button
                type={'ghost'}
                size={'large'}
                icon={<DownloadOutlined />}
                onClick={() => this.exportExcel()}
              >
                导出换件信息
              </Button>
            </div>
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
