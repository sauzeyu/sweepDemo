import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal, Tag } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import { getKeysLogsList } from '@/services/keys';
import DescriptionList from '@/components/DescriptionList';
const { Description } = DescriptionList;
@connect(({ keysLogs, keysManage, loading }) => ({
  keysLogs,
  keysManage,
  // upserting: loading.effects['keysLogs/upsert'],
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
      title: '车型名称',
      dataIndex: 'modelName',
    },
    {
      title: '车辆VIN',
      dataIndex: 'vin',
      render: (text, col) => {
        return <a onClick={() => this.carInfo(col)}>{text}</a>;
      },
    },
    {
      title: '用户名称',
      dataIndex: 'userName',
      render: (text, col) => {
        return <a onClick={() => this.userInfo(col)}>{text}</a>;
      },
    },
    {
      title: '类型',
      dataIndex: 'type',
    },
    {
      title: '结果',
      dataIndex: 'result',
    },
    {
      title: '失败原因',
      dataIndex: 'reason',
    },
    {
      title: '日期',
      dataIndex: 'date',
    },
  ];
  carInfo = (col) => {
    this.setState({
      showCarInfo: true,
      carInfo: {},
    });
    this.props
      .dispatch({
        type: 'keysManage/vehicleListById',
        payload: col.id,
      })
      .then(
        (res) => {
          this.setState({ carInfo: (res.data && res.data[0]) || [] });
        },
        (err) => {
          message.error(err.message);
        },
      );
  };
  userInfo = (col) => {
    this.setState({
      showUserInfo: true,
      userInfo: {},
    });
    this.props
      .dispatch({
        type: 'keysManage/userListByKeyId',
        payload: col.id,
      })
      .then(
        (res) => {
          this.setState({ userInfo: (res.data && res.data[0]) || [] });
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
          autoFetch
          source={getKeysLogsList}
          dataProp={'data'}
          name={'keysLogsDataTable'}
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
            <Description term={'电话'}>{userInfo.phone}</Description>
            <Description term={'姓名'}>{userInfo.name}</Description>
            <Description term={'身份证'}>{userInfo.idnum}</Description>
            <Description term={'是否有效'}>
              {userInfo.isvalid === 0 ? (
                <Tag color="#f50">无效</Tag>
              ) : (
                <Tag color="#87d068">有效</Tag>
              )}
            </Description>
            <Description term={'指纹'}>{userInfo.devFp}</Description>
            <Description term={'状态'}>
              {userInfo.status === 0 ? '已注册' : '已实名认证'}
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
            <Description term={'车型代码'}>{carInfo.code}</Description>
            <Description term={'车牌号'}>{carInfo.license}</Description>
            <Description term={'创建时间'}>{carInfo.createTime}</Description>
            <Description term={'车主身份证号'}>{carInfo.ownerID}</Description>
            <Description term={'车主手机号'}>{carInfo.phone}</Description>
            <Description term={'是否有效'}>
              {carInfo.isvalid === 0 ? (
                <Tag color="#f50">报废</Tag>
              ) : (
                <Tag color="#87d068">正常</Tag>
              )}
            </Description>
            <Description term={'车辆颜色'}>{carInfo.colour}</Description>
            <Description term={'车辆蓝牙链接标识'}>
              {carInfo.bluetooth}
            </Description>
          </DescriptionList>
        </Modal>
      </div>
    );
  }
}

export default DataTable;
