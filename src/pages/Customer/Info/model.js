import {
  addDkCustomer,
  editDkCustomer,
  deleteDkCustomer,
} from '@/services/customer';

export default {
  namespace: 'customer',
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
