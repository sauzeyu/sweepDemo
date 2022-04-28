import React, { Component } from 'react';
import { Card } from 'antd';
import SearchForm from './SearchForm';
import DataTable from './DataTable';
import SeesawView from '@/components/SeesawView';

@SeesawView()
/**
 * 手机标定数据信息
 */
class Index extends Component {
  getFormValues = (values) => {
    this.setState({
      searchFormValues: values,
    });
  };
  state = {
    searchFormValues: {},
  };

  render() {
    const { searchFormValues } = this.state;
    return (
      <div className={'card-group'}>
        <Card bordered={false}>
          <SearchForm getFormValues={this.getFormValues} />
        </Card>
        <Card bordered={false}>
          <DataTable searchFormValues={searchFormValues} />
        </Card>
      </div>
    );
  }
}

export default Index;
