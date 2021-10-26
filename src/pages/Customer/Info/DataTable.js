import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import { getCustomerList } from '@/services/customer';
import { Link } from 'umi';
@connect(({ customer, loading }) => ({
  customer,
  upserting: loading.effects['customer/upsert'],
}))
class DataTable extends Component {
  state = {
    selectedRowKeys: [],
  };
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
            <a onClick={() => this.del(col)}>查看车辆</a>
            <a onClick={() => this.userInfo(col)}>查看钥匙</a>
            <a onClick={() => this.del(col)}>启用</a>
            <a className={'text-danger'} onClick={() => this.del(col)}>
              停用
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
            type: 'customer/del',
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
  handleRowSelectChange = (selectedRowKeys) => {
    this.setState({ selectedRowKeys });
  };
  handleTableChange = () => {
    // 切换分页会置空
    // this.setState({ selectedRowKeys: [] });
  };
  userInfo = () => {};
  render() {
    const { selectedRowKeys } = this.state;
    const rowSelection = {
      selectedRowKeys,
      onChange: this.handleRowSelectChange,
    };
    return (
      <div>
        <EasyTable
          autoFetch
          source={getCustomerList}
          dataProp={'data'}
          name={'customerDataTable'}
          rowKey={'id'}
          columns={this.columns}
          rowSelection={rowSelection}
          onChange={this.handleTableChange}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <div className={'btn-group'}>
              {selectedRowKeys.length > 0 && (
                <Button
                  type={'danger'}
                  onClick={() => this.del(selectedRowKeys)}
                >
                  删除用户
                </Button>
              )}
            </div>
          }
        />
      </div>
    );
  }
}

export default DataTable;
