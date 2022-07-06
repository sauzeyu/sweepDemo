import React from 'react';

import TopForm from './TopForm';
import MiddleForm from './MiddleForm';
import BottomForm from './BottomForm';

class index extends React.Component {
  render() {
    return (
      <div className={'card-group'}>
        <TopForm />
        <MiddleForm />
        <BottomForm />
      </div>
    );
  }
}

export default index;
