'use strict';
import React from 'react';
import { FormattedMessage } from 'umi';
import {
  Alert,
  Card,
  Col,
  Icon,
  message,
  Modal,
  notification,
  Row,
  Spin,
} from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { connect } from 'dva';
import {
  SYSTEM_ROLE_ADD,
  SYSTEM_ROLE_DELETE,
  SYSTEM_ROLE_UPDATE,
} from '@/components/Authorized/AuthMap';
import roleStyle from './style.less';
import Authorized from '@/components/Authorized';
import DrawerConfirm from '@/components/DrawerConfirm';
import EditForm from './EditForm';

@connect(({ role, loading }) => ({
  role,
  fetching: loading.effects['role/fetch'],
  upserting:
    loading.effects['role/updateRole'] || loading.effects['role/addRole'],
}))
export default class extends React.Component {
  state = {
    error: null,
    editFormVisible: false,
    currentRole: null,
  };
  componentDidMount() {
    this.loadRoles();
  }
  componentWillUnmount() {
    this.props.dispatch({
      type: 'role/reset',
    });
  }
  loadRoles() {
    this.props
      .dispatch({
        type: 'role/fetch',
      })
      .catch((e) => {
        this.setState({
          error: e.message,
        });
      });
  }
  showRoleEditModal(role) {
    this.setState({ editFormVisible: true, currentRole: role });
  }
  editRoleDone() {
    this.editForm.getValues().then((values) => {
      this.props
        .dispatch({
          type: 'role/upsert',
          payload: values,
        })
        .then(
          () => {
            message.success('操作成功');
            this.closeRoleEditModal();
          },
          (e) => {
            notification.error({
              message: '错误',
              description: e.message,
            });
          },
        );
    });
  }
  deleteRole = (role) => {
    Modal.confirm({
      title: '确定删除？',
      onOk: () => {
        return this.props
          .dispatch({
            type: 'role/deleteRole',
            payload: role.id,
          })
          .then(
            () => {
              this.loadRoles();
            },
            (err) => {
              message.error(err.message);
            },
          );
      },
    });
  };
  closeRoleEditModal() {
    this.setState({ editFormVisible: false, currentRole: null });
  }
  renderFooter(role) {
    return (
      <div className="footer">
        <div className="link-group_dark">
          <Authorized route={SYSTEM_ROLE_UPDATE}>
            <a onClick={() => this.showRoleEditModal(role)}>编辑</a>
          </Authorized>
          <Authorized route={SYSTEM_ROLE_DELETE}>
            <a onClick={() => this.deleteRole(role)}>删除</a>
          </Authorized>
        </div>
      </div>
    );
  }
  render() {
    const { error, editFormVisible, currentRole } = this.state;
    if (error)
      return (
        <Alert
          message={<FormattedMessage id={'Common.message.error'} />}
          description={error}
          type="error"
          showIcon
        />
      );
    const {
      updating = false,
      fetching = false,
      role: { roles },
    } = this.props;
    return (
      <Card bordered={false}>
        <Spin spinning={fetching}>
          <div className={roleStyle.roles}>
            <div className="nav-bar">
              <FormattedMessage
                id={'Common.pagination.total'}
                values={{ total: roles.length }}
              />
            </div>
            <Row gutter={16} className="grids">
              <Col span={8}>
                <Authorized route={SYSTEM_ROLE_ADD}>
                  <Card
                    className="card-add"
                    hoverable
                    onClick={() => this.showRoleEditModal()}
                  >
                    <PlusOutlined className="icon-add" />
                  </Card>
                </Authorized>
              </Col>
              {roles.map((item) => {
                return (
                  <Col span={8} key={item.id}>
                    <Card title={item.name}>
                      <div className="body text-muted">{item.remark}</div>
                      {this.renderFooter(item)}
                    </Card>
                  </Col>
                );
              })}
            </Row>
          </div>
        </Spin>
        <DrawerConfirm
          width={500}
          destroyOnClose
          title={<FormattedMessage id={'Page.system.roles.edit.formTitle'} />}
          visible={editFormVisible}
          onCancel={() => this.closeRoleEditModal()}
          onOk={() => this.editRoleDone()}
          confirmLoading={updating}
        >
          <EditForm
            wrappedComponentRef={(ref) => {
              this.editForm = ref;
            }}
            roleId={currentRole && currentRole.id}
          />
        </DrawerConfirm>
      </Card>
    );
  }
}
