import React, { Component } from 'react';
import { FullScreenContainer } from '@jiaminghi/data-view-react';
import backgroundImage from './img/bg.png';
import Header from './Header';
import LeftContent from './LeftContent';
import RightContent from './RightContent';
import {
  DASHBOARD_DIAGNOSIS,
  DASHBOARD_DIAGNOSIS_INFO,
} from '@/components/Authorized/AuthMap';
import Authorized from '@/components/Authorized';

const styles = {
  fullScreenContainer: {
    backgroundImage: `url(${backgroundImage})`,
    color: '#fff',
    display: 'flex',
    flexDirection: 'column',
  },
  mainContent: {
    display: 'flex',
    flexDirection: 'row',
    width: '100%',
    height: '100%',
    marginTop: '30px',
  },
};

export default class Index extends Component {
  render() {
    return (
      <Authorized route={DASHBOARD_DIAGNOSIS}>
        <Authorized route={DASHBOARD_DIAGNOSIS_INFO}>
          <FullScreenContainer style={styles.fullScreenContainer}>
            <Header />
            <div style={styles.mainContent}>
              <LeftContent />
              <RightContent />
            </div>
          </FullScreenContainer>
        </Authorized>
      </Authorized>
    );
  }
}
