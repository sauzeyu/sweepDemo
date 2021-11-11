import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import FileUploader from '@/components/FileUploader';
import { Button, Modal, message } from 'antd';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';
import { getVehicles } from '@/services/cars';
import { Link, history } from 'umi';
import { log } from 'lodash-decorators/utils';
import Authorized from '@/components/Authorized';
import {
  CAR_VEHICLE_ADD,
  CAR_VEHICLE_UPDATE,
  CAR_VEHICLE_IMPORT,
} from '@/components/Authorized/AuthMap';
import { connect } from 'dva';

@connect(({ carsVehicle, loading }) => ({
  carsVehicle,
  upserting: loading.effects['carsVehicle/upsert'],
  importVehicle: loading.effects['carsVehicle/importVehicle'],
}))
class DataTable extends Component {
  state = {
    // 新增/修改
    editFormVisible: false,
    selectedRowKeys: [],
  };
  columns = [
    {
      title: 'VIN号',
      dataIndex: 'vin',
      render: (vin, col) => {
        return (
          <Authorized route={CAR_VEHICLE_UPDATE} noMatch={vin}>
            <a onClick={() => this.upsert(col)}>{vin}</a>
          </Authorized>
        );
      },
    },
    {
      title: '操作',
      fixed: 'right',
      render: (col) => {
        return (
          <div className={'link-group'}>
            <a onClick={() => this.userInfo(col)}>查看用户</a>
            <a onClick={() => this.del(col)}>查看钥匙</a>
            <a onClick={() => this.del(col)}>启用</a>
            <a className={'text-danger'} onClick={() => this.del(col)}>
              停用
            </a>
            <a className={'text-danger'} onClick={() => this.del(col)}>
              吊销
            </a>
          </div>
        );
      },
    },
  ];
  upsert = (value) => {
    this.setState({ editFormVisible: true }, () => {
      if (value) {
        setTimeout(() => {
          this.editFormWrapper.initDetailData(value);
        });
      }
    });
  };

  del = (items) => {
    Modal.confirm({
      title: '删除汽车信息',
      content: `确定删除车辆？`,
      onOk: () => {
        this.props
          .dispatch({
            type: 'carsVehicle/del',
            payload: items,
          })
          .then(
            () => {
              this.setState({ selectedRowKeys: [] });
              this.dataList.refresh();
            },
            (err) => {
              message.error(err.message);
            },
          );
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
    this.editForm.resetFields();
    this.setState({ editFormVisible: false });
  };
  handleRowSelectChange = (selectedRowKeys) => {
    this.setState({ selectedRowKeys });
  };
  handleTableChange = () => {
    // this.setState({ selectedRowKeys: [] });
  };
  render() {
    const { editFormVisible, selectedRowKeys } = this.state;
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
      </div>
    );
  }
}

export default DataTable;
