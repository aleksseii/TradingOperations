-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE organization(
    org_id          SERIAL          NOT NULL,
    inn             BIGINT          NOT NULL UNIQUE,
    name            VARCHAR(50)     NOT NULL,
    bank_account    VARCHAR(50)     NOT NULL,
    
    CONSTRAINT organization_pk PRIMARY KEY(org_id)
);

CREATE TABLE waybill(
    waybill_id       SERIAL    NOT NULL,
    waybill_date     DATE      NOT NULL,
    org_sender_id    INT       NOT NULL
        REFERENCES organization(org_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        
    CONSTRAINT waybill_pk PRIMARY KEY(waybill_id)
);

CREATE TABLE product(
    product_id       SERIAL          NOT NULL,
    name             VARCHAR(50)     NOT NULL,
    internal_code    VARCHAR(50)     NOT NULL,
    
    CONSTRAINT product_pk PRIMARY KEY(product_id)
);

CREATE TABLE waybill_article(
    waybill_article_id    SERIAL    NOT NULL,
    price                 BIGINT    NOT NULL
        CONSTRAINT positive_price  CHECK (price > 0),
    amount                INT       NOT NULL DEFAULT 1
        CONSTRAINT positive_amount CHECK (amount > 0),
    waybill_id            INT       NOT NULL
        REFERENCES waybill(waybill_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    product_id            INT       
        REFERENCES product(product_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    
    CONSTRAINT waybill_article_pk PRIMARY KEY(waybill_article_id)
);
