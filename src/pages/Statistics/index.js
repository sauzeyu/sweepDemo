import React from 'react';

import TopForm from './TopForm';
import MiddleForm from './MiddleForm';
import BottomForm from './BottomForm';

import Authorized from '@/components/Authorized';
import { STATISTICS } from '@/components/Authorized/AuthMap';

class index extends React.Component {
  render() {
    return (
      <Authorized route={STATISTICS}>
        <div className={'card-group'}>
          <TopForm />
          <MiddleForm />
          <BottomForm />
        </div>
      </Authorized>
    );
  }
}

export default index;
