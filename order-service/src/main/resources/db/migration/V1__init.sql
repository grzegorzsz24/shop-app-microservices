CREATE TABLE orders
(
    id           char(36) NOT NULL,
    order_number varchar(255) DEFAULT NULL,
    sku_code     varchar(255),
    price        decimal(19, 2),
    quantity     int(11),
    PRIMARY KEY (id)
);