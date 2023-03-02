export default {
  namespace: 'keysErrorLog',
  state: {},
  effects: {},
  reducers: {
    updateState(state, { payload }) {
      return {
        ...state,
        ...payload,
      };
    },
  },
};
