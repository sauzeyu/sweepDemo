import React, { PureComponent } from 'react';
import { Col, Row } from 'antd';
import { MenuUnfoldOutlined, MenuFoldOutlined } from '@ant-design/icons';
import { Link } from 'umi';
import Debounce from 'lodash-decorators/debounce';
import styles from './index.less';
import RightContent from './RightContent';
import { getPublicPath } from '@/utils';
const logo = getPublicPath('img/logo.svg');

export default class GlobalHeader extends PureComponent {
  componentWillUnmount() {
    this.triggerResizeEvent.cancel();
  }

  /* eslint-disable*/
  @Debounce(600)
  triggerResizeEvent() {
    // eslint-disable-line
    const event = document.createEvent('HTMLEvents');
    event.initEvent('resize', true, false);
    window.dispatchEvent(event);
  }

  toggle = () => {
    const { collapsed, onCollapse } = this.props;
    onCollapse(!collapsed);
    this.triggerResizeEvent();
  };

  render() {
    const {
      collapsed,
      collapsable = true,
      isMobile,
      currentUser,
      onSysMenuClick,
      theme,
    } = this.props;
    return (
      <div className={`${styles.header} ${styles[theme]}`}>
        <Row type={'flex'} style={{ height: '100%' }}>
          <Col span={2} className={styles.headerLeft}>
            {isMobile && (
              <Link to="/" className={styles.logo} key="logo">
                <img src={logo} alt="logo" width="32" />
              </Link>
            )}
            {collapsable && (
              <div className={styles.trigger} onClick={this.toggle}>
                {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
              </div>
            )}
          </Col>
          <Col span={18} className={styles.headerCenter} />
          <Col span={4} offset={4} className={styles.headerRight}>
            <RightContent {...this.props} />
          </Col>
        </Row>
      </div>
    );
  }
}
