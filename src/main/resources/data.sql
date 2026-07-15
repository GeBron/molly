-- 初始化超级管理员角色
INSERT IGNORE INTO sys_role (id, role_code, role_name, status, created_at, updated_at, created_by, updated_by, deleted) VALUES
(1, 'admin', '超级管理员', 1, NOW(), NOW(), 1, 1, 0);

-- 初始化超级管理员账号（默认密码 admin123）
INSERT IGNORE INTO sys_user (id, username, password, real_name, status, login_fail_count, lock_time, created_at, updated_at, created_by, updated_by, deleted) VALUES
(1, 'admin', '$2b$12$2rWnDE05xD2Dwnxse1.hFewMhh/BN.1Xh/s/Yaew9gPv.2L54Hxz2', '超级管理员', 1, 0, NULL, NOW(), NOW(), 1, 1, 0);

-- 为超级管理员分配角色
INSERT IGNORE INTO sys_user_role (id, user_id, role_id, created_at, updated_at, created_by, updated_by, deleted) VALUES
(1, 1, 1, NOW(), NOW(), 1, 1, 0);

-- 初始化权限数据
INSERT IGNORE INTO sys_permission (id, perm_code, perm_name, type, parent_id, path, sort, status, created_at, updated_at, created_by, updated_by, deleted) VALUES
-- 系统管理目录
(1, 'system:manage:menu', '系统管理', 1, 0, '/system', 1, 1, NOW(), NOW(), 1, 1, 0),

-- 用户管理
(2, 'system:user:menu', '用户管理', 2, 1, '/system/user', 1, 1, NOW(), NOW(), 1, 1, 0),
(3, 'system:user:view', '查看用户', 4, 2, '/api/users', 1, 1, NOW(), NOW(), 1, 1, 0),
(4, 'system:user:create', '新增用户', 4, 2, '/api/users', 2, 1, NOW(), NOW(), 1, 1, 0),
(5, 'system:user:update', '修改用户', 4, 2, '/api/users/{id}', 3, 1, NOW(), NOW(), 1, 1, 0),
(6, 'system:user:delete', '删除用户', 4, 2, '/api/users/{id}', 4, 1, NOW(), NOW(), 1, 1, 0),
(7, 'system:user:assign-role', '分配角色', 4, 2, '/api/users/{id}/roles', 5, 1, NOW(), NOW(), 1, 1, 0),
(8, 'system:user:btn:create', '新增用户按钮', 3, 2, NULL, 1, 1, NOW(), NOW(), 1, 1, 0),
(9, 'system:user:btn:update', '编辑用户按钮', 3, 2, NULL, 2, 1, NOW(), NOW(), 1, 1, 0),
(10, 'system:user:btn:delete', '删除用户按钮', 3, 2, NULL, 3, 1, NOW(), NOW(), 1, 1, 0),
(11, 'system:user:btn:assign-role', '分配角色按钮', 3, 2, NULL, 4, 1, NOW(), NOW(), 1, 1, 0),

-- 角色管理
(12, 'system:role:menu', '角色管理', 2, 1, '/system/role', 2, 1, NOW(), NOW(), 1, 1, 0),
(13, 'system:role:view', '查看角色', 4, 12, '/api/roles', 1, 1, NOW(), NOW(), 1, 1, 0),
(14, 'system:role:create', '新增角色', 4, 12, '/api/roles', 2, 1, NOW(), NOW(), 1, 1, 0),
(15, 'system:role:update', '修改角色', 4, 12, '/api/roles/{id}', 3, 1, NOW(), NOW(), 1, 1, 0),
(16, 'system:role:delete', '删除角色', 4, 12, '/api/roles/{id}', 4, 1, NOW(), NOW(), 1, 1, 0),
(17, 'system:role:assign-perm', '分配权限', 4, 12, '/api/roles/{id}/permissions', 5, 1, NOW(), NOW(), 1, 1, 0),
(18, 'system:role:btn:create', '新增角色按钮', 3, 12, NULL, 1, 1, NOW(), NOW(), 1, 1, 0),
(19, 'system:role:btn:update', '编辑角色按钮', 3, 12, NULL, 2, 1, NOW(), NOW(), 1, 1, 0),
(20, 'system:role:btn:delete', '删除角色按钮', 3, 12, NULL, 3, 1, NOW(), NOW(), 1, 1, 0),
(21, 'system:role:btn:assign-perm', '分配权限按钮', 3, 12, NULL, 4, 1, NOW(), NOW(), 1, 1, 0),

-- 权限管理
(22, 'system:permission:menu', '权限管理', 2, 1, '/system/permission', 3, 1, NOW(), NOW(), 1, 1, 0),
(23, 'system:permission:view', '查看权限', 4, 22, '/api/permissions', 1, 1, NOW(), NOW(), 1, 1, 0),
(24, 'system:permission:create', '新增权限', 4, 22, '/api/permissions', 2, 1, NOW(), NOW(), 1, 1, 0),
(25, 'system:permission:update', '修改权限', 4, 22, '/api/permissions/{id}', 3, 1, NOW(), NOW(), 1, 1, 0),
(26, 'system:permission:delete', '删除权限', 4, 22, '/api/permissions/{id}', 4, 1, NOW(), NOW(), 1, 1, 0),
(27, 'system:permission:btn:create', '新增权限按钮', 3, 22, NULL, 1, 1, NOW(), NOW(), 1, 1, 0),
(28, 'system:permission:btn:update', '编辑权限按钮', 3, 22, NULL, 2, 1, NOW(), NOW(), 1, 1, 0),
(29, 'system:permission:btn:delete', '删除权限按钮', 3, 22, NULL, 3, 1, NOW(), NOW(), 1, 1, 0),

-- 日志管理目录
(30, 'system:log:menu', '日志管理', 1, 1, '/system/log', 4, 1, NOW(), NOW(), 1, 1, 0),
(31, 'system:login-log:menu', '登录日志', 2, 30, '/system/login-log', 1, 1, NOW(), NOW(), 1, 1, 0),
(32, 'system:login-log:view', '查看登录日志', 4, 31, '/api/logs/login', 1, 1, NOW(), NOW(), 1, 1, 0),
(33, 'system:operation-log:menu', '操作日志', 2, 30, '/system/operation-log', 2, 1, NOW(), NOW(), 1, 1, 0),
(34, 'system:operation-log:view', '查看操作日志', 4, 33, '/api/logs/operation', 1, 1, NOW(), NOW(), 1, 1, 0),

-- 首页
(35, 'system:dashboard:menu', '首页', 2, 0, '/dashboard', 0, 1, NOW(), NOW(), 1, 1, 0),
(36, 'system:dashboard:view', '查看首页', 4, 35, '/api/dashboard', 1, 1, NOW(), NOW(), 1, 1, 0);

-- 为超级管理员角色分配所有权限
INSERT IGNORE INTO sys_role_permission (id, role_id, permission_id, created_at, updated_at, created_by, updated_by, deleted) VALUES
(1, 1, 1, NOW(), NOW(), 1, 1, 0),
(2, 1, 2, NOW(), NOW(), 1, 1, 0),
(3, 1, 3, NOW(), NOW(), 1, 1, 0),
(4, 1, 4, NOW(), NOW(), 1, 1, 0),
(5, 1, 5, NOW(), NOW(), 1, 1, 0),
(6, 1, 6, NOW(), NOW(), 1, 1, 0),
(7, 1, 7, NOW(), NOW(), 1, 1, 0),
(8, 1, 8, NOW(), NOW(), 1, 1, 0),
(9, 1, 9, NOW(), NOW(), 1, 1, 0),
(10, 1, 10, NOW(), NOW(), 1, 1, 0),
(11, 1, 11, NOW(), NOW(), 1, 1, 0),
(12, 1, 12, NOW(), NOW(), 1, 1, 0),
(13, 1, 13, NOW(), NOW(), 1, 1, 0),
(14, 1, 14, NOW(), NOW(), 1, 1, 0),
(15, 1, 15, NOW(), NOW(), 1, 1, 0),
(16, 1, 16, NOW(), NOW(), 1, 1, 0),
(17, 1, 17, NOW(), NOW(), 1, 1, 0),
(18, 1, 18, NOW(), NOW(), 1, 1, 0),
(19, 1, 19, NOW(), NOW(), 1, 1, 0),
(20, 1, 20, NOW(), NOW(), 1, 1, 0),
(21, 1, 21, NOW(), NOW(), 1, 1, 0),
(22, 1, 22, NOW(), NOW(), 1, 1, 0),
(23, 1, 23, NOW(), NOW(), 1, 1, 0),
(24, 1, 24, NOW(), NOW(), 1, 1, 0),
(25, 1, 25, NOW(), NOW(), 1, 1, 0),
(26, 1, 26, NOW(), NOW(), 1, 1, 0),
(27, 1, 27, NOW(), NOW(), 1, 1, 0),
(28, 1, 28, NOW(), NOW(), 1, 1, 0),
(29, 1, 29, NOW(), NOW(), 1, 1, 0),
(30, 1, 30, NOW(), NOW(), 1, 1, 0),
(31, 1, 31, NOW(), NOW(), 1, 1, 0),
(32, 1, 32, NOW(), NOW(), 1, 1, 0),
(33, 1, 33, NOW(), NOW(), 1, 1, 0),
(34, 1, 34, NOW(), NOW(), 1, 1, 0),
(35, 1, 35, NOW(), NOW(), 1, 1, 0),
(36, 1, 36, NOW(), NOW(), 1, 1, 0);
