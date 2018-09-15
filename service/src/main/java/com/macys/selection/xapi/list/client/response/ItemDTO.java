package com.macys.selection.xapi.list.client.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.Date;

@JsonRootName("item")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDTO implements Serializable {

    private static final long serialVersionUID = 5352358339356115280L;

    private Long itemId;
    private String itemGuid;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date addedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date lastModified;
    private Double retailPriceWhenAdded;
    private Integer qtyRequested;
    private String priority;
    private Integer upcId;
    private Integer productId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemGuid() {
        return itemGuid;
    }

    public void setItemGuid(String itemGuid) {
        this.itemGuid = itemGuid;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Double getRetailPriceWhenAdded() {
        return retailPriceWhenAdded;
    }

    public void setRetailPriceWhenAdded(Double retailPriceWhenAdded) {
        this.retailPriceWhenAdded = retailPriceWhenAdded;
    }

    public Integer getQtyRequested() {
        return qtyRequested;
    }

    public void setQtyRequested(Integer qtyRequested) {
        this.qtyRequested = qtyRequested;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getUpcId() {
        return upcId;
    }

    public void setUpcId(Integer upcId) {
        this.upcId = upcId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
