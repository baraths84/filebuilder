package com.macys.selection.xapi.list.client.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.List;

@JsonRootName("collaboratorResponse")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaboratorsDTO implements Serializable {

    private static final long serialVersionUID = 224195431469993125L;

    @JsonProperty("collaborators")
    private List<CollaboratorDTO> collaborators;

    public List<CollaboratorDTO> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<CollaboratorDTO> collaborators) {
        this.collaborators = collaborators;
    }
}
