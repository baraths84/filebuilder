package com.macys.selection.xapi.list.client;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.selection.xapi.list.common.WishlistConstants;

@Component
public class PromotionsRestClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PromotionsRestClient.class);

	@Autowired
	@Qualifier("promotionsClientPool")
	private RestClientFactory.JaxRSClientPool promotionsClientPool;

	@Value("${application.name}")
	private String applicationName;

	@Value("#{'${application.name}' == 'BCOM'}")
	private boolean isBCOM;
	
	public RestResponse getPromotions(List<Integer> productIds) {
		Response response = null;
		try {
			if(isBCOM){
				response = promotionsClientPool.getRxClient(WishlistConstants.PROMOTIONS_CLIENT_POOL)
						.target(promotionsClientPool.getHostName())
						.path(promotionsClientPool.getBasePath())
						.path(WishlistConstants.PROMOTIONS_PATH)
						.resolveTemplate(WishlistConstants.PRODUCT_IDS, productIds.toString().replaceAll("\\[|\\s|\\]", ""))
						.queryParam(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, WishlistConstants.PROMOTION_QUERY_PARAMS)
						.request(MediaType.APPLICATION_JSON_TYPE)
						.get(Response.class);
			}else {
				response = promotionsClientPool.getRxClient(WishlistConstants.PROMOTIONS_CLIENT_POOL)
						.target(promotionsClientPool.getHostName())
						.path(promotionsClientPool.getBasePath())
						.path(WishlistConstants.PROMOTIONS_PATH)
						.resolveTemplate(WishlistConstants.PRODUCT_IDS, productIds.toString().replaceAll("\\[|\\s|\\]", ""))
						.queryParam(WishlistConstants.PATH_PARAMETER_FILTER_NAME, WishlistConstants.PATH_PARAMETER_FILTER_VALUE)
						.queryParam(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, WishlistConstants.PATH_PARAMETER_FIELDS_VALUE)
						.request(MediaType.APPLICATION_JSON_TYPE)
						.get(Response.class);
			}

			if(null != response && response.getStatus() != WishlistConstants.STATUS_SUCCESS) {
				LOGGER.error("exception retrieving promotions for productids {} with responseStatus {} ", productIds.toString(), response.getStatus());
		      }
		      return RestResponseBuilder.buildFromResponse(response);
		} catch (Exception e) {
			LOGGER.error("exception retrieving promotions", e);  
		} finally {
			if (null != response) {
				response.close();
			}
		}
		return null;
	}
	

}
