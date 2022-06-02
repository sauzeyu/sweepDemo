import React from 'react';

import TopForm from './TopForm';
import MiddleForm from './MiddleForm';

class index extends React.Component {
  render() {
    return (
      <div className={'card-group'}>
        <TopForm />
        <MiddleForm />
      </div>
    );
  }
}

export default index;
