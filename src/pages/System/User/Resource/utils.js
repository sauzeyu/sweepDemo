import { PermissionsUtil } from '@/utils';

export function getPermissionByDNA(permissions, dna) {
  let found = null;
  loopPermissions(permissions, (item) => {
    if (item.dna === dna) {
      found = item;
    }
  });
  return found;
}

export function filterPermissions(permissions, filterValue) {
  if (!filterValue) return [];
  const filteredKeys = [];
  loopPermissions(permissions, (item) => {
    if (
      item.name &&
      item.name.indexOf(filterValue) > -1 &&
      item.dna.length > 1
    ) {
      filteredKeys.push(item.dna);
    }
  });
  return filteredKeys;
}

export function sortPermissions(permissions, info) {
  const dropKey = info.node.props.eventKey;
  const dragKey = info.dragNode.props.eventKey;
  const dropPos = info.node.props.pos.split('-');
  const dropPosition = info.dropPosition - Number(dropPos[dropPos.length - 1]);
  const loop = (data, key, callback) => {
    data.forEach((item, index, arr) => {
      if (item.dna === key) {
        return callback(item, index, arr);
      }
      if (item.children) {
        return loop(item.children, key, callback);
      }
    });
  };
  let dragObj = null;
  loop(permissions, dragKey, (item, index, arr) => {
    arr.splice(index, 1);
    dragObj = item;
  });
  if (info.dropToGap) {
    let ar = null;
    let i = null;
    loop(permissions, dropKey, (item, index, arr) => {
      ar = arr;
      i = index;
    });
    if (dropPosition === -1) {
      ar.splice(i, 0, dragObj);
    } else {
      ar.splice(i - 1, 0, dragObj);
    }
  } else {
    loop(permissions, dropKey, (item) => {
      item.children = item.children || [];
      item.children.push(dragObj);
    });
  }
  return permissions;
}

export const insertPermission = (permissions, permission, level) => {
  let found = null;
  if (!permission) {
    found = { item: null, index: 0, arr: permissions };
  } else {
    permissions = loopPermissions(permissions, (item, index, arr) => {
      if (permission.dna === item.dna) {
        found = { item, index, arr };
      }
    });
  }

  if (found) {
    const { item, index, arr } = found;
    const addItem = {
      title: '',
      type: '2',
      href: 'uri',
    };
    if (level === 0) {
      //没有任何菜单的情况下创建
      arr.push(addItem);
    } else if (level === 1) {
      //创建同级
      console.log(permission, level);
      addItem.type = permission.type;
      arr.splice(index + 1, 0, addItem);
    } else {
      //创建二级菜单
      console.log(permission.type);
      addItem.type =
        { 2: '0', 0: '1', 1: '1' }[permission.type] || addItem.type;
      if (!item.children) item.children = [];
      item.children.push(addItem);
    }
    PermissionsUtil.recombineDNA(permissions);
    return addItem;
  }
  return null;
};

export const loopPermissions = (permissions, callback) => {
  loop(permissions);
  function loop(data, parent) {
    data.map((item, index, arr) => {
      callback(item, index, arr, parent);
      if (item.children) {
        return loop(item.children, item);
      }
    });
  }
  return permissions;
};
