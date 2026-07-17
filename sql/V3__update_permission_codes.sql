-- 将旧版 system:xxx 权限编码统一为新版 xxx 风格
-- 已在 V2 中使用新编码，此处用于把历史数据库中的旧编码平滑迁移
UPDATE sys_permission SET perm_code = 'manage:menu' WHERE perm_code = 'system:manage:menu';
UPDATE sys_permission SET perm_code = 'user:menu' WHERE perm_code = 'system:user:menu';
UPDATE sys_permission SET perm_code = 'user:view' WHERE perm_code = 'system:user:view';
UPDATE sys_permission SET perm_code = 'user:create' WHERE perm_code = 'system:user:create';
UPDATE sys_permission SET perm_code = 'user:update' WHERE perm_code = 'system:user:update';
UPDATE sys_permission SET perm_code = 'user:delete' WHERE perm_code = 'system:user:delete';
UPDATE sys_permission SET perm_code = 'user:assign-role' WHERE perm_code = 'system:user:assign-role';
UPDATE sys_permission SET perm_code = 'user:btn:create' WHERE perm_code = 'system:user:btn:create';
UPDATE sys_permission SET perm_code = 'user:btn:update' WHERE perm_code = 'system:user:btn:update';
UPDATE sys_permission SET perm_code = 'user:btn:delete' WHERE perm_code = 'system:user:btn:delete';
UPDATE sys_permission SET perm_code = 'user:btn:assign-role' WHERE perm_code = 'system:user:btn:assign-role';

UPDATE sys_permission SET perm_code = 'role:menu' WHERE perm_code = 'system:role:menu';
UPDATE sys_permission SET perm_code = 'role:view' WHERE perm_code = 'system:role:view';
UPDATE sys_permission SET perm_code = 'role:create' WHERE perm_code = 'system:role:create';
UPDATE sys_permission SET perm_code = 'role:update' WHERE perm_code = 'system:role:update';
UPDATE sys_permission SET perm_code = 'role:delete' WHERE perm_code = 'system:role:delete';
UPDATE sys_permission SET perm_code = 'role:assign-perm' WHERE perm_code = 'system:role:assign-perm';
UPDATE sys_permission SET perm_code = 'role:btn:create' WHERE perm_code = 'system:role:btn:create';
UPDATE sys_permission SET perm_code = 'role:btn:update' WHERE perm_code = 'system:role:btn:update';
UPDATE sys_permission SET perm_code = 'role:btn:delete' WHERE perm_code = 'system:role:btn:delete';
UPDATE sys_permission SET perm_code = 'role:btn:assign-perm' WHERE perm_code = 'system:role:btn:assign-perm';

UPDATE sys_permission SET perm_code = 'permission:menu' WHERE perm_code = 'system:permission:menu';
UPDATE sys_permission SET perm_code = 'permission:view' WHERE perm_code = 'system:permission:view';
UPDATE sys_permission SET perm_code = 'permission:create' WHERE perm_code = 'system:permission:create';
UPDATE sys_permission SET perm_code = 'permission:update' WHERE perm_code = 'system:permission:update';
UPDATE sys_permission SET perm_code = 'permission:delete' WHERE perm_code = 'system:permission:delete';
UPDATE sys_permission SET perm_code = 'permission:btn:create' WHERE perm_code = 'system:permission:btn:create';
UPDATE sys_permission SET perm_code = 'permission:btn:update' WHERE perm_code = 'system:permission:btn:update';
UPDATE sys_permission SET perm_code = 'permission:btn:delete' WHERE perm_code = 'system:permission:btn:delete';

UPDATE sys_permission SET perm_code = 'log:menu' WHERE perm_code = 'system:log:menu';
UPDATE sys_permission SET perm_code = 'loginLog:menu' WHERE perm_code = 'system:login-log:menu';
UPDATE sys_permission SET perm_code = 'loginLog:view' WHERE perm_code = 'system:login-log:view';
UPDATE sys_permission SET perm_code = 'operationLog:menu' WHERE perm_code = 'system:operation-log:menu';
UPDATE sys_permission SET perm_code = 'operationLog:view' WHERE perm_code = 'system:operation-log:view';

UPDATE sys_permission SET perm_code = 'dashboard:menu' WHERE perm_code = 'system:dashboard:menu';
UPDATE sys_permission SET perm_code = 'dashboard:view' WHERE perm_code = 'system:dashboard:view';
