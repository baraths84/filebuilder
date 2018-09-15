package com.macys.selection.xapi.list.data.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.response.FinalPricePromotionDO;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Promotion;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonToPromotionConverter {

    @Value("${application.name}")
    private String applicationName;

    @Value("#{'${application.name}' == 'BCOM'}")
    private boolean isBCOM;

    @Autowired
    private KillSwitchPropertiesBean killswitchPropertiesBean;

    public KillSwitchPropertiesBean getKillswitchPropertiesBean() {
        return killswitchPropertiesBean;
    }

    public void setKillswitchPropertiesBean(KillSwitchPropertiesBean killswitchPropertiesBean) {
        this.killswitchPropertiesBean = killswitchPropertiesBean;
    }

    public WishList convert(WishList wishlist, JsonNode node, ListQueryParam listQueryParam) throws JsonProcessingException, ParseException {
    if(null == node) {
      return wishlist;
    }
	
    //mapping promotions manually based on upc/product
    if(wishlist != null && CollectionUtils.isNotEmpty(wishlist.getItems())) {

    	wishlist.getItems().forEach(item-> {
    		if(item != null
					&& item.getUpc() != null
					&& item.getUpc().getProduct() != null
					&& item.getUpc().getProduct().getId() != null) {

    			JsonNode productUPCPromotionIDs = node.get("productUPCPromotionIDs");
    			JsonNode promotions = node.get("promotions");
    			List<Promotion> promotionsList = new ArrayList<>();

    			ArrayNode  promotionIds = getPromotionsIds(item, productUPCPromotionIDs);
    			//map promotions
                if(isBCOM){
                    promotionsList = mapPromotionsForBcom(promotionIds, promotions);
                }else{
                    promotionsList = mapPromotions(promotionIds, promotions);
                }
    			//Order the Badges in the order of finalPrice promotion order(Percent off promotions display first
                if (killswitchPropertiesBean.isFinalPriceDisplayEnabled()) {
                    List<Long> finalPromotionList = getFinalPricePromoIdList(item);

                    if (CollectionUtils.isNotEmpty(finalPromotionList)) {
                        updatePromotionListOrderByFinalPrice(promotionsList, finalPromotionList, listQueryParam, item);
                    }

                }
    			item.setPromotions(promotionsList);
    		}
		});
    }
    return wishlist;
  }
  
  private ArrayNode getPromotionsIds(Item item, JsonNode productUPCPromotionIDs) {
	  ArrayNode  promotionIds;
	  JsonNode productIdNode = productUPCPromotionIDs != null ? productUPCPromotionIDs.get(item.getUpc().getProduct().getId().toString()) : null;

	  //product level item
          if(item.getProduct() != null) {
            JsonNode productPromotionIDs = productIdNode != null ? productIdNode.get("productPromotionIDs") : null;
            promotionIds = productPromotionIDs != null ? (ArrayNode) productPromotionIDs.get("promotionIds") : null;

              } else {
                  if (isBCOM) {
                      if (item.getUpc() != null && item.getUpc().getProduct() != null) {
                          JsonNode productPromotionIDs = productIdNode != null ? productIdNode.get("productPromotionIDs") : null;
                          promotionIds = productPromotionIDs != null ? (ArrayNode) productPromotionIDs.get("promotionIds") : null;
                      } else {
                          promotionIds = getUPCPromotionIds(productIdNode,item);
                          }
                  } else {
                      //upc level item
                      promotionIds = getUPCPromotionIds(productIdNode,item);}
              }

	  return promotionIds;
  }

  public ArrayNode getUPCPromotionIds(JsonNode productIdNode,Item item){
      ArrayNode  promotionIds;
      JsonNode upcPromotionIDs = productIdNode != null ? productIdNode.get("upcPromotionIDs") : null;
      JsonNode singleUpcPromotionID = upcPromotionIDs != null ? upcPromotionIDs.get(item.getUpc().getId().toString()) : null;
      promotionIds = singleUpcPromotionID != null ? (ArrayNode) singleUpcPromotionID.get("promotionIds") : null;
      return promotionIds;
  }
  private List<Promotion> mapPromotions(ArrayNode  promotionIds, JsonNode promotions) {
	  Promotion promotionBO = null;
	  List<Promotion> promotionsList = new ArrayList<>();
	  if(promotionIds != null) {
		  for(JsonNode promotionId: promotionIds) {
				JsonNode promotionIdNode = promotions !=null ? promotions.get(promotionId.asText()) : null;
                    if (promotionIdNode != null && promotionIdNode.get("badgeTextAttributeValue") != null) {
                        promotionBO = new Promotion();
                        promotionBO.setPromotionId(promotionId.asLong());
                        promotionBO.setBadgeTextAttributeValue(promotionIdNode.get("badgeTextAttributeValue").asText());
                        promotionsList.add(promotionBO);
                    }
                }
			}
	  return promotionsList;
  }

    private List<Promotion> mapPromotionsForBcom(ArrayNode  promotionIds, JsonNode promotions) {
        Promotion promotionBO = null;
        List<Promotion> promotionsList = new ArrayList<>();
        List<Promotion> loyaltyPromotions = new ArrayList<>();
        if(promotionIds != null) {
            for (JsonNode promotionId : promotionIds) {
                JsonNode promotionIdNode = promotions != null ? promotions.get(promotionId.asText()) : null;
                if (promotionIdNode != null) {
                    promotionBO = new Promotion();
                    promotionBO.setPromotionId(promotionId.asLong());

                    //for Loyalty promotions need to set the OFFER_DESCRIPTION value from promotion attributes to badgeTextAttributeValue
                    if (promotionIdNode.get(WishlistConstants.PROMOTION_TYPE) != null &&
                            WishlistConstants.LOYALTY_OFFER_FIXED.equals(promotionIdNode.get(WishlistConstants.PROMOTION_TYPE).asText()) ||
                            WishlistConstants.LOYALTY_OFFER_MULTIPLIER.equals(promotionIdNode.get(WishlistConstants.PROMOTION_TYPE).asText())) {
                        ArrayNode promotionAttribteNode = (ArrayNode) promotionIdNode.get(WishlistConstants.PROMOTION_ATTRIBUTE);
                        if (promotionAttribteNode != null) {
                            for (JsonNode node : promotionAttribteNode) {
                                if (node != null && node.get(WishlistConstants.PROMOTION_ATTRIBUTE_NAME) != null &&
                                        WishlistConstants.OFFER_DESCRIPTION.equals(node.get(WishlistConstants.PROMOTION_ATTRIBUTE_NAME).asText())) {
                                    ArrayNode attributeValueArray = (ArrayNode) node.get(WishlistConstants.PROMOTION_ATTRIBUTE_VALUE);
                                    if (attributeValueArray != null) {
                                        for (JsonNode attributeVal : attributeValueArray) {
                                            if (attributeVal != null && attributeVal.get(WishlistConstants.PROMOTION_ATTRIBUTE_NAME) != null &&
                                                    WishlistConstants.OFFER_DESCRIPTION.equals(attributeVal.get(WishlistConstants.PROMOTION_ATTRIBUTE_NAME).asText())
                                                    && attributeVal.get(WishlistConstants.PROMOTION_ATTRIBUTE_VALUE) != null) {
                                                promotionBO.setBadgeTextAttributeValue(attributeVal.get(WishlistConstants.PROMOTION_ATTRIBUTE_VALUE).asText());
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(promotionBO.getBadgeTextAttributeValue())) {
                            loyaltyPromotions.add(promotionBO);
                        }
                    } else {
                        //check offerDescription is not empty then only add promotions to the list
                        if (StringUtils.isNotBlank(promotionIdNode.get(WishlistConstants.PROMOTION_OFFER_DESCRIPTION).asText())) {
                            promotionBO.setBadgeTextAttributeValue(promotionIdNode.get(WishlistConstants.PROMOTION_OFFER_DESCRIPTION).asText());
                            promotionsList.add(promotionBO);
                        }
                    }
                }
            }

            //Bcom wants to add loyalty promotions at the end of promotions list
            promotionsList.addAll(loyaltyPromotions);
        }
        return promotionsList;
    }
    /**
     * Method to get List of PromotionIds from finalPrice response
     * @param wishListItem
     * @return
     */
    private List<Long> getFinalPricePromoIdList(Item wishListItem) {

        List<Long> finalPricePromotions = new ArrayList<>();

        List<FinalPricePromotionDO> finalPricePromotionDOS = null;

        if (wishListItem.getProduct() != null && wishListItem.getProduct().getFinalPrice() != null) {
            // Produce level finalPrice available
            finalPricePromotionDOS = wishListItem.getProduct().getFinalPrice().getPromotions();

        } else if (wishListItem.getUpc() != null && wishListItem.getUpc().getFinalPrice() != null) {
            // UPC level finalPrice available
            finalPricePromotionDOS = wishListItem.getUpc().getFinalPrice().getPromotions();
        }

        if (CollectionUtils.isNotEmpty(finalPricePromotionDOS)) {
            for (FinalPricePromotionDO finalPricePromotionDO : finalPricePromotionDOS) {
                finalPricePromotions.add(Long.valueOf(finalPricePromotionDO.getPromotionId()));
            }
        }

        return finalPricePromotions;

    }


    /**
     * Method to update order of Item's promotion badges based on the order of finalPrice promotions
     * @param promotionsList
     * @param finalPricePromotionList
     */
    private void updatePromotionListOrderByFinalPrice(List<Promotion> promotionsList, List<Long> finalPricePromotionList, ListQueryParam listQueryParam, Item wishlist) {

        List<Promotion> finalPriceMatchingPromotions = new ArrayList<>();
        int i=1, j=1;
        if (CollectionUtils.isNotEmpty(finalPricePromotionList)) {
            for (Long finalPricePromoId : finalPricePromotionList) {
                for (Promotion promotion : promotionsList) {
                    if (promotion.getPromotionId().equals(finalPricePromoId)) {
                        finalPriceMatchingPromotions.add(promotion);
                        //if the user is not signed in and product type promotion is MASK set badge type text as see bag for price and finalprice to null
                        if (listQueryParam != null && listQueryParam.getCustomerState() != null && !WishlistConstants.SIGNED_IN.equalsIgnoreCase(listQueryParam.getCustomerState()) && !WishlistConstants.RECOGNIZED.equalsIgnoreCase(listQueryParam.getCustomerState())) {
                            //for upc item
                            if (wishlist.getUpc() != null && wishlist.getUpc().getFinalPrice() != null && WishlistConstants.FINAL_PRICE_CONDITIONAL_SHOW.equals(wishlist.getUpc().getFinalPrice().getDisplayFinalPrice())) {
                                if (WishlistConstants.MASK.equals(wishlist.getUpc().getFinalPrice().getProductTypePromotion()))
                                    promotion.setBadgeTextAttributeValue(WishlistConstants.FINAL_PRICE_GUEST_BADGE_TEXT);
                                //checking if the iteration is at last index of final price promotion list before setting Final price to null
                                if (i++ == finalPricePromotionList.size()) {
                                    wishlist.getUpc().setFinalPrice(null);
                                }
                            }

                            //for product item
                            if (wishlist.getProduct() != null && wishlist.getProduct().getFinalPrice() != null && WishlistConstants.FINAL_PRICE_CONDITIONAL_SHOW.equals(wishlist.getProduct().getFinalPrice().getDisplayFinalPrice())) {
                                if (WishlistConstants.MASK.equals(wishlist.getProduct().getFinalPrice().getProductTypePromotion()))
                                    promotion.setBadgeTextAttributeValue(WishlistConstants.FINAL_PRICE_GUEST_BADGE_TEXT);
                                //checking if the iteration is at last index of final price promotion list before setting Final price to null
                                if (j++ == finalPricePromotionList.size()) {
                                    wishlist.getProduct().setFinalPrice(null);
                                }
                            }
                        }

                    }
                }
            }

            if (CollectionUtils.isNotEmpty(finalPriceMatchingPromotions)) {
                for (Promotion finalPriceMatchingpromotion : finalPriceMatchingPromotions) {
                    promotionsList.remove(finalPriceMatchingpromotion);
                }
                promotionsList.addAll(0, finalPriceMatchingPromotions);
            }
        }

    }



}
