CREATE TABLE payments
(
    id                  CHAR(36)                    NOT NULL,
    payment_session_id  VARCHAR(255)                NOT NULL,
    status              VARCHAR(255)                NOT NULL,
    creation_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_update_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    order_id            CHAR(36)                    NOT NULL,
    amount              NUMERIC(19, 2)              NOT NULL,
    currency            CHAR(3)                     NOT NULL,
    CONSTRAINT pk_payments PRIMARY KEY (id),
    CONSTRAINT fk_payments_orders FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT chk_amount_non_negative CHECK (amount >= 0),
    CONSTRAINT chk_currency_length CHECK (char_length(currency) = 3)
);

CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_order ON payments(order_id);
