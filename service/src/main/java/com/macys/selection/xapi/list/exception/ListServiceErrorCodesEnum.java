package com.macys.selection.xapi.list.exception;

public enum ListServiceErrorCodesEnum {
    INVALID_SORT_BY_FIELD(10108),
    INVALID_SORT_BY_ORDER(10109),
    DUPLICATE_WISHLIST_NAME(10134),
    DUPLICATE_LIST_NAME(10156),
    MAX_LIST_PER_USER_REACHED(10147),
    MAX_ITEMS_PER_LIST_REACHED(10148),
    INVALID_USER_ID(10101),
    INVALID_INPUT(10111),
    NO_USER_INFO(10103),
    NO_SECURE_TOKEN(10119),
    CATALOG_LOOKUP_ERROR(10110),
    INVALID_MASTER_PRODUCT(10139),
    INVALID_COLLABORATOR_ID(10151);
    private int internalCode;

    ListServiceErrorCodesEnum(int internalCode) {
        this.internalCode = internalCode;
    }

    public int getInternalCode() {
        return this.internalCode;
    }


}
