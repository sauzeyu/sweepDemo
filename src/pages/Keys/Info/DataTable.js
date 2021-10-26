import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import { getKeysList } from '@/services/keys';
import { Link } from 'umi';

@connect(({ keysManage, loading }) => ({
  keysManage,
  // upserting: loading.effects['keysManage/upsert'],
}))
class DataTable extends Component {
  state = {};
  columns = [
    {
      title: '联系电话',
      dataIndex: 'phone',
    },
    {
      title: '操作',
      fixed: 'right',
      width: 200,
      render: (col) => {
        return (
          <div className={'link-group'}>
            <a onClick={() => this.userInfo(col)}>查看用户</a>
            <a onClick={() => this.del(col)}>查看车辆</a>
            <a onClick={() => this.del(col)}>启用</a>
            <a className={'text-danger'} onClick={() => this.del(col)}>
              停用
            </a>
            <a className={'text-danger'} onClick={() => this.del(col)}>
              吊销
            </a>
          </div>
        );
      },
    },
  ];
  del = (model) => {
    Modal.confirm({
      title: '删除车型',
      content: `确定删除“${model.name}”？`,
      onOk: () => {
        return this.props
          .dispatch({
            type: 'carsType/del',
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
