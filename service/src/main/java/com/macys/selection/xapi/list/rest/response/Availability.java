/*******************************************************************************
 * Copyright (c) 2017 macys.com. All rights reserved.
 *******************************************************************************/
package com.macys.selection.xapi.list.rest.response;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;

/**
 * @author YH03933
 */
@JsonRootName("availability")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"available", "upcAvailabilityMessage", "inStoreEligible", "orderMethod"})
@JsonInclude(Include.NON_NULL)
public class Availability implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 4892784558147996449L;

@JsonProperty("available")
  private Boolean available;

  @JsonProperty("upcAvailabilityMessage")
  private String upcAvailabilityMessage;

  @JsonProperty("inStoreEligible")
  private Boolean inStoreEligible;

  @JsonProperty("orderMethod")
  private String orderMethod;

  public Boolean isAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
  }

  public String getUpcAvailabilityMessage() {
    return upcAvailabilityMessage;
  }

  public void setUpcAvailabilityMessage(String upcAvailabilityMessage) {
    this.upcAvailabilityMessage = upcAvailabilityMessage;
  }

  public Boolean isInStoreEligible() {
    return inStoreEligible;
  }

  public void setInStoreEligible(Boolean inStoreEligible) {
    this.inStoreEligible = inStoreEligible;
  }

  public String getOrderMethod() {
    return orderMethod;
  }

  public void setOrderMethod(String orderMethod) {
    this.orderMethod = orderMethod;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(this.available).append(this.inStoreEligible).append(this.orderMethod)
        .append(this.upcAvailabilityMessage);
    return builder.toHashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !this.getClass().equals(obj.getClass())) {
      return false;
    }
    
    if (obj == this) {
        return true;
    }

    EqualsBuilder builder = new EqualsBuilder();
    appendEquals(builder, obj);
    return builder.isEquals();
  }
  
  protected void appendEquals(EqualsBuilder builder, Object obj) {
    Availability other = (Availability) obj;
    builder.append(this.getOrderMethod(), other.getOrderMethod());
    builder.append(this.getUpcAvailabilityMessage(), other.getUpcAvailabilityMessage());
  }  
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("available", available)
        .add("upcAvailabilityMessage", upcAvailabilityMessage)
        .add("inStoreEligible", inStoreEligible).add("orderMethod", orderMethod).toString();
  }

}
