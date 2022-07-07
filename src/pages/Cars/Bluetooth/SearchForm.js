import React, { Component } from 'react';
import { Button, Col, Form, Row, Select, Input } from 'antd';
import EasyTable from '@/components/EasyTable';
import { Categories } from '@/constants/cars';

@EasyTable.connect(({ carsBluetoothDataTable }) => ({
  carsBluetoothDataTable,
}))
class SearchForm extends Component {
  form = React.createRef();
  handleSubmit = (evt) => {
    this.form.current
      .validateFields()
      .then((values) => {
        this.props.carsBluetoothDataTable.fetch(values);
      })
      .catch((errors) => {
        if (errors) return;
      });
  };
  render() {
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
    };
    const colSpan = {
      xs: 24,
      sm: 6,
    };
    return (
      <Form {...formItemLayout} onFinish={this.handleSubmit} ref={this.form}>
        <Row type={'flex'} gutter={16}>
          <Col {...colSpan}>
            <Form.Item label={'蓝牙设备序列号'} name="hwDeviceSn">
              <Input placeholder="请输入蓝牙设备序列号" />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="蓝牙检索号" name={'searchNumber'}>
              <Input placeholder="请输入蓝牙检索号" />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'设备状态'} name="flag">
              <Select
                mode={'multiple'}
                allowClear={true}
                showArrow
                placeholder="请选择设备状态"
              >
                <Select.Option key={1} value={1}>
                  正常
                </Select.Option>
                <Select.Option key={0} value={0}>
                  报废
                </Select.Option>
              </Select>
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item>
              <Button
                onClick={() => {
                  this.form.current.resetFields();
                }}
              >
                重置
              </Button>
              <Button
                type={'primary'}
                className={'gutter-left_lg'}
                htmlType={'submit'}
              >
                查询
              </Button>
            </Form.Item>
          </Col>
        </Row>
      </Form>
    );
  }
}

export default SearchForm;
