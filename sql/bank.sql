-- 创建用户信息表
CREATE TABLE userInfo
(
    customerID  SERIAL PRIMARY KEY COMMENT '客户编号（自增主键）',
    customerName CHAR(8) NOT NULL COMMENT '客户姓名（必须提供）',
    PID CHAR(18) NOT NULL COMMENT '身份证号（18位，唯一）',
    telephone CHAR(20) NOT NULL COMMENT '联系电话（11位数字）',
    address VARCHAR(50) COMMENT '联系地址（可选）'
) COMMENT '用户基本信息表';

-- 用户信息表约束
ALTER TABLE userInfo
    ADD CONSTRAINT uk_user_pid UNIQUE (PID),
    ADD CONSTRAINT ck_user_pid_format CHECK (PID REGEXP '^[0-9]{17}([0-9]|X)$'),
    ADD CONSTRAINT ck_user_telephone_format CHECK (telephone REGEXP '^[0-9]{11}$');

-- 创建存款类型表
CREATE TABLE deposit
(
    savingID  SERIAL PRIMARY KEY COMMENT '存款类型ID（自增主键）',
    savingName  VARCHAR(20) NOT NULL COMMENT '存款类型名称',
    descrip VARCHAR(50) COMMENT '存款类型描述',
    CONSTRAINT ck_deposit_types CHECK (
        savingName IN ('活期', '定活两便') OR
        savingName REGEXP '^活期[0-9]+年$' OR
        savingName REGEXP '^(定期|零存整取)[一二三四五六七八九十]+年$'
        )
) COMMENT '存款类型表';

-- 创建银行卡信息表
CREATE TABLE cardInfo
(
    cardID  CHAR(16) PRIMARY KEY COMMENT '银行卡号（16位，固定格式）',
    curID  VARCHAR(10) DEFAULT 'RMB' COMMENT '币种（默认RMB）',
    savingID INT NOT NULL COMMENT '存款类型ID（逻辑外键）',
    openDate  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '开户日期（默认当前时间）',
    openMoney  NUMERIC(18,2) NOT NULL CHECK (openMoney >= 1) COMMENT '开户金额（≥1元）',
    balance  NUMERIC(18,2) NOT NULL CHECK (balance >= 1) COMMENT '账户余额（≥1元）',
    pass CHAR(6) DEFAULT '888888' COMMENT '密码（6位数字，默认888888）',
    IsReportLoss VARCHAR(3) DEFAULT '否' COMMENT '是否挂失（默认否）',
    customerID INT NOT NULL COMMENT '客户ID（逻辑外键）'
) COMMENT '银行卡信息表';

-- 银行卡信息表约束
ALTER TABLE cardInfo
    ADD CONSTRAINT ck_card_pass_format CHECK (pass REGEXP '^[0-9]{6}$'),
    ADD CONSTRAINT ck_card_loss_status CHECK (IsReportLoss IN ('是', '否')),
    ADD CONSTRAINT ck_card_format CHECK (cardID REGEXP '^10103576[0-9]{8}$');

-- 创建交易信息表
CREATE TABLE tradeInfo
(
    tradeID  SERIAL PRIMARY KEY COMMENT '交易ID（自增主键）',
    tradeDate  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '交易日期（默认当前时间）',
    tradeType  CHAR(6) COMMENT '交易类型（存入/支取）',
    cardID  CHAR(16) COMMENT '银行卡号（16位，逻辑外键）',
    tradeMoney  NUMERIC(18,2) COMMENT '交易金额（＞0元）',
    remark  TEXT COMMENT '交易备注'
) COMMENT '银行卡交易记录表';

-- 交易信息表约束
ALTER TABLE tradeInfo
    ADD CONSTRAINT ck_trade_type CHECK (tradeType IN ('存入', '支取')),
    ADD CONSTRAINT ck_trade_money CHECK (tradeMoney > 0),
    ADD CONSTRAINT ck_trade_card_format CHECK (cardID REGEXP '^10103576[0-9]{8}$');

INSERT INTO deposit (savingName,descrip) VALUES ('活期','按存款日结算利息');
INSERT INTO deposit (savingName,descrip) VALUES ('定期一年','存款期是1年');
INSERT INTO deposit (savingName,descrip) VALUES ('定期二年','存款期是2年');
INSERT INTO deposit (savingName,descrip) VALUES ('定期三年','存款期是3年');
INSERT INTO deposit (savingName) VALUES ('定活两便');
INSERT INTO deposit (savingName,descrip) VALUES ('零存整取一年','存款期是1年');
INSERT INTO deposit (savingName,descrip) VALUES ('零存整取二年','存款期是2年');
INSERT INTO deposit (savingName,descrip) VALUES ('零存整取三年','存款期是3年');


INSERT INTO userInfo(customerid,customerName,PID,telephone,address)
VALUES(1,'John','110000000000000001','13900000001','江苏省南京市');
INSERT INTO userInfo(customerid,customerName,PID,telephone)
VALUES(2,'Jane','110000000000000002','13900000002');
INSERT INTO userInfo(customerid,customerName,PID,telephone)
VALUES(3,'Michael','110000000000000003','13900000003');
INSERT INTO userInfo(customerid,customerName,PID,telephone)
VALUES(4,'Emily','110000000000000004','13900000004');
INSERT INTO userInfo(customerid,customerName,PID,telephone)
VALUES(5,'David','110000000000000005','13900000005');
INSERT INTO userInfo(customerid,customerName,PID,telephone)
VALUES(6,'Sarah','110000000000000006','13900000006');
INSERT INTO userInfo(customerid,customerName,PID,telephone)
VALUES(7,'Robert','110000000000000007','13900000007');
INSERT INTO userInfo(customerid,customerName,PID,telephone)
VALUES(8,'Jessica','110000000000000008','13900000008');
INSERT INTO userInfo(customerid,customerName,PID,telephone)
VALUES(9,'William','110000000000000009','13900000009');
INSERT INTO userInfo(customerid,customerName,PID,telephone)
VALUES(10,'Olivia','110000000000000010','13900000010');

INSERT INTO cardInfo(cardid,curid,savingid,openmoney,balance,pass,isreportloss,customerid)
VALUES('1010357600000001','RMB',1,1000.00,1000.00,'888888','否',1);
INSERT INTO cardInfo(cardid,curid,savingid,openmoney,balance,pass,isreportloss,customerid)
VALUES('1010357600000002','RMB',2,1.00,1.00,'888888','否',2);
INSERT INTO cardInfo(cardid,curid,savingid,openmoney,balance,pass,isreportloss,customerid)
VALUES('1010357600000003','RMB',2,1.00,1.00,'888888','否',3);
INSERT INTO cardInfo(cardid,curid,savingid,openmoney,balance,pass,isreportloss,customerid)
VALUES('1010357600000004','RMB',2,1.00,1.00,'888888','否',4);
INSERT INTO cardInfo(cardid,curid,savingid,openmoney,balance,pass,isreportloss,customerid)
VALUES('1010357600000005','RMB',3,500.00,500.00,'888888','否',5);
INSERT INTO cardInfo(cardid,curid,savingid,openmoney,balance,pass,isreportloss,customerid)
VALUES('1010357600000006','RMB',3,300.00,300.00,'888888','否',6);
INSERT INTO cardInfo(cardid,curid,savingid,openmoney,balance,pass,isreportloss,customerid)
VALUES('1010357600000007','RMB',3,200.00,200.00,'888888','否',7);
INSERT INTO cardInfo(cardid,curid,savingid,openmoney,balance,pass,isreportloss,customerid)
VALUES('1010357600000008','RMB',3,150.00,150.00,'888888','否',8);
INSERT INTO cardInfo(cardid,curid,savingid,openmoney,balance,pass,isreportloss,customerid)
VALUES('1010357600000009','RMB',3,100.00,100.00,'888888','否',9);
INSERT INTO cardInfo(cardid,curid,savingid,openmoney,balance,pass,isreportloss,customerid)
VALUES('1010357600000010','RMB',3,80.00,80.00,'888888','否',10);



