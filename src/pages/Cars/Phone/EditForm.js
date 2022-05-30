import React, { Component } from 'react';
import {
  Col,
  DatePicker,
  Form,
  Input,
  InputNumber,
  message,
  Row,
  Select,
  Spin,
  Switch,
} from 'antd';
import RestrictiveInput from '@/components/RestrictiveInput';

class EditForm extends Component {
  state = {
    isEdit: false,
  };
  form = React.createRef();

  componentDidMount() {
    this.props.editFormRef && this.props.editFormRef(this);
    this.props.wrappedComponentRef && this.props.wrappedComponentRef(this);
    this.setState({
      isEdit: !!this.form.current.getFieldValue('id'),
    });
  }

  render() {
    return (
      <Spin spinning={loading}>
        <Form {...formItemLayout} ref={this.form}>
          <Form.Item name="id" hidden>
            <Input type={'hidden'} />
          </Form.Item>
          <Form.Item
            label={'车型'}
            name="modelId"
            rules={[{ required: true, message: '车型不能为空' }]}
          >
            <CarTypesSelect
              labelInValue
              disabled={isEdit}
              allowClear={false}
              onChange={this.handleCarTypeChange}
            />
          </Form.Item>
          <Form.Item
            label={'VIN'}
            name="vin"
            rules={[
              { required: true, message: 'VIN不能为空' },
              {
                type: 'string',
                min: 17,
                max: 17,
                message: '请输入17位长度的VIN',
              },
            ]}
          >
            <RestrictiveInput trim disabled={isEdit} maxLength={17} />
          </Form.Item>
        </Form>
      </Spin>
    );
  }
}

export default EditForm;
