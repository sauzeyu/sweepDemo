// export function config() {
//     return {
//         onError(err) {
//             if(!('onunhandledrejection' in window) && err && err.type === 'RequestError'){
//                 const event = document.createEvent('HTMLEvents');
//                 event.reason = err;
//                 event.initEvent('unhandledrejection', true, true);
//                 window.dispatchEvent(event);
//             }
//         }
//     };
// }

// 之前在 src/dva.js 下进行配置的方式已 deprecated，下个大版本会移除支持。
// 在 src 目录下新建 app.js，内容如下：

export const dva = {
  config: {
    onError(err) {
      if (
        !('onunhandledrejection' in window) &&
        err &&
        err.type === 'RequestError'
      ) {
        const event = document.createEvent('HTMLEvents');
        event.reason = err;
        event.initEvent('unhandledrejection', true, true);
        window.dispatchEvent(event);
      }
    },
  },
};
