package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@JsonRootName("listsPresentation")
@Validated
public class ListsPresentation implements Serializable {

    private static final long serialVersionUID = 7180787963857628751L;

    @JsonProperty("lists")
    private Map<String, Collection<?>> lists;

    public Map<String, Collection<?>> getLists() {
        return lists;
    }

    public void setLists(Map<String, Collection<?>> lists) {
        this.lists = lists;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("lists", lists)
        .toString();
    }

}

