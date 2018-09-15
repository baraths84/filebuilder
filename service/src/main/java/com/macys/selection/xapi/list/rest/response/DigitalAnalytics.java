package com.macys.selection.xapi.list.rest.response;

import java.io.Serializable;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.MoreObjects;

@JsonRootName("data")
@JsonPropertyOrder({ "productId", "productName", "productPrice", "productPricingState", "productQuantity", "productUPC", "wishListId" })
@JsonInclude(Include.NON_NULL)
public class DigitalAnalytics implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7970279968074751285L;

    /**
     *
     */

    @JsonProperty("productId")
    private List<String> productId;

    @JsonProperty("productName")
    private List<String> productName;

    @JsonProperty("productPrice")
    private List<String> productPrice;

    @JsonProperty("productPricingState")
    private List<String> productPricingState;

    @JsonProperty("productQuantity")
    private List<String> productQuantity;

    @JsonProperty("productUPC")
    private List<String> productUPC;

    @JsonProperty("wishListId")
    private String wishListId;

    public List<String> getProductId() {
        return productId;
    }

    public void setProductId(List<String> productId) {
        this.productId = productId;
    }

    public List<String> getProductName() {
        return productName;
    }

    public void setProductName(List<String> productName) {
        this.productName = productName;
    }

    public List<String> getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(List<String> productPrice) {
        this.productPrice = productPrice;
    }

    public List<String> getProductPricingState() {
        return productPricingState;
    }

    public void setProductPricingState(List<String> productPricingState) {
        this.productPricingState = productPricingState;
    }

    public List<String> getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(List<String> productQuantity) {
        this.productQuantity = productQuantity;
    }

    public List<String> getProductUPC() {
        return productUPC;
    }

    public void setProductUPC(List<String> productUPC) {
        this.productUPC = productUPC;
    }

    public String getWishListId() {
        return wishListId;
    }

    public void setWishListId(String wishListId) {
        this.wishListId = wishListId;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.productId).append(this.productName).append(this.productPrice).append(this.productPricingState).append(this.productQuantity)
                .append(this.productUPC).append(this.wishListId);
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
        DigitalAnalytics other = (DigitalAnalytics) obj;
        builder.append(this.getProductId(), other.getProductId());
        builder.append(this.getProductName(), other.getProductName());
        builder.append(this.getProductPrice(), other.getProductPrice());
        builder.append(this.getProductPricingState(), other.getProductPricingState());
        builder.append(this.getProductQuantity(), other.getProductQuantity());
        builder.append(this.getProductUPC(), other.getProductUPC());
        builder.append(this.getWishListId(), other.getWishListId());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("productId", productId).add("productName", productName).add("productPrice", productPrice)
                .add("productPricingState", productPricingState).add("productQuantity", productQuantity).add("productUPC", productUPC).add("wishListId", wishListId)
                .toString();
    }

}
