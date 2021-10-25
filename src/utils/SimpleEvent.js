/**
 * 简易事件触发器
 */
class SimpleEvent {
  __listeners__ = {};
  addListener = (name, listener) => {
    const listeners = this.__listeners__;
    if (!listeners[name]) listeners[name] = [];
    listeners[name].push(listener);
  };
  removeListener = (name, listener) => {
    const listeners = this.__listeners__;
    const callbacks = listeners[name];
    if (!callbacks) return false;
    let index = callbacks.findIndex((item) => item === listener);
    if (index < 0) return false;
    callbacks.splice(index, 1);
    return true;
  };
  emit = (name, ...args) => {
    const listeners = this.__listeners__;
    const callbacks = listeners[name];
    if (!callbacks) return;
    callbacks.map((cb) => {
      cb(...args);
    });
  };
  destroy() {
    this.__listeners__ = {};
  }
}

export default SimpleEvent;
