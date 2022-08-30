import React from 'react';

import TopForm from './TopForm';
import MiddleForm from './MiddleForm';
import BottomForm from './BottomForm';

import Authorized from '@/components/Authorized';
import {
  STATISTICS,
  STATISTICS_BOTTOM,
  STATISTICS_MIDDLE,
  STATISTICS_TOP,
} from '@/components/Authorized/AuthMap';

class index extends React.Component {
  render() {
    return (
      <Authorized route={STATISTICS}>
        <div className={'card-group'}>
          <Authorized route={STATISTICS_TOP}>
            <TopForm />
          </Authorized>
          <Authorized route={STATISTICS_MIDDLE}>
            <MiddleForm />
          </Authorized>
          <Authorized route={STATISTICS_BOTTOM}>
            <BottomForm />
          </Authorized>
        </div>
      </Authorized>
    );
  }
}

export default index;
