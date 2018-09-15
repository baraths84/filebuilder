package com.macys.selection.xapi.list.util;

public enum ListQueryParameterEnum {
    USER_ID("userId"),

    USER_IDS("userIds"),

    USER_GUID("userGuid"),

    USER_GUIDS("userGuids"),

    VIEWER_GUID("viewerGuid"),

    GUEST_USER("guestUser"),

    USER_FIRST_NAME("userFirstName"),

    LIST_GUID("listGuid"),

    LIST_GUIDS("listGuids"),

    DEFAULT("default"),

    LIST_TYPE("listType"),

    LIST_TYPES("listTypes"),

    OFFSET("_offset"),

    LIMIT("_limit"),

    LIST_LIMIT("listLimit"),

    COLLAB_LIMIT("collabLimit"),

    FILTER("_filter"),

    FIELDS("_fields"),

    EXPAND("_expand"),

    UPC_ID("upcId"),

    PRODUCT_ID("productId"),

    CUSTOMERSTATE("customerState"),

    ON_SALE_NOTIFY("onSaleNotify"),

    SORT_ORDER("sortOrder"),

    SORT_BY("sortBy"),

    PRIVILEGE("privilege");

    private String paramName;

    ListQueryParameterEnum(String paramName) {
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
