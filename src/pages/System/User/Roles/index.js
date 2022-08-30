'use strict';
import React from 'react';

import { Card } from 'antd';
import DataTable from './DataTable';
import SearchForm from './SearchForm';

import Authorized from '@/components/Authorized';
import {
  SYSTEM_USER_ROLES,
  SYSTEM_USER_ROLES_SELECT,
  SYSTEM_USER_ROLES_TABLE,
} from '@/components/Authorized/AuthMap';

export default class Index extends React.Component {
  render() {
    return (
      <Authorized route={SYSTEM_USER_ROLES}>
        <div className={'card-group'}>
          <Authorized route={SYSTEM_USER_ROLES_SELECT}>
            <Card>
              <SearchForm />
            </Card>
          </Authorized>
          <Authorized route={SYSTEM_USER_ROLES_TABLE}>
            <Card>
              <DataTable />
            </Card>
          </Authorized>
        </div>
      </Authorized>
    );
  }
}
