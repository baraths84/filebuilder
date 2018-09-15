package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * customer wishlist response
 **/
@JsonRootName("list")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteList implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 812917164137352130L;



    @JsonProperty("listGuid")
    private String listGuid;



    @JsonProperty("products")
    private List<FavoriteProduct> products;



    public List<FavoriteProduct> getProducts() {
        return products;
    }

    public void setProducts(List<FavoriteProduct> products) {
        this.products = products;
    }

    public String getListGuid() {
        return listGuid;
    }

    public void setListGuid(String guid) {
        this.listGuid = guid;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.listGuid)
                .append(this.products);
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !this.getClass().equals(obj.getClass())) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        EqualsBuilder builder = new EqualsBuilder();
        appendEquals(builder, obj);
        return builder.isEquals();
    }

    protected void appendEquals(EqualsBuilder builder, Object obj) {
        FavoriteList other = (FavoriteList) obj;
        builder.append(this.getListGuid(), other.getListGuid());
        builder.append(this.getProducts(),other.getProducts());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("listGuid", listGuid)
                .add("products", products).toString();
    }
}
