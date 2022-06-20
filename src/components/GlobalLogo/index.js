'use strict';
import React from 'react';
import styles from './index.less';
import { Link } from 'umi';
import { getPublicPath } from '@/utils';
import classNames from 'classnames';
/**
 * PureComponent的原理是继承了Component类，自动加载shouldComponentUpdate函数，
 * 当组件更新时，shouldComponentUpdate对props和state进行了一层浅比较，
 * 如果组件的props和state都没有发生改变，render方法就不会触发，省去Virtual DOM的生成和对比过程，达到提升性能的目的。
 */
/**
 * logo组件
 */
export default class extends React.PureComponent {
  render() {
    const { theme, className, style } = this.props;
    return (
      <div
        className={classNames(
          styles.logo,
          {
            [styles.light]: theme === 'light',
          },
          className,
        )}
        style={style}
      >
        <Link to="/">
          <img
            src={getPublicPath('img/logo.svg')}
            className={styles.logoIcon}
          />
          {/*<img className={styles.logoName} src={getPublicPath('img/app-logo_name_light.png')}/>*/}
          {/*<div className={styles.logoText}>数字钥匙管理平台</div>*/}
        </Link>
      </div>
    );
  }
}
