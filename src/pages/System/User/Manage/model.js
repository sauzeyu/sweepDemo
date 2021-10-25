'use strict';
import {
  getRoles,
  updateUser,
  deleteUser,
  upsertUser,
  resetUserPassword,
  getUserDetail,
} from '@/services/system';
import { md5 } from '@/utils';

function getInitalState() {
  return {
    roles: [],
  };
}
export default {
  namespace: 'userManage',
  state: getInitalState(),
  effects: {
    *fetchRoles({ payload }, { call, put }) {
      const data = yield call(getRoles, payload);
      const response = data.roles;
      yield put({
        type: 'changeRoles',
        payload: {
          roles: response,
        },
      });
    },
    *resetUserPassword({ payload }, { call }) {
      return yield call(resetUserPassword, payload);
    },
    *setUserStatus({ payload }, { call }) {
      return yield call(updateUser, payload);
    },
    *deleteUser({ payload }, { call }) {
      return yield call(deleteUser, payload.id);
    },
    *upsertUser({ payload }, { call }) {
      payload.password = md5(payload.loginName + '123456');
      payload.isSetValidDate = payload.validDate ? 1 : 0;
      return yield call(upsertUser, payload);
    },
    *getUserDetail({ payload }, { call }) {
      return yield call(getUserDetail, payload);
    },
  },
  reducers: {
    changeRoles(state, { payload }) {
      return {
        ...state,
        roles: payload.roles,
      };
    },
    reset() {
      return getInitalState();
    },
  },
};
