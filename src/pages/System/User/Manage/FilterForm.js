'use strict';
import React from 'react';
import { Form, Row, Col, Radio, Input } from 'antd';
import styles from './style.less';
import { FormattedMessage } from 'umi';
import EasyTable from '@/components/EasyTable';
import { removeEmptyProperty } from '@/utils';

@EasyTable.connect(({ userManageDataTable }) => ({
  userManageDataTable,
}))
export default class FilterForm extends React.Component {
  form = React.createRef();
  submit = () => {
    setTimeout(async () => {
      const values = await this.form.current.getFieldsValue();
      this.props.userManageDataTable.fetch(removeEmptyProperty(values));
    });
  };
  render() {
    return (
      <Form className={styles.navForm} ref={this.form}>
        <Row>
          <Form.Item
            label={
              <FormattedMessage id={'Page.system.users.searchByKey.label'} />
            }
            name={'keywords'}
          >
            <Input.Search
              placeholder={'请输入关键词查询'}
              style={{ width: 260 }}
              onSearch={this.submit}
            />
          </Form.Item>
        </Row>
      </Form>
    );
  }
}
