import { auditLog, putFile } from '@/services/systemLogs';

export default {
  namespace: 'systemLogs',
  state: {},
  effects: {
    *audit({ payload }, { call }) {
      return yield call(auditLog, payload);
    },
    *putFile({ payload }, { call }) {
      return yield call(putFile, payload);
    },
  },
};
