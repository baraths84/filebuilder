package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.macys.selection.xapi.list.common.CollaboratorPrivilegeEnum;

import java.util.List;
import java.util.Map;

@JsonRootName("requestsResponse")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaborativeRequestResponse {

    @JsonProperty("lists")
    private Map<CollaboratorPrivilegeEnum, List<CollaborativeListOwner>> requests;

    public Map<CollaboratorPrivilegeEnum, List<CollaborativeListOwner>> getRequests() {
        return requests;
    }

    public void setRequests(Map<CollaboratorPrivilegeEnum, List<CollaborativeListOwner>> requests) {
        this.requests = requests;
    }
}
