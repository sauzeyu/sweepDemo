import React, { Component } from 'react';
import { Card } from 'antd';
import SearchForm from './SearchForm';
import DataTable from './DataTable';

import Authorized from '@/components/Authorized';
import {
  LOG_ERROR,
  LOG_USE,
  LOG_USE_SELECT,
  LOG_USE_TABLE,
} from '@/components/Authorized/AuthMap';

/**
 * 诊断日志记录
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
      <Authorized route={LOG_ERROR}>
        <div className={'card-group'}>
          {/*<Authorized route={LOG_USE_TABLE}>*/}
          <Card bordered={false}>
            <SearchForm getFormValues={this.getFormValues} />
          </Card>
          <Card bordered={false}>
            <DataTable searchFormValues={searchFormValues} />
          </Card>
          {/*</Authorized>*/}
        </div>
      </Authorized>
    );
  }
}

export default Index;
