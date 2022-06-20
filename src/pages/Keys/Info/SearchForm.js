import React, { Component } from 'react';
import { Button, Col, Form, Row, Select, Input, DatePicker } from 'antd';
import EasyTable from '@/components/EasyTable';
import { Permit } from '@/constants/keys';
import moment from '_moment@2.29.1@moment';

const { RangePicker } = DatePicker;

@EasyTable.connect(({ keysDataTable }) => ({
  keysDataTable,
}))
class SearchForm extends Component {
  form = React.createRef();
  handleSubmit = (evt) => {
    this.form.current
      .validateFields()
      .then((values) => {
        if (!values.keyType || values.keyType.length === 0) {
          delete values.keyType;
        }
        if (!values.startTime) {
          delete values.startTime;
        }
        if (!values.vin) {
          delete values.vin;
        }
        if (!values.userId) {
          delete values.userId;
        }

        if (values.keyType && values.keyType.length > 0) {
          values.keyType = values.keyType.reduce(
            (prev, current) => prev + current,
          );
        }

        if (values.startTime) {
          const startTime = values['startTime'];
          values.startTime = moment(startTime[0]).format('YYYY-MM-DD');
          values.endTime = moment(startTime[1])
            .add(1, 'days')
            .format('YYYY-MM-DD');
        }

        this.props.keysDataTable.fetch(values);
      })
      .catch((errors) => {
        if (errors) return;
      });
  };

  render() {
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
    };
    const colSpan = {
      xs: 24,
      sm: 6,
    };
    return (
      <Form {...formItemLayout} onFinish={this.handleSubmit} ref={this.form}>
        <Row type={'flex'}>
          <Col {...colSpan}>
            <Form.Item label={'钥匙类型'} name="keyType">
              <Select mode={'multiple'} allowClear={true}>
                <Select.Option key={1} value={1}>
                  车主钥匙
                </Select.Option>
                <Select.Option key={2} value={2}>
                  分享钥匙
                </Select.Option>
              </Select>
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'车辆vin号'} name="vin">
              <Input placeholder="请输入车辆vin号" />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'用户id'} name="userId">
              <Input placeholder="请输入用户id" />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'钥匙状态'} name="keyState">
              <Select
                mode={'multiple'}
                style={{ width: 280 }}
                allowClear={true}
              >
                <Select.Option key={1} value={1}>
                  启用
                </Select.Option>
                <Select.Option key={3} value={3}>
                  冻结
                </Select.Option>
                <Select.Option key={4} value={4}>
                  过期
                </Select.Option>
                <Select.Option key={5} value={5}>
                  吊销
                </Select.Option>
              </Select>
            </Form.Item>
          </Col>
        </Row>
        <Row>
          <Col {...colSpan}>
            <Form.Item label="申请时间" name={'startTime'}>
              <RangePicker />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="生效时间" name={'startTime'}>
              <RangePicker />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="失效时间" name={'startTime'}>
              <RangePicker />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'周期'} name="userId">
              <Input placeholder="请输入钥匙周期 " style={{ width: 280 }} />
            </Form.Item>
          </Col>
        </Row>
        <Row>
          <Col offset={23}>
            <Form.Item>
              <Button
                type={'primary'}
                className={'gutter-left_lg'}
                htmlType={'submit'}
              >
                查询
              </Button>
            </Form.Item>
          </Col>
        </Row>
      </Form>
    );
  }
}

export default SearchForm;
