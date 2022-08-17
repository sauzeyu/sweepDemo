import React, { Component } from 'react';
import Authorized from '@/components/Authorized';
import { DASHBOARD_ZIPKIN } from '@/components/Authorized/AuthMap';
/**
 * Zipkin 控制台
 */

class Index extends Component {
  constructor() {
    super();
    this.state = {
      iFrameHeight: '0px',
    };
  }

  render() {
    return (
      <Authorized route={DASHBOARD_ZIPKIN}>
        <iframe
          style={{ border: 'none' }}
          sandbox="allow-scripts allow-forms allow-same-origin"
          scrolling="auto"
          // src={'http://172.16.13.13:9411/zipkin/'}
          src={'http://10.108.33.137:9411/zipkin/'}
          width="100%"
          height="100%"
        />
      </Authorized>
    );
  }
}

export default Index;
