import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Table, Card } from 'antd';
import { UpOutlined, DownOutlined } from '@ant-design/icons';
import { keyLifecycleList, keyListById } from '@/services/cars';
import { DKState, KeyState, KeyType, KeySource } from '@/constants/keys';
import { useMemo } from 'react';

const SubTable = (props) => {
  const dataList = React.useRef();
  const columns = [
    {
      title: '操作时间',
      dataIndex: 'createTime',
      width: 350,
    },
    {
      title: '操作状态',
      dataIndex: 'keyStatus',
      width: 160,
      render: (text) => {
        return KeyState[text];
      },
    },
    {
      title: '吊销来源',
      dataIndex: 'keySource',
      width: 160,
      render: (text) => {
        return KeySource[text];
      },
    },
  ];
  const name = useMemo(() => `carSubModelsDataTable_${props.id}`, [props.id]);
  return (
    <Card>
      <EasyTable
        columns={columns}
        rowKey={'id'}
        source={keyLifecycleList}
        name={name}
        autoFetch
        fixedParams={{ keyId: props.id }}
        renderHeader={() => null}
        wrappedComponentRef={() => {
          return dataList;
        }}
      />
    </Card>
  );
};

class KeyTable extends Component {
  columns = [
    {
      title: '序号',
      width: 80,
      render: (text, record, index) => {
        let currentIndex = this.carsKeyListDataTable?.state?.currentIndex;
        let currentPageSize = this.carsKeyListDataTable?.state?.currentPageSize;
        return (currentIndex - 1) * currentPageSize + (index + 1);
      },
    },
    {
      title: '用户id',
      dataIndex: 'userId',
      width: 200,
    },
    {
      title: '钥匙类型',
      dataIndex: 'parentId',
      width: 160,
      render: (text) => {
        return KeyType(text);
      },
    },
    {
      title: '具体状态',
      dataIndex: 'dkState',
      width: 200,
      render: (text) => {
        return DKState[text];
      },
    },
    {
      title: '生效时间',
      dataIndex: 'valFrom',
      width: 200,
    },
    {
      title: '失效时间',
      dataIndex: 'valTo',
      width: 200,
    },
  ];

  render() {
    const { selectedVehicleId, selectedVehicleVin } = this.props;
    return (
      <div>
        <EasyTable
          autoFetch
          columnWidth={200}
          source={keyListById}
          dataProp={'data'}
          name={'carsKeyListDataTable'}
          rowKey={'id'}
          wrappedComponentRef={(ref) => (this.carsKeyListDataTable = ref)}
          renderHeader={(title, extra, page) => {
            let total = page.total;
            total = '共 ' + total + ' 条记录';
            return (
              <>
                <p>当前车辆 vin 号：{selectedVehicleVin}</p>
                <p>{total}</p>
              </>
            );
          }}
          columns={this.columns}
          fixedParams={{ vehicleId: selectedVehicleId }}
          expandIconAsCell={false}
          expandIconColumnIndex={6}
          expandable={{
            expandedRowRender: (record) => {
              return <SubTable {...record} />;
            },
            expandIcon: (props) => {
              if (props.expanded) {
                return (
                  <a
                    onClick={(e) => {
                      props.onExpand(props.record, e);
                    }}
                  >
                    生命周期 <UpOutlined />
                  </a>
                );
              } else {
                return (
                  <a
                    onClick={(e) => {
                      props.onExpand(props.record, e);
                    }}
                  >
                    生命周期 <DownOutlined />
                  </a>
                );
              }
            },
          }}
        />
      </div>
    );
  }
}

export default KeyTable;
