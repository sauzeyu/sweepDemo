import {
  addVehicle,
  downloadTemplate,
  updateVehicle,
  deleteVehicle,
  getVehicleDetail,
} from '@/services/cars';

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
