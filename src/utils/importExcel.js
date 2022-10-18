import { getDvaApp } from '@@/plugin-dva/exports';
import { message, Modal, Upload } from 'antd';
import { InboxOutlined } from '@ant-design/icons';
import React from 'react';

const { Dragger } = Upload;

function importExcel(url, that) {
  const props = {
    name: 'file',
    // multiple: true,
    action: url,
    headers: {
      'access-token': getDvaApp()._store.getState().user.currentUser.token,
    },
    progress: {
      strokeColor: {
        '0%': '#108ee9',
        '100%': '#87d068',
      },
      strokeWidth: 3,
      format: (percent) => `${parseFloat(percent.toFixed(2))}%`,
    },
    onChange(info) {
      const { status } = info.file;
      if (status !== 'uploading') {
      }
      // if (info?.file?.response?.code === 500) {
      //   info.file.status = 'error';
      //   info.file.response = '文件上传失败';
      // }

      if (status === 'done') {
        const { response } = info.file;
        if (response.code === 200) {
          message.success(`${info.file.name} 文件上传成功.`);
          that?.dataTable?.refresh();
        }
        if (response.code === 500) {
          message.error(response.msg + ` ${info.file.name} 文件上传失败.`);
        }
      }
    },
  };
  return Modal.info({
    width: 500,
    icon: '',
    okText: '取消',
    closable: true,
    content: (
      <Dragger {...props}>
        <p className="ant-upload-drag-icon">
          <InboxOutlined />
        </p>
        <p className="ant-upload-text">单击或拖动文件到此区域上传</p>
        <p className="ant-upload-hint">仅支持 xlsx/xls 格式文件</p>
      </Dragger>
    ),
  });
}

export default importExcel;
