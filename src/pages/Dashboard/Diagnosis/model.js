import {
  selectBusiness,
  selectFaultLogs,
  selectForPage,
  selectPhoneBrandLogs,
  selectRecentLogs,
  selectTodayLogsCount,
} from '@/services/diagnosis';

export default {
  namespace: 'Diagnosis',
  state: {
    logDetail: {},
  },
  effects: {
    *selectRecentLogs({ payload }, { call }) {
      return yield call(selectRecentLogs, payload);
    },
    *selectTodayLogsCount({ payload }, { call }) {
      return yield call(selectTodayLogsCount, payload);
    },
    *selectFaultLogs({ payload }, { call }) {
      return yield call(selectFaultLogs, payload);
    },
    *selectPhoneBrandLogs({ payload }, { call }) {
      return yield call(selectPhoneBrandLogs, payload);
    },
    *selectForPage({ payload }, { call }) {
      return yield call(selectForPage, payload);
    },
    *selectBusiness({ payload }, { call }) {
      return yield call(selectBusiness, payload);
    },
  },
  reducers: {
    changeLogDetail: (state, { payload }) => {
      return {
        ...state,
        logDetail: payload,
      };
    },
  },
};
