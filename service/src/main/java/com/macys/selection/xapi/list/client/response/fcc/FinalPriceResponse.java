package com.macys.selection.xapi.list.client.response.fcc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class FinalPriceResponse {
    private double finalPrice;
    private double finalPriceHigh;
    private String displayFinalPrice;
    private String productTypePromotion;
    private List<FinalPricePromotionResponse> promotions;

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public double getFinalPriceHigh() {
        return finalPriceHigh;
    }

    public void setFinalPriceHigh(double finalPriceHigh) {
        this.finalPriceHigh = finalPriceHigh;
    }

    public String getDisplayFinalPrice() {
        return displayFinalPrice;
    }

    public void setDisplayFinalPrice(String displayFinalPrice) {
        this.displayFinalPrice = displayFinalPrice;
    }

    public String getProductTypePromotion() {
        return productTypePromotion;
    }

    public void setProductTypePromotion(String productTypePromotion) {
        this.productTypePromotion = productTypePromotion;
    }

    public List<FinalPricePromotionResponse> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<FinalPricePromotionResponse> promotions) {
        this.promotions = promotions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FinalPriceResponse that = (FinalPriceResponse) o;

        return new EqualsBuilder()
                .append(finalPrice, that.finalPrice)
                .append(finalPriceHigh, that.finalPriceHigh)
                .append(displayFinalPrice, that.displayFinalPrice)
                .append(productTypePromotion, that.productTypePromotion)
                .append(promotions, that.promotions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(finalPrice)
                .append(finalPriceHigh)
                .append(displayFinalPrice)
                .append(productTypePromotion)
                .append(promotions)
                .toHashCode();
    }
}
