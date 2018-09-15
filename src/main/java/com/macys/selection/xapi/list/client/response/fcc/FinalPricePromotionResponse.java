package com.macys.selection.xapi.list.client.response.fcc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FinalPricePromotionResponse {
    private Integer promotionId;
    private String promotionName;
    private boolean global;

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FinalPricePromotionResponse that = (FinalPricePromotionResponse) o;

        return new EqualsBuilder()
                .append(global, that.global)
                .append(promotionId, that.promotionId)
                .append(promotionName, that.promotionName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(promotionId)
                .append(promotionName)
                .append(global)
                .toHashCode();
    }
}
