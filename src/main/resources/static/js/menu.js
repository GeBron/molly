const PATH_TO_PAGE = {
  'dashboard': '/dashboard',
  'system/user': '/users',
  'system/role': '/roles',
  'system/permission': '/permissions',
  'system/login-log': '/login-logs',
  'system/operation-log': '/operation-logs'
};

function getPagePath(menuPath) {
  const key = menuPath ? menuPath.replace(/^\//, '') : menuPath;
  return PATH_TO_PAGE[key] || (key ? '/' + key + '.html' : '#');
}

function initLayout() {
  // 菜单已由服务端渲染，此处保留空函数以兼容旧调用
}
