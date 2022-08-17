'use strict';
import React from 'react';

import { Card } from 'antd';
import FilterForm from './FilterForm';
import { FormattedMessage } from 'umi';
import DataTable from './DataTable';
import SearchForm from '@/pages/System/User/Manage/SearchForm';
import Authorized from '@/components/Authorized';
import { SYSTEM_USER_MANAGE } from '@/components/Authorized/AuthMap';

export default class Index extends React.Component {
  render() {
    return (
      <Authorized route={SYSTEM_USER_MANAGE}>
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
