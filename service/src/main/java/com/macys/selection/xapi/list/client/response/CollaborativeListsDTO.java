package com.macys.selection.xapi.list.client.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.List;

@JsonRootName("collaborativeLists")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaborativeListsDTO implements Serializable {
    private static final long serialVersionUID = -49058553491156452L;

    @JsonProperty("lists")
    private List<CollaborativeListDTO> collaboratorLists;

    public List<CollaborativeListDTO> getCollaboratorLists() {
        return collaboratorLists;
    }

    public void setCollaboratorLists(List<CollaborativeListDTO> collaboratorLists) {
        this.collaboratorLists = collaboratorLists;
    }
}
