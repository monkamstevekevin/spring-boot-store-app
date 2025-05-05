# Improvement Tasks Checklist

## Database and Data Model Improvements
- [ ] Add unique constraint on user email field
- [ ] Add indexes on frequently queried fields (e.g., user email, order customer_id)
- [ ] Fix typo in package name "produts" to "products"
- [ ] Implement relationship between User and Cart entities
- [ ] Add audit fields (createdAt, updatedAt) to relevant entities
- [ ] Change EAGER fetching to LAZY in Cart-CartItem relationship to improve performance
- [ ] Add proper constraints for Role and OrderStatus values
- [ ] Add default value for order status in migration script
- [ ] Explicitly define ON DELETE behavior for all foreign keys
- [ ] Rename non-descriptive constraint "pr" in cart_items table

## Security Improvements
- [ ] Remove hardcoded database credentials from application-dev.yaml
- [ ] Add @JsonIgnore to password field in User entity to prevent exposure
- [ ] Implement rate limiting for authentication endpoints to prevent brute force attacks
- [ ] Review and restrict public access to cart endpoints
- [ ] Add validation annotations to entity fields (e.g., @Email, @NotBlank)
- [ ] Implement proper error handling for invalid input data
- [ ] Add HTTPS configuration for production environment
- [ ] Implement proper CORS configuration

## Configuration Improvements
- [ ] Complete the production configuration in application-prod.yaml
- [ ] Fix typo in websiteUrl in application-dev.yaml ("locahost" to "localhost")
- [ ] Fix typo in websiteUrl in application-prod.yaml (".con" to ".com")
- [ ] Change ddl-auto from "update" to "validate" in production environment
- [ ] Externalize all sensitive configuration to environment variables
- [ ] Add proper logging configuration

## Code Quality Improvements
- [ ] Fix inconsistent variable naming conventions (e.g., "UserService", "ProductId")
- [ ] Create a CartItemNotFoundException instead of using ProductNotFoundException
- [ ] Implement mechanism to transfer anonymous cart to user account after login
- [ ] Add comprehensive JavaDoc comments to public methods and classes
- [ ] Implement proper exception handling with meaningful error messages
- [ ] Add unit tests for critical business logic
- [ ] Add integration tests for API endpoints
- [ ] Implement proper input validation in controllers
- [ ] Refactor duplicate code in controllers and services
- [ ] Add pagination for list endpoints

## Architecture Improvements
- [ ] Implement proper DTO validation using Bean Validation
- [ ] Add caching for frequently accessed data
- [ ] Implement proper error response format
- [ ] Add API documentation using Swagger/OpenAPI
- [ ] Implement proper health check endpoints
- [ ] Add metrics collection for monitoring
- [ ] Implement proper transaction management
- [ ] Add support for internationalization
- [ ] Implement proper event-driven architecture for order processing
- [ ] Add background job processing for long-running tasks

## DevOps Improvements
- [ ] Create Docker configuration for development and production
- [ ] Implement CI/CD pipeline
- [ ] Add database migration verification in CI pipeline
- [ ] Create proper README with setup instructions
- [ ] Add environment-specific configuration for different deployment environments
- [ ] Implement proper backup strategy for database
- [ ] Add monitoring and alerting configuration
- [ ] Implement proper logging and log aggregation