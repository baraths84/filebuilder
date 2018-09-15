package com.macys.selection.xapi.list.client.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("lists")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListsDTO {

    @JsonProperty("lists")
    private List<ListDTO> lists;

    public List<ListDTO> getLists() {
        return lists;
    }

    public void setLists(List<ListDTO> lists) {
        this.lists = lists;
    }

}
