import React from 'react';
import './home.css';
import nacos from '../../imgs/nacos.jpg';
import admin from '../../imgs/admin.jpg';
import zipkin from '../../imgs/zipkin.jpg';
import sentinel from '../../imgs/sentinel.jpg';

class index extends React.Component {
  render() {
    return (
      <div style={{ width: '100%', height: '100%', background: '#92eb8726' }}>
        <div style={{ width: '200px', height: '200px', float: 'left' }}>
          <div align={'center'}>
            <a href="http://localhost:8848/nacos" target="_blank">
              <img height="120px" width="150px" src={nacos} />
            </a>
          </div>
          <div
            align={'center'}
            style={{
              color: '#2b7de2',
              fontSize: 'large',
              textAlign: 'center',
              marginTop: '5px',
            }}
          >
            注册中心
          </div>
        </div>

        <div style={{ width: '200px', height: '200px', float: 'left' }}>
          <div align={'center'}>
            <a href="http://localhost:9411/zipkin/" target="_blank">
              <img height="120px" width="150px" src={zipkin} />
            </a>
          </div>
          <div
            align={'center'}
            style={{
              color: 'blueviolet',
              fontSize: 'large',
              textAlign: 'center',
              marginTop: '5px',
            }}
          >
            链路跟踪
          </div>
        </div>
        <div style={{ width: '200px', height: '200px', float: 'left' }}>
          <div align={'center'}>
            <a href="http://localhost:8080" target="_blank">
              <img height="120px" width="150px" src={sentinel} />
            </a>
          </div>
          <div
            align={'center'}
            style={{
              color: '#e22bbbc7',
              fontSize: 'large',
              textAlign: 'center',
              marginTop: '5px',
            }}
          >
            流量限制
          </div>
        </div>
        <div style={{ width: '200px', height: '200px', float: 'left' }}>
          <div align={'center'}>
            <a href="http://localhost:7001/dkserver-admin" target="_blank">
              <img height="120px" width="150px" src={admin} />
            </a>
          </div>
          <div
            align={'center'}
            style={{
              color: '#3fe22b',
              fontSize: 'large',
              textAlign: 'center',
              marginTop: '5px',
            }}
          >
            服务监控
          </div>
        </div>
      </div>
    );
  }
}

export default index;
