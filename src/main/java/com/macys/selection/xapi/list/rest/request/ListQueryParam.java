/*******************************************************************************
 * Copyright (c) 2017 macys.com. All rights reserved.
 *******************************************************************************/
package com.macys.selection.xapi.list.rest.request;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.Set;

/**
 * 
 * @author m785440
 *
 */
public class ListQueryParam {

  @PathParam("listGuid")
  private String listGuid;

  @QueryParam("default")
  private Boolean defaultList;

  @QueryParam("listType")
  private String listType;

  @QueryParam("listTypes")
  private Set<String> listTypes;

  @QueryParam("sortBy")
  private String sortBy;

  @QueryParam("sortOrder")
  private String sortOrder;

  @QueryParam("listLimit")
  private Integer listLimit;

  @QueryParam("filter")
  private String filter;

  @QueryParam("fields")
  private String fields;

  @QueryParam("expand")
  private String expand;

  @QueryParam("upcId")
  private Integer upcId;

  @QueryParam("productId")
  private int productId;

  @QueryParam("firstName")
  private String firstName;

  @QueryParam("lastName")
  private String lastName;

  @QueryParam("state")
  private String state;

  @QueryParam("_customerState")
  private String CustomerState;

  public String getListGuid() {
    return listGuid;
  }

  public void setListGuid(String listGuid) {
    this.listGuid = listGuid;
  }

  public Boolean isDefaultList() {
    return defaultList;
  }

  public void setDefaultList(Boolean defaultList) {
    this.defaultList = defaultList;
  }

  public String getListType() {
    return listType;
  }

  public void setListType(String listType) {
    this.listType = listType;
  }

  public Set<String> getListTypes() {
    return listTypes;
  }

  public void setListTypes(Set<String> listTypes) {
    this.listTypes = listTypes;
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  public String getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  }

  public Integer getListLimit() {
    return listLimit;
  }

  public void setListLimit(Integer listLimit) {
    this.listLimit = listLimit;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public String getFields() {
    return fields;
  }

  public void setFields(String fields) {
    this.fields = fields;
  }

  public String getExpand() {
    return expand;
  }

  public void setExpand(String expand) {
    this.expand = expand;
  }

  public Integer getUpcId() {
    return upcId;
  }

  public void setUpcId(Integer upcId) {
    this.upcId = upcId;
  }

  public int getProductId() { return productId; }

  public void setProductId(int productId) { this.productId = productId; }

  public String getFirstName() { return firstName; }

  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }

  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getState() { return state; }

  public void setState(String state) { this.state = state; }

  public String getCustomerState() {return CustomerState;}

  public void setCustomerState(String CustomerState) {this.CustomerState = CustomerState;}

}
