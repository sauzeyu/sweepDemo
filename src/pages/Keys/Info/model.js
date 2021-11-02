import {
  enableKey,
  revokeKey,
  userListByKeyId,
  vehicleListById,
} from '@/services/keys';

export default {
  namespace: 'keysManage',
  state: {},
  effects: {
    *enableKey({ payload }, { call }) {
      return yield call(enableKey, { id: payload });
    },
    *revokeKey({ payload }, { call }) {
      return yield call(revokeKey, { id: payload });
    },
    *userListByKeyId({ payload }, { call }) {
      return yield call(userListByKeyId, { payload });
    },
    *vehicleListById({ payload }, { call }) {
      return yield call(vehicleListById, { payload });
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
