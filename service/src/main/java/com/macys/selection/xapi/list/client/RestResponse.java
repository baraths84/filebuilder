package com.macys.selection.xapi.list.client;

import java.io.Serializable;

/**
 * Internal representation of rest Response
 **/
public class RestResponse implements Serializable {
  private static final long serialVersionUID = 5123354024429553380L;
  private int statusCode;
  private String body;
  private String location;
  
  public int getStatusCode() {
    return statusCode;
  }
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }
  public String getBody() {
    return body;
  }
  public void setBody(String body) {
    this.body = body;
  }
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }

}
