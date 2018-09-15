package com.macys.selection.xapi.list.client.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.List;

@JsonRootName("lists")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(Include.NON_NULL)
public class WishListsDTO implements Serializable  {

	private static final long serialVersionUID = -7431584512623764565L;

	@JsonProperty("lists")
	private List<WishListDTO> lists;

	public List<WishListDTO> getLists() {
		return lists;
	}

	public void setLists(List<WishListDTO> lists) {
		this.lists = lists;
	}
}