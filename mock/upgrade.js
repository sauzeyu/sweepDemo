import Mock from 'mockjs';

const createTasks = () => {
  return {
    id: Mock.Random.id(),
    modelId: 1,
    modelName: Mock.mock('@cword(3)'),
    type: 1,
    name: Mock.mock('@cword(5)'),
    createTime: '2020-01-01 10:00:00',
    startTime: '2020-01-01 12:00:00',
    endTime: '2020-01-02 12:00:00',
    mandatoryUpdate: 1,
    status: 0,
    policyId: 1,
    policyName: Mock.mock('@string(2)'),
    packages: [
      {
        id: '1231',
        deviceCategoryName: '零件1',
        deviceName: '就是就是',
        name: Mock.mock('@cword(5)'),
      },
    ],
  };
};
const mockTasks = [createTasks(), createTasks()];
export default {
  'GET /api/policys': {
    success: true,
    total: 1,
    policys: [
      {
        id: 1,
        name: '策略001',
        remark: 'Remark',
        statue: 0,
        applier: '张安',
        applyTime: '2020-01-01 12:00:00',
        checker: '',
        creator: '',
      },
    ],
  },
  'GET /api/policysForCheck': {
    success: true,
    total: 1,
    policys: [
      {
        id: 1,
        name: '策略001',
        remark: 'Remark',
        statue: 0,
        applier: '申请人1',
        applyTime: '2020-01-01 12:00:00',
        checker: '审核人0002',
        creator: '创建者0001',
        opinion: '审核人ABC',
      },
    ],
  },
  'GET /api/tasks': {
    success: true,
    total: 2,
    tasks: mockTasks,
  },
  'GET /api/tasks/:id': (req, res) => {
    const id = req.params.id;
    const task = mockTasks.find((item) => item.id == id);
    res.send({
      success: !!task,
      task,
    });
  },
  'GET /api/task/:id/stat': {
    success: true,
    total: 131,
    taskStats: [
      {
        status: -1,
        total: 12,
      },
      {
        status: 1,
        total: 22,
      },
      {
        status: 2,
        total: 16,
      },
      {
        status: 3,
        total: 25,
      },
      {
        status: 4,
        total: 39,
      },
      {
        status: 5,
        total: 9,
      },
    ],
  },
  'GET /api/subTasks': {
    success: true,
    subTasks: [
      {
        id: 1,
        taskId: 1,
        name: Mock.mock('@cword(5)'),
        modelName: 'modelName',
        vin: 'jsklj1231',
        status: 1,
        downloadedTime: '2020-01-01 12:32:21',
        updatedTime: '2020-01-01 12:32:21',
      },
    ],
  },
  'GET /api/subTask/:id': {
    success: true,
    subTask: {
      id: 1,
      name: '子任务1',
      modelName: '车型1',
      vin: 'sajkdljakl1213',
      status: 1,
      updatelogs: [
        {
          updateTime: '2020-01-01 12:00:00',
          status: 1,
          remark: '模拟日志',
        },
      ],
      taskItems: [
        {
          code: 123,
          name: 'nmam1',
          packageName: 'PPP1',
          type: 0,
          fileSize: '12',
          downloadedSize: '12',
          downloadedTime: '2020-01-02 13:00:00',
        },
      ],
    },
  },
  'GET /api/taskItem/:id/downloadLogs': {
    success: true,
    downloadLogs: [
      {
        id: '1',
        taskItemId: '2',
        createTime: '2019-12-13 12:28:23',
        downloadedSize: '7',
        speed: '100',
      },
      {
        id: '2',
        taskItemId: '3',
        createTime: '2019-13-13 12:28:23',
        downloadedSize: '12',
        speed: '200',
      },
    ],
  },
  'GET /api/tasksForCheck': {
    success: true,
    tasks: [createTasks(), createTasks()],
  },
};
