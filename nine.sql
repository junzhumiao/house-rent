/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 80032
Source Host           : localhost:3306
Source Database       : nine

Target Server Type    : MYSQL
Target Server Version : 80032
File Encoding         : 65001

Date: 2024-04-20 15:56:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for contract
-- ----------------------------
DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract` (
  `contract_id` bigint NOT NULL AUTO_INCREMENT COMMENT '合同id',
  `landlord_id` bigint DEFAULT NULL COMMENT '房东id',
  `house_id` bigint DEFAULT NULL,
  `sign` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '0 未签署 1签署',
  `del_flag` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0',
  `contract_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '合同编号',
  `stop_time` datetime DEFAULT NULL,
  `sign_time` datetime DEFAULT NULL COMMENT '签署日期',
  `create_time` datetime DEFAULT NULL COMMENT '合同发布时间',
  `update_time` datetime DEFAULT NULL,
  `create_by` bigint DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  PRIMARY KEY (`contract_id`),
  UNIQUE KEY `contract_num` (`contract_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='合同表 -- 存储合同信息';

-- ----------------------------
-- Records of contract
-- ----------------------------

-- ----------------------------
-- Table structure for house
-- ----------------------------
DROP TABLE IF EXISTS `house`;
CREATE TABLE `house` (
  `house_id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '房屋地址',
  `del_flag` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0',
  `status` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '0 未出售,1已出售',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `landlord_id` bigint DEFAULT NULL COMMENT '房东id',
  `link_man` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人',
  `link_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
  `create_by` bigint DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  PRIMARY KEY (`house_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='房屋表 -- 存储房屋信息';

-- ----------------------------
-- Records of house
-- ----------------------------

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `payment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '缴纳记录id',
  `payer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '缴纳人账户地址',
  `payer_id` bigint DEFAULT NULL COMMENT '缴纳人用户id',
  `contract_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '合同序列号',
  `create_time` datetime DEFAULT NULL,
  `del_flag` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '0 未删除 1 已删除',
  `update_by` bigint DEFAULT NULL,
  `create_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='缴纳记录表 -- 记录租金缴纳信息';

-- ----------------------------
-- Records of payment
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id` bigint DEFAULT NULL,
  `role_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT=' 角色表';

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '管理员');
INSERT INTO `role` VALUES ('2', '房东');
INSERT INTO `role` VALUES ('3', '租客');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户区块链账户地址',
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户密码',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `del_flag` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0',
  `create_by` bigint DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `status` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '0 启用 1 禁用',
  `sex` enum('0','1','2') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '2',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表 -- 存储用户信息';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '0xa5c4f1b07f513a586b19be93fd44966bcbd5d2c5', '15069680202', 'e10adc3949ba59abbe56e057f20f883e', '2024-04-18 15:16:58', '2024-04-17 19:35:35', '0', '1', '1', '0', '2');
INSERT INTO `user` VALUES ('3', '测试房东', '0xdde5b34d81a606d74341b0d788e5e0e5db5f6918', '15069680203', 'f1887d3f9e6ee7a32fe5e76f4ab80d63', null, '2024-04-20 15:52:33', '0', '1', null, '0', '2');
INSERT INTO `user` VALUES ('4', '测试租客', '0x29eec69129e7a4ee98fa43d2fb4799b029589a5d', '15069680204', '93897cc117a734be93733779051c9926', null, '2024-04-20 15:53:33', '0', '1', null, '0', '2');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint DEFAULT NULL,
  `role_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户 - 角色表';

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1');
INSERT INTO `user_role` VALUES ('3', '2');
INSERT INTO `user_role` VALUES ('4', '3');
