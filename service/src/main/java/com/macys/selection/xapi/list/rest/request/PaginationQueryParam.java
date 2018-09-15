/*******************************************************************************
 * Copyright (c) 2017 macys.com. All rights reserved.
 *******************************************************************************/
package com.macys.selection.xapi.list.rest.request;

import javax.ws.rs.QueryParam;

/**
 * 
 * @author m785440
 *
 */
public class PaginationQueryParam {

  @QueryParam("offset")
  private Integer offset;

  @QueryParam("limit")
  private Integer limit;

  public PaginationQueryParam() {

  }

  public PaginationQueryParam(Integer offset, Integer limit) {
    this.offset = offset;
    this.limit = limit;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

}
