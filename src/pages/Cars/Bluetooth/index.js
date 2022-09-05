import React, { Component } from 'react';
import { Card } from 'antd';
import SearchForm from './SearchForm';
import DataTable from './DataTable';
import SeesawView from '@/components/SeesawView';
import Authorized from '@/components/Authorized';
import {
  CARS_BLUETOOTH,
  CARS_BLUETOOTH_TABLE,
  CARS_BLUETOOTH_SELECT,
} from '@/components/Authorized/AuthMap';

@SeesawView()
/**
 * 汽车类型
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
      <Authorized route={CARS_BLUETOOTH}>
        <div className={'card-group'}>
          <Authorized route={CARS_BLUETOOTH_TABLE}>
            <Card bordered={false}>
              <SearchForm getFormValues={this.getFormValues} />
            </Card>

            <Card bordered={false}>
              <DataTable searchFormValues={searchFormValues} />
            </Card>
          </Authorized>
        </div>
      </Authorized>
    );
  }
}

export default Index;
