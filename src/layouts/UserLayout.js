import React from 'react';
import DocumentTitle from 'react-document-title';
import GlobalFooter from '../components/GlobalFooter';
import Icon from '@ant-design/icons';
import styles from './UserLayout.less';
import { getPublicPath } from '../utils';
import classnames from 'classnames';
import BaseLayout from './BaseLayout';
import { Card } from 'antd';
export default class UserLayout extends BaseLayout {
  render() {
    const { children } = this.props;
    return (
      <DocumentTitle title={this.getPageTitle()}>
        <div className={classnames(styles.container, 'layout-body')}>
          <div className={styles.content}>
            <Card bordered={false} className={styles.contentCard}>
              <div className={styles.top}>
                <div className={styles.header}>
                  <img
                    alt="logo"
                    className={styles.logo}
                    src={getPublicPath('img/logo.svg')}
                  />
                  <div className={styles.sysName}>数字钥匙管理平台</div>
                </div>
              </div>
              {children}
            </Card>
          </div>
          <GlobalFooter className={styles.footer} />
        </div>
      </DocumentTitle>
    );
  }
}
