package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("listsPresentation")
public class ListsResponse {

    @JsonProperty("lists")
    private List<ListSimple> list;

    public List<ListSimple> getList() {
        return list;
    }

    public void setList(List<ListSimple> list) {
        this.list = list;
    }
}
