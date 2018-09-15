package com.macys.selection.xapi.list.client.response.fcc;

import com.google.common.base.MoreObjects;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandResponse {

    private Integer ozBrandId;
    private String brandName;

    public Integer getOzBrandId() {
        return ozBrandId;
    }

    public void setOzBrandId(Integer ozBrandId) {
        this.ozBrandId = ozBrandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ozBrandId", ozBrandId)
                .add("brandName", brandName)
                .toString();
    }
}
