'use strict';
import React from 'react';
import ModifyForm from './ModifyForm';
import style from './style.less';
import { connect } from 'dva';
import { message, Modal } from 'antd';
import { FormattedMessage } from 'umi';
@connect(({ user, loading }) => ({
  currentUser: user.currentUser,
  submitting: loading.effects['user/modifyPassword'],
}))
export default class extends React.Component {
  // 在子组件中调用函数 传递子组件中获得的表单上数据作为参数
  updatePassword = (vals) => {
    // 发送请求
    this.props
      .dispatch({
        type: 'user/modifyPassword',
        payload: {
          params: {
            username: this.props.currentUser.username,
            ...vals,
          },
        },
      })
      .then(
        () => {
          // 成功
          Modal.success({
            title: '密码修改成功',
            onOk: () => {
              this.modifyForm.form.current.resetFields();
              this.props.dispatch({
                type: 'user/logout',
              });
            },
          });
        },
        (err) => {
          // 失败
          Modal.error({
            content: err.message,
          });
        },
      );
  };
  render() {
    const { submitting = false } = this.props;
    return (
      <div>
        <div className={style.form}>
          <div>
            <ModifyForm
              ref={(ref) => (this.modifyForm = ref)}
              busy={submitting}
              onUpdatePassword={this.updatePassword}
            />
          </div>
        </div>
      </div>
    );
  }
}
