import React, { Component } from 'react';
import request from '@/utils/request';
import axios from 'axios';
import EasyTable from '@/components/EasyTable';
import DrawerConfirm from '@/components/DrawerConfirm';
import FileUploader from '@/components/FileUploader';
import { getPublicPath } from '@/utils';
import { Button, message, Modal, Upload, Row, Col, Form, Input } from 'antd';
import {
  CloudUploadOutlined,
  InboxOutlined,
  DownloadOutlined,
  CloudDownloadOutlined,
} from '@ant-design/icons';
import { getPhone, updatePhone } from '@/services/cars';
import { connect } from 'dva';
import { getDvaApp } from '@@/plugin-dva/exports';

const { Dragger } = Upload;
const xlsTemp = getPublicPath('template/xlsTemp.xls');
const xlsxTemp = getPublicPath('template/xlsxTemp.xlsx');

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
      title: '车型',
      dataIndex: 'vehicleModel',
    },
    {
      title: '手机品牌',
      dataIndex: 'phoneBrand',
    },
    {
      title: '手机型号',
      dataIndex: 'phoneModel',
    },
    {
      title: '手机标定数据',
      dataIndex: 'personalAndCalibrationString',
      width: 650,
    },
    {
      title: '操作',
      fixed: 'right',
      render: (row) => {
        return (
          <div className={'link-group'}>
            <a onClick={() => this.editRow(row)}>编辑</a>
          </div>
        );
      },
    },
  ];

  exportExcel = (isXlsx) => {
    let vehicleModel = this.props.searchFormValues[0];
    let phoneBrand = this.props.searchFormValues[1];
    let fileName;
    let param = new URLSearchParams();
    if (vehicleModel && vehicleModel.value) {
      param.append('vehicleModel', vehicleModel.value);
    }
    if (phoneBrand && phoneBrand.value) {
      param.append('phoneBrand', phoneBrand.value);
    }
    if (isXlsx) {
      param.append('isXlsx', isXlsx);
      fileName = '手机标定数据.xlsx';
    } else {
      fileName = '手机标定数据.xls';
      param.append('isXlsx', isXlsx);
    }
    // Excel 导出时 axios 不能配置拦截器
    axios({
      method: 'post',
      url: '/dkserver-back/dkmPhoneCalibrationData/downloadCalibrationExcel',
      data: param,
      responseType: 'blob',
      headers: {
        'access-token': getDvaApp()._store.getState().user.currentUser.token,
      },
    }).then((res) => {
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

  openModalImport = () => {
    const that = this;
    const props = {
      name: 'file',
      multiple: true,
      action: '/dkserver-back/dkmPhoneCalibrationData/importByExcel',
      headers: {
        'access-token': getDvaApp()._store.getState().user.currentUser.token,
      },
      onChange(info) {
        const { status } = info.file;
        if (status !== 'uploading') {
          console.log(info.file, info.fileList);
        }
        if (status === 'done') {
          const { response } = info.file;
          if (response.code === 200) {
            message.success(`${info.file.name} 文件上传成功.`);
            that.dataTable.reload();
          } else if (response.code === 500) {
            info.file.status = 'error';
            message.error(response.msg + ` ${info.file.name} 文件上传失败.`);
          }
        } else if (status === 'error') {
          info.file.status = 'error';
          message.error(`${info.file.name} 文件上传失败.`);
        }
      },
    };
    return Modal.info({
      width: 500,
      icon: '',
      okText: '取消',
      closable: true,
      content: (
        <Dragger {...props}>
          <p className="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p className="ant-upload-text">单击或拖动文件到此区域上传</p>
          <p className="ant-upload-hint">仅支持 xlsx/xls 格式文件</p>
        </Dragger>
      ),
    });
  };

  editRow = (row) => {
    console.log('row ', row);
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
    let phoneCalibrationData = {};
    phoneCalibrationData.id = this.editForm?.current?.getFieldValue('id');
    phoneCalibrationData.personalAndCalibrationString =
      this.editForm?.current?.getFieldValue('personalAndCalibrationString');
    updatePhone(phoneCalibrationData).then((res) => {
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
          onChange={this.handleTableChange}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <div className={'btn-group'}>
              <Button
                onClick={() => this.openDownloadTemplate()}
                type={'primary'}
                icon={<CloudDownloadOutlined />}
              >
                导入模板
              </Button>
              <Button
                onClick={() => this.openModalExport()}
                type={'primary'}
                icon={<DownloadOutlined />}
              >
                导出
              </Button>
              <Button
                onClick={() => this.openModalImport()}
                type={'primary'}
                icon={<CloudUploadOutlined />}
              >
                导入
              </Button>
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
            <Form.Item name="phoneBrand" label={'手机品牌'}>
              <Input readOnly={true} />
            </Form.Item>
            <Form.Item name="phoneModel" label={'手机型号'}>
              <Input readOnly={true} />
            </Form.Item>
            <Form.Item name="personalAndCalibrationString" label={'标定数据'}>
              <Input.TextArea autoSize={true} />
            </Form.Item>
          </Form>
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
