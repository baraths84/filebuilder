package com.macys.selection.xapi.list.client;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxClient;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocation;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvoker;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.platform.rest.framework.client.exception.RestClientException;
import com.macys.selection.xapi.list.TestUtils;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.client.request.CustomerListMerge;
import com.macys.selection.xapi.list.client.response.ItemDTO;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.util.CustomerQueryParameterEnum;
import com.macys.selection.xapi.list.util.CustomerRequestParamUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CustomerListRestClientTest {
	
	private static String LOCAL_HOST = "http://localhost:8080";
	private static String BASH_PATH = "/xapi/v1/lists/";
	private static String RESPONSE_BODY_STRING = "responseBodyString";
	private static String WISH_LIST_JSON_FILE = "com/macys/selection/xapi/wishlist/converters/wishlist.json";
	private static String GET_ALL_LISTS_ERROR_FILE = "com/macys/selection/xapi/wishlist/converters/getAllLists_Error.json";
	private static String MOVE_ITEM_ERROR_FILE = "com/macys/selection/xapi/wishlist/converters/moveItem_Error.json";
	private static String TESTING_GUID = "testingGuid";
	private static String TEST_LIST_GUID = "testListGuid";
	private static String TEST_SECURITY_TOKEN = "testing security token";
	private static String FAILED = "Failed";
	private static String SUCCESS = "Success";
	private static String ABC_LIST_GUID = "abc";
	private static String TESTING_ITEM_GUID = "testingItemGuid";
	private static String DEF_ITEM_GUID = "def";
	private static final Long USER_ID = 1234L;
	private static String USER_GUID = "userGuid";
	private static String DUPLICATE_LIST_NAME_FOUND = "Duplicate list name found";
	private static String EMAIL_SHARE_EXCEPTION = "Exception in email sharing wishlist: ";
	private static final int UPC_ID = 5678;
	private static final Long UPC_NUMBER = 89999L;
	private static final int QTY = 1;
	private static String EXPAND_PARAM_VALUE = "user,items";
	private static String USER_EXPAND_ONLY = "user";
	private static final int TIME_1 = 1;
	private static final int TIME_2 = 2;
	private static final int TIME_3 = 3;
	private static final int TIME_4 = 4;
	private static String GET_CLIENT_EXP = "exception while getting clinet:";
	private static final String SECURE_TOKEN_HEADER = "X-Macys-SecurityToken";

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerListRestClientTest.class);

	@Mock
	private RestClientFactory.JaxRSClientPool customerListClientPool;
	@Mock
	private RestClientFactory.JaxRSClientPool wishlistClientPool;

	@Mock
	private RxClient rxClient;
	@Mock
	private RxWebTarget rxWebTarget;

	@Mock
	private RxInvoker invoker;
	@Mock
	private RxInvocation rxInvocation;

	@Mock
	private CustomerRequestParamUtil requestParamUtil;

	@InjectMocks
	private CustomerServiceRestClient listRestClient;
	private RxInvocationBuilder invocationBuilder;
	private UserQueryParam userQueryParam;
	private ListQueryParam listQueryParam;
	private PaginationQueryParam paginationQueryParam;

	@BeforeMethod
	public void init() {
		MockitoAnnotations.initMocks(this);
		invocationBuilder = Mockito.mock(RxInvocationBuilder.class);
		userQueryParam = getUserQueryParam();
		listQueryParam = getListQueryParam();
		paginationQueryParam = new PaginationQueryParam();

		when(customerListClientPool.getHostName()).thenReturn(LOCAL_HOST);
		when(customerListClientPool.getBasePath()).thenReturn(BASH_PATH);

		when(wishlistClientPool.getHostName()).thenReturn(LOCAL_HOST);
		when(wishlistClientPool.getBasePath()).thenReturn(BASH_PATH);
		try {
			when(customerListClientPool.getRxClient((String) any())).thenReturn(rxClient);
			when(wishlistClientPool.getRxClient((String) any())).thenReturn(rxClient);
		} catch (RestClientException e) {
			LOGGER.error(GET_CLIENT_EXP, e);
		}

		when(rxClient.target((String) any())).thenReturn(rxWebTarget);
		when(rxWebTarget.path((String) any())).thenReturn(rxWebTarget);

	}

	@Test
	public void getListTest() {
		when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.get(Response.class)).thenReturn(response);

		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		String responseBodyString = RESPONSE_BODY_STRING;
		when(response.readEntity(String.class)).thenReturn(responseBodyString);
		RestResponse restResponse = listRestClient.get(userQueryParam, listQueryParam, paginationQueryParam);
		assertNotNull(restResponse);
		verify(requestParamUtil, times(TIME_1)).createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);
		verify(rxWebTarget, times(TIME_1)).path((String) any());
		verify(rxWebTarget, times(TIME_1)).request(MediaType.APPLICATION_JSON_TYPE);
	}

	@Test
	public void testGetAllLists() throws IOException {
		when(requestParamUtil.createGetListQueryParamMap(userQueryParam,listQueryParam, paginationQueryParam)).thenReturn(getListRequestParamMap());
		
		when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.get(Response.class)).thenReturn(response);

		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		String responseBodyString = TestUtils.readFile(WISH_LIST_JSON_FILE);
		when(response.readEntity(String.class)).thenReturn(responseBodyString);
		RestResponse restResponse = listRestClient.get(userQueryParam, listQueryParam, paginationQueryParam);
		assertNotNull(restResponse);
		verify(requestParamUtil, times(TIME_1)).createGetListQueryParamMap(
				userQueryParam, listQueryParam, paginationQueryParam);
		verify(rxWebTarget, times(TIME_1)).path((String) any());
		verify(rxWebTarget, times(TIME_4)).queryParam(anyString(), anyString());
		verify(rxWebTarget, times(TIME_1)).request(MediaType.APPLICATION_JSON_TYPE);
	}
	
	@Test
	public void testGetAllListsWithOutUserID() throws IOException {
		when(requestParamUtil.createGetListQueryParamMap(userQueryParam,listQueryParam, paginationQueryParam)).thenReturn(new EnumMap<>(CustomerQueryParameterEnum.class));
		
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.get(Response.class)).thenReturn(response);

		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		String responseBodyString = TestUtils.readFile(GET_ALL_LISTS_ERROR_FILE);
		when(response.readEntity(String.class)).thenReturn(responseBodyString);
		RestResponse restResponse = listRestClient.get(userQueryParam, listQueryParam, paginationQueryParam);
		assertNotNull(restResponse);
		verify(requestParamUtil, times(TIME_1)).createGetListQueryParamMap(
				userQueryParam, listQueryParam, paginationQueryParam);
		verify(rxWebTarget, times(TIME_1)).path((String) any());
		verify(rxWebTarget, times(TIME_1)).request(MediaType.APPLICATION_JSON_TYPE);
	}
	
	@Test(expectedExceptions = RestException.class)
	public void testGetAllListsRestException() throws IOException {
		when(requestParamUtil.createGetListQueryParamMap(userQueryParam,listQueryParam, paginationQueryParam)).thenReturn(getListRequestParamMap());
		
		when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.get(Response.class)).thenThrow(new NullPointerException());

		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		String responseBodyString = TestUtils.readFile(GET_ALL_LISTS_ERROR_FILE);
		when(response.readEntity(String.class)).thenReturn(responseBodyString);
		RestResponse restResponse = listRestClient.get(userQueryParam, listQueryParam, paginationQueryParam);
		assertNotNull(restResponse);
		verify(requestParamUtil, times(TIME_1)).createGetListQueryParamMap(
				userQueryParam, listQueryParam, paginationQueryParam);
		verify(rxWebTarget, times(TIME_1)).path((String) any());
		verify(rxWebTarget, times(TIME_1)).request(MediaType.APPLICATION_JSON_TYPE);
	}

	@Test
	public void testGetListByListGuid() throws IOException {
		when(requestParamUtil.createGetListQueryParamMap(userQueryParam,listQueryParam, paginationQueryParam)).thenReturn(getListRequestParamMap());
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

		String token = anyString();
		when(invocationBuilder.header(any(), token)).thenReturn(invocationBuilder);
		when(invocationBuilder.get(Response.class)).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		String responseBodyString = TestUtils.readFile(WISH_LIST_JSON_FILE);
		when(response.readEntity(String.class)).thenReturn(responseBodyString);
		RestResponse restResponse = listRestClient.getListByGuid(token, TEST_LIST_GUID, userQueryParam, listQueryParam, paginationQueryParam);
		assertNotNull(restResponse);
		verify(requestParamUtil, times(TIME_1)).createGetListQueryParamMap(
				userQueryParam, listQueryParam, paginationQueryParam);
		verify(rxWebTarget, times(TIME_2)).path((String) any());
		verify(rxWebTarget, times(TIME_4)).queryParam(anyString(), anyString());
		verify(rxWebTarget, times(TIME_2)).request(MediaType.APPLICATION_JSON_TYPE);
	}
	
	@Test
	public void testGetListByListGuidServiceError() throws IOException {
		when(requestParamUtil.createGetListQueryParamMap(userQueryParam,listQueryParam, paginationQueryParam)).thenReturn(new EnumMap<>(CustomerQueryParameterEnum.class));
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(anyString(), anyString()))
		.thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

		String token = anyString();
		when(invocationBuilder.header(any(), token)).thenReturn(invocationBuilder);
		when(invocationBuilder.get(Response.class)).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		String responseBodyString = TestUtils.readFile(GET_ALL_LISTS_ERROR_FILE);
		when(response.readEntity(String.class)).thenReturn(responseBodyString);
		RestResponse restResponse = listRestClient.getListByGuid(token, TEST_LIST_GUID, userQueryParam, listQueryParam, paginationQueryParam);
		assertNotNull(restResponse);
		verify(requestParamUtil, times(TIME_1)).createGetListQueryParamMap(
				userQueryParam, listQueryParam, paginationQueryParam);
		verify(rxWebTarget, times(TIME_2)).path((String) any());
		verify(rxWebTarget, times(TIME_2)).request(MediaType.APPLICATION_JSON_TYPE);
	}
	
	@Test(expectedExceptions = RestException.class)
	public void testGetListByListGuidException() throws IOException {
		when(requestParamUtil.createGetListQueryParamMap(userQueryParam,listQueryParam, paginationQueryParam)).thenReturn(new EnumMap<>(CustomerQueryParameterEnum.class));
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(anyString(), anyString()))
		.thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.get(Response.class)).thenThrow(new NullPointerException());
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		String responseBodyString = TestUtils.readFile(GET_ALL_LISTS_ERROR_FILE);
		when(response.readEntity(String.class)).thenReturn(responseBodyString);
		RestResponse restResponse = listRestClient.getListByGuid(null, TEST_LIST_GUID, userQueryParam, listQueryParam, paginationQueryParam);
		assertNotNull(restResponse);
		verify(requestParamUtil, times(TIME_1)).createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);
		verify(rxWebTarget, times(TIME_2)).path((String) any());
		verify(rxWebTarget, times(TIME_1)).request(MediaType.APPLICATION_JSON_TYPE);
	}

	private Map<CustomerQueryParameterEnum, String> getListRequestParamMap() {
		Map<CustomerQueryParameterEnum, String> listQueryParamMap = new EnumMap<>(
				CustomerQueryParameterEnum.class);
		listQueryParamMap.put(CustomerQueryParameterEnum.USERID,
				String.valueOf(USER_ID));
		listQueryParamMap.put(CustomerQueryParameterEnum.DEFAULT,
				String.valueOf(true));
		listQueryParamMap.put(CustomerQueryParameterEnum.LISTTYPE, WishlistConstants.WISH_LIST_TYPE_VALUE);
		listQueryParamMap.put(CustomerQueryParameterEnum.EXPAND, USER_EXPAND_ONLY);
		return listQueryParamMap;
	}

	private UserQueryParam getUserQueryParam() {
		UserQueryParam sampleUserQueryParam = new UserQueryParam();
		sampleUserQueryParam.setUserId(USER_ID);
		return sampleUserQueryParam;
	}

	private ListQueryParam getListQueryParam() {
		ListQueryParam sampleListQueryParam = new ListQueryParam();
		sampleListQueryParam.setDefaultList(true);
		sampleListQueryParam.setListGuid(TEST_LIST_GUID);
		sampleListQueryParam.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
		sampleListQueryParam.setExpand(EXPAND_PARAM_VALUE);
		return sampleListQueryParam;
	}
	
	private CustomerList getTestingCustomerList() {
		CustomerList customerList = new CustomerList();
		List<WishList> wishlistList = new ArrayList<WishList>();
		WishList testingWishList = new WishList();
		Item testingItem = new Item();
		testingItem.setItemGuid(TESTING_ITEM_GUID);
		List<Item> itemList = new ArrayList<Item>();
		itemList.add(testingItem);
		testingWishList.setItems(itemList);
		wishlistList.add(testingWishList);
		customerList.setWishlist(wishlistList);
		
		User testingUser = new User();
		testingUser.setId(USER_ID);
		customerList.setUser(testingUser);
		
		return customerList;
	}

	@Test(expectedExceptions = RestException.class)
	public void getWishlistByGuidOnException() throws RestClientException {

		RestClientException exception = new RestClientException(FAILED, new NullPointerException());

		when(customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL)).thenThrow(exception);

		listRestClient.getListByGuid(null, TEST_LIST_GUID, new UserQueryParam(), new ListQueryParam(), new PaginationQueryParam());
	}

	@Test(expectedExceptions = RestException.class)
	public void getWishlistsByUserIdOnException() throws RestClientException {

		RestClientException exception = new RestClientException(FAILED, new NullPointerException());

		when(customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL)).thenThrow(exception);

		listRestClient.get(new UserQueryParam(), new ListQueryParam(), new PaginationQueryParam());

	}


	@Test
	public void moveItemToWishlist() {

		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);

		String token = anyString();
		when(invocationBuilder.header(any(), token)).thenReturn(invocationBuilder);
		when(invocationBuilder.post(null)).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

		listRestClient.moveItemToWishlist(token, TESTING_ITEM_GUID, getTestingCustomerList());
		verify(rxClient, times(TIME_1)).target(anyString());
		verify(rxWebTarget, times(TIME_2)).path(anyString());
		verify(rxWebTarget, times(TIME_3)).queryParam(anyString(), any());
		verify(rxWebTarget, times(TIME_1)).request();
	}
	
	@Test
	public void testMoveItemToWishlistWithIncorrectRequest() throws IOException {
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);

		String token = anyString();
		when(invocationBuilder.header(any(), token)).thenReturn(invocationBuilder);
		when(invocationBuilder.post(null)).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		String responseBodyString = TestUtils.readFile(MOVE_ITEM_ERROR_FILE);
		when(response.readEntity(String.class)).thenReturn(responseBodyString);

		listRestClient.moveItemToWishlist(token, ABC_LIST_GUID, getTestingCustomerList());
		verify(rxClient, times(TIME_1)).target(anyString());
		verify(rxWebTarget, times(TIME_2)).path(anyString());
		verify(rxWebTarget, times(TIME_3)).queryParam(anyString(), anyString());
		verify(rxWebTarget, times(TIME_1)).request();

	}
	
	@Test(expectedExceptions = RestException.class)
	public void testMoveItemToWishlistException() throws IOException {
		when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.post(null)).thenThrow(new NullPointerException());

		listRestClient.moveItemToWishlist(any(), ABC_LIST_GUID, getTestingCustomerList());
		verify(rxClient, times(TIME_1)).target(anyString());
		verify(rxWebTarget, times(TIME_2)).path(anyString());
		verify(rxWebTarget, times(TIME_3)).queryParam(anyString(), anyString());
		verify(rxWebTarget, times(TIME_1)).request();

	}

    @Test
	public void testDeleteItem() {
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(anyString(), anyString()))
				.thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);

		String token = "xxx-token";
		when(invocationBuilder.header(SECURE_TOKEN_HEADER, token)).thenReturn(invocationBuilder);
		when(invocationBuilder.delete()).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.NO_CONTENT.getStatusCode());

		listRestClient.delete(token, ABC_LIST_GUID, DEF_ITEM_GUID);
		verify(rxClient, times(TIME_1)).target((String) any());
		verify(rxWebTarget, times(TIME_1)).request();
	}

	@Test
	public void testDeleteItemWithIncorrectRequest() {
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(anyString(), anyString()))
				.thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);

		String token = "xxx-token";
		when(invocationBuilder.header(SECURE_TOKEN_HEADER, token)).thenReturn(invocationBuilder);
		when(invocationBuilder.delete()).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());

		listRestClient.delete(token, ABC_LIST_GUID, DEF_ITEM_GUID);
		verify(rxClient, times(TIME_1)).target((String) any());
		verify(rxWebTarget, times(TIME_1)).request();
	}

	@Test(expectedExceptions = RestException.class)
	public void testDeleteItemException() {

		when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);

		String token = "xxx-token";
		when(invocationBuilder.header(SECURE_TOKEN_HEADER, token)).thenReturn(invocationBuilder);
		when(invocationBuilder.delete())
				.thenThrow(new ArrayIndexOutOfBoundsException());

		listRestClient.delete(token, ABC_LIST_GUID, DEF_ITEM_GUID);
		verify(rxClient, times(TIME_1)).target((String) any());
		verify(rxWebTarget, times(TIME_1)).request();
	}
	
	@Test
	public void testUpdateWishlist() throws IOException {
		Map<CustomerQueryParameterEnum, String> listQueryParamMap = new EnumMap<>(CustomerQueryParameterEnum.class);
		listQueryParamMap.put(CustomerQueryParameterEnum.USERID, String.valueOf(USER_ID));
    	
		when(requestParamUtil.createGetListQueryParamMap(userQueryParam,null, null)).thenReturn(listQueryParamMap);
		
		Response response = Mockito.mock(Response.class);
		ListCookies cookies = new ListCookies(10L, "xxx-guid", "xxx-token");

		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.header(SECURE_TOKEN_HEADER, cookies.getToken())).thenReturn(invocationBuilder);
		when(invocationBuilder.method(any(), Entity.text(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		CustomerList updateWishlistJson = new CustomerList();
		RestResponse restResponse = listRestClient.updateWishlist(cookies, TEST_LIST_GUID, userQueryParam, updateWishlistJson);
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.OK.getStatusCode());
		verify(rxWebTarget, times(TIME_1)).queryParam(anyString(), anyString());
		
	}
	
	@Test
	public void testUpdateWishlistServiceError() throws IOException {
    	
		when(requestParamUtil.createGetListQueryParamMap(userQueryParam,null, null)).thenReturn(new EnumMap<>(CustomerQueryParameterEnum.class));
		
		Response response = Mockito.mock(Response.class);
		ListCookies cookies = new ListCookies(10L, "xxx-guid", "xxx-token");

		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.header(SECURE_TOKEN_HEADER, cookies.getToken())).thenReturn(invocationBuilder);
		when(invocationBuilder.method(any(), Entity.text(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		CustomerList updateWishlistJson = new CustomerList();
		RestResponse restResponse = listRestClient.updateWishlist(cookies, TEST_LIST_GUID, userQueryParam, updateWishlistJson);
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
		
	}
	
	@Test(expectedExceptions = RestException.class)
	public void testUpdateWishlistException() throws IOException {

		ListCookies cookies = new ListCookies(anyLong(), anyString(), anyString());

		when(requestParamUtil.createGetListQueryParamMap(userQueryParam,null, null)).thenReturn(new EnumMap<>(CustomerQueryParameterEnum.class));
		Response response = Mockito.mock(Response.class);
		
		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.header(SECURE_TOKEN_HEADER, cookies.getToken())).thenReturn(invocationBuilder);
		when(invocationBuilder.method(any(), Entity.text(any()))).thenThrow(new NullPointerException());
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		CustomerList updateWishlistJson = new CustomerList();
		RestResponse restResponse = listRestClient.updateWishlist(cookies, TEST_LIST_GUID, userQueryParam, updateWishlistJson);
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
		
	}
	
	@Test
	public void createListByUserIdTest() {
		String TOKEN_VALUE = "xxx-token";

		Response response = Mockito.mock(Response.class);
		
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.header(SECURE_TOKEN_HEADER, TOKEN_VALUE)).thenReturn(invocationBuilder);
		when(invocationBuilder.post(any())).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(SUCCESS);

		CustomerList inputCustomerList = new CustomerList();
		ListCookies cookie = new ListCookies(10L, "xxx-guid", TOKEN_VALUE);
		RestResponse restResponse = listRestClient.createList(cookie, inputCustomerList);
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.OK.getStatusCode());
		assertTrue(SUCCESS.equals(restResponse.getBody()));
	}
	
	@Test
	public void createListFailOnDuplicateNameTest() {
		String TOKEN_VALUE = "xxx-token";

		Response response = Mockito.mock(Response.class);
		
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.header(SECURE_TOKEN_HEADER, TOKEN_VALUE)).thenReturn(invocationBuilder);
		when(invocationBuilder.post(any())).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(DUPLICATE_LIST_NAME_FOUND);

		CustomerList inputCustomerList = new CustomerList();
		ListCookies cookie = new ListCookies(10L, "xxx-guid", TOKEN_VALUE);
		RestResponse restResponse = listRestClient.createList(cookie, inputCustomerList);
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
		assertTrue(DUPLICATE_LIST_NAME_FOUND.equals(restResponse.getBody()));
		
	}
	
	@Test(expectedExceptions = RestException.class)
	public void createListThrowExceptionTest() {
		String TOKEN_VALUE = "xxx-token";

		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.header(SECURE_TOKEN_HEADER, TOKEN_VALUE)).thenReturn(invocationBuilder);
		when(invocationBuilder.post(any())).thenReturn(null);
		CustomerList inputCustomerList = new CustomerList();
		ListCookies cookie = new ListCookies(10L, "xxx-guid", TOKEN_VALUE);
		listRestClient.createList(cookie, inputCustomerList);
	}  
    
//	@Test(expectedExceptions = RestException.class)
	@Test
	public void shareEmailWishListOnResponseErrorTest() {
		ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");

		Response response = Mockito.mock(Response.class);
		
		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.header(any(), Entity.text(any()))).thenReturn(invocationBuilder);
		when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(FAILED);
		
		RestResponse restResponse = listRestClient.emailShareWishlist(cookie, TEST_LIST_GUID, new EmailShare());
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
		assertTrue(FAILED.equals(restResponse.getBody()));
	}
    
    @Test
	public void shareEmailWishListTest() {
		ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");

		Response response = Mockito.mock(Response.class);
		
		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.header(any(), Entity.text(any()))).thenReturn(invocationBuilder);
		when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(SUCCESS);
		RestResponse restResponse = listRestClient.emailShareWishlist(cookie, TEST_LIST_GUID, new EmailShare());
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.OK.getStatusCode());
		assertTrue(SUCCESS.equals(restResponse.getBody()));
		
	}
	
	@Test(expectedExceptions = RestException.class)
	public void shareEmailWishListOnExceptionTest() {
		ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");

		RestException restException = new RestException(EMAIL_SHARE_EXCEPTION, new NullPointerException());
		
		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.post(any())).thenThrow(restException);

		listRestClient.emailShareWishlist(cookie, TEST_LIST_GUID, new EmailShare());

	}

	@Test
	public void addToDefaultWishlistByUpcIDOnResponseErrorTest() {

		Response response = Mockito.mock(Response.class);

		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(FAILED);
		RestResponse restResponse = listRestClient.addToDefaultWishlist(new CustomerList());
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
		assertTrue(FAILED.equals(restResponse.getBody()));

	}

	@Test
	public void addToDefaultWishlistByUpcIDTest() {
		CustomerList customerList = new CustomerList();		
		WishList wishList = new WishList();
		User user = new User();
		user.setId(USER_ID);
		List<Item> itemList = new ArrayList<Item>();
		Item item = new Item();
		item.setQtyRequested(QTY);
		Upc upc = new Upc();
		upc.setId(UPC_ID);
		item.setUpc(upc);
		itemList.add(item);
		wishList.setItems(itemList);
		customerList.setUser(user);
		List<WishList> wishLists = new ArrayList<WishList>();
		wishLists.add(wishList);
		customerList.setWishlist(wishLists);

		Response response = Mockito.mock(Response.class);

		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(SUCCESS);
		RestResponse restResponse = listRestClient.addToDefaultWishlist(customerList);
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.OK.getStatusCode());
		assertTrue(SUCCESS.equals(restResponse.getBody()));

	}

	@Test(expectedExceptions = RestException.class)
	public void addToDefaultWishlistByUpcIDOnExceptionTest() {

		RestException exception = new RestException(FAILED, new NullPointerException());

		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.post(any())).thenThrow(exception);
		listRestClient.addToDefaultWishlist(new CustomerList());
	}
	
	@Test
	public void addToDefaultWishlistByProductIdOnResponseErrorTest() {

		Response response = Mockito.mock(Response.class);

		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(FAILED);
		RestResponse restResponse = listRestClient.addToDefaultWishlist(new CustomerList());
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
		assertTrue(FAILED.equals(restResponse.getBody()));

	}

	@Test
	public void addToDefaultWishlistByProductIdTest() {

		Response response = Mockito.mock(Response.class);

		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(SUCCESS);
		RestResponse restResponse = listRestClient.addToDefaultWishlist(new CustomerList());
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.OK.getStatusCode());
		assertTrue(SUCCESS.equals(restResponse.getBody()));

	}

	@Test(expectedExceptions = RestException.class)
	public void addToDefaultWishlistByProductIdOnExceptionTest() {

		RestException exception = new RestException(FAILED, new NullPointerException());

		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.post(any())).thenThrow(exception);

		listRestClient.addToDefaultWishlist(new CustomerList());
	}
	
	@Test
	public void addItemToGivenListByUPCOnResponseErrorTest() {
		Response response = Mockito.mock(Response.class);

		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.header(any(), any())).thenReturn(invocationBuilder);
		when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(FAILED);

		List<Item> itemList = getTestItemList();
		ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");
		RestResponse restResponse = listRestClient.addItemToGivenListByUPC(cookie, userQueryParam, TESTING_GUID, itemList);
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
		assertTrue(FAILED.equals(restResponse.getBody()));

	}

	@Test
	public void addItemToGivenListByUPCTest() {
		
		Map<CustomerQueryParameterEnum, String> testQueryMap = new EnumMap<>(CustomerQueryParameterEnum.class);
		testQueryMap.put(CustomerQueryParameterEnum.USERID, TESTING_GUID);
		testQueryMap.put(CustomerQueryParameterEnum.USERGUID, TESTING_GUID);

		when(requestParamUtil.createGetListQueryParamMap(userQueryParam, null, null)).thenReturn(testQueryMap);

		Response response = Mockito.mock(Response.class);

		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
		when(invocationBuilder.header(any(), any())).thenReturn(invocationBuilder);
		when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(SUCCESS);
		List<Item> itemList = getTestItemList();
		ListCookies cookie = new ListCookies(10L, "xxx-guid","xxx-token");
		RestResponse restResponse = listRestClient.addItemToGivenListByUPC(cookie, userQueryParam, TESTING_GUID, itemList);
		assertNotNull(restResponse);
		assertTrue(restResponse.getStatusCode() == Response.Status.OK.getStatusCode());
		assertTrue(SUCCESS.equals(restResponse.getBody()));

	}

	@Test(expectedExceptions = RestException.class)
	public void addItemToGivenListByUPCOnExceptionTest() {

		RestException exception = new RestException(FAILED, new NullPointerException());

		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.post(any())).thenThrow(exception);

		List<Item> itemList = getTestItemList();
		ListCookies cookie = new ListCookies(10L, "xxx-guid","xxx-token");
		listRestClient.addItemToGivenListByUPC(cookie, userQueryParam, TESTING_GUID, itemList);
	}
	
	public static List<Item> getTestItemList() {
		List<Item> itemList = new ArrayList<Item>();
		Item item = new Item();
        Upc upc = new Upc();
        upc.setId(UPC_ID);
        upc.setUpcNumber(UPC_NUMBER);
        item.setUpc(upc);
        itemList.add(item);
        item.setQtyRequested(QTY);
        return itemList;
	}

	@Test
	public void testMergeList() {
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.path(wishlistClientPool.getBasePath())).thenReturn(rxWebTarget);
		when(rxWebTarget.path(WishlistConstants.MERGE_LIST_PATH)).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(SUCCESS);

		RestResponse restResponse = listRestClient.mergeList(new CustomerListMerge());
		assertNotNull(restResponse);
		assertEquals(restResponse.getStatusCode(), Response.Status.OK.getStatusCode());
		assertEquals(SUCCESS, restResponse.getBody());
	}

	@Test
	public void testMergeListErrorCode() {
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.path(wishlistClientPool.getBasePath())).thenReturn(rxWebTarget);
		when(rxWebTarget.path(WishlistConstants.MERGE_LIST_PATH)).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(FAILED);

		RestResponse restResponse = listRestClient.mergeList(new CustomerListMerge());
		assertNotNull(restResponse);
		assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
		assertEquals(FAILED, restResponse.getBody());
	}

	@Test(expectedExceptions = RestException.class)
	public void testMergeListException() {
		when(rxWebTarget.path(wishlistClientPool.getBasePath())).thenReturn(rxWebTarget);
		when(rxWebTarget.path(WishlistConstants.MERGE_LIST_PATH)).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.post(any())).thenThrow(new RuntimeException());
		listRestClient.mergeList(new CustomerListMerge());
	}

	@Test
	public void testUpdateItemPriority() {
		ItemDTO itemRequest = new ItemDTO();
		itemRequest.setPriority("H");
		String item = "{\"item\":{\"priority\":\"H\"}}";
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.buildPatch(any())).thenReturn(rxInvocation);
		when(rxInvocation.invoke()).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(item);

		RestResponse restResponse = listRestClient.updateItemPriority(TEST_LIST_GUID, TESTING_ITEM_GUID, USER_ID, USER_GUID, itemRequest);
		verify(rxClient).target(customerListClientPool.getHostName());
		verify(rxWebTarget).path(customerListClientPool.getBasePath());
		verify(rxWebTarget).path(WishlistConstants.UPDATE_ITEM_PRIORITY_PATH);
		verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, TEST_LIST_GUID);
		verify(rxWebTarget).resolveTemplate(WishlistConstants.ITEM_GUID, TESTING_ITEM_GUID);
		verify(rxWebTarget).queryParam(WishlistConstants.USER_ID, USER_ID);
		verify(rxWebTarget).queryParam(WishlistConstants.USER_GUID, USER_GUID);
		assertNotNull(restResponse);
		assertEquals(restResponse.getStatusCode(), Response.Status.OK.getStatusCode());
		assertEquals(item, restResponse.getBody());
	}

	@Test
	public void testUpdateItemPriorityErrorCode() {
		ItemDTO itemRequest = new ItemDTO();
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.buildPatch(any())).thenReturn(rxInvocation);
		when(rxInvocation.invoke()).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(FAILED);

		RestResponse restResponse = listRestClient.updateItemPriority(TEST_LIST_GUID, TESTING_ITEM_GUID, USER_ID, USER_GUID, itemRequest);
		assertNotNull(restResponse);
		assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
		assertEquals(FAILED, restResponse.getBody());
	}

	@Test(expectedExceptions = RestException.class)
	public void testUpdateItemPriorityException() {
		ItemDTO itemRequest = new ItemDTO();
		Response response = Mockito.mock(Response.class);
		when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
		when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
		when(rxWebTarget.request()).thenReturn(invocationBuilder);
		when(invocationBuilder.buildPatch(any())).thenThrow(new RuntimeException());
		when(rxInvocation.invoke()).thenReturn(response);
		when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
		when(response.readEntity(String.class)).thenReturn(FAILED);

		listRestClient.updateItemPriority(TEST_LIST_GUID, TESTING_ITEM_GUID, USER_ID, USER_GUID, itemRequest);
	}
}
