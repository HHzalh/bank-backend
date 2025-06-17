-- 创建数据库
CREATE DATABASE IF NOT EXISTS bank;
USE bank;

-- 创建统一用户信息表（包含管理员和普通用户）
CREATE TABLE userInfo
(
    userID      SERIAL PRIMARY KEY COMMENT '用户编号（自增主键）',
    userName    VARCHAR(8)       NOT NULL COMMENT '用户姓名',
    PID         VARCHAR(18)      NOT NULL COMMENT '身份证号（18位，唯一）',
    gender      ENUM ('男','女') NOT NULL COMMENT '性别',
    telephone   VARCHAR(20)      NOT NULL COMMENT '联系电话（11位数字）',
    address     VARCHAR(50) COMMENT '联系地址（可选）',
    imageUrl    VARCHAR(255) COMMENT '头像地址（可选）',
    role        TINYINT   DEFAULT 0 COMMENT '权限：0-普通用户,1-管理员',
    account     VARCHAR(20)      NOT NULL COMMENT '登录账号',
    password    VARCHAR(100)     NOT NULL COMMENT '密码',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '统一用户信息表';

ALTER TABLE userInfo
    ADD CONSTRAINT uk_user_pid UNIQUE (PID),
    ADD CONSTRAINT uk_user_account UNIQUE (account),
    ADD CONSTRAINT ck_pid_format CHECK (PID REGEXP '^[0-9]{17}[0-9X]$'),
    ADD CONSTRAINT ck_telephone_format CHECK (telephone REGEXP '^[0-9]{11}$'),
    ADD CONSTRAINT ck_role CHECK (role IN (0, 1));

-- 创建存款类型表
CREATE TABLE deposit
(
    savingID    SERIAL PRIMARY KEY COMMENT '存款类型ID（自增主键）',
    savingName  VARCHAR(20) NOT NULL COMMENT '存款类型名称',
    descrip     VARCHAR(50) COMMENT '存款类型描述',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '存款类型表';

ALTER TABLE deposit
    ADD CONSTRAINT ck_deposit_types CHECK (
                savingName IN ('活期', '定活两便') OR
                savingName REGEXP '^(活期|定期|零存整取)[一二三四五六七八九十0-9]+年?$'
    );

-- 创建银行卡信息表
CREATE TABLE cardInfo
(
    cardID       CHAR(16) PRIMARY KEY COMMENT '银行卡号（16位，格式：10103576xxxxxxxx）',
    curID        VARCHAR(10)      DEFAULT 'RMB' COMMENT '币种',
    savingID     BIGINT UNSIGNED NOT NULL COMMENT '存款类型ID',
    openDate     TIMESTAMP        DEFAULT CURRENT_TIMESTAMP COMMENT '开户日期',
    openMoney    DECIMAL(18, 2)  NOT NULL CHECK (openMoney >= 1) COMMENT '开户金额（≥1元）',
    balance      DECIMAL(18, 2)  NOT NULL CHECK (balance >= 1) COMMENT '账户余额（≥1元）',
    pass         CHAR(6)          DEFAULT '888888' COMMENT '密码（6位数字）',
    IsReportLoss ENUM ('是','否') DEFAULT '否' COMMENT '是否挂失',
    customerID   BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    create_time  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '银行卡信息表';

ALTER TABLE cardInfo
    ADD CONSTRAINT ck_card_pass_format CHECK (pass REGEXP '^[0-9]{6}$'),
    ADD CONSTRAINT ck_card_format CHECK (cardID REGEXP '^10103576[0-9]{8}$');

-- 创建交易信息表
CREATE TABLE tradeInfo
(
    tradeID     SERIAL PRIMARY KEY COMMENT '交易ID（自增主键）',
    tradeDate   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '交易日期',
    tradeType   ENUM ('存款','取款','转账') COMMENT '交易类型',
    cardID      CHAR(16) COMMENT '银行卡号',
    tradeMoney  DECIMAL(18, 2) COMMENT '交易金额（＞0元）',
    remark      TEXT COMMENT '交易备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '银行卡交易记录表';

ALTER TABLE tradeInfo
    ADD CONSTRAINT ck_trade_money CHECK (tradeMoney > 0),
    ADD CONSTRAINT ck_trade_card_format CHECK (cardID REGEXP '^10103576[0-9]{8}$');

-- 插入存款类型数据
INSERT INTO deposit (savingName, descrip)
VALUES ('活期', '按存款日结算利息'),
       ('定期一年', '存款期是1年'),
       ('定期二年', '存款期是2年'),
       ('定期三年', '存款期是3年'),
       ('定活两便', NULL),
       ('零存整取一年', '存款期是1年'),
       ('零存整取二年', '存款期是2年'),
       ('零存整取三年', '存款期是3年');

-- 插入用户信息（普通用户+管理员）
INSERT INTO userInfo (userName, PID, gender, telephone, address, role, account, password)
VALUES
-- 普通用户（role=0）
('张三', '110000000000000001', '男', '13900000001', '江苏省南京市', 0, 'user001', '12345678'),
('李四', '110000000000000002', '女', '13900000002', '浙江省杭州市', 0, 'user002', '12345678'),
('王五', '110000000000000003', '男', '13900000003', '广东省深圳市', 0, 'user003', '12345678'),
('赵六', '110000000000000004', '女', '13900000004', '四川省成都市', 0, 'user004', '12345678'),
('钱七', '110000000000000005', '男', '13900000005', '湖北省武汉市', 0, 'user005', '12345678'),
('孙八', '110000000000000006', '女', '13900000006', '陕西省西安市', 0, 'user006', '12345678'),
('周九', '110000000000000007', '男', '13900000007', '山东省青岛市', 0, 'user007', '12345678'),
('吴十', '110000000000000008', '女', '13900000008', '辽宁省大连市', 0, 'user008', '12345678'),
('郑十一', '110000000000000009', '男', '13900000009', '福建省厦门市', 0, 'user009', '12345678'),
('王十二', '110000000000000010', '女', '13900000010', '湖南省长沙市', 0, 'user010', '12345678'),
-- 管理员（role=1）
('严经理', '110000000000000011', '男', '13900000011', '北京市朝阳区', 1, 'admin001', 'admin'),
('覃行长', '110000000000000012', '男', '13900000012', '上海市浦东新区', 1, 'admin002', 'admin'),
('王总监', '110000000000000013', '女', '13900000013', '广州市天河区', 1, 'admin003', 'admin');

-- 插入银行卡信息
INSERT INTO cardInfo(cardid, curid, savingid, openmoney, balance, pass, isreportloss, customerid)
VALUES ('1010357600000001', 'RMB', 1, 10000.00, 10000.00, '888888', '否', 1),
       ('1010357600000002', 'RMB', 2, 5000.00, 5000.00, '888888', '否', 2),
       ('1010357600000003', 'RMB', 2, 3000.00, 3000.00, '888888', '否', 3),
       ('1010357600000004', 'RMB', 2, 2000.00, 2000.00, '888888', '否', 4),
       ('1010357600000005', 'RMB', 3, 1500.00, 1500.00, '888888', '否', 5),
       ('1010357600000006', 'RMB', 3, 1000.00, 1000.00, '888888', '否', 6),
       ('1010357600000007', 'RMB', 3, 800.00, 800.00, '888888', '否', 7),
       ('1010357600000008', 'RMB', 3, 500.00, 500.00, '888888', '否', 8),
       ('1010357600000009', 'RMB', 3, 300.00, 300.00, '888888', '否', 9),
       ('1010357600000010', 'RMB', 3, 200.00, 200.00, '888888', '否', 10);