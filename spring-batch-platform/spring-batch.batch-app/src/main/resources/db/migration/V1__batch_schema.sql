CREATE TABLE IF NOT EXISTS INVENTORY (
    ITEM_ID VARCHAR(13) NOT NULL,
    UNIT_ITEM_ID VARCHAR(5) NOT NULL,
    BASE_QTY NUMERIC(10) NOT NULL,
    USABLE_QTY NUMERIC(10) NOT NULL,
    REG_DTS TIMESTAMP NOT NULL,
    REG_ID VARCHAR(20) NOT NULL,
    MOD_DTS TIMESTAMP NOT NULL,
    MOD_ID VARCHAR(20) NOT NULL,
    PRIMARY KEY (ITEM_ID, UNIT_ITEM_ID)
);