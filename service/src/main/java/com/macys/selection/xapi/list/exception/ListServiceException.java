package com.macys.selection.xapi.list.exception;

/**
 *  Wishlist service exception class to identify that exception occured with-in this service. 
 **/
public class ListServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;


	private int statusCode;
	private int serviceErrorCode;
	private String serviceError;
	private String message;
	
	public ListServiceException() {
		super();
	}
	
	public ListServiceException(String msg) {
		super(msg);
	}

	public ListServiceException(int statusCode, String msg) {
		super(msg);
		this.statusCode = statusCode;
		this.serviceError = msg;
	}

	public ListServiceException(int statusCode, ListServiceErrorCodesEnum serviceErrorCode) {
		this.statusCode = statusCode;
		this.serviceErrorCode = serviceErrorCode.getInternalCode();
	}

	public ListServiceException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public static ListServiceException buildExceptionWithStatusAndMessage(int statusCode, String message) {
		ListServiceException ex = new ListServiceException(message);
		ex.setMessage(message);
		ex.setStatusCode(statusCode);
		return  ex;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getServiceErrorCode() {
		return serviceErrorCode;
	}

	public void setServiceErrorCode(int serviceErrorCode) {
		this.serviceErrorCode = serviceErrorCode;
	}

	public String getServiceError() {
		return serviceError;
	}

	public void setServiceError(String serviceError) {
		this.serviceError = serviceError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
