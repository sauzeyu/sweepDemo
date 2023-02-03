'use strict';
import React, { Component } from 'react';
import { Button, Col, Form, Input, Modal, Select, Spin, message } from 'antd';
import {
  getAllRole,
  updateAdminById,
  resetPasswordById,
} from '@/services/admin';
import moment from 'moment';
import { getDvaApp } from '@@/plugin-dva/exports';
import { getPublicPath, md5 } from '@/utils';
export default class ResetPasswordForm extends Component {
  form = React.createRef();
  state = {
    roles: [],
  };

  componentDidMount() {
    this.fetchRoles();
    this.props.resetPasswordRef && this.props.resetPasswordRef(this);
  }

  handleSubmit = () => {
    this.form.current.validateFields().then((values) => {
      if (values?.roleList) {
        let role = values.roleList;
        if (role instanceof Array && role.length > 0) {
          values.roleList = values.roleList[0];
        }
      }

      // values.roleList = values.roleList[0];
      values.password = md5(values.password);
      values.updator =
        getDvaApp()._store.getState()?.user?.currentUser?.username;
      values.updateTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
      resetPasswordById(values).then(
        (res) => {
          message.success(res.msg);
          this.props.dataTableRef.reload();
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
          label={'密码'}
          name="password"
          rules={[
            { required: true, message: '密码不能为空' },
            {
              pattern: new RegExp(
                /(?=^.{8,}$)(?=.*\d)(?=.*\W+)(?=.*[A-Z])(?=.*[a-z])(?!.*\n).*$/,
              ),
              message:
                '密码必须由数字/大写字母/小写字母/特殊字符组成,长度在8位以上',
            },
          ]}
        >
          <Input.Password
            maxLength={16}
            placeholder={'请输入密码'}
            id="addPassword"
          />
        </Form.Item>
      </Form>
      // </Spin>
    );
  }
}
