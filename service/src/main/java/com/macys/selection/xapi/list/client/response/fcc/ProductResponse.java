package com.macys.selection.xapi.list.client.response.fcc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;


@JsonRootName("product")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponse {

    private Integer id;
    private String name;
    private boolean active;
    private ColorwayImageResponse primaryImage;
    private Integer defaultCategoryId;
    private String typeName;
    private Integer taxwareCode;
    private Integer maxQuantity;
    private List<UpcResponse> upcs;
    private PriceResponse price;
    private List<AttributeResponse> attributes;
    private FobResponse fob;
    private BrandResponse brand;
    private boolean live;
    private List<ColorwayImageResponse> additionalImages;
    // TODO It looks like it is not applied for wishlists
    private List<ColorwayResponse> colorwayImages;
    private boolean isNew;
    private boolean countryEligible;
    private ShippingResponse shipping;
    private Integer giftWrapId;
    private List<String> additionalImageSource;
    private ReviewStatisticsResponse reviewStatistics;
    private boolean available;
    private String primaryPortraitSource;
    private List<Integer> memberProductIds;
    private FinalPriceResponse finalPrice;

    public List<Integer> getMemberProductIds() {
        return memberProductIds;
    }

    public void setMemberProductIds(List<Integer> memberProductIds) {
        this.memberProductIds = memberProductIds;
    }

    public String getPrimaryPortraitSource() {
        return primaryPortraitSource;
    }

    public void setPrimaryPortraitSource(String primaryPortraitSource) {
        this.primaryPortraitSource = primaryPortraitSource;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ShippingResponse getShipping() {
        return shipping;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDefaultCategoryId() {
        return defaultCategoryId;
    }

    public void setDefaultCategoryId(Integer defaultCategoryId) {
        this.defaultCategoryId = defaultCategoryId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getTaxwareCode() {
        return taxwareCode;
    }

    public void setTaxwareCode(Integer taxwareCode) {
        this.taxwareCode = taxwareCode;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public List<UpcResponse> getUpcs() {
        return upcs;
    }

    public void setUpcs(List<UpcResponse> upcs) {
        this.upcs = upcs;
    }

    public PriceResponse getPrice() {
        return price;
    }

    public void setPrice(PriceResponse price) {
        this.price = price;
    }

    public FobResponse getFob() {
        return fob;
    }

    public void setFob(FobResponse fob) {
        this.fob = fob;
    }

    public BrandResponse getBrand() {
        return brand;
    }

    public void setBrand(BrandResponse brand) {
        this.brand = brand;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public ColorwayImageResponse getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(ColorwayImageResponse primaryImage) {
        this.primaryImage = primaryImage;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isCountryEligible() {
        return countryEligible;
    }

    public void setCountryEligible(boolean countryEligible) {
        this.countryEligible = countryEligible;
    }

    public List<ColorwayImageResponse> getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(List<ColorwayImageResponse> additionalImages) {
        this.additionalImages = additionalImages;
    }

    public List<ColorwayResponse> getColorwayImages() {
        return colorwayImages;
    }

    public void setColorwayImages(List<ColorwayResponse> colorwayImages) {
        this.colorwayImages = colorwayImages;
    }

    public void setShipping(ShippingResponse shipping) {
        this.shipping = shipping;
    }

    public List<AttributeResponse> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeResponse> attributes) {
        this.attributes = attributes;
    }

    public void setGiftWrapId(Integer giftWrapId) {
        this.giftWrapId = giftWrapId;
    }

    public void setAdditionalImageSource(List<String> additionalImageSource) {
        this.additionalImageSource = additionalImageSource;
    }

    public ReviewStatisticsResponse getReviewStatistics() {
        return reviewStatistics;
    }

    public void setReviewStatistics(ReviewStatisticsResponse reviewStatistics) {
        this.reviewStatistics = reviewStatistics;
    }

    public FinalPriceResponse getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(FinalPriceResponse finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("active", active)
                .add("primaryImage", primaryImage)
                .add("defaultCategoryId", defaultCategoryId)
                .add("typeName", typeName)
                .add("taxwareCode", taxwareCode)
                .add("maxQuantity", maxQuantity)
                .add("price", price)
                .add("attributes", attributes)
                .add("fob", fob)
                .add("brand", brand)
                .add("live", live)
                .add("additionalImages", additionalImages)
                .add("colorwayImages", colorwayImages)
                .add("isNew", isNew)
                .add("countryEligible", countryEligible)
                .add("shipping", shipping)
                .add("giftWrapId", giftWrapId)
                .add("additionalImageSource", additionalImageSource)
                .add("reviewStatistics", reviewStatistics)
                .add("available", available)
                .add("primaryPortraitSource", primaryPortraitSource)
                .add("memberProductIds", memberProductIds)
                .add("finalPrice", finalPrice)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductResponse that = (ProductResponse) o;

        return new EqualsBuilder()
                .append(active, that.active)
                .append(live, that.live)
                .append(isNew, that.isNew)
                .append(countryEligible, that.countryEligible)
                .append(available, that.available)
                .append(id, that.id)
                .append(name, that.name)
                .append(primaryImage, that.primaryImage)
                .append(defaultCategoryId, that.defaultCategoryId)
                .append(typeName, that.typeName)
                .append(taxwareCode, that.taxwareCode)
                .append(maxQuantity, that.maxQuantity)
                .append(price, that.price)
                .append(attributes, that.attributes)
                .append(fob, that.fob)
                .append(brand, that.brand)
                .append(additionalImages, that.additionalImages)
                .append(colorwayImages, that.colorwayImages)
                .append(shipping, that.shipping)
                .append(giftWrapId, that.giftWrapId)
                .append(additionalImageSource, that.additionalImageSource)
                .append(reviewStatistics, that.reviewStatistics)
                .append(primaryPortraitSource, that.primaryPortraitSource)
                .append(memberProductIds, that.memberProductIds)
                .append(finalPrice, that.finalPrice)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(active)
                .append(primaryImage)
                .append(defaultCategoryId)
                .append(typeName)
                .append(taxwareCode)
                .append(maxQuantity)
                .append(price)
                .append(attributes)
                .append(fob)
                .append(brand)
                .append(live)
                .append(additionalImages)
                .append(colorwayImages)
                .append(isNew)
                .append(countryEligible)
                .append(shipping)
                .append(giftWrapId)
                .append(additionalImageSource)
                .append(reviewStatistics)
                .append(available)
                .append(primaryPortraitSource)
                .append(memberProductIds)
                .append(finalPrice)
                .toHashCode();
    }
}
