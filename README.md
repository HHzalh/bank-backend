

银行后台管理系统README.md如下：

```markdown
# 银行后台管理系统

该项目是一个基于Spring Boot的银行后台管理系统，提供了登录、账户管理、存款、取款、转账、交易记录查询等核心银行功能。同时支持通过JWT进行身份验证，并整合了Redis和COS（腾讯云对象存储）服务。

## 项目特性

- **用户和管理员权限分离**：管理员和普通用户有不同的接口和权限管理。
- **完整的账户生命周期管理**：包括注册、登录、资料更新、密码修改等功能。
- **银行卡管理**：支持存款、取款、转账、挂失、修改密码等操作。
- **交易记录**：支持分页查询所有交易记录。
- **文件上传**：通过腾讯云COS支持用户头像上传。
- **全局异常处理**：优雅地处理业务异常和运行时异常。
- **自动填充字段**：通过AOP切面实现数据字段的自动填充。

## 技术栈

- **Spring Boot**：快速开发框架。
- **JWT**：用于身份认证。
-- **Redis**：用于令牌存储。
- **COS (Tencent Cloud Object Storage)**：用于存储用户头像。
- **MyBatis Plus**：数据库访问层。
- **Swagger / OpenAPI**：接口文档生成工具。

## 模块说明

- **AdminController**：管理员的登录、账户管理、银行卡管理、存款管理、交易记录管理。
- **UserController**：普通用户的登录、注册、账户管理、转账、存款、取款、挂失、交易记录查询。
- **Service 层**：业务逻辑处理，包括CardService, CustomerService, TradeService等。
- **Mapper 层**：数据库操作接口，使用MyBatis @Mapper 注解。
- **Model 层**：包含数据传输对象（DTO）、实体类（Entity）、视图对象（VO）。
- **Utils 工具类**：JWT工具、COS工具、银行卡ID生成工具、Redis工具。
- **AOP/Aspect**：自动填充切面，统一处理数据字段。
- **Exception 异常处理**：全局异常拦截，统一返回格式。
- **Config 配置类**：包括CORS跨域配置、WebMvc配置、Redis配置等。

## 接口文档

通过`OpenAPI`提供接口文档支持，访问路径：`/swagger-ui.html` 或 `/swagger-ui/index.html`。

## 安全机制

- **JWT Token**：用于管理员和普通用户的身份认证。
- **Redis 缓存 Token**：支持Token的存储与清理。
- **拦截器**：`JwtTokenAdminInterceptor` 和 `JwtTokenUserInterceptor` 分别拦截管理员与用户请求。

## 启动与部署

1. 下载项目代码并导入IDE（如IntelliJ IDEA）。
2. 配置`application.yml`中的数据库、JWT、Redis、COS等信息。
3. 执行SQL脚本`sql/bank.sql`，初始化数据库。
4. 运行`BankBackendApplication.java`启动Spring Boot服务。

## 使用说明

- � to管理员：通过`/Admin/customer/login`进行登录，获取Token后访问管理员接口。
- 普通用户：通过`/User/user/login`进行登录，获取Token后访问用户接口。
- 接口测试：推荐使用Postman或Swagger UI进行接口测试。

## 贡献者

欢迎提交Issue和Pull Request来帮助改进该项目。

## 许可证

该项目使用Apache-2.0 license，请遵守相关开源协议。
```