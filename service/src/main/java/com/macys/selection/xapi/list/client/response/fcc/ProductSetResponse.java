package com.macys.selection.xapi.list.client.response.fcc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

@JsonRootName("products")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"product"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductSetResponse {

    private List<ProductResponse> product;

    public List<ProductResponse> getProduct() {
        return product;
    }

    public void setProduct(List<ProductResponse> product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("product", product)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductSetResponse that = (ProductSetResponse) o;

        return new EqualsBuilder()
                .append(product, that.product)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(product)
                .toHashCode();
    }
}
