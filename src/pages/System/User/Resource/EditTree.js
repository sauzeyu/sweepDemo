import React from 'react';
import { Tree, Icon, Modal, Dropdown, Menu } from 'antd';
const TreeNode = Tree.TreeNode;
import classNames from 'classnames';
import styles from './style.less';
import { FormattedMessage, formatMessage, getLocale } from 'umi';
import { filterPermissions } from './utils';
import {
  LinkOutlined,
  PlayCircleOutlined,
  FolderOutlined,
  PlusCircleOutlined,
  MinusCircleOutlined,
} from '@ant-design/icons';
export default class extends React.Component {
  state = {
    expandedKeys: [],
    autoExpandParent: true,
  };
  UNSAFE_componentWillReceiveProps(nextProps) {
    if (this.props.filter !== nextProps.filter) {
      this.filter(nextProps.filter);
    }
    if (
      this.props.permission &&
      nextProps.permission &&
      this.props.permission !== nextProps.permission &&
      nextProps.permission.selectedPermission &&
      this.props.permission.selectedPermission !==
        nextProps.permission.selectedPermission
    ) {
      const dna = [...nextProps.permission.selectedPermission.$dna];
      dna.pop();
      if (dna.length)
        this.setState({
          expandedKeys: this.state.expandedKeys.concat([dna.join('-')]),
        });
    }
  }
  componentDidMount() {
    if (this.props.filter) this.filter(this.props.filter);
  }
  filter = (keywords) => {
    this.handleSelect([false]);
    const filteredKeys = filterPermissions(
      this.props.permission.permissions,
      keywords,
    );
    this.setState({
      expandedKeys: filteredKeys,
      autoExpandParent: true,
    });
  };
  handleExpand = (expandedKeys) => {
    this.setState({
      expandedKeys,
      autoExpandParent: false,
    });
  };
  handleDrop = (info) => {
    this.props.onChange('drop', info);
  };
  handleSelect = (selectedKeys) => {
    selectedKeys = selectedKeys.splice(0, 1);
    this.props.onSelect && this.props.onSelect(selectedKeys[0]);
  };
  del = (permission) => {
    if (!permission.id) {
      return this.props.onChange('del', permission);
    }
    Modal.confirm({
      title: formatMessage({ id: 'Common.message.sure' }),
      content: (
        <div
          style={{ lineHeight: 1.8 }}
          dangerouslySetInnerHTML={{
            __html: formatMessage(
              { id: 'Page.system.permissions.deletePrompt' },
              { name: permission.name },
            ),
          }}
        />
      ),
      onOk: () => {
        this.props.onChange('del', permission);
      },
    });
  };
  add = (permission, level) => {
    this.props.onChange('insert', { permission, level });
  };
  render() {
    const { permissions, selectedPermission, existsPermissionsID } =
      this.props.permission;
    const selectedKeys = [];
    if (selectedPermission) selectedKeys.push(selectedPermission.dna);
    const { expandedKeys, autoExpandParent } = this.state;
    const { filter } = this.props;
    const loop = (data) =>
      data.map((item) => {
        let before = null,
          after = null;
        switch (String(item.type)) {
          case '0':
            before = <LinkOutlined className="menu-icon" />;
            break;
          case '1':
            before = <PlayCircleOutlined className="menu-icon" />;
            break;
          case '2':
            before = <FolderOutlined className="menu-icon" />;
            break;
          default:
            break;
        }
        if (selectedKeys[0] === item.dna) {
          after = (
            <div className="menu-item-bars inline">
              <MinusCircleOutlined
                className="button"
                onClick={() => this.del(item)}
              />
              <Dropdown
                trigger={['click']}
                placement="bottomCenter"
                overlay={
                  <Menu>
                    <Menu.Item>
                      <a onClick={() => this.add(selectedPermission, 1)}>
                        <FormattedMessage
                          id={'Page.system.permissions.addBrother'}
                        />
                      </a>
                    </Menu.Item>
                    <Menu.Item>
                      <a onClick={() => this.add(selectedPermission, 2)}>
                        <FormattedMessage
                          id={'Page.system.permissions.addChild'}
                        />
                      </a>
                    </Menu.Item>
                  </Menu>
                }
              >
                <PlusCircleOutlined className="button" />
              </Dropdown>
            </div>
          );
        }
        let name = item.title || '';
        const isExists = existsPermissionsID.indexOf(item.id) !== -1;
        if (filter) {
          const index = name.indexOf(filter);
          const beforeStr = name.substr(0, index);
          const afterStr = name.substr(index + filter.length);
          name =
            index > -1 ? (
              <span>
                {beforeStr}
                <span style={{ color: '#f50' }}>{filter}</span>
                {afterStr}
              </span>
            ) : (
              <span>{name}</span>
            );
        }
        const title = (
          <span className={classNames({ 'text-primary': !isExists })}>
            {before}
            {name}
            {after}
          </span>
        );

        const key = item.dna;
        if (item.children) {
          return (
            <TreeNode key={key} title={title} className={styles.menuItem}>
              {loop(item.children)}
            </TreeNode>
          );
        }
        return <TreeNode key={key} title={title} className={styles.menuItem} />;
      });
    return (
      <div>
        {permissions.length <= 0 && (
          <div className="gutter-left-lg gutter-top">
            <PlusCircleOutlined
              role="button"
              onClick={() => this.add(null, 0)}
            />
          </div>
        )}
        {permissions.length ? (
          <Tree
            draggable={true}
            onExpand={this.handleExpand}
            onDrop={this.handleDrop}
            onSelect={this.handleSelect}
            expandedKeys={expandedKeys}
            selectedKeys={selectedKeys}
            autoExpandParent={autoExpandParent}
          >
            {loop(permissions)}
          </Tree>
        ) : null}
      </div>
    );
  }
}
