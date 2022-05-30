import { enableKey, revokeKey } from '@/services/keys';

export default {
  namespace: 'keysManage',
  state: {},
  effects: {
    *enableKey({ payload }, { call }) {
      const params = new URLSearchParams();
      params.append('id', payload.id);
      params.append('dkState', payload.dkState);
      return yield call(enableKey, params);
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
