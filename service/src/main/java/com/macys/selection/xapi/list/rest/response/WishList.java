/*******************************************************************************
 * Copyright (c) 2017 macys.com. All rights reserved.
 *******************************************************************************/
package com.macys.selection.xapi.list.rest.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.macys.platform.rest.framework.jaxb.Link;
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
 */
@JsonRootName("list")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"id", "listGuid", "name", "listType", "defaultList", "onSaleNotify", "searchable",
	"numberOfItems", "showPurchaseInfo", "createdDate", "lastModified", "imageUrlsList", "items"})
@JsonInclude(Include.NON_NULL)
public class WishList implements Serializable, Comparable<WishList> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8206240820555835270L;

	@JsonProperty("id")
	private Long id;

	@JsonProperty("listGuid")
	private String listGuid;

	@JsonProperty("name")
	private String name;

	@JsonProperty("listType")
	private String listType;

	@JsonProperty("defaultList")
	private Boolean defaultList;

	@JsonProperty("onSaleNotify")
	private Boolean onSaleNotify;

	@JsonProperty("searchable")
	private Boolean searchable;

	@JsonProperty("numberOfItems")
	private Integer numberOfItems;

	@JsonProperty("showPurchaseInfo")
	private Boolean showPurchaseInfo;

	@JsonProperty("createdDate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date createdDate;

	@JsonProperty("lastModified")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date lastModified;

	@JsonProperty("imageUrlsList")
	private List<String> imageUrlsList;

	@JsonProperty("items")
	private List<Item> items;

	@JsonProperty("links")
	private List<Link> links;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getListGuid() {
		return listGuid;
	}

	public void setListGuid(String guid) {
		this.listGuid = guid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public Boolean isDefaultList() {
		return defaultList;
	}

	public void setDefaultList(Boolean defaultList) {
		this.defaultList = defaultList;
	}

	public Boolean isOnSaleNotify() {
		return onSaleNotify;
	}

	public void setOnSaleNotify(Boolean onSaleNotify) {
		this.onSaleNotify = onSaleNotify;
	}

	public Boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(Boolean searchable) {
		this.searchable = searchable;
	}

	public Integer getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(Integer numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public Boolean isShowPurchaseInfo() {
		return showPurchaseInfo;
	}

	public void setShowPurchaseInfo(Boolean showPurchaseInfo) {
		this.showPurchaseInfo = showPurchaseInfo;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public List<String> getImageUrlsList() {
		return imageUrlsList;
	}

	public void setImageUrlsList(List<String> imageUrlsList) {
		this.imageUrlsList = imageUrlsList;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

    @Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(this.id)
		.append(this.defaultList)
		.append(this.listGuid)
		.append(this.listType)
		.append(this.name)
		.append(this.numberOfItems)
		.append(this.onSaleNotify)
		.append(this.searchable)
		.append(this.showPurchaseInfo);    
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
		WishList other = (WishList) obj;
		builder.append(this.getId(), other.getId());
		builder.append(this.getListGuid(), other.getListGuid());
		builder.append(this.getListType(), other.getListType());
		builder.append(this.getName(), other.getName());
		builder.append(this.getNumberOfItems(), other.getNumberOfItems());
		builder.append(this.getOnSaleNotify(), other.getOnSaleNotify());
		builder.append(this.getSearchable(), other.getSearchable());
		builder.append(this.getShowPurchaseInfo(), other.getShowPurchaseInfo());
		builder.append(this.getImageUrlsList(), other.getImageUrlsList());
		builder.append(this.getItems(), other.getItems());    
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("guid", listGuid).add("name", name)
				.add("listType", listType).add("defaultList", defaultList).add("onSaleNotify", onSaleNotify)
				.add("searchable", searchable).add("numberOfItems", numberOfItems)
				.add("showPurchaseInfo", showPurchaseInfo).add("createdDate", createdDate)
				.add("lastModified", lastModified).add("imageUrlsList",  imageUrlsList).add("items", items).toString();
	}

	public Boolean getDefaultList() {
		return defaultList;
	}

	public Boolean getOnSaleNotify() {
		return onSaleNotify;
	}

	public Boolean getSearchable() {
		return searchable;
	}

	public Boolean getShowPurchaseInfo() {
		return showPurchaseInfo;
	}

	public int compareTo(WishList wishlist) {

	   Date inputCreatedDate = wishlist.getCreatedDate();
	   
	   if (inputCreatedDate == null || this.createdDate == null) {
		   return 0;
	   }
	   
	   if (wishlist.isDefaultList() != null && wishlist.isDefaultList()) {
		   return 1;  //default list is always on the top of the list
	   } else {
		   //DESC order
		   return inputCreatedDate.compareTo(this.createdDate);
	   }
	}


}
