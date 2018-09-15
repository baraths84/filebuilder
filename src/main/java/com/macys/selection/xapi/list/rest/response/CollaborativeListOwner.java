package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"listGuid", "name", "owner"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaborativeListOwner {

    @JsonProperty("listGuid")
    private String listGuid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("owner")
    private Collaborator owner;

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

    public Collaborator getOwner() {
        return owner;
    }

    public void setOwner(Collaborator owner) {
        this.owner = owner;
    }
}
