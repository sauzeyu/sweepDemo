import React from 'react';
import { Card, Col, Row, Tabs, DatePicker, Button } from 'antd';
import ReactEcharts from 'echarts-for-react';

class BottomForm extends React.Component {
  componentDidMount() {}

  keyLifecycleOption = () => {
    return {
      legend: {
        data: ['第一季度', '第二季度', '第三季度', '第四季度'],
        bottom: '0%',
      },
      radar: {
        indicator: [
          { name: 'Sales', max: 6500, color: 'red' },
          { name: 'Administration', max: 16000 },
          { name: 'Information Technology', max: 30000 },
          { name: 'Customer Support', max: 38000 },
          { name: 'Development', max: 52000 },
        ],
      },
      series: [
        {
          name: 'Budget vs spending',
          type: 'radar',
          data: [
            {
              value: [4200, 3000, 20000, 35000, 25000, 1800],
              name: '第一季度',
            },
            {
              value: [2000, 14000, 28000, 26000, 14000, 2100],
              name: '第二季度',
            },
            {
              value: [3000, 1100, 25000, 30900, 32000, 61000],
              name: '第三季度',
            },
            {
              value: [5000, 1400, 27000, 26000, 42000, 21000],
              name: '第四季度',
            },
          ],
        },
      ],
    };
  };

  render() {
    return (
      <>
        <Row gutter={24}>
          <Col span={12}>
            <Card title={'钥匙生命周期'}></Card>
          </Col>
          <Col span={12}>
            <Card title={'生命周期占比'}>
              <ReactEcharts
                option={this.keyLifecycleOption()}
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
