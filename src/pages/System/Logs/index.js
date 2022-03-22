import React from 'react';
import FilterForm from './filter';
import DataTable from './table';

export default class Index extends React.Component {
  render() {
    console.log('log----');
    return (
      <React.Fragment>
        <FilterForm />
        <DataTable />
      </React.Fragment>
    );
  }
}
