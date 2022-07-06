import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { message, Modal, Tag, Button, Badge, Space, Row, Col } from 'antd';
import { SyncOutlined } from '@ant-design/icons';
import { keyLifecycleList, keyListById } from '@/services/cars';
import {
  analyzePermissions,
  DKState,
  KeyState,
  KeyType,
  KeySource,
} from '@/constants/keys';
import DescriptionList from '@/components/DescriptionList';

class KeyTable extends Component {
  state = {
    keyLifecycleInfoVisible: false,
    selectedKey: {},
  };
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
    {
      title: '操作',
      fixed: 'right',
      width: 160,
      render: (text, col) => {
        return <a onClick={() => this.keyLifecycle(col)}>生命周期</a>;
      },
    },
  ];
  lifecycleColumns = [
    {
      title: '序号',
      width: 80,
      render: (text, record, index) => {
        let currentIndex = this.keyLifecycleDataTable?.state?.currentIndex;
        let currentPageSize =
          this.keyLifecycleDataTable?.state?.currentPageSize;
        return (currentIndex - 1) * currentPageSize + (index + 1);
      },
    },
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
      title: '操作来源',
      dataIndex: 'keySource',
      width: 160,
      render: (text) => {
        return KeySource[text];
      },
    },
  ];

  keyLifecycle = (col) => {
    this.setState({
      keyLifecycleInfoVisible: true,
      selectedKey: col,
    });
  };
  onCancel = () => {
    this.setState({
      keyLifecycleInfoVisible: false,
      selectedKey: {},
    });
  };

  render() {
    const { selectedVehicleId, selectedVehicleVin } = this.props;
    const { selectedKey } = this.state;
    let selectedKeyId = selectedKey.id;
    let selectedKeyType = selectedKey.parentId;
    return (
      <div>
        <EasyTable
          autoFetch
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
                <p>
                  <Badge color="pink" text="当前车辆 vin 号：" />
                  <Tag color={'gold'} style={{ fontSize: 14 }}>
                    {selectedVehicleVin}
                  </Tag>
                </p>
                <div>
                  <Badge color="blue" text={total} />
                </div>
              </>
            );
          }}
          columns={this.columns}
          fixedParams={{ vehicleId: selectedVehicleId }}
        />

        <Modal
          footer={null}
          title={'钥匙生命周期'}
          visible={this.state.keyLifecycleInfoVisible}
          onCancel={this.onCancel}
          destroyOnClose={true}
          // width={800}
        >
          <EasyTable
            autoFetch
            source={keyLifecycleList}
            dataProp={'data'}
            name={'keyLifecycleDataTable'}
            rowKey={'id'}
            columns={this.lifecycleColumns}
            wrappedComponentRef={(ref) => (this.keyLifecycleDataTable = ref)}
            renderHeader={(title, extra, page) => {
              return (
                <>
                  <p>
                    <Badge color="pink" text="车辆 vin 号：" />
                    <Tag color={'gold'}>{selectedVehicleVin}</Tag>
                    <Badge color="purple" text="钥匙类型：" />
                    {KeyType(selectedKeyType)}
                  </p>
                  {/*<div><Badge color="blue" text={total}/></div>*/}
                </>
              );
            }}
            fixedParams={{ keyId: selectedKeyId }}
          />
        </Modal>
      </div>
    );
  }
}

export default KeyTable;
