'use strict';
import React, { Component } from 'react';
import { Button, Col, Form, Input, Modal, Select, Spin, message } from 'antd';
import { getAllRole, updateAdminById } from '@/services/admin';
import moment from 'moment';

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
    // console.log("this.dataTableRef ", this.props.dataTableRef)
    this.form.current.validateFields().then((values) => {
      values.updator = localStorage.getItem('last_username');
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

  render() {
    const { roles } = this.state;
    const loadingRoles = false;
    const loadingDetail = false;
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
      <Form ref={this.form} {...formItemLayout} onFinish={this.handleSubmit}>
        <Form.Item name="id" hidden>
          <Input type="hidden" />
        </Form.Item>
        <Form.Item
          label={'姓名'}
          colon={false}
          name="username"
          rules={[{ required: true, message: '姓名不能为空' }]}
        >
          <Input maxLength={40} placeholder={'用户姓名'} />
        </Form.Item>
        <Spin spinning={loadingRoles}>
          <Form.Item
            label={'权限角色'}
            colon={false}
            name="roleList"
            rules={[{ required: true, message: '权限角色不能为空' }]}
          >
            <Select mode={'multiple'} allowClear>
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
        <Form.Item
          {...formItemLayout}
          label={'额外信息'}
          colon={false}
          name="extraInfo"
        >
          <Input.TextArea maxLength={200} />
        </Form.Item>
      </Form>
      // </Spin>
    );
  }
}
