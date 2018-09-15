package com.macys.selection.xapi.list.client.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

@JsonRootName("list")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListDTO implements Serializable {

    private static final long serialVersionUID = -3252904522856726390L;

    private String listGuid;
    private String name;
    private String listType;

    public String getListGuid() {
        return listGuid;
    }

    public void setListGuid(String listGuid) {
        this.listGuid = listGuid;
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
}
