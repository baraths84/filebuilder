package com.macys.selection.xapi.list.util;

public enum CustomerQueryParameterEnum {
	USERID("userid"),

	USERGUID("userguid"),

	LISTGUID("listguid"),

	DEFAULT("default"),

	LISTTYPE("listtype"),

	OFFSET("_offset"),

	LIMIT("_limit"),

	SORTBY("_sortby"),

	SORTORDER("_sortorder"),

	LISTLIMIT("listLimit"),

	FILTER("_filter"),

	FIELDS("_fields"),

	EXPAND("_expand"),

	FIRSTNAME("firstName"),

	LASTNAME("lastName"),

	STATE("state"),

	CUSTOMERSTATE("_customerState"),

	EMAIL("emailAddress"),

	PHONE("phoneNumber"),

	PRODUCTID("productId");

	private String paramName;

	CustomerQueryParameterEnum(String paramName) {
    this.paramName = paramName;
  }

	public String getParamName() {
		return paramName;
	}

}
