# 📚 银行业务系统技术文档

## 📋 一、项目概述

本项目是一个基于Spring Boot框架开发的银行业务管理系统后端服务，旨在提供用户账户管理、银行卡管理、存取款、转账等基本银行业务功能。系统采用了分层架构设计，具有良好的可扩展性和安全性。

## 🛠️ 二、技术架构

### 1. 开发环境
- 编程语言：Java
- JDK 17
- 框架：Spring Boot 3.3.12
- 数据库：MySQL
- 缓存：Redis
- ORM框架：MyBatis
- 项目管理工具：Maven

### 2. 主要依赖
- Spring Boot Web：提供Web应用开发支持
- Spring Boot Data Redis：提供Redis缓存支持
- MyBatis：数据库ORM映射
- MySQL Connector：MySQL数据库连接驱动
- Lombok：简化Java代码
- Knife4j：API文档生成工具 地址：http://localhost:8080/api/doc.html
- JWT：用户认证及鉴权
- Hutool：Java工具类库
- 腾讯云COS：对象存储服务
- PageHelper：分页插件

### 3. 项目结构
项目遵循标准的Spring Boot项目结构，主要包含以下模块：

```
src/main/java/com/king/bankbackend/
├── annotation/     # 自定义注解
├── aspect/         # AOP切面
├── common/         # 通用类
├── config/         # 配置类
├── constant/       # 常量定义
├── context/        # 上下文
├── controller/     # 控制器
│   ├── admin/      # 管理员控制器
│   └── user/       # 用户控制器
├── exception/      # 异常处理
├── interceptor/    # 拦截器
├── mapper/         # MyBatis映射接口
├── model/          # 数据模型
│   ├── dto/        # 数据传输对象
│   ├── entity/     # 实体类
│   └── vo/         # 视图对象
├── properties/     # 配置属性类
├── service/        # 服务接口
│   └── impl/       # 服务实现类
└── utils/          # 工具类
```

## 💾 三、数据库设计

### 1. 主要表结构

#### 用户信息表（userInfo）
- userID：用户编号（主键）
- userName：用户姓名
- PID：身份证号（唯一）
- gender：性别（男/女）
- telephone：联系电话
- address：联系地址
- imageUrl：头像地址
- role：权限（0-普通用户,1-管理员）
- account：登录账号
- password：登录密码
- create_time：创建时间
- update_time：更新时间

#### 银行卡信息表（cardInfo）
- cardID：银行卡号（16位，格式：10103576xxxxxxxx）
- curID：币种（默认为RMB）
- savingID：存款类型ID
- openDate：开户日期
- openMoney：开户金额（≥1元）
- balance：账户余额（≥0元）
- pass：银行卡密码
- IsReportLoss：是否挂失（是/否）
- customerID：用户ID
- customerName：用户姓名
- create_time：创建时间
- update_time：更新时间

#### 存款类型表（deposit）
- savingID：存款类型ID（主键）
- savingName：存款类型名称
- descrip：存款类型描述
- create_time：创建时间
- update_time：更新时间

#### 交易信息表（tradeInfo）
- tradeID：交易ID（主键）
- tradeDate：交易日期
- tradeType：交易类型（存款/取款/转账）
- cardID：银行卡号
- tradeMoney：交易金额（>0元）
- remark：交易备注
- create_time：创建时间
- update_time：更新时间

### 2. 表关系
- 用户与银行卡：一对多关系，一个用户可以拥有多张银行卡
- 银行卡与交易记录：一对多关系，一张银行卡可以有多条交易记录
- 存款类型与银行卡：一对多关系，一种存款类型可以对应多张银行卡

## 🔍 四、功能模块

### 1. 管理员模块

#### 📝 用户模块
- 用户查询：查询系统中所有用户信息，支持条件筛选和分页
- 用户管理：新增、修改、删除用户信息
- 管理员管理：上传头像、获取所有管理员信息
- 导出数据报表

#### 💳 银行卡模块
- 银行卡查询：查询所有银行卡信息，支持条件筛选和分页
- 银行卡管理：新增、修改、删除银行卡信息
- 银行卡状态管理：调整银行卡的挂失状态
- 导出数据报表

#### 💰 存款类型模块
- 存款类型查询：查询所有存款类型信息，支持条件筛选和分页
- 存款类型管理：新增、修改、删除存款类型
- 导出数据报表

#### 🔄 交易信息模块
- 交易记录查询：查询所有交易记录，支持条件筛选和分页
- 删除交易信息
- 导出数据报表

### 2. 用户模块

#### 👤 用户信息管理
- 用户注册：提供新用户注册功能，注册时自动开卡
- 用户登录：通过账号密码验证身份，登录成功后返回JWT令牌
- 用户信息查询：获取当前登录用户的信息
- 用户资料更新：修改用户个人资料，包括姓名、电话、地址等信息
- 修改用户密码：提供用户账号密码修改功能
- 头像上传：支持用户上传和更新头像

#### 💳 银行卡管理
- 银行卡查询：查询当前用户的银行卡列表和详情
- 银行卡挂失：提供银行卡挂失功能
- 修改银行卡密码：提供银行卡交易密码修改功能

#### 💸 资金操作
- 存款：向银行卡中存入资金
- 取款：从银行卡中取出资金
- 转账：在不同银行卡之间转移资金
- 余额查询：查询银行卡当前余额

#### 📊 交易记录
- 交易记录查询：查询用户的所有交易记录，支持分页和条件筛选

## 🚀 五、核心功能实现

### 1. 用户认证与授权
系统使用JWT（JSON Web Token）结合Redis实现用户认证与授权：
- 用户登录成功后，服务端生成JWT令牌返回给前端
- 同时将用户信息存储在Redis中，设置适当的过期时间
- 前端请求时携带令牌，后端通过拦截器验证令牌的有效性
- 拦截器从Redis中获取用户信息，避免重复查询数据库，提高系统性能
- 基于用户角色（普通用户/管理员）实现不同的权限控制
- 用户退出登录时，从Redis中移除相关缓存数据
- Redis还用于存储用户会话信息，实现会话共享，支持分布式部署

### 2. 用户注册自动开卡
系统设计了完善的用户注册流程：
- 用户填写个人基本信息（姓名、身份证、电话等）提交注册
- 系统验证身份证号的唯一性，确保一个身份证只能注册一个账号
- 注册成功后，系统自动为用户创建一个默认的活期银行卡
- 自动生成16位的银行卡号（格式：10103576xxxxxxxx）
- 设置初始密码和开户金额，完成开卡流程
- 整个流程在一个事务中完成，确保数据一致性

### 3. 范围分页查询
系统实现了高效的范围分页查询功能：
- 使用PageHelper插件实现物理分页，减少数据库压力
- 支持多条件组合查询，如按卡号、用户名、余额范围等条件筛选
- 支持按时间范围查询交易记录或银行卡开户记录
- 查询结果按照指定字段排序，提升用户体验
- 返回结果包含分页信息（总记录数、总页数、当前页等）

### 4. Redis优化登录及缓存
系统利用Redis实现了多方面的性能优化：
- 登录信息缓存：用户登录后，将用户信息和权限存入Redis，提高后续请求的响应速度
- 热点数据缓存：对频繁访问的数据进行缓存，减轻数据库压力
- 防重复提交：使用Redis实现接口幂等性控制，防止表单重复提交
- 验证码存储：登录验证码等临时数据存储在Redis中，设置合理的过期时间
- 会话管理：使用Redis存储会话信息，支持分布式部署下的会话共享

### 5. 腾讯云COS实现头像管理
系统集成了腾讯云对象存储服务，实现用户头像的上传和管理：
- 用户上传头像时，前端将图片文件发送到后端
- 后端接收文件并进行格式、大小等校验
- 调用腾讯云COS SDK上传图片到云存储空间
- 获取图片的访问URL并更新到用户信息中
- 支持图片替换和删除功能，便于用户更新头像
- 通过CDN加速，提高头像访问速度和体验

## 📝 六、API接口设计

本系统提供了RESTful风格的API接口，所有接口统一前缀为 `/api`。

### 1. 用户端API (`/User`)

#### 👤 用户相关接口
- `POST /User/user/login`: 用户登录
- `POST /User/user/register`: 用户注册
- `GET /User/user/userInfo`: 获取当前登录用户信息
- `PUT /User/user/updateProfile`: 更新用户个人资料
- `PUT /User/user/changedUserPwd`: 修改用户登录密码
- `POST /User/uploadImage`: 上传用户头像

#### 💳 银行卡相关接口
- `GET /User/user/cards`: 查询当前用户的所有银行卡
- `GET /User/cards/{cardId}`: 根据卡号查询余额
- `PUT /User/cards/reportLoss`: 挂失银行卡
- `PUT /User/cards/changedPwd`: 修改银行卡密码

#### 💸 资金操作接口
- `POST /User/cards/deposit`: 存款
- `POST /User/cards/withdraw`: 取款
- `POST /User/cards/transfer`: 转账

#### 📊 交易记录接口
- `POST /User/trade/pageTradeUser`: 分页查询用户交易记录

### 2. 管理员端API (`/Admin`)

#### 📝 用户管理接口 (`/Admin/customer`)
- `POST /Admin/customer/login`: 管理员登录
- `GET /Admin/customer/getAdminInfo`: 获取管理员信息
- `POST /Admin/customer/addCustomer`: 新增用户
- `PUT /Admin/customer/updateCustomer`: 更新用户信息
- `DELETE /Admin/customer/deleteCustomer/{id}`: 删除用户
- `POST /Admin/customer/pageCustomer`: 分页查询用户信息
- `POST /Admin/customer/uploadImage`: (管理员)上传头像

#### 💳 银行卡管理接口 (`/Admin/card`)
- `POST /Admin/card/addCard`: 新增银行卡
- `PUT /Admin/card/changePwd`: (管理员)修改银行卡密码
- `GET /Admin/card/getStatus/{cardid}`: 获取银行卡状态
- `PUT /Admin/card/changeStatus`: 更新银行卡状态（挂失等）
- `DELETE /Admin/card/deleteCard/{cardid}`: 删除银行卡
- `GET /Admin/card/getCard/{cardid}`: 根据卡号获取银行卡信息
- `GET /Admin/card/getCardsByPid/{pid}`: 根据身份证号获取银行卡列表
- `POST /Admin/card/pageCard`: 分页查询银行卡

#### 💰 存款类型管理接口 (`/Admin/deposit`)
- `POST /Admin/deposit/addDeposit`: 新增存款类型
- `PUT /Admin/deposit/updateDeposit`: 更新存款类型
- `DELETE /Admin/deposit/deleteDeposit/{id}`: 删除存款类型
- `GET /Admin/deposit/getDeposit/{savingid}`: 获取存款类型信息
- `POST /Admin/deposit/pageDeposit`: 分页查询存款类型

#### 🔄 交易管理接口 (`/Admin/trade`)
- `DELETE /Admin/trade/deleteTrade/{tradeid}`: 删除交易记录
- `POST /Admin/trade/pageTrade`: 分页查询交易记录

## 🔒 七、事务安全管理

对涉及资金操作的功能（存款、取款、转账等）实现事务管理：
- 使用Spring `@Transactional` 注解保证操作的原子性，要么全部成功，要么全部失败
- 详细记录每笔交易，确保资金流向清晰可追溯
- 实现交易前余额检查，防止透支操作
- 实现幂等性控制，防止同一交易被重复执行

## 🌐 八、系统部署

### 1. 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 5.0+

### 2. 配置说明
系统配置主要在`application.yml`中，包括：
- 服务器配置
- 数据库连接配置
- Redis配置
- JWT配置
- 腾讯云COS对象存储配置

### 3. 部署步骤
1. 创建MySQL数据库并执行`sql/bank.sql`初始化脚本
2. 配置`application.yml`中的数据库连接信息和Redis连接信息
3. 配置JWT密钥和腾讯云COS相关参数
4. 使用Maven打包项目：`mvn clean package`
5. 运行生成的jar包：`java -jar bank-backend-0.0.1-SNAPSHOT.jar`

## 🔮 九、项目可拓展方向（未实现）

### 1. 功能扩展
- 增加利息计算与结算功能
- 添加定期存款到期提醒功能
- 实现大额交易风险控制机制
- 增加短信/邮件通知服务

### 2. 性能优化
- 引入分布式缓存机制，提高系统响应速度
- 优化数据库查询，添加必要的索引

### 3. 安全加固
- 增加登录IP限制和异常行为检测
- 实现敏感操作的二次验证机制
- 增强密码策略，定期提醒用户修改密码

## 📊 十、结论

本银行业务系统基于Spring Boot框架，采用分层架构设计，实现了用户管理、银行卡管理、资金操作和交易记录等核心功能。系统具有良好的可扩展性和安全性，为银行业务的数字化管理提供了有效解决方案。通过持续优化和功能扩展，系统可以满足更复杂的银行业务需求，提供更优质的用户体验。
