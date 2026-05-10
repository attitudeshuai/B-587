-- 创建数据库
CREATE DATABASE IF NOT EXISTS fruit_warehouse CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fruit_warehouse;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色：ADMIN/USER',
    phone VARCHAR(20) COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 水果分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    description VARCHAR(200) COMMENT '分类描述',
    icon VARCHAR(50) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='水果分类表';

-- 水果表
CREATE TABLE IF NOT EXISTS fruits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '水果名称',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    unit VARCHAR(20) NOT NULL DEFAULT '斤' COMMENT '单位',
    purchase_price DECIMAL(10,2) NOT NULL COMMENT '进货价格',
    sale_price DECIMAL(10,2) NOT NULL COMMENT '批发价格',
    stock_quantity DECIMAL(10,2) DEFAULT 0 COMMENT '库存数量',
    min_stock DECIMAL(10,2) DEFAULT 0 COMMENT '最低库存预警',
    origin VARCHAR(100) COMMENT '产地',
    description TEXT COMMENT '描述',
    image_url VARCHAR(255) COMMENT '图片URL',
    status TINYINT DEFAULT 1 COMMENT '状态：0下架 1上架',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='水果表';

-- 供应商表
CREATE TABLE IF NOT EXISTS suppliers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '供应商名称',
    contact_name VARCHAR(50) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(255) COMMENT '地址',
    remark TEXT COMMENT '备注',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商表';

-- 客户表
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '客户名称',
    contact_name VARCHAR(50) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(255) COMMENT '地址',
    level VARCHAR(20) DEFAULT 'NORMAL' COMMENT '客户等级：VIP/NORMAL',
    remark TEXT COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户表';

-- 入库记录表
CREATE TABLE IF NOT EXISTS stock_in_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    record_no VARCHAR(50) NOT NULL UNIQUE COMMENT '入库单号',
    fruit_id BIGINT NOT NULL COMMENT '水果ID',
    supplier_id BIGINT COMMENT '供应商ID',
    quantity DECIMAL(10,2) NOT NULL COMMENT '入库数量',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '进货单价',
    total_amount DECIMAL(12,2) NOT NULL COMMENT '总金额',
    operator_id BIGINT COMMENT '操作人ID',
    remark TEXT COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fruit_id) REFERENCES fruits(id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    FOREIGN KEY (operator_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='入库记录表';

-- 出库记录表
CREATE TABLE IF NOT EXISTS stock_out_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    record_no VARCHAR(50) NOT NULL UNIQUE COMMENT '出库单号',
    fruit_id BIGINT NOT NULL COMMENT '水果ID',
    customer_id BIGINT COMMENT '客户ID',
    quantity DECIMAL(10,2) NOT NULL COMMENT '出库数量',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '批发单价',
    total_amount DECIMAL(12,2) NOT NULL COMMENT '总金额',
    operator_id BIGINT COMMENT '操作人ID',
    remark TEXT COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fruit_id) REFERENCES fruits(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (operator_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='出库记录表';

-- 插入初始数据

-- 管理员账户
INSERT INTO users (username, password, real_name, role, phone, status) VALUES
('admin', '123456', '系统管理员', 'ADMIN', '13800138000', 1),
('operator1', '123456', '张三', 'USER', '13800138001', 1),
('operator2', '123456', '李四', 'USER', '13800138002', 1);

-- 水果分类
INSERT INTO categories (name, description, icon, sort_order) VALUES
('热带水果', '产自热带地区的水果', '🥭', 1),
('温带水果', '产自温带地区的水果', '🍎', 2),
('浆果类', '各类浆果', '🍓', 3),
('柑橘类', '柑橘类水果', '🍊', 4),
('瓜果类', '各类瓜果', '🍉', 5);

-- 供应商
INSERT INTO suppliers (name, contact_name, phone, address, status) VALUES
('海南热带水果基地', '王经理', '13900001111', '海南省海口市美兰区', 1),
('山东苹果合作社', '李经理', '13900002222', '山东省烟台市栖霞市', 1),
('云南水果批发中心', '张经理', '13900003333', '云南省昆明市官渡区', 1),
('广西柑橘种植园', '陈经理', '13900004444', '广西壮族自治区桂林市', 1),
('新疆哈密瓜基地', '刘经理', '13900005555', '新疆维吾尔自治区哈密市', 1);

-- 客户
INSERT INTO customers (name, contact_name, phone, address, level) VALUES
('鲜果超市连锁', '赵总', '13800001111', '北京市朝阳区建国路88号', 'VIP'),
('美好生活便利店', '钱经理', '13800002222', '上海市浦东新区陆家嘴', 'VIP'),
('绿色果蔬批发市场', '孙老板', '13800003333', '广州市白云区江高镇', 'NORMAL'),
('社区团购平台', '周经理', '13800004444', '深圳市南山区科技园', 'VIP'),
('学校食堂采购部', '吴主任', '13800005555', '武汉市洪山区珞喻路', 'NORMAL');

-- 水果数据
INSERT INTO fruits (name, category_id, unit, purchase_price, sale_price, stock_quantity, min_stock, origin, description, status) VALUES
('芒果', 1, '斤', 5.50, 8.00, 500, 100, '海南三亚', '海南三亚特产大芒果，香甜多汁', 1),
('榴莲', 1, '斤', 25.00, 35.00, 200, 50, '泰国', '泰国金枕榴莲，肉厚核小', 1),
('菠萝', 1, '个', 8.00, 12.00, 300, 80, '广东湛江', '新鲜菠萝，酸甜可口', 1),
('香蕉', 1, '斤', 2.50, 4.00, 800, 200, '云南', '云南高山香蕉，自然成熟', 1),
('苹果', 2, '斤', 4.00, 6.50, 1000, 200, '山东烟台', '烟台红富士苹果，脆甜多汁', 1),
('梨', 2, '斤', 3.50, 5.50, 600, 150, '河北石家庄', '雪花梨，皮薄水多', 1),
('桃子', 2, '斤', 5.00, 8.00, 400, 100, '山东临沂', '水蜜桃，软糯香甜', 1),
('葡萄', 2, '斤', 8.00, 12.00, 350, 80, '新疆吐鲁番', '新疆无籽葡萄，晶莹剔透', 1),
('草莓', 3, '斤', 15.00, 25.00, 150, 50, '辽宁丹东', '丹东草莓，个大味甜', 1),
('蓝莓', 3, '盒', 18.00, 28.00, 200, 60, '云南昆明', '云南高山蓝莓，新鲜采摘', 1),
('橙子', 4, '斤', 4.00, 6.00, 700, 150, '江西赣州', '赣南脐橙，甜而不酸', 1),
('柚子', 4, '个', 6.00, 10.00, 300, 80, '福建漳州', '漳州蜜柚，饱满多汁', 1),
('西瓜', 5, '斤', 1.50, 2.50, 500, 100, '宁夏中卫', '宁夏硒砂瓜，清甜爽口', 1),
('哈密瓜', 5, '个', 15.00, 22.00, 200, 50, '新疆哈密', '新疆哈密瓜，香甜如蜜', 1);

-- 入库记录
INSERT INTO stock_in_records (record_no, fruit_id, supplier_id, quantity, unit_price, total_amount, operator_id, remark) VALUES
('IN202601220001', 1, 1, 200, 5.50, 1100.00, 1, '芒果首批到货'),
('IN202601220002', 5, 2, 500, 4.00, 2000.00, 1, '烟台苹果到货'),
('IN202601220003', 11, 4, 300, 4.00, 1200.00, 2, '赣南脐橙采购'),
('IN202601220004', 2, 1, 100, 25.00, 2500.00, 1, '泰国榴莲进口'),
('IN202601220005', 9, 3, 80, 15.00, 1200.00, 2, '丹东草莓新鲜到货');

-- 出库记录
INSERT INTO stock_out_records (record_no, fruit_id, customer_id, quantity, unit_price, total_amount, operator_id, remark) VALUES
('OUT202601220001', 1, 1, 50, 8.00, 400.00, 1, '鲜果超市采购'),
('OUT202601220002', 5, 2, 100, 6.50, 650.00, 2, '便利店补货'),
('OUT202601220003', 11, 3, 80, 6.00, 480.00, 1, '批发市场订单'),
('OUT202601220004', 13, 4, 200, 2.50, 500.00, 2, '社区团购订单');
