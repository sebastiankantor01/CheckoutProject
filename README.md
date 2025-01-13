How to run this project:
1. Clone repository
2. Build and run using Docker Compose:
   `docker-compose up --build`
3. Test the application
   - swagger: http://localhost:8080/swagger-ui/index.html#/

### Access and Authentication
After starting the application, navigate to http://localhost:8080/swagger-ui.html

You'll need to authenticate to use the API. 

You have two options:

1. Use existing account:
   
Email: user@user.com

Password: 12345678

3. Create new account using /register endpoint

### Step by step guide:
1. Authentication
After opening Swagger UI, you need to authenticate first.
You can register your own account using /register endpoint or use existing test account (user@user.com / 12345678).

Use /login endpoint with your credentials.
In the response, you'll receive JWT token - copy it (without quotes).
Go to the top of the swagger page and click "Authorize" button. Paste the copied JWT token and click "Authorize".

2. Using the API
Now you can use two main endpoints: /scan and /cart.

To add products to cart, use /scan endpoint with product name parameter - available products are: A, B, C, D.
Each execution of /scan adds one product to cart. To add multiple items of the same product, execute the request multiple times.

To view cart contents, prices and applied discounts, use /cart endpoint.

### Available Discount Methods
1. Quantity Discounts
The system automatically reduces the unit price of a product when a specified quantity is reached in the cart.

Examples:
Product A: standard price 40.00
when buying 3 or more pieces: price drops to 30.00 per piece
example: 4 × A = 4 × 30.00 = 120.00 (instead of 4 × 40.00 = 160.00)

2. Combined Discounts
The system grants an additional discount when specific pairs of products are present in the cart.

Basic rules:
Discount is applied for each pair of products.
When multiple combinations are possible, the system chooses the most beneficial for the customer.
The same product can only be used in one combination.

Available combinations:
A + B = 5.00 discount
A + C = 10.00 discount

Examples:
- Simple case:
Cart: A, B
Discount: 5.00 (one A+B pair)

- Multiple pairs:
Cart: A, A, B, B
Discount: 10.00 (two A+B pairs, 5.00 each)

- Competing combinations:
Cart: A, B, C
Available combinations:
A+B = 5.00
A+C = 10.00
System will choose A+C (10.00) as it provides greater benefit.
