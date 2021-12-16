//钥匙具体状态
export const DKState = {
  0: '初始',
  1: '已启用',
  2: '线下学习',
  3: '停用',
  4: '过期',
  5: '吊销',
};
// 钥匙操作状态
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
/**
 * 解析授权权限值
 * @param permission 传入的权限值 1-31
 * @returns {string} 解析后的权限
 */
export const analyzePermissions = (permission) => {
  let p = '';
  for (let i = 1; i < 1 << 5; i <<= 1)
    if ((permission & i) === i) p = p.concat(Permit[i], ' ');
  return p;
};
