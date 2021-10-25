import React from 'react';
import PropTypes from 'prop-types';
import { Layout } from 'antd';
import DocumentTitle from 'react-document-title';
import { connect } from 'dva';
import { ContainerQuery } from 'react-container-query';
import classNames from 'classnames';
import { enquireScreen } from 'enquire-js';
import GlobalFooter from '../components/GlobalFooter';
import SiderMenu from '../components/SiderMenu';
import { urlToList } from '../utils';
import BaseLayout from './BaseLayout';
import Header from './Header';
// import router from 'umi/router';
import { history } from 'umi';
const { Content, Footer } = Layout;
const query = {
  'screen-xs': {
    maxWidth: 575,
  },
  'screen-sm': {
    minWidth: 576,
    maxWidth: 767,
  },
  'screen-md': {
    minWidth: 768,
    maxWidth: 991,
  },
  'screen-lg': {
    minWidth: 992,
    maxWidth: 1199,
  },
  'screen-xl': {
    minWidth: 1200,
  },
};

let isMobile;
// 获取当前设备
enquireScreen((b) => {
  isMobile = b;
});
//connect的第一个参数是个函数 mapStateToProps
//mapStateToProps 函数允许我们将store中的数据作为props绑定到组件上。
//mapStateToProps 函数的第一个参数就是 Redux 的store
@connect(({ user, global, setting }) => ({
  currentUser: user.currentUser || {},
  collapsed: global.collapsed,
  ...setting,
}))
export default class BasicLayout extends BaseLayout {
  static childContextTypes = {
    location: PropTypes.object,
    breadcrumbNameMap: PropTypes.object,
  };
  state = {
    // 是否为手机
    isMobile,
    currentSys: '',
    redirectData: [],
    title: '数字钥匙管理平台',
  };

  getChildContext() {
    const { location, currentUser } = this.props;
    return {
      location,
      breadcrumbNameMap: currentUser.routeMap,
    };
  }
  // 获取菜单数据部分 将要装载，在render之前调用
  // 可能被废弃
  UNSAFE_componentWillMount() {
    this.updateCurrentSys();
  }
  //（装载完成），在render之后调用
  componentDidMount() {
    enquireScreen((mobile) => {
      this.setState({
        isMobile: mobile,
      });
    });
  }
  /* 当props发生变化时执行，初始化render时不执行，在这个回调函数里面，你可以根据属性的变化，通过调用this.setState()来更新你的组件状态，
    旧的属性还是可以通过this.props来获取,这里调用更新状态是安全的，并不会触发额外的render调用 */
  /**
   * 父组件中改变了props传值时触发的函数
   */
  UNSAFE_componentWillReceiveProps(nextProps) {
    if (nextProps.location.pathname !== this.props.location.pathname) {
      this.updateCurrentSys(nextProps);
    }
  }

  /**
   * 根据菜单取配置获得第一个有效的路由配置并进入.
   */
  autoRedirectToFirstRoute() {
    // 没有登陆用户
    // if (!this.props.currentUser) return null;
    // 获取菜单和路由
    const { menus, routeMap } = this.props.currentUser;
    // 没有子菜单
    if (!menus) return null;
    // 获取pathname 最开始 是 /
    const {
      location: { pathname },
    } = this.props;
    const paths = pathname.split('/').filter((item) => !!item);
    if (menus.length > 0) {
      if (paths.length === 0) {
        const firstSys = menus[0];
        if (firstSys) {
          // 跳转到 /[parts]
          history.replace(firstSys.route);
        }
      } else if (paths.length === 1) {
        // this.state.currentSys ==> /parts
        const menus = routeMap[this.state.currentSys];
        const firstRoute = findFirstChild(menus?.children);
        // 跳转到/parts 的 第一个子路由
        if (firstRoute) history.replace(firstRoute.route);
      }
    }
    function findFirstChild(list) {
      if (!list) return null;
      // 返回第一个菜单项
      for (let i = 0; i < list.length; i++) {
        const menu = list[i];
        if (menu.isMenu) return menu;
        if (menu.children && menu.children.length > 0)
          return findFirstChild(menu.children);
      }
      return null;
    }
  }
  // 更新当前页面
  updateCurrentSys(props) {
    const {
      location: { pathname },
    } = props || this.props;
    // 获取当前url
    const sysPath = urlToList(pathname)[0];
    // 保存的当前路由和获取的实际路由不一致则更新保存的路由 并且根据菜单取配置获得第一个有效的路由配置并进入.
    if (this.state.currentSys !== sysPath) {
      this.setState(
        {
          currentSys: sysPath,
        },
        () => {
          this.autoRedirectToFirstRoute();
        },
      );
    }

    /**
     * 第二个参数是一个回调函数，在setState() 的异步操作结束并且组件已经重新渲染的时候执行。
     * 我们可以通过这个回调来拿到更新的state的值。
     */
  }

  getSiderMenu() {
    const { currentUser } = this.props;
    if (!currentUser || !currentUser.routeMap || !currentUser.menus)
      return null;
    return currentUser.menus;
  }
  handleMenuCollapse = (collapsed) => {
    this.props.dispatch({
      type: 'global/changeLayoutCollapsed',
      payload: collapsed,
    });
  };
  getLayoutStyle = () => {
    const { isMobile } = this.state;
    const { fixSiderbar, collapsed, layout } = this.props;
    if (fixSiderbar && layout !== 'topmenu' && !isMobile) {
      return {
        paddingLeft: collapsed ? '80px' : '256px',
      };
    }
    return null;
  };
  /**
   *
   * @returns
   */
  getContentStyle = () => {
    const { fixedHeader } = this.props;
    return {
      margin: '24px 24px 0',
      paddingTop: fixedHeader ? 64 : 0,
    };
  };
  render() {
    const { collapsed, location, children, navTheme, headerTheme } = this.props;
    const layout = (
      <Layout>
        <SiderMenu
          menuData={this.getSiderMenu()}
          collapsed={collapsed}
          location={location}
          isMobile={this.state.isMobile}
          onCollapse={this.handleMenuCollapse}
          theme={navTheme}
          {...this.props}
        />
        <Layout
          className={'layout-body'}
          style={{
            ...this.getLayoutStyle(),
            minHeight: '100vh',
          }}
        >
          <Header
            handleMenuCollapse={this.handleMenuCollapse}
            isMobile={isMobile}
            theme={headerTheme}
            {...this.props}
          />
          <Content style={this.getContentStyle()}>
            {/* 对应当前地址路由对应的组件 地址改变 children也会改变 */}
            {children}
          </Content>
          <Footer style={{ padding: 0 }}>
            <GlobalFooter />
          </Footer>
        </Layout>
      </Layout>
    );

    return (
      // 调用函数 获取页面标题
      <DocumentTitle title={this.getPageTitle()}>
        {/* 屏幕尺寸: 顶部定义的屏幕划分  */}
        <ContainerQuery query={query}>
          {(params) => <div className={classNames(params)}>{layout}</div>}
        </ContainerQuery>
      </DocumentTitle>
    );
  }
}
