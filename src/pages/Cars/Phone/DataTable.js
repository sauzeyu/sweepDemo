import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Button, Modal, message, Tag } from 'antd';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';
import { getPhone, updatePhone } from '@/services/cars';
import { connect } from 'dva';
import DescriptionList from '@/components/DescriptionList';
import VehicleTable from '@/pages/Cars/Vehicle/VehicleTable';
import { checkResult, windowType, sunroofType, color } from '@/constants/cars';
import { stopDkUser } from '@/services/customer';

const { Description } = DescriptionList;

@connect(({ carsVehicle, loading }) => ({
  carsVehicle,
  // upserting: loading.effects['carsVehicle/upsert'],
  // importVehicle: loading.effects['carsVehicle/importVehicle'],
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
      title: '手机品牌',
      dataIndex: 'phoneBrand',
      width: 150,
    },
    {
      title: '手机型号',
      dataIndex: 'phoneModel',
      width: 150,
    },
    {
      title: '手机标定数据',
      dataIndex: 'personalAndCalibrationString',
      width: 650,
    },
    {
      title: '操作',
      fixed: 'right',
      width: 170,
      render: (col) => {
        return (
          <div className={'link-group'}>
            <a onClick={() => this.editPhone(col)}>编辑</a>
          </div>
        );
      },
    },
  ];

  // del = (model) => {
  //   Modal.confirm({
  //     title: '删除车辆',
  //     content: `确定删除？`,
  //   });
  // };
  //
  editPhone = (col) => {
    this.setState(
      {
        editFormVisible: true,
        selectedUser: col,
      },
      () => {
        if (col) {
          this.editForm.setFieldsValue(col);
        }
      },
    );
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
          source={getPhone}
          dataProp={'data'}
          name={'phoneDataTable'}
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
      </div>
    );
  }
}

export default DataTable;
