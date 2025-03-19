# E-commerce Application

This is a robust e-commerce application built with Spring Boot.

The application supports key functionalities such as product management, user authentication, cart management, order processing, and payment handling. 

It also includes caching and circuit breaker patterns to improve performance and resilience.

## Features
- **User Management:**  
  - User registration and profile management.
  - Authentication using JWT.
  - Role-based access control (e.g.,ADMIN and CUSTOMER).

- **Product Management:**  
  - CRUD operations for products.
  - List products by category.
  - Caching to optimize product lookups.

- **Cart Management:**  
  - Add, update, and remove products in the cart.
  - View all cart items.
  - Clear the entire cart.

- **Order Processing:**  
  - Place orders from cart items.
  - Fetch orders for a user.
  - Cancel orders (admin only).

- **Payment Handling:**  
  - Process payments for orders.
  - Update order status and clear the cart after payment.
  
- **Resilience & Caching:**  
  - Use of caching for improved performance.
  - Circuit breakers to gracefully handle failures.

## Architecture
The application is designed using a layered architecture that includes:

- **Controllers:** REST controllers that handle incoming HTTP requests.
- **Services:** Business logic encapsulated in service classes (e.g., `CartServiceImpl`, `OrderServiceImpl`, `PaymentServiceImpl`, `ProductServiceImpl`, `UserServiceImpl`).
- **Repositories:** Data persistence managed via Spring Data JPA.
- **Security:** Configured using Spring Security with JWT for authentication and role-based access.
- **Exception Handling & DTO Mapping:** Global exception handling is implemented using `@RestControllerAdvice` to ensure consistent error responses, and dedicated mapper classes (e.g., `CartItemMapper`) convert entities to DTOs for streamlined data transfer.
- **Caching & Resilience:** Integrated caching via Spring Cache annotations and circuit breaker patterns (using libraries like Resilience4j) to manage fallbacks in case of service failure.

## Tech Stack

- **Backend Framework:** Spring Boot
- **Database:** (MySQL)
- **ORM:** Spring Data JPA / Hibernate
- **Security:** Spring Security, JWT
- **Caching:** Spring Cache
- **Logging:** SLF4J with Logback
- **Resilience:** Circuit Breaker pattern (Resilience4j or similar)

## Setup and Installation
- **Clone the Repository:**

   git clone https://github.com/vish-govind/ecommerce.git
   cd your-ecommerce-app

- **Update your application.yml with your actual database configuration**

	spring.datasource.url=jdbc:mysql://localhost:3306/yourdatabase
	spring.datasource.username=yourusername
	spring.datasource.password=yourpassword
	spring.jpa.hibernate.ddl-auto=update
	
- **Build the Project:**
    
     mvn clean install
     
- **Run the Application:** 

	- **Using the Maven Spring Boot plugin:**  
        mvn spring-boot:run
    - **execute the generated JAR file:**
      java -jar target/ecommerce-app.jar
	
# API Endpoints
Below are some of the key API endpoints available in this application:

**Authentication:**

POST /auth/login – Login with username and password to receive a JWT token.

**User Management:**

POST /users/register – Register a new user.

GET /users/profile?username={username} – Fetch user profile.

PUT /users/update – Update user profile.

DELETE /users/delete/{userId} – Delete a user (admin only).

**Product Management:**

POST /products/add – Add a new product (admin only).

PUT /products/update/{id} – Update product details (admin only).

DELETE /products/delete/{id} – Delete a product (admin only).

GET /products/list – List all products.

GET /products/category/{category} – Get products by category.

**Cart Management:**

POST /cart/add/{userId} – Add an item to the cart.

PUT /cart/update/{userId} – Update cart item quantity.

DELETE /cart/remove/{userId}/{productId} – Remove an item from the cart.

GET /cart/items/{userId} – Retrieve all items in the cart.

DELETE /cart/remove/{userId} – Clear the cart.

**Order Processing:**

POST /orders/place/{userId} – Place an order based on cart items.

GET /orders/user/{userId} – Retrieve orders for a specific user.

GET /orders/{orderId} – Fetch order details.

PUT /orders/cancel/{orderId} – Cancel an order (admin only).

**Payment Handling:**

POST /payments/process/{orderId} – Process payment for an order (customer only).

# Security

**Authentication:**
The application uses JWT-based authentication. Users must log in via the /auth/login endpoint to receive a token, which should be included in the Authorization header for secured endpoints.

**Authorization:**
Role-based access is enforced using Spring Security annotations (e.g., @PreAuthorize). For example, only users with the ADMIN role can add, update, or delete products.

