import React, { Component } from 'react';
import { Button, Col, DatePicker, Form, Input, Row } from 'antd';
import EasyTable from '@/components/EasyTable';
import moment from 'moment';

const { RangePicker } = DatePicker;
@EasyTable.connect(({ aftermarketReplacementDataTable }) => ({
  aftermarketReplacementDataTable,
}))
class SearchForm extends Component {
  form = React.createRef();
  handleSubmit = (values) => {
    this.form.current
      .validateFields()
      .then((values) => {
        if (values.startTime) {
          const startTime = values['startTime'];
          values.startTime = moment(startTime[0]).format('YYYY-MM-DD');
          values.endTime = moment(startTime[1])
            .add(1, 'days')
            .format('YYYY-MM-DD');
        }
        this.props.aftermarketReplacementDataTable.fetch(values);
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
            <Form.Item label={'车辆vin号'} name="vin">
              <Input placeholder={'车辆vin号'} />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="换件时间" name={'startTime'}>
              <RangePicker />
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
      </Form>
    );
  }
}

export default SearchForm;
