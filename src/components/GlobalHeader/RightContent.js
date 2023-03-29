import React, { PureComponent } from 'react';
import { FormattedMessage, setLocale, getLocale, getDvaApp } from 'umi';
import { Menu, Dropdown, Avatar, Modal, DatePicker } from 'antd';
import {
  SettingOutlined,
  LogoutOutlined,
  DeleteOutlined,
} from '@ant-design/icons';
import styles from './index.less';
// import router from 'umi/router';
import { history } from 'umi';
import AddForm from '@/pages/System/User/Roles/AddForm';
import EditForm from '@/pages/System/Logs/EditForm';

import { selectForLast } from '@/services/systemLogs';

export default class RightContent extends PureComponent {
  state = {
    configurationExpiredVisible: false,
    validityPeriod: 0,
  };

  handleUserMenuClick = ({ key }) => {
    switch (key) {
      case 'logout':
        getDvaApp()._store.dispatch({
          type: 'user/logout',
        });
        localStorage.removeItem('expiredModalFirst');
        break;
      case 'modifyPassword':
        history.push('/user/changePassword');
        break;
      case 'configurationExpired':
        this.showExportExcelModal();
        // history.push('/user/saveOrUpdateConfigExpired');
        break;
      default:
        break;
    }
  };

  showExportExcelModal = () => {
    selectForLast().then((res) => {
      this.setState({
        validityPeriod: res.data,
        configurationExpiredVisible: true,
      });
    });
  };

  handleExportExcelCancel = () => {
    this.setState({
      configurationExpiredVisible: false,
    });
  };

  handleExportExcelOk = () => {
    console.log('this ', this);
    console.log('this.props ', this.props);
    console.log('this.editForm ', this.editForm);
    // 提交表单
    this.editForm?.submit();
    this.handleExportExcelCancel();
  };

  render() {
    const onFinishFailed = (value) => {
      this.setState({ configurationExpiredVisible: value });
    };
    const { currentUser, theme, isMobile } = this.props;
    let className = styles.right;
    if (theme === 'dark') {
      className = `${styles.right}  ${styles.dark}`;
    }
    if (!currentUser) return null;
    currentUser.avatar.content = currentUser.username;

    const admin = localStorage.getItem('last_username');
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
        {admin === 'admin' ? ( // Use ternary operator for conditional rendering
          <Menu.Item key="configurationExpired">
            <DeleteOutlined />{' '}
            <FormattedMessage
              id={'Component.globalHeader.menu.configurationExpired'}
            />
          </Menu.Item>
        ) : null}
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

        <Modal
          destroyOnClose={true}
          title={'过期日志定时清理'}
          visible={this.state.configurationExpiredVisible}
          onOk={this.handleExportExcelOk}
          onCancel={this.handleExportExcelCancel}
          okText="确认"
          cancelText="取消"
        >
          <EditForm
            editFormRef={(ref) => {
              this.editForm = ref.editForm.current;
            }}
            validityPeriod={this.state.validityPeriod}
            finish={onFinishFailed}
          />
        </Modal>
      </div>
    );
  }
}
