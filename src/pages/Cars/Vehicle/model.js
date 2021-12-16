import {
  addVehicle,
  downloadTemplate,
  updateVehicle,
  deleteVehicle,
  getVehicleDetail,
  selectUserByPhone,
} from '@/services/cars';
import { vehicleListById } from '@/services/keys';

export default {
  namespace: 'carsVehicle',
  state: {},
  effects: {
    *upsert({ payload }, { call }) {
      return yield call(payload.id ? updateVehicle : addVehicle, payload);
    },
    *deleteVehicle({ payload }, { call }) {
      return yield call(deleteVehicle, payload);
    },
    *getVehicleDetail({ payload }, { call }) {
      return yield call(getVehicleDetail, payload);
    },
    *downloadTemplate({ payload }, { call }) {
      return yield call(downloadTemplate);
    },
    *selectUserByPhone({ payload }, { call }) {
      return yield call(selectUserByPhone, payload);
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
