package com.macys.selection.xapi.list.client.response.fcc;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ColorwayResponse {

    private Integer colorwayId;
    private String colorName;
    private String colorNormalName;
    private Integer swatchSeqNumber;
    private Integer swapoutSeqNumber;
    private ColorwayImageResponse primaryImage;
    private Collection<ColorwayImageResponse> additionalImages;
    private Collection<AttributeResponse> attributes;
    private ColorwayImageResponse swatchImage;

    public Integer getColorwayId() {
        return colorwayId;
    }

    public void setColorwayId(Integer colorwayId) {
        this.colorwayId = colorwayId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorNormalName() {
        return colorNormalName;
    }

    public void setColorNormalName(String colorNormalName) {
        this.colorNormalName = colorNormalName;
    }

    public Integer getSwatchSeqNumber() {
        return swatchSeqNumber;
    }

    public void setSwatchSeqNumber(Integer swatchSeqNumber) {
        this.swatchSeqNumber = swatchSeqNumber;
    }

    public Integer getSwapoutSeqNumber() {
        return swapoutSeqNumber;
    }

    public void setSwapoutSeqNumber(Integer swapoutSeqNumber) {
        this.swapoutSeqNumber = swapoutSeqNumber;
    }

    public ColorwayImageResponse getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(ColorwayImageResponse primaryImage) {
        this.primaryImage = primaryImage;
    }

    public Collection<ColorwayImageResponse> getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(Collection<ColorwayImageResponse> additionalImages) {
        this.additionalImages = additionalImages;
    }

    public Collection<AttributeResponse> getAttributes() {
        return attributes;
    }

    public void setAttributes(Collection<AttributeResponse> attributes) {
        this.attributes = attributes;
    }

    public ColorwayImageResponse getSwatchImage() {
        return swatchImage;
    }

    public void setSwatchImage(ColorwayImageResponse swatchImage) {
        this.swatchImage = swatchImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ColorwayResponse that = (ColorwayResponse) o;

        return new EqualsBuilder()
                .append(colorwayId, that.colorwayId)
                .append(colorName, that.colorName)
                .append(colorNormalName, that.colorNormalName)
                .append(swatchSeqNumber, that.swatchSeqNumber)
                .append(swapoutSeqNumber, that.swapoutSeqNumber)
                .append(primaryImage, that.primaryImage)
                .append(additionalImages, that.additionalImages)
                .append(attributes, that.attributes)
                .append(swatchImage, that.swatchImage)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(colorwayId)
                .append(colorName)
                .append(colorNormalName)
                .append(swatchSeqNumber)
                .append(swapoutSeqNumber)
                .append(primaryImage)
                .append(additionalImages)
                .append(attributes)
                .append(swatchImage)
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("colorwayId", colorwayId)
                .add("colorName", colorName)
                .add("colorNormalName", colorNormalName)
                .add("swatchSeqNumber", swatchSeqNumber)
                .add("swapoutSeqNumber", swapoutSeqNumber)
                .add("primaryImage", primaryImage)
                .add("additionalImages", additionalImages)
                .add("attributes", attributes)
                .add("swatchImage", swatchImage)
                .toString();
    }
}
