package com.macys.selection.xapi.list.client.response.fcc;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailabilityResponse {

    private Integer upcId;
    private Boolean available;
    private Integer shipDays;
    private Integer premiumShipDays;
    private Integer expressShipDays;
    private String orderMethod;
    private Boolean giftWrappable;
    private Boolean giftMessageable;
    private String returnConstraints;
    private Integer vendorId;
    private String source;
    private Short shippingMethodsCode;
    private String shipMethodsSource;
    private Boolean inStoreEligibility;
    private String inventoryStatusCode;
    private String upcAvailabilityMessage;
    private Integer maxQuantity;
    private String reasonCode;
    private Boolean registryEligible;
    private Integer registryCategoryId;
    private String registryCategoryDescription;
    private Boolean dropShipFlag;
    private Boolean dropShipGiftWrappable;
    private Boolean discontinued;
    private Integer ngfProductCode;
    private Date ngfProductDate;
    private Boolean checkoutGiftAttrUpdt;
    private Boolean checkoutFulfilUpdt;
    private Date availabilityLastModified;
    private Date giftAttributesLastModified;

    public Integer getUpcId() {
        return upcId;
    }

    public void setUpcId(Integer upcId) {
        this.upcId = upcId;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getShipDays() {
        return shipDays;
    }

    public void setShipDays(Integer shipDays) {
        this.shipDays = shipDays;
    }

    public Integer getPremiumShipDays() {
        return premiumShipDays;
    }

    public void setPremiumShipDays(Integer premiumShipDays) {
        this.premiumShipDays = premiumShipDays;
    }

    public Integer getExpressShipDays() {
        return expressShipDays;
    }

    public void setExpressShipDays(Integer expressShipDays) {
        this.expressShipDays = expressShipDays;
    }

    public String getOrderMethod() {
        return orderMethod;
    }

    public void setOrderMethod(String orderMethod) {
        this.orderMethod = orderMethod;
    }

    public Boolean getGiftWrappable() {
        return giftWrappable;
    }

    public void setGiftWrappable(Boolean giftWrappable) {
        this.giftWrappable = giftWrappable;
    }

    public Boolean getGiftMessageable() {
        return giftMessageable;
    }

    public void setGiftMessageable(Boolean giftMessageable) {
        this.giftMessageable = giftMessageable;
    }

    public String getReturnConstraints() {
        return returnConstraints;
    }

    public void setReturnConstraints(String returnConstraints) {
        this.returnConstraints = returnConstraints;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Short getShippingMethodsCode() {
        return shippingMethodsCode;
    }

    public void setShippingMethodsCode(Short shippingMethodsCode) {
        this.shippingMethodsCode = shippingMethodsCode;
    }

    public String getShipMethodsSource() {
        return shipMethodsSource;
    }

    public void setShipMethodsSource(String shipMethodsSource) {
        this.shipMethodsSource = shipMethodsSource;
    }

    public Boolean getInStoreEligibility() {
        return inStoreEligibility;
    }

    public void setInStoreEligibility(Boolean inStoreEligibility) {
        this.inStoreEligibility = inStoreEligibility;
    }

    public String getInventoryStatusCode() {
        return inventoryStatusCode;
    }

    public void setInventoryStatusCode(String inventoryStatusCode) {
        this.inventoryStatusCode = inventoryStatusCode;
    }

    public String getUpcAvailabilityMessage() {
        return upcAvailabilityMessage;
    }

    public void setUpcAvailabilityMessage(String upcAvailabilityMessage) {
        this.upcAvailabilityMessage = upcAvailabilityMessage;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Boolean getRegistryEligible() {
        return registryEligible;
    }

    public void setRegistryEligible(Boolean registryEligible) {
        this.registryEligible = registryEligible;
    }

    public Integer getRegistryCategoryId() {
        return registryCategoryId;
    }

    public void setRegistryCategoryId(Integer registryCategoryId) {
        this.registryCategoryId = registryCategoryId;
    }

    public String getRegistryCategoryDescription() {
        return registryCategoryDescription;
    }

    public void setRegistryCategoryDescription(String registryCategoryDescription) {
        this.registryCategoryDescription = registryCategoryDescription;
    }

    public Boolean getDropShipFlag() {
        return dropShipFlag;
    }

    public void setDropShipFlag(Boolean dropShipFlag) {
        this.dropShipFlag = dropShipFlag;
    }

    public Boolean getDropShipGiftWrappable() {
        return dropShipGiftWrappable;
    }

    public void setDropShipGiftWrappable(Boolean dropShipGiftWrappable) {
        this.dropShipGiftWrappable = dropShipGiftWrappable;
    }

    public Boolean getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(Boolean discontinued) {
        this.discontinued = discontinued;
    }

    public Integer getNgfProductCode() {
        return ngfProductCode;
    }

    public void setNgfProductCode(Integer ngfProductCode) {
        this.ngfProductCode = ngfProductCode;
    }

    public Date getNgfProductDate() {
        return ngfProductDate;
    }

    public void setNgfProductDate(Date ngfProductDate) {
        this.ngfProductDate = ngfProductDate;
    }

    public Boolean getCheckoutGiftAttrUpdt() {
        return checkoutGiftAttrUpdt;
    }

    public void setCheckoutGiftAttrUpdt(Boolean checkoutGiftAttrUpdt) {
        this.checkoutGiftAttrUpdt = checkoutGiftAttrUpdt;
    }

    public Boolean getCheckoutFulfilUpdt() {
        return checkoutFulfilUpdt;
    }

    public void setCheckoutFulfilUpdt(Boolean checkoutFulfilUpdt) {
        this.checkoutFulfilUpdt = checkoutFulfilUpdt;
    }

    public Date getAvailabilityLastModified() {
        return availabilityLastModified;
    }

    public void setAvailabilityLastModified(Date availabilityLastModified) {
        this.availabilityLastModified = availabilityLastModified;
    }

    public Date getGiftAttributesLastModified() {
        return giftAttributesLastModified;
    }

    public void setGiftAttributesLastModified(Date giftAttributesLastModified) {
        this.giftAttributesLastModified = giftAttributesLastModified;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AvailabilityResponse that = (AvailabilityResponse) o;

        return new EqualsBuilder()
                .append(upcId, that.upcId)
                .append(available, that.available)
                .append(shipDays, that.shipDays)
                .append(premiumShipDays, that.premiumShipDays)
                .append(expressShipDays, that.expressShipDays)
                .append(orderMethod, that.orderMethod)
                .append(giftWrappable, that.giftWrappable)
                .append(giftMessageable, that.giftMessageable)
                .append(returnConstraints, that.returnConstraints)
                .append(vendorId, that.vendorId)
                .append(source, that.source)
                .append(shippingMethodsCode, that.shippingMethodsCode)
                .append(shipMethodsSource, that.shipMethodsSource)
                .append(inStoreEligibility, that.inStoreEligibility)
                .append(inventoryStatusCode, that.inventoryStatusCode)
                .append(upcAvailabilityMessage, that.upcAvailabilityMessage)
                .append(maxQuantity, that.maxQuantity)
                .append(reasonCode, that.reasonCode)
                .append(registryEligible, that.registryEligible)
                .append(registryCategoryId, that.registryCategoryId)
                .append(registryCategoryDescription, that.registryCategoryDescription)
                .append(dropShipFlag, that.dropShipFlag)
                .append(dropShipGiftWrappable, that.dropShipGiftWrappable)
                .append(discontinued, that.discontinued)
                .append(ngfProductCode, that.ngfProductCode)
                .append(ngfProductDate, that.ngfProductDate)
                .append(checkoutGiftAttrUpdt, that.checkoutGiftAttrUpdt)
                .append(checkoutFulfilUpdt, that.checkoutFulfilUpdt)
                .append(availabilityLastModified, that.availabilityLastModified)
                .append(giftAttributesLastModified, that.giftAttributesLastModified)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(upcId)
                .append(available)
                .append(shipDays)
                .append(premiumShipDays)
                .append(expressShipDays)
                .append(orderMethod)
                .append(giftWrappable)
                .append(giftMessageable)
                .append(returnConstraints)
                .append(vendorId)
                .append(source)
                .append(shippingMethodsCode)
                .append(shipMethodsSource)
                .append(inStoreEligibility)
                .append(inventoryStatusCode)
                .append(upcAvailabilityMessage)
                .append(maxQuantity)
                .append(reasonCode)
                .append(registryEligible)
                .append(registryCategoryId)
                .append(registryCategoryDescription)
                .append(dropShipFlag)
                .append(dropShipGiftWrappable)
                .append(discontinued)
                .append(ngfProductCode)
                .append(ngfProductDate)
                .append(checkoutGiftAttrUpdt)
                .append(checkoutFulfilUpdt)
                .append(availabilityLastModified)
                .append(giftAttributesLastModified)
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("upcId", upcId)
                .add("available", available)
                .add("shipDays", shipDays)
                .add("premiumShipDays", premiumShipDays)
                .add("expressShipDays", expressShipDays)
                .add("orderMethod", orderMethod)
                .add("giftWrappable", giftWrappable)
                .add("giftMessageable", giftMessageable)
                .add("returnConstraints", returnConstraints)
                .add("vendorId", vendorId)
                .add("source", source)
                .add("shippingMethodsCode", shippingMethodsCode)
                .add("shipMethodsSource", shipMethodsSource)
                .add("inStoreEligibility", inStoreEligibility)
                .add("inventoryStatusCode", inventoryStatusCode)
                .add("upcAvailabilityMessage", upcAvailabilityMessage)
                .add("maxQuantity", maxQuantity)
                .add("reasonCode", reasonCode)
                .add("registryEligible", registryEligible)
                .add("registryCategoryId", registryCategoryId)
                .add("registryCategoryDescription", registryCategoryDescription)
                .add("dropShipFlag", dropShipFlag)
                .add("dropShipGiftWrappable", dropShipGiftWrappable)
                .add("discontinued", discontinued)
                .add("ngfProductCode", ngfProductCode)
                .add("ngfProductDate", ngfProductDate)
                .add("checkoutGiftAttrUpdt", checkoutGiftAttrUpdt)
                .add("checkoutFulfilUpdt", checkoutFulfilUpdt)
                .add("availabilityLastModified", availabilityLastModified)
                .add("giftAttributesLastModified", giftAttributesLastModified)
                .toString();
    }
}
