import React from 'react';
import { Card, Col, Row, Tabs, DatePicker, Button } from 'antd';
import ReactEcharts from 'echarts-for-react';
import {
  selectKeyErrorLogByAllPhoneBrand,
  selectKeyErrorLogByPhoneBrand,
} from '@/services/statistics';

class BottomForm extends React.Component {
  componentDidMount() {
    selectKeyErrorLogByAllPhoneBrand().then((res) => {
      this.setState({
        phoneErrorData: res.data,
      });
    });
    selectKeyErrorLogByPhoneBrand().then((res) => {
      this.setState({
        keyErrorCount: res.data,
      });
    });
  }

  state = {
    phoneErrorData: [],
    keyErrorCount: [],
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
          type: 'treemap',
          roam: false,
          top: 30,
          left: 0,
          right: 0,
          bottom: 10,
          nodeClick: false,
          breadcrumb: { show: false },
          data: this.state?.phoneErrorData,
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
  keyErrorTypeCountOption = () => {
    return {
      tooltip: {
        trigger: 'item',
      },
      // legend: {
      //   top: '15%',
      //   orient: 'vertical',
      //   left: 'right',
      // },
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
          radius: [25, 125],
          center: ['50%', '45%'],
          roseType: 'area',
          itemStyle: {
            borderRadius: 8,
          },
          data: this.state.keyErrorCount,
        },
      ],
    };
  };

  render() {
    let onClick = {
      click: (event) => {
        const params = new URLSearchParams();
        params.append('phoneBrand', event?.data?.name);
        console.log('params ', params);
        selectKeyErrorLogByPhoneBrand(params).then((res) => {
          this.setState({
            keyErrorCount: res.data,
          });
        });
      },
    };
    return (
      <>
        <Row gutter={24}>
          <Col span={12}>
            <Card title={'手机品牌故障率占比'}>
              <ReactEcharts
                option={this.keyStateCountOption()}
                notMerge={true}
                onEvents={onClick}
              />
            </Card>
          </Col>
          <Col span={12}>
            <Card title={'钥匙故障类型占比'}>
              <ReactEcharts
                option={this.keyErrorTypeCountOption()}
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
