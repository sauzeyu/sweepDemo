import React, { Component, useMemo } from 'react';
import EasyTable from '@/components/EasyTable';
import { Card, Icon } from 'antd';
import { MinusSquareOutlined, PlusSquareOutlined } from '@ant-design/icons';

import { selectByParentId, selectForPage } from '@/services/menu';
import { menuFlag, menuType } from '@/constants/user';

const SubTable = (props) => {
  const dataList = React.useRef();
  const columns = [
    {
      title: '菜单名称',
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
  const name = useMemo(() => `menuSubModelsDataTable_${props.id}`, [props.id]);
  return (
    <Card>
      <EasyTable
        columns={columns}
        rowKey={'id'}
        source={selectByParentId}
        name={name}
        autoFetch
        fixedParams={{ parentId: props.id }}
        renderHeader={() => null}
        wrappedComponentRef={() => {
          return dataList;
        }}
        expandable={{
          expandedRowRender: (record) => {
            return <ButtonTable {...record} />;
          },
          expandIcon: ({ expanded, onExpand, record }) => {
            // let flag = menuFlag[record?.id];
            let flag = record.type === 1;
            if (!flag) {
              if (expanded) {
                return (
                  <MinusSquareOutlined onClick={(e) => onExpand(record, e)} />
                );
              } else {
                return (
                  <PlusSquareOutlined onClick={(e) => onExpand(record, e)} />
                );
              }
            }
          },
        }}
      />
    </Card>
  );
};

const ButtonTable = (props) => {
  const dataList = React.useRef();
  const columns = [
    {
      title: '菜单名称',
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
  const name = useMemo(
    () => `menuButtonModelsDataTable_${props.id}`,
    [props.id],
  );

  return (
    <Card>
      <EasyTable
        columns={columns}
        rowKey={'id'}
        source={selectByParentId}
        name={name}
        autoFetch
        fixedParams={{ parentId: props.id }}
        renderHeader={() => null}
        wrappedComponentRef={() => {
          return dataList;
        }}
      />
    </Card>
  );
};

export class DataTable extends Component {
  columns = [
    {
      title: '菜单名称',
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
      <EasyTable
        autoFetch
        source={selectForPage}
        dataProp={'data'}
        name={'resourceDataTable'}
        rowKey={'id'}
        columns={this.columns}
        wrappedComponentRef={(ref) => (this.dataTable = ref)}
        expandable={{
          expandedRowRender: (record) => {
            return <SubTable {...record} />;
          },
          expandIcon: ({ expanded, onExpand, record }) => {
            console.log('record ', record);

            let flag = record.type === 1;
            if (!flag) {
              if (expanded) {
                return (
                  <MinusSquareOutlined onClick={(e) => onExpand(record, e)} />
                );
              } else {
                return (
                  <PlusSquareOutlined onClick={(e) => onExpand(record, e)} />
                );
              }
            }
          },
        }}
      />
    );
  }
}

export default DataTable;
