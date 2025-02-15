CREATE TABLE order_ordered_products
(
    order_id CHAR(36)       NOT NULL,
    id       BIGINT         NOT NULL,
    quantity INT            NOT NULL,
    price    DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (order_id, id),
    FOREIGN KEY (order_id) REFERENCES orders (id)
);
