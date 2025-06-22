# Bank Management System

This project is a bank management system based on Spring Boot, providing core banking functions such as login, account management, deposit, withdrawal, transfer, transaction record query, etc. It also supports JWT-based authentication and integrates Redis and COS (Tencent Cloud Object Storage) services.

## Project Features

- **Separation of User and Administrator Permissions**: Administrators and regular users have different interfaces and permission management.
- **Complete Account Lifecycle Management**: Includes registration, login, profile update, password change, and more.
- **Bank Card Management**: Supports deposit, withdrawal, transfer, card loss reporting, password change, and other operations.
- **Transaction Records**: Supports paginated queries of all transaction records.
- **File Upload**: Supports user avatar uploads via Tencent Cloud COS.
- **Global Exception Handling**: Gracefully handles business and runtime exceptions.
- **Automatic Field Filling**: Achieves automatic data field filling through AOP aspects.

## Technology Stack

- **Spring Boot**: Rapid development framework.
- **JWT**: Used for authentication.
- **Redis**: Used for token storage.
- **COS (Tencent Cloud Object Storage)**: Used for storing user avatars.
- **MyBatis Plus**: Database access layer.
- **Swagger / OpenAPI**: Interface documentation generation tool.

## Module Description

- **AdminController**: Administrator login, account management, bank card management, deposit management, transaction record management.
- **UserController**: Regular user login, registration, account management, transfer, deposit, withdrawal, card loss reporting, transaction record query.
- **Service Layer**: Business logic processing, including CardService, CustomerService, TradeService, etc.
- **Mapper Layer**: Database operation interface using MyBatis @Mapper annotation.
- **Model Layer**: Contains Data Transfer Objects (DTO), Entity classes (Entity), View Objects (VO).
- **Utils Utility Classes**: JWT tools, COS tools, bank card ID generation tools, Redis tools.
- **AOP/Aspect**: Auto-fill aspect for unified data field handling.
- **Exception Handling**: Global exception interception with unified return format.
- **Config Configuration Classes**: Includes CORS cross-domain configuration, WebMvc configuration, Redis configuration, etc.

## API Documentation

API documentation is supported via `OpenAPI`, accessible at: `/swagger-ui.html` or `/swagger-ui/index.html`.

## Security Mechanisms

- **JWT Token**: Used for authentication of administrators and regular users.
- **Redis Token Caching**: Supports token storage and cleanup.
- **Interceptors**: `JwtTokenAdminInterceptor` and `JwtTokenUserInterceptor` intercept administrator and user requests, respectively.

## Startup and Deployment

1. Download the project code and import it into an IDE (e.g., IntelliJ IDEA).
2. Configure database, JWT, Redis, COS, and other information in `application.yml`.
3. Execute the SQL script `sql/bank.sql` to initialize the database.
4. Run `BankBackendApplication.java` to start the Spring Boot service.

## Usage Instructions

- **Administrator**: Log in via `/Admin/customer/login`, then access administrator interfaces using the obtained token.
- **Regular User**: Log in via `/User/user/login`, then access user interfaces using the obtained token.
- **Interface Testing**: Recommended to use Postman or Swagger UI for interface testing.

## Contributors

Welcome to submit Issues and Pull Requests to help improve this project.

## License

This project uses the Apache-2.0 license. Please comply with the relevant open-source agreements.