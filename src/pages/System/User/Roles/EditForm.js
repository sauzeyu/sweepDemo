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

  handleSubmit = (values) => {
    this.form.current.validateFields().then((values) => {
      values.menuList = this.state.keys;
      console.log('values', values);

      values.updator =
        getDvaApp()._store.getState()?.user?.currentUser?.username;
      values.updateTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
      values.checkedKey = this.state.checkedKeys?.checked;
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
      })
      .catch((e) => {
        Modal.error({
          title: '数据加载失败',
          content: e.message,
        });
      });
  }

  onCheck = (checkedKeys, info) => {
    let checkedKeysResult = [...checkedKeys, ...info.halfCheckedKeys];

    this.setState({
      checkedKeys: checkedKeys,
      keys: checkedKeysResult,
    });
  };

  onSelect = (checkedKeys, info) => {
    let checkedKeysResult = [...checkedKeys, ...info.halfCheckedKeys];
    console.log('info', info);
    console.log('checkedKeysResult', checkedKeysResult);
    // debugger;
    this.setState({
      checkedKeys: checkedKeysResult,
    });
    this.state.selectedKeys = checkedKeysResult;
    console.log('this.state.checkedKeys', this.state.checkedKeys);
  };

  onLoad = (loadedKeys, tree) => {};
  changeSubmit = () => {
    this.props.finish(true);
  };
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
        <Form.Item label={'菜单权限'} name={'menuList'}>
          {menus.length > 0 && (
            <Tree
              onLoad={this.onLoad}
              checkable
              // checkStrictly

              autoExpandParent={false}
              onCheck={this.onCheck}
              onSelect={this.onSelect}
              treeData={menus}
              checkedKeys={this.state.checkedKeys}
            />
          )}
        </Form.Item>
        <Form.Item {...formItemLayout} label={'角色描述'} name="intro">
          <Input.TextArea maxLength={200} />
        </Form.Item>
      </Form>
    );
  }
}
