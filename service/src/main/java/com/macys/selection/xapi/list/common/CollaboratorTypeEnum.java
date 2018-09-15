package com.macys.selection.xapi.list.common;

public enum CollaboratorTypeEnum {
    LIST_OWNER("listOwner"),
    LIST_COLLABORATOR("listCollaborator");

    private String value;

    CollaboratorTypeEnum(String value){
        this.value=value;
    }

    public String getValue(){
        return this.value;
    }
}
