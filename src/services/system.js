import request from '../utils/request';
import { PermissionsUtil } from '../utils';

function decoratePermission(list = []) {
  const existsPermissionsID = [];
  list.forEach((item) => {
    existsPermissionsID.push(item.id);
  });
  const tree = PermissionsUtil.structureByDNA(list);
  return { tree, existsPermissionsID, list };
}
export async function getPermissions(params) {
  return request
    .get('menus', { params })
    .then((res) => decoratePermission(res.menus));
}
export async function savePermissions(data) {
  return request.post('menus', data);
}
export async function getRoles(params) {
  return request.get('roles', { params });
}
export async function getRoleDetail(id) {
  return request.get(`role/${id}`);
}
export async function updateRole(params) {
  const { id, ...rest } = params;
  return request.post(`role/${id}`, rest);
}
export async function addRole(role) {
  return request.post('role', role);
}
export async function getUsers(params) {
  return request.get('users', { params });
}
export async function deleteRole(id) {
  return request.delete(`role/${id}`);
}
export async function createUser(user) {
  return request.post('user', user);
}
export async function updateUser(params) {
  const { id, ...rest } = params;
  return request.post(`user/${id}`, rest);
}
export async function upsertUser(user) {
  return user.id ? updateUser(user) : createUser(user);
}
export async function deleteUser(id) {
  return request.delete(`user/${id}`);
}
export async function getUserDetail(id) {
  return request.get(`user/${id}`);
}
export async function resetUserPassword(params) {
  const { id } = params;
  return request.post(`resetPassword?id=${id}`, params);
}
