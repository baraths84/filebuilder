package com.macys.selection.xapi.list.client.response;

import java.util.List;

public class CollaborativeRequestDTO {
    private String listGuid;
    private String name;
    private String requestPrivilege;
    private List<CollaboratorDTO> collaborators;

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

    public String getRequestPrivilege() {
        return requestPrivilege;
    }

    public void setRequestPrivilege(String requestPrivilege) {
        this.requestPrivilege = requestPrivilege;
    }


    public List<CollaboratorDTO> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<CollaboratorDTO> collaborators) {
        this.collaborators = collaborators;
    }
}
