package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CollaborativeItem implements Serializable {

    private static final long serialVersionUID = 1221938885181312969L;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("itemGuid")
    private String itemGuid;

    @JsonProperty("retailPriceWhenAdded")
    private Double retailPriceWhenAdded;

    @JsonProperty("retailPriceDropAfterAddedToList")
    private Double retailPriceDropAfterAddedToList;

    @JsonProperty("retailPriceDropPercentage")
    private Integer retailPriceDropPercentage;

    @JsonProperty("qtyRequested")
    private Integer qtyRequested;

    @JsonProperty("qtyStillNeeded")
    private Integer qtyStillNeeded;

    @JsonProperty("priority")
    private String priority;

    @JsonProperty("upc")
    private Upc upc;

    @JsonProperty("product")
    private Product product;

    @JsonProperty("promotions")
    private List<Promotion> promotions;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonProperty("addedDate")
    private Date addedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("lastModified")
    private Date lastModified;

    @JsonProperty("likes")
    private int likes;

    @JsonProperty("dislikes")
    private int dislikes;

    @JsonProperty("feedback")
    private String feedback;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemGuid() {
        return itemGuid;
    }

    public void setItemGuid(String itemGuid) {
        this.itemGuid = itemGuid;
    }

    public Double getRetailPriceWhenAdded() {
        return retailPriceWhenAdded;
    }

    public void setRetailPriceWhenAdded(Double retailPriceWhenAdded) {
        this.retailPriceWhenAdded = retailPriceWhenAdded;
    }

    public Double getRetailPriceDropAfterAddedToList() {
        return retailPriceDropAfterAddedToList;
    }

    public void setRetailPriceDropAfterAddedToList(Double retailPriceDropAfterAddedToList) {
        this.retailPriceDropAfterAddedToList = retailPriceDropAfterAddedToList;
    }

    public Integer getRetailPriceDropPercentage() {
        return retailPriceDropPercentage;
    }

    public void setRetailPriceDropPercentage(Integer retailPriceDropPercentage) {
        this.retailPriceDropPercentage = retailPriceDropPercentage;
    }

    public Integer getQtyRequested() {
        return qtyRequested;
    }

    public void setQtyRequested(Integer qtyRequested) {
        this.qtyRequested = qtyRequested;
    }

    public Integer getQtyStillNeeded() {
        return qtyStillNeeded;
    }

    public void setQtyStillNeeded(Integer qtyStillNeeded) {
        this.qtyStillNeeded = qtyStillNeeded;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Upc getUpc() {
        return upc;
    }

    public void setUpc(Upc upc) {
        this.upc = upc;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
