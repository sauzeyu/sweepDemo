// 升级策略审核状态
export const StrategyStatus = {
  0: '新创建',
  1: '待审核',
  2: '审核通过',
  3: '审核拒绝',
};

// 升级策略审核状态标识颜色
export const StrategyStatusColors = {
  0: '#ccc',
  1: '#4794ff',
  2: '#6cd41d',
  3: '#fd2e13',
};

// 升级类型
export const UpgradeTypes = {
  0: '测试',
  1: '正式',
};
// 升级任务审核状态
export const UpTaskStatus = {
  0: '新创建',
  1: '待审核',
  2: '审核通过',
  3: '审核拒绝',
  4: '执行中',
  5: '已完成',
};
export const UpTaskStatusColor = {
  0: '#6c90dd',
  1: '#428cff',
  2: '#6cd41d',
  3: '#fd2e13',
  4: '#ffae1f',
  5: '#ccc',
};
// 子任务状态
export const SubTaskStatus = {
  '-1': '下载超时终止',
  0: '待执行',
  1: '用户取消下载',
  2: '用户选择延迟下载',
  3: '下载中',
  4: '已下载',
  5: '验签成功',
  6: '验签失败',
  7: '用户取消更新',
  8: '用户确认更新',
  9: '更新成功',
  10: '更新失败',
};
