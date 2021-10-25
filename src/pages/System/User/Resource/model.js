'use strict';
import { getPermissions, savePermissions } from '@/services/system';
import { PermissionsUtil } from '@/utils';
import {
  getPermissionByDNA,
  insertPermission,
  loopPermissions,
  sortPermissions,
} from './utils';

export default {
  namespace: 'permission',
  state: {
    permissions: [],
    existsPermissionsID: [],
    selectedPermission: null,
    deleted: [],
  },
  effects: {
    *fetch({ payload }, { call, put }) {
      const response = yield call(getPermissions, payload);
      yield put({
        type: 'updatePermissions',
        payload: {
          permissions: response.tree,
          existsPermissionsID: response.existsPermissionsID,
        },
      });
    },
    *savePermissions({ payload }, { call, put }) {
      const list = PermissionsUtil.toTile(payload.permissions);
      yield call(savePermissions, {
        menus: list,
        deleteMenus: payload.deleteMenus,
      });
      yield put({
        type: 'updateDeleted',
        payload: [],
      });
    },
  },

  reducers: {
    updateDeleted(state, { payload }) {
      return {
        ...state,
        deleted: payload,
      };
    },
    updatePermissions(state, { payload }) {
      const newState = { ...state, permissions: payload.permissions };
      if ('existsPermissionsID' in payload)
        newState.existsPermissionsID = payload.existsPermissionsID;
      if ('selectedPermission' in payload)
        newState.selectedPermission = payload.selectedPermission;
      return newState;
    },
    updateCurrentPermission(state, { payload }) {
      let current;
      loopPermissions(state.permissions, (item) => {
        if (item.dna === state.selectedPermission.dna) {
          current = Object.assign(item, payload.values);
        }
      });
      return {
        ...state,
        selectedPermission: current,
      };
    },
    selectPermission(state, { payload }) {
      let selectedPermission = null;
      if (payload.permission) {
        selectedPermission = payload.permission;
      } else if (payload.dna) {
        selectedPermission = getPermissionByDNA(state.permissions, payload.dna);
      }
      return {
        ...state,
        selectedPermission: selectedPermission,
      };
    },
    sortPermissions(state, { payload }) {
      return {
        ...state,
        permissions: sortPermissions(state.permissions, payload.info),
      };
    },
    delPermission(state, { payload }) {
      const permissions = loopPermissions(
        state.permissions,
        (item, index, arr) => {
          if (item.dna === payload.permission.dna) {
            arr.splice(index, 1);
            if (item.id) {
              !state.deleted && (state.deleted = []);
              state.deleted?.push(item.id);
            }
          }
        },
      );
      return {
        ...state,
        selectedPermission: null,
        permissions,
      };
    },
    insertPermission(state, { payload }) {
      const newItem = insertPermission(
        state.permissions,
        payload.permission,
        payload.level,
      );
      return {
        ...state,
        selectedPermission: newItem,
      };
    },
    reset(state) {
      return {
        permissions: [],
        existsPermissionsID: [],
        selectedPermission: null,
      };
    },
  },
};
