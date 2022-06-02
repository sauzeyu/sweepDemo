import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal, Tag } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import {
  getKeysList,
  selectVehicleById,
  selectUserById,
} from '@/services/keys';
import { analyzePermissions, DKState, KeyType } from '@/constants/keys';
import DescriptionList from '@/components/DescriptionList';
import { color, sunroofType, windowType } from '@/constants/cars';
import { keyLifecycleList, keyUseListById } from '@/services/cars';

const { Description } = DescriptionList;

@connect(({ keysManage, loading }) => ({
  keysManage,
  // upserting: loading.effects['keysManage/upsert'],
}))
class DataTable extends Component {
  state = {
    showUserInfo: false,
    showCarInfo: false,
    keyUseInfo: false,
    selectKey: {},
    userInfo: {},
    carInfo: {},
  };
  columns = [
    {
      title: '钥匙类型',
      dataIndex: 'parentId',
      render: (text) => {
        return KeyType(text);
      },
    },
    {
      title: '车辆vin号',
      dataIndex: 'vin',
      width: 250,
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
      title: '钥匙权限',
      dataIndex: 'permissions',
      width: 300,
      render: (text) => {
        return analyzePermissions(text);
      },
    },
    {
      title: '申请时间',
      dataIndex: 'applyTime',
    },
    {
      title: '操作',
      fixed: 'right',
      width: 350,
      render: (col) => {
        let isDisable = col.dkState === 3;

        let shareKeyStyle = {};

        if (col.parentId) {
          shareKeyStyle = {
            onClick: false,
            style: { opacity: 0.2, cursor: 'not-allowed' },
          };
        } else {
          shareKeyStyle = {
            onClick: () => {
              this.userInfo(col);
            },
          };
        }

        return (
          <div className={'link-group'}>
            <a onClick={() => this.keyUseInfo(col)}>使用记录</a>
            <a {...shareKeyStyle}>查看用户</a>
            <a onClick={() => this.carInfo(col)}>查看车辆</a>
            <a onClick={() => this.enableKey(col, true)} hidden={!isDisable}>
              解冻
            </a>
            <a
              className={'text-danger'}
              onClick={() => this.enableKey(col, false)}
              hidden={isDisable}
            >
              冻结
            </a>
            <a className={'text-danger'} onClick={() => this.revokeKey(col)}>
              吊销
            </a>
          </div>
        );
      },
    },
  ];
  keyUseLogColumns = [
    {
      title: '车辆vin号',
      dataIndex: 'vin',
      width: 250,
    },
    {
      title: '操作类型',
      dataIndex: 'statusCode',
      width: 700,
    },
    {
      title: '操作时间',
      dataIndex: 'operateTime',
      width: 400,
    },
  ];
  keyUseInfo = (col) => {
    this.setState({
      keyUseInfo: true,
      selectKey: col,
    });
  };
  userInfo = (col) => {
    this.setState({
      showUserInfo: true,
      userInfo: {},
    });

    selectUserById(col.userId).then(
      (res) => {
        this.setState({ userInfo: res.data });
      },

      (err) => {
        message.error(err.message);
      },
    );
  };
  onCancel = () => {
    this.setState({
      showUserInfo: false,
      showCarInfo: false,
      keyUseInfo: false,
      selectKey: {},
    });
  };
  carInfo = (col) => {
    this.setState({
      showCarInfo: true,
      carInfo: {},
    });
    selectVehicleById(col.vehicleId).then(
      (res) => {
        this.setState({ carInfo: res.data });
      },
      (err) => {
        message.error(err.message);
      },
    );
  };
  enableKey = (col, isEnableKey) => {
    let txt = isEnableKey ? '冻结' : '解冻';
    Modal.confirm({
      title: txt + '钥匙',
      content: `确定${txt}钥匙？`,
      onOk: () => {
        return this.props
          .dispatch({
            type: 'keysManage/enableKey',
            payload: { id: col.id, dkState: isEnableKey ? 2 : 3 },
          })
          .then(
            (res) => {
              if (res.code === 200) {
                message.success(res.msg);
              } else {
                message.error(res.msg);
              }
              this.dataTable.reload();
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

  render() {
    const {
      showUserInfo,
      showCarInfo,
      userInfo = {},
      carInfo = {},
    } = this.state;
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
        <Modal
          footer={null}
          title={'用户信息'}
          visible={showUserInfo}
          onCancel={this.onCancel}
          destroyOnClose={true}
        >
          <DescriptionList col={1}>
            <Description term={'电话'}>
              {userInfo ? userInfo.phone : ''}
            </Description>
            <Description term={'姓名'}>
              {userInfo ? userInfo.username : ''}
            </Description>
          </DescriptionList>
        </Modal>

        <Modal
          footer={null}
          title={'车辆信息'}
          visible={showCarInfo}
          onCancel={this.onCancel}
          destroyOnClose={true}
        >
          <DescriptionList col={1}>
            <Description term={'工厂编号'}>{carInfo.factoryNo}</Description>
            <Description term={'车主手机号'}>{carInfo.phone}</Description>
            <Description term={'车窗类型'}>
              {windowType[carInfo.windowType]}
            </Description>
            <Description term={'车辆颜色'}>{color[carInfo.color]}</Description>
            <Description term={'天窗类型'}>
              <Tag color="#87d068">{sunroofType[carInfo.sunroofType]}</Tag>
            </Description>
            <Description term={'蓝牙编号'}>{carInfo.hwDeviceSn}</Description>
            <Description term={'创建时间'}>{carInfo.createTime}</Description>
          </DescriptionList>
        </Modal>

        <Modal
          footer={null}
          title={'钥匙使用记录'}
          visible={this.state.keyUseInfo}
          onCancel={this.onCancel}
          destroyOnClose={true}
          // width={800}
        >
          <EasyTable
            autoFetch
            source={keyUseListById}
            dataProp={'data'}
            name={'keyUseLogDataTable'}
            rowKey={'id'}
            columns={this.keyUseLogColumns}
            renderHeader={(title, extra, page) => {
              let total = page.total;
              total = '共 ' + total + ' 条记录';
              return (
                <>
                  <p>
                    <Badge color="purple" text="钥匙类型：" />
                    {KeyType(this.state?.selectKey?.parentId)}
                  </p>
                  <div>
                    <Badge color="blue" text={total} />
                  </div>
                </>
              );
            }}
            fixedParams={{ keyId: this.state?.selectKey?.id }}
          />
        </Modal>
      </div>
    );
  }
}

export default DataTable;
