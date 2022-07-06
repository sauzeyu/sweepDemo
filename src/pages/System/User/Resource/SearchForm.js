import React, { Component } from 'react';
import { Button, Col, Form, Input, Row } from 'antd';
import EasyTable from '@/components/EasyTable';
import moment from 'moment';

@EasyTable.connect(({ resourceDataTable }) => ({
  resourceDataTable,
}))
export default class SearchForm extends Component {
  form = React.createRef();
  handleSubmit = (values) => {
    this.form.current
      .validateFields()
      .then((values) => {
        this.props.resourceDataTable.fetch(values);
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

    return (
      <Form {...formItemLayout} onFinish={this.handleSubmit} ref={this.form}>
        <Row gutter={16}>
          <Col {...colSpan}>
            <Form.Item label={'菜单名'} name="title">
              <Input placeholder={'请输入菜单名'} />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'icon'} name="icon">
              <Input placeholder={'请输入icon'} />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="路由" name={'href'}>
              <Input placeholder={'请输入路由'} />
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
