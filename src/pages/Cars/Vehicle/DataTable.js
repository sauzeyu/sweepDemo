import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import FileUploader from '@/components/FileUploader';
import { Button, Modal, message, Tag } from 'antd';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';
import { getVehicles, stopVehicle, discardVehicle } from '@/services/cars';
import { Link, history } from 'umi';
import { log } from 'lodash-decorators/utils';
import Authorized from '@/components/Authorized';
import {
  CAR_VEHICLE_ADD,
  CAR_VEHICLE_UPDATE,
  CAR_VEHICLE_IMPORT,
} from '@/components/Authorized/AuthMap';
import { connect } from 'dva';
import DescriptionList from '@/components/DescriptionList';
import VehicleTable from '@/pages/Cars/Vehicle/VehicleTable';
import { status } from '@/constants/user';

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
    selectedCarId: '',
    //用来进行行内渲染，无实际作用
    flag: '',
  };
  columns = [
    {
      title: 'VIN号',
      dataIndex: 'vin',
      width: 200,
    },
    {
      title: '车型ID',
      dataIndex: 'model_id',
      width: 100,
    },
    // {
    //   title: '证书',
    //   dataIndex: 'cert',
    // },
    {
      title: '车牌号',
      dataIndex: 'license',
      width: 130,
    },
    {
      title: '车主身份证号',
      dataIndex: 'ownerID',
      width: 180,
    },
    {
      title: '车主手机号',
      dataIndex: 'phone',
      width: 150,
    },
    {
      title: '是否有效',
      dataIndex: 'isvalid',
      width: 70,
      render: (text) => {
        return status[text];
      },
    },
    {
      title: '颜色',
      dataIndex: 'colour',
      width: 80,
    },
    {
      title: '车辆蓝牙连接标识',
      dataIndex: 'bluetooth',
      width: 200,
    },
    {
      title: '操作',
      fixed: 'right',
      render: (col) => {
        if (col.isvalid === 3) {
          return null;
        }
        const flag = col.isvalid === 1;
        return (
          <div className={'link-group'}>
            <a onClick={() => this.userInfo(col)}>查看用户</a>
            <a onClick={() => this.keysInfo(col)}>查看钥匙</a>
            <a
              onClick={() => {
                return stopVehicle(col.id).then(() => {
                  //改变状态
                  col.isvalid = col.isvalid ^ 1 ^ 2;
                  //仅为刷新组件，无其他作用
                  this.setState({ flag: col.isvalid });
                });
              }}
              style={{ color: flag ? '#1890FF' : '#FF4D4F' }}
            >
              {flag ? '启用' : '停用'}
            </a>
            <a className={'text-danger'} onClick={() => this.del(col)}>
              吊销
            </a>
          </div>
        );
      },
    },
  ];

  userInfo = (col) => {
    this.setState({
      showUserInfo: true,
      userInfo: {},
    });
    this.props
      .dispatch({
        type: 'carsVehicle/userListByVehicleId',
        payload: { id: col.id },
      })
      .then(
        (res) => {
          this.setState({ userInfo: (res.data && res.data[0]) || [] });
        },
        (err) => {
          message.error(err.message);
        },
      );
  };

  keysInfo = (col) => {
    this.setState({
      keysFormVisible: true,
      selectedCarId: col.id,
    });
  };

  upsert = (value) => {
    this.setState({ editFormVisible: true }, () => {
      if (value) {
        setTimeout(() => {
          this.editFormWrapper.initDetailData(value);
        });
      }
    });
  };

  del = (col) => {
    Modal.confirm({
      title: '吊销车辆信息',
      content: `确定吊销车辆？`,
      onOk: () => {
        discardVehicle(col.id).then(() => {
          col.isvalid = 3;
          //仅为刷新组件，无其他作用
          this.setState({ flag: col.isvalid });
        });
      },
    });
  };
  onOk = async () => {
    this.editForm.validateFields().then((data) => {
      if (data.modelId && data.modelId.key != null) {
        data.modelId = data.modelId.key;
      }
      this.props
        .dispatch({
          type: 'carsVehicle/upsert',
          payload: data,
        })
        .then(() => {
          this.setState({ editFormVisible: false });
          this.dataList.refresh();
        });
    });
  };
  onCancel = () => {
    // this.editForm.resetFields();
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
      selectedRowKeys,
      userInfo = {},
      showUserInfo,
    } = this.state;
    const rowSelection = {
      selectedRowKeys,
      onChange: this.handleRowSelectChange,
    };
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
          rowSelection={rowSelection}
          onChange={this.handleTableChange}
          wrappedComponentRef={(ref) => (this.dataList = ref)}
          extra={
            <div className={'btn-group'}>
              {selectedRowKeys.length > 0 && (
                <Button
                  type={'danger'}
                  onClick={() => this.del(selectedRowKeys)}
                >
                  删除
                </Button>
              )}
              <Button onClick={() => this.upsert()} type={'primary'}>
                新增
              </Button>
            </div>
          }
        />
        <Modal
          footer={null}
          title={'用户信息'}
          visible={showUserInfo}
          onCancel={this.onCancel}
          destroyOnClose={true}
        >
          <DescriptionList col={1}>
            <Description term={'电话'}>{userInfo.phone}</Description>
            <Description term={'姓名'}>{userInfo.name}</Description>
            <Description term={'身份证'}>{userInfo.idnum}</Description>
            <Description term={'是否有效'}>
              {userInfo.isvalid === 0 ? (
                <Tag color="#f50">无效</Tag>
              ) : (
                <Tag color="#87d068">有效</Tag>
              )}
            </Description>
            <Description term={'指纹'}>{userInfo.devFp}</Description>
            <Description term={'状态'}>
              {userInfo.status === 0 ? '已注册' : '已实名认证'}
            </Description>
          </DescriptionList>
        </Modal>

        <DrawerConfirm
          title={'汽车'}
          width={650}
          visible={editFormVisible}
          onOk={this.onOk}
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
          title={'钥匙'}
          width={1000}
          visible={keysFormVisible}
          onOk={this.onOk}
          onCancel={this.onCancel}
          destroyOnClose
        >
          <VehicleTable
            selectedCarId={this.state.selectedCarId}
            editFormRef={(ref) => (this.editForm = ref.form.current)}
            wrappedComponentRef={(ref) => (this.editFormWrapper = ref)}
          />
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
