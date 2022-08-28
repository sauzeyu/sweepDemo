import React, { PureComponent } from 'react';
import { FormattedMessage, setLocale, getLocale, getDvaApp } from 'umi';
import { Menu, Dropdown, Avatar } from 'antd';
import { SettingOutlined, LogoutOutlined } from '@ant-design/icons';
import styles from './index.less';
// import router from 'umi/router';
import { history } from 'umi';

export default class RightContent extends PureComponent {
  handleUserMenuClick = ({ key }) => {
    switch (key) {
      case 'logout':
        getDvaApp()._store.dispatch({
          type: 'user/logout',
        });
        break;
      case 'modifyPassword':
        history.push('/user/changePassword');
        break;
    }
  };
  render() {
    const { currentUser, theme, isMobile } = this.props;
    let className = styles.right;
    if (theme === 'dark') {
      className = `${styles.right}  ${styles.dark}`;
    }
    if (!currentUser) return null;
    currentUser.avatar.content = currentUser.username;
    const menu = (
      <Menu
        theme={theme}
        className={styles.menu}
        selectedKeys={[]}
        onClick={this.handleUserMenuClick}
      >
        <Menu.Item key="modifyPassword">
          <SettingOutlined />{' '}
          <FormattedMessage id={'Component.globalHeader.menu.changePassword'} />
        </Menu.Item>
        <Menu.Item key="logout">
          <LogoutOutlined />{' '}
          <FormattedMessage id={'Component.globalHeader.menu.logout'} />
        </Menu.Item>
      </Menu>
    );
    return (
      <div className={className}>
        <Dropdown overlay={menu}>
          <div className={`${styles.action} ${styles.account}`}>
            <Avatar
              size={isMobile ? 'small' : 'large'}
              title={currentUser.username}
              className={styles.avatar}
              style={{
                backgroundColor: currentUser.avatar.bgColor,
                color: currentUser.avatar.color,
              }}
              src={currentUser.avatar.src}
              alt={currentUser.username}
            >
              {currentUser.avatar.content}
            </Avatar>
            {/*<span className={styles.name}>{currentUser.real_name}</span>*/}
          </div>
        </Dropdown>
      </div>
    );
  }
}
