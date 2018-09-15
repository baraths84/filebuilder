package com.macys.selection.xapi.list.exception;

/**
 * 
 **/
public enum ErrorCode {
	BAD_REQUEST(1, "Bad request."),
	INTERNAL_SERVER_ERROR(2, "Internal server error."),
	WISHLIST_SERVICE_ERROR(3,"Internal server error.");

	private int value;
	private final String message;

	ErrorCode(int value, String message) {
		this.value = value;
		this.message = message;
	}

	public int getValue() {
		return value;
	}

	public String getMessage() { return message; }

}
