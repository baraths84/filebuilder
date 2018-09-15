package com.macys.selection.xapi.list.rest.request;

import javax.ws.rs.QueryParam;

public class SortQueryParam {
    @QueryParam("sortBy")
    private String sortBy;

    @QueryParam("sortOrder")
    private String sortOrder;

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
