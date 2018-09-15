package com.macys.selection.xapi.list.rest.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;

@JsonRootName("data")
@JsonPropertyOrder({
        "event_name",
        "product_id",
        "product_name",
        "product_original_price",
        "product_price",
        "product_size",
        "product_color",
        "product_upc"})
@JsonInclude(Include.NON_NULL)
public class ResponseDigitalAnalytics implements Serializable{

    private static final long serialVersionUID = 4756778276678970315L;

    @JsonProperty("event_name")
    private String eventName;

    @JsonProperty("product_id")
    private List<String> productId;

    @JsonProperty("product_name")
    private List<String> productName;

    @JsonProperty("product_price")
    private List<String> productPrice;

    @JsonProperty("product_original_price")
    private List<String> productOriginalPrice;

    @JsonProperty("product_size")
    private List<String> productSize;

    @JsonProperty("product_color")
    private List<String> productColor;

    @JsonProperty("product_upc")
    private List<String> productUPC;

    public List<String> getProductColor() {
        return productColor;
    }

    public void setProductColor(List<String> productColor) {
        this.productColor = productColor;
    }

    public List<String> getProductSize() {
        return productSize;
    }

    public void setProductSize(List<String> productSize) {
        this.productSize = productSize;
    }

    public List<String> getProductId() {
        return productId;
    }

    public void setProductId(List<String> productId) {
        this.productId = new ArrayList<>(productId);
    }

    public List<String> getProductName() {
        return productName;
    }

    public void setProductName(List<String> productName) {
        this.productName = new ArrayList<>(productName);
    }

    public List<String> getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(List<String> productPrice) {
        this.productPrice = productPrice;
    }

    public List<String> getProductUPC() {
        return productUPC;
    }

    public void setProductUPC(List<String> productUPC) {
        this.productUPC = productUPC;
    }

    public List<String> getProductOriginalPrice() {
        return productOriginalPrice;
    }

    public void setProductOriginalPrice(List<String> productOriginalPrice) {
        this.productOriginalPrice = new ArrayList<>(productOriginalPrice);
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.eventName)
               .append(this.productId)
               .append(this.productName)
               .append(this.productOriginalPrice)
               .append(this.productPrice)
               .append(this.productSize)
               .append(this.productColor)
               .append(this.productUPC);
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
        ResponseDigitalAnalytics other = (ResponseDigitalAnalytics) obj;
        builder.append(this.getEventName(), other.getEventName());
        builder.append(this.getProductId(), other.getProductId());
        builder.append(this.getProductName(), other.getProductName());
        builder.append(this.getProductOriginalPrice(), other.getProductOriginalPrice());
        builder.append(this.getProductPrice(), other.getProductPrice());
        builder.append(this.getProductSize(), other.getProductSize());
        builder.append(this.getProductColor(), other.getProductColor());
        builder.append(this.getProductUPC(), other.getProductUPC());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("event_name", eventName)
                          .add("product_id", productId)
                          .add("product_name", productName)
                          .add("product_original_price", productOriginalPrice)
                          .add("product_price", productPrice)
                          .add("product_size", productSize)
                          .add("product_color", productColor)
                          .add("product_upc", productUPC)
                          .toString();
    }
}
