const PATH_TO_PAGE = {
  'dashboard': 'dashboard.html',
  'system/user': 'users.html',
  'system/role': 'roles.html',
  'system/permission': 'permissions.html',
  'system/login-log': 'login-logs.html',
  'system/operation-log': 'operation-logs.html'
};

function getPagePath(menuPath) {
  return PATH_TO_PAGE[menuPath] || (menuPath ? menuPath + '.html' : '#');
}

function renderMenu(menus, container, currentPath) {
  if (!menus || menus.length === 0) {
    return;
  }
  const ul = $('<ul class="nav flex-column"></ul>');
  menus.forEach(function (menu) {
    const hasChildren = menu.children && menu.children.length > 0;
    const isActive = menu.path === currentPath;
    const li = $('<li class="nav-item"></li>');

    if (hasChildren) {
      const collapseId = 'menu-collapse-' + menu.id;
      const hasActiveChild = menu.children.some(function (c) { return c.path === currentPath; });
      const link = $('<a class="nav-link d-flex justify-content-between align-items-center" data-bs-toggle="collapse" href="#' + collapseId + '" role="button" aria-expanded="' + (hasActiveChild ? 'true' : 'false') + '" aria-controls="' + collapseId + '"></a>');
      link.append('<span>' + menu.name + '</span>');
      link.append('<i class="bi bi-chevron-down small"></i>');
      li.append(link);

      const collapse = $('<div class="collapse ps-3 ' + (hasActiveChild ? 'show' : '') + '" id="' + collapseId + '"></div>');
      const childUl = $('<ul class="nav flex-column"></ul>');
      menu.children.forEach(function (child) {
        const childLi = $('<li class="nav-item"></li>');
        const childLink = $('<a class="nav-link ' + (child.path === currentPath ? 'active' : '') + '" href="/' + getPagePath(child.path) + '">' + child.name + '</a>');
        childLi.append(childLink);
        childUl.append(childLi);
      });
      collapse.append(childUl);
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
