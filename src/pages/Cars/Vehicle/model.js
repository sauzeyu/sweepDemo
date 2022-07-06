import {
  addVehicle,
  updateVehicle,
  selectUserByVehicleId,
} from '@/services/cars';

export default {
  namespace: 'carsVehicle',
  state: {},
  effects: {
    *upsert({ payload }, { call }) {
      return yield call(payload.id ? updateVehicle : addVehicle, payload);
    },
    *selectUserByVehicleId({ payload }, { call }) {
      return yield call(selectUserByVehicleId, payload);
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
