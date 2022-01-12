import React, { Component } from 'react';
import EasyTable from '@/components/EasyTable';
import { Badge, Button, message, Modal, Tag } from 'antd';
import { PlusCircleOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import { checkResult, windowType, sunroofType, color } from '@/constants/cars';
import {
  getKeysLogsList,
  selectUserById,
  selectVehicleById,
} from '@/services/keys';
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
      title: '车辆VIN',
      dataIndex: 'vin',
      render: (text, col) => {
        return <a onClick={() => this.carInfo(col)}>{text}</a>;
      },
    },
    {
      title: '用户名称',
      dataIndex: 'username',
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

    selectVehicleById(col.vehicleId).then(
      (res) => {
        this.setState({ carInfo: res.data });
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
            <Description term={'姓名'}>{userInfo.username}</Description>
            <Description term={'身份证'}>{userInfo.idCard}</Description>
            <Description term={'是否有效'}>
              {userInfo.isValid === 0 ? (
                <Tag color="#f50">无效</Tag>
              ) : (
                <Tag color="#87d068">有效</Tag>
              )}
            </Description>
            <Description term={'指纹'}>{userInfo.phoneFingerprint}</Description>
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
            <Description term={'工厂编号'}>{carInfo.factoryNo}</Description>
            <Description term={'检测结果'}>
              {carInfo.checkResult === 0 ? (
                <Tag color="#f50">{checkResult[carInfo.checkResult]}</Tag>
              ) : (
                <Tag color="#87d068">{checkResult[carInfo.checkResult]}</Tag>
              )}
            </Description>
            <Description term={'车主手机号'}>{carInfo.phone}</Description>
            <Description term={'车窗类型'}>
              {windowType[carInfo.windowType]}
            </Description>
            <Description term={'车辆颜色'}>{color[carInfo.color]}</Description>
            <Description term={'天窗类型'}>
              <Tag color="#87d068">{sunroofType[carInfo.sunroofType]}</Tag>
            </Description>
            <Description term={'蓝牙编号'}>{carInfo.bleNo}</Description>
            <Description term={'创建时间'}>{carInfo.createTime}</Description>
          </DescriptionList>
        </Modal>
      </div>
    );
  }
}

export default DataTable;
