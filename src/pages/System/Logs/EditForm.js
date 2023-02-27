'use strict';
import React, { Component } from 'react';
import { Modal, Form, DatePicker, Input, message, InputNumber } from 'antd';
import moment from 'moment';
import { removeEmptyProperty } from '@/utils';
import {
  saveOrUpdateConfigExpired,
  selectForLast,
} from '@/services/systemLogs';
import { getDvaApp } from '@@/plugin-dva/exports';
const { RangePicker } = DatePicker;
export default class EditForm extends Component {
  editForm = React.createRef();

  constructor(props) {
    super(props);
    this.state = {
      selectedKeys: [],
      validityPeriod: {},
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
      saveOrUpdateConfigExpired(values).then((res) => {
        if (res.code === 200) {
          message.success(res.msg);
        } else {
          message.error(res.msg);
        }
      });
      this.setState({
        selectedKeys: [],
      });
    });
  };
  changeSubmit = () => {
    this.props.finish(true);
  };
  render() {
    const { visible, onCancel, validityPeriod } = this.props;

    return (
      <Form
        ref={this.editForm}
        onFinish={this.handleSubmit}
        onFinishFailed={this.changeSubmit}
      >
        <Form.Item
          name="validityPeriod"
          label="钥匙使用记录保存时间"
          rules={[{ required: true, message: '请输入钥匙使用记录保存时间' }]}
        >
          <InputNumber
            addonAfter="月"
            defaultValue={validityPeriod}
            min={1}
            max={100000}
          />
        </Form.Item>
      </Form>
    );
  }
}
