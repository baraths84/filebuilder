package com.macys.selection.xapi.list.client;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxClient;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvoker;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.platform.rest.framework.client.exception.RestClientException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class PromotionsRestClientTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PromotionsRestClientTest.class);
	private static final String HOST_NAME = "http://localhost:8080";
    private static final String BASE_PATH = "/product/productids/{productids}";
    
    private static final int SUCCESS_STATUS = 200;
    
    private static final int ERROR_STATUS = 400;
    
    private static final int TWICE = 2;
    
    private static final int PRODUCT_ID1 = 3543075;
    
    private static final int PRODUCT_ID2 = 3675506;
    
    private static final String TEST_RESPONSE_STRING = "{\"ProductUPCPromotionResponse\":{\"productUPCPromotionIDs\":{\"3543075\":{\"productPromotionIDs\":{\"promotionIds\":[19876644]},\"upcPromotionIDs\":{\"36652740\":{\"promotionIds\":[19876644]},\"36652741\":{\"promotionIds\":[19876644]}}},\"3675506\":{\"productPromotionIDs\":{\"promotionIds\":[19876644]},\"upcPromotionIDs\":{\"37218907\":{\"promotionIds\":[19876644]},\"37218908\":{\"promotionIds\":[19876644]}}}},\"promotions\":{\"19873015\":{\"badgeTextAttributeValue\":\"coupon excluded\"},\"19876644\":{\"badgeTextAttributeValue\":\"EXTRA 25% OFF USE: FRESH\"}}}}";

	private static String GET_CLIENT_EXP = "exception while getting clinet:";

	@InjectMocks
	private PromotionsRestClient promotionsRestClient;
	
	@Mock
	private RestClientFactory.JaxRSClientPool promotionsClientPool;

	@Mock
	private RxClient rxClient;
	
	@Mock
	private RxWebTarget rxWebTarget;

	@Mock
	private RxInvoker invoker;

	@Mock
	private RxInvocationBuilder invocationBuilder;
	
	@BeforeMethod
	public void init() {
		MockitoAnnotations.initMocks(this);
		invocationBuilder = Mockito.mock(RxInvocationBuilder.class);
		try {
			when(promotionsClientPool.getRxClient(anyString())).thenReturn(rxClient);
		} catch (RestClientException e) {
			LOGGER.error(GET_CLIENT_EXP, e);
		}
		
		when(promotionsClientPool.getHostName()).thenReturn(HOST_NAME);
		when(promotionsClientPool.getBasePath()).thenReturn(BASE_PATH);
		when(rxWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(rxWebTarget);
		
		when(rxClient.target((String) any())).thenReturn(rxWebTarget);
		when(rxWebTarget.path((String) any())).thenReturn(rxWebTarget);
	}
	
	@Test
	public void testGetPromotions() {
		
		List<Integer> productIds = buildProductIdsTestData();
		when(rxWebTarget.queryParam(anyString(), anyString()))
		.thenReturn(rxWebTarget);
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.get(Response.class)).thenReturn(response);

		when(response.getStatus()).thenReturn(SUCCESS_STATUS);
		String responseBodyString = TEST_RESPONSE_STRING;
		when(response.readEntity(String.class)).thenReturn(responseBodyString);
		
		RestResponse restResponse = promotionsRestClient.getPromotions(productIds);
		assertNotNull(restResponse);
		assertEquals(restResponse.getBody(), responseBodyString);
		
		verify(rxWebTarget, times(TWICE)).queryParam(anyString(), anyString());
	}
	
	@Test
	public void testGetPromotionsServiceErrors() {
		
		List<Integer> productIds = buildProductIdsTestData();
		when(rxWebTarget.queryParam(anyString(), anyString()))
		.thenReturn(rxWebTarget);
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.get(Response.class)).thenReturn(response);

		when(response.getStatus()).thenReturn(ERROR_STATUS);
		RestResponse restResponse = promotionsRestClient.getPromotions(productIds);
		assertNotNull(restResponse);
		
		verify(rxWebTarget, times(TWICE)).queryParam(anyString(), anyString());
		
		
		when(invocationBuilder.get(Response.class)).thenReturn(null);
		RestResponse restResponse1 = promotionsRestClient.getPromotions(productIds);
		assertNull(restResponse1);
	}
	
	@Test
	public void testGetPromotionsServiceException() {
		
		List<Integer> productIds = buildProductIdsTestData();
		when(rxWebTarget.queryParam(anyString(), anyString()))
		.thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.get(Response.class)).thenThrow(new ArrayIndexOutOfBoundsException());

		RestResponse restResponse = promotionsRestClient.getPromotions(productIds);
		assertNull(restResponse);
		
		verify(rxWebTarget, times(TWICE)).queryParam(anyString(), anyString());
	}
	
	private List<Integer> buildProductIdsTestData(){
		List<Integer> productIds = new ArrayList<>();
		productIds.add(PRODUCT_ID1);
		productIds.add(PRODUCT_ID2);
		
		return productIds;
	}

	@Test
	public void testGetPromotions_BCOM() {
		ReflectionTestUtils.setField(promotionsRestClient, "applicationName", "BCOM");
		ReflectionTestUtils.setField(promotionsRestClient, "isBCOM", true);

		List<Integer> productIds = buildProductIdsTestData();
		when(rxWebTarget.queryParam(anyString(), anyString()))
				.thenReturn(rxWebTarget);
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.get(Response.class)).thenReturn(response);

		when(response.getStatus()).thenReturn(SUCCESS_STATUS);
		String responseBodyString = TEST_RESPONSE_STRING;
		when(response.readEntity(String.class)).thenReturn(responseBodyString);

		RestResponse restResponse = promotionsRestClient.getPromotions(productIds);
		assertNotNull(restResponse);
		assertEquals(restResponse.getBody(), responseBodyString);

	}

}
