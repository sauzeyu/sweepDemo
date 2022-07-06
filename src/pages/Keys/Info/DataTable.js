import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import {
  Badge,
  Button,
  message,
  Modal,
  Tag,
  Popover,
  Card,
  Tooltip,
} from 'antd';
import { DownloadOutlined, DownOutlined, UpOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import { getKeysList, selectVehicleById } from '@/services/keys';
import { DKState, KeySource, KeyState, KeyType } from '@/constants/keys';
import { keyLifecycleList, keyUseListById } from '@/services/cars';
import { useMemo } from 'react';

const SubTable = (props) => {
  const dataList = React.useRef();
  const columns = [
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
      title: '吊销来源',
      dataIndex: 'keySource',
      width: 160,
      render: (text) => {
        return KeySource[text];
      },
    },
  ];
  const name = useMemo(() => `carSubModelsDataTable_${props.id}`, [props.id]);
  return (
    <Card>
      <EasyTable
        columns={columns}
        rowKey={'id'}
        source={keyLifecycleList}
        name={name}
        autoFetch
        fixedParams={{ keyId: props.id }}
        renderHeader={() => null}
        wrappedComponentRef={() => {
          return dataList;
        }}
      />
    </Card>
  );
};

@connect(({ keysManage, loading }) => ({
  keysManage,
}))
class DataTable extends Component {
  state = {
    carInfo: {},
  };

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
      title: '钥匙状态',
      dataIndex: 'dkState',
      render: (text) => {
        return DKState[text];
      },
    },
    {
      title: '生效时间',
      dataIndex: 'valFrom',
      ellipsis: {
        showTitle: false,
      },
      render: (text) => (
        <Tooltip placement="topLeft" title={text}>
          {text}
        </Tooltip>
      ),
    },
    {
      title: '失效时间',
      dataIndex: 'valTo',
      ellipsis: {
        showTitle: false,
      },
      render: (text) => (
        <Tooltip placement="topLeft" title={text}>
          {text}
        </Tooltip>
      ),
    },
    {
      title: '申请时间',
      dataIndex: 'applyTime',
      ellipsis: {
        showTitle: false,
      },
      render: (text) => (
        <Tooltip placement="topLeft" title={text}>
          {text}
        </Tooltip>
      ),
    },
    {
      title: '周期(分钟)',
      dataIndex: 'period',
    },
    {
      title: '操作',
      fixed: 'right',
      width: 240,
      render: (col) => {
        let isDisable = col.dkState === 3;
        let disableStyle = {};

        if (col.dkState === 5 || col.dkState === 4) {
          disableStyle = {
            onClick: () => {},
            style: { opacity: 0.2, cursor: 'not-allowed', color: 'gray' },
          };
        }
        return (
          <div className={'link-group'}>
            <Popover
              content={
                <>
                  <p>车辆vin号：{col?.vin}</p>
                  <p>车辆型号：{this.state.carInfo?.vehicleModel}</p>
                  <p>车辆品牌：{this.state.carInfo?.vehicleBrand}</p>
                </>
              }
              title=""
              trigger="click"
            >
              <a onClick={() => this.carInfo(col)}>查看车辆</a>
            </Popover>
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
              hidden={isDisable}
              {...disableStyle}
            >
              吊销
            </a>
          </div>
        );
      },
    },
  ];
  carInfo = (col) => {
    this.setState({
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
            payload: {
              keyId: col.id,
              dkState: isEnableKey ? 1 : 3,
              userId: col.userId,
            },
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
      content: '确定吊销钥匙?',
      onOk: () => {
        this.props
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
          columnWidth={120}
          expandIconAsCell={false}
          expandIconColumnIndex={10}
          expandable={{
            expandedRowRender: (record) => {
              return <SubTable {...record} />;
            },
            expandIcon: (props) => {
              return (
                <a onClick={(e) => props.onExpand(props.record, e)}>
                  生命周期 {props.expanded ? <UpOutlined /> : <DownOutlined />}
                </a>
              );
            },
          }}
        />
      </div>
    );
  }
}

export default DataTable;
