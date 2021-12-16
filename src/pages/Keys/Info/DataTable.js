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
import { Link } from 'umi';
import { DKState, KeysState } from '@/constants/keys';
import DescriptionList from '@/components/DescriptionList';

const { Description } = DescriptionList;

@connect(({ keysManage, loading }) => ({
  keysManage,
  // upserting: loading.effects['keysManage/upsert'],
}))
class DataTable extends Component {
  state = {
    showUserInfo: false,
    showCarInfo: false,
    userInfo: {},
    carInfo: {},
  };
  columns = [
    {
      title: '手机设备指纹',
      dataIndex: 'phoneFingerprint',
    },
    {
      title: '数字钥匙ID',
      dataIndex: 'keyOwnerId',
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
      dataIndex: 'permissions',
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
      dataIndex: 'keyStatus',
      render: (text) => {
        return KeysState[text];
      },
    },
    {
      title: '预配对值',
      dataIndex: 'pp',
    },
    {
      title: '操作',
      fixed: 'right',
      width: 300,
      render: (col) => {
        return col.dkState !== 5 ? (
          <div className={'link-group'}>
            <a onClick={() => this.userInfo(col)}>查看用户</a>
            <a onClick={() => this.carInfo(col)}>查看车辆</a>
            {col.dkState === 3 && (
              <a onClick={() => this.enableKey(col, true)}>启用</a>
            )}
            {col.dkState === 1 && (
              <a
                className={'text-danger'}
                onClick={() => this.enableKey(col, false)}
              >
                停用
              </a>
            )}
            {col.dkState === 0 && <a>未激活</a>}
            {col.dkState === 4 && <a>已过期</a>}
            <a className={'text-danger'} onClick={() => this.revokeKey(col)}>
              吊销
            </a>
          </div>
        ) : null;
      },
    },
  ];
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
    let txt = isEnableKey ? '启用' : '停用';
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
            (res) => {
              if (res.code === 200) {
                message.success(res.msg);
              } else {
                message.error(res.msg);
              }
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
            <Description term={'身份证'}>
              {userInfo ? userInfo.idCard : ''}
            </Description>
            <Description term={'是否有效'}>
              {userInfo ? (
                userInfo.isValid === 0 ? (
                  <Tag color="#f50">无效</Tag>
                ) : (
                  <Tag color="#87d068">有效</Tag>
                )
              ) : (
                <Tag color="red">无</Tag>
              )}
            </Description>
            <Description term={'指纹'}>
              {userInfo ? userInfo.phoneFingerprint : ''}
            </Description>
            <Description term={'状态'}>
              {userInfo
                ? userInfo.status === 0
                  ? '已注册'
                  : '已实名认证'
                : ''}
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
            <Description term={'车型代码'}>
              {carInfo ? carInfo.modelCode : ''}
            </Description>
            <Description term={'车牌号'}>
              {carInfo ? carInfo.license : ''}
            </Description>
            <Description term={'创建时间'}>
              {carInfo ? carInfo.createTime : ''}
            </Description>
            <Description term={'车主身份证号'}>
              {carInfo ? carInfo.ownerIdCard : ''}
            </Description>
            <Description term={'车主手机号'}>
              {carInfo ? carInfo.phone : ''}
            </Description>
            <Description term={'是否有效'}>
              {carInfo ? (
                carInfo.isValid === 0 ? (
                  <Tag color="#f50">报废</Tag>
                ) : (
                  <Tag color="#87d068">正常</Tag>
                )
              ) : (
                <Tag color="red">无</Tag>
              )}
            </Description>
            <Description term={'车辆颜色'}>
              {carInfo ? carInfo.color : ''}
            </Description>
            <Description term={'车辆蓝牙链接标识'}>
              {carInfo ? carInfo.bluetooth : ''}
            </Description>
          </DescriptionList>
        </Modal>
      </div>
    );
  }
}

export default DataTable;
