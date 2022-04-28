import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Button, Modal, message, Tag } from 'antd';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';
import { getVehicles, stopVehicle, discardVehicle } from '@/services/cars';
import { connect } from 'dva';
import DescriptionList from '@/components/DescriptionList';
import VehicleTable from '@/pages/Cars/Vehicle/VehicleTable';
import { checkResult, windowType, sunroofType, color } from '@/constants/cars';

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
    idList: [],
  };
  columns = [
    {
      title: 'VIN号',
      dataIndex: 'vin',
      width: 200,
    },
    {
      title: '发动机号',
      dataIndex: 'ven',
      width: 200,
    },
    {
      title: '车主手机号',
      dataIndex: 'phone',
      width: 150,
    },
    {
      title: '车身颜色',
      dataIndex: 'color',
      width: 100,
      render: (text) => {
        return color[text];
      },
    },
    {
      title: '天窗类型',
      dataIndex: 'sunroofType',
      width: 120,
      render: (text) => {
        return sunroofType[text];
      },
    },
    {
      title: '后备门类型',
      dataIndex: 'trunkType',
      width: 150,
    },
    {
      title: '车窗类型',
      dataIndex: 'windowType',
      width: 120,
      render: (text) => {
        return windowType[text];
      },
    },
    {
      title: '检测结果',
      dataIndex: 'checkResult',
      width: 100,
      render: (text) => {
        return checkResult[text];
      },
    },
    {
      title: '蓝牙编号',
      dataIndex: 'bleNo',
      width: 150,
    },
    {
      title: '操作',
      fixed: 'right',
      render: (col) => {
        if (col.isValid === 0) {
          return null;
        }
        const flag = col.isValid === 1;
        // TODO 编辑车辆
        return (
          <div className={'link-group'}>
            <a onClick={() => this.userInfo(col)}>查看车主</a>
            <a onClick={() => this.keysInfo(col)}>查看钥匙</a>
            {/*<a*/}
            {/*  onClick={() => {*/}
            {/*    return stopVehicle(col.id).then(() => {*/}
            {/*      col.isValid = col.isValid ^ 1 ^ 2;*/}
            {/*      this.dataTable.refresh();*/}
            {/*    });*/}
            {/*  }}*/}
            {/*  style={{color: flag ? '#1890FF' : '#FF4D4F'}}*/}
            {/*>*/}
            {/*  {flag ? '启用' : '停用'}*/}
            {/*</a>*/}
            {/*<a className={'text-danger'} onClick={() => this.revokeCar(col)}>*/}
            {/*  吊销*/}
            {/*</a>*/}
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
        type: 'carsVehicle/selectUserByPhone',
        payload: { phone: col.phone },
      })
      .then(
        (res) => {
          this.setState({ userInfo: res.data });
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
      // onOk: () => {
      //   return this.props
      //     .dispatch({
      //       type: 'carsVehicle/deleteVehicle',
      //       payload: model,
      //     })
      //     .then(
      //       () => {
      //         message.success('操作成功');
      //         // this.setState({flag: true})
      //       },
      //       (err) => {
      //         message.error(err.message);
      //       },
      //     );
      // },
    });
  };

  addCar = async () => {
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
          this.dataTable.refresh();
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
          // rowSelection={rowSelection}
          onChange={this.handleTableChange}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <div className={'btn-group'}>
              {/*selectedRowKeys.length > 0 && (
                <Button
                  type={'danger'}
                  onClick={() => this.del(selectedRowKeys)}
                >
                  删除
                </Button>
              )*/}
              {/*<Button*/}
              {/*  onClick={() => this.upsert()}*/}
              {/*  type={'primary'}*/}
              {/*  icon={<PlusCircleOutlined/>}*/}
              {/*>*/}
              {/*  新增车辆*/}
              {/*</Button>*/}
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
            <Description term={'电话'}>
              {userInfo ? userInfo.phone : ''}
            </Description>
            <Description term={'姓名'}>
              {userInfo ? userInfo.username : ''}
            </Description>
            <Description term={'身份证'}>
              {userInfo ? userInfo.idCard : ''}
            </Description>
            <Description term={'是否有效'}>
              {userInfo ? (
                userInfo.isValid === 0 ? (
                  <Tag color="#f50">无效</Tag>
                ) : (
                  <Tag color="#87d068">有效</Tag>
                )
              ) : (
                <Tag color="red">没有车主</Tag>
              )}
            </Description>
            <Description term={'指纹'}>
              {userInfo ? userInfo.phoneFingerprint : ''}
            </Description>
            <Description term={'实名信息'}>
              {userInfo
                ? (userInfo ? userInfo.status : 0) === 0
                  ? '已注册'
                  : '已实名认证'
                : ''}
            </Description>
          </DescriptionList>
        </Modal>

        <DrawerConfirm
          title={'汽车'}
          width={650}
          visible={editFormVisible}
          onOk={this.addCar}
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
          onOk={this.onCancel}
          confirmLoading={false}
          onCancel={this.onCancel}
          hiddenOk={true}
          destroyOnClose
        >
          <VehicleTable
            selectedVehicleId={this.state.selectedCarId}
            editFormRef={(ref) => (this.editForm = ref.form.current)}
            wrappedComponentRef={(ref) => (this.editFormWrapper = ref)}
          />
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
