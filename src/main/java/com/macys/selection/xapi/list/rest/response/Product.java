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
 *
 */
@JsonRootName("product")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"id", "name", "active", "primaryImage", "imageURL", "phoneOnly", "clickToCall", "live",
    "available", "price", "suppressReviews", "reviewStatistics", "images", "promotions",
    "multipleUpc", "finalPrice"})
@JsonInclude(Include.NON_NULL)
public class Product implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 7889280161054709636L;

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("active")
  private Boolean active;

  @JsonProperty("primaryImage")
  private String primaryImage;
  
  @JsonProperty("imageURL")
  private String imageURL;

  @JsonProperty("phoneOnly")
  private String phoneOnly;

  @JsonProperty("clickToCall")
  private String clickToCall;

  @JsonProperty("live")
  private Boolean live;

  @JsonProperty("available")
  private Boolean available;

  @JsonProperty("price")
  private Price price;

  @JsonProperty("supressReviews")
  private Boolean suppressReviews;

  @JsonProperty("reviewStatistics")
  private ReviewStatistics reviewStatistics;

  @JsonProperty("multipleUpc")
  private Boolean multipleUpc;
  
  @JsonProperty("finalPrice")
  private FinalPrice finalPrice;

  public FinalPrice getFinalPrice() {
	return finalPrice;
  }

  public void setFinalPrice(FinalPrice finalPrice) {
	this.finalPrice = finalPrice;
  }

	public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getPrimaryImage() {
    return primaryImage;
  }

  public void setPrimaryImage(String primaryImage) {
    this.primaryImage = primaryImage;
  }

  public String getPhoneOnly() {
    return phoneOnly;
  }

  public void setPhoneOnly(String phoneOnly) {
    this.phoneOnly = phoneOnly;
  }

  public String getClickToCall() {
    return clickToCall;
  }

  public void setClickToCall(String clickToCall) {
    this.clickToCall = clickToCall;
  }

  public Boolean isLive() {
    return live;
  }

  public void setLive(Boolean live) {
    this.live = live;
  }

  public Boolean isAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
  }

  public Boolean isSuppressReviews() {
    return suppressReviews;
  }

  public void setSuppressReviews(Boolean suppressReviews) {
    this.suppressReviews = suppressReviews;
  }

  public ReviewStatistics getReviewStatistics() {
    return reviewStatistics;
  }

  public void setReviewStatistics(ReviewStatistics reviewStatistics) {
    this.reviewStatistics = reviewStatistics;
  }

  public Boolean isMultipleUpc() {
    return multipleUpc;
  }

  public void setMultipleUpc(Boolean multipleUpc) {
    this.multipleUpc = multipleUpc;
  }

  public Price getPrice() {
    return price;
  }

  public void setPrice(Price price) {
    this.price = price;
  }
  
  public String getImageURL() {
	    return imageURL;
	  }

  public void setImageURL(String imageURL) {
	    this.imageURL = imageURL;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(this.id)
    .append(this.active )
    .append(this.available)
    .append(this.primaryImage)
    .append(this.imageURL)
    .append(this.live)
    .append(this.multipleUpc)
    .append(this.name)
    .append(this.phoneOnly)
    .append(this.price)
    .append(this.primaryImage)
    .append(this.reviewStatistics);    
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
    Product other = (Product) obj;
    builder.append(this.getId(), other.getId());
    builder.append(this.getName(), other.getName());
    builder.append(this.getPrice(), other.getPrice());
    builder.append(this.getPrimaryImage(), other.getPrimaryImage());
    builder.append(this.getReviewStatistics(), other.getReviewStatistics());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("active", active)
        .add("primaryImage", primaryImage).add("imageURL", imageURL).add("phoneOnly", phoneOnly)
        .add("clickToCall", clickToCall).add("live", live).add("available", available)
        .add("suppressReviews", suppressReviews).add("reviewStatistics", reviewStatistics)
        .add("multipleUpc", multipleUpc).add("price", price).add("finalPrice", finalPrice).toString();
  }


}
