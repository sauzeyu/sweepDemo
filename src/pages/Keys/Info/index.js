import React, { Component } from 'react';
import { Card } from 'antd';
import SearchForm from './SearchForm';
import DataTable from './DataTable';
import Authorized from '@/components/Authorized';
import {
  KEYS_INFO,
  KEYS_INFO_SELECT,
  KEYS_INFO_TABLE,
} from '@/components/Authorized/AuthMap';

/**
 * 汽车类型
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
      <Authorized route={KEYS_INFO}>
        <div className={'card-group'}>
          <Authorized route={KEYS_INFO_TABLE}>
            <Card bordered={false}>
              <SearchForm getFormValues={this.getFormValues} />
            </Card>
            <Card bordered={false}>
              <DataTable searchFormValues={searchFormValues} />
            </Card>
          </Authorized>
        </div>
      </Authorized>
    );
  }
}

export default Index;
