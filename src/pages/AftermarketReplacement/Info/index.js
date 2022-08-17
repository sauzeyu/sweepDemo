'use strict';
import React from 'react';
import { Card } from 'antd';
import DataTable from './DataTable';
import SearchForm from './SearchForm';
import Authorized from '@/components/Authorized';
import SeesawView from '@/components/SeesawView';
import { AFTERMARKET_REPLACEMENT_INFO } from '@/components/Authorized/AuthMap';
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
