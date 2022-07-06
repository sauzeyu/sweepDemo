import React from 'react';
import { Card, Col, Row, Tabs, DatePicker, Button } from 'antd';
import ReactEcharts from 'echarts-for-react';

class BottomForm extends React.Component {
  componentDidMount() {}

  keyStateCountOption = {
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
        type: 'treemap',
        roam: false,
        top: 30,
        left: 0,
        right: 0,
        bottom: 10,
        nodeClick: false,
        breadcrumb: { show: false },
        data: [
          {
            itemStyle: { color: '#5c2223' },
            name: '华为',
            value: 1875,
          },
          {
            itemStyle: { color: '#ed5a65' },
            name: '苹果',
            value: 3965,
          },
          {
            itemStyle: { color: '#80766e' },
            name: '小米',
            value: 453,
          },
          {
            itemStyle: { color: '#7ed3f4' },
            name: 'Oppo',
            value: 735,
          },
          {
            itemStyle: { color: '#ff915a' },
            name: 'Vivo',
            value: 2765,
          },
          {
            itemStyle: { color: '#c02c38' },
            name: '荣耀',
            value: 3765,
          },
          {
            itemStyle: { color: '#eeaa9c' },
            name: '魅族',
            value: 535,
          },
          {
            itemStyle: { color: '#c5708b' },
            name: '红米',
            value: 235,
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
  keyErrorTypeCountOption = {
    tooltip: {
      trigger: 'item',
    },
    legend: {
      top: '15%',
      orient: 'vertical',
      left: 'right',
    },
    series: [
      {
        type: 'pie',
        radius: [25, 125],
        center: ['50%', '45%'],
        roseType: 'area',
        itemStyle: {
          borderRadius: 8,
        },
        data: [
          { value: 40, name: '喇叭驱动失效' },
          { value: 38, name: '电源模式非OFF' },
          { value: 32, name: '车辆未上锁' },
          { value: 30, name: '锁系统失效' },
          { value: 28, name: '灯系统失效' },
          { value: 26, name: '车门打开' },
          { value: 22, name: 'PEPS网络超时' },
          { value: 18, name: '没有权限' },
        ],
      },
    ],
  };

  render() {
    return (
      <>
        <Row gutter={24}>
          <Col span={12}>
            <Card title={'手机品牌故障率占比'}>
              <ReactEcharts option={this.keyStateCountOption} notMerge={true} />
            </Card>
          </Col>
          <Col span={12}>
            <Card title={'钥匙故障类型占比'}>
              <ReactEcharts
                option={this.keyErrorTypeCountOption}
                notMerge={true}
              />
            </Card>
          </Col>
        </Row>
      </>
    );
  }
}

export default BottomForm;
