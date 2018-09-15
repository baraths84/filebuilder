package com.macys.selection.xapi.list.util;

public enum LinkTypeEnum {
	SELF("self"),
	USER("user"),
	ITEMS("items");
	private String value;
	
	LinkTypeEnum(String value){
		this.value=value;
	}
	
	public String getValue(){
		return this.value;
	}
}
