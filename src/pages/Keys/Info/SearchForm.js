import React, { Component } from 'react';
import { Button, Col, Form, Row, Select, Input } from 'antd';
import EasyTable from '@/components/EasyTable';
import { Permit } from '@/constants/keys';

@EasyTable.connect(({ keysDataTable }) => ({
  keysDataTable,
}))
class SearchForm extends Component {
  form = React.createRef();
  handleSubmit = (evt) => {
    this.form.current
      .validateFields()
      .then((values) => {
        if (values.permission && values.permission.length > 0) {
          values.permission = values.permission.reduce(
            (prev, current) => prev + current,
          );
        }
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
    // const colSpan = {
    //   xs: 24,
    //   sm: 6,
    // };
    const permit = [1, 2, 4, 8];
    return (
      <Form {...formItemLayout} onFinish={this.handleSubmit} ref={this.form}>
        <Row type={'flex'}>
          <Col span={8}>
            <Form.Item label={'钥匙权限'} name="permission">
              <Select mode={'multiple'} allowClear={true}>
                {permit.map((permission) => {
                  return (
                    <Select.Option key={permission} value={permission}>
                      {Permit[permission]}
                    </Select.Option>
                  );
                })}
              </Select>
            </Form.Item>
          </Col>
          <Col span={8}>
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
