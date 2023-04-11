import ReactEcharts from 'echarts-for-react';
import React from 'react';
import 'echarts-liquidfill';
import PropTypes from 'prop-types';

const styles = {
  phoneBrandChart: {
    height: 120,
    width: 110,
  },
};

export default class PhoneBrandChart extends React.Component {
  static propTypes = {
    data: PropTypes.string.isRequired, // 数据源
  };
  static defaultProps = {
    data: '',
  };

  render() {
    const { data } = this.props;
    let option = {
      series: [
        {
          backgroundColor: '',
          label: {
            position: ['50%', '50%'],
            formatter: function () {
              return data;
            },
            fontSize: 20,
            color: 'white',
          },
          backgroundStyle: {
            color: 'rgb(9, 24, 43)',
          },
          outline: {
            borderDistance: 0,
            itemStyle: {
              borderWidth: 5,
              borderColor: '#00BAFF',
              shadowBlur: 20,
              shadowColor: 'rgb(134, 166, 245)',
            },
          },
          shape: 'container',
          type: 'liquidFill',
          data: [0.6, 0.5, 0.4, 0.3],
          itemStyle: {
            opacity: 0.6,
          },
          emphasis: {
            itemStyle: {
              opacity: 0.9,
            },
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
