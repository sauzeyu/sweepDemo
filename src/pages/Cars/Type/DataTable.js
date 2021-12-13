import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';
import { connect } from 'dva';
import { getCarModes, delCarModesPost } from '@/services/cars';

@connect(({ carsType, loading }) => ({
  carsType,
}))
class DataTable extends Component {
  state = {
    editFormVisible: false,
    idList: [],
  };
  columns = [
    {
      title: '车型名称',
      dataIndex: 'modelName',
      ellipsis: true,
    },
    {
      title: '代码',
      dataIndex: 'modelCode',
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
            <a onClick={() => this.upsert(col)}>编辑</a>
          </div>
        );
      },
    },
  ];
  del = (model) => {
    const json = JSON.parse(JSON.stringify({ id: model.id }));
    const { idList } = this.state;
    idList.push(json);
    Modal.confirm({
      title: '删除车型',
      content: `确定删除“${model.name}”？`,
      onOk: () => {
        return this.props
          .dispatch({
            type: 'carsType/delModeBatch',
            payload: idList,
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
            (res) => {
              this.setState({ editFormVisible: false });
              this.editForm.resetFields();
              const msg = values.id == null ? '新增' : '修改';
              if (res.code === 200) {
                Modal.success({
                  title: msg + '成功',
                });
                this.dataTable.refresh();
              } else {
                Modal.error({
                  title: msg + '失败',
                });
              }
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
        >
          <EditForm editFormRef={(ref) => (this.editForm = ref.form.current)} />
        </DrawerConfirm>
      </div>
    );
  }
}

export default DataTable;
