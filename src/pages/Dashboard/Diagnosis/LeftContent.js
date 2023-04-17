import { BorderBox11, ScrollBoard } from '@jiaminghi/data-view-react';
import { Component } from 'react';
import { connect } from 'dva';

const styles = {
  leftContent: {
    marginTop: '-30px',
    display: 'flex',
    flexDirection: 'column',
  },
};

@connect(({ Diagnosis }) => ({
  Diagnosis,
}))
export default class LeftContent extends Component {
  state = {
    recentLogs: [],
    logList: [],
  };

  componentDidMount() {
    this.selectRecentLogs();
    // 设置定时器，30s更新一次
    this.timer = setInterval(() => {
      this.selectRecentLogs();
    }, 1000 * 30);
  }

  componentWillUnmount() {
    //  组件卸载时清除定时器
    clearInterval(this.timer);
  }

  selectRecentLogs = () => {
    this.props
      .dispatch({
        type: 'Diagnosis/selectRecentLogs',
        payload: {},
      })
      .then((res) => {
        if (res && res.code === 200 && res.data) {
          let logs = [];

          res.data.forEach((item) => {
            logs.push([item.functionalAbnormality.fault, item.time]);
          });
          this.setState({
            recentLogs: res.data,
            logList: logs,
          });
          this.props.dispatch({
            type: 'Diagnosis/changeLogDetail',
            payload: res?.data[0],
          });
          console.log('logs', logs);
        }
      });
  };

  render() {
    return (
      <div style={styles.leftContent}>
        <BorderBox11 title={'最新日志'} style={{ width: 380, padding: 20 }}>
          <ScrollBoard
            style={{ height: '95%', marginTop: 40 }}
            config={{
              data: this.state.logList,
              rowNum: 20,
              columnWidth: [200],
              oddRowBGC: 'rgba(0, 44, 81, 0.8)',
              evenRowBGC: 'rgba(10, 29, 50, 0.8)',
            }}
            onClick={(item) => {
              this.props.dispatch({
                type: 'Diagnosis/changeLogDetail',
                payload: this.state.recentLogs[item.rowIndex],
              });
            }}
          />
        </BorderBox11>
      </div>
    );
  }
}
