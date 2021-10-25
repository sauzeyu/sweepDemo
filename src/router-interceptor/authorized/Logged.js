import React from 'react';
import { Redirect } from 'umi';
import { checkUserLogged } from '@/components/Authorized';

export default ({ children, route, location }) => {
  if (!checkUserLogged()) {
    const loginPath = '/user/login';
    const ignorePath = [loginPath, '/'];
    const query =
      ignorePath.indexOf(location.pathname) !== -1
        ? {}
        : { r: window.location.href };
    return <Redirect to={{ pathname: loginPath, query }} />;
  }
  return children;
};
