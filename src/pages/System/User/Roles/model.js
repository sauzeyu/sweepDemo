import {
  addRole,
  deleteRole,
  getPermissions,
  getRoleDetail,
  getRoles,
  updateRole,
} from '@/services/system';
import { PermissionsUtil } from '@/utils';

function getInitalState() {
  return {
    roles: [],
    permissions: [],
    permissionsIDMap: {},
    permissionsDNAMap: {},
  };
}

export default {
  namespace: 'role',
  state: getInitalState(),
  effects: {
    *fetch({ payload }, { call, put }) {
      const data = yield call(getRoles, payload);
      const roles = data.roles;
      yield put({ type: 'changeState', payload: { roles } });
    },
    *fetchDetail({ payload }, { call, put }) {
      return yield call(getRoleDetail, payload.id);
    },
    *fetchPermissions({ payload }, { call, put }) {
      const permissions = yield call(getPermissions);
      yield put({
        type: 'changeState',
        payload: {
          ...mapPermissions(permissions.list),
        },
      });
    },
    *upsert({ payload }, { call, put }) {
      yield call(payload.id ? updateRole : addRole, payload);
      yield put({ type: 'fetch' });
    },
    *deleteRole({ payload }, { select, call, put }) {
      yield call(deleteRole, payload);
      yield put({ type: 'fetch' });
    },
  },
  reducers: {
    changeState: (state, { payload }) => {
      return {
        ...state,
        ...payload,
      };
    },
    reset() {
      return getInitalState();
    },
  },
};

function mapPermissions(list = []) {
  const IDMap = {},
    DNAMap = {};
  list.map((item) => {
    IDMap[String(item.id)] = item;
    DNAMap[item.dna] = item;
  });
  const permissions = PermissionsUtil.structureByDNA(list);
  return {
    permissions,
    permissionsIDMap: IDMap,
    permissionsDNAMap: DNAMap,
  };
}
