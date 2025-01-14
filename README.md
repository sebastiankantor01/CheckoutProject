# Checkout Component Service

## How to Run This Project

### Quick Start
1. Clone repository
2. Build and run using Docker Compose:
   `docker-compose up --build`
3. Test the application via Swagger UI: http://localhost:8080/swagger-ui/index.html

## Access and Authentication

After starting the application, navigate to http://localhost:8080/swagger-ui/index.html

You'll need to authenticate to use the API. You have two options:

#### 1. Use existing account:
- Email: user@user.com
- Password: 12345678

#### 2. Create a new account using `/register` endpoint

## Step by Step Guide

### Authentication
1. After opening Swagger UI, you need to authenticate first.
2. You can register your own account using `/register` endpoint or use the existing test account (user@user.com / 12345678)
3. Use `/login` endpoint with your credentials
4. In the response, you'll receive a JWT token - copy it (without quotes)
5. Go to the top of the swagger page and click "Authorize" button
6. Paste the copied JWT token and click "Authorize"

### Using the API
1. Now you can use two main endpoints: `/scan` and `/cart`
2. To add products to the cart, use `/scan` endpoint with product name parameter - available products are: A, B, C, D
3. Each execution of `/scan` adds one product to the cart
4. To add multiple items of the same product, execute the request multiple times
5. To view cart contents, prices, and applied discounts, use `/cart` endpoint

## Available Discount Methods

### Quantity Discounts
The system automatically reduces the unit price of a product when a specified quantity is reached in the cart.

**Examples:**
- Product A: standard price 40.00
  - when buying 3 or more pieces: price drops to 30.00 per piece
  - example: 4 × A = 4 × 30.00 = 120.00 (instead of 4 × 40.00 = 160.00)

### Combined Discounts
The system grants an additional discount when specific pairs of products are present in the cart.

**Basic rules:**
- Discount is applied for each pair of products
- When multiple combinations are possible, the system chooses the most beneficial for the customer
- The same product can only be used in one combination

**Available combinations:**
- A + B = 5.00 discount
- A + C = 10.00 discount

**Examples:**

1. Simple case:
   - Cart: A, B
   - Discount: 5.00 (one A+B pair)

2. Multiple pairs:
   - Cart: A, A, B, B
   - Discount: 10.00 (two A+B pairs, 5.00 each)

3. Competing combinations:
   - Cart: A, B, C
   - Available combinations:
     - A+B = 5.00
     - A+C = 10.00
   - System will choose A+C (10.00) as it provides greater benefit
