import React from 'react';
import { Input } from 'antd';
import PropTypes from 'prop-types';

const RestrictiveInput = (props) => {
  /**
   * onChange 函数
   * trim     true
   * noSpace  undefined
   * type     undefined
   * rest     剩余属性 放入 rest中组成对象
   */
  const { onChange, trim, noSpace, type, ...rest } = props;
  const onChangeEvent = (evt) => {
    const value = evt.currentTarget?.value;
    if (value && trim) {
      evt.currentTarget.value = value.trim();
    }
    onChange?.(evt);
  };
  return type === 'textarea' ? (
    <Input.TextArea {...rest} onChange={onChangeEvent} />
  ) : (
    <Input {...rest} onChange={onChangeEvent} />
  );
};

RestrictiveInput.propTypes = {
  trim: PropTypes.bool,
  noSpace: PropTypes.bool,
  type: PropTypes.string,
};

export default RestrictiveInput;
