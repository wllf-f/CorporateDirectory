-- ============================================================
-- 企业通讯录管理系统 - 优化版数据库构建脚本
-- 改进点：1.每个部门至少4名员工 2.所有员工均有25名客户 3.部门管理员均为在职员工
-- ============================================================

-- 强制当前会话字符集为utf8mb4，解决中文解析问题
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 创建数据库
DROP DATABASE IF EXISTS corporate_directory_wei;
CREATE DATABASE corporate_directory_wei DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE corporate_directory_wei;

-- 1. 创建表结构
CREATE TABLE department (
    dept_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    dept_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    manager_id BIGINT COMMENT '部门管理员ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门信息表';

CREATE TABLE employee (
    emp_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '员工ID',
    emp_number VARCHAR(50) NOT NULL UNIQUE COMMENT '员工工号',
    emp_name VARCHAR(100) NOT NULL COMMENT '员工姓名',
    emp_gender ENUM('男', '女') COMMENT '员工性别',
    emp_phone VARCHAR(20) COMMENT '员工电话',
    emp_email VARCHAR(100) COMMENT '员工邮箱',
    dept_id BIGINT COMMENT '所属部门ID',
    password VARCHAR(255) NOT NULL COMMENT '登录密码',
    is_active ENUM('在职', '离职') DEFAULT '在职' COMMENT '是否在职',
    emp_role ENUM('员工', '管理员', '超级管理员') DEFAULT '员工' COMMENT '员工角色',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_dept_id (dept_id),
    INDEX idx_emp_number (emp_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工信息表';

CREATE TABLE customer (
    cust_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '客户ID',
    cust_name VARCHAR(100) NOT NULL COMMENT '客户姓名',
    cust_gender ENUM('男', '女') COMMENT '客户性别',
    cust_phone VARCHAR(20) COMMENT '客户电话',
    cust_email VARCHAR(100) COMMENT '客户邮箱',
    personal_group VARCHAR(50) COMMENT '客户个人分组',
    added_by BIGINT COMMENT '添加员工ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    privacy_level ENUM('personal', 'group') DEFAULT 'personal' COMMENT '隐私等级',
    is_deleted ENUM('是', '否') DEFAULT '否' COMMENT '是否删除',
    INDEX idx_added_by (added_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户信息表';

CREATE TABLE oper_log (
    login_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志主键ID',
    operator_id VARCHAR(50) NOT NULL COMMENT '操作工号',
    operation VARCHAR(20) NOT NULL COMMENT '执行操作类型',
    operated_table VARCHAR(100) COMMENT '操作的表名',
    table_id BIGINT COMMENT '操作表的id',
    old_data TEXT COMMENT '旧数据',
    new_data TEXT COMMENT '新数据',
    oper_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作日期',
    INDEX idx_operator_id (operator_id),
    INDEX idx_oper_date (oper_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 2. 插入部门数据
INSERT INTO department (dept_id, dept_name, create_time) VALUES
(1, '总裁办公室', '2024-01-01 00:00:00'),
(2, '技术研发部', '2024-01-01 00:00:00'),
(3, '市场销售部', '2024-01-01 00:00:00'),
(4, '人力资源部', '2024-01-01 00:00:00'),
(5, '财务部', '2024-01-01 00:00:00'),
(6, '产品部', '2024-01-01 00:00:00'),
(7, '客户服务部', '2024-01-01 00:00:00'),
(8, '行政部', '2024-01-01 00:00:00');

-- 3. 插入员工数据（32名员工，每个部门4人）
INSERT INTO employee (emp_id, emp_number, emp_name, emp_gender, emp_phone, emp_email, dept_id, password, is_active, emp_role, create_time) VALUES
-- 部门1: 总裁办公室 (4人)
(1, 'SUPER001', '张伟', '男', '13800000001', 'zhangwei@company.com', 1, '123456', '在职', '超级管理员', '2024-01-01 08:00:00'),
(2, 'ADMIN001', '李娜', '女', '13800000002', 'lina@company.com', 1, '123456', '在职', '管理员', '2024-01-05 09:00:00'),
(3, 'EMP001', '王静', '女', '13800000003', 'wangjing@company.com', 1, '123456', '在职', '员工', '2024-01-10 09:00:00'),
(4, 'EMP002', '刘洋', '男', '13800000004', 'liuyang@company.com', 1, '123456', '在职', '员工', '2024-01-10 10:00:00'),

-- 部门2: 技术研发部 (4人)
(5, 'TECH001', '陈明', '男', '13800000005', 'chenming@company.com', 2, '123456', '在职', '管理员', '2024-01-05 09:00:00'),
(6, 'TECH002', '杨雪', '女', '13800000006', 'yangxue@company.com', 2, '123456', '在职', '员工', '2024-01-10 09:00:00'),
(7, 'TECH003', '赵雷', '男', '13800000007', 'zhaolei@company.com', 2, '123456', '在职', '员工', '2024-01-10 10:00:00'),
(8, 'TECH004', '周婷', '女', '13800000008', 'zhouting@company.com', 2, '123456', '在职', '员工', '2024-01-12 09:00:00'),

-- 部门3: 市场销售部 (4人)
(9, 'SALE001', '王强', '男', '13800000009', 'wangqiang@company.com', 3, '123456', '在职', '管理员', '2024-01-05 09:00:00'),
(10, 'SALE002', '孙丽', '女', '13800000010', 'sunli@company.com', 3, '123456', '在职', '员工', '2024-01-10 09:00:00'),
(11, 'SALE003', '高伟', '男', '13800000011', 'gaowei@company.com', 3, '123456', '在职', '员工', '2024-01-10 10:00:00'),
(12, 'SALE004', '吴芳', '女', '13800000012', 'wufang@company.com', 3, '123456', '在职', '员工', '2024-01-12 09:00:00'),

-- 部门4: 人力资源部 (4人)
(13, 'HR001', '刘芳', '女', '13800000013', 'liufang@company.com', 4, '123456', '在职', '管理员', '2024-01-05 09:00:00'),
(14, 'HR002', '郑浩', '男', '13800000014', 'zhenghao@company.com', 4, '123456', '在职', '员工', '2024-01-10 09:00:00'),
(15, 'HR003', '董洁', '女', '13800000015', 'dongjie@company.com', 4, '123456', '在职', '员工', '2024-01-10 10:00:00'),
(16, 'HR004', '袁明', '男', '13800000016', 'yuanming@company.com', 4, '123456', '在职', '员工', '2024-01-12 09:00:00'),

-- 部门5: 财务部 (4人)
(17, 'FINA001', '赵静', '女', '13800000017', 'zhaojing@company.com', 5, '123456', '在职', '管理员', '2024-01-10 10:00:00'),
(18, 'FINA002', '钱勇', '男', '13800000018', 'qianyong@company.com', 5, '123456', '在职', '员工', '2024-01-10 09:00:00'),
(19, 'FINA003', '田雪', '女', '13800000019', 'tianxue@company.com', 5, '123456', '在职', '员工', '2024-01-12 09:00:00'),
(20, 'FINA004', '冯刚', '男', '13800000020', 'fenggang@company.com', 5, '123456', '在职', '员工', '2024-01-12 10:00:00'),

-- 部门6: 产品部 (4人)
(21, 'PROD001', '陈亮', '男', '13800000021', 'chenliang@company.com', 6, '123456', '在职', '管理员', '2024-01-10 10:00:00'),
(22, 'PROD002', '林娜', '女', '13800000022', 'linna@company.com', 6, '123456', '在职', '员工', '2024-01-12 09:00:00'),
(23, 'PROD003', '何文', '男', '13800000023', 'hewen@company.com', 6, '123456', '在职', '员工', '2024-01-12 10:00:00'),
(24, 'PROD004', '徐敏', '女', '13800000024', 'xumin@company.com', 6, '123456', '在职', '员工', '2024-01-15 09:00:00'),

-- 部门7: 客户服务部 (4人)
(25, 'SERV001', '丁静', '女', '13800000025', 'dingjing@company.com', 7, '123456', '在职', '管理员', '2024-01-12 09:00:00'),
(26, 'SERV002', '薛刚', '男', '13800000026', 'xuegang@company.com', 7, '123456', '在职', '员工', '2024-01-10 09:00:00'),
(27, 'SERV003', '姜薇', '女', '13800000027', 'jiangwei@company.com', 7, '123456', '在职', '员工', '2024-01-12 09:00:00'),
(28, 'SERV004', '范伟', '男', '13800000028', 'fanwei@company.com', 7, '123456', '在职', '员工', '2024-01-12 10:00:00'),

-- 部门8: 行政部 (4人)
(29, 'ADMIN002', '欧阳静', '女', '13800000029', 'ouyangjing@company.com', 8, '123456', '在职', '管理员', '2024-01-15 10:00:00'),
(30, 'ADMIN003', '马超', '男', '13800000030', 'machao@company.com', 8, '123456', '在职', '员工', '2024-01-10 09:00:00'),
(31, 'ADMIN004', '朱琳', '女', '13800000031', 'zhulin@company.com', 8, '123456', '在职', '员工', '2024-01-12 09:00:00'),
(32, 'ADMIN005', '胡军', '男', '13800000032', 'hujun@company.com', 8, '123456', '在职', '员工', '2024-01-12 10:00:00');

-- 4. 更新部门管理员（确保均为在职员工）
UPDATE department SET manager_id = 1 WHERE dept_id = 1;  -- 总裁办公室:张伟(超级管理员)
UPDATE department SET manager_id = 5 WHERE dept_id = 2;  -- 技术研发部:陈明(管理员)
UPDATE department SET manager_id = 9 WHERE dept_id = 3;  -- 市场销售部:王强(管理员)
UPDATE department SET manager_id = 13 WHERE dept_id = 4; -- 人力资源部:刘芳(管理员)
UPDATE department SET manager_id = 17 WHERE dept_id = 5; -- 财务部:赵静(管理员)
UPDATE department SET manager_id = 21 WHERE dept_id = 6; -- 产品部:陈亮(管理员)
UPDATE department SET manager_id = 25 WHERE dept_id = 7; -- 客户服务部:丁静(管理员)
UPDATE department SET manager_id = 29 WHERE dept_id = 8; -- 行政部:欧阳静(管理员)

-- 5. 使用存储过程循环插入客户数据（800条，每位员工25名客户）
DELIMITER $$
DROP PROCEDURE IF EXISTS InsertCustomers;
CREATE PROCEDURE InsertCustomers()
BEGIN
    DECLARE i, j, total_employees INT DEFAULT 0;
    DECLARE current_emp_id, base_cust_id BIGINT;
    DECLARE cust_name_val, cust_phone_val, cust_email_val VARCHAR(255);
    DECLARE cust_gender_val ENUM('男', '女');
    DECLARE group_val VARCHAR(50);
    DECLARE privacy_val ENUM('personal', 'group');
    
    SET total_employees = 32;  -- 总员工数
    SET base_cust_id = 1;      -- 客户ID起始值
    
    -- 为每位员工循环插入客户
    SET current_emp_id = 1;
    WHILE current_emp_id <= total_employees DO
        SET i = 1;
        -- 每位员工分配25个客户
        WHILE i <= 25 DO
            -- 生成客户信息
            SET cust_name_val = CONCAT('客户', LPAD(base_cust_id, 3, '0'));
            SET cust_gender_val = IF(MOD(base_cust_id, 2) = 0, '男', '女');
            SET cust_phone_val = CONCAT('139', LPAD(1000000 + base_cust_id, 8, '0'));
            SET cust_email_val = CONCAT('customer', base_cust_id, '@example.com');
            
            -- 随机分配客户分组
            SET group_val = ELT(1 + MOD(base_cust_id, 4), '重要客户', '普通客户', 'VIP客户', '潜在客户');
            
            -- 随机分配隐私级别
            SET privacy_val = IF(MOD(base_cust_id, 3) = 0, 'group', 'personal');
            
            -- 插入客户记录
            INSERT INTO customer (cust_id, cust_name, cust_gender, cust_phone, cust_email, personal_group, added_by, privacy_level, is_deleted, create_time)
            VALUES (
                base_cust_id,
                cust_name_val,
                cust_gender_val,
                cust_phone_val,
                cust_email_val,
                group_val,
                current_emp_id,
                privacy_val,
                '否',
                DATE_ADD('2024-01-01 00:00:00', INTERVAL base_cust_id * 10 MINUTE)
            );
            
            SET base_cust_id = base_cust_id + 1;
            SET i = i + 1;
        END WHILE;
        SET current_emp_id = current_emp_id + 1;
    END WHILE;
END
$$
DELIMITER ;

-- 执行存储过程插入800条客户数据
CALL InsertCustomers();
DROP PROCEDURE InsertCustomers;

-- 6. 数据验证查询
-- 查看各表数据量
SELECT '部门' AS 表名, COUNT(*) AS 记录数 FROM department
UNION ALL
SELECT '员工', COUNT(*) FROM employee
UNION ALL
SELECT '客户', COUNT(*) FROM customer
UNION ALL
SELECT '操作日志', COUNT(*) FROM oper_log;

-- 查看每位员工名下的客户数量分布
SELECT 
    e.emp_id AS 员工ID,
    e.emp_name AS 员工姓名,
    d.dept_name AS 所属部门,
    e.emp_role AS 角色,
    COUNT(c.cust_id) AS 名下客户数
FROM employee e
LEFT JOIN customer c ON e.emp_id = c.added_by
LEFT JOIN department d ON e.dept_id = d.dept_id
GROUP BY e.emp_id, e.emp_name, d.dept_name, e.emp_role
ORDER BY e.dept_id, e.emp_id;

-- 查看部门员工统计
SELECT 
    d.dept_name AS 部门名称,
    COUNT(e.emp_id) AS 员工数量,
    SUM(CASE WHEN e.emp_role IN ('管理员', '超级管理员') THEN 1 ELSE 0 END) AS 管理员数量
FROM department d
LEFT JOIN employee e ON d.dept_id = e.dept_id
GROUP BY d.dept_id, d.dept_name
ORDER BY d.dept_id;