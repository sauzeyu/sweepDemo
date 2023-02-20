'use strict';
import React, { Component } from 'react';
import { Modal, Form, DatePicker, Input, message } from 'antd';
import moment from 'moment';
import { removeEmptyProperty } from '@/utils';
import { saveOrUpdateConfigExpired } from '@/services/systemLogs';
import { getDvaApp } from '@@/plugin-dva/exports';
const { RangePicker } = DatePicker;
export default class EditForm extends Component {
  editForm = React.createRef();

  constructor(props) {
    super(props);
    this.state = {
      selectedKeys: [],
      validityPeriod: undefined,
    };
  }

  componentDidMount() {
    this.props.editFormRef && this.props.editFormRef(this);
  }

  handleSubmit = (values) => {
    this.editForm.current.validateFields().then((values) => {
      values.username =
        getDvaApp()._store.getState()?.user?.currentUser?.username;
      values.operateTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
      // values.validityPeriod = this.state.validityPeriod;
      saveOrUpdateConfigExpired(values).then(
        (res) => {
          if (res.code === 200) {
            message.success(res.msg);
          } else {
            message.error(res.msg);
          }
        },
        (res) => {
          message.error(res.msg);
        },
      );
      this.setState({
        selectedKeys: [],
      });
    });
  };

  render() {
    const { visible, onCancel } = this.props;

    const onFinish = (values) => {
      console.log(values); // 提交表单的回调函数
      debugger;
    };

    return (
      <Form ref={this.editForm} onFinish={this.handleSubmit}>
        <Form.Item
          name="validityPeriod"
          label="钥匙使用记录保存时间"
          rules={[{ required: true, message: '请输入钥匙使用记录保存时间' }]}
        >
          <Input />
        </Form.Item>
      </Form>
    );
  }
}
