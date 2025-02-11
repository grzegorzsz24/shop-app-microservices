CREATE TABLE orders
(
    id              CHAR(36) NOT NULL,
    orderer_email   VARCHAR(255) NOT NULL,
    order_date      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status          VARCHAR(255) NOT NULL,
    price           DECIMAL(19, 2),
    PRIMARY KEY (id)
);