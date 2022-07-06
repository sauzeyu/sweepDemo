import React, { Component } from 'react';
import { Button, Col, DatePicker, Form, Input, Row } from 'antd';
import EasyTable from '@/components/EasyTable';
import moment from 'moment';

const { RangePicker } = DatePicker;
@EasyTable.connect(({ roleDataTable }) => ({
  roleDataTable,
}))
export default class SearchForm extends Component {
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
        this.props.roleDataTable.fetch(values);
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
      span: 6,
    };

    // const rangeConfig = {
    //   rules: [{ type: 'array' as const, required: true, message: 'Please select time!' }],
    // };

    return (
      <Form {...formItemLayout} onFinish={this.handleSubmit} ref={this.form}>
        <Row>
          <Col {...colSpan}>
            <Form.Item label={'角色名称'} name="roleName">
              <Input placeholder={'请输入角色名称'} />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'角色代码'} name="code">
              <Input placeholder={'请输入角色代码'} />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="创建时间" name={'startTime'}>
              <RangePicker />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
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
