package com.macys.selection.xapi.list.client.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CollaborativeListDTO implements Serializable {
    private static final long serialVersionUID = -6453522557712775197L;

    private String listGuid;
    private String name;
    private String listType;
    private String userGuid;
    private Integer numberOfItems;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date lastModified;
    private Boolean onSaleNotify;
    private List<ItemDTO> items;
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

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
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

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public List<CollaboratorDTO> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<CollaboratorDTO> collaborators) {
        this.collaborators = collaborators;
    }
}
