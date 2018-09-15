package com.macys.selection.xapi.list.exception;


public class RestException extends RuntimeException {
  private static final long serialVersionUID = 2983104055871045690L;

  public RestException(String msg, Throwable throwable) {
    super(msg, throwable);
  }
  
  public RestException(String msg) {
	    super(msg);
	  }
  
}
