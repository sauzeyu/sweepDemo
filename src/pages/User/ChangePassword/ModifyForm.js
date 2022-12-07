'use strict';
import React from 'react';
import { Form, Input, Row, Col, Button, Modal } from 'antd';
import style from './style.less';
import { FormattedMessage } from 'umi';
const labels = {
  password: <FormattedMessage id={'Page.changePassword.oldPassword.label'} />,
  newPassword: (
    <FormattedMessage id={'Page.changePassword.newPassword.label'} />
  ),
  confirmPassword: (
    <FormattedMessage id={'Page.changePassword.confirmPassword.label'} />
  ),
};

export default class ModifyForm extends React.Component {
  form = React.createRef();
  submit = (vals) => {
    this.form.current
      .validateFields()
      .then((vals) => {
        // 校验两次密码是否一致
        if (vals.newPassword !== vals.reNewPassword) {
          // 调用ant 的 modal组件
          return Modal.error({
            content: '两次输入密码不一致',
          });
        }
        // 校验新旧密码是否一致
        if (vals.password == vals.newPassword) {
          // 调用ant 的 modal组件
          return Modal.error({
            content: '新密码与旧密码相同，修改失败',
          });
        }
        // 一致的情况下调用父级组件的方法并传递表单数据作为参数
        this.props.onUpdatePassword(vals);
      })
      .catch((err) => {});
  };
  render() {
    /**
     * busy:    父级组件传递的submitting值 默认为 false
     */
    const { busy } = this.props;
    const formItemLayout = {
      // labelCol:{ span: 8 },
      // wrapperCol: { span: 16 }
    };
    return (
      <Form onFinish={this.submit} ref={this.form}>
        {/* 旧密码 */}
        <Form.Item
          {...formItemLayout}
          label="旧密码"
          name="password"
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id={'Validator.required'}
                  values={{ name: labels.password }}
                />
              ),
            },
          ]}
        >
          <Input type="password" maxLength={16} placeholder={'请输入原密码'} />
        </Form.Item>
        {/* 新密码 */}

        <Form.Item
          {...formItemLayout}
          label="新密码"
          name="newPassword"
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id={'Validator.required'}
                  values={{ name: labels.newPassword }}
                />
              ),
            },
            {
              pattern: new RegExp(
                /(?=^.{8,}$)(?=.*\d)(?=.*\W+)(?=.*[A-Z])(?=.*[a-z])(?!.*\n).*$/,
              ),
              // pattern: /^[a-zA-Z0-9~!@#$%^&*()_+-=;':",./<>?`]{6,16}$/,
              message: (
                <FormattedMessage
                  id={'Validator.password'}
                  values={{ name: labels.newPassword }}
                />
              ),
            },
            // {
            //   message: (
            //     <FormattedMessage
            //       id={'Validator.notequals'}
            //       values={{ name: labels.newPassword }}
            //     />
            //   ),
            // },
          ]}
        >
          <Input
            maxLength={16}
            type="password"
            placeholder={
              '密码必须由数字/大写字母/小写字母/特殊字符组成,长度在8位以上'
            }
          />
        </Form.Item>
        {/* 重复输入新密码 */}
        <Form.Item
          {...formItemLayout}
          label="确认密码"
          name="reNewPassword"
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id={'Validator.required'}
                  values={{ name: labels.confirmPassword }}
                />
              ),
            },
          ]}
        >
          <Input
            maxLength={16}
            type="password"
            placeholder={'请重新输入新密码'}
          />
        </Form.Item>
        <Form.Item>
          <Button
            type="primary"
            className={style.submitBtn}
            htmlType="submit"
            size="large"
            loading={busy}
          >
            <FormattedMessage id={'Common.button.apply'} />
          </Button>
        </Form.Item>
      </Form>
    );
  }
}
