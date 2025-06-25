/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : 127.0.0.1:3306
 Source Schema         : miyu-mcs

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 04/07/2024 16:02:37
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `databasechangeloglock`;
CREATE TABLE `databasechangeloglock`
(
    `ID`          int(11) NOT NULL,
    `LOCKED`      bit(1) NOT NULL,
    `LOCKGRANTED` datetime(0) NULL DEFAULT NULL,
    `LOCKEDBY`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of databasechangeloglock
-- ----------------------------
INSERT INTO `databasechangeloglock`
VALUES (1, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for dms_device_type
-- ----------------------------
DROP TABLE IF EXISTS `dms_device_type`;
CREATE TABLE `dms_device_type`
(
    `id`             varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `code`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型编号',
    `name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名称',
    `enable`         int(255) NULL DEFAULT NULL COMMENT '是否启用',
    `specification`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格型号',
    `manufacturer`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产厂家',
    `country_region` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产地',
    `contacts`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '厂家联系人',
    `contact_phone`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '厂家联系电话',
    `remark`         text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
    `creator`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`    datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`    datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`        bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_device_type
-- ----------------------------
INSERT INTO `dms_device_type`
VALUES ('1803267202606936066', 'HDCHF', '5号流水线', 1, '1000米', '北京地铁值', '北京', '规划好', '123123123121231',
        '1备注测试值，备注测试值，备注测试值。备注测试值，备注测试值，备注测试值。\n2备注测试值，备注测试值，备注测试值。备注测试值，备注测试值，备注测试值。\n3备注测试值，备注测试值，备注测试值。备注测试值，备注测试值，备注测试值。\n4备注测试值，备注测试值，备注测试值。备注测试值，备注测试值，备注测试值。\n5备注测试值，备注测试值，备注测试值。备注测试值，备注测试值，备注测试值。\n6备注测试值，备注测试值，备注测试值。备注测试值，备注测试值，备注测试值。\n7备注测试值，备注测试值，备注测试值。备注测试值，备注测试值，备注测试值。\n8备注测试值，备注测试值，备注测试值。备注测试值，备注测试值，备注测试值。\n9备注测试值，备注测试值，备注测试值。备注测试值，备注测试值，备注测试值。',
        '1', '2024-06-19 11:22:59', '1', '2024-06-28 10:24:41', b'0');
INSERT INTO `dms_device_type`
VALUES ('1803293176006422529', 'CSEE', '测试类型', 1, '测试规格型号', NULL, NULL, NULL, NULL, NULL, '1',
        '2024-06-19 13:06:11', '1', '2024-07-02 13:46:28', b'0');
INSERT INTO `dms_device_type`
VALUES ('1803293209355333633', 'SKHG', 'sdfgsd', 1, NULL, NULL, NULL, NULL, NULL, NULL, '1', '2024-06-19 13:06:19', '1',
        '2024-06-28 10:24:33', b'0');

-- ----------------------------
-- Table structure for dms_failure_record
-- ----------------------------
DROP TABLE IF EXISTS `dms_failure_record`;
CREATE TABLE `dms_failure_record`
(
    `id`                varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `device`            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备',
    `code`              varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '异常编码',
    `fault_state`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '故障状态',
    `description`       varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '故障描述',
    `cause`             varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '故障原因',
    `fault_time`        datetime(0) NULL DEFAULT NULL COMMENT '故障时间',
    `maintenance_by`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '维修人员',
    `repair_time`       datetime(0) NULL DEFAULT NULL COMMENT '修复时间',
    `restoration_costs` decimal(10, 2) NULL DEFAULT NULL COMMENT '维修费用',
    `remarks`           varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `creator`           varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`       datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`           varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`       datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`           bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '异常记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_failure_record
-- ----------------------------
INSERT INTO `dms_failure_record`
VALUES ('1802619145846484994', '1802867206887796738', 'qq', '2', '劣化', '老化', '2024-06-01 10:05:53', '112',
        '2024-06-16 00:00:00', 100.00, NULL, '1', '2024-06-18 09:46:13', '1', '2024-07-03 10:30:23', b'0');
INSERT INTO `dms_failure_record`
VALUES ('1802988492357808130', '1802867206887796738', 'aa', '0', '突发', '磨损', '2024-06-12 00:00:00', '104', NULL,
        NULL, NULL, '1', '2024-06-18 16:55:29', '1', '2024-07-03 10:30:26', b'0');
INSERT INTO `dms_failure_record`
VALUES ('1803236774648709122', '1802867206887796738', 'bb', '2', '突发', '损坏', '2024-06-02 00:00:00', '1',
        '2024-06-18 00:00:00', NULL, NULL, '1', '2024-06-19 09:22:04', '1', '2024-07-03 10:30:29', b'0');

-- ----------------------------
-- Table structure for dms_inspection_plan
-- ----------------------------
DROP TABLE IF EXISTS `dms_inspection_plan`;
CREATE TABLE `dms_inspection_plan`
(
    `id`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `code`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划编码',
    `tree`            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属计划关联树',
    `device`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备',
    `enable_status`   int(255) NULL DEFAULT NULL COMMENT '启用状态',
    `type`            int(2) NULL DEFAULT NULL COMMENT '检查类型',
    `superintendent`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
    `start_time`      datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
    `corn_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
    `content`         varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检查内容',
    `remark`          varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
    `creator`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`     datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`     datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备检查计划' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_inspection_plan
-- ----------------------------
INSERT INTO `dms_inspection_plan`
VALUES ('1806965246934347777', 'DFCFS', '1807575757057114113', '1802867206887796738', 1, 1, NULL, '2024-06-05 00:00:00',
        '111', '[{\"name\":\"是否损坏\"},{\"name\":\"是否磨损\"},{\"name\":\"是否XXX\"}]', '111', '1',
        '2024-06-29 16:17:41', '1', '2024-07-01 08:43:52', b'1');
INSERT INTO `dms_inspection_plan`
VALUES ('1807599608675647490', '12312', '1806615456237142018', '1802867206887796738', NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, '1', '2024-07-01 10:18:25', '1', '2024-07-01 10:18:25', b'1');
INSERT INTO `dms_inspection_plan`
VALUES ('1807645923065413633', 'JNDHGDVJH', '1805061448649334786', '1802867206887796738', 1, 1, 'super_admin',
        '2024-07-01 13:22:04', NULL,
        '[{\"name\":\"检查内容1\"},{\"name\":\"检查内容2\"},{\"name\":\"检查内容3\"},{\"name\":\"检查内容4\"}]', NULL,
        '1', '2024-07-01 13:22:27', '1', '2024-07-04 14:03:41', b'0');
INSERT INTO `dms_inspection_plan`
VALUES ('1807676701337927681', 'FEASF', '1806594867770355713', '1806595500317536258', 1, 2, 'super_admin',
        '2024-07-01 15:24:34', NULL,
        '[{\"name\":\"waerfwea\"},{\"name\":\"waefwaef\"},{\"name\":\"waefwaeff\"},{\"name\":\"Fqf\"}]', NULL, '1',
        '2024-07-01 15:24:45', '1', '2024-07-04 14:12:43', b'0');

-- ----------------------------
-- Table structure for dms_inspection_plan_notice
-- ----------------------------
DROP TABLE IF EXISTS `dms_inspection_plan_notice`;
CREATE TABLE `dms_inspection_plan_notice`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `plan_id`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划ID',
    `status`      int(255) NULL DEFAULT NULL COMMENT '状态',
    `creator`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备检查计划提醒表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_inspection_plan_notice
-- ----------------------------
INSERT INTO `dms_inspection_plan_notice`
VALUES ('1808701973872979969', '1807676701337927681', 0, '1', '2024-07-04 11:18:49', '1', '2024-07-04 16:00:45', b'0');

-- ----------------------------
-- Table structure for dms_inspection_record
-- ----------------------------
DROP TABLE IF EXISTS `dms_inspection_record`;
CREATE TABLE `dms_inspection_record`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `code`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划编码',
    `device`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备',
    `type`        int(2) NULL DEFAULT NULL COMMENT '检查类型',
    `remark`      varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `content`     varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检查内容',
    `create_by`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检查人',
    `start_time`  datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
    `end_time`    datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
    `creator`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备检查记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_inspection_record
-- ----------------------------
INSERT INTO `dms_inspection_record`
VALUES ('1803328981464420354', 'as', '1802867206887796738', 1, '111',
        '[{\"name\":\"检查选项1\",\"value\":true},{\"name\":\"检查选项2\",\"value\":false}]', '1',
        '2024-06-05 16:16:34', '2024-06-18 16:16:39', '1', '2024-06-19 15:28:28', '1', '2024-07-01 10:20:11', b'0');
INSERT INTO `dms_inspection_record`
VALUES ('1803350818005082113', 'fd', '1802867206887796738', 1, '111',
        '[{\"name\":\"检查选项1\",\"value\":true},{\"name\":\"检查选项2\",\"value\":false}]', '104',
        '2024-06-12 02:00:01', '2024-06-18 00:00:00', '1', '2024-06-19 16:55:14', '1', '2024-07-03 10:28:02', b'0');
INSERT INTO `dms_inspection_record`
VALUES ('1803351164576227329', 'jkl', '1802867206887796738', 2, '111',
        '[{\"name\":\"检查选项1\",\"value\":true},{\"name\":\"检查选项2\",\"value\":false}]', '1',
        '2024-06-13 00:00:00', '2024-06-18 00:00:00', '1', '2024-06-19 16:56:37', '1', '2024-07-03 10:28:08', b'0');
INSERT INTO `dms_inspection_record`
VALUES ('1807646438377603074', 'JNDHGDVJH', '1802867206887796738', 1, '饿得要死', NULL, '1', '2024-07-10 00:00:00',
        '2024-07-18 00:00:00', '1', '2024-07-01 13:24:30', '1', '2024-07-01 13:24:30', b'0');
INSERT INTO `dms_inspection_record`
VALUES ('1807676791121199106', 'FEASF', '1806595500317536258', 2, 'qwdqwdqwdqwd',
        '[{\"name\":\"waerfwea\",\"value\":true},{\"name\":\"waefwaef\",\"value\":false},{\"name\":\"waefwaeff\",\"value\":true},{\"name\":\"Fqf\",\"value\":false}]',
        '1', '2024-07-04 00:00:00', '2024-07-24 00:00:00', '1', '2024-07-01 15:25:07', '1', '2024-07-01 15:25:07',
        b'0');
INSERT INTO `dms_inspection_record`
VALUES ('1807677033124151297', 'FEASF', '1806595500317536258', 2,
        '11111111111111111111111111111111111111111111111111111111111111111111111111111111111',
        '[{\"name\":\"waerfwea\",\"value\":true},{\"name\":\"waefwaef\",\"value\":false},{\"name\":\"waefwaeff\",\"value\":true},{\"name\":\"Fqf\",\"value\":false}]',
        '1', NULL, NULL, '1', '2024-07-01 15:26:04', '1', '2024-07-01 15:26:04', b'0');
INSERT INTO `dms_inspection_record`
VALUES ('1808755074294312961', 'FEASF', '1806595500317536258', 2, '123',
        '[{\"name\":\"waerfwea\",\"value\":true},{\"name\":\"waefwaef\",\"value\":true},{\"name\":\"waefwaeff\",\"value\":false},{\"name\":\"Fqf\",\"value\":false}]',
        '1', '2024-07-04 14:49:39', '2024-07-17 00:00:00', '1', '2024-07-04 14:49:49', '1', '2024-07-04 14:49:49',
        b'0');
INSERT INTO `dms_inspection_record`
VALUES ('1808772922743943169', 'FEASF', '1806595500317536258', 2, '777777777777777777777777777777777',
        '[{\"name\":\"waerfwea\",\"value\":false},{\"name\":\"waefwaef\",\"value\":true},{\"name\":\"waefwaeff\",\"value\":true},{\"name\":\"Fqf\",\"value\":false}]',
        '1', '2024-07-04 16:00:37', NULL, '1', '2024-07-04 16:00:45', '1', '2024-07-04 16:00:45', b'0');

-- ----------------------------
-- Table structure for dms_ledger
-- ----------------------------
DROP TABLE IF EXISTS `dms_ledger`;
CREATE TABLE `dms_ledger`
(
    `id`                   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `code`                 varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
    `name`                 varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称',
    `type`                 varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型',
    `status`               int(255) NULL DEFAULT NULL COMMENT '状态',
    `processing_unit`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属生产单元',
    `superintendent`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
    `maintenance_date`     datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '维护日期',
    `maintenance_by`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '维护人员',
    `inspection_date`      datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '检验日期',
    `purchase_date`        datetime(0) NULL DEFAULT NULL COMMENT '采购日期',
    `technical_parameter1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数1',
    `technical_parameter2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数2',
    `technical_parameter3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数3',
    `technical_parameter4` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数4',
    `creator`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`          datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`          datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`              bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备台账' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_ledger
-- ----------------------------
INSERT INTO `dms_ledger`
VALUES ('1802867206887796738', 'HJKLSD', '一区2号5型流水线', '1803267202606936066', 0, '1801416307278745601', '1',
        '2024-06-18 08:53:32', '付费更换', '2024-06-21 00:00:00', '2024-06-13 00:00:00', NULL, NULL, NULL, '111', '1',
        '2024-06-18 08:53:32', '1', '2024-07-03 10:25:14', b'0');
INSERT INTO `dms_ledger`
VALUES ('1806595500317536258', 'LKSDN', '测试设备', '1803293176006422529', 0, '1801416307278745601', '104',
        '2024-06-28 15:48:26', NULL, '2024-06-28 15:48:26', NULL, NULL, NULL, NULL, NULL, '1', '2024-06-28 15:48:27',
        '1', '2024-07-03 10:25:10', b'0');

-- ----------------------------
-- Table structure for dms_maintain_application
-- ----------------------------
DROP TABLE IF EXISTS `dms_maintain_application`;
CREATE TABLE `dms_maintain_application`
(
    `id`                     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `device`                 varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备id',
    `code`                   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编码',
    `name`                   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称',
    `processing_unit_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产单元编号',
    `model`                  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备型号',
    `important`              tinyint(1) NULL DEFAULT NULL COMMENT '关键设备',
    `type`                   int(2) NULL DEFAULT NULL COMMENT '维修类型',
    `describe1`              varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '故障信息描述',
    `duration`               int(10) NULL DEFAULT NULL COMMENT '期望修复时间',
    `status`                 tinyint(2) NULL DEFAULT NULL COMMENT '申请状态',
    `process_instance_id`    varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程实例编号',
    `applicant`              varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请人',
    `application_time`       datetime(0) NULL DEFAULT NULL COMMENT '申请时间',
    `creator`                varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`            datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`                varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`            datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `deleted`                bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备维修申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_maintain_application
-- ----------------------------
INSERT INTO `dms_maintain_application`
VALUES ('1808070784325697537', '1802867206887796738', 'HJKLSD', '一区2号5型流水线', '1801416307278745601', NULL, NULL,
        NULL, '111', NULL, 3, 'c034b5cd-3855-11ef-839b-d843ae7975d2', '1', '2024-07-02 17:30:42', '1',
        '2024-07-02 17:30:42', '1', '2024-07-02 17:31:00', b'0');
INSERT INTO `dms_maintain_application`
VALUES ('1808071306856284161', '1802867206887796738', 'HJKLSD', '一区2号5型流水线', '1801416307278745601', NULL, NULL,
        NULL, '222222', NULL, 1, '0a76914a-3856-11ef-839b-d843ae7975d2', '1', '2024-07-02 17:32:47', '1',
        '2024-07-02 17:32:47', '1', '2024-07-02 17:32:47', b'0');
INSERT INTO `dms_maintain_application`
VALUES ('1808072122103152642', '1802867206887796738', 'HJKLSD', '一区2号5型流水线', '1801416307278745601', NULL, 1,
        NULL, '444', NULL, 1, '7e4fda0a-3856-11ef-839b-d843ae7975d2', '1', '2024-07-02 17:36:01', '1',
        '2024-07-02 17:36:01', '1', '2024-07-02 17:36:01', b'0');

-- ----------------------------
-- Table structure for dms_maintenance_plan
-- ----------------------------
DROP TABLE IF EXISTS `dms_maintenance_plan`;
CREATE TABLE `dms_maintenance_plan`
(
    `id`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `code`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划编码',
    `tree`            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属计划关联树',
    `device`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备',
    `critical_device` int(255) NULL DEFAULT NULL COMMENT '是否为关键设备',
    `enable_status`   int(255) NULL DEFAULT NULL COMMENT '启用状态',
    `type`            int(2) NULL DEFAULT NULL COMMENT '维护类型',
    `start_time`      datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
    `corn_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
    `content`         varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '维护内容',
    `remark`          varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
    `superintendent`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
    `last_time`       datetime(0) NULL DEFAULT NULL COMMENT '最后一次保养时间',
    `last_status`     int(2) NULL DEFAULT NULL COMMENT '上一次完成状态',
    `creator`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`     datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`     datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备保养维护计划' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_maintenance_plan
-- ----------------------------
INSERT INTO `dms_maintenance_plan`
VALUES ('1804057139488968705', '11', '1806594867770355713', '1806595500317536258', 0, 1, 2, '2024-06-04 02:08:02',
        '1111', '[{\"name\":\"内容1\"},{\"name\":\"内容2\"},{\"name\":\"内容3\"}]', '111', '117', '2024-06-06 03:01:02',
        3, '1', '2024-06-21 15:41:54', '1', '2024-07-03 10:05:52', b'0');
INSERT INTO `dms_maintenance_plan`
VALUES ('1805049773875408898', '123', '1805061448649334786', '1802867206887796738', 1, 1, 0, NULL, NULL,
        '[{\"name\":\"123\"}]', NULL, '112', NULL, NULL, '1', '2024-06-24 09:26:17', '1', '2024-07-03 10:05:47', b'0');
INSERT INTO `dms_maintenance_plan`
VALUES ('1806885246512005122', NULL, '1806615417834094594', '1802867206887796738', NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, '1', '2024-06-29 10:59:48', '1', '2024-06-29 10:59:48', b'1');
INSERT INTO `dms_maintenance_plan`
VALUES ('1806885295673442306', NULL, '1806615417834094594', '1806595500317536258', NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, '1', '2024-06-29 10:59:59', '1', '2024-06-29 10:59:59', b'1');
INSERT INTO `dms_maintenance_plan`
VALUES ('1806885419745148930', NULL, '1806615417834094594', '1802867206887796738', NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, '117', NULL, NULL, '1', '2024-06-29 11:00:29', '1', '2024-07-03 10:05:43', b'0');
INSERT INTO `dms_maintenance_plan`
VALUES ('1806928929332527105', '675', '1806615417834094594', '1802867206887796738', 1, 1, 1, '2024-06-29 13:53:15',
        NULL,
        '[{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658668658687三个serge认识而是如果是责任，87三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\"}]',
        NULL, '117', '2024-06-29 15:18:42', 1, '1', '2024-06-29 13:53:23', '1', '2024-07-03 10:05:39', b'0');
INSERT INTO `dms_maintenance_plan`
VALUES ('1806946377528115201', '23432', '1806595115313983489', '1802867206887796738', 0, 1, 0, NULL, NULL,
        '[{\"name\":\"ytyhxtszgsdr\"},{\"name\":\"xzrgxrg\"},{\"name\":\"zrgzr\"},{\"name\":\"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111\"}]',
        NULL, '112', '2024-07-01 15:28:51', 1, '1', '2024-06-29 15:02:43', '1', '2024-07-03 10:05:35', b'0');
INSERT INTO `dms_maintenance_plan`
VALUES ('1806947203336241154', '45645', '1806615456237142018', '1802867206887796738', 0, NULL, 1, NULL, NULL, NULL,
        NULL, '1', '2024-07-30 00:00:00', 0, '1', '2024-06-29 15:05:59', '1', '2024-07-03 15:42:46', b'0');

-- ----------------------------
-- Table structure for dms_maintenance_record
-- ----------------------------
DROP TABLE IF EXISTS `dms_maintenance_record`;
CREATE TABLE `dms_maintenance_record`
(
    `id`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `code`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划编码',
    `device`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备',
    `critical_device` int(255) NULL DEFAULT NULL COMMENT '是否为关键设备',
    `type`            int(2) NULL DEFAULT NULL COMMENT '保养类型',
    `status`          int(2) NULL DEFAULT NULL COMMENT '完成状态',
    `remarks`         text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
    `content`         text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '保养内容',
    `maintenance_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保养人',
    `start_time`      datetime(0) NULL DEFAULT NULL COMMENT '开始维护时间',
    `end_time`        datetime(0) NULL DEFAULT NULL COMMENT '结束维护时间',
    `creator`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`     datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`     datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备保养维护记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_maintenance_record
-- ----------------------------
INSERT INTO `dms_maintenance_record`
VALUES ('1803678060122193921', '234', '1802867206887796738', 0, 2, 3, '2342',
        '[{\"name\":\"内容1\",\"value\":\"值1\"},{\"name\":\"内容2\",\"value\":\"值2\"}]', '1', '2024-06-10 15:11:13',
        '2024-06-20 11:11:14', '1', '2024-06-20 14:35:35', '1', '2024-07-03 09:37:53', b'0');
INSERT INTO `dms_maintenance_record`
VALUES ('1806950723208740865', '675', '1802867206887796738', 1, 1, 1, '5uirus5ey46YS54YS45嗄也↗4而尔特瑞特人',
        '[{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\",\"value\":\"是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\",\"value\":\"是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\",\"value\":\"是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\",\"value\":\"是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658668658687三个serge认识而是如果是责任，87三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\",\"value\":\"是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\",\"value\":\"是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\",\"value\":\"是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔\"},{\"name\":\"68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，68658687三个serge认识而是如果是责任，\",\"value\":\"是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔是否为娃儿发瓦尔\"}]',
        '138', '2024-06-29 15:18:41', '2024-06-29 15:18:42', '1', '2024-06-29 15:19:59', '1', '2024-07-03 09:40:06',
        b'0');
INSERT INTO `dms_maintenance_record`
VALUES ('1807677768012349441', '23432', '1802867206887796738', 0, 0, 1, '111111111111111111111',
        '[{\"name\":\"ytyhxtszgsdr\",\"value\":\"1\"},{\"name\":\"xzrgxrg\",\"value\":\"1\"},{\"name\":\"zrgzr\",\"value\":\"1\"},{\"name\":\"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111\",\"value\":\"1\"}]',
        '112', '2024-07-18 00:00:00', '2024-07-01 15:28:51', '1', '2024-07-01 15:29:00', '1', '2024-07-03 09:40:10',
        b'0');
INSERT INTO `dms_maintenance_record`
VALUES ('1807965507484000257', '45645', '1802867206887796738', 0, NULL, NULL, '4天气3提前', NULL, '1',
        '2024-07-02 10:32:17', NULL, '1', '2024-07-02 10:32:22', '1', '2024-07-03 09:39:22', b'0');
INSERT INTO `dms_maintenance_record`
VALUES ('1808417371728060417', '45645', '1802867206887796738', 0, 1, 0, '123', NULL, '1', '2024-07-03 16:27:46',
        '2024-07-30 00:00:00', '1', '2024-07-03 16:27:55', '1', '2024-07-03 16:27:55', b'0');

-- ----------------------------
-- Table structure for dms_plan_tree
-- ----------------------------
DROP TABLE IF EXISTS `dms_plan_tree`;
CREATE TABLE `dms_plan_tree`
(
    `id`             varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `parent`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父节点',
    `name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点名',
    `remark`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `device_id`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联设备',
    `device_type_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联设备类型',
    `creator`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`    datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`    datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`        bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '计划关联树' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_plan_tree
-- ----------------------------
INSERT INTO `dms_plan_tree`
VALUES ('1805061448649334786', NULL, '一区', NULL, NULL, NULL, '1', '2024-06-24 10:12:40', '1', '2024-06-28 15:45:50',
        b'0');
INSERT INTO `dms_plan_tree`
VALUES ('1806594867770355713', NULL, '二区', NULL, NULL, NULL, '1', '2024-06-28 15:45:56', '1', '2024-06-28 15:45:56',
        b'0');
INSERT INTO `dms_plan_tree`
VALUES ('1806594889228414977', NULL, '三区', NULL, '', '', '1', '2024-06-28 15:46:01', '1', '2024-06-28 15:51:32',
        b'0');
INSERT INTO `dms_plan_tree`
VALUES ('1806594956941258753', '1805061448649334786', '产线设备', NULL, NULL, NULL, '1', '2024-06-28 15:46:17', '1',
        '2024-06-28 15:46:17', b'0');
INSERT INTO `dms_plan_tree`
VALUES ('1806594995474329602', '1806594867770355713', '生产设备', NULL, NULL, NULL, '1', '2024-06-28 15:46:27', '1',
        '2024-06-28 15:46:27', b'0');
INSERT INTO `dms_plan_tree`
VALUES ('1806595039590019073', '1806594889228414977', '维护设备', NULL, NULL, NULL, '1', '2024-06-28 15:46:37', '1',
        '2024-06-28 15:46:37', b'0');
INSERT INTO `dms_plan_tree`
VALUES ('1806595115313983489', '1806594956941258753', '23号产线', NULL, NULL, NULL, '1', '2024-06-28 15:46:55', '1',
        '2024-06-28 15:46:55', b'0');
INSERT INTO `dms_plan_tree`
VALUES ('1806615417834094594', NULL, '设备区', NULL, '1802867206887796738', '', '1', '2024-06-28 17:07:36', '1',
        '2024-06-28 17:07:36', b'0');
INSERT INTO `dms_plan_tree`
VALUES ('1806615456237142018', NULL, '设备类型', NULL, '', '1803267202606936066', '1', '2024-06-28 17:07:45', '1',
        '2024-06-28 17:07:45', b'0');
INSERT INTO `dms_plan_tree`
VALUES ('1807575757057114113', '1806595115313983489', '测试', NULL, NULL, NULL, '1', '2024-07-01 08:43:38', '1',
        '2024-07-01 08:43:38', b'0');

-- ----------------------------
-- Table structure for dms_processing_unit
-- ----------------------------
DROP TABLE IF EXISTS `dms_processing_unit`;
CREATE TABLE `dms_processing_unit`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `type_id`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型Id',
    `unit_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单元编码',
    `unit_name`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单元名称',
    `unit_status` int(2) NULL DEFAULT NULL COMMENT '状态',
    `remarks`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `creator`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '加工单元' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dms_processing_unit
-- ----------------------------
INSERT INTO `dms_processing_unit`
VALUES ('1801416307278745601', '123123', 'ABC', '测试1', 1, '123', '1', '2024-06-14 08:48:11', '1',
        '2024-06-14 08:48:11', b'0');
INSERT INTO `dms_processing_unit`
VALUES ('1801416604025753602', '32132132132', 'abc', '测试2', 2, '345', '1', '2024-06-14 08:49:22', '1',
        '2024-06-14 08:49:22', b'0');
INSERT INTO `dms_processing_unit`
VALUES ('1806566250533322754', '321412412', 'Code', '测试3', NULL, '234', '1', '2024-06-28 13:52:13', '1',
        '2024-06-28 13:52:19', b'0');

-- ----------------------------
-- Table structure for mcs_batch_order
-- ----------------------------
DROP TABLE IF EXISTS `mcs_batch_order`;
CREATE TABLE `mcs_batch_order`
(
    `id`                 varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'id',
    `order_id`           varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '订单id',
    `order_number`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
    `process_id`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '工序Id',
    `processing_unit_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产单元Id',
    `count`              int(4) NOT NULL COMMENT '数量',
    `quality_numbers`    varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '质量编号',
    `plan_start_time`    datetime(0) NULL DEFAULT NULL COMMENT '预计开始时间',
    `deadline`           datetime(0) NULL DEFAULT NULL COMMENT '截止日期',
    `start_time`         datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
    `end_time`           datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
    `creator`            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`        datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`        datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`            bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    `workstation_code`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工位编号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '批次级订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mcs_batch_order
-- ----------------------------
INSERT INTO `mcs_batch_order`
VALUES ('1801159192165462017', '456', '123', '111', '1801416307278745601', 2, '1111', '2024-06-13 00:00:00',
        '2024-06-14 00:00:00', '2024-06-14 00:00:00', '2024-06-14 00:00:00', '1', '2024-06-13 15:46:30', '1',
        '2024-06-13 15:46:43', b'0', NULL);

-- ----------------------------
-- Table structure for mcs_batch_order_demand
-- ----------------------------
DROP TABLE IF EXISTS `mcs_batch_order_demand`;
CREATE TABLE `mcs_batch_order_demand`
(
    `id`             varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'id',
    `order_id`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '订单id',
    `order_number`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
    `batch_id`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '批次订单Id',
    `batch_code`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次订单编码',
    `resource_type`  int(2) NULL DEFAULT NULL COMMENT '资源类型',
    `resource_code`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源编码',
    `total`          int(4) NULL DEFAULT NULL COMMENT '需求数量',
    `minimum`        int(4) NULL DEFAULT NULL COMMENT '最小需求数量',
    `status`         int(2) NULL DEFAULT NULL COMMENT '齐备情况',
    `confirmed_by`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '确认人',
    `confirmed_time` datetime(0) NULL DEFAULT NULL COMMENT '确认时间',
    `creator`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`    datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`    datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`        bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '批次订单需求' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mcs_batch_order_demand
-- ----------------------------
INSERT INTO `mcs_batch_order_demand`
VALUES ('1801484117233299457', '123123', '12312', '23123', '1231', 1, '1231', 3, NULL, 0, NULL, NULL, '1',
        '2024-06-14 13:17:38', '1', '2024-06-14 13:17:38', b'0');
INSERT INTO `mcs_batch_order_demand`
VALUES ('1801484117233499457', '123123', '12312', '23123', '1231', 2, 'zxc', 3, 2, 1, NULL, NULL, '1',
        '2024-06-14 13:17:38', '1', '2024-06-14 13:17:38', b'0');

-- ----------------------------
-- Table structure for mcs_material_distribution
-- ----------------------------
DROP TABLE IF EXISTS `mcs_material_distribution`;
CREATE TABLE `mcs_material_distribution`
(
    `id`                varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'id',
    `batch_demand_id`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '批次需求Id',
    `batch_demand_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '批次需求编码',
    `resource_type`     int(2) NOT NULL COMMENT '资源类型',
    `resource_code`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源编码',
    `status`            int(2) NOT NULL COMMENT '配送状态',
    `start_time`        datetime(0) NULL DEFAULT NULL COMMENT '开始配送时间',
    `end_time`          datetime(0) NULL DEFAULT NULL COMMENT '配送完成时间',
    `creator`           varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`       datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`           varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`       datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`           bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '物料配送' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mcs_material_distribution
-- ----------------------------
INSERT INTO `mcs_material_distribution`
VALUES ('1801484117243499457', '1801484117233299457', '1231', 1, '1231', 1, '2024-06-14 14:18:25',
        '2024-06-14 14:18:27', NULL, '2024-06-14 14:18:29', NULL, '2024-06-14 14:18:29', b'0');

-- ----------------------------
-- Table structure for mcs_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `mcs_order_detail`;
CREATE TABLE `mcs_order_detail`
(
    `id`                   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `detail_number`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编号',
    `order_id`             varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单id',
    `order_number`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
    `quality_number`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '质量编号',
    `processing_unit_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前单元',
    `status`               int(2) NOT NULL COMMENT '状态',
    `start_time`           datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
    `end_time`             datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
    `creator`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`          datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`          datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`              bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '生产订单详情' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mcs_order_detail
-- ----------------------------
INSERT INTO `mcs_order_detail`
VALUES ('1801073451569766402', NULL, '1801073444846297089', '123', NULL, NULL, 0, NULL, NULL, '1',
        '2024-06-13 10:05:48', '1', '2024-06-13 10:05:48', b'1');
INSERT INTO `mcs_order_detail`
VALUES ('1801073451632680962', NULL, '1801073444846297089', '123', NULL, NULL, 0, NULL, NULL, '1',
        '2024-06-13 10:05:48', '1', '2024-06-13 10:05:48', b'1');
INSERT INTO `mcs_order_detail`
VALUES ('1801073451632680963', NULL, '1801073444846297089', '123', NULL, NULL, 0, NULL, NULL, '1',
        '2024-06-13 10:05:48', '1', '2024-06-13 10:05:48', b'1');
INSERT INTO `mcs_order_detail`
VALUES ('1801073451632680964', NULL, '1801073444846297089', '123', NULL, NULL, 0, NULL, NULL, '1',
        '2024-06-13 10:05:48', '1', '2024-06-13 10:05:48', b'1');
INSERT INTO `mcs_order_detail`
VALUES ('1801073451632680965', NULL, '1801073444846297089', '123', NULL, NULL, 0, NULL, NULL, '1',
        '2024-06-13 10:05:48', '1', '2024-06-13 10:05:48', b'1');
INSERT INTO `mcs_order_detail`
VALUES ('1801078646659756035', '223_1', '1801078646659756034', '223', NULL, '1', 1, NULL, NULL, '1',
        '2024-06-13 10:26:26', '1', '2024-06-13 10:26:26', b'0');
INSERT INTO `mcs_order_detail`
VALUES ('1801078646710087681', '223_2', '1801078646659756034', '223', NULL, '2', 2, NULL, NULL, '1',
        '2024-06-13 10:26:26', '1', '2024-06-13 10:26:26', b'0');
INSERT INTO `mcs_order_detail`
VALUES ('1801078646710087682', '223_3', '1801078646659756034', '223', NULL, '3', 3, '2024-06-13 13:42:39',
        '2024-06-13 13:42:41', '1', '2024-06-13 10:26:26', '1', '2024-06-13 10:26:26', b'0');

-- ----------------------------
-- Table structure for mcs_order_form
-- ----------------------------
DROP TABLE IF EXISTS `mcs_order_form`;
CREATE TABLE `mcs_order_form`
(
    `id`                 varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'id',
    `order_number`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
    `technology_id`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '工艺Id',
    `product_name`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
    `order_type`         int(2) NULL DEFAULT NULL COMMENT '订单类型',
    `priority`           int(255) NULL DEFAULT NULL COMMENT '优先级',
    `count`              int(4) NOT NULL COMMENT '数量',
    `reception_time`     datetime(0) NOT NULL COMMENT '接收时间',
    `delivery_time`      datetime(0) NOT NULL COMMENT '交付时间',
    `responsible_person` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
    `creator`            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`        datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`        datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`            bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '生产订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mcs_order_form
-- ----------------------------
INSERT INTO `mcs_order_form`
VALUES ('1798966547884167169', '1', '123', '2', 1, 3, 4, '2024-05-28 00:00:00', '2024-06-13 00:00:00', '1231', '1',
        '2024-06-07 14:33:43', '1', '2024-06-07 14:33:43', b'0');
INSERT INTO `mcs_order_form`
VALUES ('1801073444846297089', '123', '1798966547884167169', '2', 2, 1, 5, '2024-06-13 00:00:00', '2024-06-29 00:00:00',
        'aaa', '1', '2024-06-13 10:05:46', '1', '2024-06-13 10:05:46', b'1');
INSERT INTO `mcs_order_form`
VALUES ('1801078646659756034', '223', '1798966547884167169', '2', 1, 1, 3, '2024-06-13 00:00:00', '2024-06-30 00:00:00',
        'bbb', '1', '2024-06-13 10:26:26', '1', '2024-06-13 10:26:26', b'0');

-- ----------------------------
-- Table structure for mcs_production_records
-- ----------------------------
DROP TABLE IF EXISTS `mcs_production_records`;
CREATE TABLE `mcs_production_records`
(
    `id`                 varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
    `order_id`           varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单id',
    `batch_id`           varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次订单Id',
    `processing_unit_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产单元Id',
    `quality_number`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '质量编号',
    `equipment_id`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加工设备',
    `operation_type`     int(2) NULL DEFAULT NULL COMMENT '操作类型',
    `completed_quantity` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报工数量',
    `operation_time`     datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
    `operation_by`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
    `creator`            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time`        datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
    `update_time`        datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`            bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '现场作业记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mcs_production_records
-- ----------------------------
INSERT INTO `mcs_production_records`
VALUES ('1801452763003174914', '1801078646659756034', '1801159192165462017', '测试1', NULL, NULL, 1, NULL, NULL, NULL,
        '1', '2024-06-14 11:13:03', '1', '2024-06-14 11:13:03', b'0');
INSERT INTO `mcs_production_records`
VALUES ('1801452775871299586', '1801078646659756034', '1801159192165462017', '测试1', NULL, NULL, 2, NULL, NULL, NULL,
        '1', '2024-06-14 11:13:06', '1', '2024-06-14 11:13:06', b'0');
INSERT INTO `mcs_production_records`
VALUES ('1801452783177777154', '1801078646659756034', '1801159192165462017', '测试1', NULL, NULL, 2, NULL, NULL, NULL,
        '1', '2024-06-14 11:13:08', '1', '2024-06-14 11:13:08', b'0');
INSERT INTO `mcs_production_records`
VALUES ('1801452789364375554', '1801078646659756034', '1801159192165462017', '测试1', NULL, NULL, 3, NULL, NULL, NULL,
        '1', '2024-06-14 11:13:09', '1', '2024-06-14 11:13:09', b'0');

SET
FOREIGN_KEY_CHECKS = 1;
