import {
  addVehicle,
  downloadTemplate,
  updateVehicle,
  deleteVehicle,
  getVehicleDetail,
  userListByVehicleId,
} from '@/services/cars';
import { vehicleListById } from '@/services/keys';

export default {
  namespace: 'carsVehicle',
  state: {
    vehicleDetail: undefined,
  },
  effects: {
    *upsert({ payload }, { call }) {
      return yield call(payload.id ? updateVehicle : addVehicle, payload);
    },
    *del({ payload }, { call }) {
      return yield call(deleteVehicle, payload);
    },
    *getVehicleDetail({ payload }, { call }) {
      return yield call(getVehicleDetail, payload);
    },
    *downloadTemplate({ payload }, { call }) {
      return yield call(downloadTemplate);
    },
    *userListByVehicleId({ payload }, { call }) {
      return yield call(userListByVehicleId, payload);
    },
  },
  reducers: {
    updateState(state, { payload }) {
      return {
        ...state,
        ...payload,
      };
    },
    initVehicleDetail(state, { payload }) {
      let vehicleDetail = { vehicleDetail: payload };
      return {
        ...state,
        ...vehicleDetail,
      };
    },
  },
};
