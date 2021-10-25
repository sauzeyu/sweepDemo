import { connect } from 'dva';
import React from 'react';

export default function (getProps, options = {}) {
  // 装饰器的第一个参数就是要装饰的类(组件)
  return function (Component) {
    return createWrapperComponent(getProps, options, Component);
  };
}
/**
 *
 * @param {函数} getProps
 * @param {选项默认为{}} options
 * @param {修饰的类(组件)} Component
 * @returns
 */
function createWrapperComponent(getProps, options, Component) {
  /**
   * easyTableProvider
   * {
   * params:{},
   * page:{},
   * loading:{},
   * errors:{},
   * fixedParams:{},
   * pageProps: {},
   * dataProp: {}
   * }
   */
  @connect(({ easyTableProvider }) => ({
    easyTableProvider,
  }))
  class WrapperComponent extends React.PureComponent {
    render() {
      const extProps = mapProviderProps(this.props, getProps);
      if (
        options.ensureProvider &&
        Object.keys(extProps).some((key) => !extProps[key])
      )
        return null;
      // 根据指定的第一个参数创建一个React元素。
      return React.createElement(Component, {
        ...this.props,
        ...extProps,
      });
    }
  }
  return WrapperComponent;
}

function mapProviderProps(props, getProps) {
  const args = {
    easyTableProvider: props.easyTableProvider,
  };
  if (!getProps) return args;
  Object.keys(props.easyTableProvider.page).map((name) => {
    args[name] = {
      page: props.easyTableProvider.page[name],
      loading: props.easyTableProvider.loading[name],
      params: props.easyTableProvider.params[name],
      fixedParams: props.easyTableProvider.fixedParams[name],
      errors: props.easyTableProvider.errors[name],
      pageProps: props.easyTableProvider.errors[name],
      dataProp: props.easyTableProvider.errors[name],
      fetch(params, pagination) {
        return props.dispatch({
          type: 'easyTableProvider/fetch',
          payload: {
            name,
            params,
            pagination,
          },
        });
      },
      search(params) {
        return props.dispatch({
          type: 'easyTableProvider/search',
          payload: {
            name,
            params,
          },
        });
      },
      paging(pagination) {
        return props.dispatch({
          type: 'easyTableProvider/paging',
          payload: {
            name,
            pagination,
          },
        });
      },
      refresh(pagination) {
        return props.dispatch({
          type: 'easyTableProvider/refresh',
          payload: {
            name,
            pagination,
          },
        });
      },
      reload() {
        return props.dispatch({
          type: 'easyTableProvider/reload',
          payload: {
            name,
          },
        });
      },
      update(callback) {
        return props.dispatch({
          type: 'easyTableProvider/update',
          payload: {
            name,
            data: callback(props.easyTableProvider.page[name].data),
          },
        });
      },
      clean() {
        return props.dispatch({
          type: 'easyTableProvider/clean',
          payload: {
            name,
          },
        });
      },
    };
  });
  return getProps(args);
}
