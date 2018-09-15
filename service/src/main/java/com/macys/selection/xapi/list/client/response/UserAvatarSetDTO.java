package com.macys.selection.xapi.list.client.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("profilePictures")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAvatarSetDTO {
    private List<UserAvatarDTO> profilePicture;

    public List<UserAvatarDTO> getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(List<UserAvatarDTO> profilePicture) {
        this.profilePicture = profilePicture;
    }
}
