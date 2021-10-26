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
          label={'车型名称'}
          name="name"
          rules={[{ required: true, message: '名称不能为空' }]}
        >
          <RestrictiveInput trim maxLength={40} />
        </Form.Item>
        <Form.Item
          label={'代码'}
          name="code"
          rules={[{ required: true, message: '车型代码不能为空' }]}
        >
          <RestrictiveInput trim maxLength={40} disabled={isEdit} />
        </Form.Item>
        <Form.Item
          label={'VIN规则'}
          name="vinMatch"
          rules={[{ required: true, message: 'VIN规则不能为空' }]}
        >
          <RestrictiveInput trim maxLength={17} disabled={this.isEdit} />
        </Form.Item>
        <Form.Item label={'备注'} name="remark">
          <Input.TextArea maxLength={200} />
        </Form.Item>
      </Form>
    );
  }
}

export default EditForm;
