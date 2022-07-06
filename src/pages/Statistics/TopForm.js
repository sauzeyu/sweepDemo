import React from 'react';
import ReactEcharts from 'echarts-for-react';
import * as echarts from 'echarts';
import { Card, Col, Row, Statistic, Spin, Progress } from 'antd';
import Icon, {
  KeyOutlined,
  CarOutlined,
  UserOutlined,
  CloseCircleOutlined,
  CheckCircleOutlined,
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
  vehicleCountOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'line',
        lineStyle: {
          type: 'solid',
        },
      },
    },
    xAxis: {
      show: false,
      type: 'category',
      boundaryGap: false,
      data: [
        '1月',
        '2月',
        '3月',
        '4月',
        '5月',
        '6月',
        '7月',
        '8月',
        '9月',
        '10月',
        '11月',
        '12月',
      ],
    },
    yAxis: {
      show: false,
      type: 'value',
      boundaryGap: false,
    },
    series: [
      {
        data: [15, 32, 44, 55, 78, 185, 169, 73, 213, 95, 62, 11],
        type: 'line',
        lineStyle: {
          color: '#ff7070',
          width: 1,
        },
        itemStyle: {
          opacity: 0,
        },
        // smooth: true,
      },
    ],
    grid: {
      x: 0, //距离左边
      x2: 0, //距离右边
      y: 0, //距离上边
      y2: 0, //距离下边
      left: 0,
      right: 0,
      top: 0,
      bottom: 0,
    },
  };
  keyStateCountOption = {
    tooltip: {
      trigger: 'item',
    },
    series: [
      {
        roam: false,
        type: 'treemap',
        top: 30,
        left: 0,
        right: 0,
        bottom: 10,
        nodeClick: false,
        breadcrumb: { show: false },
        data: [
          {
            itemStyle: { color: '#ff915a' },
            name: '车主钥匙',
            value: 875,
          },
          {
            itemStyle: { color: '#7ed3f4' },
            name: '子钥匙',
            value: 1625,
          },
        ],
      },
    ],
    grid: {
      x: 0, //距离左边
      x2: 0, //距离右边
      y: 0, //距离上边
      y2: 0, //距离下边
      left: 0,
      right: 0,
      top: 0,
      bottom: 0,
    },
  };
  keyUseCountOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'line',
        lineStyle: {
          type: 'solid',
        },
      },
    },
    xAxis: {
      show: false,
      type: 'category',
      boundaryGap: false,
      data: [
        '1月',
        '2月',
        '3月',
        '4月',
        '5月',
        '6月',
        '7月',
        '8月',
        '9月',
        '10月',
        '11月',
        '12月',
      ],
    },
    yAxis: {
      show: false,
      type: 'value',
      boundaryGap: false,
    },
    series: [
      {
        data: [765, 212, 446, 518, 815, 645, 739, 643, 431, 1255, 963, 786],
        type: 'line',
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(151,95,228,0.1)' },
            { offset: 0.5, color: 'rgba(238,130,238,0.6)' },
            { offset: 1, color: 'rgba(186,85,211)' },
          ]),
        },
        lineStyle: {
          color: '#975FE4',
          width: 1,
        },
        itemStyle: {
          opacity: 0,
        },
        smooth: true,
      },
    ],
    grid: {
      x: 0, //距离左边
      x2: 0, //距离右边
      y: 0, //距离上边
      y2: 0, //距离下边
      left: 0,
      right: 0,
      top: 0,
      bottom: 0,
    },
  };
  keyErrorCountOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow',
      },
    },
    xAxis: {
      type: 'category',
      data: [
        '1月',
        '2月',
        '3月',
        '4月',
        '5月',
        '6月',
        '7月',
        '8月',
        '9月',
        '10月',
        '11月',
        '12月',
      ],
    },
    yAxis: {
      splitLine: { show: false },
      type: 'value',
    },
    series: [
      {
        data: [715, 216, 436, 512, 876, 560, 920, 330, 153, 541, 396, 675],
        type: 'bar',
        color: '#6293f9',
        itemStyle: {
          emphasis: {
            color: '#53ea13',
          },
        },
      },
    ],
    grid: {
      x: 0, //距离左边
      x2: 0, //距离右边
      y: 0, //距离上边
      y2: 0, //距离下边
      left: 0,
      right: 0,
      top: 0,
      bottom: 0,
    },
  };

  render() {
    const { spinningVisible, statisticsTotal } = this.state;

    const { vehicleCount, keyCount, keyUseCount, keyErrorCount } =
      statisticsTotal;
    const cardStyle = {
      bodyStyle: {
        paddingTop: 9,
        paddingBottom: 8,
      },
    };
    const colSpan = {
      xs: 24,
      xl: 6,
      lg: 12,
      style: { marginBottom: 24 },
      md: 12,
      sm: 12,
    };
    const valueStyle = {
      valueStyle: {
        fontSize: 30,
        fontWeight: 'normal',
      },
    };
    return (
      <Row gutter={24}>
        <Col {...colSpan}>
          <Spin spinning={spinningVisible}>
            <Card
              {...cardStyle}
              title={
                <>
                  <Statistic title="车辆总数" value={'1032'} {...valueStyle} />
                  <ReactEcharts
                    option={this.vehicleCountOption}
                    notMerge={true}
                    style={{ width: '100%', height: '50%' }}
                  />
                </>
              }
            >
              今日新增车辆 361
            </Card>
          </Spin>
        </Col>
        <Col {...colSpan}>
          <Spin spinning={spinningVisible}>
            <Card
              {...cardStyle}
              title={
                <>
                  <Statistic
                    title={'钥匙总量'}
                    value={'2500'}
                    {...valueStyle}
                  />
                  <ReactEcharts
                    option={this.keyStateCountOption}
                    notMerge={true}
                    style={{ width: '100%', height: '50%' }}
                  />
                </>
              }
            >
              车主钥匙占比 35%
            </Card>
          </Spin>
        </Col>
        <Col {...colSpan}>
          <Spin spinning={spinningVisible}>
            <Card
              {...cardStyle}
              title={
                <>
                  <Statistic
                    title="钥匙使用次数"
                    value={'6330'}
                    {...valueStyle}
                  />
                  <ReactEcharts
                    option={this.keyUseCountOption}
                    notMerge={true}
                    style={{ width: '100%', height: '50%' }}
                  />
                </>
              }
            >
              今日使用次数 214
            </Card>
          </Spin>
        </Col>
        <Col {...colSpan}>
          <Spin spinning={spinningVisible}>
            <Card
              {...cardStyle}
              title={
                <>
                  <Statistic
                    title="钥匙故障次数"
                    value={'8218'}
                    {...valueStyle}
                  />
                  <ReactEcharts
                    option={this.keyErrorCountOption}
                    notMerge={true}
                    style={{ width: '100%', height: '50%' }}
                  />
                </>
              }
            >
              今日故障次数 3512
            </Card>
          </Spin>
        </Col>
      </Row>
    );
  }
}

export default TopForm;
