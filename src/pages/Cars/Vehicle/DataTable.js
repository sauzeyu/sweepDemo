import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Button, Modal, message, Tag, Tooltip } from 'antd';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';
import { getVehicles, stopVehicle, discardVehicle } from '@/services/cars';
import { connect } from 'dva';
import DescriptionList from '@/components/DescriptionList';
import KeyTable from './KeyTable';
import { windowType, sunroofType, color } from '@/constants/cars';
import { exportVehicle } from '@/services/exportVehicle';
import { PlusCircleOutlined, DownloadOutlined } from '@ant-design/icons';
import Authorized from '@/components/Authorized';
import { CARS_VEHICLE_EXPORT } from '@/components/Authorized/AuthMap';

const { Description } = DescriptionList;

@connect(({ carsVehicle, loading }) => ({
  carsVehicle,
  upserting: loading.effects['carsVehicle/upsert'],
  importVehicle: loading.effects['carsVehicle/importVehicle'],
}))
class DataTable extends Component {
  state = {
    // 新增/修改
    showUserInfo: false,
    showKeysInfo: false,
    editFormVisible: false,
    keysFormVisible: false,
    selectedRowKeys: [],
    userInfo: {},
    keysInfo: {},
    selectedVehicleId: '',
    selectedVehicleVin: '',
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
      width: 200,
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
      title: '车型',
      dataIndex: 'vehicleType',
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
      title: '蓝牙设备序列号',
      dataIndex: 'hwDeviceSn',
      width: 300,
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
      title: '蓝牙检索号',
      dataIndex: 'searchNumber',
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
      title: '蓝牙供应商编号',
      dataIndex: 'hwDeviceProviderNo',
    },
    {
      title: '蓝牙Mac地址',
      dataIndex: 'bleMacAddress',
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
        if (col.isValid === 0) {
          return null;
        }
        return (
          <div className={'link-group'}>
            <a onClick={() => this.keysInfo(col)}>查看钥匙</a>
          </div>
        );
      },
    },
  ];

  keysInfo = (col) => {
    this.setState({
      keysFormVisible: true,
      selectedVehicleId: col.id,
      selectedVehicleVin: col.vin,
    });
  };

  revokeCar = (col) => {
    Modal.confirm({
      title: '吊销车辆',
      content: (
        <span style={{ color: 'red' }}>
          <br />
          确定吊销车辆？吊销后将无法恢复
        </span>
      ),
      onOk: () => {
        discardVehicle(col.id).then(() => {
          this.dataTable.reload();
        });
      },
    });
  };

  del = (model) => {
    Modal.confirm({
      title: '删除车辆',
      content: `确定删除？`,
    });
  };
  //
  upsert = () => {
    this.editForm.validateFields().then((data) => {
      this.props
        .dispatch({
          type: 'carsVehicle/upsert',
          payload: data,
        })
        .then((res) => {
          this.setState({ editFormVisible: false });
          this.dataTable.reload();
          if (res.code === 200) {
            message.success(res.msg);
          } else {
            message.error(res.msg);
          }
        });
    });
  };
  onCancel = () => {
    this.setState({
      editFormVisible: false,
      keysFormVisible: false,
      showUserInfo: false,
      showKeysInfo: false,
    });
  };
  exportExcel = () => {
    let hwDeviceSn = this.props.searchFormValues[0];
    let vin = this.props.searchFormValues[1];
    let vehicleType = this.props.searchFormValues[2];
    let vehicleBrand = this.props.searchFormValues[3];
    let vehicleModel = this.props.searchFormValues[4];
    let fileName = '车辆信息.xlsx';
    let param = new URLSearchParams();
    if (hwDeviceSn && hwDeviceSn.value) {
      param.append('hwDeviceSn', hwDeviceSn.value);
    }
    if (vin && vin.value) {
      param.append('vin', vin.value);
    }
    if (vehicleModel && vehicleModel.value) {
      param.append('vehicleModel', vehicleModel.value);
    }
    if (vehicleBrand && vehicleBrand.value) {
      param.append('vehicleBrand', vehicleBrand.value);
    }
    if (vehicleType && vehicleType.value) {
      param.append('vehicleType', vehicleType.value);
    }
    exportVehicle(param).then((res) => {
      let blob = new Blob([res.data]);
      let link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = fileName;
      link.click();
      window.URL.revokeObjectURL(link.href);
    });
  };

  render() {
    const {
      editFormVisible,
      keysFormVisible,
      userInfo = {},
      showUserInfo,
    } = this.state;
    return (
      <div>
        <EasyTable
          autoFetch
          source={getVehicles}
          dataProp={'data'}
          name={'carsVehicleDataTable'}
          rowKey={'id'}
          columns={this.columns}
          scroll={{ x: 1300 }}
          onChange={this.handleTableChange}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <Authorized route={CARS_VEHICLE_EXPORT}>
              <Button
                type={'ghost'}
                size={'large'}
                icon={<DownloadOutlined />}
                onClick={() => this.exportExcel()}
              >
                导出车辆信息
              </Button>
            </Authorized>
          }
        />
        <Modal
          footer={null}
          title={'车主信息'}
          visible={showUserInfo}
          onCancel={this.onCancel}
          destroyOnClose={true}
        >
          <DescriptionList col={1}>
            <Description term={'电话'}>{userInfo?.phone}</Description>
            <Description term={'姓名'}>{userInfo?.username}</Description>
          </DescriptionList>
        </Modal>

        <DrawerConfirm
          title={'汽车'}
          width={650}
          visible={editFormVisible}
          onOk={this.upsert}
          onCancel={this.onCancel}
          confirmLoading={this.props.upserting}
          destroyOnClose
        >
          <EditForm
            editFormRef={(ref) => (this.editForm = ref.form.current)}
            wrappedComponentRef={(ref) => (this.editFormWrapper = ref)}
          />
        </DrawerConfirm>

        <DrawerConfirm
          title={'钥匙列表'}
          width={1200}
          visible={keysFormVisible}
          onOk={this.onCancel}
          confirmLoading={false}
          onCancel={this.onCancel}
          hiddenOk={true}
          destroyOnClose
        >
          <KeyTable
            selectedVehicleId={this.state.selectedVehicleId}
            selectedVehicleVin={this.state.selectedVehicleVin}
            editFormRef={(ref) => (this.editForm = ref.form.current)}
            wrappedComponentRef={(ref) => (this.editFormWrapper = ref)}
          />
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
