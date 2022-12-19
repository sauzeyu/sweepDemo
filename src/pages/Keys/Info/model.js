import { enableKey, revokeKey } from '@/services/keys';

export default {
  namespace: 'keysManage',
  state: {},
  effects: {
    *enableKey({ payload }, { call }) {
      const params = new URLSearchParams();
      params.append('keyId', payload.keyId);
      params.append('vin', payload.vin);
      params.append('dkState', payload.dkState);
      params.append('userId', payload.userId);
      return yield call(enableKey, params);
    },
    *revokeKey({ payload }, { call }) {
      const params = new URLSearchParams();
      params.append('userId', payload.userId);
      params.append('vin', payload.vin);
      return yield call(revokeKey, params);
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
