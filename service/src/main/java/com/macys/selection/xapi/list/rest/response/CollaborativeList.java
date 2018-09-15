package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonRootName("collaborativeList")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaborativeList implements Serializable {

    private static final long serialVersionUID = -216651381560659977L;

    @JsonProperty("listGuid")
    private String listGuid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("listType")
    private String listType;

    @JsonProperty("userGuid")
    private String userGuid;

    @JsonProperty("numberOfItems")
    private Integer numberOfItems;

    @JsonProperty("createdDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createdDate;

    @JsonProperty("lastModified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date lastModified;

    @JsonProperty("onSaleNotify")
    private Boolean onSaleNotify;

    @JsonProperty("imageUrlsList")
    private List<String> imageUrlsList;

    @JsonProperty("items")
    private List<Item> items;

    @JsonProperty("collaborators")
    private List<Collaborator> collaborators;

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

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getOnSaleNotify() {
        return onSaleNotify;
    }

    public void setOnSaleNotify(Boolean onSaleNotify) {
        this.onSaleNotify = onSaleNotify;
    }

    public List<String> getImageUrlsList() {
        return imageUrlsList;
    }

    public void setImageUrlsList(List<String> imageUrlsList) {
        this.imageUrlsList = imageUrlsList;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }
}
