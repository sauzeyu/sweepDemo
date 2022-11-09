import React, { Component } from 'react';
import { Button, Col, Form, Row, Select, Input, DatePicker, Modal } from 'antd';
import EasyTable from '@/components/EasyTable';
import { Permit } from '@/constants/keys';
import moment from 'moment';
import { DownOutlined, UpOutlined } from '@ant-design/icons';
import { Code } from '@/constants/statusCode';
const { RangePicker } = DatePicker;
const dateFormat = 'YYYY/MM/DD';
const { Option } = Select;
const children = [];
@EasyTable.connect(({ keyErrorLogDataTable }) => ({
  keyErrorLogDataTable,
}))
class SearchForm extends Component {
  state = {
    isExpand: true,
    buttonHidden: true,
    flag: true,
  };
  // componentDidMount() {
  //   this.fetchRoles();
  // }

  form = React.createRef();
  handleSubmit = (values) => {
    this.form.current
      .validateFields()
      .then((values) => {
        if (values.startTime) {
          const startTime = values['startTime'];
          values.startTime = moment(startTime[0]).format('YYYY-MM-DD 00:00:00');
          values.endTime = moment(startTime[1])
            .add(1, 'days')
            .format('YYYY-MM-DD 00:00:00');
        }
        this.props.keyErrorLogDataTable.fetch(values);
      })
      .catch((errors) => {
        if (errors) return;
      });
  };
  // componentDidMount() {
  //   //
  // }
  // componentWillUnmount() {}

  componentDidUpdate(prevProps, prevState, snapshot) {
    //   this.props.keyErrorLogDataTable,
    //   'this.props.keyErrorLogDataTable',
    //   prevProps,
    //   prevState,
    // );

    if (this.props.keyErrorLogDataTable && this.state.flag) {
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
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
    };
    const colSpan = {
      xs: 24,
      sm: 24,
      md: 24,
      lg: 6,

      // xs: 24,
      // sm: 6,
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
    for (let i = 0; i < Code.length; i++) {
      children.push(<Option key={Code[i][0]}> {Code[i][1]}</Option>);
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
              label="操作时间"
              name={'startTime'}
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
            <Row gutter={16} wrap={true}>
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
        <Row type={'flex'} gutter={16} hidden={buttonHidden} wrap={true}>
          <Col {...colSpan}>
            <Form.Item label="操作类型" name={'statusCode'}>
              <Select
                mode="multiple"
                allowClear
                style={{
                  width: '100%',
                }}
                placeholder="请输入操作类型"
              >
                {children}
              </Select>
            </Form.Item>
          </Col>
          <Col {...colSpan}>
            <Form.Item label={'车辆vin号'} name="vin">
              <Input placeholder="请输入车辆vin号" />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Form.Item label={'操作结果'} name="flag">
              <Select
                // mode={'multiple'}
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
        <Row type={'flex'} gutter={16} hidden={buttonHidden} wrap={true}>
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

        <Row type={'flex'} gutter={16} hidden={buttonHidden} wrap={true}>
          <Col {...colSpan}>
            <Form.Item label={'车型'} name="vehicleType">
              <Input placeholder="请输入车型" />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    );
  }
}

export default SearchForm;
