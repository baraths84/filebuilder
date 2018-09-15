package com.macys.selection.xapi.list.client.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.User;
import jersey.repackaged.com.google.common.base.MoreObjects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.List;

@JsonRootName(value="list")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"list", "user", "guid", "name", "listType", "defaultList", "onSaleNotify", "searchable", "showPurchaseInfo", "items"})
@JsonInclude(Include.NON_NULL)
public class CustomerListRequest implements Serializable {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	  @JsonProperty("guid")
	  private String guid;
	  
	  @JsonProperty("name")
	  @JsonInclude(Include.NON_NULL)
	  private String name;

	  @JsonProperty("listType")
	  private String listType;

	  @JsonProperty("defaultList")
	  private Boolean defaultList;

	  @JsonProperty("onSaleNotify")
	  private Boolean onSaleNotify;

	  @JsonProperty("searchable")
	  private Boolean searchable;

	  @JsonProperty("showPurchaseInfo")
	  private Boolean showPurchaseInfo;
	  
	  @JsonProperty("user")
	  private User user;

	  @JsonProperty("items")
	  private List<Item> items;

	  public String getGuid() {
	    return guid;
	  }

	  public void setGuid(String guid) {
	    this.guid = guid;
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

	  public Boolean isShowPurchaseInfo() {
	    return showPurchaseInfo;
	  }

	  public void setShowPurchaseInfo(Boolean showPurchaseInfo) {
	    this.showPurchaseInfo = showPurchaseInfo;
	  }

	  public List<Item> getItems() {
	    return items;
	  }

	  public void setItems(List<Item> items) {
	    this.items = items;
	  }
	  
	  @Override
	  public int hashCode() {
	    HashCodeBuilder builder = new HashCodeBuilder();
	    builder.append(this.defaultList)
	    .append(this.guid)
	    .append(this.listType)
	    .append(this.name)
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
	    CustomerListRequest other = (CustomerListRequest) obj;
	    builder.append(this.getGuid(), other.getGuid());
	    builder.append(this.getListType(), other.getListType());
	    builder.append(this.getName(), other.getName());
	    builder.append(this.getOnSaleNotify(), other.getOnSaleNotify());
	    builder.append(this.getSearchable(), other.getSearchable());
	    builder.append(this.getShowPurchaseInfo(), other.getShowPurchaseInfo());
	    builder.append(this.getItems(), other.getItems());    
	  }
	  
	 @Override
	  public String toString() {
	    return MoreObjects.toStringHelper(this).add("guid", guid).add("name", name)
	        .add("listType", listType).add("defaultList", defaultList).add("onSaleNotify", onSaleNotify)
	        .add("searchable", searchable)
	        .add("showPurchaseInfo", showPurchaseInfo)
	        .add("items", items).toString();
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


	  public User getUser() {
		  return user;
	  }

	  public void setUser(User user) {
		  this.user = user;
	  }

}
