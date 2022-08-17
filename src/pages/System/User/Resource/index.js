import React, { Component } from 'react';
import { Card } from 'antd';
import SearchForm from './SearchForm';
import DataTable from './DataTable';
import Authorized from '@/components/Authorized';
import { SYSTEM_USER_RESOURCE } from '@/components/Authorized/AuthMap';

export default class extends React.Component {
  render() {
    return (
      <Authorized route={SYSTEM_USER_RESOURCE}>
        <div className={'card-group'}>
          <Card bordered={false}>
            <SearchForm />
          </Card>
          <Card bordered={false}>
            <DataTable />
          </Card>
        </div>
      </Authorized>
    );
  }
}
