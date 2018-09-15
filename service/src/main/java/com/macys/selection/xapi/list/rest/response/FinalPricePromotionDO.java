package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.MoreObjects;

@JsonRootName("finalPricePromotionDO")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"promotionId", "promotionName", "global"})
@JsonInclude(Include.NON_NULL)
public class FinalPricePromotionDO {
	
 	@JsonProperty("promotionId")
	private Integer promotionId;	
 	
 	@JsonProperty("promotionName")
    private String promotionName;
 	
 	@JsonProperty("global")
    private boolean global;

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }
    
    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this).add("promotionId", promotionId)
    		  .add("promotionName", promotionName)
    		  .add("global", global).toString();
    }
}
