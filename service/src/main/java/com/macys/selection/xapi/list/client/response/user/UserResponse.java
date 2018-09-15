package com.macys.selection.xapi.list.client.response.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonRootName("user")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserResponse {
    private Long id;
    private String guid;
    private boolean guestUser;
    @JsonProperty("profileAddress")
    private ProfileResponse profile;
    private LoginCredentialsResponse loginCredentials;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public boolean isGuestUser() {
        return guestUser;
    }

    public void setGuestUser(boolean guestUser) {
        this.guestUser = guestUser;
    }

    public ProfileResponse getProfile() {
        return profile;
    }

    public void setProfile(ProfileResponse profile) {
        this.profile = profile;
    }

    public LoginCredentialsResponse getLoginCredentials() {
        return loginCredentials;
    }

    public void setLoginCredentials(LoginCredentialsResponse loginCredentials) {
        this.loginCredentials = loginCredentials;
    }
}
