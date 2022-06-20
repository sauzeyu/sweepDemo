import React, { Component } from 'react';
import { Button, Col, Form, Row, Select, Input, DatePicker } from 'antd';
import EasyTable from '@/components/EasyTable';
import { Permit } from '@/constants/keys';
import moment from '_moment@2.29.1@moment';
const { RangePicker } = DatePicker;

@EasyTable.connect(({ keyErrorLogDataTable }) => ({
  keyErrorLogDataTable,
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
        this.props.keyErrorLogDataTable.fetch(values);
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
        <Row type={'flex'}>
          <Col {...colSpan}>
            <Form.Item label={'车辆vin号'} name="vin">
              <Input placeholder="请输入车辆vin码" />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'用户id'} name="userId">
              <Input placeholder="请输入用户id" />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="手机品牌" name={'phoneBrand'}>
              <Input placeholder="请输入手机品牌" />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="手机型号" name={'phoneModel'}>
              <Input placeholder="请输入手机型号" />
            </Form.Item>
          </Col>
        </Row>
        <Row>
          <Col {...colSpan}>
            <Form.Item label="操作类型" name={'statusCode'}>
              <Input placeholder="请输入手机型号" />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'操作结果'} name="flag">
              <Select mode={'multiple'} allowClear={true}>
                <Select.Option key={1} value={1}>
                  成功
                </Select.Option>
                <Select.Option key={0} value={0}>
                  失败
                </Select.Option>
              </Select>
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="手机型号" name={'phoneModel'}>
              <Input placeholder="请输入手机型号" />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="操作时间" name={'startTime'}>
              <RangePicker />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item>
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
