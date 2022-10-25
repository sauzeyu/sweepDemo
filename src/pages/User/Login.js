import React, { Component } from 'react';
import { connect } from 'dva';
import { Checkbox, Form, Input, Spin, Button, message } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import styles from './Login.less';
import { FormattedMessage, getLocale, setLocale } from 'umi';
import RestrictiveInput from '@/components/RestrictiveInput';
// import router from 'umi/router';
import { history } from 'umi';
import Captcha from './Captcha';
@connect(({ user, loading }) => ({
  user,
  submitting: loading.effects['user/login'],
}))
export default class LoginPage extends Component {
  constructor(props) {
    super(props);
    // 获取本地的用户
    const lastUser = localStorage.getItem('last_username');
    this.state = {
      // 是否存在用户
      saveUsername: !!lastUser,
      // 用户名
      lastUsername: lastUser,
      // 验证码
      needCaptcha: false,
    };
  }
  form = React.createRef();
  // form表单提交事件
  handleSubmit = () => {
    // 调用登陆事件
    this.login();
  };
  /**
   * 登陆
   */
  login = async () => {
    /**
     * 触发校验
     * err      错误
     * values   字段:值
     */

    let values = await this.form.current.validateFields();
    // 发送请求
    this.props
      .dispatch({
        type: 'user/login',
        payload: values,
      })
      .then(
        (user) => {
          // 登陆成功
          /* 是否保存用户名 */
          if (this.state.saveUsername) {
            localStorage.setItem('last_username', values.username);
          } else {
            localStorage.removeItem('last_username');
          }
          // 获取当前语言
          const currentLocale = getLocale();
          if (user.language && currentLocale !== user.language) {
            setLocale(user.language);
          }
          // 如果是初始密码则修改密码
          // if (!user.changedPWD) {
          //   message.info('请修改您的密码');
          //   history.replace('/user/changePassword');
          // } else {
          this.goHome();
          // }
        },
        (e) => {
          // 登陆失败
          const data = e.data || {};
          if (this.captcha) {
            this.captcha.refresh();
          }
          // message.error(e.message);
          // this.setState({ needCaptcha: data.validateCode != -1 });
        },
      );
  };

  goHome() {
    // 回到主页
    let {
      query: { r: redirect },
    } = this.props.location;
    // if(isUrl(redirect)){
    //     window.location.replace(redirect);
    // }else {
    history.replace('/');
    // }
  }
  // 勾选确定/取消记住用户
  changeSaveUsername = (e) => {
    this.setState({
      saveUsername: e.target.checked,
    });
  };

  render() {
    /**
     * 第一次打印为 submitting 值为 undefined
     * 触发 submitting的默认值
     */
    const { submitting = false } = this.props;
    /**
     * lastUsername: undefined
     * needCaptcha:false
     */
    const { lastUsername, needCaptcha } = this.state;
    return (
      <div className={styles.login}>
        <Spin spinning={submitting}>
          <Form onFinish={this.handleSubmit} ref={this.form}>
            <Form.Item
              name="username"
              initialValue={lastUsername || ''}
              rules={[
                {
                  required: true,
                  message: '请填写用户名',
                },
              ]}
            >
              <RestrictiveInput
                trim
                prefix={<UserOutlined />}
                size={'large'}
                placeholder={'用户名'}
              />
            </Form.Item>
            <Form.Item
              name="password"
              rules={[
                {
                  required: true,
                  message: '请填写密码',
                },
              ]}
            >
              <Input
                prefix={<LockOutlined />}
                type={'password'}
                size={'large'}
                placeholder={'密码'}
              />
            </Form.Item>
            {/* 是否需要验证码 false */}
            {needCaptcha && (
              <Form.Item
                name="code"
                rules={[{ required: true, message: '请输入图片中的验证码' }]}
              >
                <Captcha
                  placeholder={'验证码'}
                  ref={(ref) => (this.captcha = ref)}
                />
              </Form.Item>
            )}
            <div>
              {/* 记住账号选择框 默认 false */}
              <Checkbox
                checked={this.state.saveUsername}
                onChange={this.changeSaveUsername}
              >
                <FormattedMessage id={'Page.login.rememberAccount'} />
              </Checkbox>
              {/*<a style={{float: 'right'}} onClick={this.forgotPassword}>
                            <FormattedMessage id={'Page.login.resetPassword'}/>
                        </a>*/}
            </div>
            <Button
              type={'primary'}
              size={'large'}
              htmlType={'submit'}
              className={styles.submit}
            >
              确定
            </Button>
          </Form>
        </Spin>
      </div>
    );
  }
}
