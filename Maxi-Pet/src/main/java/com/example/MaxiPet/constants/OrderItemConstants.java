package com.example.MaxiPet.constants;

public class OrderItemConstants {
    // Error messages
    public static final String ORDER_ITEM_CANNOT_BE_NULL = "Order item cannot be null";
    public static final String QUANTITY_MUST_BE_GREATER_THAN_ZERO = "Quantity must be greater than 0";
    public static final String ORDER_ITEM_NOT_FOUND_WITH_ID = "Order item with id {} was not found in db";
    public static final String PRODUCT_NOT_FOUND_WITH_ID = "Product with id {} was not found in db";

    // Logger messages
    public static final String VALIDATING_ORDER_ITEM_CREATION = "Validating order item creation...";
    public static final String VALIDATING_ORDER_ITEM_UPDATE = "Validating order item update...";
    public static final String ORDER_ITEM_VALIDATION_SUCCESSFUL = "Order item validation successful";
    public static final String VALIDATING_ORDER_ITEM_EXISTS = "Validating if order item exists with id: {}";
}
