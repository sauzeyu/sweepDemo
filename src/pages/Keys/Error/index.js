import React, { Component } from 'react';
import { Card } from 'antd';
import SearchForm from './SearchForm';
import DataTable from './DataTable';
/**
 * 钥匙故障记录
 */
class Index extends Component {
  render() {
    return (
      <div className={'card-group'}>
        <Card bordered={false}>
          <SearchForm />
        </Card>
        <Card bordered={false}>
          <DataTable />
        </Card>
      </div>
    );
  }
}

export default Index;
