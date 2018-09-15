package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("approvalsResponse")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaboratorApprovalResponse {

    @JsonProperty("lists")
    private List<CollaborativeList> list;

    public List<CollaborativeList> getList() {
        return list;
    }

    public void setList(List<CollaborativeList> list) {
        this.list = list;
    }
}
