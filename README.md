# Auth Demo API

A RESTful Authentication & User Management Service built with Java 17, Spring Boot 3, Spring Data JPA, H2 In-Memory Database, and Swagger UI. Designed for quick integration, role-based workflows, and cloud deployment on Render.

---

## Live Demo

* **Live Deployment:** [https://auth-demo-api-ryxh.onrender.com](https://auth-demo-api-ryxh.onrender.com)
* **Swagger UI Documentation:** [https://auth-demo-api-ryxh.onrender.com/swagger-ui/index.html](https://auth-demo-api-ryxh.onrender.com/swagger-ui/index.html)

---

## Features

* **User Authentication & Recovery:** User registration, OTP verification, login, refresh token generation, and password recovery flow.
* **Profile Management:** Fetch user details, update profile/KYC info, and change password securely.
* **Customer Status Tracking:** Manage user account states (`PENDING_VERIFICATION`, `ACTIVE`, `LOCKED`, `CLOSED`).
* **CORS Pre-Configured:** Cross-Origin Resource Sharing (`@CrossOrigin`) enabled across all endpoints for frontend integration and API testing.
* **API Documentation:** Integrated OpenAPI 3.0 / Swagger UI for testing endpoints directly in the browser.
* **Containerized Deployment:** Includes Docker and Maven Wrapper support optimized for cloud hosting (Render).

---

## Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot 3.2.5
* **Database:** H2 In-Memory Database (`jdbc:h2:mem:authdb`)
* **ORM:** Spring Data JPA / Hibernate
* **API Documentation:** Springdoc OpenAPI (Swagger UI)
* **Build Tool:** Maven

---

## Project Architecture

```text
src/main/java/com/example/auth_demo/
├── controller/          # REST Controllers (Auth, Customer, User)
├── dto/                 # Request & Response Data Transfer Objects
├── model/              # JPA Entities & Enums (UserEntity, Role, CustomerStatus)
├── repository/          # Spring Data JPA Repositories
└── service/             # Business Logic Layer (UserService & Implementation)

Quickstart (Local Setup)PrerequisitesJDK 17 or higherMaven 3.8+ (or use included ./mvnw)GitRunning LocallyClone the repository:Bashgit clone [https://github.com/praksingh-2702/auth-demo-api.git](https://github.com/praksingh-2702/auth-demo-api.git)
cd auth-demo-api
Build and run the project:Bash./mvnw spring-boot:run
(On Windows Command Prompt / PowerShell, use mvnw spring-boot:run)Access the application:Swagger UI: http://localhost:8080/swagger-ui/index.htmlH2 Database Console: http://localhost:8080/h2-consoleJDBC URL: jdbc:h2:mem:authdbUsername: saPassword: (leave blank)
Main API Endpoints
1. Authentication Management (/api/v1/auth)
MethodEndpointDescription
POST/api/v1/auth/registerRegister a new user
POST/api/v1/auth/verify-otpVerify OTP and activate account
POST/api/v1/auth/loginAuthenticate user & return token
POST/api/v1/auth/forgot-passwordRequest password reset token
POST/api/v1/auth/reset-passwordReset password using valid reset token
POST/api/v1/auth/refresh-tokenGenerate fresh access token
POST/api/v1/auth/logoutTerminate session2.

User Profile Management(/api/v1/users)
MethodEndpointDescription
GET/api/v1/users/meFetch logged-in user details
PUT/api/v1/users/meUpdate personal profile details
POST/api/v1/users/change-passwordUpdate account password3.

Customer Management (/api/v1/customer)
MethodEndpointDescription
GET/api/v1/customer/profileGet customer profile details
POST/api/v1/customer/profileSubmit customer KYC information
GET/api/v1/customer/allList all customer accounts
GET/api/v1/customer/statusFetch current account status

🌐 Deploying to RenderPush your latest code to GitHub:Bashgit add .
git commit -m "Update README and final configs"
git push origin main
Create a Web Service on Render.Connect your GitHub repository praksingh-2702/auth-demo-api.Set the build and start settings:Runtime: Docker (or Java)Build Command: ./mvnw clean package -DskipTestsStart Command: java -jar target/auth-demo-0.0.1-SNAPSHOT.jarEnvironment Variable: Add JAVA_VERSION = 17Note: Because H2 is an in-memory database, database records clear on every application restart or redeploy on Render.
