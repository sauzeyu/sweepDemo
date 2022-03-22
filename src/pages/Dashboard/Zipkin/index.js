import React, { Component } from 'react';
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
      <iframe
        style={{ border: 'none' }}
        sandbox="allow-scripts allow-forms allow-same-origin"
        scrolling="auto"
        src={'http://172.16.13.12:9411/zipkin/'}
        width="100%"
        height="100%"
      />
    );
  }
}

export default Index;
