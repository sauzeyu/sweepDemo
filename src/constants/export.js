//钥匙具体状态
import { Tag, Badge } from 'antd';
import React from 'react';

// export const keyLogFlag1 = {
//   0: <Tag color="red">失败</Tag>,
//   1: <Tag color="green">成功</Tag>,
// };
// export const keyLogFlagBadge1 = {
//   0: <Badge color="red" text="失败" />,
//   1: <Badge color="green" text="成功" />,
// };

// export const KeyType1 = (text) => {
//   if (text === '0') {
//     return <Tag color="green">车主钥匙</Tag>;
//   } else {
//     return <Tag color="blue">分享钥匙</Tag>;
//   }
// };

export const exportStatus = {
  0: <Badge color="bule" text="正在导出" />,
  1: <Badge color="green" text="导出完成" />,
  2: <Badge color="red" text="导出失败" />,
};
