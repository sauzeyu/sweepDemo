import React, { Component } from 'react';
import {
  Button,
  Col,
  Form,
  Row,
  Select,
  Input,
  DatePicker,
  InputNumber,
} from 'antd';
import EasyTable from '@/components/EasyTable';
import { UpOutlined, DownOutlined, SwapRightOutlined } from '@ant-design/icons';
import { Permit } from '@/constants/keys';
import moment from 'moment';

const { RangePicker } = DatePicker;

@EasyTable.connect(({ keysDataTable }) => ({
  keysDataTable,
}))
class SearchForm extends Component {
  state = {
    isExpand: true,
    buttonHidden: true,
  };
  form = React.createRef();
  handleSubmit = (evt) => {
    this.form.current
      .validateFields()
      .then((values) => {
        if (values.keyType && values.keyType.length > 0) {
          values.keyType = values.keyType.reduce(
            (prev, current) => prev + current,
          );
        }
        if (values.dkState && values.dkState.length > 0) {
          values.dkState = values.dkState.toString();
        }
        if (values.applyTime) {
          const time = values.applyTime;
          values.applyStartTime = moment(time[0]).format('YYYY-MM-DD');
          values.applyEndTime = moment(time[1])
            .add(1, 'days')
            .format('YYYY-MM-DD');
          delete values.applyTime;
        }

        if (values.valFromTime) {
          const time = values.valFromTime;
          values.valFromStartTime = moment(time[0]).format('YYYY-MM-DD');
          values.valFromEndTime = moment(time[1])
            .add(1, 'days')
            .format('YYYY-MM-DD');
          delete values.valFromTime;
        }
        if (values.valToTime) {
          const time = values.valToTime;
          values.valToStartTime = moment(time[0]).format('YYYY-MM-DD');
          values.valToEndTime = moment(time[1])
            .add(1, 'days')
            .format('YYYY-MM-DD');
          delete values.valToTime;
        }

        console.log(' values ', values);
        this.props.keysDataTable.fetch(values);
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
      <Form
        {...formItemLayout}
        onFinish={this.handleSubmit}
        ref={this.form}
        onFieldsChange={(changedFields, allFields) => {
          this.props.getFormValues(allFields);
        }}
      >
        <Row type={'flex'} gutter={16}>
          <Col {...colSpan}>
            <Form.Item label={'用户id'} name="userId">
              <Input placeholder="请输入用户id" />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Form.Item label={'车辆vin号'} name="vin">
              <Input placeholder="请输入车辆vin号" style={{ width: 275 }} />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <div style={{ height: 56 }}>
              <Form.Item label={'周期'}>
                <Input.Group compact>
                  <Form.Item name="periodMin">
                    <InputNumber
                      style={{
                        width: 85,
                      }}
                      placeholder="min"
                    />
                  </Form.Item>
                  <Input
                    style={{
                      width: 30,
                      pointerEvents: 'none',
                    }}
                    placeholder="~"
                    disabled
                  />
                  <Form.Item name="periodMax">
                    <InputNumber
                      style={{
                        width: 85,
                      }}
                      placeholder="max"
                    />
                  </Form.Item>
                  <Form.Item name="periodUnit" initialValue="minute">
                    <Select style={{ width: 60 }}>
                      <Select.Option value="minute">分</Select.Option>
                      <Select.Option value="hour">时</Select.Option>
                      <Select.Option value="day">天</Select.Option>
                    </Select>
                  </Form.Item>
                </Input.Group>
              </Form.Item>
            </div>
          </Col>

          <Col {...colSpan}>
            <Row gutter={16} type={'flex'}>
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

        <Row hidden={buttonHidden} type={'flex'} gutter={16}>
          <Col {...colSpan}>
            <Form.Item label="申请时间" name={'applyTime'}>
              <RangePicker />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="生效时间" name={'valFromTime'}>
              <RangePicker style={{ width: 275 }} />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label="失效时间" name={'valToTime'}>
              <RangePicker />
            </Form.Item>
          </Col>
        </Row>
        <Row hidden={buttonHidden} type={'flex'} gutter={16}>
          <Col {...colSpan}>
            <Form.Item label={'钥匙类型'} name="keyType">
              <Select
                placeholder="请选择钥匙类型"
                mode={'multiple'}
                allowClear={true}
                showArrow
              >
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
            <Form.Item label={'钥匙状态'} name="dkState">
              <Select
                placeholder="请选择钥匙状态"
                mode={'multiple'}
                style={{ width: 275 }}
                showArrow
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
      </Form>
    );
  }
}

export default SearchForm;
