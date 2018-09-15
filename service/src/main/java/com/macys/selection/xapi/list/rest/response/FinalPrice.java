package com.macys.selection.xapi.list.rest.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.MoreObjects;

/**
 * @author M835785
 *
 */
@JsonRootName("finalPrice")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"finalPrice", "finalPriceHigh", "displayFinalPrice", "productTypePromotion", "promotions"})
@JsonInclude(Include.NON_NULL)
public class FinalPrice {
	
	 	@JsonProperty("finalPrice")
	 	private double finalPrice;
	 	
	 	@JsonProperty("finalPriceHigh")
	    private double finalPriceHigh;

	 	@JsonProperty("displayFinalPrice")
	    private String displayFinalPrice;

	 	@JsonProperty("productTypePromotion")
	    private String productTypePromotion;

	 	@JsonProperty("promotions")
	    private List<FinalPricePromotionDO> promotions;


	    public double getFinalPrice() {
	        return finalPrice;
	    }

	    public void setFinalPrice(double finalPrice) {
	        this.finalPrice = finalPrice;
	    }

	    public double getFinalPriceHigh() {
	        return finalPriceHigh;
	    }

	    public void setFinalPriceHigh(double finalPriceHigh) {
	        this.finalPriceHigh = finalPriceHigh;
	    }

	    public String getDisplayFinalPrice() {
	        return displayFinalPrice;
	    }

	    public void setDisplayFinalPrice(String displayFinalPrice) {
	        this.displayFinalPrice = displayFinalPrice;
	    }

	    public String getProductTypePromotion() {
	        return productTypePromotion;
	    }

	    public void setProductTypePromotion(String productTypePromotion) {
	        this.productTypePromotion = productTypePromotion;
	    }

	    public List<FinalPricePromotionDO> getPromotions() {
	        return promotions;
	    }

	    public void setPromotions(List<FinalPricePromotionDO> promotions) {
	        this.promotions = promotions;
	    }
	    
	    @Override
	    public String toString() {
	      return MoreObjects.toStringHelper(this).add("finalPrice", finalPrice)
	    		  .add("finalPriceHigh", finalPriceHigh)
	    		  .add("displayFinalPrice", displayFinalPrice)
	    		  .add("productTypePromotion", productTypePromotion)
	    		  .add("promotions", promotions).toString();
	    }

}
