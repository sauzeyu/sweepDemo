import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal, Tag } from 'antd';
import { DownloadOutlined, PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import {
  getKeysList,
  selectVehicleById,
  selectUserById,
} from '@/services/keys';
import {
  analyzePermissions,
  DKState,
  KeySource,
  KeyState,
  KeyType,
} from '@/constants/keys';
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
    keyLifecycleInfoVisible: false,
    selectedKey: {},
  };

  lifecycleColumns = [
    {
      title: '序号',
      width: 80,
      render: (text, record, index) => {
        let currentIndex = this.dataTable?.state?.currentIndex;
        let currentPageSize = this.dataTable?.state?.currentPageSize;
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

  columns = [
    {
      title: '序号',
      width: 80,
      render: (text, record, index) => {
        let currentIndex = this.dataTable?.state?.currentIndex;
        let currentPageSize = this.dataTable?.state?.currentPageSize;
        return (currentIndex - 1) * currentPageSize + (index + 1);
      },
    },

    {
      title: '钥匙类型',
      dataIndex: 'parentId',
      render: (text) => {
        return KeyType(text);
      },
    },
    {
      title: '用户id',
      dataIndex: 'userId',
    },
    {
      title: '车辆vin号',
      dataIndex: 'vin',
      width: 180,
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
      title: '申请时间',
      dataIndex: 'applyTime',
    },
    {
      title: '周期(分钟)',
      dataIndex: 'period',
    },
    {
      title: '操作',
      fixed: 'right',
      width: 350,
      render: (col) => {
        let isDisable = col.dkState === 3;

        let disableStyle = {};

        if (col.dkState === 5 || col.dkState === 4) {
          disableStyle = {
            onClick: false,
            style: { opacity: 0.2, cursor: 'not-allowed', color: 'gray' },
          };
        }

        return (
          <div className={'link-group'}>
            {/*<a onClick={() => this.keyUseInfo(col)}>使用记录</a>*/}
            <a onClick={() => this.keyLifecycle(col)}>生命周期</a>
            <a onClick={() => this.carInfo(col)}>查看车辆</a>
            <a
              onClick={() => this.enableKey(col, true)}
              hidden={!isDisable}
              {...disableStyle}
            >
              解冻
            </a>
            <a
              className={'text-danger'}
              onClick={() => this.enableKey(col, false)}
              hidden={isDisable}
              {...disableStyle}
            >
              冻结
            </a>
            <a
              className={'text-danger'}
              onClick={() => this.revokeKey(col)}
              {...disableStyle}
            >
              吊销
            </a>
          </div>
        );
      },
    },
  ];
  keyUseLogColumns = [
    {
      title: '序号',
      width: 80,
      render: (text, record, index) => {
        let currentIndex = this.keyUseLogDataTable?.state?.currentIndex;
        let currentPageSize = this.keyUseLogDataTable?.state?.currentPageSize;
        return (currentIndex - 1) * currentPageSize + (index + 1);
      },
    },
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
  keyLifecycle = (col) => {
    this.setState({
      keyLifecycleInfoVisible: true,
      selectedKey: col,
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
      keyLifecycleInfoVisible: false,
      selectedKey: {},
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

    const { selectedKey } = this.state;
    let selectedKeyId = selectedKey.id;
    let selectedKeyType = selectedKey.parentId;

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
          extra={
            <Button type={'ghost'} size={'large'} icon={<DownloadOutlined />}>
              导出钥匙信息
            </Button>
          }
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
            <Description term={'车辆vin号'}>{carInfo?.vin}</Description>
            <Description term={'车辆型号'}>{carInfo?.vehicleModel}</Description>
            <Description term={'车辆品牌'}>{carInfo?.vehicleBrand}</Description>
            <Description term={'蓝牙序列号'}>{carInfo?.hwDeviceSn}</Description>
            <Description term={'蓝牙Mac地址'}>
              {carInfo?.bleMacAddress}
            </Description>
            <Description term={'蓝牙供应商编号'}>
              {carInfo?.hwDeviceProviderNo}
            </Description>
            <Description term={'创建时间'}>{carInfo?.createTime}</Description>
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
            wrappedComponentRef={(ref) => (this.keyUseLogDataTable = ref)}
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
                    <Badge color="pink" text="钥匙id：" />
                    <Tag color={'gold'}>{selectedKey?.id}</Tag>
                    <Badge color="purple" text="钥匙类型：" />
                    {KeyType(selectedKeyType)}
                  </p>
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

export default DataTable;
