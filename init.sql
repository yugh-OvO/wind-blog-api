SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '参数主键',
    `name`        varchar(100)     DEFAULT '' COMMENT '参数名称',
    `code`        varchar(100)     DEFAULT '' COMMENT '参数键名',
    `value`       varchar(500)     DEFAULT '' COMMENT '参数键值',
    `type`        tinyint unsigned DEFAULT '2' COMMENT '系统内置（1-是 2-否）',
    `create_by`   varchar(64)      DEFAULT '' COMMENT '创建者',
    `create_time` datetime         DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64)      DEFAULT '' COMMENT '更新者',
    `update_time` datetime         DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500)     DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='参数配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(50)      DEFAULT '' COMMENT '菜单名称',
    `parent_id`   int unsigned     DEFAULT '0' COMMENT '父菜单ID',
    `sort`        int unsigned     DEFAULT '0' COMMENT '排序值',
    `type`        tinyint unsigned DEFAULT '0' COMMENT '菜单类型（1-模块 2-菜单 3-按钮）',
    `mark`        varchar(100)     DEFAULT '' COMMENT '权限标识',
    `create_by`   varchar(64)      DEFAULT '' COMMENT '创建者',
    `create_time` datetime         DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64)      DEFAULT '' COMMENT '更新者',
    `update_time` datetime         DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500)     DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '日志主键',
    `title`           varchar(50)   DEFAULT '' COMMENT '模块标题',
    `business_type`   int           DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
    `method`          varchar(100)  DEFAULT '' COMMENT '方法名称',
    `request_method`  varchar(10)   DEFAULT '' COMMENT '请求方式',
    `operator_type`   int           DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
    `name`            varchar(50)   DEFAULT '' COMMENT '操作人员',
    `department_name` varchar(50)   DEFAULT '' COMMENT '部门名称',
    `url`             varchar(255)  DEFAULT '' COMMENT '请求URL',
    `ip`              varchar(128)  DEFAULT '' COMMENT '主机地址',
    `location`        varchar(255)  DEFAULT '' COMMENT '操作地点',
    `param`           varchar(2000) DEFAULT '' COMMENT '请求参数',
    `json_result`     varchar(2000) DEFAULT '' COMMENT '返回参数',
    `status`          int           DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
    `error_msg`       varchar(2000) DEFAULT '' COMMENT '错误消息',
    `operation_time`  datetime      DEFAULT NULL COMMENT '操作时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='操作日志记录';

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name`        varchar(30)  DEFAULT NULL COMMENT '角色名称',
    `create_by`   varchar(64)  DEFAULT '' COMMENT '创建者',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64)  DEFAULT '' COMMENT '更新者',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role`
VALUES (1, '超级管理员', 'admin', '2022-09-18 15:06:06', '', NULL, '超级管理员');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `menu_id` bigint NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='角色和菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `username`    varchar(30)      DEFAULT '' COMMENT '用户账号',
    `nickname`    varchar(30)      DEFAULT '' COMMENT '用户昵称',
    `email`       varchar(100)     DEFAULT '' COMMENT '用户邮箱',
    `mobile`      char(11)         DEFAULT '' COMMENT '手机号码',
    `avatar`      varchar(255)     DEFAULT '' COMMENT '头像地址',
    `password`    varchar(100)     DEFAULT '' COMMENT '密码',
    `status`      tinyint unsigned DEFAULT '1' COMMENT '状态(1-正常，2-禁用)',
    `login_ip`    varchar(128)     DEFAULT '' COMMENT '最后登录IP',
    `login_time`  datetime         DEFAULT NULL COMMENT '最后登录时间',
    `create_by`   varchar(64)      DEFAULT '' COMMENT '创建者',
    `create_time` datetime         DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64)      DEFAULT '' COMMENT '更新者',
    `update_time` datetime         DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500)     DEFAULT '' COMMENT '备注信息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user`
VALUES (1, 'admin', '管理员', '111@163.com', '11111111111', '',
        '$2a$10$ftww/gSkGrKghrIzZFFI8exy/RGOWItzbDdfVI8WybEuyRLXZSy7K', 1, '0:0:0:0:0:0:0:1', '2022-11-14 16:17:30',
        'admin', '2022-09-18 15:06:06', 'admin', '2022-11-14 16:17:30', '管理员');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role`
VALUES (1, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
