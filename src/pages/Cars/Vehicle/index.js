import React, { Component } from 'react';
import { Card } from 'antd';
import SearchForm from './SearchForm';
import DataTable from './DataTable';
import SeesawView from '@/components/SeesawView';
import Authorized from '@/components/Authorized';
import { CARS_VEHICLE } from '@/components/Authorized/AuthMap';
@SeesawView()
/**
 * 车辆信息
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
      <Authorized route={CARS_VEHICLE}>
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
