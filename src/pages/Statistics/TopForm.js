import React from 'react';
import ReactEcharts from 'echarts-for-react';
import * as echarts from 'echarts';
import { Card, Col, Row, Statistic, Spin, Progress, Modal } from 'antd';
import Icon, {
  KeyOutlined,
  CarOutlined,
  UserOutlined,
  CloseCircleOutlined,
  CheckCircleOutlined,
} from '@ant-design/icons';
import { icon_bluetooth } from '@/assets/icon';
import {
  selectTotal,
  vehicleStatistics,
  keyStatistics,
  keyUseTimeStatistics,
  keyErrorTimeStatistics,
  selectForExpiration,
} from '@/services/statistics';

class TopForm extends React.Component {
  state = {
    spinningVisible: true,
    statisticsTotal: {},
    vehicleTotal: [],
    keyUseTimeTotal: {},
    keyErrorTimeTotal: {},
    keyTotal: {},
    expiredLogsModat: false,
  };

  componentDidMount() {
    if (!localStorage.getItem('expiredModalFirst')) {
      selectForExpiration().then((res) => {
        if (res?.data) {
          this.setState({
            expiredLogsModat: true,
          });

          console.log('res', res);
          localStorage.setItem('expiredModalFirst', 'true');
          Modal.info({
            title: '数据过期提醒',

            content: (
              <>
                {res.msg},请联系管理人员及时对数据进行备份操作：&nbsp;
                <br />
                <ul>
                  {res.data &&
                    Object.keys(res.data).map((key) => (
                      <li key={key}>
                        {key}月过期数据{res.data[key]}条
                      </li>
                    ))}
                </ul>
              </>
            ),
            okText: '确认',
          });
        }
      });
    }
    selectTotal().then((res) => {
      this.setState({
        statisticsTotal: res.data,
      });
    });
    vehicleStatistics().then((res) => {
      this.setState({
        vehicleTotal: res.data,
      });
    });
    keyStatistics().then((res) => {
      this.setState({
        keyTotal: res.data,
        spinningVisible: false,
      });
    });
    keyUseTimeStatistics().then((res) => {
      this.setState({
        keyUseTimeTotal: res.data,
      });
    });
    keyErrorTimeStatistics().then((res) => {
      this.setState({
        keyErrorTimeTotal: res.data,
      });
    });
  }

  vehicleCountOption = () => {
    return {
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
          data: this.state.vehicleTotal?.vehicleMonthList,
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
  };
  keyStateCountOption = () => {
    return {
      tooltip: {
        trigger: 'item',
        formatter: function (param) {
          return (
            param.marker +
            param.name +
            '&nbsp;&nbsp;&nbsp;&nbsp;' +
            '<b>' +
            param.value +
            '</b>'
          );
        },
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
              value: this.state.keyTotal?.masterCount,
            },
            {
              itemStyle: { color: '#7ed3f4' },
              name: '子钥匙',
              value: this.state.keyTotal?.childCount,
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
  };
  keyUseCountOption = () => {
    return {
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
          data: this.state.keyUseTimeTotal?.useMonthList,
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
  };
  keyErrorCountOption = () => {
    return {
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
          data: this.state.keyErrorTimeTotal?.errorMonthList,
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
  };

  render() {
    const { spinningVisible, statisticsTotal } = this.state;

    const { vehicleCount, keyCount, keyUseCount, keyErrorCount } =
      statisticsTotal;

    const { countErrorToday } = this.state?.keyErrorTimeTotal;
    const { countUseToday } = this.state?.keyUseTimeTotal;
    const { proportion } = this.state?.keyTotal;
    const { newToday } = this.state?.vehicleTotal;
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
      <div>
        <Row gutter={24}>
          <Col {...colSpan}>
            <Spin spinning={spinningVisible}>
              <Card
                {...cardStyle}
                title={
                  <>
                    <Statistic
                      title="车辆总数"
                      value={vehicleCount}
                      {...valueStyle}
                    />
                    <ReactEcharts
                      option={this.vehicleCountOption()}
                      notMerge={true}
                      style={{ width: '100%', height: '50%' }}
                    />
                  </>
                }
              >
                今日新增车辆 {newToday}
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
                      value={keyCount}
                      {...valueStyle}
                    />
                    <ReactEcharts
                      option={this.keyStateCountOption()}
                      notMerge={true}
                      style={{ width: '100%', height: '50%' }}
                    />
                  </>
                }
              >
                车主钥匙占比 {proportion}
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
                      title="本年钥匙使用次数"
                      value={keyUseCount}
                      {...valueStyle}
                    />
                    <ReactEcharts
                      option={this.keyUseCountOption()}
                      notMerge={true}
                      style={{ width: '100%', height: '50%' }}
                    />
                  </>
                }
              >
                今日使用次数 {countUseToday}
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
                      title="本年钥匙故障次数"
                      value={keyErrorCount}
                      {...valueStyle}
                    />
                    <ReactEcharts
                      option={this.keyErrorCountOption()}
                      notMerge={true}
                      style={{ width: '100%', height: '50%' }}
                    />
                  </>
                }
              >
                今日故障次数 {countErrorToday}
              </Card>
            </Spin>
          </Col>
        </Row>
      </div>
    );
  }
}

export default TopForm;
