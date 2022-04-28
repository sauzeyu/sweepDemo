import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal, Tag } from 'antd';
import { getErrorLogList } from '@/services/log';

class DataTable extends Component {
  state = {
    showUserInfo: false,
    showCarInfo: false,
    userInfo: {},
    carInfo: {},
  };
  columns = [
    {
      title: '文档id',
      dataIndex: 'id',
    },
    {
      title: '时间',
      dataIndex: 'time',
    },
    {
      title: '钥匙 Slot',
      dataIndex: 'keySlot',
    },
    {
      title: '日志类型',
      dataIndex: 'logType',
    },
    {
      title: '业务',
      dataIndex: 'business',
    },
    {
      title: '错误码',
      dataIndex: 'errorCode',
    },
    // {
    //   title: '操作',
    //   fixed: 'right',
    //   width: 300,
    //   render: (col) => {
    //     return (
    //       <div className={'link-group'}>
    //         <a className={'text-danger'} onClick={false}>
    //           删除
    //         </a>
    //       </div>
    //     );
    //   },
    // },
  ];
  onCancel = () => {
    this.setState({
      showUserInfo: false,
      showCarInfo: false,
    });
  };

  revokeKey = (col) => {
    Modal.confirm({
      title: '删除日志',
      content: '确定删除日志？',
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

  render() {
    return (
      <div>
        <EasyTable
          scroll={{ x: '1200px' }}
          autoFetch
          source={getErrorLogList}
          dataProp={'data'}
          name={'errorLogDataTable'}
          rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
        />
      </div>
    );
  }
}

export default DataTable;
