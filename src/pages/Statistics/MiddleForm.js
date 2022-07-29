import React from 'react';
import moment from 'moment';
import { Card, Col, Row, Tabs, DatePicker, Button } from 'antd';
import {
  selectKeyLogByMonth,
  selectKeyUseLogByTime,
  selectKeyErrorLogByTime,
  selectTotal,
} from '@/services/statistics';
import ReactEcharts from 'echarts-for-react';

const { RangePicker } = DatePicker;
const { TabPane } = Tabs;

class MiddleForm extends React.Component {
  state = {
    useLogCount: [],
    errorLogCount: [],
    keyUseLogCount: [],
    keyErrorLogCount: [],
    monthList: [],
    pickerTime: [moment(), moment().add(1, 'days')],
    fileUseName: [],
    fileErrorName: [],
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
        data: this.state.monthList,
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
        data: this.state.monthList,
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

  getOptionByKeyUseLog = () => {
    return {
      title: {
        text: this.state.fileUseName,
        left: '20%',
        top: '2%',
        show: false,
      },
      tooltip: {
        trigger: 'item',
        // formatter: '{a} <br/>{b} : {c} ({d}%)'
      },
      legend: {
        type: 'scroll',
        orient: 'vertical',
        right: 10,
        top: 20,
        bottom: 20,
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
          center: ['30%', '50%'],
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
          data: this.state.keyUseLogCount,
        },
      ],
    };
  };

  getOptionByKeyErrorLog = () => {
    return {
      title: {
        text: this.state.fileErrorName,
        left: '20%',
        top: '2%',
        show: false,
      },
      tooltip: {
        trigger: 'item',
      },
      legend: {
        type: 'scroll',
        orient: 'vertical',
        right: 10,
        top: 20,
        bottom: 20,
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
          center: ['30%', '50%'],
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
          data: this.state.keyErrorLogCount,
        },
      ],
    };
  };

  componentDidMount() {
    selectKeyLogByMonth().then((res) => {
      this.setState({
        useLogCount: res.data?.useLogCount,
        errorLogCount: res.data?.errorLogCount,
        monthList: res.data?.monthList,
      });
    });

    const timeString = [
      moment().format('YYYY-MM-DD'),
      moment().add(1, 'days').format('YYYY-MM-DD'),
    ];
    this.selectKeyLogCount(timeString);
  }

  selectKeyLogCount = (times) => {
    let params = new URLSearchParams();
    let startTime1 = moment(times[0]).format('YYYY-MM-DD');
    let endTime1 = moment(times[1]).format('YYYY-MM-DD');
    let startTime;
    let endTime;

    if (startTime1 == endTime1 || moment(times[1]) - moment(times[0]) > 1) {
      startTime = startTime1;
      endTime = endTime1;
    } else {
      startTime = startTime1;
      endTime = moment(times[1]).add(1, 'days').format('YYYY-MM-DD');
    }

    let fileUse = '钥匙使用类型';
    let fileError = '钥匙故障类型';
    this.state.fileUseName = startTime + '~' + endTime + fileUse;
    this.state.fileErrorName = startTime + '~' + endTime + fileError;
    params.append('startTime', moment(times[0]).format('YYYY-MM-DD'));
    params.append(
      'endTime',
      moment(times[1]).add(1, 'days').format('YYYY-MM-DD'),
    );

    selectKeyUseLogByTime(params).then((res) => {
      if (res.data) {
        this.setState({
          keyUseLogCount: res.data,
        });
      } else {
        this.setState({
          keyUseLogCount: [],
        });
      }
    });
    selectKeyErrorLogByTime(params).then((res) => {
      if (res.data) {
        this.setState({
          keyErrorLogCount: res.data,
        });
      } else {
        this.setState({
          keyErrorLogCount: [],
        });
      }
    });
  };

  timePickerOnChange = (time, timeString) => {
    this.setState({
      pickerTime: time,
    });
    if (time) {
      this.selectKeyLogCount(timeString);
    }
  };

  todayOnClick = () => {
    const timeString = [moment().startOf('days'), moment().startOf('days')];
    this.setState({
      pickerTime: timeString,
    });
    this.selectKeyLogCount(timeString);
  };

  weekOnClick = () => {
    const timeString = [
      moment().day('Monday').add(1, 'days'),
      moment()
        .day('Monday')
        .day(+7),
    ];
    this.setState({
      pickerTime: timeString,
    });
    this.selectKeyLogCount(timeString);
  };
  monthOnClick = () => {
    const timeString = [moment().startOf('month'), moment().endOf('month')];
    this.setState({
      pickerTime: timeString,
    });
    this.selectKeyLogCount(timeString);
  };

  yearOnClick = () => {
    const timeString = [moment().startOf('year'), moment().endOf('year')];
    this.setState({
      pickerTime: timeString,
    });
    this.selectKeyLogCount(timeString);
  };

  render() {
    return (
      <Card>
        <Tabs
          size={'large'}
          defaultActiveKey="use"
          onTabClick={this.onTabClick}
          destroyInactiveTabPane={true}
          tabBarExtraContent={
            <Row gutter={0}>
              <Col>
                <Button type="default" onClick={this.todayOnClick}>
                  今日
                </Button>
              </Col>
              <Col>
                <Button type="default" onClick={this.weekOnClick}>
                  本周
                </Button>
              </Col>
              <Col>
                <Button type="default" onClick={this.monthOnClick}>
                  本月
                </Button>
              </Col>
              <Col>
                <Button type="default" onClick={this.yearOnClick}>
                  本年
                </Button>
              </Col>
              <Col>
                <RangePicker
                  value={this.state.pickerTime}
                  onChange={this.timePickerOnChange}
                />
              </Col>
            </Row>
          }
        >
          <TabPane tab="钥匙使用记录" key="use">
            <Row>
              <Col span={16}>
                <ReactEcharts
                  option={this.keyUseLogTotalByMonth()}
                  notMerge={true}
                />
              </Col>
              <Col span={8}>
                {/*<Card title={'钥匙使用类型'}>*/}
                <ReactEcharts
                  option={this.getOptionByKeyUseLog()}
                  notMerge={true}
                />
                {/*</Card>*/}
              </Col>
            </Row>
          </TabPane>
          <TabPane tab="钥匙故障记录" key="error">
            <Row>
              <Col span={16}>
                <ReactEcharts
                  option={this.keyErrorLogTotalByMonth()}
                  notMerge={true}
                />
              </Col>
              <Col span={8}>
                <ReactEcharts
                  option={this.getOptionByKeyErrorLog()}
                  notMerge={true}
                />
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

export default MiddleForm;
