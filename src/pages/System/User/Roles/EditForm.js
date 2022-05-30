'use strict';
import React, { Component, useState } from 'react';
import { Form, Input, Modal, Select, Spin, message, Tree } from 'antd';
import { selectMenuTree, selectMenuByRoleId } from '@/services/menu';
import { updateRoleById } from '@/services/role';
import { removeEmptyProperty } from '@/utils';
import moment from 'moment';
import { getDvaApp } from '@@/plugin-dva/exports';

export default class EditForm extends Component {
  form = React.createRef();
  state = {
    menus: [],
    checkedKeys: [],
    selectedKeys: [],
  };

  componentWillReceiveProps(nextProps, nextContext) {
    this.setState({
      checkedKeys: nextProps.keyList,
    });
  }

  componentDidMount() {
    this.fetchMenus();
    this.props.editFormRef && this.props.editFormRef(this);
  }

  handleSubmit = (val) => {
    this.form.current.validateFields().then((values) => {
      values.updator =
        getDvaApp()._store.getState()?.user?.currentUser?.username;
      values.updateTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
      // if (this.state.selectedKeys.length === 0) {
      values.checkedKey = this.state.checkedKeys?.checked;
      // } else {
      //   values.checkedKey = this.state.selectedKeys;
      // }
      updateRoleById(values).then(
        (res) => {
          message.success(res.msg);
          this.props.dataTableRef.refresh();
        },
        (res) => {
          message.error(res.msg);
        },
      );
      this.setState({
        selectedKeys: [],
      });
    });
  };

  fetchMenus() {
    selectMenuTree()
      .then((res) => {
        this.setState({
          menus: res.data,
        });
        console.log('res.data menus -----', res.data);
      })
      .catch((e) => {
        Modal.error({
          title: '数据加载失败',
          content: e.message,
        });
      });
  }

  onCheck = (checkedKey, event) => {
    console.log('checkedKey ', checkedKey);
    console.log('checkedKey event ', event);
    this.setState({
      checkedKeys: checkedKey,
      // selectedKeys: checkedKey.concat(event.halfCheckedKeys),
    });
  };
  onLoad = (loadedKeys, tree) => {};

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
    const { menus } = this.state;
    return (
      // <Spin spinning={loadingDetail}>
      <Form ref={this.form} {...formItemLayout} onFinish={this.handleSubmit}>
        <Form.Item name="id" hidden>
          <Input type="hidden" />
        </Form.Item>
        <Form.Item
          label={'角色名称'}
          name="roleName"
          rules={[{ required: true, message: '角色名称不能为空' }]}
        >
          <Input maxLength={40} placeholder={'角色名称'} />
        </Form.Item>
        <Form.Item
          label={'角色代码'}
          name="code"
          rules={[{ required: true, message: '角色代码不能为空' }]}
        >
          <Input maxLength={5} placeholder={'角色代码'} />
        </Form.Item>
        <Form.Item label={'菜单权限'}>
          {menus.length > 0 && (
            <Tree
              onLoad={this.onLoad}
              checkable
              checkStrictly
              autoExpandParent={false}
              onCheck={this.onCheck}
              treeData={menus}
              checkedKeys={this.state.checkedKeys}
            />
          )}
        </Form.Item>
        <Form.Item {...formItemLayout} label={'角色描述'} name="intro">
          <Input.TextArea maxLength={200} />
        </Form.Item>
      </Form>
      // </Spin>
    );
  }
}
