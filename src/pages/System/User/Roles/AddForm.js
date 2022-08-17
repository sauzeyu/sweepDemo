'use strict';
import React, { Component, useState } from 'react';
import {
  Form,
  Input,
  Modal,
  Select,
  Spin,
  message,
  Tree,
  InputNumber,
} from 'antd';
import { selectMenuTree, selectMenuByRoleId } from '@/services/menu';
import { insert } from '@/services/role';
import { removeEmptyProperty } from '@/utils';
import moment from 'moment';
import { getDvaApp } from '@@/plugin-dva/exports';

export default class AddForm extends Component {
  form = React.createRef();
  state = {
    menus: [],
    checkedKeys: [],
  };

  componentDidMount() {
    this.fetchMenus();
    this.props.editFormRef && this.props.editFormRef(this);
  }

  handleSubmit = (values) => {
    this.form.current.validateFields().then((values) => {
      values.menuList = this.state.keys;
      console.log('values', values);
      values.code = values.code + '';
      debugger;
      insert(values).then(
        (res) => {
          if (res.code === 200) {
            message.success(res.msg);
          } else {
            message.error(res.msg);
          }

          this.props.dataTableRef.refresh();
        },
        (res) => {
          message.error(res.msg);
        },
      );
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

  onCheck = (checkedKey, event) => {
    console.log(
      'checkedKey',
      checkedKey,
      'event.halfCheckedKeys ',
      event.halfCheckedKeys,
    );
    let checkedKeysResult = [...checkedKey, ...event.halfCheckedKeys];
    this.setState({
      checkedKeys: checkedKey,
      keys: checkedKeysResult,
    });
  };
  onLoad = (loadedKeys, tree) => {};

  onSelect = (selectedKeys, info) => {
    console.log('selected', selectedKeys, info);
  };
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
          <InputNumber
            style={{ width: 200 }}
            maxLength={5}
            placeholder={'角色代码'}
          />
        </Form.Item>
        <Form.Item label={'菜单权限'} name={'menuList'}>
          {menus.length > 0 && (
            <Tree
              onLoad={this.onLoad}
              checkable
              // checkStrictly
              autoExpandParent={false}
              onCheck={this.onCheck}
              treeData={menus}
              checkedKeys={this.state.checkedKeys}
              onSelect={this.onSelect}
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
