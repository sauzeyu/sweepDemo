import React, { Component } from 'react';
import { Modal, Tag } from 'antd';
import JSONView from 'react-json-view';
import DescriptionList from '@/components/DescriptionList';
const { Description } = DescriptionList;

class RequestInfo extends Component {
  state = {
    visible: false,
  };
  show = () => {
    this.setState({ visible: true });
  };
  hide = () => {
    this.setState({ visible: false });
  };
  renderJson = (data) => {
    if (typeof data === 'string') {
      try {
        const json = JSON.parse(data);
        return (
          <JSONView
            src={json}
            collapsed={1}
            displayObjectSize={false}
            displayDataTypes={false}
          />
        );
      } catch (e) {
        return data;
      }
    }
    return data;
  };
  render() {
    const { visible } = this.state;
    const { data } = this.props;
    return (
      <div>
        <a onClick={this.show}>点击查看</a>
        <Modal
          title={'请求信息'}
          visible={visible}
          onCancel={this.hide}
          onOk={this.hide}
        >
          <DescriptionList col={1}>
            <Description term={'请求地址'}>
              <Tag>{data.httpMethod}</Tag>
              <span>{data.requestUri}</span>
            </Description>
            <Description term={'请求参数'}>
              {this.renderJson(data.params)}
            </Description>
            <Description term={'返回内容'}>
              {this.renderJson(data.response)}
            </Description>
            <Description term={'浏览器'}>{data.browser}</Description>
            <Description term={'异常信息'}>{data.exception}</Description>
          </DescriptionList>
        </Modal>
      </div>
    );
  }
}

export default RequestInfo;
