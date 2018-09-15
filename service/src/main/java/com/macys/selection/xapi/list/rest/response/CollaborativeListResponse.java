package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class CollaborativeListResponse implements Serializable {

    private static final long serialVersionUID = -250979355163358174L;

    @JsonProperty("user")
    private User user;

    @JsonProperty("list")
    private WishList wishlist;

    @JsonProperty("collaborators")
    private List<Collaborator> collaborators;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WishList getWishlist() {
        return wishlist;
    }

    public void setWishlist(WishList wishlist) {
        this.wishlist = wishlist;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

}
