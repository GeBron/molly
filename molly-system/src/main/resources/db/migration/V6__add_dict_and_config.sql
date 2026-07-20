-- 数据字典类型
CREATE TABLE IF NOT EXISTS sys_dict_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
    dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0否 1是',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT 0,
    updated_by BIGINT DEFAULT 0,
    UNIQUE KEY uk_dict_type (dict_type)
);

-- 数据字典数据
CREATE TABLE IF NOT EXISTS sys_dict_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
    dict_label VARCHAR(100) NOT NULL COMMENT '字典标签',
    dict_value VARCHAR(100) NOT NULL COMMENT '字典键值',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    is_default TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认：1是 0否',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0否 1是',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT 0,
    updated_by BIGINT DEFAULT 0
);

-- 系统参数配置
CREATE TABLE IF NOT EXISTS sys_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL COMMENT '参数键',
    config_value VARCHAR(500) NOT NULL COMMENT '参数值',
    config_name VARCHAR(100) NOT NULL COMMENT '参数名称',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0否 1是',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT 0,
    updated_by BIGINT DEFAULT 0,
    UNIQUE KEY uk_config_key (config_key)
);

-- 初始化字典数据：用户状态
INSERT INTO sys_dict_type (dict_name, dict_type, status, remark, deleted, created_by, updated_by)
VALUES ('用户状态', 'user_status', 1, NULL, 0, 1, 1)
ON DUPLICATE KEY UPDATE dict_name = dict_name;

INSERT INTO sys_dict_data (dict_type, dict_label, dict_value, sort, is_default, status, remark, deleted, created_by, updated_by)
VALUES
    ('user_status', '启用', '1', 1, 1, 1, NULL, 0, 1, 1),
    ('user_status', '禁用', '0', 2, 0, 1, NULL, 0, 1, 1)
ON DUPLICATE KEY UPDATE dict_label = dict_label;

-- 初始化系统参数
INSERT INTO sys_config (config_key, config_value, config_name, status, remark, deleted, created_by, updated_by)
VALUES
    ('sys.name', 'Molly Admin', '系统名称', 1, NULL, 0, 1, 1),
    ('sys.copyright', '© 2026 Molly Admin', '版权信息', 1, NULL, 0, 1, 1)
ON DUPLICATE KEY UPDATE config_name = config_name;
