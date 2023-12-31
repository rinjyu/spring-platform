CREATE TABLE INVENTORY (
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

CREATE PROCEDURE SP_INVENTORY_USABLE_QTY_UPD (
    IN P_ITEM_ID VARCHAR(13),
    IN P_UNIT_ITEM_ID VARCHAR(5),
    IN P_MOD_ID VARCHAR(20)
)
LANGUAGE plpgsql
AS $$
BEGIN

    UPDATE INVENTORY
    SET USABLE_QTY = BASE_QTY
        , MOD_DTS = NOW()
        , MOD_ID = P_MOD_ID
    WHERE ITEM_ID = P_ITEM_ID
    AND UNIT_ITEM_ID = P_UNIT_ITEM_ID;

    COMMIT;

END
$$;