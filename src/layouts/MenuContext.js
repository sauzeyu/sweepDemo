import { createContext } from 'react';
// Context 通过组件树提供了一个传递数据的方法，从而避免了在每一个层级手动的传递 props 属性。
// createContext(defaultValue) 创建一个上下文的容器(组件), defaultValue可以设置共享的默认数据
export default createContext();
