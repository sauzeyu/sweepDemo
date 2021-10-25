export default {
  'GET /api/vehiclesStat': {
    success: true,
    vehicleTotal: 12,
    vehicleRegistTotal: 10,
    vehicleAcitveTotal: 3,
    datas: [
      {
        statDate: '2020-01-01',
        vehicleRegistAdd: 2,
        vehicleAcitve: 2,
      },
      {
        statDate: '2020-01-02',
        vehicleRegistAdd: 3,
        vehicleAcitve: 1,
      },
    ],
  },
  'GET /api/deviceVerStat': {
    success: true,
    datas: [
      {
        name: '零件1',
        code: 'L1',
        diagPhyaddr: 'xs123',
        verCode: '1232',
        type: 0,
        subTaskTotal: 18,
        successSubTask: 17,
      },
    ],
  },
  'GET /api/taskStat': {
    success: true,
    datas: [
      {
        name: '零件1',
        code: 'L1',
        diagPhyaddr: 'xs123',
        verCode: '1232',
        type: 0,
        subTaskTotal: 18,
        successSubTask: 17,
      },
    ],
  },
};
