'use strict';
import React, { Component } from 'react';
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
    flag: true,
  };

  componentWillReceiveProps(nextProps, nextContext) {
    this.setState({
      checkedKeys: nextProps.keyList,
    });
  }
  componentDidUpdate(prevProps, prevState, snapshot) {
    if (this.form.current && this.state.flag) {
      this.form.current.setFieldsValue({ menuList: this.state.checkedKeys });
      // this.setState({flag:false})
    }
  }

  componentDidMount() {
    this.fetchMenus();
    this.props.editFormRef && this.props.editFormRef(this);
  }

  handleSubmit = (values) => {
    this.form.current.validateFields().then((values) => {
      if (this.form.current && this.state.flag) {
        this.form.current.setFieldsValue({ menuList: this.state.checkedKeys });
        // this.setState({flag:false})
      }
      if (values.menuList == null || values?.menuList.length === 0) {
        message.error('菜单权限不能为空');
        return;
      }
      values.updator =
        getDvaApp()._store.getState()?.user?.currentUser?.username;
      values.updateTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
      values.checkedKey = this.state.checkedKeys?.checked;
      updateRoleById(values).then(
        (res) => {
          if (res.code === 200) {
            message.success(res.msg);
          } else {
            message.error(res.msg);
          }

          this.props.dataTableRef.reload();
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
        // this.form.current.setFieldsValue({menuList:res.data})
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
    this.form.current.setFieldsValue({ menuList: checkedKeys });
  };

  onSelect = (checkedKeys, info) => {
    let checkedKeysResult = [...checkedKeys, ...info.halfCheckedKeys];
    this.setState({
      checkedKeys: checkedKeysResult,
    });
    this.state.selectedKeys = checkedKeysResult;
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
          rules={[
            { required: true, message: '角色名称不能为空' },
            {
              // pattern: new RegExp(/^(?!(\s+$))^[\w\s]+$/),
              pattern: new RegExp(/^[\u4e00-\u9fa5_a-zA-Z0-9]+$/),

              message: '角色名称不能有特殊字符',
            },
          ]}
        >
          <Input maxLength={40} placeholder={'角色名称'} />
        </Form.Item>
        <Form.Item
          label={'角色代码'}
          name="code"
          rules={[
            { required: true, message: '角色代码不能为空' },

            {
              // pattern: new RegExp(/^(?!(\s+$))^[\w\s]+$/),
              pattern: new RegExp(/^[\u4e00-\u9fa5_a-zA-Z0-9]+$/),

              message: '角色代码不能有特殊字符',
            },
          ]}
        >
          <Input maxLength={5} placeholder={'角色代码'} />
        </Form.Item>
        <Form.Item
          label={'菜单权限'}
          name={'menuList'}
          // rules={[
          //   {
          //     required: true,
          //     message: '菜单权限不能为空',
          //     defaultField: [],
          //     type: 'array',
          //   },
          // ]}
        >
          {menus.length > 0 && (
            <Tree
              onLoad={this.onLoad}
              checkable
              // checkStrictly
              selectable={false}
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
