-- 初始化超级管理员角色
INSERT IGNORE INTO sys_role (id, role_code, role_name, status, deleted, created_at, updated_at) VALUES
(1, 'admin', '超级管理员', 1, 0, NOW(), NOW());

-- 初始化超级管理员账号（默认密码 admin123）
INSERT IGNORE INTO sys_user (id, username, password, real_name, status, deleted, login_fail_count, lock_time, created_at, updated_at) VALUES
(1, 'admin', '$2b$12$2rWnDE05xD2Dwnxse1.hFewMhh/BN.1Xh/s/Yaew9gPv.2L54Hxz2', '超级管理员', 1, 0, 0, NULL, NOW(), NOW());

-- 为超级管理员分配角色
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 初始化权限数据
INSERT IGNORE INTO sys_permission (id, perm_code, perm_name, type, parent_id, path, sort, status, deleted, created_at, updated_at) VALUES
-- 系统管理目录
(1, 'system:menu', '系统管理', 1, 0, '/system', 1, 1, 0, NOW(), NOW()),

-- 用户管理
(2, 'user:menu', '用户管理', 2, 1, '/system/user', 1, 1, 0, NOW(), NOW()),
(3, 'user:view', '查看用户', 4, 2, '/api/users', 1, 1, 0, NOW(), NOW()),
(4, 'user:create', '新增用户', 4, 2, '/api/users', 2, 1, 0, NOW(), NOW()),
(5, 'user:update', '修改用户', 4, 2, '/api/users/{id}', 3, 1, 0, NOW(), NOW()),
(6, 'user:delete', '删除用户', 4, 2, '/api/users/{id}', 4, 1, 0, NOW(), NOW()),
(7, 'user:btn:create', '新增用户按钮', 3, 2, NULL, 1, 1, 0, NOW(), NOW()),
(8, 'user:btn:update', '编辑用户按钮', 3, 2, NULL, 2, 1, 0, NOW(), NOW()),
(9, 'user:btn:delete', '删除用户按钮', 3, 2, NULL, 3, 1, 0, NOW(), NOW()),
(10, 'user:btn:assign-role', '分配角色按钮', 3, 2, NULL, 4, 1, 0, NOW(), NOW()),

-- 角色管理
(11, 'role:menu', '角色管理', 2, 1, '/system/role', 2, 1, 0, NOW(), NOW()),
(12, 'role:view', '查看角色', 4, 11, '/api/roles', 1, 1, 0, NOW(), NOW()),
(13, 'role:create', '新增角色', 4, 11, '/api/roles', 2, 1, 0, NOW(), NOW()),
(14, 'role:update', '修改角色', 4, 11, '/api/roles/{id}', 3, 1, 0, NOW(), NOW()),
(15, 'role:delete', '删除角色', 4, 11, '/api/roles/{id}', 4, 1, 0, NOW(), NOW()),
(16, 'role:assign-perm', '分配权限', 4, 11, '/api/roles/{id}/permissions', 5, 1, 0, NOW(), NOW()),
(17, 'role:btn:create', '新增角色按钮', 3, 11, NULL, 1, 1, 0, NOW(), NOW()),
(18, 'role:btn:update', '编辑角色按钮', 3, 11, NULL, 2, 1, 0, NOW(), NOW()),
(19, 'role:btn:delete', '删除角色按钮', 3, 11, NULL, 3, 1, 0, NOW(), NOW()),
(20, 'role:btn:assign-perm', '分配权限按钮', 3, 11, NULL, 4, 1, 0, NOW(), NOW()),

-- 权限管理
(21, 'permission:menu', '权限管理', 2, 1, '/system/permission', 3, 1, 0, NOW(), NOW()),
(22, 'permission:view', '查看权限', 4, 21, '/api/permissions', 1, 1, 0, NOW(), NOW()),
(23, 'permission:create', '新增权限', 4, 21, '/api/permissions', 2, 1, 0, NOW(), NOW()),
(24, 'permission:update', '修改权限', 4, 21, '/api/permissions/{id}', 3, 1, 0, NOW(), NOW()),
(25, 'permission:delete', '删除权限', 4, 21, '/api/permissions/{id}', 4, 1, 0, NOW(), NOW()),
(26, 'permission:btn:create', '新增权限按钮', 3, 21, NULL, 1, 1, 0, NOW(), NOW()),
(27, 'permission:btn:update', '编辑权限按钮', 3, 21, NULL, 2, 1, 0, NOW(), NOW()),
(28, 'permission:btn:delete', '删除权限按钮', 3, 21, NULL, 3, 1, 0, NOW(), NOW()),

-- 日志管理目录
(29, 'log:menu', '日志管理', 1, 1, '/system/log', 4, 1, 0, NOW(), NOW()),
(30, 'loginLog:menu', '登录日志', 2, 29, '/system/login-log', 1, 1, 0, NOW(), NOW()),
(31, 'loginLog:view', '查看登录日志', 4, 30, '/api/logs/login', 1, 1, 0, NOW(), NOW()),
(32, 'operationLog:menu', '操作日志', 2, 29, '/system/operation-log', 2, 1, 0, NOW(), NOW()),
(33, 'operationLog:view', '查看操作日志', 4, 32, '/api/logs/operation', 1, 1, 0, NOW(), NOW()),

-- 首页
(34, 'dashboard:menu', '首页', 2, 0, '/dashboard', 0, 1, 0, NOW(), NOW()),
(35, 'dashboard:view', '查看首页', 4, 34, '/api/dashboard', 1, 1, 0, NOW(), NOW());

-- 为超级管理员角色分配所有权限
INSERT IGNORE INTO sys_role_permission (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10),
(1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17), (1, 18), (1, 19), (1, 20),
(1, 21), (1, 22), (1, 23), (1, 24), (1, 25), (1, 26), (1, 27), (1, 28), (1, 29), (1, 30),
(1, 31), (1, 32), (1, 33), (1, 34), (1, 35);
