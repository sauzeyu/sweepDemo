import React, { Component } from 'react';
import { Form, Input, Select, Switch } from 'antd';
import { Categories } from '@/constants/cars';
import RestrictiveInput from '@/components/RestrictiveInput';

class EditForm extends Component {
  form = React.createRef();
  state = {
    isEdit: false,
  };
  componentDidMount() {
    this.props.editFormRef && this.props.editFormRef(this);
    this.setState({
      isEdit: !!this.form.current.getFieldValue('id'),
    });
  }

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

    const isEdit = this.state.isEdit;
    return (
      <Form {...formItemLayout} ref={this.form}>
        <Form.Item name="id" hidden>
          <Input type={'hidden'} />
        </Form.Item>
        <Form.Item
          label={'姓名'}
          name="username"
          rules={[{ required: true, message: '姓名不能为空' }]}
        >
          <RestrictiveInput trim maxLength={40} />
        </Form.Item>

        <Form.Item
          label={'手机'}
          name="phone"
          rules={[{ required: true, message: '电话不能为空' }]}
        >
          <RestrictiveInput trim maxLength={11} disabled={isEdit} />
        </Form.Item>

        <Form.Item
          label={'指纹'}
          name="phoneFingerprint"
          rules={[{ required: true, message: '指纹不能为空' }]}
        >
          <RestrictiveInput trim maxLength={40} disabled={isEdit} />
        </Form.Item>

        <Form.Item
          label={'身份证'}
          name="idCard"
          rules={[{ required: true, message: '身份证不能为空' }]}
        >
          <RestrictiveInput trim maxLength={18} disabled={isEdit} />
        </Form.Item>
        <Form.Item
          label={'密码'}
          name="password"
          rules={[{ required: true, message: '密码不能为空' }]}
        >
          <RestrictiveInput trim maxLength={40} disabled={isEdit} />
        </Form.Item>
      </Form>
    );
  }
}

export default EditForm;
