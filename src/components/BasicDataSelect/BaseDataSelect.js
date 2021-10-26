'use strict';
import React from 'react';
import AssociativeSearch from '../AssociativeSearch';
import PropTypes from 'prop-types';
import { Alert } from 'antd';
import { removeEmptyProperty } from '@/utils';
import diff from 'deep-diff';

function createPage() {
  return { pageIndex: 1, pageSize: 20, total: Infinity, data: [] };
}
export default class extends React.Component {
  state = {
    error: null,
    filter: undefined,
    pagination: createPage(),
  };
  static propTypes = {
    action: PropTypes.func.isRequired,
  };
  UNSAFE_componentWillReceiveProps(nextProps) {
    if (diff(this.props.params, nextProps.params)) {
      setTimeout(() => {
        this.handleFetch(this.state.filter, createPage());
      });
    }
  }
  hasFoundItems = false;
  lastData = [];
  handleFetch = (filter, pagination) => {
    const {
      action,
      labelKey,
      searchKey = labelKey,
      params,
      showAll,
    } = this.props;
    if (filter !== this.state.filter) {
      pagination = createPage();
    }
    const hasPage = !!pagination;
    if (!hasPage) pagination = this.state.pagination;
    if (showAll) pagination.pageSize = 999;
    this.setState({ error: null });
    return action(
      removeEmptyProperty({
        pageIndex: pagination.pageIndex,
        pageSize: pagination.pageSize,
        ...params,
        [searchKey]: filter,
      }),
    ).then(
      (res) => {
        pagination.total = res.total;
        const resData = res[this.props.dataPropName] || [];
        pagination.data = hasPage ? pagination.data.concat(resData) : resData;
        this.lastData = this.state.pagination;
        this.hasFoundItems = pagination.data.length > 0;
        this.setState({ pagination, filter });
        return pagination.data;
      },
      (err) => {
        this.setState({ error: err.message });
      },
    );
  };
  handleScrollBottom = () => {
    const { pagination } = this.state;
    const hasNext =
      pagination.total / pagination.pageSize > pagination.pageIndex;
    if (!hasNext) return null;
    pagination.pageIndex++;
    return this.handleFetch(this.state.filter, pagination);
  };
  // handleBlur=(evt)=>{
  //     if(!this.hasFoundItems)this.setState({pagination:this.lastData});
  //     if(this.props.onBlur)this.props.onBlur(evt);
  // };
  render() {
    if (this.state.error)
      return <Alert type={'error'} showIcon message={this.state.error} />;
    const {
      fetchOnSearch = true,
      fetchOnMount = true,
      ...restProps
    } = this.props;
    return (
      <AssociativeSearch
        {...restProps}
        fetchOnMount={fetchOnMount}
        fetchOnSearch={fetchOnSearch}
        onFetch={this.handleFetch}
        data={this.state.pagination.data}
        // onBlur={this.handleBlur}
        onScrollBottom={this.handleScrollBottom}
      />
    );
  }
}
