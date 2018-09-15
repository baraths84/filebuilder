package com.macys.selection.xapi.list.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.macys.selection.xapi.list.client.PromotionsRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserPromotions;
import com.macys.selection.xapi.list.data.converters.JsonToPromotionConverter;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.response.WishList;

@Service
public class PromotionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PromotionService.class);

	@Value("${MAX_ITEMS_TO_PROCESS_FOR_PROMOTIONS}")
	private static Integer MAX_ITEMS_TO_PROCESS_FOR_PROMOTIONS = 50;

	@Autowired
	private PromotionsRestClient promotionsRestClient;

	@Autowired
	private JsonResponseParserPromotions promotions;

	@Autowired
	private JsonToPromotionConverter promotionsConverter;

	@Autowired
	private KillSwitchPropertiesBean killswitchPropertiesBean;

	public WishList getPromotions(WishList wishlist, ListQueryParam listQueryParam) {
		WishList resultWishlist = wishlist;
		List<Integer> productIds = getProductIds(wishlist);

		if(CollectionUtils.isNotEmpty(productIds) && killswitchPropertiesBean.isResponsiveWishlistPromotionsEnabled()) {
			try {
				RestResponse promotionResponse = promotionsRestClient.getPromotions(productIds);
				if(null != promotionResponse) {
					String promotionResponseStr = promotionResponse.getBody();
					JsonNode node1 = promotions.parse(promotionResponseStr);
					JsonNode promotionsNode = promotions.readValue(node1);
					resultWishlist = promotionsConverter.convert(wishlist, promotionsNode, listQueryParam);
				}
			} catch (ParseException e) {
				LOGGER.error("expcetion parsing customer json data");
				throw new ListServiceException(e.getMessage(), e);
			} catch (IOException e) {
				LOGGER.error("expcetion reading customer json data");
				throw new ListServiceException(e.getMessage(), e);
			}
		}
		
		return resultWishlist;
	}
	
	public List<Integer> getProductIds(WishList wishlist) {
		List<Integer> productIds = new ArrayList<>();
		if (wishlist != null && CollectionUtils.isNotEmpty(wishlist.getItems())) {

			productIds = wishlist.getItems().stream().limit(MAX_ITEMS_TO_PROCESS_FOR_PROMOTIONS)
								 .map(item -> item.getUpc()).filter(u -> Objects.nonNull(u))
								 .map(upc -> upc.getProduct()).filter(p -> Objects.nonNull(p))
								 .map(product -> product.getId())
								 .collect(Collectors.toList());
		}
		return productIds;
	}

}
	
