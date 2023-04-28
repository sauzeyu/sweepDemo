import {
  BorderBox1,
  BorderBox3,
  BorderBox4,
  BorderBox8,
} from '@jiaminghi/data-view-react';
import { Col, Row, Typography } from 'antd';
import React from 'react';
import { connect } from 'dva';
import 'echarts-liquidfill';
import PhoneBrandChart from '@/pages/Dashboard/Diagnosis/PhoneBrandChart';
import FaultLogsChart from '@/pages/Dashboard/Diagnosis/FaultLogsChart';
import NumberCountChart from '@/pages/Dashboard/Diagnosis/NumberCountChart';

const styles = {
  rightContent: {
    width: '100%',
    display: 'flex',
    flexDirection: 'column',
  },
  topContainer: {
    display: 'flex',
    flexDirection: 'row',
  },
  bottomContainer: {
    height: '100%',
    display: 'flex',
    flexDirection: 'column',
  },
  topContainerLeft: {
    width: '100%',
    display: 'flex',
  },
  topContainerRight: {
    display: 'flex',
    flexDirection: 'column',
  },
  rightTopTitle: {
    fontSize: '18px',
    fontWeight: 'bold',
    color: 'white',
    marginTop: 10,
  },
  bottomTitle: {
    fontSize: '25px',
    fontWeight: 'bold',
    color: 'white',
    marginTop: 40,
  },
  topTitle: {
    fontSize: '25px',
    fontWeight: 'bold',
    color: 'white',
    marginTop: 30,
  },
};

@connect(({ Diagnosis }) => ({
  Diagnosis,
}))
export default class RightContent extends React.Component {
  topRightChartBox = React.createRef();
  topLeftChartBox = React.createRef();
  bottomChartBox = React.createRef();
  topLeftBottomChartBox = React.createRef();
  state = {
    phoneBrandLogs: [],
    faultLogs: [],
    todayLogsCount: [],
  };

  componentDidMount() {
    this.selectPhoneBrandLogs();
    this.selectFaultLogs();
    this.selectTodayLogsCount();
    // 设置定时器，30s更新一次
    this.timer = setInterval(() => {
      this.selectPhoneBrandLogs();
      this.selectFaultLogs();
      this.selectTodayLogsCount();
    }, 1000 * 30);
  }

  componentWillUnmount() {
    //  组件卸载时清除定时器
    clearInterval(this.timer);
  }

  selectTodayLogsCount = () => {
    this.props
      .dispatch({
        type: 'Diagnosis/selectTodayLogsCount',
        payload: {},
      })
      .then((res) => {
        if (res && res.code === 200) {
          this.setState({
            todayLogsCount: res.data || [0, 0, 0, 0, 0, 0, 0],
          });
        }
      });
  };
  selectFaultLogs = () => {
    this.props
      .dispatch({
        type: 'Diagnosis/selectFaultLogs',
        payload: {},
      })
      .then((res) => {
        if (res && res.code === 200 && res.data) {
          this.setState({
            faultLogs: res.data,
          });
        }
      });
  };

  selectPhoneBrandLogs = () => {
    this.props
      .dispatch({
        type: 'Diagnosis/selectPhoneBrandLogs',
        payload: {},
      })
      .then((res) => {
        if (res && res.code === 200 && res.data) {
          this.setState({
            phoneBrandLogs: res.data,
          });
          this.topRightChartBox.current.setWH();
          this.topLeftChartBox.current.setWH();
          this.bottomChartBox.current.setWH();
          this.topLeftBottomChartBox.current.setWH();
        }
      });
  };

  render() {
    let logDetail = this.props.Diagnosis.logDetail;
    let solution = logDetail?.functionalAbnormality?.solution;

    return (
      <div style={styles.rightContent}>
        <div style={styles.topContainer}>
          <div style={styles.topContainerLeft}>
            <BorderBox3 ref={this.topLeftChartBox}>
              <Row justify="center">
                <Typography
                  style={{ width: '100%', height: '100%', padding: 50 }}
                >
                  <Typography.Title style={{ color: 'rgba(102, 255, 255, 1)' }}>
                    车辆VIN：{logDetail?.vin}
                  </Typography.Title>
                  <Typography.Title
                    level={5}
                    style={{ color: 'rgba(102, 255, 255, 1)' }}
                  >
                    业务ID：{logDetail?.functionalAbnormality?.businessId}
                  </Typography.Title>
                  <Typography.Title
                    level={5}
                    style={{ color: 'rgba(102, 255, 255, 1)' }}
                  >
                    故障ID：{logDetail?.functionalAbnormality?.faultId}
                  </Typography.Title>
                  <Typography.Paragraph style={{ color: 'white' }}>
                    在 {logDetail?.time}，
                    {logDetail?.functionalAbnormality?.business}
                    业务发生异常，经过系统分析故障原因为：
                  </Typography.Paragraph>
                  <Typography.Title level={4} style={{ color: 'red' }}>
                    {logDetail?.functionalAbnormality?.fault}
                  </Typography.Title>
                  <Typography.Title style={{ color: 'white' }}>
                    解决方案
                  </Typography.Title>
                  {solution &&
                    solution.map((item, index) => {
                      return (
                        <Typography.Title
                          level={5}
                          key={index}
                          style={{ color: 'white' }}
                        >
                          {item}
                        </Typography.Title>
                      );
                    })}
                </Typography>
              </Row>
            </BorderBox3>
          </div>
          <div style={styles.topContainerRight}>
            <BorderBox4
              reverse={true}
              ref={this.topLeftBottomChartBox}
              style={{ height: 200 }}
            >
              <Row justify="center">
                <div style={styles.rightTopTitle}>今日收集日志数量</div>
              </Row>
              <Row justify="center">
                <BorderBox1 style={{ width: 500, height: 130 }}>
                  <Row justify="center" style={{ height: 200, marginTop: 35 }}>
                    {this.state.todayLogsCount.map((item, index) => {
                      return (
                        <Col key={index} style={{ width: 60 }}>
                          <NumberCountChart data={item} />
                        </Col>
                      );
                    })}
                  </Row>
                </BorderBox1>
              </Row>
            </BorderBox4>
            <div style={styles.topContainerRight}>
              <BorderBox8 ref={this.topRightChartBox}>
                <Row justify="center">
                  <div style={styles.rightTopTitle}>手机故障品牌占比</div>
                </Row>
                <Row
                  gutter={[8, 16]}
                  style={{ padding: 15, marginLeft: 10 }}
                  justify="center"
                >
                  {this.state.phoneBrandLogs.map((item) => {
                    return (
                      <Col key={item?.phoneBrand} span={6}>
                        <PhoneBrandChart data={item?.formatValue} />
                      </Col>
                    );
                  })}
                </Row>
              </BorderBox8>
            </div>
          </div>
        </div>
        <div style={styles.bottomContainer}>
          <BorderBox4 ref={this.bottomChartBox}>
            <Row justify="center">
              <div style={styles.bottomTitle}>系统故障占比</div>
            </Row>
            <Row style={{ padding: 15 }}>
              {this.state.faultLogs.map((item) => {
                return (
                  <Col key={item?.functionalAbnormalId} span={4}>
                    <FaultLogsChart data={item} />
                  </Col>
                );
              })}
            </Row>
          </BorderBox4>
        </div>
      </div>
    );
  }
}
