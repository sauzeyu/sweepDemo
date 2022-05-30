//钥匙具体状态
import { Tag } from 'antd';
import React from 'react';

export const DKState = {
  1: <Tag color="green">已启用</Tag>,
  3: <Tag color="blue">冻结</Tag>,
  4: <Tag color="warning">过期</Tag>,
};
export const KeyState = {
  1: <Tag color="green">开通</Tag>,
  2: <Tag color="blue">冻结</Tag>,
  3: <Tag color="geekblue">解冻</Tag>,
  4: <Tag color="warning">过期</Tag>,
  5: <Tag color="red">吊销</Tag>,
};
export const KeySource = {
  1: <Tag color="green">web 端</Tag>,
  2: <Tag color="blue">app 端</Tag>,
  3: <Tag color="warning">换件</Tag>,
};
export const KeyType = (text) => {
  if (text) {
    return <Tag color="blue">分享钥匙</Tag>;
  } else {
    return <Tag color="green">车主钥匙</Tag>;
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
  16: '车机',
};

export const PermitKeyLog = {
  1: '前门解锁',
  2: '后门解锁',
  4: '尾箱解锁',
  8: '启动解锁',
  16: '车机解锁',
};
/**
 * 解析授权权限值
 * @param permission 传入的权限值 1-31
 * @returns {string} 解析后的权限
 */
export const analyzePermissions = (permission) => {
  let p = [];
  for (let i = 1; i < 1 << 5; i <<= 1)
    if ((permission & i) === i) p.push(<Tag color="blue">{Permit[i]}</Tag>);
  return p;
};
export const analyzePermissionsKeyLog = (permission) => {
  let p = [];
  for (let i = 1; i < 1 << 5; i <<= 1)
    if ((permission & i) === i)
      p.push(<Tag color="blue">{PermitKeyLog[i]}</Tag>);
  return p;
};
