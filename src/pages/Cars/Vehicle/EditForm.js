import React, { Component } from 'react';
import {
  Col,
  DatePicker,
  Form,
  Input,
  InputNumber,
  message,
  Row,
  Select,
  Spin,
  Switch,
} from 'antd';
import { CarTypesSelect } from '@/components/BasicDataSelect';
import { DriveModes, FuelTypes } from '@/constants/cars';
import FormItemInterceptor from '@/components/FormItemInterceptor';
import { getVehicleDetail } from '@/services/cars';
import RestrictiveInput from '@/components/RestrictiveInput';

class EditForm extends Component {
  form = React.createRef();
  state = {
    vinMatch: '',
    loading: false,
    isEdit: false,
  };
  initDetailData = (data) => {
    this.setState({ loading: true });
    getVehicleDetail(data.id)
      .then(
        (vehicle) => {
          vehicle.region = [];
          if (vehicle.modelId) {
            vehicle.modelId = {
              key: vehicle.modelId,
              // label: vehicle.modelName,
            };
          }
          this.form.current.setFieldsValue(vehicle);
        },
        (err) => {
          message.error(err.message);
        },
      )
      .finally(() => {
        this.setState({ loading: false });
      });
  };
  onRegionChange = (ids, values = []) => {
    const province = values[0],
      city = values[1];
    this.form.current.setFieldsValue({
      provinceId: province.value,
      province: province.label,
      cityId: city.value,
      city: city.label,
    });
  };
  handleCarTypeChange = (value, type, c) => {
    console.log(type.vinMatch);
    this.form.current.setFieldsValue({
      vin: type.vinMatch,
    });
    this.setState({
      vinMatch: type.vinMatch,
    });
  };
  componentDidMount() {
    this.props.editFormRef && this.props.editFormRef(this);
    this.props.wrappedComponentRef && this.props.wrappedComponentRef(this);
    this.setState({
      isEdit: !!this.form.current.getFieldValue('id'),
    });
  }
  render() {
    const { loading } = this.state;
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 19 },
      },
    };
    const formItemLayoutDouble = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 10 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 14 },
      },
    };
    const isEdit = this.state.isEdit;
    return (
      <Spin spinning={loading}>
        <Form {...formItemLayout} ref={this.form}>
          <Form.Item name="id" hidden>
            <Input type={'hidden'} />
          </Form.Item>
          <Form.Item
            label={'车型'}
            name="modelId"
            rules={[{ required: true, message: '车型不能为空' }]}
          >
            <CarTypesSelect
              labelInValue
              disabled={isEdit}
              allowClear={false}
              onChange={this.handleCarTypeChange}
            />
          </Form.Item>
          <Form.Item
            label={'VIN'}
            name="vin"
            rules={[
              { required: true, message: 'VIN不能为空' },
              {
                type: 'string',
                min: 17,
                max: 17,
                message: '请输入17位长度的VIN',
              },
              {
                validator: (rule, value) => {
                  if (!value || !this.state.vinMatch || value.length !== 17)
                    return Promise.resolve(true);
                  if (value.startsWith(this.state.vinMatch)) {
                    return Promise.resolve();
                  }
                  return Promise.reject();
                  // return Promise.resolve(value.startsWith(this.state.vinMatch));
                },
                message: '输入的VIN与车型不匹配',
              },
            ]}
          >
            <RestrictiveInput trim disabled={isEdit} maxLength={17} />
          </Form.Item>
          <Form.Item label={'车辆名称'} name="name">
            <RestrictiveInput trim maxLength={50} />
          </Form.Item>
          <Form.Item label={'底盘型号'} name="chassisModel">
            <RestrictiveInput trim maxLength={50} />
          </Form.Item>
          <Form.Item label={'发动机型号'} name="engineModel">
            <RestrictiveInput trim maxLength={50} />
          </Form.Item>
          <Row type={'flex'}>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'驱动方式'}
                name="driveMode"
              >
                <FormItemInterceptor pipes={FormItemInterceptor.Pipes.String}>
                  <Select allowClear>
                    {Object.keys(DriveModes).map((key) => (
                      <Select.Option value={key} key={key}>
                        {DriveModes[key]}
                      </Select.Option>
                    ))}
                  </Select>
                </FormItemInterceptor>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item {...formItemLayoutDouble} label={'功率'} name="power">
                <RestrictiveInput trim maxLength={50} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'排量'}
                name="displacement"
              >
                <RestrictiveInput trim maxLength={50} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'燃料种类'}
                name="fuelType"
              >
                <FormItemInterceptor pipes={FormItemInterceptor.Pipes.String}>
                  <Select allowClear>
                    {Object.keys(FuelTypes).map((key) => (
                      <Select.Option value={key} key={key}>
                        {FuelTypes[key]}
                      </Select.Option>
                    ))}
                  </Select>
                </FormItemInterceptor>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'车身颜色'}
                name="color"
              >
                <RestrictiveInput
                  trim
                  maxLength={50}
                  placeholder={'比如：蓝色'}
                />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'座位数'}
                name="seatCount"
                initialValue={5}
              >
                <InputNumber min={1} max={100} style={{ width: '100%' }} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'出厂日期'}
                name="outDate"
              >
                <FormItemInterceptor
                  pipes={FormItemInterceptor.Pipes.DateString('YYYY-MM-DD')}
                >
                  <DatePicker style={{ width: '100%' }} />
                </FormItemInterceptor>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'车主姓名'}
                name="ownerName"
              >
                <RestrictiveInput trim maxLength={10} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'车主手机号'}
                name="ownerPhone"
              >
                <RestrictiveInput trim maxLength={11} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'车牌号'}
                name="licenseNumber"
              >
                <RestrictiveInput trim maxLength={10} />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item label={'车主身份证号'} name="ownerCard">
            <RestrictiveInput trim maxLength={18} />
          </Form.Item>
          <Form.Item label={'备注'} name="remark">
            <Input.TextArea maxLength={200} />
          </Form.Item>
        </Form>
      </Spin>
    );
  }
}

export default EditForm;
