import React from 'react';
import { Button, Card, DatePicker, Form, Input, Select } from 'antd';
import { FormattedMessage } from 'umi';
import style from './style.less';
import EasyTable from '@/components/EasyTable';
import { removeEmptyProperty } from '@/utils';
import moment from 'moment';
const { RangePicker } = DatePicker;

@EasyTable.connect(
  ({ logsDataTable }) => ({
    logsDataTable,
  }),
  { ensureProvider: true },
)
export default class LogFilterForm extends React.Component {
  form = React.createRef();
  componentDidMount() {
    this.handleSubmit();
  }

  handleSubmit = () => {
    this.form.current.validateFields().then((values) => {
      if (values.date && values.date.length > 0) {
        values.startDate = values.date[0].format('YYYY-MM-DD HH:mm:ss');
        values.endDate = values.date[1].format('YYYY-MM-DD HH:mm:ss');
      }
      values.date = undefined;
      this.props.logsDataTable.fetch(removeEmptyProperty(values));
    });
  };

  render() {
    return (
      <Card bordered={false}>
        <Form
          ref={this.form}
          layout={'inline'}
          onFinish={this.handleSubmit}
          className={style.resetFormMargin}
        >
          <Form.Item label={'操作用户'} name={'userName'}>
            <Input />
          </Form.Item>
          <Form.Item label={'行为'} name={'title'}>
            <Input />
          </Form.Item>
          <Form.Item label={'是否审计'} name={'isAudit'} initialValue={0}>
            <Select>
              <Select.Option value={0}>未审核</Select.Option>
              <Select.Option value={1}>已审核</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item label={'日志时间'} name={'date'}>
            <RangePicker
              showTime={{
                defaultValue: [
                  moment('00:00:00', 'HH:mm:ss'),
                  moment('23:59:59', 'HH:mm:ss'),
                ],
              }}
            />
          </Form.Item>
          <Form.Item>
            <Button htmlType={'submit'} type={'primary'}>
              <FormattedMessage id={'Common.message.search'} />
            </Button>
          </Form.Item>
        </Form>
      </Card>
    );
  }
}
