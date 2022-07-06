import React, { PureComponent } from 'react';
import { Layout } from 'antd';
import pathToRegexp from 'path-to-regexp';
import classNames from 'classnames';
import styles from './index.less';
import BaseMenu, { getMenuMatches } from './BaseMenu';
import { urlToList } from '@/utils';
import GlobalLogo from '@/components/GlobalLogo';

const { Sider } = Layout;

/**
 * 获得菜单子节点
 * @memberof SiderMenu
 */
const getDefaultCollapsedSubMenus = (props) => {
  // 获取当前 路径 和 扁平化的菜单key
  const {
    location: { pathname },
    flatMenuKeys,
  } = props;
  return urlToList(pathname)
    .map((item) => getMenuMatches(flatMenuKeys, item)[0])
    .filter((item) => item);
};

export default class SiderMenu extends PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      // ["/parts", "/parts/type"]
      openKeys: getDefaultCollapsedSubMenus(props),
    };
  }

  static getDerivedStateFromProps(props, state) {
    const { pathname } = state;
    if (props.location.pathname !== pathname) {
      return {
        pathname: props.location.pathname,
        openKeys: getDefaultCollapsedSubMenus(props),
      };
    }
    return null;
  }

  isMainMenu = (key) => {
    const { menuData } = this.props;
    return menuData.some((item) => {
      if (key) {
        return item.key === key || item.path === key;
      }
      return false;
    });
  };
  // SubMenu 展开/关闭的回调
  handleOpenChange = (openKeys) => {
    const moreThanOne =
      openKeys.filter((openKey) => this.isMainMenu(openKey)).length > 1;
    this.setState({
      openKeys: moreThanOne ? [openKeys.pop()] : [...openKeys],
    });
  };

  render() {
    const { collapsed, onCollapse, fixSiderbar, theme } = this.props;
    const { openKeys } = this.state;
    // ["/parts", "/parts/type"]
    const defaultProps = collapsed ? {} : { openKeys };
    // 类名
    const siderClassName = classNames(styles.sider, {
      [styles.fixSiderbar]: fixSiderbar,
      [styles.light]: theme === 'light',
    });

    return (
      // ant layout 布局的固定侧边栏
      <Sider
        trigger={null} /* 自定义 trigger 设置为null时隐藏trigger */
        collapsible /* 是否可收起 */
        collapsed={collapsed} /* 当前收起状态 */
        breakpoint="lg" /* 触发响应式布局的断点 */
        onCollapse={onCollapse} /* 收起展开的回调函数 */
        width={208}
        theme={theme}
        className={siderClassName}
      >
        <GlobalLogo {...this.props} />
        <BaseMenu
          {...this.props}
          mode="inline"
          handleOpenChange={this.handleOpenChange}
          onOpenChange={this.handleOpenChange}
          style={{ padding: '16px 0', width: '100%' }}
          {...defaultProps}
        />
      </Sider>
    );
  }
}
