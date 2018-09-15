package com.macys.selection.xapi.list.common;

public enum ListTypeEnum {

    WISH_LIST("W", "wishList"),
    COLLABORATIVE_LIST("C", "collaborativeList");

    private String value;

    private String text;

    ListTypeEnum(String value, String text){
        this.value=value;
        this.text=text;
    }

    public String getValue(){
        return this.value;
    }
    public String getText(){
        return this.text;
    }

}
