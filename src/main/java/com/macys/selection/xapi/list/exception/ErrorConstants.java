package com.macys.selection.xapi.list.exception;

public class ErrorConstants {
    public static final String BAD_JASON_INPUT_ITMES_NOT_AVAILABLE = "Bad Json from UI, at least one item is required!";
    public static final String BAD_JSON_INPUT_BODY_USER_IS_NULL = "User cannot be null when trying to delete the list, specify the user info in the payload.";
    public static final String USERID_IS_NULL = "user id can't be null when deleting a list by listGuid.";
    public static final String INVALID_INPUT = "Incorrect request";

    private ErrorConstants(){
        throw new IllegalStateException();
    }

}
