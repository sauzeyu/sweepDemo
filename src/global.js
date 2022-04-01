// 此文件会在入口文件的最前面被自动引入
import React from 'react';
import { message, Input, Modal } from 'antd';
import SimpleEvent from '@/utils/SimpleEvent';
window.appMeta = APP_METADATA;
window.React = React;

window.addEventListener('unhandledrejection', function (event) {
  if (event.reason && event.reason.type === 'RequestError') {
    event.preventDefault();
  }
});
// 模态窗上扩展prompt 弹出输入组件
Modal.prompt = function (opts) {
  const { inputProps, onOk, content, inputType, ...restProps } = opts;
  let ref = React.createRef();
  const Field = inputType === 'textarea' ? Input.TextArea : Input;
  return Modal.confirm({
    ...restProps,
    content: (
      <div className={'gutter-v_lg'} ref={ref}>
        {content}
        <Field {...inputProps} />
      </div>
    ),
    onOk: () => {
      const value = ref.current.querySelector('.ant-input').value;
      return onOk(value);
    },
  });
};

window.eventCenter = new SimpleEvent();
