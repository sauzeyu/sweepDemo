import React, { Component } from 'react';
import request from '@/utils/request';
import axios from 'axios';
import EasyTable from '@/components/EasyTable';
import DrawerConfirm from '@/components/DrawerConfirm';
import importExcel from '@/utils/importExcel';
import {
  exportPhoneCalibration,
  exportVehicleCalibration,
} from '@/services/upOrDownload';
import { getPublicPath } from '@/utils';
import {
  CARS_VEHICLE_CALIBRATION_UPDATE,
  CARS_VEHICLE_CALIBRATION_EXPORT,
  CARS_VEHICLE_CALIBRATION_IMPORT,
  CARS_VEHICLE_CALIBRATION_TEMPLATE,
} from '@/components/Authorized/AuthMap';
import Authorized from '@/components/Authorized';
import {
  Button,
  message,
  Modal,
  Upload,
  Row,
  Col,
  Form,
  Input,
  Tooltip,
  Select,
} from 'antd';
import {
  CloudUploadOutlined,
  InboxOutlined,
  DownloadOutlined,
  CloudDownloadOutlined,
} from '@ant-design/icons';
import {
  getPhone,
  getVehicle,
  updatePhone,
  updateVehicleCalibration,
} from '@/services/cars';
import { connect } from 'dva';
import { getDvaApp } from '@@/plugin-dva/exports';
import { TableHeaderColumn } from '@/utils/TableHeaderColumn';

const { Dragger } = Upload;
const xlsTemp = getPublicPath(
  'template/手机定位适配（蓝牙灵敏度等级切换）.xls',
);
const xlsxTemp = getPublicPath(
  'template/手机定位适配（蓝牙灵敏度等级切换）.xlsx',
);

@connect(({ carsVehicle, loading }) => ({
  carsVehicle,
}))
class DataTable extends Component {
  editForm = React.createRef();

  state = {
    editFormVisible: false,
    rowData: {},
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
      title: '车型',
      dataIndex: 'vehicleModel',
    },
    {
      title: '蓝牙灵敏度等级',
      dataIndex: 'level',
    },

    {
      title: '手机标定数据',
      dataIndex: 'vehicleAndCalibrationString',
      width: 650,
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
      render: (row) => {
        return (
          <Authorized route={CARS_VEHICLE_CALIBRATION_UPDATE}>
            <div className={'link-group'}>
              <a onClick={() => this.editRow(row)}>编辑</a>
            </div>
          </Authorized>
        );
      },
    },
  ];

  exportExcel = (isXlsx) => {
    const { vehicleModel, level } = this.props.searchFormValues;
    let fileName;
    let param = new URLSearchParams();
    if (vehicleModel) {
      param.append('vehicleModel', vehicleModel);
    }
    if (level) {
      param.append('level', level);
    }
    if (isXlsx) {
      param.append('isXlsx', isXlsx);
      fileName = '手机标定数据.xlsx';
    } else {
      fileName = '手机标定数据.xls';
      param.append('isXlsx', isXlsx);
    }

    exportVehicleCalibration(param).then((res) => {
      let blob = new Blob([res.data]);
      let link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = fileName;
      link.click();
      window.URL.revokeObjectURL(link.href);
    });
  };

  openModalExport = () => {
    return Modal.info({
      width: 450,
      icon: '',
      title: '选择导出 Excel 文件格式',
      okText: '取消',
      closable: true,
      content: (
        <Row>
          <Col span={12}>
            <Button
              type={'dashed'}
              onClick={() => this.exportExcel(true)}
              icon={<DownloadOutlined style={{ fontSize: '50px' }} />}
              style={{ width: '200px', height: '150px' }}
            >
              导出 xlsx 文件
            </Button>
          </Col>
          <Col span={12}>
            <Button
              type={'dashed'}
              icon={<CloudDownloadOutlined style={{ fontSize: '50px' }} />}
              onClick={() => this.exportExcel(false)}
              style={{ width: '200px', height: '150px' }}
            >
              导出 xls 文件
            </Button>
          </Col>
        </Row>
      ),
    });
  };

  openDownloadTemplate = () => {
    return Modal.info({
      width: 450,
      icon: '',
      title: '选择导入模板',
      okText: '取消',
      closable: true,
      content: (
        <Row>
          <Col span={12}>
            <Button
              type={'dashed'}
              onClick={() => (window.location.href = xlsxTemp)}
              icon={<DownloadOutlined style={{ fontSize: '50px' }} />}
              style={{ width: '200px', height: '150px' }}
            >
              xlsx 模板
            </Button>
          </Col>
          <Col span={12}>
            <Button
              type={'dashed'}
              icon={<CloudDownloadOutlined style={{ fontSize: '50px' }} />}
              onClick={() => (window.location.href = xlsTemp)}
              style={{ width: '200px', height: '150px' }}
            >
              xls 模板
            </Button>
          </Col>
        </Row>
      ),
    });
  };

  editRow = (row) => {
    this.setState(
      {
        rowData: row,
        editFormVisible: true,
      },
      () => {
        if (row) {
          this.editForm.current?.setFieldsValue(row);
        }
      },
    );
  };

  editFormCancel = () => {
    this.setState({
      editFormVisible: false,
      rowData: {},
    });
    this.editForm.current.resetFields();
  };

  editFormOk = () => {
    let vehicleCalibrationData = {};
    vehicleCalibrationData.id = this.editForm?.current?.getFieldValue('id');
    vehicleCalibrationData.vehicleAndCalibrationString =
      this.editForm?.current?.getFieldValue('vehicleAndCalibrationString');
    updateVehicleCalibration(vehicleCalibrationData).then((res) => {
      if (res.code === 200) {
        message.success(res.msg);
        this.dataTable.reload();
      } else {
        message.error(res.msg);
      }
      this.setState({
        rowData: {},
        editFormVisible: false,
      });
      this.editForm.current.resetFields();
    });
  };

  render() {
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 18 },
      },
    };
    this.openModal;
    return (
      <div>
        <EasyTable
          autoFetch
          source={getVehicle}
          dataProp={'data'}
          name={'vehicleCalibrationDataTable'}
          rowKey={'id'}
          columns={this.columns}
          scroll={{ x: 1300 }}
          onChange={this.handleTableChange}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <div className={'btn-group'}>
              <Authorized route={CARS_VEHICLE_CALIBRATION_TEMPLATE}>
                <Button
                  onClick={() => this.openDownloadTemplate()}
                  type={'ghost'}
                  size={'large'}
                  icon={<CloudDownloadOutlined />}
                >
                  导入模板
                </Button>
              </Authorized>
              <Authorized route={CARS_VEHICLE_CALIBRATION_EXPORT}>
                <Button
                  onClick={() => this.openModalExport()}
                  type={'ghost'}
                  size={'large'}
                  icon={<DownloadOutlined />}
                >
                  导出
                </Button>
              </Authorized>
              <Authorized route={CARS_VEHICLE_CALIBRATION_IMPORT}>
                <Button
                  onClick={() =>
                    importExcel(
                      '/dkserver-back/dkmVehicleCalibrationData/importByExcel',
                      this,
                    )
                  }
                  type={'ghost'}
                  size={'large'}
                  icon={<CloudUploadOutlined />}
                >
                  导入
                </Button>
              </Authorized>
            </div>
          }
        />

        <DrawerConfirm
          title={'标定数据'}
          width={600}
          visible={this.state.editFormVisible}
          onCancel={this.editFormCancel}
          onOk={this.editFormOk}
        >
          <Form {...formItemLayout} ref={this.editForm}>
            <Form.Item name="id" hidden>
              <Input type={'hidden'} />
            </Form.Item>
            <Form.Item
              name="vehicleModel"
              label={'车' + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + '型'}
            >
              <Input readOnly={true} />
            </Form.Item>

            <Form.Item name="level" label={'蓝牙灵敏度等级'}>
              <Input readOnly={true} />
            </Form.Item>
            <Form.Item
              name="vehicleAndCalibrationString"
              label={'手机标定数据'}
            >
              <Input.TextArea autoSize={true} />
            </Form.Item>
          </Form>
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
