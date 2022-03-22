import React from 'react';
import { Drawer } from 'antd';
import SiderMenu from './SiderMenu';
import { getFlatMenuKeys } from './BaseMenu';

const SiderMenuWrapper = (props) => {
  /**
   * menuData: 菜单数据
   *
   * */
  const { isMobile, menuData, collapsed } = props;
  return isMobile ? (
    <Drawer
      visible={!collapsed}
      placement="left"
      onClose={() => {
        props.onCollapse(true);
      }}
      style={{
        padding: 0,
        height: '100vh',
      }}
    >
      <SiderMenu
        {...props}
        flatMenuKeys={getFlatMenuKeys(menuData)}
        collapsed={isMobile ? false : collapsed}
      />
    </Drawer>
  ) : (
    /* 将父组件传递的全部属性都传递给子组件 并 新增属性flatMenuKeys */
    <SiderMenu {...props} flatMenuKeys={getFlatMenuKeys(menuData)} />
  );
};

export default SiderMenuWrapper;
