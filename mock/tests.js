import Mock from 'mockjs';

export default {
  'GET /api/vehicleSysLog': {
    success: true,
    total: 2,
    data: [
      {
        modelName: Mock.mock('@cword(5)'),
        modelDeviceName: Mock.mock('@cword(5)'),
        logTime: new Date().toISOString(),
        content: Mock.mock('@cword(100)'),
      },
      {
        modelName: Mock.mock('@cword(5)'),
        modelDeviceName: Mock.mock('@cword(5)'),
        logTime: new Date().toISOString(),
        content: Mock.mock('@cword(100)'),
      },
    ],
  },
};
