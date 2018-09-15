package com.macys.selection.xapi.list.client.response.fcc;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

public class ShippingResponse {

    private List<StateResponse> excludedStates;
    private List<ShippingMethodResponse> shippingMethods;
    private List<ReturnConstraintResponse> returnConstraints;
    private List<String> notes;
    private boolean giftWrappable;

    public List<String> getNotes() {
        return notes;
    }

    public List<StateResponse> getExcludedStates() {
        return excludedStates;
    }

    public List<ShippingMethodResponse> getShippingMethods() {
        return shippingMethods;
    }

    public List<ReturnConstraintResponse> getReturnConstraints() {
        return returnConstraints;
    }

    public boolean isGiftMessageable() {
        return giftMessageable;
    }

    public GiftWrapResponse getGiftWrap() {
        return giftWrap;
    }

    public boolean isGiftWrappable() {
        return giftWrappable;
    }

    private boolean giftMessageable;

    private GiftWrapResponse giftWrap;

    public void setExcludedStates(List<StateResponse> excludedStates) {
        this.excludedStates = excludedStates;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public void setShippingMethods(List<ShippingMethodResponse> shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    public void setReturnConstraints(List<ReturnConstraintResponse> returnConstraints) {
        this.returnConstraints = returnConstraints;
    }

    public void setGiftMessageable(boolean giftMessageable) {
        this.giftMessageable = giftMessageable;
    }

    public void setGiftWrappable(boolean giftWrappable) {
        this.giftWrappable = giftWrappable;
    }

    public void setGiftWrap(GiftWrapResponse giftWrap) {
        this.giftWrap = giftWrap;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("excludedStates", excludedStates)
                .add("shippingMethods", shippingMethods)
                .add("returnConstraints", returnConstraints)
                .add("notes", notes)
                .add("giftWrappable", giftWrappable)
                .add("giftMessageable", giftMessageable)
                .add("giftWrap", giftWrap)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ShippingResponse that = (ShippingResponse) o;

        return new EqualsBuilder()
                .append(giftWrappable, that.giftWrappable)
                .append(giftMessageable, that.giftMessageable)
                .append(excludedStates, that.excludedStates)
                .append(shippingMethods, that.shippingMethods)
                .append(returnConstraints, that.returnConstraints)
                .append(notes, that.notes)
                .append(giftWrap, that.giftWrap)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(excludedStates)
                .append(shippingMethods)
                .append(returnConstraints)
                .append(notes)
                .append(giftWrappable)
                .append(giftMessageable)
                .append(giftWrap)
                .toHashCode();
    }
}
