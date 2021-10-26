import React from 'react';
import { Button, Icon, notification } from 'antd';
import FileUploader from './Uploader';
import PropTypes from 'prop-types';
import request from '@/utils/request';
import { FormattedMessage } from 'umi';
import styles from './index.less';
import controllable from '@/components/react-controllables';
import { UploaderPropTypes } from './Uploader';
import Viewer from 'react-viewer';

@controllable(['value', 'fileList'])
class ImageUploader extends React.Component {
  static propTypes = {
    ...UploaderPropTypes,
    showAllowFileTypes: PropTypes.bool,
    listType: PropTypes.string,
  };
  static defaultProps = {
    allowFileExt: ['jpg', 'jpeg', 'png'],
    showAllowFileTypes: false,
    fileList: [],
    listType: 'card',
  };
  state = {
    previewVisible: false,
    previewUrl: '',
  };
  handlePreview = (file) => {
    this.setState({
      previewVisible: true,
      previewUrl: file.url,
    });
  };
  closePreview = () => {
    this.setState({
      previewVisible: false,
      previewUrl: '',
    });
  };
  render() {
    const { listType, maxFileCount, multiple, showAllowFileTypes, fileList } =
      this.props;
    let { previewVisible, previewUrl } = this.state;
    const renderAddButton = () => {
      let shouldRender = true;
      if (multiple) {
        if (maxFileCount && fileList.length >= maxFileCount) {
          shouldRender = false;
        }
      } else {
        shouldRender = fileList.length <= 0;
      }
      return shouldRender ? (
        <div>
          <Icon type="plus" style={{ fontSize: 32, color: '#999' }} />
          <div className="ant-upload-text">
            <FormattedMessage id={'Common.message.upload'} />
          </div>
        </div>
      ) : null;
    };
    return (
      <>
        <FileUploader
          {...this.props}
          onPreview={this.handlePreview}
          showUploadList
          showLoading={false}
          listType={listType === 'card' ? 'picture-card' : 'picture'}
        >
          {renderAddButton()}
        </FileUploader>
        <div style={{ clear: 'both' }}>
          {showAllowFileTypes && (
            <div className={styles.fileTypeTip}>
              <FormattedMessage
                id={'Component.fileUploader.allowTypes'}
                values={{ types: this.props.allowFileExt.join(',') }}
              />
            </div>
          )}
        </div>
        <Viewer
          visible={previewVisible}
          activeIndex={0}
          onClose={this.closePreview}
          images={[{ src: previewUrl }]}
        />
      </>
    );
  }
}

export default ImageUploader;
