import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';
import { connect } from 'dva';
import { getBluetooth, delBluetooth } from '@/services/cars';

@connect(({ carsType, loading }) => ({
  carsType,
}))
class DataTable extends Component {
  state = {
    editFormVisible: false,
    idList: [],
  };
  columns = [
    {
      title: '设备序列号',
      dataIndex: 'hwDeviceSn',
    },
    {
      title: '设备ID号',
      dataIndex: 'hwDeviceId',
    },
    {
      title: '设备供应商编号',
      dataIndex: 'hwDeviceProviderNo',
    },
    {
      title: '数字钥匙软件版本号',
      dataIndex: 'dkSdkVersion',
    },
    {
      title: '数字钥匙安全单元ID',
      dataIndex: 'dkSecUnitId',
    },
    {
      title: '蓝牙名称',
      dataIndex: 'bleName',
    },
    {
      title: '蓝牙MAC地址',
      dataIndex: 'bleMacAddress',
    },
    {
      title: '蓝牙协议版本号',
      dataIndex: 'bleProtocolVersion',
    },
    {
      title: '蓝牙硬件版本号',
      dataIndex: 'bleHardwareVersion',
    },
    {
      title: '蓝牙软件版本号',
      dataIndex: 'bleSoftwareVersion',
    },
    {
      title: '操作',
      render: (col) => {
        return (
          <div className={'link-group'}>
            {/*<a onClick={() => this.upsert(col)}>编辑</a>*/}
            <a className={'text-danger'} onClick={() => this.del(col)}>
              删除
            </a>
          </div>
        );
      },
    },
  ];
  del = (model) => {
    const { hwDeviceSn } = model;
    Modal.confirm({
      title: '删除蓝牙设备',
      content: `确定设备“${hwDeviceSn}”？`,
      onOk: () => {
        return delBluetooth(hwDeviceSn).then(
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
  upsert = (model) => {
    this.setState({ editFormVisible: true }, () => {
      if (model) {
        this.editForm.setFieldsValue(model);
      }
    });
  };
  onOk = () => {
    this.editForm
      .validateFields()
      .then((values) => {
        this.props
          .dispatch({
            type: 'carsType/upsert',
            payload: values,
          })
          .then(
            (res) => {
              this.setState({ editFormVisible: false });
              this.editForm.resetFields();
              const msg = values.id == null ? '新增' : '修改';
              if (res.code === 200) {
                Modal.success({
                  title: msg + '成功',
                });
                this.dataTable.refresh();
              } else {
                Modal.error({
                  title: msg + '失败',
                });
              }
            },
            (err) => {
              Modal.error({
                title: err.message,
              });
            },
          );
      })
      .catch((errors) => {
        if (errors) return;
      });
  };
  onCancel = () => {
    this.editForm.resetFields();
    this.setState({ editFormVisible: false });
  };

  render() {
    const { editFormVisible } = this.state;
    return (
      <div>
        <EasyTable
          autoFetch
          source={getBluetooth}
          dataProp={'data'}
          name={'carsTypeDataTable'}
          rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          // extra={
          //   <Button
          //     type={'primary'}
          //     icon={<PlusCircleOutlined />}
          //     onClick={() => this.upsert()}
          //   >
          //     新增蓝牙
          //   </Button>
          // }
        />
        <DrawerConfirm
          title={'车型'}
          width={600}
          visible={editFormVisible}
          onOk={this.onOk}
          onCancel={this.onCancel}
        >
          <EditForm editFormRef={(ref) => (this.editForm = ref.form.current)} />
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
