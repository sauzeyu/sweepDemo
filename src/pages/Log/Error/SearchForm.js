import React, { Component } from 'react';
import { Button, Col, Form, Row, Select, Input, DatePicker, Modal } from 'antd';
import EasyTable from '@/components/EasyTable';
import moment from 'moment';
import { DownOutlined, UpOutlined } from '@ant-design/icons';
import { keyStatusCode } from '@/constants/statusCode';

const { RangePicker } = DatePicker;
const dateFormat = 'YYYY/MM/DD';

@EasyTable.connect(({ keyErrorLogDataTable }) => ({
  keyErrorLogDataTable,
}))
class SearchForm extends Component {
  state = {
    isExpand: true,
    buttonHidden: true,
    flag: true,
    businessList: null,
  };

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
  selectBusiness = () => {
    this.props
      .dispatch({
        type: 'Diagnosis/selectBusiness',
        payload: {},
      })
      .then((res) => {
        if (res && res.code === 200) {
          this.setState({
            businessList: res.data,
          });
        }
      });
  };

  componentDidMount() {
    this.selectBusiness();
  }
  componentDidUpdate(prevProps, prevState, snapshot) {
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

  render() {
    const { businessList } = this.state;
    let selectOptions = null;
    if (businessList) {
      selectOptions = businessList.map((business) => {
        return (
          <Select.Option key={business.businessId} value={business.businessId}>
            {business.business}
          </Select.Option>
        );
      });
    }
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
          let result = {};
          allFields.forEach((field) =>
            Object.defineProperty(result, field.name.toString(), field),
          );
          this.props.getFormValues(result);
        }}
      >
        <Row type={'flex'} gutter={16} wrap={true}>
          <Col {...colSpan}>
            <Form.Item label="用户ID" name={'userId'}>
              <Input placeholder="请输入用户ID" />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Form.Item label="车辆VIN号" name={'vin'}>
              <Input placeholder="请输入车辆VIN号" />
            </Form.Item>
          </Col>

          <Col {...colSpan}>
            <Form.Item label="记录时间" name={'startTime'} format={dateFormat}>
              <RangePicker allowClear />
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
            <Form.Item label="业务类型" name={'businessId'}>
              <Select
                allowClear
                style={{
                  width: '100%',
                }}
                placeholder="请输入操作类型"
              >
                {selectOptions}
              </Select>
            </Form.Item>
          </Col>
        </Row>
      </Form>
    );
  }
}

export default SearchForm;
