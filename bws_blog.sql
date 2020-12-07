/*
 Navicat MySQL Data Transfer

 Source Server         : root1234
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : bws_blog

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 07/12/2020 11:05:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `category_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类ID',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容',
  `type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型（0表示富文本，1表示Markdown）',
  `cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章的封面',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0表示已发布，1表示草稿，2表示删除）',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '摘要',
  `labels` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `view_count` int(0) NOT NULL DEFAULT 0 COMMENT '阅读数量',
  `create_time` datetime(0) NOT NULL COMMENT '发布时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_article_on_user_id`(`user_id`) USING BTREE,
  INDEX `fk_category_article_on_category_id`(`category_id`) USING BTREE,
  CONSTRAINT `fk_category_article_on_category_id` FOREIGN KEY (`category_id`) REFERENCES `tb_categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_article_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES ('383744435411746816', '测试文章的标题', '381539180993314816', '383272801403928576', '测试发布的文章内容', '1', NULL, '3', '测试文章的摘要', '测试-测试2-测试3', 0, '2020-11-24 14:23:23', '2020-11-24 14:23:23');
INSERT INTO `tb_article` VALUES ('383754200816812032', '测试文章的标题22', '381539180993314816', '383272305930797056', '测试发布的文章内容22', '0', NULL, '1', '测试文章的摘要22', '测试', 0, '2020-11-24 15:02:11', '2020-11-24 15:02:11');
INSERT INTO `tb_article` VALUES ('383756754481053696', '测试文章的标题33', '381539180993314816', '381539180993314816', '测试发布的文章内容3', '0', '', '1', NULL, '查不到', 0, '2020-11-24 15:12:20', '2020-11-24 15:12:20');
INSERT INTO `tb_article` VALUES ('385105135698706432', '测试文章的标题22', '381539180993314816', '381539180993314816', '测试发布的文章内容22', '0', '', '1', '测试文章的摘要22', 'zenghao-徐航', 0, '2020-11-28 08:30:19', '2020-11-28 08:30:19');
INSERT INTO `tb_article` VALUES ('385105199087222784', '测试文章的标题22', '381539180993314816', '381539180993314816', '测试发布的文章内容22', '0', '', '1', '测试文章的摘要22', 'zenghao-徐航', 0, '2020-11-28 08:30:34', '2020-11-28 08:30:34');
INSERT INTO `tb_article` VALUES ('385106531596632064', '测试文章的标题22', '381539180993314816', '381539180993314816', '测试发布的文章内容22', '0', '', '1', '测试文章的摘要22', '测试-label', 0, '2020-11-28 08:35:52', '2020-11-28 08:35:52');

-- ----------------------------
-- Table structure for tb_categories
-- ----------------------------
DROP TABLE IF EXISTS `tb_categories`;
CREATE TABLE `tb_categories`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `pinyin` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '拼音',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
  `order` int(0) NOT NULL COMMENT '顺序',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态，0表示不适用，1表示正常',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_categories
-- ----------------------------
INSERT INTO `tb_categories` VALUES ('381539180993314816', '更新测试分类', 'ceshi', '测试更新222', 1, '0', '2020-11-11 08:59:36', '2020-11-18 08:59:40');
INSERT INTO `tb_categories` VALUES ('383272305930797056', '测试分类标题', 'ceshifenlei', '测试分类描述', 1, '1', '2020-11-23 07:07:19', '2020-11-23 07:07:19');
INSERT INTO `tb_categories` VALUES ('383272801403928576', '测试分类标题22', 'ceshifenlei', '测试分类描述', 1, '1', '2020-11-23 07:09:17', '2020-11-23 07:09:17');

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `parent_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '父内容',
  `article_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论用户的ID',
  `user_avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论用户的头像',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论用户的名称',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0表示删除，1表示正常）',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_comment_on_user_id`(`user_id`) USING BTREE,
  INDEX `fk_article_comment_on_article_id`(`article_id`) USING BTREE,
  CONSTRAINT `fk_user_comment_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------
INSERT INTO `tb_comment` VALUES ('388099309272301568', '我是你爸爸', '383744435411746816', '你的文章太棒了', '381539180993314816', 'https://profile.csdnimg.cn/8/1/1/2_simon_477', 'admin', '1', '2020-12-06 14:48:06', '2020-12-06 14:48:06');
INSERT INTO `tb_comment` VALUES ('388099421860003840', '22', '383744435411746816', '2', '381539180993314816', 'https://profile.csdnimg.cn/8/1/1/2_simon_477', 'admin', '1', '2020-12-06 14:48:33', '2020-12-06 14:48:33');
INSERT INTO `tb_comment` VALUES ('388099448506417152', '33', '383744435411746816', '233', '381539180993314816', 'https://profile.csdnimg.cn/8/1/1/2_simon_477', 'admin', '1', '2020-12-06 14:48:39', '2020-12-06 14:48:39');
INSERT INTO `tb_comment` VALUES ('388099538201608192', '2322', '383744435411746816', '23223', '381539180993314816', 'https://profile.csdnimg.cn/8/1/1/2_simon_477', 'admin', '1', '2020-12-06 14:49:00', '2020-12-06 14:49:00');
INSERT INTO `tb_comment` VALUES ('388099631134801920', '', '383744435411746816', '这是置顶得到第一条评论', '381539180993314816', 'https://profile.csdnimg.cn/8/1/1/2_simon_477', 'admin', '1', '2020-12-06 14:49:23', '2020-12-06 14:49:23');
INSERT INTO `tb_comment` VALUES ('388099653452693504', '', '383744435411746816', '这是置顶得到第2条评论', '381539180993314816', 'https://profile.csdnimg.cn/8/1/1/2_simon_477', 'admin', '3', '2020-12-06 14:49:28', '2020-12-06 14:49:28');

-- ----------------------------
-- Table structure for tb_daily_view_count
-- ----------------------------
DROP TABLE IF EXISTS `tb_daily_view_count`;
CREATE TABLE `tb_daily_view_count`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `view_count` int(0) NOT NULL DEFAULT 0 COMMENT '每天浏览量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_daily_view_count
-- ----------------------------

-- ----------------------------
-- Table structure for tb_friends
-- ----------------------------
DROP TABLE IF EXISTS `tb_friends`;
CREATE TABLE `tb_friends`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接名称',
  `logo` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接logo',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接',
  `order` int(0) NOT NULL COMMENT '顺序',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接状态，0表示不可用，1表示正常',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_friends
-- ----------------------------
INSERT INTO `tb_friends` VALUES ('383304234948362240', '科技毒瘤君', 'http://img.netbian.com/file/2018/0112/5052a9caac63ff6a723eed36d7daa415.jpg', 'http://beiwangshan.com/', 1, '1', '2020-11-23 09:14:11', '2020-11-23 09:14:11');
INSERT INTO `tb_friends` VALUES ('383319411148193792', '修改了一下', 'string', 'http://beiwangshan.com/', 0, '1', '2020-11-23 10:14:29', '2020-11-23 10:14:29');

-- ----------------------------
-- Table structure for tb_images
-- ----------------------------
DROP TABLE IF EXISTS `tb_images`;
CREATE TABLE `tb_images`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原名称',
  `path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存储路径',
  `content_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片类型',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0表示删除，1表示正常）',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_images_on_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_user_images_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_images
-- ----------------------------
INSERT INTO `tb_images` VALUES ('383543114524000256', '381539180993314816', '1606179804387_383543114524000256.png', 'ey66pk.png', 'E:\\javaProjects\\blog\\code\\images\\2020_11_24\\png\\383543114524000256.png', 'image/png', '1', '2020-11-24 01:03:24', '2020-11-24 01:03:24');
INSERT INTO `tb_images` VALUES ('383543744269385728', '382330713233424384', '1606179954531_383543744269385728.png', 'ey66pk.png', 'E:\\javaProjects\\blog\\code\\images\\2020_11_24\\png\\383543744269385728.png', 'image/png', '1', '2020-11-24 01:05:55', '2020-11-24 01:05:55');
INSERT INTO `tb_images` VALUES ('383543792872980480', '381539180993314816', '1606179966120_383543792872980480.png', '2020.png', 'E:\\javaProjects\\blog\\code\\images\\2020_11_24\\png\\383543792872980480.png', 'image/png', '0', '2020-11-24 01:06:06', '2020-11-24 01:06:06');
INSERT INTO `tb_images` VALUES ('386448574004592640', '381539180993314816', '1606872519909_386448574004592640.gif', '111.gif', 'E:\\javaProjects\\blog\\code\\images\\2020_12_02\\gif\\386448574004592640.gif', 'image/gif', '1', '2020-12-02 01:28:40', '2020-12-02 01:28:40');
INSERT INTO `tb_images` VALUES ('386452207819554816', '381539180993314816', '1606873386277_386452207819554816.jpg', 'Apple.jpg', 'E:\\javaProjects\\blog\\code\\images\\2020_12_02\\jpg\\386452207819554816.jpg', 'image/jpeg', '1', '2020-12-02 01:43:06', '2020-12-02 01:43:06');

-- ----------------------------
-- Table structure for tb_labels
-- ----------------------------
DROP TABLE IF EXISTS `tb_labels`;
CREATE TABLE `tb_labels`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名',
  `count` int(0) NOT NULL COMMENT '数量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_labels
-- ----------------------------
INSERT INTO `tb_labels` VALUES ('381450895688204288', '徐航', 1003, '2020-11-18 06:29:41', '2020-11-18 07:11:52');
INSERT INTO `tb_labels` VALUES ('381451666462867456', 'zenghao', 224, '2020-11-18 06:32:44', '2020-11-18 06:32:44');
INSERT INTO `tb_labels` VALUES ('385106531625992192', '测试', 1, '2020-11-28 08:35:52', '2020-11-28 08:35:52');
INSERT INTO `tb_labels` VALUES ('385106531676323840', 'label', 1, '2020-11-28 08:35:52', '2020-11-28 08:35:52');

-- ----------------------------
-- Table structure for tb_looper
-- ----------------------------
DROP TABLE IF EXISTS `tb_looper`;
CREATE TABLE `tb_looper`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '轮播图id',
  `order` int(0) NOT NULL COMMENT '顺序',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0表示不可以，1表示正常）',
  `target_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目标URL',
  `image_url` varchar(2014) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片地址',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_looper
-- ----------------------------

-- ----------------------------
-- Table structure for tb_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `tb_refresh_token`;
CREATE TABLE `tb_refresh_token`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `refresh_token` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'refresh_token',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `token_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'tokenKey，32位',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_refresh_token
-- ----------------------------
INSERT INTO `tb_refresh_token` VALUES ('383047885421281280', 'eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzODIzMzA3MTMyMzM0MjQzODQiLCJpYXQiOjE2MDYwNjE3MzIsImV4cCI6MTYwNjA2MTk0OH0.Qk8gM2IKpLPQiBdPLUfEgAcc1aBQyiZE1PArE9BE6zA', '382330713233424384', 'b10ae43c3d1bef13fe3e02e6bb7b9003', '2020-11-22 16:15:33', '2020-11-22 16:15:33');
INSERT INTO `tb_refresh_token` VALUES ('384488564823425024', 'eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzODQ0ODgzNDA2NTg4NDc3NDQiLCJpYXQiOjE2MDY0MDUyMTcsImV4cCI6MTYwNjQwNTQzM30.oWlxKg15uwVmt-ISm_JFy-5usQuXDHk5weIblQFoS0E', '384488340658847744', '2c1f8ba3995d750628c696ad6b2f8fa8', '2020-11-26 15:40:17', '2020-11-26 15:40:17');
INSERT INTO `tb_refresh_token` VALUES ('388099297985429504', 'eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzODE1MzkxODA5OTMzMTQ4MTYiLCJpYXQiOjE2MDcyNjYwODMsImV4cCI6MTYwNzI2NjI5OX0.jMzG8dfmwDTYUORMZabL-6WXocxv6PmmdG3T8QDDgnY', '381539180993314816', 'b84a9ffbb2cf7e4e99ec4a4274a19456', '2020-12-06 14:48:03', '2020-12-06 14:48:03');

-- ----------------------------
-- Table structure for tb_setting
-- ----------------------------
DROP TABLE IF EXISTS `tb_setting`;
CREATE TABLE `tb_setting`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '键',
  `value` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '值',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_setting
-- ----------------------------
INSERT INTO `tb_setting` VALUES ('381539181517602816', 'manager_account_init_state', '1', '2020-11-18 12:20:30', '2020-11-18 12:20:30');
INSERT INTO `tb_setting` VALUES ('383704518379962368', 'web_site_title', 'beiwangshan', '2020-11-24 11:44:46', '2020-11-24 11:44:46');
INSERT INTO `tb_setting` VALUES ('383705464963072000', 'web_site_description', '自豪的使用北忘山的博客', '2020-11-24 11:48:32', '2020-11-24 11:48:32');
INSERT INTO `tb_setting` VALUES ('383705464996626432', 'web_site_keyword', '北忘山', '2020-11-24 11:48:32', '2020-11-24 11:48:32');
INSERT INTO `tb_setting` VALUES ('388074578892554240', 'web_site_view_count', '1', '2020-12-06 13:09:50', '2020-12-06 13:09:50');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `roles` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色',
  `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像地址',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `sign` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态，0表示删除，1表示正常',
  `reg_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册ip',
  `login_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录ip',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('381539180993314816', 'admin', '$2a$10$yPAYs2iY.1qnx9c/NgrM1eIrpCAzZNOfYWjsZYhGkLhksLjakU85i', 'role_admin', 'https://profile.csdnimg.cn/8/1/1/2_simon_477', 'bws@163.com', '这是我修改的签名', '1', '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2020-11-18 12:20:29', '2020-11-18 12:20:29');
INSERT INTO `tb_user` VALUES ('382330713233424384', 'bws', '$2a$10$4sIseteqI840PCB4vV3HM.ZkCkzxBX6TTjOnaJrUwlSJknEiVeja.', 'role_normal', 'https://profile.csdnimg.cn/8/1/1/2_simon_477', 'beiwangshan22@yeah.net', NULL, '1', '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2020-11-20 16:45:45', '2020-11-20 16:45:45');
INSERT INTO `tb_user` VALUES ('382331511250092032', 'bws1', '$2a$10$RGYAguP6kMv7rRs8XiKcmuflGYMlDVcNB0TLr7knEirZs1z72vp0u', 'role_normal', 'https://profile.csdnimg.cn/8/1/1/2_simon_477', 'beiwangshan1@yeah.net', NULL, '0', '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2020-11-20 16:48:56', '2020-11-20 16:48:56');
INSERT INTO `tb_user` VALUES ('384488340658847744', 'beiwangshan', '$2a$10$t5eVzGaIwghOXiP4PASozOVr/fDeek4L9ABa3n4rEMdA04Qr3D4CW', 'role_normal', 'https://profile.csdnimg.cn/8/1/1/2_simon_477', 'beiwangshan@yeah.net', NULL, '1', '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2020-11-26 15:39:24', '2020-11-26 15:39:24');

SET FOREIGN_KEY_CHECKS = 1;
