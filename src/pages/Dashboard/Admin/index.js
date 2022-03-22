import React, { Component } from 'react';
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
      <iframe
        style={{ border: 'none' }}
        sandbox="allow-scripts allow-forms allow-same-origin"
        scrolling="auto"
        src={'http://localhost:7001/dkserver-admin/wallboard'}
        width="100%"
        height="100%"
      />
    );
  }
}

export default Index;
