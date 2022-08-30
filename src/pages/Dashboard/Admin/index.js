import React, { Component } from 'react';
import Authorized from '@/components/Authorized';
import {
  DASHBOARD_ADMIN,
  DASHBOARD_ADMIN_INFO,
} from '@/components/Authorized/AuthMap';

/**
 * SpringBootAdmin 控制台
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
      <Authorized route={DASHBOARD_ADMIN}>
        <Authorized route={DASHBOARD_ADMIN_INFO}>
          <iframe
            style={{ border: 'none' }}
            sandbox="allow-scripts allow-forms allow-same-origin"
            scrolling="auto"
            // src={'http://172.16.6.107:7001/dkserver-admin/wallboard'}
            src={'http://10.108.33.50:7001/dkserver-admin/wallboard'}
            width="100%"
            height="100%"
          />
        </Authorized>
      </Authorized>
    );
  }
}

export default Index;
