'use strict';
import React from 'react';
import { Card } from 'antd';
import DataTable from './DataTable';
import SearchForm from './SearchForm';
import Authorized from '@/components/Authorized';
import SeesawView from '@/components/SeesawView';
import {
  AFTERMARKET_REPLACEMENT_INFO,
  AFTERMARKET_REPLACEMENT_INFO_SELECT,
  AFTERMARKET_REPLACEMENT_INFO_TABLE,
} from '@/components/Authorized/AuthMap';
@SeesawView()
export default class Index extends React.Component {
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
      <Authorized route={AFTERMARKET_REPLACEMENT_INFO}>
        <div className={'card-group'}>
          <Authorized route={AFTERMARKET_REPLACEMENT_INFO_SELECT}>
            <Card bordered={false}>
              <SearchForm getFormValues={this.getFormValues} />
            </Card>
          </Authorized>
          <Authorized route={AFTERMARKET_REPLACEMENT_INFO_TABLE}>
            <Card bordered={false}>
              <DataTable searchFormValues={searchFormValues} />
            </Card>
          </Authorized>
        </div>
      </Authorized>
    );
  }
}
