'use strict';
import React from 'react';
import {
  DatePicker,
  Form,
  Input,
  Modal,
  Radio,
  Select,
  Spin,
  Switch,
} from 'antd';
import { FormattedMessage } from 'umi';
import { connect } from 'dva';
import { ValidateRegex } from '@/utils';
import FormItemInterceptor from '@/components/FormItemInterceptor';
import { Sex } from '@/constants/user';

@connect(({ userManage, loading }) => ({
  userManage,
  loadingRoles: loading.effects['userManage/fetchRoles'],
  loadingDetail: loading.effects['userManage/getUserDetail'],
}))
export default class EditForm extends React.Component {
  form = React.createRef();
  state = {
    isEdit: false,
  };
  componentDidMount() {
    this.props.wrappedComponentRef && this.props.wrappedComponentRef(this);
    this.fetchRoles();
    this.setState({
      isEdit: !!this.form.current.getFieldValue('id'),
    });
  }

  fetchRoles() {
    this.props
      .dispatch({
        type: 'userManage/fetchRoles',
      })
      .catch((e) => {
        Modal.error({
          title: '数据加载失败',
          content: e.message,
        });
      });
  }
  setValues(values) {
    this.form.current.setFieldsValue(values);
  }
  getValues() {
    return new Promise((resolve, reject) => {
      this.form.current.validateFields((errors, values) => {
        if (errors) return reject(errors);
        resolve(values);
      });
    });
  }
  reset() {
    this.form.current.resetFields();
  }
  render() {
    const {
      userManage: { roles },
      loadingRoles = false,
      loadingDetail = false,
    } = this.props;
    const formItemLayout = {
      labelCol: { span: 6 },
      wrapperCol: { span: 18 },
    };
    const isEdit = this.state.isEdit;
    return (
      <Spin spinning={loadingDetail}>
        <Form ref={this.form}>
          <Form.Item name="id" hidden>
            <Input type="hidden" />
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label={'登录名'}
            name="loginName"
            rules={[
              { required: true, message: '登录名不能为空' },
              {
                pattern: /^[a-zA-Z0-9_-]{4,16}$/,
                message: '只支持4到16位（字母，数字，_，-）',
              },
            ]}
          >
            <Input
              maxLength={36}
              placeholder={'用于登录的用户名'}
              disabled={isEdit}
            />
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label={'姓名'}
            name="name"
            rules={[{ required: true, message: '姓名不能为空' }]}
          >
            <Input maxLength={40} placeholder={'用户姓名'} />
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label={'性别'}
            name="sex"
            initialValue="F"
            rules={[
              {
                required: true,
                message: '性别必选',
              },
            ]}
          >
            <Radio.Group>
              {Object.keys(Sex).map((sex) => (
                <Radio value={sex}>{Sex[sex]}</Radio>
              ))}
            </Radio.Group>
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label={'电子邮箱'}
            name="email"
            rules={[
              {
                type: 'email',
                message: '请输入正确邮箱',
              },
            ]}
          >
            <Input maxLength={200} />
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label={'联系电话'}
            name="tel"
            rules={[
              {
                pattern: ValidateRegex.MobilePhoneNo,
                message: '请输入正确手机号',
              },
            ]}
          >
            <Input maxLength={11} />
          </Form.Item>
          {/* <Form.Item {...formItemLayout} label={'有效期(截止)'} name='validDate'>
                        <FormItemInterceptor pipes={FormItemInterceptor.Pipes.DateString('YYYY-MM-DD')}>
                            <DatePicker style={{ width: '100%' }} />
                        </FormItemInterceptor>
                    </Form.Item> */}
          <Spin spinning={loadingRoles}>
            <Form.Item
              {...formItemLayout}
              label={'权限角色'}
              name="roleIds"
              rules={[{ required: true, message: '权限角色不能为空' }]}
            >
              <Select mode={'multiple'} allowClear>
                {roles.map((role) => {
                  return (
                    <Select.Option key={role.id} value={role.id}>
                      {role.name}
                    </Select.Option>
                  );
                })}
              </Select>
            </Form.Item>
          </Spin>
          <Form.Item
            {...formItemLayout}
            label={'启用'}
            name="isvalid"
            initialValue={1}
            valuePropName="checked"
          >
            <FormItemInterceptor pipes={FormItemInterceptor.Pipes.Bool2Number}>
              <Switch />
            </FormItemInterceptor>
          </Form.Item>
          <Form.Item {...formItemLayout} label={'备注'} name="remark">
            <Input.TextArea maxLength={200} />
          </Form.Item>
        </Form>
      </Spin>
    );
  }
}
