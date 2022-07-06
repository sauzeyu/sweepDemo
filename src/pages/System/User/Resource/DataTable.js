import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal, Tag, Select, notification } from 'antd';

import { selectForPage } from '@/services/menu';
import { menuType } from '@/constants/user';

export class DataTable extends Component {
  state = {};

  columns = [
    {
      title: '菜单名',
      dataIndex: 'title',
    },
    {
      title: 'icon',
      dataIndex: 'icon',
    },
    {
      title: '路由',
      dataIndex: 'href',
    },
    {
      title: '类型',
      dataIndex: 'type',
      render: (text) => {
        return menuType[text];
      },
    },
    {
      title: 'dna',
      dataIndex: 'dna',
    },
  ];
  render() {
    return (
      <>
        <EasyTable
          autoFetch
          source={selectForPage}
          dataProp={'data'}
          name={'resourceDataTable'}
          rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
        />
      </>
    );
  }
}

export default DataTable;
