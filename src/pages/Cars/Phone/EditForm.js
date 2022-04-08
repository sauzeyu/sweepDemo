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
    console.log(data);
    this.setState({ loading: true });
    getVehicleDetail(data.id)
      .then(
        (vehicle) => {
          console.log(vehicle);
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
    // this.form.current.setFieldsValue({
    //   vin: type.vinMatch,
    // });
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
            ]}
          >
            <RestrictiveInput trim disabled={isEdit} maxLength={17} />
          </Form.Item>
          <Row type={'flex'}>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'车身颜色'}
                name="colour"
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
                label={'车主手机号'}
                name="phone"
              >
                <RestrictiveInput trim maxLength={11} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'车牌号'}
                name="license"
              >
                <RestrictiveInput trim maxLength={10} />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item label={'蓝牙'} name="bluetooth">
            <RestrictiveInput trim maxLength={10} />
          </Form.Item>
          <Form.Item label={'车主身份证号'} name="ownerID">
            <RestrictiveInput trim maxLength={18} />
          </Form.Item>
        </Form>
      </Spin>
    );
  }
}

export default EditForm;
