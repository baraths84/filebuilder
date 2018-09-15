package com.macys.selection.xapi.list.client.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.macys.selection.xapi.list.rest.response.WishList;

@JsonRootName("lists")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(Include.NON_NULL)
public class CustomerWishListsResponse implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7022338327369911574L;
	
	@JsonProperty("lists")
	private List<WishList> lists;

	public List<WishList> getLists() {
		return lists;
	}

	public void setLists(List<WishList> lists) {
		this.lists = lists;
	}
}