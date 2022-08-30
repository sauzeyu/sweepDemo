import React, { Component } from 'react';
import { Card } from 'antd';
import SearchForm from './SearchForm';
import DataTable from './DataTable';
import Authorized from '@/components/Authorized';
import {
  SYSTEM_USER_RESOURCE,
  SYSTEM_USER_RESOURCE_SELECT,
  SYSTEM_USER_RESOURCE_TABLE,
} from '@/components/Authorized/AuthMap';

export default class extends React.Component {
  render() {
    return (
      <Authorized route={SYSTEM_USER_RESOURCE}>
        <div className={'card-group'}>
          <Authorized route={SYSTEM_USER_RESOURCE_SELECT}>
            <Card bordered={false}>
              <SearchForm />
            </Card>
          </Authorized>
          <Authorized route={SYSTEM_USER_RESOURCE_TABLE}>
            <Card bordered={false}>
              <DataTable />
            </Card>
          </Authorized>
        </div>
      </Authorized>
    );
  }
}
