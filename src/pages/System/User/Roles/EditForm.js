'use strict';
import React from 'react';
import { Form, Input, Tree, Spin, Icon } from 'antd';
import {
  LinkOutlined,
  PlayCircleOutlined,
  FolderOutlined,
} from '@ant-design/icons';
import { FormattedMessage, getLocale } from 'umi';
import { connect } from 'dva';

const TreeNode = Tree.TreeNode;

@connect(({ role, loading }) => ({
  role,
  fetching: loading.effects['role/fetchDetail'],
  fetchingPermissions: loading.effects['role/fetchPermissions'],
}))
export default class EditForm extends React.Component {
  form = React.createRef();
  state = {
    detail: null,
    expandedPermissions: [],
  };
  componentDidMount() {
    this.props.wrappedComponentRef && this.props.wrappedComponentRef(this);
    this.fetchPermissions().then(() => {
      this.fetchDetail();
    });
  }
  handlePermissionExpand = (expandedPermissions) => {
    this.setState({ expandedPermissions });
  };
  fetchDetail = () => {
    if (this.props.roleId) {
      this.props
        .dispatch({
          type: 'role/fetchDetail',
          payload: {
            id: this.props.roleId,
          },
        })
        .then((detail) => {
          let user = detail.role;
          const values = {
            ...detail,
            ...user,
            menus: this.unCompriseParentTree(detail.menus),
          };
          this.form.current.setFieldsValue(values);
        });
    }
  };
  fetchPermissions = () => {
    return this.props.dispatch({
      type: 'role/fetchPermissions',
    });
  };
  getValues = () => {
    return this.form.current.validateFields().then((values) => {
      values.menus = this.compriseParentTree(values.menus);
      return values;
    });
  };
  unCompriseParentTree = (list) => {
    const newList = [];
    if (!list || !list.length) return newList;
    const dnaMap = this.props.role.permissionsDNAMap;
    const dnaList = [];
    list.forEach((item) => {
      if (item.dna) {
        dnaList.push(item.dna);
      }
    });
    dnaList
      .sort((a, b) => a.length < b.length)
      .forEach((item) => {
        dnaList.forEach((dna, index) => {
          if (item && dna && item !== dna && item.startsWith(dna)) {
            dnaList[index] = null;
          }
        });
      });
    dnaList.forEach((item) => {
      if (!item) return;
      const permission = dnaMap[item];
      if (permission) {
        newList.push(String(permission.id));
      }
    });
    return newList;
  };
  compriseParentTree = (list) => {
    const {
      role: { permissionsIDMap: idMap, permissionsDNAMap: dnaMap },
    } = this.props;
    const newList = [];
    if (!list || !list.length) return newList;
    list.map((id) => {
      id = String(id);
      const permission = idMap[id];
      if (newList.indexOf(id) === -1) newList.push(id);
      if (permission.$dna.length > 1) {
        //DNA链大于1则说明有父节点
        const chain = [...permission.$dna];
        while (chain.length > 1) {
          chain.pop();
          const dna = chain.join('-');
          const permission = dnaMap[dna];
          const id = String(permission.id);
          if (newList.indexOf(id) === -1) newList.push(id);
        }
      }
    });
    return newList.map((id) => idMap[id]);
  };
  render() {
    const {
      fetching = false,
      fetchingPermissions = false,
      role: { permissions = [] },
    } = this.props;
    const { expandedPermissions } = this.state;
    const formItemLayout = {
      labelCol: { span: 8 },
      wrapperCol: { span: 16 },
    };
    return (
      <Spin spinning={fetching}>
        <Form onChange={this.handleFormChange} ref={this.form}>
          <Form.Item name="id" hidden={true}>
            <Input />
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label={'名称'}
            name={'name'}
            rules={[
              {
                required: true,
                message: (
                  <FormattedMessage
                    id={'Validator.required'}
                    values={{ name: '名称' }}
                  />
                ),
              },
            ]}
          >
            <Input maxLength={40} />
          </Form.Item>
          <Form.Item {...formItemLayout} label={'备注'} name={'remark'}>
            <Input maxLength={255} type="textarea" rows="5" />
          </Form.Item>
          <Form.Item
            {...formItemLayout}
            label={'权限'}
            name={'menus'}
            initialValue={[]}
            rules={[
              {
                required: true,
                message: (
                  <FormattedMessage
                    id={'Validator.required'}
                    values={{ name: '权限' }}
                  />
                ),
              },
            ]}
          >
            <PermissionsTreeSelect
              loading={fetchingPermissions}
              permissions={permissions}
              onChange={this.handleFormChange}
              onExpand={this.handlePermissionExpand}
              expandedKeys={expandedPermissions}
            />
          </Form.Item>
        </Form>
      </Spin>
    );
  }
}

export class PermissionsTreeSelect extends React.Component {
  state = {
    expandedKeys: [],
    autoExpandParent: false,
    checkedKeys: [],
  };

  componentDidMount() {
    this.applyStateFromProps(this.props);
  }

  UNSAFE_componentWillReceiveProps(nextProps) {
    this.applyStateFromProps(nextProps);
  }

  applyStateFromProps(nextProps) {
    const newState = {};
    if ('value' in nextProps) {
      newState.checkedKeys = nextProps.value || [];
    }
    if ('expandedKeys' in nextProps) {
      newState.expandedKeys = nextProps.expandedKeys || [];
    }
    this.setState(newState);
  }

  onExpand = (expandedKeys) => {
    const props = this.props;
    if (!('expandedKeys' in props)) {
      this.setState({
        expandedKeys,
      });
    }
    props.onExpand && props.onExpand(expandedKeys);
  };
  onCheck = (checkedKeys) => {
    const props = this.props;
    if (!('value' in props)) {
      this.setState({
        checkedKeys,
      });
    }
    props.onChange && props.onChange(checkedKeys);
  };

  render() {
    const permissions = this.props.permissions;
    if (!permissions) return null;
    const { autoExpandParent, checkedKeys, expandedKeys } = this.state;
    const loopTree = (data) =>
      data.map((item) => {
        let before = null;
        switch (item.type) {
          case '0':
            before = <LinkOutlined className="menu-icon" />;
            break;
          case '1':
            before = <PlayCircleOutlined className="menu-icon" />;
            break;
          case '2':
            before = <FolderOutlined className="menu-icon" />;
            break;
        }
        const key = String(item.id);
        const title = (
          <span className="menu-item-title">
            {before} {item.title}
          </span>
        );
        if (item.children) {
          return (
            <TreeNode key={key} title={title}>
              {loopTree(item.children)}
            </TreeNode>
          );
        }
        return <TreeNode key={key} title={title} />;
      });
    return (
      <div className="permissions-tree">
        <Spin spinning={this.props.loading}>
          <Tree
            checkable
            onExpand={this.onExpand}
            expandedKeys={expandedKeys}
            autoExpandParent={autoExpandParent}
            onCheck={this.onCheck}
            checkedKeys={checkedKeys}
          >
            {loopTree(permissions)}
          </Tree>
        </Spin>
      </div>
    );
  }
}
