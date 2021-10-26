'use strict';
import React from 'react';
import { Card } from 'antd';
import DataTable from './DataTable';
import SearchForm from './SearchForm';

export default class UserList extends React.Component {
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
