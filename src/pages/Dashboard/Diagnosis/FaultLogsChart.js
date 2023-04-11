import ReactEcharts from 'echarts-for-react';
import React from 'react';
import 'echarts-liquidfill';

const styles = {
  faultLogsChart: { width: 280, height: 280 },
};

export default class FaultLogsChart extends React.Component {
  // static propTypes = {
  //   data: PropTypes.string.isRequired, // 数据源
  // };
  static defaultProps = {
    data: '',
  };

  render() {
    const { data } = this.props;
    let option = {
      series: [
        {
          backgroundColor: '',
          type: 'gauge',
          startAngle: 90,
          endAngle: -270,
          pointer: {
            show: false,
          },
          progress: {
            show: true,
            overlap: false,
            roundCap: true,
            clip: false,
            itemStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: 'rgba(0, 255, 255, 1)', // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: 'rgba(0, 145, 255, 1)', // 100% 处的颜色
                  },
                ],
                global: false, // 缺省为 false
              },
            },
          },
          axisLine: {
            lineStyle: {
              width: 20,
              color: [[1, 'rgb(61, 127, 255,0.5)']],
            },
          },
          splitLine: {
            show: false,
            distance: 0,
            length: 10,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            show: false,
            distance: 50,
          },
          data: [
            {
              value: data.percentage,
              name: data.functionalAbnormal,
              title: {
                offsetCenter: ['0%', '120%'],
              },
              detail: {
                valueAnimation: true,
                offsetCenter: ['0%', '0%'],
              },
            },
          ],
          title: {
            fontSize: 14,
            color: 'rgb(102, 255, 255)',
          },
          detail: {
            width: 50,
            height: 14,
            fontSize: 24,
            color: 'rgb(102, 255, 255)',
            formatter: '{value}%',
          },
        },
      ],
    };
    return (
      <ReactEcharts
        notMerge={true}
        option={option}
        style={styles.faultLogsChart}
      />
    );
  }
}
