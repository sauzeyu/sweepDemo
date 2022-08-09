'use strict';
import React, { Component } from 'react';
import { Button, Col, Form, Input, Modal, Select, Spin, message } from 'antd';
import { getAllRole, updateAdminById } from '@/services/admin';
import moment from 'moment';
import { getDvaApp } from '@@/plugin-dva/exports';

export default class EditForm extends Component {
  form = React.createRef();
  state = {
    roles: [],
  };

  componentDidMount() {
    this.fetchRoles();
    this.props.editFormRef && this.props.editFormRef(this);
  }

  handleSubmit = () => {
    debugger;
    this.form.current.validateFields().then((values) => {
      console.log(values);
      values.updator =
        getDvaApp()._store.getState()?.user?.currentUser?.username;
      values.updateTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
      updateAdminById(values).then(
        (res) => {
          message.success(res.msg);
          this.props.dataTableRef.refresh();
        },
        (res) => {
          message.error(res.msg);
        },
      );
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
  changeSubmit = () => {
    this.props.finish(true);
  };
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
      // <Spin spinning={loadingDetail}>
      <Form
        ref={this.form}
        {...formItemLayout}
        onFinish={this.handleSubmit}
        onFinishFailed={this.changeSubmit}
      >
        <Form.Item name="id" hidden>
          <Input type="hidden" />
        </Form.Item>
        <Form.Item
          label={'姓名'}
          name="username"
          rules={[{ required: true, message: '姓名不能为空' }]}
        >
          <Input maxLength={40} placeholder={'用户姓名'} />
        </Form.Item>
        <Spin spinning={loadingRoles}>
          <Form.Item
            label={'权限角色'}
            name="roleList"
            rules={[{ required: true, message: '权限角色不能为空' }]}
          >
            <Select allowClear>
              {roles.map((role) => {
                return (
                  <Select.Option key={role.id} value={role.roleName}>
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
      // </Spin>
    );
  }
}
