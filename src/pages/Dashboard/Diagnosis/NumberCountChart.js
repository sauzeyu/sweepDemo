import ReactEcharts from 'echarts-for-react';
import React from 'react';
import 'echarts-liquidfill';

const styles = {
  phoneBrandChart: {
    height: 60,
    width: 50,
  },
};

export default class NumberCountChart extends React.Component {
  // static propTypes = {
  //   data: PropTypes.number, // 数据源
  // };
  static defaultProps = {
    data: 0,
  };

  render() {
    const { data } = this.props;
    let option = {
      series: [
        {
          amplitude: 0,

          label: {
            position: ['55%', '60%'],
            formatter: function () {
              return data;
            },
            fontSize: 40,
            color: 'rgb(102, 255, 255)',
          },
          backgroundStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: 'rgb(54, 98, 164)', // 0% 处的颜色
                },
                {
                  offset: 0.5,
                  color: 'rgb(10, 26, 66)', // 0% 处的颜色
                },
                {
                  offset: 1,
                  color: 'rgb(61, 127, 255)', // 100% 处的颜色
                },
              ],
              global: false, // 缺省为 false
            },
          },
          outline: {
            borderDistance: 1,
            itemStyle: {
              borderWidth: 3,
              borderColor: 'rgb(79, 148, 230)',
              shadowBlur: 20,
              shadowColor: 'rgb(134, 166, 245)',
            },
          },
          shape: 'container',
          type: 'liquidFill',
          data: [0],
          itemStyle: {
            opacity: 0,
          },
        },
      ],
    };

    return (
      <ReactEcharts
        notMerge={true}
        option={option}
        style={styles.phoneBrandChart}
      />
    );
  }
}
