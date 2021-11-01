import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import { getKeysList } from '@/services/keys';
import { Link } from 'umi';
import { DKState, KeysState } from '@/constants/keys';
@connect(({ keysManage, loading }) => ({
  keysManage,
  // upserting: loading.effects['keysManage/upsert'],
}))
class DataTable extends Component {
  state = {};
  columns = [
    {
      title: '手机设备指纹',
      dataIndex: 'phone',
    },
    {
      title: '数字钥匙ID',
      dataIndex: 'keyId',
    },
    {
      title: '电话',
      dataIndex: 'phone',
    },
    {
      title: '具体状态',
      dataIndex: 'dkState',
      render: (text) => {
        return DKState[text];
      },
    },
    {
      title: '生效时间',
      dataIndex: 'valFrom',
    },
    {
      title: '失效时间',
      dataIndex: 'valTo',
    },
    {
      title: '授权权限值',
      dataIndex: 'permit',
    },
    {
      title: '附加信息',
      dataIndex: 'serverPersonal',
    },
    {
      title: '申请时间',
      dataIndex: 'applyTime',
    },
    {
      title: '操作状态',
      dataIndex: 'status',
      render: (text) => {
        return KeysState[text];
      },
    },
    {
      title: '预配对值',
      dataIndex: 'phone',
    },
    {
      title: '操作',
      fixed: 'right',
      width: 300,
      render: (col) => {
        return col.dkState != 5 ? (
          <div className={'link-group'}>
            <a onClick={() => this.userInfo(col)}>查看用户</a>
            <a onClick={() => this.carInfo(col)}>查看车辆</a>
            {col.dkState == 3 && (
              <a onClick={() => this.enableKey(col, true)}>启用</a>
            )}
            {col.dkState == 1 && (
              <a
                className={'text-danger'}
                onClick={() => this.enableKey(col, false)}
              >
                停用
              </a>
            )}
            <a className={'text-danger'} onClick={() => this.revokeKey(col)}>
              吊销
            </a>
          </div>
        ) : null;
      },
    },
  ];
  userInfo = (col) => {};
  carInfo = (col) => {};
  enableKey = (col, isEnablekey) => {
    let txt = isEnablekey ? '启用' : '停用';
    Modal.confirm({
      title: txt + '钥匙',
      content: `确定${txt}钥匙？`,
      onOk: () => {
        return this.props
          .dispatch({
            type: 'keysManage/enableKey',
            payload: col.id,
          })
          .then(
            () => {
              message.success('操作成功');
              this.dataTable.refresh();
            },
            (err) => {
              message.error(err.message);
            },
          );
      },
    });
  };
  revokeKey = (col) => {
    Modal.confirm({
      title: '吊销钥匙',
      content: `确定吊销钥匙？`,
      onOk: () => {
        return this.props
          .dispatch({
            type: 'keysManage/revokeKey',
            payload: col.id,
          })
          .then(
            () => {
              message.success('操作成功');
              this.dataTable.refresh();
            },
            (err) => {
              message.error(err.message);
            },
          );
      },
    });
  };
  del = (model) => {
    Modal.confirm({
      title: '删除车型',
      content: `确定删除“${model.name}”？`,
      onOk: () => {
        return this.props
          .dispatch({
            type: 'keysManage/del',
            payload: model.id,
          })
          .then(
            () => {
              message.success('操作成功');
              this.dataTable.refresh();
            },
            (err) => {
              message.error(err.message);
            },
          );
      },
    });
  };
  userInfo = () => {};
  render() {
    return (
      <div>
        <EasyTable
          scroll={{ x: '1200px' }}
          autoFetch
          source={getKeysList}
          dataProp={'data'}
          name={'keysDataTable'}
          rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
        />
      </div>
    );
  }
}

export default DataTable;
