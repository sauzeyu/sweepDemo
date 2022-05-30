import React, { Component } from 'react';
import { Col, Form, Input, message, Row, Spin } from 'antd';
import RestrictiveInput from '@/components/RestrictiveInput';

class EditForm extends Component {
  form = React.createRef();
  state = {
    vinMatch: '',
    loading: false,
    isEdit: false,
  };
  initDetailData = (data) => {
    this.setState({ loading: false });
    this.form.current.setFieldsValue(data);
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
            label={'工厂编号'}
            name="factoryNo"
            rules={[{ required: true, message: '工厂编号不能为空' }]}
          >
            <RestrictiveInput trim />
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
          <Form.Item label={'发动机号'} name={'ven'}>
            <Input trim />
          </Form.Item>
          <Row type={'flex'}>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'车身颜色'}
                name="color"
              >
                <RestrictiveInput
                  trim
                  maxLength={10}
                  placeholder={'比如：蓝色'}
                />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'天窗类型'}
                name="sunroofType"
              >
                <RestrictiveInput trim maxLength={10} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'后备门类型'}
                name="trunkType"
              >
                <RestrictiveInput trim maxLength={10} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'车窗类型'}
                name="windowType"
              >
                <RestrictiveInput trim maxLength={10} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                {...formItemLayoutDouble}
                label={'遥控泊车'}
                name="RPAType"
              >
                <RestrictiveInput trim maxLength={10} />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item
            label={'蓝牙编号'}
            name="hwDeviceSn"
            rules={[{ required: true, message: '蓝牙编号不能为空' }]}
          >
            <RestrictiveInput trim maxLength={10} />
          </Form.Item>
        </Form>
      </Spin>
    );
  }
}

export default EditForm;
