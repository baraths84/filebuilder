package com.macys.selection.xapi.list.rest.request.collaborators;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.macys.selection.xapi.list.rest.response.Item;

import java.io.Serializable;
import java.util.List;

@JsonRootName("list")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"userGuid", "name", "onSaleNotify", "items"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaborativeListRequest implements Serializable {

    private static final long serialVersionUID = 7032163062567816930L;

    @JsonProperty("userGuid")
    private String userGuid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("onSaleNotify")
    private Boolean onSaleNotify;

    @JsonProperty("items")
    private List<Item> items;

/*    @JsonProperty("collaborators")
    private List<Collaborator> collaborators;*/

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOnSaleNotify() {
        return onSaleNotify;
    }

    public void setOnSaleNotify(Boolean onSaleNotify) {
        this.onSaleNotify = onSaleNotify;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
