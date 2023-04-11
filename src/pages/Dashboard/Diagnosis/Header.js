import React from 'react';

import { Decoration5, Decoration8 } from '@jiaminghi/data-view-react';

const styles = {
  topHeader: {
    width: '100%',
    height: '60px',
    display: 'flex',
  },
  decoration8: {
    width: '50%',
    height: '60px',
  },
  decoration5: {
    marginTop: '30px',
  },
  title: {
    position: 'absolute',
    fontSize: '30px',
    fontWeight: 'bold',
    left: '50%',
    top: '10px',
    transform: 'translateX(-50%)',
  },
};

export default () => {
  return (
    <div style={styles.topHeader}>
      <Decoration8 style={styles.decoration8} />
      <Decoration5 style={styles.decoration5} />
      <Decoration8 style={styles.decoration8} reverse={true} />
      <div style={styles.title}>数字钥匙功能诊断</div>
    </div>
  );
};
