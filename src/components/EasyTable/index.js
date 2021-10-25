'use strict';
import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { Alert, Button, Spin, Table } from 'antd';
import { connect } from 'dva';
import './provider';
import connector from './connector';
import { FormattedMessage } from 'umi';
import './style.less';
import diff from 'deep-diff';

@connect(({ easyTableProvider }) => ({
  easyTableProvider,
}))
class EasyTable extends React.Component {
  // 设置prop的数据约束
  static propTypes = {
    source: PropTypes.oneOfType([PropTypes.func, PropTypes.string]).isRequired, // 数据源
    name: PropTypes.string.isRequired, // Table的名称，provider数据池识别的键,必须唯一。
    autoFetch: PropTypes.bool, // 是否在初始化后自动加载数据
    keepData: PropTypes.bool, // 持久保存Redux数据
    fixedParams: PropTypes.object, // 请求固定携带的附加参数
    renderHeader: PropTypes.func, // 顶部渲染回调
    onDataLoaded: PropTypes.func, // 数据加载成功后回调
    onError: PropTypes.func, // 发生错误时回调
    onChange: PropTypes.func, // Table发生变化时回调
    before: PropTypes.any, // 表格前面的内容
    after: PropTypes.any, // 表格后面的内容
    wrappedComponentRef: PropTypes.func,
    rowKey: PropTypes.oneOfType([PropTypes.string, PropTypes.func]).isRequired,
    columns: PropTypes.array,
    pageProps: PropTypes.object, // Page的参数属性
    dataProp: PropTypes.string, // data取值的属性
  };
  // 默认值设置
  static defaultProps = {
    autoFetch: false,
    keepData: false,
    renderHeader: (title, extra) => {
      return (
        <div className={'comp-easytable_header'}>
          <div className={'comp-easytable_header_title'}>{title}</div>
          <div className={'comp-easytable_header_extra'}>{extra}</div>
        </div>
      );
    },
    onDataLoaded() {},
    onError() {},
  };
  // 组件增强 在头部search中使用过
  static connect = connector;
  constructor(props) {
    // easyTableProvider空间 内 调用数据池初始化
    props.dispatch({
      type: 'easyTableProvider/_initialize',
      payload: {
        name: props.name,
        source: props.source,
        fixedParams: props.fixedParams,
        onDataLoaded: props.onDataLoaded,
        pageProps: props.pageProps,
        dataProp: props.dataProp,
        onError: props.onError,
      },
    });
    if (typeof props.name !== 'string') {
      throw new ReferenceError(
        'Argument [name] require string,But got a ' + typeof props.name,
      );
    }
    super(props);
  }
  componentDidMount() {
    const {
      easyTableProvider: { page: dataPage },
      name,
      keepData,
      autoFetch,
      wrappedComponentRef,
    } = this.props;
    if (
      autoFetch &&
      !(keepData && dataPage[name] && dataPage[name].total > 0)
    ) {
      this.fetch();
    }
    if (wrappedComponentRef) {
      wrappedComponentRef(this);
    }
  }
  componentWillUnmount() {
    if (!this.props.keepData) {
      this.clean();
    }
  }
  UNSAFE_componentWillReceiveProps(nextProps, nextContext) {
    if (this.props.name !== nextProps.name) {
      throw new Error(
        'The name of the EasyTable cannot be changed，You can switch between multiple tables',
      );
    }
    const changedProps = [];
    ['source', 'onDataLoaded', 'onError'].map((key) => {
      if (this.props[key] !== nextProps[key]) {
        changedProps.push(key);
      }
    });
    ['fixedParams'].map((key) => {
      if (diff(this.props[key], nextProps[key])) {
        changedProps.push(key);
      }
    });
    if (changedProps.length > 0) {
      let changeValue = {};
      changedProps.map((key) => {
        if (key in nextProps) {
          changeValue[key] = nextProps[key];
        }
      });
      this.props.dispatch({
        type: 'easyTableProvider/_update',
        payload: {
          name: this.props.name,
          ...changeValue,
        },
      });
    }
  }
  //
  fetch = (params, pagination) => {
    return this._dispatch('easyTableProvider/fetch', {
      params,
      pagination,
    });
  };
  refresh = (pagination) => {
    return this._dispatch('easyTableProvider/refresh', { pagination });
  };
  reload = () => {
    return this._dispatch('easyTableProvider/reload');
  };
  paging = (pagination) => {
    return this._dispatch('easyTableProvider/paging', { pagination });
  };
  search = (params) => {
    return this._dispatch('easyTableProvider/search', { params });
  };
  getProviderState = () => {
    const { easyTableProvider, name } = this.props;
    return {
      errors: easyTableProvider.errors[name],
      fixedParams: easyTableProvider.fixedParams[name],
      params: easyTableProvider.params[name],
      loading: easyTableProvider.loading[name],
      page: easyTableProvider.page[name],
      pageProps: easyTableProvider.pageProps[name],
      dataProp: easyTableProvider.dataProp[name],
    };
  };
  clean() {
    this._dispatch('easyTableProvider/clean', {});
  }
  /**
   *
   * @param {} action action名称
   * @param {} params 请求携带的参数
   * @returns
   */
  _dispatch(action, params) {
    return this.props.dispatch({
      type: action,
      payload: {
        name: this.props.name,
        ...params,
      },
    });
  }
  handleChange = (pagination) => {
    this.paging(pagination);
    this.props.onChange && this.props.onChange(pagination);
  };
  render() {
    const {
      easyTableProvider: { page: dataPage, loading, errors },
      name,
    } = this.props;
    let page = dataPage[name] || {},
      busy = loading[name] || false,
      error = errors[name];
    let {
      title = (page) => (
        <FormattedMessage
          id={'Common.pagination.total'}
          values={{ total: page.total }}
        />
      ),
      extra,
      className,
      style,
      renderHeader,
      before,
      after,
      ...restProps
    } = this.props;
    if (title === false) title = null;
    if (typeof title === 'function') title = title(page);
    return (
      <div className={className} style={style}>
        <Spin spinning={busy}>
          {renderHeader(title, extra, page)}
          {error ? (
            <Alert
              message={error.message}
              type="error"
              description={
                <div className={'text-center gutter-top'}>
                  <Button onClick={this.refresh}>
                    <FormattedMessage id={'Common.button.retry'} />
                  </Button>
                </div>
              }
            />
          ) : (
            <Fragment>
              {before != null && (
                <div className={'comp-easytable_before'}>{before}</div>
              )}
              <Table
                {...restProps}
                pagination={page}
                dataSource={page.data}
                onChange={this.handleChange}
              />
              {after != null && (
                <div className={'comp-easytable_after'}>{after}</div>
              )}
            </Fragment>
          )}
        </Spin>
      </div>
    );
  }
}

export default EasyTable;
