package com.macys.selection.xapi.list.client.response.fcc;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceResponse {
    private Double originalPrice;
    private Double originalPriceHigh;
    private Double intermediatePrice;
    private Double intermediatePriceHigh;
    private Double retailPrice;
    private Double retailPriceHigh;
    private Integer priceType;
    private Double saleValue;
    private Double saleValueHigh;
    private Double intermediateSaleValue;
    private Double intermediateSaleValueHigh;
    private Date effectiveDate;
    private Date expirationDate;
    private String displayCode;
    private Integer basePriceType;
    private Boolean onSale;
    private Boolean upcOnSale;
    private Boolean memberProductOnSale;
    private String originalPriceLabel;
    private String intermediatePriceLabel;
    private String retailPriceLabel;
    private String priceTypeText;
    private String pricingPolicyText;
    private String pricingPolicy;

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getOriginalPriceHigh() {
        return originalPriceHigh;
    }

    public void setOriginalPriceHigh(Double originalPriceHigh) {
        this.originalPriceHigh = originalPriceHigh;
    }

    public Double getIntermediatePrice() {
        return intermediatePrice;
    }

    public void setIntermediatePrice(Double intermediatePrice) {
        this.intermediatePrice = intermediatePrice;
    }

    public Double getIntermediatePriceHigh() {
        return intermediatePriceHigh;
    }

    public void setIntermediatePriceHigh(Double intermediatePriceHigh) {
        this.intermediatePriceHigh = intermediatePriceHigh;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getRetailPriceHigh() {
        return retailPriceHigh;
    }

    public void setRetailPriceHigh(Double retailPriceHigh) {
        this.retailPriceHigh = retailPriceHigh;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public Double getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(Double saleValue) {
        this.saleValue = saleValue;
    }

    public Double getSaleValueHigh() {
        return saleValueHigh;
    }

    public void setSaleValueHigh(Double saleValueHigh) {
        this.saleValueHigh = saleValueHigh;
    }

    public Double getIntermediateSaleValue() {
        return intermediateSaleValue;
    }

    public void setIntermediateSaleValue(Double intermediateSaleValue) {
        this.intermediateSaleValue = intermediateSaleValue;
    }

    public Double getIntermediateSaleValueHigh() {
        return intermediateSaleValueHigh;
    }

    public void setIntermediateSaleValueHigh(Double intermediateSaleValueHigh) {
        this.intermediateSaleValueHigh = intermediateSaleValueHigh;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDisplayCode() {
        return displayCode;
    }

    public void setDisplayCode(String displayCode) {
        this.displayCode = displayCode;
    }

    public Integer getBasePriceType() {
        return basePriceType;
    }

    public void setBasePriceType(Integer basePriceType) {
        this.basePriceType = basePriceType;
    }

    public Boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public Boolean isUpcOnSale() {
        return upcOnSale;
    }

    public void setUpcOnSale(Boolean upcOnSale) {
        this.upcOnSale = upcOnSale;
    }

    public Boolean isMemberProductOnSale() {
        return memberProductOnSale;
    }

    public void setMemberProductOnSale(Boolean memberProductOnSale) {
        this.memberProductOnSale = memberProductOnSale;
    }

    public String getOriginalPriceLabel() {
        return originalPriceLabel;
    }

    public void setOriginalPriceLabel(String originalPriceLabel) {
        this.originalPriceLabel = originalPriceLabel;
    }

    public String getIntermediatePriceLabel() {
        return intermediatePriceLabel;
    }

    public void setIntermediatePriceLabel(String intermediatePriceLabel) {
        this.intermediatePriceLabel = intermediatePriceLabel;
    }

    public String getRetailPriceLabel() {
        return retailPriceLabel;
    }

    public void setRetailPriceLabel(String retailPriceLabel) {
        this.retailPriceLabel = retailPriceLabel;
    }

    public String getPriceTypeText() {
        return priceTypeText;
    }

    public void setPriceTypeText(String priceTypeText) {
        this.priceTypeText = priceTypeText;
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
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("originalPrice", originalPrice)
                .add("originalPriceHigh", originalPriceHigh)
                .add("intermediatePrice", intermediatePrice)
                .add("intermediatePriceHigh", intermediatePriceHigh)
                .add("retailPrice", retailPrice)
                .add("retailPriceHigh", retailPriceHigh)
                .add("priceType", priceType)
                .add("saleValue", saleValue)
                .add("saleValueHigh", saleValueHigh)
                .add("intermediateSaleValue", intermediateSaleValue)
                .add("intermediateSaleValueHigh", intermediateSaleValueHigh)
                .add("effectiveDate", effectiveDate)
                .add("expirationDate", expirationDate)
                .add("displayCode", displayCode)
                .add("basePriceType", basePriceType)
                .add("onSale", onSale)
                .add("upcOnSale", upcOnSale)
                .add("memberProductOnSale", memberProductOnSale)
                .add("originalPriceLabel", originalPriceLabel)
                .add("intermediatePriceLabel", intermediatePriceLabel)
                .add("retailPriceLabel", retailPriceLabel)
                .add("priceTypeText", priceTypeText)
                .add("pricingPolicyText", pricingPolicyText)
                .add("pricingPolicy", pricingPolicy)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PriceResponse that = (PriceResponse) o;

        return new EqualsBuilder()
                .append(originalPrice, that.originalPrice)
                .append(originalPriceHigh, that.originalPriceHigh)
                .append(intermediatePrice, that.intermediatePrice)
                .append(intermediatePriceHigh, that.intermediatePriceHigh)
                .append(retailPrice, that.retailPrice)
                .append(retailPriceHigh, that.retailPriceHigh)
                .append(priceType, that.priceType)
                .append(saleValue, that.saleValue)
                .append(saleValueHigh, that.saleValueHigh)
                .append(intermediateSaleValue, that.intermediateSaleValue)
                .append(intermediateSaleValueHigh, that.intermediateSaleValueHigh)
                .append(effectiveDate, that.effectiveDate)
                .append(expirationDate, that.expirationDate)
                .append(displayCode, that.displayCode)
                .append(basePriceType, that.basePriceType)
                .append(onSale, that.onSale)
                .append(upcOnSale, that.upcOnSale)
                .append(memberProductOnSale, that.memberProductOnSale)
                .append(originalPriceLabel, that.originalPriceLabel)
                .append(intermediatePriceLabel, that.intermediatePriceLabel)
                .append(retailPriceLabel, that.retailPriceLabel)
                .append(priceTypeText, that.priceTypeText)
                .append(pricingPolicyText, that.pricingPolicyText)
                .append(pricingPolicy, that.pricingPolicy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(originalPrice)
                .append(originalPriceHigh)
                .append(intermediatePrice)
                .append(intermediatePriceHigh)
                .append(retailPrice)
                .append(retailPriceHigh)
                .append(priceType)
                .append(saleValue)
                .append(saleValueHigh)
                .append(intermediateSaleValue)
                .append(intermediateSaleValueHigh)
                .append(effectiveDate)
                .append(expirationDate)
                .append(displayCode)
                .append(basePriceType)
                .append(onSale)
                .append(upcOnSale)
                .append(memberProductOnSale)
                .append(originalPriceLabel)
                .append(intermediatePriceLabel)
                .append(retailPriceLabel)
                .append(priceTypeText)
                .append(pricingPolicyText)
                .append(pricingPolicy)
                .toHashCode();
    }
}
