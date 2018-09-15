package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("collaborativeListDetails")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaborativeListDetails {
    @JsonProperty("viewer")
    private UserProfile viewer;
    @JsonProperty("owner")
    private UserProfile owner;
    @JsonProperty("listGuid")
    private String listGuid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("numberOfItems")
    private Integer numberOfItems;
    @JsonProperty("numberOfCollaborators")
    private Integer numberOfCollaborators;
    @JsonProperty("imageUrlsList")
    private List<String> imageUrlsList;
    @JsonProperty("items")
    private List<CollaborativeItem> items;
    @JsonProperty("collaborators")
    private List<Collaborator> collaborators;
    @JsonProperty("recentActivity")
    private ActivityLog recentActivity;

    public UserProfile getViewer() {
        return viewer;
    }

    public void setViewer(UserProfile viewer) {
        this.viewer = viewer;
    }

    public UserProfile getOwner() {
        return owner;
    }

    public void setOwner(UserProfile owner) {
        this.owner = owner;
    }

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

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public Integer getNumberOfCollaborators() {
        return numberOfCollaborators;
    }

    public void setNumberOfCollaborators(Integer numberOfCollaborators) {
        this.numberOfCollaborators = numberOfCollaborators;
    }

    public List<String> getImageUrlsList() {
        return imageUrlsList;
    }

    public void setImageUrlsList(List<String> imageUrlsList) {
        this.imageUrlsList = imageUrlsList;
    }

    public List<CollaborativeItem> getItems() {
        return items;
    }

    public void setItems(List<CollaborativeItem> items) {
        this.items = items;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public ActivityLog getRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(ActivityLog recentActivity) {
        this.recentActivity = recentActivity;
    }
}
