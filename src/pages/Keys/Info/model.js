import { addCarModes, delCarModes, updateCarModes } from '@/services/keys';

export default {
  namespace: 'keysManage',
  state: {},
  effects: {
    *upsert({ payload }, { call }) {
      return yield call(payload.id ? updateCarModes : addCarModes, payload);
    },
    *del({ payload }, { call }) {
      return yield call(delCarModes, payload);
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
