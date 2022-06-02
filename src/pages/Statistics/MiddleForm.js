import React from 'react';

import {
  Card,
  Col,
  Row,
  Statistic,
  Button,
  Spin,
  Tabs,
  DatePicker,
  Segmented,
} from 'antd';
import Icon, {
  KeyOutlined,
  CarOutlined,
  UserOutlined,
} from '@ant-design/icons';
import { icon_bluetooth } from '@/assets/icon';
import { selectKeyLogByMonth, selectTotal } from '@/services/statistics';
import ReactEcharts from 'echarts-for-react';

const { RangePicker } = DatePicker;
const { TabPane } = Tabs;

class TopForm extends React.Component {
  state = {
    spinningVisible: true,
    statisticsTotal: {},
    useLogCount: [],
    errorLogCount: [],
    buttonColor: '#272727',
  };

  keyUseLogTotalByMonth = () => {
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
        type: 'value',
      },
      series: [
        {
          data: this.state.useLogCount,
          type: 'bar',
          barWidth: '50',
        },
      ],
      grid: {
        left: '5%',
      },
      color: '#6293f9',
    };
  };

  keyErrorLogTotalByMonth = () => {
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
        type: 'value',
      },
      series: [
        {
          data: this.state.errorLogCount,
          type: 'bar',
          barWidth: '50',
        },
      ],
      grid: {
        left: '5%',
      },
      color: '#6293f9',
    };
  };

  componentDidMount() {
    selectTotal().then((res) => {
      this.setState({
        statisticsTotal: res.data,
        spinningVisible: false,
      });
    });
    selectKeyLogByMonth().then((res) => {
      this.setState({
        useLogCount: res.data.useLogCount,
        errorLogCount: res.data.errorLogCount,
      });
    });
  }

  render() {
    const buttonStyle = {
      style: { color: this.state.buttonColor },
      onClick: (a, b, c, d) => {
        console.log('buttonRef1 ', this.buttonRef2);
      },
      hover: () => {},
    };

    return (
      <Card>
        <Tabs
          size={'large'}
          defaultActiveKey="1"
          tabBarExtraContent={
            <Row>
              <Col>
                <Button
                  type="link"
                  ref={(ref) => (this.buttonRef1 = ref)}
                  {...buttonStyle}
                >
                  今日
                </Button>
              </Col>
              <Col>
                <Button
                  type="link"
                  ref={(ref) => (this.buttonRef2 = ref)}
                  {...buttonStyle}
                >
                  本周
                </Button>
              </Col>
              <Col>
                <Button
                  type="link"
                  ref={(ref) => (this.buttonRef3 = ref)}
                  {...buttonStyle}
                >
                  本月
                </Button>
              </Col>
              <Col>
                <Button
                  type="link"
                  ref={(ref) => (this.buttonRef4 = ref)}
                  {...buttonStyle}
                >
                  本年
                </Button>
              </Col>
              <Col>
                <RangePicker />
              </Col>
            </Row>
          }
        >
          <TabPane tab="钥匙使用记录" key="1">
            <Row>
              <Col span={18}>
                <ReactEcharts
                  option={this.keyUseLogTotalByMonth()}
                  notMerge={true}
                />
              </Col>
              <Col span={6}>
                <Card>
                  <Statistic title="Total Users" value={111} />
                </Card>
              </Col>
            </Row>
          </TabPane>
          <TabPane tab="钥匙故障记录" key="2">
            <Row>
              <Col span={18}>
                <ReactEcharts
                  option={this.keyErrorLogTotalByMonth()}
                  notMerge={true}
                />
              </Col>
              <Col span={6}>
                <Card>
                  <Statistic title="Total Users" value={111} />
                </Card>
              </Col>
            </Row>
          </TabPane>
        </Tabs>

        {/*<Row align={'bottom'} style={{marginTop: 20}}>*/}
        {/*  <Col span={6} push={4}>*/}
        {/*  </Col>*/}
        {/*  <Col span={6} push={8}>*/}
        {/*    <ReactEcharts option={this.getOptionAsMid()} notMerge={true}/>*/}
        {/*  </Col>*/}
        {/*</Row>*/}

        {/*<Row align={'bottom'} style={{marginTop: 20}}>*/}
        {/*  <Col span={24}>*/}
        {/*    <ReactEcharts option={this.getLineChart()} notMerge={true}/>*/}
        {/*  </Col>*/}
        {/*</Row>*/}
      </Card>
    );
  }
}

export default TopForm;
