package com.macys.selection.xapi.list.client.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("requestsResponse")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaborativeRequestsDTO {

    @JsonProperty("list")
    private List<CollaborativeRequestDTO> requests;

    public List<CollaborativeRequestDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<CollaborativeRequestDTO> requests) {
        this.requests = requests;
    }
}
