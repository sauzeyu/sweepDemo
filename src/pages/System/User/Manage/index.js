'use strict';
import React from 'react';
import { Card } from 'antd';
import FilterForm from './FilterForm';
import { FormattedMessage } from 'umi';
import DataTable from './DataTable';
import SearchForm from '@/pages/System/User/Manage/SearchForm';

export default class Index extends React.Component {
  render() {
    return (
      <div className={'card-group'}>
        <Card>
          <SearchForm />
        </Card>
        <Card>
          <DataTable />
        </Card>
      </div>
    );
  }
}
