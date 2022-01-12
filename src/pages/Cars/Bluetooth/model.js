import {
  addCarModes,
  delCarModes,
  updateCarModes,
  delModeBatch,
} from '@/services/cars';

export default {
  namespace: 'carsType',
  state: {},
  effects: {
    *upsert({ payload }, { call }) {
      return yield call(payload.id ? updateCarModes : addCarModes, payload);
    },
    *del({ payload }, { call }) {
      return yield call(delCarModes, payload);
    },
    *delModeBatch({ payload }, { call }) {
      return yield call(delModeBatch, payload);
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
