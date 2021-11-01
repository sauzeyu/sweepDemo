import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';
import { connect } from 'dva';
import { getCarModes } from '@/services/cars';

@connect(({ carsType, loading }) => ({
  carsType,
  upserting: loading.effects['carsType/upsert'],
}))
class DataTable extends Component {
  state = {
    editFormVisible: false,
  };
  columns = [
    {
      title: '车型名称',
      dataIndex: 'name',
      ellipsis: true,
      render: (name, col) => {
        return (
          <Badge
            status={col.isValid == 1 ? 'success' : 'error'}
            text={<a onClick={() => this.upsert(col)}>{name}</a>}
          />
        );
      },
    },
    {
      title: '代码',
      dataIndex: 'code',
      ellipsis: true,
    },
    {
      title: 'VIN号匹配规则',
      dataIndex: 'vinMatch',
    },
    {
      title: '备注',
      dataIndex: 'remark',
      ellipsis: true,
    },
    {
      title: '操作',
      render: (col) => {
        return (
          <div className={'link-group'}>
            <a onClick={() => this.del(col)}>删除</a>
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
  upsert = (model) => {
    this.setState({ editFormVisible: true }, () => {
      if (model) {
        this.editForm.setFieldsValue(model);
      }
    });
  };
  onOk = () => {
    this.editForm
      .validateFields()
      .then((values) => {
        this.props
          .dispatch({
            type: 'carsType/upsert',
            payload: values,
          })
          .then(
            () => {
              this.setState({ editFormVisible: false });
              this.dataTable.refresh();
            },
            (err) => {
              Modal.error({
                title: err.message,
              });
            },
          );
      })
      .catch((errors) => {
        if (errors) return;
      });
  };
  onCancel = () => {
    this.editForm.resetFields();
    this.setState({ editFormVisible: false });
  };
  render() {
    const { editFormVisible } = this.state;
    const { upserting } = this.props;
    return (
      <div>
        <EasyTable
          autoFetch
          source={getCarModes}
          dataProp={'data'}
          name={'carsTypeDataTable'}
          rowKey={'id'}
          columns={this.columns}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <Button
              type={'primary'}
              icon={<PlusCircleOutlined />}
              onClick={() => this.upsert()}
            >
              新增车型
            </Button>
          }
        />
        <DrawerConfirm
          title={'车型'}
          width={600}
          visible={editFormVisible}
          onOk={this.onOk}
          onCancel={this.onCancel}
          confirmLoading={upserting}
        >
          <EditForm editFormRef={(ref) => (this.editForm = ref.form.current)} />
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
