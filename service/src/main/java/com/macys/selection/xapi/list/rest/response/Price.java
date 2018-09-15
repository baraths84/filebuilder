/*******************************************************************************
 * Copyright (c) 2017 macys.com. All rights reserved.
 *******************************************************************************/
package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * @author YH03933
 */
@JsonRootName("price")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"retailPrice", "originalPrice", "intermediateSalesValue", "salesValue",
        "onSale", "upcOnSale", "priceType", "priceTypeText", "basePriceType", "retailPriceHigh",
        "originalPriceLabel", "retailPriceLabel","pricingPolicyText","pricingPolicy"})

@JsonInclude(Include.NON_NULL)
public class Price implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3941177317503995609L;

    @JsonProperty("retailPrice")
    private Double retailPrice;

    @JsonProperty("originalPrice")
    private Double originalPrice;

    @JsonProperty("intermediateSalesValue")
    private Double intermediateSalesValue;

    @JsonProperty("salesValue")
    private Double salesValue;

    @JsonProperty("onSale")
    private Boolean onSale;

    @JsonProperty("upcOnSale")
    private Boolean upcOnSale;

    @JsonProperty("priceType")
    private Integer priceType;

    @JsonProperty("priceTypeText")
    private String priceTypeText;

    @JsonProperty("basePriceType")
    private Integer basePriceType;

    @JsonProperty("retailPriceHigh")
    private Double retailPriceHigh;

    @JsonProperty("originalPriceLabel")
    private String originalPriceLabel;

    @JsonProperty("retailPriceLabel")
    private String retailPriceLabel;


    @JsonProperty("pricingPolicyText")
    private String pricingPolicyText;


    @JsonProperty("pricingPolicy")
    private String pricingPolicy;


    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getIntermediateSalesValue() {
        return intermediateSalesValue;
    }

    public void setIntermediateSalesValue(Double intermediateSalesValue) {
        this.intermediateSalesValue = intermediateSalesValue;
    }

    public Double getSalesValue() {
        return salesValue;
    }

    public void setSalesValue(Double salesValue) {
        this.salesValue = salesValue;
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public Boolean getUpcOnSale() {
        return upcOnSale;
    }

    public void setUpcOnSale(Boolean upcOnSale) {
        this.upcOnSale = upcOnSale;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public String getPriceTypeText() {
        return priceTypeText;
    }

    public void setPriceTypeText(String priceTypeText) {
        this.priceTypeText = priceTypeText;
    }

    public Integer getBasePriceType() {
        return basePriceType;
    }

    public void setBasePriceType(Integer basePriceType) {
        this.basePriceType = basePriceType;
    }

    public Double getRetailPriceHigh() {
        return retailPriceHigh;
    }

    public void setRetailPriceHigh(Double retailPriceHigh) {
        this.retailPriceHigh = retailPriceHigh;
    }

    public String getOriginalPriceLabel() {
        return originalPriceLabel;
    }

    public void setOriginalPriceLabel(String originalPriceLabel) {
        this.originalPriceLabel = originalPriceLabel;
    }

    public String getRetailPriceLabel() {
        return retailPriceLabel;
    }

    public void setRetailPriceLabel(String retailPriceLabel) {
        this.retailPriceLabel = retailPriceLabel;
    }

    public String getPricingPolicyText() {
        return pricingPolicyText;
    }

    public void setPricingPolicyText(String pricingPolicyText) {
        this.pricingPolicyText = pricingPolicyText;
    }

    public String getPricingPolicy() {
        return pricingPolicy;
    }

    public void setPricingPolicy(String pricingPolicy) {
        this.pricingPolicy = pricingPolicy;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.retailPrice).append(this.originalPrice).append(this.intermediateSalesValue)
                .append(this.salesValue).append(this.onSale).append(this.upcOnSale).append(this.priceType)
                .append(this.priceTypeText).append(this.basePriceType).append(this.retailPriceHigh)
                .append(this.originalPriceLabel).append(this.retailPriceLabel).append(this.pricingPolicyText).append(this.pricingPolicy);
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
        Price other = (Price) obj;
        builder.append(this.getBasePriceType(), other.getBasePriceType());
        builder.append(this.getIntermediateSalesValue(), other.getIntermediateSalesValue());
        builder.append(this.getOriginalPrice(), other.getOriginalPrice());
        builder.append(this.getPriceType(), other.getPriceType());
        builder.append(this.getRetailPrice(), other.getRetailPrice());
        builder.append(this.getRetailPriceHigh(), other.getRetailPriceHigh());
        builder.append(this.getSalesValue(), other.getSalesValue());
        builder.append(this.getPriceTypeText(), other.getPriceTypeText());
        builder.append(this.getOriginalPriceLabel(), other.getOriginalPriceLabel());
        builder.append(this.getRetailPriceLabel(), other.getRetailPriceLabel());
        builder.append(this.getPricingPolicyText(), other.getPriceTypeText());
        builder.append(this.getPricingPolicy(), other.getPricingPolicy());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("retailPrice", retailPrice)
                .add("originalPrice", originalPrice).add("intermediateSalesValue", intermediateSalesValue)
                .add("salesValue", salesValue).add("onSale", onSale).add("upcOnSale", upcOnSale)
                .add("priceType", priceType).add("priceTypeText", priceTypeText)
                .add("basePriceType", basePriceType).add("retailPriceHigh", retailPriceHigh)
                .add("originalPriceLabel", originalPriceLabel).add("retailPriceLabel", retailPriceLabel)
                .add("pricingPolicyText", pricingPolicyText).add("pricingPolicy", pricingPolicy)
                .toString();
    }


}
