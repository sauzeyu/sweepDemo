import React, { Component } from 'react';
import { Button, Col, DatePicker, Form, Input, Row, Select } from 'antd';
import EasyTable from '@/components/EasyTable';

@EasyTable.connect(({ vehicleCalibrationDataTable }) => ({
  vehicleCalibrationDataTable,
}))
class SearchForm extends Component {
  form = React.createRef();

  handleSubmit = (values) => {
    this.form.current
      .validateFields()
      .then((values) => {
        this.props.vehicleCalibrationDataTable.fetch(values);
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
      sm: 5,
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
          let result = {};
          allFields.forEach((field) =>
            Object.defineProperty(result, field.name.toString(), field),
          );

          this.props.getFormValues(result);
        }}
      >
        <Row type={'flex'} gutter={16}>
          <Col {...colSpan1}>
            <Form.Item label={'车型'} name="vehicleModel">
              <Input placeholder={'请输入车辆型号'} />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'蓝牙灵敏度'} name="level">
              <Select
                // mode={'multiple'}
                allowClear={true}
                showArrow
                placeholder="请选择蓝牙灵敏度等级"
              >
                <Select.Option key={1} value={1}>
                  1
                </Select.Option>
                <Select key={2} value={2}>
                  2
                </Select>
                <Select key={3} value={3}>
                  3
                </Select>
              </Select>
            </Form.Item>
          </Col>
          <Col {...colSpan2} style={{ flex: 1 }}>
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
      </Form>
    );
  }
}

export default SearchForm;
