/*******************************************************************************
 * Copyright (c) 2017 macys.com. All rights reserved.
 *******************************************************************************/
package com.macys.selection.xapi.list.rest.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@JsonRootName("upc")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"id", "upcNumber", "color", "size", "price", "availability", "product", "finalPrice"})
@JsonInclude(Include.NON_NULL)
public class Upc implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1550186320640815469L;

@JsonProperty("id")
  private Integer id;

  @JsonProperty("upcNumber")
  private Long upcNumber;

  @JsonProperty("color")
  private String color;

  @JsonProperty("size")
  private String size;

  @JsonProperty("price")
  private Price price;

  @JsonProperty("availability")
  private Availability availability;

  @JsonProperty("product")
  private Product product;

  @JsonProperty("type")
  private String type;
  
  @JsonProperty("finalPrice")
  private FinalPrice finalPrice;

  public FinalPrice getFinalPrice() {
	return finalPrice;
  }

  public void setFinalPrice(FinalPrice finalPrice) {
	this.finalPrice = finalPrice;
  }

  @JsonIgnore
  private String upcPrimaryImageName;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Long getUpcNumber() {
    return upcNumber;
  }

  public void setUpcNumber(Long upcNumber) {
    this.upcNumber = upcNumber;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public Price getPrice() {
    return price;
  }

  public void setPrice(Price price) {
    this.price = price;
  }

  public Availability getAvailability() {
    return availability;
  }

  public void setAvailability(Availability availability) {
    this.availability = availability;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public String getType() {
        return type;
  }

  public void setType(String type) {
        this.type = type;
  }

  public String getUpcPrimaryImageName() {
    return upcPrimaryImageName;
  }

  public void setUpcPrimaryImageName(String upcPrimaryImageName) {
    this.upcPrimaryImageName = upcPrimaryImageName;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(this.availability)
    .append(this.color)
    .append(this.id)
    .append(this.price)
    .append(this.upcNumber)
    .append(this.product);
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
    Upc other = (Upc) obj;
    builder.append(this.getAvailability(), other.getAvailability());
    builder.append(this.getColor(), other.getColor());
    builder.append(this.getId(), other.getId());
    builder.append(this.getPrice(), other.getPrice());
    builder.append(this.getProduct(), other.getProduct());
    builder.append(this.getSize(), other.getSize());
    builder.append(this.getUpcNumber(), other.getUpcNumber());
    builder.append(this.getType(), other.getType());
  }
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("upcNumber", upcNumber)
        .add("color", color).add("size", size).add("price", price).add("availability", availability)
        .add("product", product).add("type", type).add("finalPrice", finalPrice).toString();
  }


}
