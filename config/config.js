import { defineConfig, utils } from 'umi';
import pageRoutes from './router.config';
import webpackplugin from './plugin.config';
import defaultSettings from '../src/defaultSettings';
const APP_METADATA = getGlobalParams();
// global ?
global['APP_METADATA'] = APP_METADATA;
const { winPath } = utils;
export default defineConfig({
  // 代理请求
  proxy: {
    '/dkserver-back': {
      // target: 'http://172.16.11.210:8008/',
      // target: 'https://cd.vecentek.com:20611',
      // target: 'http://172.16.6.106:9001/vecentek',
      // target: 'http://localhost:9001/vecentek',
      target: 'http://172.16.70.50:8000/vecentek',
      // target: 'http://172.16.6.107:8000/vecentek',
      // target: 'http://172.16.13.12:9002/vecentek',
      // target: 'http://172.16.13.13:9001/vecentek',
      // target: 'http://172.16.13.12:8000/vecentek',
      changeOrigin: true,
      // pathRewrite: { '^/api': '' },
      logLevel: 'debug',
      secure: false,
    },
  },
  devtool: APP_METADATA.env === 'development' ? 'source-map' : undefined,
  nodeModulesTransform: {
    type: 'none',
  },
  antd: {} /* 启用后自动配置 实现antd按需加载 */,
  dva: {
    hmr: true /* 是否启用dva的热更新 */,
  },
  locale: {
    default: 'zh-CN',
    antd: true,
    // default true, when it is true, will use `navigator.language` overwrite default
    baseNavigator: true,
  },
  dynamicImport: {
    loading: '@/components/PageLoading/index',
  },
  define: {
    APP_TYPE: process.env.APP_TYPE || '',
    APP_METADATA,
  },
  targets: { ie: 11 } /* 配置浏览器的最低版本 例如: 兼容ie11 */,
  routes: pageRoutes,
  fastRefresh: {},
  history: { type: 'browser' }, // 开启打包文件的h5历史功能
  //通过 webpack-chain 的 API 扩展或修改 webpack 配置。
  chainWebpack: webpackplugin,
  base: APP_METADATA.baseUrl, // 部署到非根目录时才需配置
  publicPath: APP_METADATA.baseUrl, // 部署到非根目录和base一起使用
  // 在 document.ejs中使用了变量需要动态判断 所以需要设置 runtimePublicPath:true
  runtimePublicPath: true, // 对于需要在 html 里管理 publicPath 的场景，比如在 html 里判断环境做不同的输出，可通过配置 runtimePublicPath 为解决。
  // Theme for antd ant的主题定制
  // https://ant.design/docs/react/customize-theme-cn
  theme: defaultSettings.customTheme,
  //忽略 moment 的 locale 文件，用于减少尺寸。
  ignoreMomentLocale: false,
  //给 less-loader 的额外配置项。
  lessLoader: {
    javascriptEnabled: true,
  },
  //给 css-loader 的额外配置项。
  cssLoader: {
    modules: {
      getLocalIdent: (context, _, localName) => {
        if (
          context.resourcePath.includes('node_modules') ||
          context.resourcePath.includes('ant.design.pro.less') ||
          context.resourcePath.includes('global.less')
        ) {
          return localName;
        }

        const match = context.resourcePath.match(/src(.*)/);

        if (match && match[1]) {
          const antdProPath = match[1].replace('.less', '');
          const arr = winPath(antdProPath)
            .split('/')
            .map((a) => a.replace(/([A-Z])/g, '-$1'))
            .map((a) => a.toLowerCase());
          return `antd-pro${arr.join('-')}-${localName}`.replace(/--/g, '-');
        }
        return localName;
      },
    },
  },
  //cssnano 能为你的 CSS 文件做多方面的的优化， 以确保最终生成的文件 对生产环境来说体积是最小的。
  // 但是没有找到相关模块
  cssnano: {
    mergeRules: false,
  },
});

// 获取全局参数
function getGlobalParams() {
  // process.env 指向当前shell的环境变量，比如process.env.HOME。
  // BUILD_ENV: 获取当前环境是开发环境还是生产环境
  const BUILD_ENV = process.env.BUILD_ENV || process.env.NODE_ENV;
  // process.argv: 当前进程的命令行参数数组。
  /* 开发环境下打印的内容
  [
      'D:\\nodejs\\node.exe',
      'E:\\job\\01\\webclient\\node_modules\\umi\\lib\\scripts\\realDev.js',
      'dev'
  ]
  */
  const startParams = process.argv
    .slice(2)
    .map((arg) => arg.split('='))
    .reduce((args, [value, key]) => {
      if (/^--/.test(value)) {
        args[value.replace(/^--/, '')] = key;
      }
      return args;
    }, {});
  const PUBLIC_PATH = startParams.basePath || '/';
  return {
    env: BUILD_ENV,
    baseUrl: PUBLIC_PATH, // /
    buildVersion: '2.3.2', // undefined
    StartParams: startParams, // {}
  };
}
