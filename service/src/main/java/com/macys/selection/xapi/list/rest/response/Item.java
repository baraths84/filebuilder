/*******************************************************************************
 * Copyright (c) 2017 macys.com. All rights reserved.
 *******************************************************************************/
package com.macys.selection.xapi.list.rest.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@JsonRootName("item")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"guid", "addedDate", "lastModified", "retailPriceWhenAdded",
    "retailPriceDropAfterAddedToList", "retailPriceDropPercentage", "qtyRequested",
    "qtyStillNeeded", "priority", "upc", "product", "promotions"})
@JsonInclude(Include.NON_NULL)
public class Item implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 6050752886431409820L;

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("itemGuid")
  private String itemGuid;

  @JsonProperty("retailPriceWhenAdded")
  private Double retailPriceWhenAdded;

  @JsonProperty("retailPriceDropAfterAddedToList")
  private Double retailPriceDropAfterAddedToList;

  @JsonProperty("retailPriceDropPercentage")
  private Integer retailPriceDropPercentage;

  @JsonProperty("qtyRequested")
  private Integer qtyRequested;

  @JsonProperty("qtyStillNeeded")
  private Integer qtyStillNeeded;

  @JsonProperty("priority")
  private String priority;

  @JsonProperty("upc")
  private Upc upc;

  @JsonProperty("product")
  private Product product;
  
  @JsonProperty("promotions")
  private List<Promotion> promotions;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  @JsonProperty("addedDate")
  private Date addedDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
  @JsonProperty("lastModified")
  private Date lastModified;

  @JsonIgnore
  private Boolean upcLevelItem;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getItemGuid() {
    return itemGuid;
  }

  public void setItemGuid(String itemGuid) {
    this.itemGuid = itemGuid;
  }

  public Date getAddedDate() {
    return addedDate;
  }

  public void setAddedDate(Date addedDate) {
    this.addedDate = addedDate;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  public Double getRetailPriceWhenAdded() {
    return retailPriceWhenAdded;
  }

  public void setRetailPriceWhenAdded(Double retailPriceWhenAdded) {
    this.retailPriceWhenAdded = retailPriceWhenAdded;
  }

  public Double getRetailPriceDropAfterAddedToList() {
    return retailPriceDropAfterAddedToList;
  }

  public void setRetailPriceDropAfterAddedToList(Double retailPriceDropAfterAddedToList) {
    this.retailPriceDropAfterAddedToList = retailPriceDropAfterAddedToList;
  }

  public Integer getRetailPriceDropPercentage() {
    return retailPriceDropPercentage;
  }

  public void setRetailPriceDropPercentage(Integer retailPriceDropPercentage) {
    this.retailPriceDropPercentage = retailPriceDropPercentage;
  }

  public Integer getQtyRequested() {
    return qtyRequested;
  }

  public void setQtyRequested(Integer qtyRequested) {
    this.qtyRequested = qtyRequested;
  }

  public Integer getQtyStillNeeded() {
    return qtyStillNeeded;
  }

  public void setQtyStillNeeded(Integer qtyStillNeeded) {
    this.qtyStillNeeded = qtyStillNeeded;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public Upc getUpc() {
    return upc;
  }

  public void setUpc(Upc upc) {
    this.upc = upc;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public List<Promotion> getPromotions() {
	return promotions;
  }

  public void setPromotions(List<Promotion> promotions) {
	this.promotions = promotions;
  }

  public Boolean isUpcLevelItem() {
    return upcLevelItem;
  }

  public void setUpcLevelItem(Boolean upcLevelItem) {
    this.upcLevelItem = upcLevelItem;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(this.qtyRequested)
    .append(this.qtyStillNeeded)
    .append(this.retailPriceDropAfterAddedToList)
    .append(this.retailPriceDropPercentage)
    .append(this.retailPriceWhenAdded)
    .append(this.itemGuid)
    .append(this.upc)
    .append(this.product)
    .append(this.promotions);    
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
    Item other = (Item) obj;
    builder.append(this.getQtyRequested(), other.getQtyRequested());
    builder.append(this.getQtyStillNeeded(), other.getQtyStillNeeded());
    builder.append(this.getRetailPriceDropAfterAddedToList(), other.getRetailPriceDropAfterAddedToList());
    builder.append(this.getRetailPriceDropPercentage(), other.getRetailPriceDropPercentage());
    builder.append(this.getRetailPriceWhenAdded(), other.getRetailPriceWhenAdded());
    builder.append(this.getItemGuid(), other.getItemGuid());
    builder.append(this.getUpc(), other.getUpc());
    builder.append(this.getProduct(), other.getProduct());
    builder.append(this.getPromotions(), other.getPromotions());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("guid", itemGuid).add("addedDate", addedDate)
        .add("lastModified", lastModified).add("retailPriceWhenAdded", retailPriceWhenAdded)
        .add("retailPriceDropAfterAddedToList", retailPriceDropAfterAddedToList)
        .add("retailPriceDropPercentage", retailPriceDropPercentage)
        .add("qtyRequested", qtyRequested).add("qtyStillNeeded", qtyStillNeeded).add("upc", upc)
        .add("product", product).add("promotions", promotions).toString();
  }

}
