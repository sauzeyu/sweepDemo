'use strict';
import React, { Component } from 'react';
import { Button, Col, Form, Input, Modal, Select, Spin, message } from 'antd';
import { getAllRole, insertAdmin } from '@/services/admin';
import { md5, removeEmptyProperty } from '@/utils';
import EasyTable from '@/components/EasyTable';
import { getDvaApp } from '@@/plugin-dva/exports';
import moment from 'moment';
export default class AddUserForm extends Component {
  form = React.createRef();
  state = {
    roles: [],
    addUserVisible: false,
  };

  componentDidMount() {
    this.fetchRoles();
    this.props.addFormEef && this.props.addFormEef(this);
  }

  handleSubmit = () => {
    this.form.current.validateFields().then((values) => {
      values.password = md5(values.password);
      values.creator = getDvaApp()._store.getState().user.currentUser.username;

      insertAdmin(values).then((res) => {
        if (res.code === 200) {
          this.setState({ addUserVisible: false });
          message.success({
            content: res.msg,
          });
        } else {
          message.error({
            content: res.msg,
          });
        }
      });
      this.form.current.resetFields();
    });
  };

  fetchRoles() {
    getAllRole()
      .then((res) => {
        this.setState({
          roles: res.data,
        });
      })
      .catch((e) => {
        Modal.error({
          title: '数据加载失败',
          content: e.message,
        });
      });
  }

  render() {
    const { roles } = this.state;
    const loadingRoles = false;
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
    return (
      <Form ref={this.form} {...formItemLayout} onFinish={this.handleSubmit}>
        <Form.Item
          label={'用户名'}
          name="username"
          rules={[{ required: true, message: '姓名不能为空' }]}
        >
          <Input maxLength={40} placeholder={'请输入用户姓名'} />
        </Form.Item>
        <Form.Item
          label={'密码'}
          name="password"
          rules={[{ required: true, message: '密码不能为空' }]}
        >
          <Input.Password maxLength={40} placeholder={'请输入密码'} />
        </Form.Item>
        <Spin spinning={loadingRoles}>
          <Form.Item
            label={'权限角色'}
            name="role"
            rules={[{ required: true, message: '权限角色不能为空' }]}
          >
            <Select mode={'multiple'} allowClear>
              {roles.map((role) => {
                return (
                  <Select.Option key={role.id} value={role.id}>
                    {role.roleName}
                  </Select.Option>
                );
              })}
            </Select>
          </Form.Item>
        </Spin>

        <Form.Item {...formItemLayout} label={'描述'} name="extraInfo">
          <Input.TextArea maxLength={200} />
        </Form.Item>
      </Form>
    );
  }
}
