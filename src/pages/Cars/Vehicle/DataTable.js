import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Button, Modal, message, Tag } from 'antd';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';
import { getVehicles, stopVehicle, discardVehicle } from '@/services/cars';
import { connect } from 'dva';
import DescriptionList from '@/components/DescriptionList';
import KeyTable from './KeyTable';
import { windowType, sunroofType, color } from '@/constants/cars';

import { PlusCircleOutlined } from '@ant-design/icons';

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
      title: '车辆vin号',
      dataIndex: 'vin',
      width: 200,
    },
    {
      title: '车辆车型',
      dataIndex: 'vehicleModel',
    },
    {
      title: '车辆品牌',
      dataIndex: 'vehicleBrand',
    },
    {
      title: '蓝牙设备序列号',
      dataIndex: 'hwDeviceSn',
      width: 300,
    },
    {
      title: '蓝牙检索号',
      dataIndex: 'searchNumber',
    },
    {
      title: '蓝牙供应商编号',
      dataIndex: 'hwDeviceProviderNo',
    },
    {
      title: '蓝牙Mac地址',
      dataIndex: 'bleMacAddress',
    },
    {
      title: '操作',
      fixed: 'right',
      width: 300,
      render: (col) => {
        if (col.isValid === 0) {
          return null;
        }
        // const flag = col.isValid === 1;
        return (
          <div className={'link-group'}>
            <a onClick={() => this.keysInfo(col)}>查看钥匙</a>
            {/*<a onClick={() => this.addOrEditVehicle(col)}>编辑车辆</a>*/}
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

  addOrEditVehicle = (value) => {
    this.setState({ editFormVisible: true }, () => {
      if (value) {
        setTimeout(() => {
          this.editFormWrapper.initDetailData(value);
        });
      }
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
          this.dataTable.refresh();
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
  handleRowSelectChange = (selectedRowKeys) => {
    this.setState({ selectedRowKeys });
  };
  handleTableChange = () => {
    // this.setState({ selectedRowKeys: [] });
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
          // rowSelection={rowSelection}
          onChange={this.handleTableChange}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <div className={'btn-group'}>
              <Button
                onClick={() => this.addOrEditVehicle()}
                type={'primary'}
                icon={<PlusCircleOutlined />}
              >
                新增车辆
              </Button>
            </div>
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
