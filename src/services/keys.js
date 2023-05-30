import request from '@/utils/request';

// 获取钥匙信息
export async function getKeysList(params) {
  return request.get('dkmKey/selectForPage', { params });
}

// 启用停用钥匙
export async function enableKey(params) {
  return request.post('dkmKey/updateStateById?', params);
}

// 吊销钥匙
export async function revokeKey(params) {
  return request.post('dkmKey/updateStateForRevokeById?', params);
}

//通过id查询用户信息
export async function selectUserById(id) {
  return request.get('dkmUser/selectById', { params: { id: id } });
}

//通过id查询车辆信息
export async function selectVehicleById(id) {
  return request.get('dkmVehicle/selectById', { params: { id: id } });
}

export async function getKeyLogList(params) {
  return request.get('dkmKeyLog/selectForPage', {
    params,
    paramsSerializer: (params) => {
      clearDeep(params);
      // if (params?.statusCode && params?.statusCode?.length !== 0) {

      // return params.statusCode.map(item => `statusCode=${item}`).join('&');

      // let newParams = params.filter(
      //   (item) => !['', null, undefined, NaN, false, true].includes(item)
      // )
      const keys = Object.keys(params);

      const arr = [];
      keys.forEach((item) => {
        if (Array.isArray(params[item])) {
          const url = params[item]
            // .filter(item => params[item] || params[item] === 0)   // 删除无值的字段，可根据需要进行保留或删除
            .map((_) => `${item}=${_}`)
            .join('&');
          arr.push(url);
        } else {
          arr.push(`${item}=${params[item]}`);
        }
      });
      const result = arr.join('&');
      return `${result}`;
    },

    // }
  });
}

//通过创建者查询 历史导出记录表(倒排)
export async function checkKeyUseLog(params) {
  return request.get('dkmKeyLogHistoryExport/checkKeyUseLog', { params });
}

function clearDeep(obj) {
  if (!obj || !typeof obj === 'object') return;

  const keys = Object.keys(obj);
  for (var key of keys) {
    const val = obj[key];
    if (
      typeof val === 'undefined' ||
      ((typeof val === 'object' || typeof val === 'string') && !val)
    ) {
      // 如属性值为null或undefined或''，则将该属性删除
      delete obj[key];
    } else if (typeof val === 'object') {
      // 属性值为对象，递归调用
      clearDeep(obj[key]);

      if (Object.keys(obj[key]).length === 0) {
        // 如某属性的值为不包含任何属性的独享，则将该属性删除
        delete obj[key];
      }
    }
  }
}
