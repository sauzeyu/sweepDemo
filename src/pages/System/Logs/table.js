import React from 'react';
import { Badge, Button, Card, Modal } from 'antd';
import style from './style.less';
import EasyTable from '@/components/EasyTable';
import { getLogs } from '@/services/systemLogs';
import RequestInfo from '@/pages/System/Logs/RequestInfo';
import { connect } from 'dva';
import Authorized from '@/components/Authorized';
import {
  SYSTEM_LOG_AUDIT,
  SYSTEM_LOG_FILE,
} from '@/components/Authorized/AuthMap';

@EasyTable.connect(({ logsDataTable }) => ({
  logsDataTable,
}))
@connect(({ systemLogs }) => ({
  systemLogs,
}))
export default class LogsTable extends React.Component {
  audit = () => {
    Modal.confirm({
      title: '确定审核？',
      onOk: () => {
        return this.props
          .dispatch({
            type: 'systemLogs/audit',
          })
          .then(
            () => {
              this.dataTable.refresh();
            },
            (err) => {
              Modal.error({
                title: '失败',
                content: err.message,
              });
            },
          );
      },
    });
  };
  onFile = () => {
    Modal.confirm({
      title: '确定归档？',
      render: () => {
        return this.props
          .dispatch({
            type: 'systemLogs/putFile',
          })
          .then(
            () => {
              this.dataTable.refresh();
            },
            (err) => {
              Modal.error({
                title: '失败',
                content: err.message,
              });
            },
          );
      },
    });
  };
  render() {
    const columns = [
      {
        title: '审核状态',
        dataIndex: 'isAudit',
        render: (isAudit) => {
          return (
            <Badge
              status={isAudit ? 'success' : 'default'}
              text={isAudit ? '已审核' : '未审核'}
            />
          );
        },
      },
      {
        title: '操作用户',
        dataIndex: 'userName',
      },
      {
        title: '行为',
        dataIndex: 'title',
      },
      {
        title: '执行时间',
        dataIndex: 'createDate',
      },
      {
        title: '执行时长(毫秒)',
        dataIndex: 'useTime',
      },
      {
        title: '请求信息',
        key: 'request_info',
        render: (col) => {
          return <RequestInfo data={col} />;
        },
      },
    ];
    const isAudit = this.props.logsDataTable?.params?.isAudit == 1;
    return (
      <Card bordered={false} className={style.layoutVerticalSpace}>
        <EasyTable
          name={'logsDataTable'}
          columns={columns}
          rowKey={'id'}
          dataProp={'sysLogs'}
          source={getLogs}
          wrappedComponentRef={(ref) => (this.dataTable = ref)}
          extra={
            <div className={'btn-group'}>
              {isAudit ? (
                <Authorized route={SYSTEM_LOG_FILE}>
                  <Button onClick={this.onFile}>归档(2000条)</Button>
                </Authorized>
              ) : (
                <Authorized route={SYSTEM_LOG_AUDIT}>
                  <Button onClick={this.audit}>审计(2000条)</Button>
                </Authorized>
              )}
            </div>
          }
        />
      </Card>
    );
  }
}
