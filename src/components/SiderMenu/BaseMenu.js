import React, { PureComponent } from 'react';
import { Menu } from 'antd';
import * as Icons from '@ant-design/icons';
import { Link } from 'umi';
import { FormattedMessage, getLocale } from 'umi';
import pathToRegexp from 'path-to-regexp';
import { urlToList } from '@/utils';
import styles from './index.less';

const { SubMenu } = Menu;

const getIcon = (icon) => {
  if (!icon) return null;
  if (typeof icon === 'string' && icon.indexOf('http') === 0) {
    return (
      <img
        src={icon}
        alt="icon"
        className={`${styles.icon} sider-menu-item-img`}
      />
    );
  }
  if (typeof icon === 'string') {
    icon = icon.replace(/-([a-zA-Z])/g, ($, $1) => {
      return $1.toUpperCase();
    });
    icon = icon[0].toUpperCase() + icon.slice(1);
    return React.createElement(Icons[icon + 'Outlined']);
  }
  return null;
};
// 获取含有当前路径的所有路由 例如 当前路径 /parts/type 则 获取 ["/parts/type/insert", "/parts/type/del", "/parts/type/update", "/parts/type/uri", "/parts/type"]
export const getMenuMatches = (flatMenuKeys, path) =>
  flatMenuKeys.filter((item) => item && pathToRegexp(item).test(path));

/**
 * Recursively flatten the data
 * [{path:string},{path:string}] => [path,path2]
 * ["/parts/type/insert", "/parts/type/del", "/parts/type/update", "/parts/type/uri",...]
 * @param  menuData
 */
export const getFlatMenuKeys = (menuData) => {
  let keys = [];
  if (!menuData) return keys;
  menuData.forEach((item) => {
    if (item.children) {
      keys = keys.concat(getFlatMenuKeys(item.children));
    }
    keys.push(item.route);
  });
  return keys;
};

export default class BaseMenu extends PureComponent {
  constructor(props) {
    super(props);
  }

  /**
   * 获得菜单子节点
   * @memberof SiderMenu
   */
  getNavMenuItems = (menusData) => {
    if (!menusData) {
      return [];
    }
    return menusData
      .filter((item) => item.isMenu || item.isDir)
      .map((item) => {
        return this.getSubMenuOrItem(item);
      });
  };

  // Get the currently selected menu
  getSelectedMenuKeys = () => {
    const {
      location: { pathname },
    } = this.props;
    return urlToList(pathname).map((itemPath) => {
      return getMenuMatches(this.props.flatMenuKeys, itemPath).pop();
    });
  };

  /**
   * get SubMenu or Item
   */
  getSubMenuOrItem = (item, isRoot) => {
    if (item.isDir) {
      const childrenItems = this.getNavMenuItems(item.children);
      // 当无子菜单时就不展示菜单
      if (childrenItems && childrenItems.length > 0) {
        return (
          <SubMenu
            title={
              /* 子菜单项值 */
              item.icon ? (
                <label>
                  {getIcon(item.icon)}
                  <span>{item.title}</span>
                </label>
              ) : (
                <span>{item.title}</span>
              )
            }
            key={item.route} /* 唯一标识 */
          >
            {childrenItems}
          </SubMenu>
        );
      }
      return null;
    } else {
      return (
        <Menu.Item key={item.route}>{this.getMenuItemPath(item)}</Menu.Item>
      );
    }
  };

  /**
   * 判断是否是http链接.返回 Link 或 a
   * Judge whether it is http link.return a or Link
   * 基本没有http链接
   * @memberof SiderMenu
   */
  getMenuItemPath = (item) => {
    const itemPath = this.conversionPath(item.route);
    const icon = getIcon(item.icon);
    const { target, title } = item;
    // Is it a http link
    if (/^https?:\/\//.test(itemPath)) {
      return (
        <a href={itemPath} target={target}>
          {icon}
          <span>{title}</span>
        </a>
      );
    }
    const { location, isMobile, onCollapse } = this.props;
    return (
      <Link
        to={itemPath}
        target={target}
        replace={itemPath === location.pathname}
        onClick={
          isMobile
            ? () => {
                onCollapse(true);
              }
            : undefined
        }
      >
        {icon}
        <span>{title}</span>
      </Link>
    );
  };

  conversionPath = (path) => {
    if (path && path.indexOf('http') === 0) {
      return path;
    } else {
      return `/${path || ''}`.replace(/\/+/g, '/');
    }
  };

  render() {
    const { openKeys, theme, mode } = this.props;
    // if pathname can't match, use the nearest parent's key
    let selectedKeys = this.getSelectedMenuKeys();
    if (!selectedKeys.length && openKeys) {
      selectedKeys = [openKeys[openKeys.length - 1]];
    }
    let props = {};
    if (openKeys) {
      props = {
        openKeys,
      };
    }
    const { handleOpenChange, style, menuData } = this.props;
    return (
      <Menu
        key="Menu"
        mode={
          mode
        } /* 菜单类型，现在支持垂直、水平、和内嵌模式三种 这里是 inline 内嵌 */
        theme={theme} /* 主题颜色 */
        onOpenChange={handleOpenChange} /* SubMenu 展开/关闭的回调 */
        selectedKeys={selectedKeys} /* 当前选中的菜单项 key 数组 */
        style={style} /* 根节点样式 */
        {...props}
      >
        {this.getNavMenuItems(menuData)}
      </Menu>
    );
  }
}
