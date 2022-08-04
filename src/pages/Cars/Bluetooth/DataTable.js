import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal } from 'antd';
import { PlusCircleOutlined, DownloadOutlined } from '@ant-design/icons';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';
import { connect } from 'dva';
import { getBluetooth, delBluetooth } from '@/services/cars';
import { overdue } from '@/constants/cars';
import { exportBluetooth } from '@/services/exportBluetooth';
@connect(({ carsType, loading }) => ({
  carsType,
}))
class DataTable extends Component {
  state = {
    editFormVisible: false,
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
      title: '蓝牙设备序列号',
      dataIndex: 'hwDeviceSn',
    },
    {
      title: '蓝牙检索号',
      dataIndex: 'searchNumber',
    },
    {
      title: '设备供应商编号',
      dataIndex: 'hwDeviceProviderNo',
    },
    {
      title: '蓝牙MAC地址',
      dataIndex: 'bleMacAddress',
    },
    {
      title: '安全芯片SEID',
      dataIndex: 'dkSecUnitId',
    },
    {
      title: '设备状态',
      dataIndex: 'flag',
      render: (text) => {
        return overdue[text];
      },
    },
    // {
    //   title: '操作',
    //   render: (col) => {
    //     return (
    //       <div className={'link-group'}>
    //         <a className={'text-danger'} onClick={() => this.del(col)}>
    //           删除
    //         </a>
    //       </div>
    //     );
    //   },
    // },
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
            this.dataTable.reload();
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
  exportExcel = () => {
    let hwDeviceSn = this.props.searchFormValues[0];
    let searchNumber = this.props.searchFormValues[1];
    let flag = this.props.searchFormValues[2];
    let fileName = '蓝牙信息.xlsx';
    let param = new URLSearchParams();
    if (hwDeviceSn && hwDeviceSn.value) {
      param.append('hwDeviceSn', hwDeviceSn.value);
    }
    if (searchNumber && searchNumber.value) {
      param.append('searchNumber', searchNumber.value);
    }
    // debugger;
    if (flag && flag.value) {
      param.append('flag', flag.value);
    }
    exportBluetooth(param).then((res) => {
      let blob = new Blob([res.data]);
      let link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = fileName;
      link.click();
      window.URL.revokeObjectURL(link.href);
    });
  };
  render() {
    // const bodyScale = () => {
    //   let clientWidth = document.documentElement.clientWidth;
    //   let scale = clientWidth
    //   document.body.style.zoom = scale;
    // }
    // bodyScale();
    const { editFormVisible } = this.state;
    return (
      <div>
        <EasyTable
          autoFetch
          source={getBluetooth}
          dataProp={'data'}
          name={'carsBluetoothDataTable'}
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
                导出蓝牙信息
              </Button>
            </div>
          }
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
          title={'车辆型号'}
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
