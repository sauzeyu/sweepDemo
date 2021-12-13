import React, { Component } from 'react';
import { Button, Col, Form, Row, Select, Input } from 'antd';
import EasyTable from '@/components/EasyTable';

@EasyTable.connect(({ keysDataTable }) => ({
  keysDataTable,
}))
class SearchForm extends Component {
  form = React.createRef();
  handleSubmit = (evt) => {
    this.form.current
      .validateFields()
      .then((values) => {
        this.props.keysDataTable.fetch(values);
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
            <Form.Item label={'关键词'} name="phone">
              <Input placeholder="请输入手机号码" />
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
