import React, { Component } from 'react';
import { Button, Col, DatePicker, Form, Input, Row, Select } from 'antd';
import EasyTable from '@/components/EasyTable';

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
    return (
      <Form {...formItemLayout} onFinish={this.handleSubmit} ref={this.form}>
        <Row type={'flex'}>
          <Col {...colSpan}>
            <Form.Item label={'关键字'} name="keywords">
              <Input placeholder={'车型代码/车牌号/车主手机号/VIN'} />
            </Form.Item>
          </Col>
          <Col {...colSpan} style={{ flex: 1 }}>
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
