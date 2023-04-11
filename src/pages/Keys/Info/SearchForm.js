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
const dateFormat = 'YYYY/MM/DD';

@EasyTable.connect(({ keysDataTable }) => ({
  keysDataTable,
}))
class SearchForm extends Component {
  state = {
    isExpand: true,
    buttonHidden: true,
    flag: true,
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

        if (values.keyClassification && values.keyClassification.length > 0) {
          values.keyClassification = values.keyClassification.toString();
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

        this.props.keysDataTable.fetch(values);
      })
      .catch((errors) => {
        if (errors) return;
      });
  };

  componentDidUpdate(prevProps, prevState, snapshot) {
    if (this.props.keysDataTable && this.state.flag) {
      this.handleSubmit();
      this.setState({ flag: false });
    }
  }
  onClick = () => {
    this.setState({
      isExpand: !this.state.isExpand,
      buttonHidden: !this.state.buttonHidden,
    });
  };
  defaultStartTime = () => {
    let defaultTime = [];

    let now = new Date(); //当前日期
    let nowMonth = now.getMonth(); //当前月
    let nowYear = now.getFullYear(); //当前年
    //本月的开始时间
    let monthStartDate = new Date(nowYear, nowMonth, 1);
    //本月的结束时间
    let monthEndDate = new Date(nowYear, nowMonth + 1, 0);

    defaultTime[0] = monthStartDate;
    defaultTime[1] = monthEndDate;
    return defaultTime;
  };

  render() {
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
        // md: { span: 4 },
        // lg: { span: 2 },
        // xl: { span: 1 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
        // md: { span: 4 },
        // lg: { span: 2 },
        // xl: { span: 1 },
      },
    };
    const colSpan = {
      xs: 24,
      sm: 24,
      md: 24,
      lg: 24,
      xl: 24,
      xxl: 6,
    };
    // const colSpan = {
    //   xs: 12,
    //   sm: 24,
    //   md: 24,
    //   lg: 24,
    //   xl: 24,
    //   xxl: 6,
    // };
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
          let result = {};
          allFields.forEach((field) =>
            Object.defineProperty(result, field.name.toString(), field),
          );
          this.props.getFormValues(result);
        }}
      >
        <Row type={'flex'} gutter={16} wrap={true}>
          <Col {...colSpan}>
            <Form.Item
              label="申请时间"
              name={'applyTime'}
              initialValue={[
                moment(this.defaultStartTime()[0], dateFormat),
                moment(this.defaultStartTime()[1], dateFormat),
              ]}
              format={dateFormat}
            >
              <RangePicker allowClear />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Form.Item label={'钥匙状态'} name="dkState" initialValue={1}>
              <Select
                placeholder="请选择钥匙状态"
                mode={'multiple'}
                style={{ width: 258 }}
                showArrow
                allowClear={true}
              >
                <Select.Option key={0} value={0}>
                  未激活
                </Select.Option>
                <Select.Option key={1} value={1}>
                  激活
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

          <Col {...colSpan}>
            <Form.Item label={'钥匙来源'} name="keyResource" initialValue={1}>
              <Select
                placeholder="请选择钥匙来源"
                // mode={'multiple'}
                style={{ width: 258 }}
                showArrow
                allowClear={true}
              >
                <Select.Option key={1} value={1}>
                  APP
                </Select.Option>
                <Select.Option key={2} value={2}>
                  小程序
                </Select.Option>
              </Select>
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Row gutter={16} type={'flex'} wrap={true}>
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

        <Row hidden={buttonHidden} type={'flex'} gutter={16} wrap={true}>
          <Col {...colSpan}>
            <Form.Item label="生效时间" name={'valFromTime'}>
              <RangePicker />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Form.Item label={'钥匙分类'} name="keyClassification">
              <Select
                placeholder="钥匙分类"
                mode={'multiple'}
                style={{ width: 258 }}
                allowClear={true}
                showArrow
              >
                <Select.Option key={1} value={1}>
                  icce
                </Select.Option>
                <Select.Option key={2} value={2}>
                  ccc
                </Select.Option>
              </Select>
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Form.Item label={'车辆标识符'} name="vin">
              <Input placeholder="请输入车辆标识符" style={{ width: 258 }} />
            </Form.Item>
          </Col>
        </Row>
        <Row hidden={buttonHidden} type={'flex'} gutter={16} wrap={true}>
          <Col {...colSpan}>
            <Form.Item label="失效时间" name={'valToTime'}>
              <RangePicker />
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'钥匙类型'} name="keyType">
              <Select
                placeholder="请选择钥匙类型"
                mode={'multiple'}
                style={{ width: 258 }}
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
            <Form.Item label={'用户id'} name="userId">
              <Input style={{ width: 258 }} placeholder="请输入用户id" />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    );
  }
}

export default SearchForm;
