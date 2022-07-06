import React, { Component } from 'react';
import { Button, Col, Form, Row, Select, Input, DatePicker } from 'antd';
import EasyTable from '@/components/EasyTable';
import { Permit } from '@/constants/keys';
import moment from '_moment@2.29.1@moment';
import {
  DownOutlined,
  UpOutlined,
} from '_@ant-design_icons@4.7.0@@ant-design/icons';
const { RangePicker } = DatePicker;

@EasyTable.connect(({ keyErrorLogDataTable }) => ({
  keyErrorLogDataTable,
}))
class SearchForm extends Component {
  state = {
    isExpand: true,
    buttonHidden: true,
  };
  form = React.createRef();
  handleSubmit = (values) => {
    this.form.current
      .validateFields()
      .then((values) => {
        if (values.startTime) {
          const startTime = values['startTime'];
          values.startTime = moment(startTime[0]).format('YYYY-MM-DD');
          values.endTime = moment(startTime[1])
            .add(1, 'days')
            .format('YYYY-MM-DD');
        }
        this.props.keyErrorLogDataTable.fetch(values);
      })
      .catch((errors) => {
        if (errors) return;
      });
  };

  onClick = () => {
    this.setState({
      isExpand: !this.state.isExpand,
      buttonHidden: !this.state.buttonHidden,
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
    const { buttonHidden, isExpand } = this.state;
    let buttonName;
    let icon;
    if (isExpand) {
      buttonName = '展开';
      icon = <DownOutlined />;
    } else {
      buttonName = '收起';
      icon = <UpOutlined />;
    }
    return (
      <Form {...formItemLayout} onFinish={this.handleSubmit} ref={this.form}>
        <Row type={'flex'} gutter={16}>
          <Col {...colSpan}>
            <Form.Item label={'车辆vin号'} name="vin">
              <Input placeholder="请输入车辆vin号" />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Form.Item label="手机品牌" name={'phoneBrand'}>
              <Input placeholder="请输入手机品牌" />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Form.Item label="手机型号" name={'phoneModel'}>
              <Input placeholder="请输入手机型号" />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Row gutter={16}>
              <Col>
                <Button
                  onClick={() => {
                    this.form.current.resetFields();
                  }}
                >
                  重置
                </Button>
              </Col>
              <Col>
                <Form.Item>
                  <Button type={'primary'} htmlType={'submit'}>
                    查询
                  </Button>
                </Form.Item>
              </Col>
              <Col>
                <Button type="link" onClick={this.onClick}>
                  {buttonName}
                  {icon}
                </Button>
              </Col>
            </Row>
          </Col>
        </Row>
        <Row type={'flex'} gutter={16} hidden={buttonHidden}>
          <Col {...colSpan}>
            <Form.Item label="操作类型" name={'statusCode'}>
              <Input placeholder="请输入操作类型" />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="操作时间" name={'startTime'}>
              <RangePicker />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'操作结果'} name="flag">
              <Select
                mode={'multiple'}
                placeholder="请选择操作结果"
                allowClear={true}
                showArrow
              >
                <Select.Option key={1} value={1}>
                  成功
                </Select.Option>
                <Select.Option key={0} value={0}>
                  失败
                </Select.Option>
              </Select>
            </Form.Item>
          </Col>
        </Row>
        <Row type={'flex'} gutter={16} hidden={buttonHidden}>
          <Col {...colSpan}>
            <Form.Item label={'用户id'} name="userId">
              <Input placeholder="请输入用户id" />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Form.Item label={'车辆品牌'} name="vehicleBrand">
              <Input placeholder="请输入车辆品牌" />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Form.Item label={'车辆型号'} name="vehicleModel">
              <Input placeholder="请输入车辆型号" />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    );
  }
}

export default SearchForm;
