import {
  addDkCustomer,
  editDkCustomer,
  deleteDkCustomer,
} from '@/services/aftermarketReplacement';

export default {
  namespace: 'aftermarketReplacement',
  state: {},
  effects: {
    *upsert({ payload }, { call }) {
      return yield call(payload.id ? editDkCustomer : addDkCustomer, payload);
    },
    *del({ payload }, { call }) {
      return yield call(deleteDkCustomer, payload);
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
