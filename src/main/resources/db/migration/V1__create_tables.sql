CREATE TABLE users
(
    user_id     SERIAL       NOT NULL,
    user_hash   VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(100) NOT NULL,
    first_name  VARCHAR(50)  NOT NULL,
    last_name   VARCHAR(50)  NOT NULL,
    created_at  TIMESTAMP    NOT NULL,
    modified_at TIMESTAMP,
    PRIMARY KEY (user_id),
    UNIQUE (email),
    UNIQUE (user_hash)
);

CREATE TABLE carts
(
    cart_id    SERIAL       NOT NULL,
    cart_hash  VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    user_id    BIGINT UNIQUE,
    PRIMARY KEY (cart_id),
    UNIQUE (cart_hash),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE products
(
    product_id             SERIAL         NOT NULL,
    product_hash           VARCHAR(255)   NOT NULL,
    name                   VARCHAR(255)   NOT NULL,
    normal_price           DECIMAL(10, 2) NOT NULL,
    special_price_quantity INT,
    special_price          DECIMAL(10, 2),
    PRIMARY KEY (product_id),
    UNIQUE (product_hash),
    unique (name)
);

CREATE TABLE cart_item
(
    id         SERIAL NOT NULL,
    quantity   INT    NOT NULL,
    cart_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cart_id) REFERENCES carts (cart_id),
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);

INSERT INTO users (user_hash, password, email, first_name, last_name, created_at)
VALUES ('hash1', '$2a$10$DhjmzBh5ubu8mrkF8sAZ1eocxwB58zZLhGCLbJUA6r0FMSJGtnl1e',
        'user@user.com', 'Sebastian', 'Kantor', CURRENT_TIMESTAMP);

INSERT INTO products (product_hash, name, normal_price, special_price_quantity, special_price)
VALUES ('hash123', 'A', 40.00, 3, 30.00),
       ('hash456', 'B', 10.00, 2, 7.50),
       ('hash789', 'C', 30.00, 4, 20.00),
       ('hash012', 'D', 25.00, 2, 23.50);

CREATE TABLE combined_discounts
(
    combined_discount_id SERIAL PRIMARY KEY,
    product_x_id         BIGINT         NOT NULL,
    product_y_id         BIGINT         NOT NULL,
    discount_amount      DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (product_x_id) REFERENCES products (product_id),
    FOREIGN KEY (product_y_id) REFERENCES products (product_id)
);

INSERT INTO combined_discounts (product_x_id, product_y_id, discount_amount)
VALUES (1, 2, 5.00),
       (1, 3, 10.00),
       (2, 4, 3.00);
