'use strict';
import React from 'react';

import { Card } from 'antd';
import DataTable from './DataTable';
import SearchForm from './SearchForm';

import Authorized from '@/components/Authorized';
import { SYSTEM_USER_ROLES } from '@/components/Authorized/AuthMap';

export default class Index extends React.Component {
  render() {
    return (
      <Authorized route={SYSTEM_USER_ROLES}>
        <div className={'card-group'}>
          <Card>
            <SearchForm />
          </Card>
          <Card>
            <DataTable />
          </Card>
        </div>
      </Authorized>
    );
  }
}
