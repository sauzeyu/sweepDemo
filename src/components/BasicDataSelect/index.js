'use strict';
import React from 'react';
import BaseDataSelect from './BaseDataSelect';
import { getCarModes } from '@/services/cars';

export class CarTypesSelect extends React.Component {
  render() {
    return (
      <BaseDataSelect
        labelKey={'modelName'}
        valueKey={'id'}
        dataPropName={'data'}
        {...this.props}
        action={getCarModes}
      />
    );
  }
}

export class UpgradeStrategySelect extends React.Component {
  render() {
    return (
      <BaseDataSelect
        labelKey={'name'}
        valueKey={'id'}
        dataPropName={'policys'}
        {...this.props}
        action={getStrategies}
      />
    );
  }
}

export class UpgradeTasksSelect extends React.Component {
  render() {
    return (
      <BaseDataSelect
        labelKey={'name'}
        valueKey={'id'}
        dataPropName={'tasks'}
        {...this.props}
        action={getUpTasks}
      />
    );
  }
}

export class TestTasksSelect extends React.Component {
  render() {
    return (
      <BaseDataSelect
        labelKey={'taskName'}
        valueKey={'taskId'}
        dataPropName={'tasks'}
        {...this.props}
        action={getTasksList}
      />
    );
  }
}

export class SystemUserSelect extends React.Component {
  render() {
    return (
      <BaseDataSelect
        labelKey={'name'}
        valueKey={'id'}
        searchKey={'name'}
        dataPropName={'users'}
        {...this.props}
        action={getUsers}
      />
    );
  }
}
