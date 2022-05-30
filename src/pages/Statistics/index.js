import React from 'react';
import nacos from '../../imgs/nacos.jpg';
import admin from '../../imgs/admin.jpg';
import zipkin from '../../imgs/zipkin.jpg';
import sentinel from '../../imgs/sentinel.jpg';

import {
  selectTotalUser,
  selectTotalVehicle,
  selectTotalKey,
  selectYesterdayRegisterUser,
  selectYesterdayRegisterKey,
  selectVehicleByFactory,
  selectLastWeekTotal,
} from '@/services/statistics';
import ReactEcharts from 'echarts-for-react';

import { Card, Col, Row, Statistic, Button, Space } from 'antd';
import {
  ArrowUpOutlined,
  KeyOutlined,
  CarOutlined,
  UserOutlined,
  UsergroupAddOutlined,
} from '@ant-design/icons';
import user from '@/models/user';

class index extends React.Component {
  state = {
    userTotal: 0,
    keyTotal: 0,
    vehicleTotal: 0,
    yesterdayRegisterUserTotal: 0,
    yesterdayRegisterKeyTotal: 0,
    vehicleFactoryList: [],
    userTotalList: [],
    keyTotalList: [],
    vehicleTotalList: [],
    dateList: [],
  };

  getOptionAsLeft = () => {
    return {
      title: {
        text: '车辆工厂分布',
        left: '40%',
        top: '2%',
      },
      tooltip: {
        trigger: 'item',
      },
      legend: {
        top: '5%',
        orient: 'vertical',
        left: 'left',
      },
      toolbox: {
        show: true,
        feature: {
          mark: { show: true },
          saveAsImage: {
            show: true,
            title: '保存为图片',
          },
          restore: {
            show: true,
            title: '重置',
          },
        },
        top: 'top',
      },
      series: [
        {
          type: 'pie',
          selectedMode: 'multiple',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          center: ['55%', '50%'],
          itemStyle: {
            borderRadius: 10,
            borderColor: '#FFFFFF',
            borderWidth: 2,
          },
          label: {
            show: false,
            position: 'center',
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '20',
              fontWeight: 'bold',
            },
          },
          labelLine: {
            show: false,
          },
          data: this.state.vehicleFactoryList,
        },
      ],
    };
  };
  getOptionAsMid = () => {
    return {
      title: {
        text: '车辆-钥匙-用户',
        left: '35%',
        top: '2%',
      },
      tooltip: {
        trigger: 'item',
      },
      legend: {
        top: '5%',
        orient: 'vertical',
        left: 'left',
      },
      toolbox: {
        show: true,
        feature: {
          mark: { show: true },
          saveAsImage: {
            show: true,
            title: '保存为图片',
          },
          restore: {
            show: true,
            title: '重置',
          },
        },
        top: 'top',
      },
      series: [
        {
          type: 'pie',
          selectedMode: 'multiple',
          radius: [20, 100],
          center: ['55%', '50%'],
          roseType: 'area',
          avoidLabelOverlap: true,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#ffffff',
            borderWidth: 2,
          },
          data: [
            { value: this.state.userTotal, name: '用户' },
            { value: this.state.keyTotal, name: '钥匙' },
            { value: this.state.vehicleTotal, name: '车辆' },
          ],
        },
      ],
    };
  };
  getLineChart = () => {
    return {
      title: {
        text: '近七日数据增长',
        bottom: 'bottom',
        left: 'center',
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          crossStyle: {
            color: '#999',
          },
        },
      },
      toolbox: {
        feature: {
          magicType: {
            show: true,
            type: ['line', 'bar', 'stack'],
            title: {
              line: '折线图',
              bar: '柱状图',
              stack: '堆叠',
              tiled: '平铺',
            },
          },
          restore: {
            show: true,
            title: '重置',
          },
        },
        right: '1%',
      },
      legend: {
        data: ['用户', '车辆', '钥匙'],
      },
      xAxis: [
        {
          type: 'category',
          data: this.state.dateList,
          axisPointer: {
            type: 'shadow',
          },
        },
      ],
      yAxis: [
        {
          type: 'value',
          name: '钥匙',
          min: 0,
          max: 50,
          interval: 10,
        },
        {
          type: 'value',
          name: '车辆/用户',
          min: 0,
          max: 50,
          interval: 10,
        },
      ],
      series: [
        {
          name: '用户',
          type: 'bar',
          tooltip: {
            valueFormatter: function (value) {
              return value + ' 个';
            },
          },
          data: this.state.userTotalList,
        },
        {
          name: '车辆',
          type: 'bar',
          tooltip: {
            valueFormatter: function (value) {
              return value + ' 辆';
            },
          },
          data: this.state.vehicleTotalList,
        },
        {
          name: '钥匙',
          type: 'line',
          yAxisIndex: 1,
          tooltip: {
            valueFormatter: function (value) {
              return value + ' 把';
            },
          },
          data: this.state.keyTotalList,
        },
      ],
    };
  };

  getUserTotal = () => {
    selectTotalUser().then((res) => {
      this.setState({ userTotal: res.data });
    });
  };
  getLastWeekTotal = () => {
    selectLastWeekTotal().then((res) => {
      this.setState({ userTotalList: res.data.userTotalList });
      this.setState({ keyTotalList: res.data.keyTotalList });
      this.setState({ vehicleTotalList: res.data.vehicleTotalList });
      this.setState({ dateList: res.data.dateList });
    });
  };

  getKeyTotal = () => {
    selectTotalKey().then((res) => {
      this.setState({ keyTotal: res.data });
    });
  };

  getVehicleTotal = () => {
    selectTotalVehicle().then((res) => {
      this.setState({ vehicleTotal: res.data });
    });
  };
  getYesterdayRegisterUserTotal = () => {
    selectYesterdayRegisterUser().then((res) => {
      this.setState({ yesterdayRegisterUserTotal: res.data });
    });
  };
  getYesterdayRegisterKey = () => {
    selectYesterdayRegisterKey().then((res) => {
      this.setState({ yesterdayRegisterKeyTotal: res.data });
    });
  };
  getVehicleFactoryList = () => {
    selectVehicleByFactory().then((res) => {
      this.setState({ vehicleFactoryList: res.data });
    });
  };

  componentDidMount() {
    // this.getUserTotal();
    // this.getKeyTotal();
    // this.getVehicleTotal();
    // this.getYesterdayRegisterUserTotal();
    // this.getYesterdayRegisterKey();
    // this.getVehicleFactoryList();
    // this.getLastWeekTotal();
  }

  render() {
    const {
      userTotal,
      keyTotal,
      vehicleTotal,
      yesterdayRegisterUserTotal,
      yesterdayRegisterKeyTotal,
    } = this.state;

    return (
      <div style={{ width: '100%', height: '100%', background: '#FFFFFF' }}>
        <Row>
          <Col span={5}>
            <Card
              title={<Statistic value={'用户总量'} prefix={<UserOutlined />} />}
              // extra={<a href="#">More</a>}
              hoverable={true}
            >
              <Statistic title="Total Users" value={userTotal} />
            </Card>
          </Col>
          <Col span={5}>
            <Card
              title={
                <Statistic
                  value={'今日注册用户'}
                  prefix={<UsergroupAddOutlined />}
                />
              }
              // extra={<a href="#">More</a>}
              hoverable={true}
            >
              <Statistic
                title="Increase"
                value={yesterdayRegisterUserTotal}
                valueStyle={{ color: '#cf1322' }}
                prefix={<ArrowUpOutlined />}
              />
            </Card>
          </Col>
          <Col span={5}>
            <Card
              title={<Statistic value={'钥匙总量'} prefix={<KeyOutlined />} />}
              // extra={<a href="#">More</a>}
              hoverable={true}
            >
              <Statistic title="Total Keys" value={keyTotal} />
            </Card>
          </Col>
          <Col span={5}>
            <Card
              title={
                <Statistic value={'今日新增钥匙'} prefix={<KeyOutlined />} />
              }
              // extra={<a href="#">More</a>}
              hoverable={true}
            >
              <Statistic
                title="Increase"
                value={yesterdayRegisterKeyTotal}
                valueStyle={{ color: '#cf1322' }}
                prefix={<ArrowUpOutlined />}
              />
            </Card>
          </Col>
          <Col span={4}>
            <Card
              title={<Statistic value={'车辆总量'} prefix={<CarOutlined />} />}
              // extra={<a href="#">More</a>}
              hoverable={true}
            >
              <Statistic title="Total Cars" value={vehicleTotal} />
            </Card>
          </Col>
        </Row>
        <Row align={'bottom'} style={{ marginTop: 20 }}>
          <Col span={6} push={4}>
            <ReactEcharts option={this.getOptionAsLeft()} notMerge={true} />
          </Col>
          <Col span={6} push={8}>
            <ReactEcharts option={this.getOptionAsMid()} notMerge={true} />
          </Col>
        </Row>

        <Row align={'bottom'} style={{ marginTop: 20 }}>
          <Col span={24}>
            <ReactEcharts option={this.getLineChart()} notMerge={true} />
          </Col>
        </Row>
      </div>
    );
  }
}

export default index;
