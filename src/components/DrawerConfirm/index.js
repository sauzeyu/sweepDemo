'use strict';
import React, { Fragment } from 'react';
import { Button, Drawer } from 'antd';

import './index.less';
import PropTypes from 'prop-types';
import classnames from 'classnames';

export default class DrawerConfirm extends React.Component {
  static defaultProps = {
    closable: false,
  };
  static propTypes = {
    onCancel: PropTypes.func,
    onOk: PropTypes.func,
    confirmLoading: PropTypes.bool,
    cancelText: PropTypes.any,
    okText: PropTypes.any,
    okType: PropTypes.string,
    okButtonProps: PropTypes.object,
    cancelButtonProps: PropTypes.object,
    footer: PropTypes.any,
    hiddenOk: PropTypes.bool,
    extraFooter: PropTypes.any,
    ...Drawer.propTypes,
  };

  render() {
    const {
      children,
      onCancel,
      onOk,
      confirmLoading = false,
      className,
      cancelText = '取消',
      okText = '确定',
      okType = 'primary',
      okButtonProps,
      cancelButtonProps,
      hiddenOk = false,
      footer,
      extraFooter,
      ...restProps
    } = this.props;
    return (
      <Drawer
        {...restProps}
        className={classnames(className, 'comp-DrawerConfirm')}
        onClose={onCancel}
      >
        <div className={'comp-DrawerConfirm_content'}>{children}</div>
        {footer !== false && (
          <div className={'comp-DrawerConfirm_footer'}>
            {footer ? (
              footer
            ) : (
              <Fragment>
                <div className={'pull-left'}>{extraFooter}</div>
                <Button {...cancelButtonProps} onClick={onCancel}>
                  {cancelText}
                </Button>
                <Button
                  type={okType}
                  loading={confirmLoading}
                  {...okButtonProps}
                  hidden={hiddenOk}
                  onClick={onOk}
                >
                  {okText}
                </Button>
              </Fragment>
            )}
          </div>
        )}
      </Drawer>
    );
  }
}
