//钥匙具体状态
import { Tag, Badge } from 'antd';
import React from 'react';

export const SimpleDKState = {
  0: '未激活 ',
  1: '激活 ',
  3: '冻结 ',
  4: '过期 ',
  5: '吊销 ',
};

export const DKState = {
  0: <Badge color="green" text="未激活" />,
  1: <Badge color="green" text="激活" />,
  3: <Badge color="blue" text="冻结" />,
  4: <Badge color="yellow" text="过期" />,
  5: <Badge color="red" text="吊销" />,
};

export const SimplekeyLogFlag = {
  0: '失败',
  1: '成功',
};

export const keyLogFlag = {
  0: <Tag color="red">失败</Tag>,
  1: <Tag color="green">成功</Tag>,
};
export const keyLogFlagBadge = {
  0: <Badge color="red" text="失败" />,
  1: <Badge color="green" text="成功" />,
};
export const KeyState = {
  0: <Badge color="green" text="开通" />,
  1: <Badge color="blue" text="激活" />,
  2: <Badge color="#2db7f5" text="冻结" />,
  3: <Badge color="#2db7f5" text="解冻" />,
  4: <Badge color="yellow" text="过期" />,
  5: <Badge color="red" text="吊销" />,
};
export const KeySource = {
  1: 'web 端',
  2: 'app 端',
  3: '变更车主|售后换机|更换手机',
  4: '小程序',
};

export const SimpleKeyType = {
  1: '车主钥匙 ',
  2: '分享钥匙 ',
};

export const KeyType = (text) => {
  if (text === '0') {
    return <Tag color="green">车主钥匙</Tag>;
  } else {
    return <Tag color="blue">分享钥匙</Tag>;
  }
};

export const QuickFlag = (text) => {
  if (text === 1) {
    return 'icce ';
    // return <Tag color="green">普通</Tag>;
  } else {
    return 'ccc ';
    // return <Tag color="blue">快连</Tag>;
  }
};

export const SimplekeyClassification = (text) => {
  if (text === 1) {
    return 'icce ';
  } else if (text === 2) {
    return 'ccc ';
  }
};

export const KeyClassification = (text) => {
  if (text === 1) {
    return <Tag color="blue">icce</Tag>;
  } else if (text === 2) {
    return <Tag color="green">ccc</Tag>;
  }
};

export const SimplekeyResource = {
  1: 'App',

  2: '小程序',
};

export const KeyResource = (text) => {
  if (text === 1) {
    return <Tag color="blue">App</Tag>;
  } else if (text === 2) {
    return <Tag color="green">小程序</Tag>;
  }
};

export const KeysState = {
  1: '未配对',
  101: '已配对',
  2: '分享待确认',
  102: '分享已确认',
  3: '修改权限待确认',
  103: '修改权限已确认',
  4: '吊销待确认',
  104: '吊销已确认',
};
//授权权限值
export const Permit = {
  1: '前门',
  2: '后门',
  4: '尾箱',
  8: '启动',
};

export const PermitKeyLog = {
  1: '前门解锁',
  2: '后门解锁',
  4: '尾箱解锁',
  8: '启动解锁',
};
/**
 * 解析授权权限值
 * @param permission 传入的权限值 1-31
 * @returns {string} 解析后的权限
 */
export const analyzePermissions = (permission) => {
  let p = [];
  for (let i = 1; i < 1 << 4; i <<= 1)
    if ((permission & i) === i) p.push(<Tag color="blue">{Permit[i]}</Tag>);
  return p;
};
export const analyzePermissionsKeyLog = (permission) => {
  let p = [];
  for (let i = 1; i < 1 << 4; i <<= 1)
    if ((permission & i) === i)
      p.push(<Tag color="blue">{PermitKeyLog[i]}</Tag>);
  return p;
};
