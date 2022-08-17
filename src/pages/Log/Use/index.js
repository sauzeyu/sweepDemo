import React, { Component } from 'react';
import { Card } from 'antd';
import SearchForm from './SearchForm';
import DataTable from './DataTable';

import Authorized from '@/components/Authorized';
import { LOG_USE } from '@/components/Authorized/AuthMap';

/**
 * 钥匙故障记录
 */
class Index extends Component {
  getFormValues = (values) => {
    this.setState({
      searchFormValues: values,
    });
  };
  state = {
    searchFormValues: {},
  };
  render() {
    const { searchFormValues } = this.state;
    return (
      <Authorized route={LOG_USE}>
        <div className={'card-group'}>
          <Card bordered={false}>
            <SearchForm getFormValues={this.getFormValues} />
          </Card>
          <Card bordered={false}>
            <DataTable searchFormValues={searchFormValues} />
          </Card>
        </div>
      </Authorized>
    );
  }
}

export default Index;
