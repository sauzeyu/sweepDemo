import React from 'react';
import { Input, Spin, Row, Col, Button, Alert, Modal } from 'antd';
import EditForm from './EditForm';
import EditTree from './EditTree';
const Search = Input.Search;
import classNames from 'classnames';
import style from './style.less';
import { FormattedMessage, formatMessage } from 'umi';

export default class extends React.Component {
  state = {
    error: null,
    filter: '',
    changed: false,
  };
  componentDidMount() {
    this.fetchPermissions();
  }
  dispatch = (opts) => {
    opts.type = 'permission/' + opts.type;
    return this.props.dispatch(opts);
  };
  fetchPermissions() {
    this.dispatch({
      type: 'fetch',
    }).catch((e) => {
      this.setState({ error: e.message });
    });
  }
  onFilterChange = (evt) => {
    const value = evt.target.value;
    this.setState({ filter: value });
  };
  handleTreeSelect = (dna) => {
    if (null == dna) return;
    const currPer = this.props.permission.selectedPermission;
    const select = () => {
      this.dispatch({
        type: 'selectPermission',
        payload: {
          dna,
        },
      });
    };
    if (currPer) {
      this.editForm.form.current
        .validateFields()
        .then((values) => {
          this.dispatch({
            type: 'updateCurrentPermission',
            payload: {
              values,
            },
          });
          select();
        })
        .catch(() => {
          Modal.error({
            title: formatMessage({ id: 'Common.validateError' }),
            content: formatMessage({
              id: 'Page.system.permissions.formInvalidMessage',
            }),
          });
        });
    } else {
      select();
    }
  };
  update = () => {
    const submit = () => {
      this.dispatch({
        type: 'savePermissions',
        payload: {
          permissions: this.props.permission.permissions,
          deleteMenus: this.props.permission.deleted,
        },
      }).then(
        () => {
          this.fetchPermissions();
        },
        (err) => {
          Modal.error({
            title: formatMessage({ id: 'Common.message.error' }),
            content: err.message,
          });
        },
      );
    };
    const { selectedPermission } = this.props.permission;
    if (selectedPermission) {
      this.editForm.form.current
        .validateFields()
        .then((values) => {
          this.dispatch({
            type: 'updateCurrentPermission',
            payload: {
              values,
            },
          });
          submit();
        })
        .catch((error) => {
          Modal.error({
            title: formatMessage({ id: 'Common.validateError' }),
            content: formatMessage({
              id: 'Page.system.permissions.formInvalidMessage',
            }),
          });
        });
    } else {
      submit();
    }
  };
  handleFormChange = () => {
    this.setState({ changed: true });
  };
  handleTreeChange = (type, data) => {
    const { dispatch } = this;
    switch (type) {
      case 'drop':
        dispatch({
          type: 'sortPermissions',
          payload: {
            info: data,
          },
        });
        break;
      case 'del':
        dispatch({
          type: 'delPermission',
          payload: {
            permission: data,
          },
        });
        break;
      case 'insert':
        dispatch({
          type: 'insertPermission',
          payload: {
            permission: data.permission,
            level: data.level,
          },
        });
    }
    this.setState({ changed: true });
  };
  exportsJSON = (evt) => {
    const { permission } = this.props;
    evt.currentTarget.download = 'p_' + Date.now() + '.json';
    evt.currentTarget.href =
      'data:text/plain,' + JSON.stringify(permission.permissions);
  };
  importJSON = (evt) => {
    const files = evt.currentTarget.files;
    if (!files || !files.length) return;
    const file = files[0];
    if (window.FileReader) {
      const reader = new FileReader();
      reader.onload = (evt) => {
        let json = null;
        try {
          json = JSON.parse(reader.result);
        } catch (e) {
          Modal.error({
            title: formatMessage({ id: 'Common.validateError' }),
            content: formatMessage({
              id: 'Page.system.permissions.importFileError',
            }),
          });
        }
        if (json) {
          console.log('import', json);
          loop(json, (item) => {
            item.local_added = true;
          });
          this.dispatch({
            type: 'updatePermissions',
            payload: {
              permissions: json,
              selectedPermission: null,
            },
          });
          this.setState({ changed: true });
        }

        function loop(data, callback) {
          data.forEach((item, index, arr) => {
            callback(item, index, arr);
            if (item.children) {
              return loop(item.children, callback);
            }
          });
        }
      };
      reader.readAsText(file);
    } else {
      Modal.error({
        content: formatMessage({ id: 'Common.unsupported.fileReader' }),
      });
    }
  };
  showImport = () => {
    const fileEl = this.importFile;
    fileEl.value = '';
    fileEl.click();
  };
  exportSQL = (evt) => {
    const {
      permission: { permissions },
    } = this.props;
    let vals = [];
    const loop = (data, level, parent) => {
      data.forEach((item) => {
        vals.push([
          item.id,
          item.title,
          parent ? parent.id : null,
          level,
          null,
          null,
          item.href,
          null,
          item.icon,
          null,
          1,
          '',
          null,
          null,
          null,
          null,
          '',
          0,
          item.dna,
          item.type,
        ]);
        if (item.children && item.children.length > 0) {
          loop(item.children, level + 1, item);
        }
      });
    };
    loop(permissions, 0);
    let SQL = 'INSERT  IGNORE INTO `sys_menu` VALUES ';
    const arr = [];
    vals.forEach((item) => {
      const str = JSON.stringify(item);
      arr.push(str.replace(/^\[/, '(').replace(/]$/, ')'));
    });
    SQL += arr.join(',');
    evt.currentTarget.download = 'menu_' + Date.now() + '.txt';
    evt.currentTarget.href = 'data:text/plain,' + SQL;
  };
  render() {
    const { filter, changed } = this.state;
    const { permission, loading } = this.props;
    return (
      <div>
        {this.state.error ? (
          <Alert
            message={formatMessage({ id: 'Common.message.error' })}
            description={this.state.error}
            type="error"
            showIcon
          />
        ) : (
          <Spin spinning={loading}>
            <div className={style.permissionsHeader}>
              <Row>
                <Col span={12}>
                  <Search
                    style={{ width: 230 }}
                    value={filter}
                    placeholder={formatMessage({
                      id: 'Page.system.permissions.searchPlaceholder',
                    })}
                    onChange={this.onFilterChange}
                  />
                </Col>
                <Col span={12}>
                  <div className="text-right gutter-right-lg">
                    {/*<Button icon='download' className='gutter-right'
                                            onClick={this.showImport}>
                                        导入JSON
                                        <input type="file" ref={ref=>this.importFile=ref} style={{display:'none'}}
                                               onChange={this.importJSON}/>
                                    </Button>
                                    <a className={'gutter-right'} onClick={this.exportsJSON}>
                                        <Button icon='upload'>
                                            导出JSON
                                        </Button>
                                    </a>*/}
                    {/*<a className={'gutter-right'} onClick={this.exportSQL}>
                                        <Button>导出SQL</Button>
                                    </a>*/}
                    <Button
                      type="primary"
                      disabled={!changed}
                      onClick={this.update}
                    >
                      <FormattedMessage id={'Common.message.save'} />
                    </Button>
                  </div>
                </Col>
              </Row>
            </div>
            <Row gutter={5} className={style.roleRow}>
              <Col span={10}>
                <EditTree
                  permission={permission}
                  filter={filter}
                  onSelect={this.handleTreeSelect}
                  onChange={this.handleTreeChange}
                />
              </Col>
              <Col span={14}>
                <div
                  className={classNames(style.editForm, {
                    hidden: !permission.selectedPermission,
                  })}
                >
                  <div className={style.editFormTitle}>
                    <FormattedMessage
                      id={'Page.system.permissions.formTitle'}
                    />
                  </div>
                  <EditForm
                    permission={permission}
                    ref={(ref) => (this.editForm = ref)}
                    onChange={this.handleFormChange}
                  />
                </div>
              </Col>
            </Row>
          </Spin>
        )}
      </div>
    );
  }
}
