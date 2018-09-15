package com.macys.selection.xapi.list.rest.request.collaborators;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("collaboratorPrivilege")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"currentUserGuid", "collaborator"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaboratorPrivilegeRequest {

    private String currentUserGuid;

    private Collaborator collaborator;

    public String getCurrentUserGuid() {
        return currentUserGuid;
    }

    public void setCurrentUserGuid(String currentUserGuid) {
        this.currentUserGuid = currentUserGuid;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    public static class Collaborator {
        private String userGuid;

        private String privilege;

        public String getUserGuid() {
            return userGuid;
        }

        public void setUserGuid(String userGuid) {
            this.userGuid = userGuid;
        }

        public String getPrivilege() {
            return privilege;
        }

        public void setPrivilege(String privilege) {
            this.privilege = privilege;
        }
    }
}
