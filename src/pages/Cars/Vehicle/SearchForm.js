import React, { Component } from 'react';
import { Button, Col, DatePicker, Form, Input, Row, Select } from 'antd';
import EasyTable from '@/components/EasyTable';
import RestrictiveInput from '@/components/RestrictiveInput';

@EasyTable.connect(({ carsVehicleDataTable }) => ({
  carsVehicleDataTable,
}))
class SearchForm extends Component {
  form = React.createRef();
  handleSubmit = (values) => {
    this.form.current
      .validateFields()
      .then((values) => {
        this.props.carsVehicleDataTable.fetch(values);
      })
      .catch((errors) => {
        if (errors) return;
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
    const colSpan = {
      xs: 24,
      sm: 6,
    };
    const colSpan2 = {
      xs: 24,
      sm: 4,
    };
    const colSpan1 = {
      xs: 24,
      sm: 7,
    };
    return (
      <Form
        {...formItemLayout}
        onFinish={this.handleSubmit}
        ref={this.form}
        onFieldsChange={(changedFields, allFields) => {
          this.props.getFormValues(allFields);
        }}
      >
        <Row type={'flex'} gutter={16}>
          <Col {...colSpan}>
            <Form.Item label={'蓝牙设备序列'} name="hwDeviceSn">
              <RestrictiveInput trim placeholder={'请输入蓝牙设备序列号'} />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'车辆vin号'} name="vin">
              <RestrictiveInput trim placeholder={'请输入车辆vin号'} />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'车辆型号'} name="vehicleModel">
              <RestrictiveInput trim placeholder={'请输入车辆型号'} />
            </Form.Item>
          </Col>

          <Col {...colSpan} style={{ flex: 1 }}>
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
          </Col>
        </Row>
        <Row type={'flex'} gutter={16}>
          <Col {...colSpan}>
            <Form.Item label={'车辆品牌'} name="vehicleBrand">
              <RestrictiveInput trim placeholder={'请输入车辆品牌'} />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'车型'} name="vehicleType">
              <RestrictiveInput trim placeholder={'请输入车型'} />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    );
  }
}

export default SearchForm;
