-- 将菜单类型权限的 path 字段从分类路径改为实际页面路由，
-- 消除 MenuHelper 中的硬编码映射，使菜单导航路径直接来自数据库。
UPDATE sys_permission SET path = '/users' WHERE perm_code = 'user:menu';
UPDATE sys_permission SET path = '/roles' WHERE perm_code = 'role:menu';
UPDATE sys_permission SET path = '/permissions' WHERE perm_code = 'permission:menu';
UPDATE sys_permission SET path = '/login-logs' WHERE perm_code = 'loginLog:menu';
UPDATE sys_permission SET path = '/operation-logs' WHERE perm_code = 'operationLog:menu';
