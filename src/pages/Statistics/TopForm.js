import React from 'react';

import { Card, Col, Row, Statistic, Spin } from 'antd';
import Icon, {
  KeyOutlined,
  CarOutlined,
  UserOutlined,
} from '@ant-design/icons';
import { icon_bluetooth } from '@/assets/icon';
import { selectTotal } from '@/services/statistics';

class TopForm extends React.Component {
  state = {
    spinningVisible: true,
    statisticsTotal: {},
  };

  componentDidMount() {
    selectTotal().then((res) => {
      this.setState({
        statisticsTotal: res.data,
        spinningVisible: false,
      });
    });
  }

  render() {
    const { spinningVisible, statisticsTotal } = this.state;

    const { userCount, vehicleCount, keyCount, bluetoothCount } =
      statisticsTotal;

    const colSpan = {
      xs: 24,
      xl: 6,
      lg: 12,
      style: { marginBottom: 24 },
      md: 12,
      sm: 12,
    };
    return (
      <Row gutter={24}>
        <Col {...colSpan}>
          <Spin spinning={spinningVisible}>
            <Card
              title={<Statistic value={'用户总量'} prefix={<UserOutlined />} />}
              hoverable={true}
            >
              <Statistic title="Total Users" value={userCount} />
            </Card>
          </Spin>
        </Col>
        <Col {...colSpan}>
          <Spin spinning={spinningVisible}>
            <Card
              title={<Statistic value={'钥匙总量'} prefix={<KeyOutlined />} />}
              hoverable={true}
            >
              <Statistic title="Total Keys" value={keyCount} />
            </Card>
          </Spin>
        </Col>
        <Col {...colSpan}>
          <Spin spinning={spinningVisible}>
            <Card
              title={
                <Statistic
                  value={'蓝牙总量'}
                  prefix={<Icon component={icon_bluetooth} />}
                />
              }
              hoverable={true}
            >
              <Statistic title="Total Bluetooth" value={bluetoothCount} />
            </Card>
          </Spin>
        </Col>
        <Col {...colSpan}>
          <Spin spinning={spinningVisible}>
            <Card
              title={<Statistic value={'车辆总量'} prefix={<CarOutlined />} />}
              hoverable={true}
            >
              <Statistic title="Total Cars" value={vehicleCount} />
            </Card>
          </Spin>
        </Col>
      </Row>
    );
  }
}

export default TopForm;
