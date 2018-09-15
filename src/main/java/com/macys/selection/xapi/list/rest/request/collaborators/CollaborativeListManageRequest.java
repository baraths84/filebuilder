package com.macys.selection.xapi.list.rest.request.collaborators;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

@JsonRootName("list")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaborativeListManageRequest implements Serializable {

    private static final long serialVersionUID = -4153376568973583595L;

    @JsonProperty("name")
    private String name;

    @JsonProperty("onSaleNotify")
    private Boolean onSaleNotify;

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
}
