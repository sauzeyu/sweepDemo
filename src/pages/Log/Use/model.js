export default {
  namespace: 'keyUseLogDataTable',
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
