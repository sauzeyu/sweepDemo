import React, { Component } from 'react';
import { Card } from 'antd';
import SearchForm from './SearchForm';
import DataTable from './DataTable';
import SeesawView from '@/components/SeesawView';
import Authorized from '@/components/Authorized';
import { CARS_PHONE } from '@/components/Authorized/AuthMap';
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
      <Authorized route={CARS_PHONE}>
        <div className={'card-group'}>
          <Card bordered={false}>
            <SearchForm getFormValues={this.getFormValues} />
          </Card>
          <Card bordered={false}>
            <DataTable searchFormValues={searchFormValues} />
          </Card>
        </div>
      </Authorized>
    );
  }
}

export default Index;
