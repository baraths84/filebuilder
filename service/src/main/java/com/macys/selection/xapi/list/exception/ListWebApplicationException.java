package com.macys.selection.xapi.list.exception;

import javax.ws.rs.WebApplicationException;

public class ListWebApplicationException extends WebApplicationException {

    public ListWebApplicationException(String message, int status) {
        super(message, status);
    }
}
