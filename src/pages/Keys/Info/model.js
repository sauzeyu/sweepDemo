import { enableKey, revokeKey } from '@/services/keys';

export default {
  namespace: 'keysManage',
  state: {},
  effects: {
    *enableKey({ payload }, { call }) {
      return yield call(enableKey, { id: payload });
    },
    *revokeKey({ payload }, { call }) {
      return yield call(revokeKey, { id: payload });
    },
  },
  reducers: {
    updateState(state, { payload }) {
      return {
        ...state,
        ...payload,
      };
    },
  },
};
