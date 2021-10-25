import React from 'react';
import { Form, Input, Radio } from 'antd';
import { ResTypes } from '@/constants/permission';
import { FormattedMessage } from 'umi';
import FormItemInterceptor from '@/components/FormItemInterceptor';

export default class extends React.Component {
  form = React.createRef();
  UNSAFE_componentWillReceiveProps(nextProps) {
    if (
      this.props.permission &&
      nextProps.permission &&
      this.props.permission !== nextProps.permission &&
      nextProps.permission.selectedPermission &&
      this.props.permission.selectedPermission !==
        nextProps.permission.selectedPermission
    ) {
      this.setFormValues(nextProps.permission.selectedPermission);
    }
  }
  setFormValues(values) {
    this.form.current.setFieldsValue({
      type: values.type,
      id: values.id,
      title: values.title,
      href: values.href,
      icon: values.icon,
      permission: values.permission,
    });
  }
  handleChange = (evt) => {
    this.props.onChange && this.props.onChange(evt);
  };
  render() {
    const { permission } = this.props;
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
    let level = 0,
      type = this.form?.current?.getFieldValue('type');
    const { selectedPermission } = permission;
    if (selectedPermission) {
      level = selectedPermission.$dna.length;
    }
    const labels = {
      type: <FormattedMessage id={'Model.permissions.type'} />,
      name: <FormattedMessage id={'Model.permissions.name'} />,
      status: <FormattedMessage id={'Model.permissions.status'} />,
      uri: <FormattedMessage id={'Model.permissions.uri'} />,
      icon: <FormattedMessage id={'Model.permissions.icon'} />,
      describe: <FormattedMessage id={'Model.permissions.describe'} />,
    };
    return (
      <Form onChange={this.handleChange} ref={this.form}>
        <Form.Item name={'id'} hidden={true}>
          <Input type={'hidden'} />
        </Form.Item>
        <Form.Item
          name={'type'}
          initialValue={0}
          {...formItemLayout}
          label={labels.type}
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id={'Validator.required'}
                  values={{ name: labels.type }}
                />
              ),
            },
          ]}
        >
          <FormItemInterceptor pipes={FormItemInterceptor.Pipes.Number}>
            <Radio.Group>
              {ResTypes.map((type) => {
                if ('level' in type && type.level !== level) return null;
                return (
                  <Radio key={type.value} value={type.value}>
                    <FormattedMessage id={type.label} />
                  </Radio>
                );
              })}
            </Radio.Group>
          </FormItemInterceptor>
        </Form.Item>
        <Form.Item
          name={'title'}
          {...formItemLayout}
          label={labels.name}
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id={'Validator.required'}
                  values={{ name: labels.name }}
                />
              ),
            },
          ]}
        >
          <Input maxLength={40} />
        </Form.Item>
        <Form.Item
          name={'href'}
          {...formItemLayout}
          label={labels.uri}
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id={'Validator.required'}
                  values={{ name: labels.uri }}
                />
              ),
            },
          ]}
        >
          <Input maxLength={200} />
        </Form.Item>
        <Form.Item
          name={'icon'}
          {...formItemLayout}
          label={labels.icon}
          rules={
            level === 1 || type === 'StatusBar'
              ? [
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id={'Validator.required'}
                        values={{ name: labels.icon }}
                      />
                    ),
                  },
                ]
              : []
          }
        >
          <Input maxLength={100} />
        </Form.Item>
        <Form.Item
          name={'permission'}
          {...formItemLayout}
          label={'接口权限标识'}
        >
          <Input maxLength={200} />
        </Form.Item>
      </Form>
    );
  }
}
