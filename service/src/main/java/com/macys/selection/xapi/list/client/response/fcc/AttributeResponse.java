package com.macys.selection.xapi.list.client.response.fcc;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttributeResponse {

    private String name;
    private Integer sortWeight;
    private Boolean visible;
    private Boolean unary;
    private Collection<AttributeValueResponse> attributeValues;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSortWeight(Integer sortWeight) {
        this.sortWeight = sortWeight;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setUnary(Boolean unary) {
        this.unary = unary;
    }

    public Collection<AttributeValueResponse> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(Collection<AttributeValueResponse> attributeValues) {
        this.attributeValues = attributeValues;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("sortWeight", sortWeight)
                .add("visible", visible)
                .add("unary", unary)
                .add("attributeValues", attributeValues)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AttributeResponse that = (AttributeResponse) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(sortWeight, that.sortWeight)
                .append(visible, that.visible)
                .append(unary, that.unary)
                .append(attributeValues, that.attributeValues)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(sortWeight)
                .append(visible)
                .append(unary)
                .append(attributeValues)
                .toHashCode();
    }
}
