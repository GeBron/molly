const PATH_TO_PAGE = {
  'dashboard': 'dashboard.html',
  'system/user': 'users.html',
  'system/role': 'roles.html',
  'system/permission': 'permissions.html',
  'system/login-log': 'login-logs.html',
  'system/operation-log': 'operation-logs.html'
};

function getPagePath(menuPath) {
  const key = menuPath ? menuPath.replace(/^\//, '') : menuPath;
  return PATH_TO_PAGE[key] || (key ? key + '.html' : '#');
}

function hasActiveDescendant(menu, currentPath) {
  if (!menu.children || menu.children.length === 0) {
    return false;
  }
  return menu.children.some(function (child) {
    return child.path === currentPath || hasActiveDescendant(child, currentPath);
  });
}

function renderMenu(menus, container, currentPath) {
  if (!menus || menus.length === 0) {
    return;
  }
  const normalizedCurrentPath = currentPath && !currentPath.startsWith('/') ? '/' + currentPath : currentPath;
  const ul = $('<ul class="nav flex-column"></ul>');
  menus.forEach(function (menu) {
    const hasChildren = menu.children && menu.children.length > 0;
    const isActive = menu.path === normalizedCurrentPath;
    const li = $('<li class="nav-item"></li>');

    if (hasChildren) {
      const collapseId = 'menu-collapse-' + menu.id;
      const hasActiveChild = hasActiveDescendant(menu, normalizedCurrentPath);
      const link = $('<a class="nav-link d-flex justify-content-between align-items-center" data-bs-toggle="collapse" href="#' + collapseId + '" role="button" aria-expanded="' + (hasActiveChild ? 'true' : 'false') + '" aria-controls="' + collapseId + '"></a>');
      link.append('<span>' + menu.name + '</span>');
      link.append('<i class="bi bi-chevron-down small"></i>');
      li.append(link);

      const collapse = $('<div class="collapse ps-3 ' + (hasActiveChild ? 'show' : '') + '" id="' + collapseId + '"></div>');
      renderMenu(menu.children, collapse, currentPath);
      li.append(collapse);
    } else {
      const link = $('<a class="nav-link ' + (isActive ? 'active' : '') + '" href="/' + getPagePath(menu.path) + '">' + menu.name + '</a>');
      li.append(link);
    }

    ul.append(li);
  });
  container.empty().append(ul);
}

function initLayout(pageTitle, currentPath) {
  checkLogin(function (userInfo) {
    $('#current-username').text(userInfo.user.username);
    $('#page-title').text(pageTitle);
    renderMenu(userInfo.menus, $('#side-menu'), currentPath);
    $('#logout-btn').on('click', function (e) {
      e.preventDefault();
      logout();
    });
  });
}
