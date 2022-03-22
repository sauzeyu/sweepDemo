import React, { Component } from 'react';
/**
 * Kibana 日志仪表盘
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
        src={
          "http://172.16.13.11:5601/app/kibana#/dashboard/8551eb20-6f83-11ec-b8d3-27a48fedb6cb?_g=(refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(description:'',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),panels:!((embeddableConfig:(legendOpen:!f,timeRange:(from:now-1y,to:now),vis:(legendOpen:!t)),gridData:(h:16,i:cabdf239-ed24-466a-9b15-8da17e5d167e,w:24,x:0,y:0),id:'41ec43f0-6f81-11ec-b8d3-27a48fedb6cb',panelIndex:cabdf239-ed24-466a-9b15-8da17e5d167e,type:visualization,version:'7.6.2'),(embeddableConfig:(colors:(%E8%AE%A1%E6%95%B0:%23052B51),legendOpen:!f,vis:(colors:(%E8%AE%A1%E6%95%B0:%23052B51),legendOpen:!t)),gridData:(h:16,i:'05fc3dd2-6650-450c-a771-33359bc4cc3e',w:24,x:24,y:0),id:efa9a1b0-6f93-11ec-b8d3-27a48fedb6cb,panelIndex:'05fc3dd2-6650-450c-a771-33359bc4cc3e',type:visualization,version:'7.6.2'),(embeddableConfig:(colors:('2':%23AEA2E0),legendOpen:!t,timeRange:(from:now-1y,to:now),vis:(colors:('2':%237EB26D),legendOpen:!t)),gridData:(h:19,i:e63f23e4-0666-4f89-b729-b5e4284cd030,w:48,x:0,y:16),id:'9768fc60-6f8b-11ec-b8d3-27a48fedb6cb',panelIndex:e63f23e4-0666-4f89-b729-b5e4284cd030,type:visualization,version:'7.6.2')),query:(language:kuery,query:''),timeRestore:!f,title:Vecentek,viewMode:view)"
        }
        width="100%"
        height="100%"
      />
    );
  }
}

export default Index;
