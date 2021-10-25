import React, { Component } from 'react';
import request from '@/utils/request';
import { Col, Input, Row, Spin } from 'antd';
import { SafetyOutlined } from '@ant-design/icons';
import styles from './Login.less';

function genCaptcha() {
  return request.getAbsUrl('genCaptcha') + '?_=' + Date.now();
}

class Captcha extends Component {
  state = {
    src: '',
    loading: false,
  };
  componentDidMount() {
    this.refresh();
  }
  refresh = () => {
    this.setState({ loading: true, src: genCaptcha() });
  };
  hideLoading = () => {
    this.setState({ loading: false });
  };
  render() {
    const { src, loading } = this.state;
    const { value, onChange, placeholder } = this.props;
    return (
      <Row gutter={8} type={'flex'}>
        <Col span={16} className={styles.captchaInputCol}>
          <Input
            value={value}
            onChange={onChange}
            prefix={<SafetyOutlined />}
            size={'large'}
            placeholder={placeholder}
          />
        </Col>
        <Col span={8} className={styles.captchaImgCol}>
          <Spin spinning={loading}>
            <img
              src={src}
              alt={'验证码'}
              title={'点击可刷新'}
              role="button"
              onClick={this.refresh}
              onLoad={this.hideLoading}
              onError={this.hideLoading}
              className={styles.captchaImg}
            />
          </Spin>
        </Col>
      </Row>
    );
  }
}

export default Captcha;
