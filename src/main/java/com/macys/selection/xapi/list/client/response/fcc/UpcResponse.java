package com.macys.selection.xapi.list.client.response.fcc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.macys.platform.rest.framework.jaxb.Link;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JsonRootName("upc")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpcResponse {
    private Integer id;
    private Long upc;
    private Integer productId;
    private PriceResponse price;
    private AvailabilityResponse availability;
    private String addByApp;
    private boolean baseFeeExempt;
    private Double surchargeFee;
    private List<AttributeResponse> attributes;
    private boolean active;
    private boolean available;
    private ColorwayResponse colorway;
    private EligibilityStatusResponse globalBopsEligibility;
    @JsonProperty("link")
    private Collection<Link> links = new ArrayList<>();
    private FinalPriceResponse finalPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUpc() {
        return upc;
    }

    public void setUpc(Long upc) {
        this.upc = upc;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public PriceResponse getPrice() {
        return price;
    }

    public void setPrice(PriceResponse price) {
        this.price = price;
    }

    public AvailabilityResponse getAvailability() {
        return availability;
    }

    public void setAvailability(AvailabilityResponse availability) {
        this.availability = availability;
    }

    public String getAddByApp() {
        return addByApp;
    }

    public void setAddByApp(String addByApp) {
        this.addByApp = addByApp;
    }

    public boolean isBaseFeeExempt() {
        return baseFeeExempt;
    }

    public void setBaseFeeExempt(boolean baseFeeExempt) {
        this.baseFeeExempt = baseFeeExempt;
    }

    public Double getSurchargeFee() {
        return surchargeFee;
    }

    public void setSurchargeFee(Double surchargeFee) {
        this.surchargeFee = surchargeFee;
    }

    public List<AttributeResponse> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeResponse> attributes) {
        this.attributes = attributes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ColorwayResponse getColorway() {
        return colorway;
    }

    public void setColorway(ColorwayResponse colorway) {
        this.colorway = colorway;
    }

    public EligibilityStatusResponse getGlobalBopsEligibility() {
        return globalBopsEligibility;
    }

    public void setGlobalBopsEligibility(EligibilityStatusResponse globalBopsEligibility) {
        this.globalBopsEligibility = globalBopsEligibility;
    }

    public Collection<Link> getLinks() {
        return links;
    }

    public void setLinks(Collection<Link> links) {
        this.links = links;
    }

    public FinalPriceResponse getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(FinalPriceResponse finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UpcResponse that = (UpcResponse) o;

        return new EqualsBuilder()
                .append(baseFeeExempt, that.baseFeeExempt)
                .append(active, that.active)
                .append(available, that.available)
                .append(id, that.id)
                .append(upc, that.upc)
                .append(productId, that.productId)
                .append(price, that.price)
                .append(availability, that.availability)
                .append(addByApp, that.addByApp)
                .append(surchargeFee, that.surchargeFee)
                .append(attributes, that.attributes)
                .append(colorway, that.colorway)
                .append(globalBopsEligibility, that.globalBopsEligibility)
                .append(links, that.links)
                .append(finalPrice, that.finalPrice)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(upc)
                .append(productId)
                .append(price)
                .append(availability)
                .append(addByApp)
                .append(baseFeeExempt)
                .append(surchargeFee)
                .append(attributes)
                .append(active)
                .append(available)
                .append(colorway)
                .append(globalBopsEligibility)
                .append(links)
                .append(finalPrice)
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("upc", upc)
                .add("productId", productId)
                .add("price", price)
                .add("availability", availability)
                .add("addByApp", addByApp)
                .add("baseFeeExempt", baseFeeExempt)
                .add("surchargeFee", surchargeFee)
                .add("attributes", attributes)
                .add("active", active)
                .add("available", available)
                .add("colorway", colorway)
                .add("globalBopsEligibility", globalBopsEligibility)
                .add("links", links)
                .add("finalPrice", finalPrice)
                .toString();
    }
}
