# Starter Project

A Spring Boot starter project with authentication, file handling, email services, and Excel export functionality.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Setup and Installation](#setup-and-installation)
- [Available Services](#available-services)

## Features
- JWT Authentication
- File Upload/Download Management
- Email Service Integration
- Excel Export Functionality
- PostgreSQL Database Integration
- Profile-based Configuration

## Prerequisites
- Java 17
- PostgreSQL Database
- Maven
- SMTP Server Access (Gmail)

## Configuration

### Database Configuration
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/starter_system
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Mail Configuration
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```

### JWT Configuration
```properties
jwt.expiresIn=86400000  # 24 hours
```

### File Upload Configuration
```properties
uploads.extensions=pdf,png,jpeg,jpg
uploads.directory=/home/uploads/ne-springboot/starter/uploads
```

## API Documentation
Swagger UI URL: `http://localhost:9091/Starter/swagger-ui.html`
API Base URL: `http://localhost:9091/Starter`

## Setup and Installation

1. Clone the repository
2. Configure application.properties and application-dev.properties
3. Create the PostgreSQL database
4. Run the application:
```bash
./mvnw spring-boot:run
```

## Available Services

### Mail Service
- Password Reset Emails
- Account Activation Emails
- Verification Notifications
- Token Expiry Notifications

### File Service
- Support for multiple file types (PDF, PNG, JPEG, JPG)
- File upload/download functionality
- User profile picture management

### Excel Service
- Generate Excel reports
- Support for custom headers and data
- Auto-column sizing

### Authentication
- JWT-based authentication
- Token validation
- User role management

## Frontend Integration
Frontend URLs:
- Login: `http://localhost:3006/auth/login`
- Reset Password: `http://localhost:3006/auth/reset-password`

## Support
For support, please contact: aimablebyumvuhore

## Contributing
Please read the contribution guidelines before submitting pull requests.

## License
This project is licensed under the MIT License - see the LICENSE file for details
