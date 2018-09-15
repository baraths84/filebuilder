package com.macys.selection.xapi.list.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxClient;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocation;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.platform.rest.framework.client.exception.RestClientException;
import com.macys.selection.xapi.list.client.request.MergeListRequest;
import com.macys.selection.xapi.list.client.response.CollaboratorDTO;
import com.macys.selection.xapi.list.client.response.EmailShareDTO;
import com.macys.selection.xapi.list.client.response.ItemDTO;
import com.macys.selection.xapi.list.client.response.UserAvatarDTO;
import com.macys.selection.xapi.list.client.response.WishListDTO;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.comparators.SortByField;
import com.macys.selection.xapi.list.comparators.SortOrder;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.SortQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.util.ListQueryParameterEnum;
import com.macys.selection.xapi.list.util.ListRequestParamUtil;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class ListRestClientTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FccRestClientTest.class);
    private static final String HOST_NAME = "http://localhost:8080";
    private static final String BASE_PATH = "/api/selection/list/v1";

    private static final String LIST_GUID = "testListGUID";
    private static final String ITEM_GUID = "testItemGUID";
    private static final String USER_FIRST_NAME = "userFirstName";
    private static final String LIST_ITEM_GUID = "testListItemGUID";
    private static final long USER_ID = 123;
    private static final long USER_ID_2 = 456;
    private static final String USER_GUID = "testUserGUID";
    private static final String USER_GUID_2 = "testUserGUID2";
    private static final int UPC_ID = 1010101;
    private static final int PRODUCT_ID = 2020202;
    private static final String TEST_RESPONSE = "Some response body";
    private static final String EXPAND_ITEMS = "items";
    private static final String ERRORS = "errors";

    public static final String ITEM_FEEDBACK = "like";

    private static final String TOKEN = "testToken";
    private static final int LIMIT = 100;
    private static final int OFFSET = 100;
    private static final String CREATED = "CREATED";

    private ListRestClient listRestClient;

    @Mock
    private RestClientFactory.JaxRSClientPool listClientPool;

    @Mock
    private RxClient rxClient;

    @Mock
    private RxWebTarget rxWebTarget;

    @Mock
    private RxInvocationBuilder invocationBuilder;

    @Mock
    private RxInvocation rxInvocation;

    @BeforeMethod
    public void init() {

        MockitoAnnotations.initMocks(this);

        listRestClient = Mockito.spy(new ListRestClient(true, listClientPool, new ListRequestParamUtil()));

        try {
            when(listClientPool.getRxClient(anyString())).thenReturn(rxClient);
        } catch (RestClientException e) {
            LOGGER.error("Exception while getting clinet:", e);
        }

        when(listClientPool.getHostName()).thenReturn(HOST_NAME);
        when(listClientPool.getBasePath()).thenReturn(BASE_PATH);

        when(rxWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(rxWebTarget);

        when(rxClient.target(anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
    }

    @Test
    public void testGetListByUserId() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(TEST_RESPONSE);
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setGuestUser(false);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(EXPAND_ITEMS);
        listQueryParam.setDefaultList(true);
        RestResponse restResponse = listRestClient.getList(userQueryParam, listQueryParam, new PaginationQueryParam());

        verify(rxWebTarget).path(WishlistConstants.WISHLISTS_PATH);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), USER_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_ID.getParamName(), String.valueOf(USER_ID));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), "false");
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.EXPAND.getParamName(), EXPAND_ITEMS);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.DEFAULT.getParamName(), "true");

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
        Assert.assertEquals(TEST_RESPONSE, restResponse.getBody());
    }

    @Test
    public void testGetListByUserIds() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(TEST_RESPONSE);
        UserQueryParam userQueryParam = new UserQueryParam();
        List<Long> userIds = Arrays.asList(USER_ID, USER_ID_2);
        userQueryParam.setUserIds(userIds);
        ListQueryParam listQueryParam = new ListQueryParam();
        RestResponse restResponse = listRestClient.getList(userQueryParam, listQueryParam, new PaginationQueryParam());

        verify(rxWebTarget).path(WishlistConstants.WISHLISTS_PATH);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_IDS.getParamName(), USER_ID + "," + USER_ID_2);

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
        Assert.assertEquals(TEST_RESPONSE, restResponse.getBody());
    }

    @Test
    public void testGetListByGuid() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(TEST_RESPONSE);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(EXPAND_ITEMS);
        RestResponse restResponse = listRestClient.getListByGuid(LIST_GUID, listQueryParam, new PaginationQueryParam());

        verify(rxWebTarget).path(WishlistConstants.WISHLISTS_PATH);
        verify(rxWebTarget).path(WishlistConstants.PATH_PARAMETER_LIST_GUID);
        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.EXPAND.getParamName(), EXPAND_ITEMS);

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
        Assert.assertEquals(TEST_RESPONSE, restResponse.getBody());
    }

    @Test
    public void testDeleteList() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.NO_CONTENT.getStatusCode());

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setGuestUser(false);

        RestResponse restResponse = listRestClient.deleteList(TOKEN, LIST_GUID, userQueryParam);

        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), USER_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_ID.getParamName(), String.valueOf(USER_ID));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), "false");
        verify(invocationBuilder).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, TOKEN);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), restResponse.getStatusCode());
    }

    @Test
    public void testDeleteListWithInvalidStatus() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn("Some error happend!");

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);

        RestResponse restResponse = listRestClient.deleteList(TOKEN, LIST_GUID, userQueryParam);

        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).queryParam(anyString(), any());
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), USER_GUID);

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), restResponse.getStatusCode());
        Assert.assertEquals("Some error happend!", restResponse.getBody());
    }

    @Test
    public void testDeleteListWithException() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenThrow(new ProcessingException("Some error happend!"));

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);

        try {
            listRestClient.deleteList(TOKEN, LIST_GUID, userQueryParam);
            Assert.assertTrue("Rest exception is expected", false);
        } catch (RestException re) {
            Assert.assertEquals("Service failure while deleting list: Some error happend!", re.getMessage());
        } catch (Exception e) {
            Assert.assertTrue("Unexpected exception", false);
        }
    }

    @Test
    public void testUpdateWishlist() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        String requestBody = "{\"list\":{\"name\":\"listName\",\"defaultList\":true,\"searchable\":true}}";
        when(invocationBuilder.method("PATCH", Entity.text(requestBody))).thenReturn(response);

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setGuestUser(false);

        CustomerList customerList = new CustomerList();
        WishList wl = new WishList();
        wl.setName("listName");
        wl.setDefaultList(true);
        wl.setSearchable(true);
        customerList.setWishlist(Arrays.asList(wl));

        RestResponse restResponse = listRestClient.updateWishlist(TOKEN, LIST_GUID, userQueryParam, customerList);

        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), USER_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_ID.getParamName(), String.valueOf(USER_ID));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), "false");
        verify(invocationBuilder).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, TOKEN);

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
    }

    @Test
    public void testUpdateWishlistWithInvalidStatus() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn("Some error happend!");

        when(invocationBuilder.method("PATCH", Entity.text("{\"list\":{}}"))).thenReturn(response);

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        CustomerList customerList = new CustomerList();

        RestResponse restResponse = listRestClient.updateWishlist(TOKEN, LIST_GUID, userQueryParam, customerList);

        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        // only 1 param is added to request
        verify(rxWebTarget).queryParam(anyString(), any());
        // and it is userId
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_ID.getParamName(), String.valueOf(USER_ID));
        verify(invocationBuilder).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, TOKEN);

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), restResponse.getStatusCode());
        Assert.assertEquals("Some error happend!", restResponse.getBody());
    }

    @Test
    public void testUpdateWishlistWithException() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.method("PATCH", Entity.text("{\"list\":{}}")))
                .thenThrow(new ProcessingException("Some error happend!"));

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        CustomerList customerList = new CustomerList();

        try {
            listRestClient.updateWishlist(TOKEN, LIST_GUID, userQueryParam, customerList);
            Assert.assertTrue("Rest exception is expected", false);
        } catch (RestException re) {
            Assert.assertEquals("Service failure while updating list: Some error happend!", re.getMessage());
            verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
            verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), String.valueOf(USER_GUID));
        } catch (Exception e) {
            Assert.assertTrue("Unexpected exception", false);
        }
    }

    @Test
    public void testDeleteItem() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.NO_CONTENT.getStatusCode());

        RestResponse restResponse = listRestClient.deleteItem(LIST_GUID, ITEM_GUID, false);
        verify(rxClient).target(listClientPool.getHostName());
        verify(rxWebTarget).path(listClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.DELETE_ITEM_PATH);
        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).resolveTemplate(WishlistConstants.ITEM_GUID, ITEM_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), false);
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void testDeleteItemErrorCode() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(ERRORS);

        RestResponse restResponse = listRestClient.deleteItem(LIST_GUID, ITEM_GUID, false);
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(ERRORS, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testDeleteItemException() {
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenThrow(new RuntimeException());
        listRestClient.deleteItem(LIST_GUID, ITEM_GUID, false);
    }

    @Test
    public void testDeleteFavoriteItem() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.NO_CONTENT.getStatusCode());
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setUpcId(UPC_ID);
        listQueryParam.setProductId(PRODUCT_ID);
        RestResponse restResponse = listRestClient.deleteFavoriteItem(LIST_GUID, false, listQueryParam);
        verify(rxClient).target(listClientPool.getHostName());
        verify(rxWebTarget).path(listClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.FAVORITES_PATH);
        verify(rxWebTarget).path(WishlistConstants.PATH_PARAMETER_LIST_GUID);
        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), false);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.PRODUCT_ID.getParamName(), String.valueOf(PRODUCT_ID));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.UPC_ID.getParamName(), String.valueOf(UPC_ID));
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void testDeleteFavoriteItemErrorCode() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(ERRORS);
        RestResponse restResponse = listRestClient.deleteFavoriteItem(LIST_GUID, false, new ListQueryParam());
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(ERRORS, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testDeleteFavoriteItemException() {
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenThrow(new RuntimeException());
        listRestClient.deleteFavoriteItem(LIST_GUID, false, new ListQueryParam());
    }

    @Test
    public void testUpdateItemPriority() {
        ItemDTO itemRequest = new ItemDTO();
        itemRequest.setPriority("H");
        String item = "{\"item\":{\"priority\":\"H\"}}";
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.buildPatch(any())).thenReturn(rxInvocation);
        when(rxInvocation.invoke()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(item);

        RestResponse restResponse = listRestClient.updateItemPriority(TOKEN, LIST_GUID, ITEM_GUID, USER_ID, USER_GUID, false, itemRequest);
        verify(rxClient).target(listClientPool.getHostName());
        verify(rxWebTarget).path(listClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.WISHLISTS_PATH);
        verify(rxWebTarget).path(WishlistConstants.UPDATE_ITEM_PRIORITY_PATH);
        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).resolveTemplate(WishlistConstants.ITEM_GUID, ITEM_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_ID.getParamName(), USER_ID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), USER_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), false);
        verify(invocationBuilder).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, TOKEN);
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.OK.getStatusCode());
        assertEquals(item, restResponse.getBody());
    }

    @Test
    public void testUpdateItemPriorityErrorCode() {
        ItemDTO itemRequest = new ItemDTO();
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.buildPatch(any())).thenReturn(rxInvocation);
        when(rxInvocation.invoke()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(ERRORS);

        RestResponse restResponse = listRestClient.updateItemPriority(TOKEN, LIST_GUID, ITEM_GUID, USER_ID, USER_GUID, false, itemRequest);
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(ERRORS, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testUpdateItemPriorityException() {
        ItemDTO itemRequest = new ItemDTO();
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.buildPatch(any())).thenThrow(new RuntimeException());

        listRestClient.updateItemPriority(TOKEN, LIST_GUID, ITEM_GUID, USER_ID, USER_GUID, false, itemRequest);
    }


    @Test
    public void testCreateList() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        String requestBody = "{\"list\":{\"name\":\"listName\",\"defaultList\":false,\"searchable\":true,\"items\":[{\"qtyRequested\":1}]}}";
        when(invocationBuilder.post(Entity.json(requestBody))).thenReturn(response);

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setGuestUser(false);

        WishListDTO wishList = new WishListDTO();
        wishList.setName("listName");
        wishList.setDefaultList(false);
        wishList.setSearchable(true);
        ItemDTO item = new ItemDTO();
        item.setQtyRequested(1);
        wishList.setItems(Arrays.asList(item));

        RestResponse restResponse = listRestClient.createList(TOKEN, wishList, userQueryParam);

        verify(rxWebTarget).queryParam(anyString(), any());
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), "false");

        verify(invocationBuilder).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, TOKEN);

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
    }

    @Test
    public void testCreateWishlistWithInvalidStatus() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn("Some error happend!");

        when(invocationBuilder.post(any())).thenReturn(response);

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setGuestUser(true);

        RestResponse restResponse = listRestClient.createList(TOKEN, new WishListDTO(), userQueryParam);

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), restResponse.getStatusCode());
        Assert.assertEquals("Some error happend!", restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testCreateListWithException() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new ProcessingException("Some error happend!"));

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setGuestUser(false);

        try {
            listRestClient.createList(TOKEN, new WishListDTO(), userQueryParam);
        } catch (RestException re) {
            Assert.assertEquals("Exception in creating wishlist: Some error happend!", re.getMessage());
            throw re;
        }
    }

    @Test
    public void testAddToDefaultWishlist() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        String requestBody = "[{\"qtyRequested\":1,\"productId\":2}]";
        when(invocationBuilder.post(Entity.json(requestBody))).thenReturn(response);

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setGuestUser(false);
        userQueryParam.setFirstName(USER_FIRST_NAME);

        ItemDTO item = new ItemDTO();
        item.setQtyRequested(1);
        item.setProductId(2);
        List<ItemDTO> items = Arrays.asList(item);

        boolean onSaleNotify = true;

        RestResponse restResponse = listRestClient.addItemToDefaultWishlist(items, userQueryParam, onSaleNotify);

        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), USER_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_ID.getParamName(), String.valueOf(USER_ID));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), "false");
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.ON_SALE_NOTIFY.getParamName(), "true");
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_FIRST_NAME.getParamName(), USER_FIRST_NAME);
        verify(rxWebTarget, times(5)).queryParam(anyString(), any());

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
    }

    @Test
    public void testAddToDefaultWishlistWithInvalidStatus() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn("Some error happend!");

        when(invocationBuilder.post(any())).thenReturn(response);

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);

        List<ItemDTO> items = Arrays.asList(new ItemDTO());

        RestResponse restResponse = listRestClient.addItemToDefaultWishlist(items, userQueryParam, false);

        verify(rxWebTarget, times(2)).queryParam(anyString(), any());
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_ID.getParamName(), String.valueOf(USER_ID));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.ON_SALE_NOTIFY.getParamName(), "false");

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), restResponse.getStatusCode());
        Assert.assertEquals("Some error happend!", restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testAddToDefaultWishlistWithException() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new ProcessingException("Some error happend!"));

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);

        List<ItemDTO> items = Arrays.asList(new ItemDTO());

        try {
            listRestClient.addItemToDefaultWishlist(items, userQueryParam, false);
        } catch (RestException re) {
            Assert.assertEquals("Service failure while adding item to default wishlist: Some error happend!", re.getMessage());
            verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), String.valueOf(USER_GUID));
            throw re;
        }
    }

    @Test
    public void testAddItemToGivenList() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        String requestBody = "[{\"qtyRequested\":1,\"productId\":2},{\"qtyRequested\":3,\"productId\":22}]";
        when(invocationBuilder.post(Entity.json(requestBody))).thenReturn(response);

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setGuestUser(true);

        ItemDTO item1 = new ItemDTO();
        item1.setQtyRequested(1);
        item1.setProductId(2);
        ItemDTO item2 = new ItemDTO();
        item2.setQtyRequested(3);
        item2.setProductId(22);
        List<ItemDTO> items = Arrays.asList(item1, item2);

        RestResponse restResponse = listRestClient.addItemToGivenList(LIST_GUID, items, userQueryParam);

        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), USER_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_ID.getParamName(), String.valueOf(USER_ID));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), "true");
        verify(rxWebTarget, times(3)).queryParam(anyString(), any());

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
    }

    @Test
    public void testAddItemToGivenListWithInvalidStatus() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn("Some error happend!");

        when(invocationBuilder.post(any())).thenReturn(response);

        UserQueryParam userQueryParam = new UserQueryParam();

        List<ItemDTO> items = Arrays.asList(new ItemDTO());

        RestResponse restResponse = listRestClient.addItemToGivenList(LIST_GUID, items, userQueryParam);

        verify(rxWebTarget, never()).queryParam(anyString(), any());

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), restResponse.getStatusCode());
        Assert.assertEquals("Some error happend!", restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testAddItemToGivenListWithException() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new ProcessingException("Some error happend!"));

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);

        try {
            listRestClient.addItemToGivenList(LIST_GUID, null, userQueryParam);
        } catch (RestException re) {
            Assert.assertEquals("Failed on adding to a given wishlist by UPC: Some error happend!", re.getMessage());
            throw re;
        }
    }

    @Test
    public void testMoveItemToWishlist() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.NO_CONTENT.getStatusCode());
        when(invocationBuilder.post(null)).thenReturn(response);

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setGuestUser(false);

        RestResponse restResponse = listRestClient.moveItemToWishlist(TOKEN, LIST_GUID, LIST_ITEM_GUID, userQueryParam);

        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), USER_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_ID.getParamName(), String.valueOf(USER_ID));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), "false");
        verify(rxWebTarget).queryParam(WishlistConstants.ITEM_GUID, LIST_ITEM_GUID);
        verify(rxWebTarget, times(4)).queryParam(anyString(), any());

        verify(invocationBuilder).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, TOKEN);

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), restResponse.getStatusCode());
    }

    @Test
    public void testMoveItemToWishlistWithInvalidStatus() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn("Some error happend!");

        when(invocationBuilder.post(any())).thenReturn(response);

        UserQueryParam userQueryParam = new UserQueryParam();

        RestResponse restResponse = listRestClient.moveItemToWishlist(TOKEN, LIST_GUID, LIST_ITEM_GUID, userQueryParam);

        verify(rxWebTarget).queryParam(WishlistConstants.ITEM_GUID, LIST_ITEM_GUID);
        verify(rxWebTarget).queryParam(anyString(), any());

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), restResponse.getStatusCode());
        Assert.assertEquals("Some error happend!", restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testMoveItemToWishlistWithException() {

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new ProcessingException("Some error happend!"));

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);

        try {
            listRestClient.moveItemToWishlist(TOKEN, LIST_GUID, LIST_ITEM_GUID, userQueryParam);
        } catch (RestException re) {
            Assert.assertEquals("Service failure while moving item to list: Some error happend!", re.getMessage());
            throw re;
        }
    }

    @Test
    public void testGetFavoriteItemsFromDefaultList() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request((MediaType) any())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        RestResponse restResponse = listRestClient.getFavoriteItemsFromDefaultList(USER_ID);
        verify(rxClient).target(listClientPool.getHostName());
        verify(rxWebTarget).path(listClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.FAVORITES_PATH);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_ID.getParamName(), USER_ID);
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.OK.getStatusCode());
    }
    @Test
    public void testGetFavoriteItemsFromDefaultListErrorCode() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request((MediaType) any())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(ERRORS);
        RestResponse restResponse = listRestClient.getFavoriteItemsFromDefaultList(USER_ID);
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(ERRORS, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testGetFavoriteItemsFromDefaultListException() {
        when(rxWebTarget.request((MediaType) any())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenThrow(new RuntimeException());
        listRestClient.getFavoriteItemsFromDefaultList(USER_ID);
    }

    @Test
    public void testMergeList() {
        Response response = Mockito.mock(Response.class);

        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.NO_CONTENT.getStatusCode());

        RestResponse restResponse = listRestClient.mergeList(TOKEN, new MergeListRequest());
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.NO_CONTENT.getStatusCode());
        assertNull(restResponse.getBody());

        verify(rxWebTarget).path(listClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.WISHLISTS_PATH);
        verify(rxWebTarget).path(WishlistConstants.MERGE_LIST_PATH);
        verify(invocationBuilder).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, TOKEN);
    }

    @Test
    public void testMergeListErrorCode() {
        String errorMessage = "Some error happend!";
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(errorMessage);

        RestResponse restResponse = listRestClient.mergeList(TOKEN, new MergeListRequest());
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(errorMessage, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testMergeListException() {
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new RuntimeException());
        listRestClient.mergeList(TOKEN, new MergeListRequest());
    }

    @Test
    public void testShareEmail() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(response.getStatus()).thenReturn(Response.Status.NO_CONTENT.getStatusCode());
        when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);

        RestResponse restResponse = listRestClient.emailShareWishlist(TOKEN, LIST_GUID, new EmailShareDTO());

        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(invocationBuilder).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, TOKEN);

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), restResponse.getStatusCode());
        assertNull(restResponse.getBody());
    }

    @Test
    public void testShareEmailErrorCode() {
        String errorMessage = "Some error happend!";
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(Entity.json(any()))).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(errorMessage);

        RestResponse restResponse = listRestClient.emailShareWishlist(TOKEN, LIST_GUID, new EmailShareDTO());
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(errorMessage, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testShareEmailWithException() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new RuntimeException());
        listRestClient.emailShareWishlist(TOKEN, LIST_GUID, new EmailShareDTO());
    }

    @Test
    public void testAddItemFeedback() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(response.getStatus()).thenReturn(Response.Status.NO_CONTENT.getStatusCode());
        when(invocationBuilder.post(null)).thenReturn(response);

        RestResponse restResponse = listRestClient.addItemFeedback(LIST_GUID, ITEM_GUID, USER_GUID, ITEM_FEEDBACK);

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), restResponse.getStatusCode());
        assertNull(restResponse.getBody());

        verify(rxClient).target(HOST_NAME);
        verify(rxWebTarget).path(BASE_PATH);
        verify(rxWebTarget).path(WishlistConstants.COLLABORATORS);
        verify(rxWebTarget).path(WishlistConstants.ADD_ITEM_FEEDBACK_PATH);

        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).resolveTemplate(WishlistConstants.ITEM_GUID, ITEM_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), String.valueOf(USER_GUID));
        verify(rxWebTarget).queryParam(WishlistConstants.ITEM_FEEDBACK, ITEM_FEEDBACK);
        verify(rxWebTarget).request(MediaType.APPLICATION_JSON_TYPE);
        verify(invocationBuilder).post(null);
        verify(response).close();
    }

    @Test
    public void testAddItemFeedbackWithError() {
        String errorMessage = "Some error happend!";
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(null)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(errorMessage);

        RestResponse restResponse = listRestClient.addItemFeedback(LIST_GUID, ITEM_GUID, USER_GUID, ITEM_FEEDBACK);
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(errorMessage, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testAddItemFeedbackWithErrorWithException() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new RuntimeException());

        listRestClient.addItemFeedback(LIST_GUID, ITEM_GUID, USER_GUID, ITEM_FEEDBACK);
    }

    @Test
    public void testAddCollaborator() throws JsonProcessingException {
        Response response = Mockito.mock(Response.class);
        CollaboratorDTO collaboratorDTO = new CollaboratorDTO();
        collaboratorDTO.setUserGuid(USER_GUID);
        collaboratorDTO.setListGuid(LIST_GUID);

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(invocationBuilder.post(any())).thenReturn(response);

        RestResponse restResponse = listRestClient.addCollaborator(collaboratorDTO);

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
        assertNull(restResponse.getBody());

        verify(rxClient).target(HOST_NAME);
        verify(rxWebTarget).path(BASE_PATH);
        verify(rxWebTarget).path(WishlistConstants.COLLABORATORS);
        verify(rxWebTarget).path(WishlistConstants.PATH_PARAMETER_LIST_GUID);

        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).request(MediaType.APPLICATION_JSON_TYPE);
        verify(invocationBuilder).post(any());
        verify(response).close();
    }

    @Test
    public void testAddCollaboratorsWithError() throws JsonProcessingException {
        String errorMessage = "Some error happend!";
        Response response = Mockito.mock(Response.class);
        CollaboratorDTO collaboratorDTO = new CollaboratorDTO();
        collaboratorDTO.setUserGuid(USER_GUID);
        collaboratorDTO.setListGuid(LIST_GUID);

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(errorMessage);

        RestResponse restResponse = listRestClient.addCollaborator(collaboratorDTO);
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(errorMessage, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testAddCollaboratorsWithErrorWithException() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new RuntimeException());

        listRestClient.addCollaborator(null);
    }

    @Test
    public void testGetCollaboratorsByGuid() {
        Response response = Mockito.mock(Response.class);

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        RestResponse restResponse = listRestClient.getCollaboratorsByGuid(LIST_GUID);

        verify(rxClient).target(listClientPool.getHostName());
        verify(rxWebTarget).path(listClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.COLLABORATORS);
        verify(rxWebTarget).path(WishlistConstants.PATH_PARAMETER_LIST_GUID);
        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);

        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testGetCollaboratorsByGuidWithError() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request((MediaType) any())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(ERRORS);

        RestResponse restResponse = listRestClient.getCollaboratorsByGuid(LIST_GUID);

        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(ERRORS, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testGetCollaboratorsByGuidWithErrorWithException() {
        when(rxWebTarget.request((MediaType) any())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenThrow(new RuntimeException());
        listRestClient.getCollaboratorsByGuid(LIST_GUID);
    }

    @Test
    public void testGetActivityLog() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(TEST_RESPONSE);
        when(invocationBuilder.get()).thenReturn(response);

        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        paginationQueryParam.setOffset(OFFSET);
        paginationQueryParam.setLimit(LIMIT);

        SortQueryParam sortQueryParam = new SortQueryParam();
        sortQueryParam.setSortOrder(SortOrder.ASC.getValue());
        sortQueryParam.setSortBy(CREATED);

        RestResponse restResponse = listRestClient.getActivityLog(LIST_GUID, USER_GUID, paginationQueryParam, sortQueryParam);

        assertNotNull(restResponse);
        assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
        assertEquals(TEST_RESPONSE, restResponse.getBody());

        verify(rxClient).target(HOST_NAME);
        verify(rxWebTarget).path(BASE_PATH);
        verify(rxWebTarget).path(WishlistConstants.COLLABORATORS);
        verify(rxWebTarget).path(WishlistConstants.GET_ACTIVITY_LOG_PATH);

        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), USER_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.LIMIT.getParamName(), String.valueOf(LIMIT));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.OFFSET.getParamName(), String.valueOf(OFFSET));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.SORT_BY.getParamName(), CREATED);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.SORT_ORDER.getParamName(), SortOrder.ASC.getValue());
        verify(rxWebTarget).request(MediaType.APPLICATION_JSON_TYPE);
        verify(invocationBuilder).get();
        verify(response).close();
    }

    @Test
    public void testGetActivityLogWithError() {
        String errorMessage = "Some error happend!";
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(errorMessage);

        RestResponse restResponse = listRestClient.getActivityLog(LIST_GUID, USER_GUID, new PaginationQueryParam(), new SortQueryParam());
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(errorMessage, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testGetActivityLogWithException() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new RuntimeException());

        listRestClient.getActivityLog(LIST_GUID, USER_GUID, new PaginationQueryParam(), new SortQueryParam());
    }


    @Test
    public void testGetUserCollaborators() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(TEST_RESPONSE);
        when(invocationBuilder.get()).thenReturn(response);

        RestResponse restResponse = listRestClient.getUserCollaborators(USER_GUID, Collections.emptySet());

        assertNotNull(restResponse);
        assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
        assertEquals(TEST_RESPONSE, restResponse.getBody());

        verify(rxClient).target(HOST_NAME);
        verify(rxWebTarget).path(BASE_PATH);
        verify(rxWebTarget).path(WishlistConstants.COLLABORATORS);
        verify(rxWebTarget).path(WishlistConstants.USER_COLLABORATORS);

        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), String.valueOf(USER_GUID));
        verify(rxWebTarget).request(MediaType.APPLICATION_JSON_TYPE);
        verify(invocationBuilder).get();
        verify(response).close();
    }

    @Test
    public void testGetUserCollaboratorsWithError() {
        String errorMessage = "Some error happend!";
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(errorMessage);

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);

        RestResponse restResponse = listRestClient.getUserCollaborators(USER_GUID, Collections.emptySet());
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(errorMessage, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testGetUserCollaboratorsWithException() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new RuntimeException());

        listRestClient.getUserCollaborators(USER_GUID, Collections.emptySet());
    }

    @Test
    public void testGetListFeedback() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(TEST_RESPONSE);
        when(invocationBuilder.get()).thenReturn(response);

        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        paginationQueryParam.setOffset(OFFSET);
        paginationQueryParam.setLimit(LIMIT);

        SortQueryParam sortQueryParam = new SortQueryParam();
        sortQueryParam.setSortOrder(SortOrder.ASC.getValue());
        sortQueryParam.setSortBy(SortByField.ADDED_DATE.getField());

        RestResponse restResponse = listRestClient.getListFeedback(LIST_GUID, USER_GUID, USER_GUID_2);

        assertNotNull(restResponse);
        assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
        assertEquals(TEST_RESPONSE, restResponse.getBody());

        verify(rxClient).target(HOST_NAME);
        verify(rxWebTarget).path(BASE_PATH);
        verify(rxWebTarget).path(WishlistConstants.COLLABORATORS);
        verify(rxWebTarget).path(WishlistConstants.LIST_FEEDBACK_PATH);

        verify(rxWebTarget).resolveTemplate(WishlistConstants.LIST_GUID, LIST_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), USER_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.VIEWER_GUID.getParamName(), USER_GUID_2);
        verify(rxWebTarget).request(MediaType.APPLICATION_JSON_TYPE);
        verify(invocationBuilder).get();
        verify(response).close();
    }

    @Test
    public void testGetListFeedbackWithError() {
        String errorMessage = "Some error happend!";
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(errorMessage);

        RestResponse restResponse = listRestClient.getListFeedback(LIST_GUID, USER_GUID, USER_GUID_2);
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(errorMessage, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testGetListFeedbackWithException() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new RuntimeException());

        listRestClient.getListFeedback(LIST_GUID, USER_GUID, USER_GUID_2);
    }

    @Test
    public void testDeleteUserAvatar() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.NO_CONTENT.getStatusCode());

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setGuestUser(false);

        RestResponse restResponse = listRestClient.deleteUserAvatar(TOKEN, USER_GUID);

        verify(rxWebTarget).path(listClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.AVATAR);
        verify(rxWebTarget).path(WishlistConstants.PATH_PARAMETER_USER_GUID);
        verify(rxWebTarget).resolveTemplate(WishlistConstants.USER_GUID, USER_GUID);
        verify(invocationBuilder).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, TOKEN);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), restResponse.getStatusCode());
    }

    @Test
    public void testDeleteUserAvatarWithError() {
        String errorMessage = "Some error happend!";
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(errorMessage);

        RestResponse restResponse = listRestClient.deleteUserAvatar(TOKEN, USER_GUID);
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(errorMessage, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testDeleteUserAvatarWithException() {
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenThrow(new RuntimeException());

        listRestClient.deleteUserAvatar(TOKEN, USER_GUID);
    }

    @Test
    public void testAddUserAvatar() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.NO_CONTENT.getStatusCode());
        String requestBody = "{\"profilePicture\":{\"userGuid\":\"testUserGUID\",\"avatar\":\"test1\"}}";
        when(invocationBuilder.post(any())).thenReturn(response);

        UserAvatarDTO userAvatarDTO = new UserAvatarDTO();
        userAvatarDTO.setUserGuid(USER_GUID);
        userAvatarDTO.setAvatar("test1");

        RestResponse restResponse = listRestClient.addUserAvatar(TOKEN, userAvatarDTO);

        verify(rxWebTarget).path(listClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.AVATAR);
        verify(invocationBuilder).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, TOKEN);
        verify(invocationBuilder).post(Entity.json(requestBody));

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), restResponse.getStatusCode());
    }

    @Test
    public void testAddUserAvatarWithError() {
        String errorMessage = "Some error happend!";
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(errorMessage);

        RestResponse restResponse = listRestClient.addUserAvatar(TOKEN, new UserAvatarDTO());
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(errorMessage, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testAddUserAvatarWithException() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(new RuntimeException());

        listRestClient.addUserAvatar(TOKEN, new UserAvatarDTO());
    }

    @Test
    public void testGetUserAvatars() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get()).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setGuestUser(false);

        RestResponse restResponse = listRestClient.getUserAvatars(Arrays.asList(USER_GUID));

        verify(rxWebTarget).path(listClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.AVATAR);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUIDS.getParamName(), USER_GUID);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), restResponse.getStatusCode());
    }

    @Test
    public void testGetUserAvatarsWithError() {
        String errorMessage = "Some error happend!";
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(errorMessage);

        RestResponse restResponse = listRestClient.getUserAvatars(Arrays.asList(USER_GUID));
        assertNotNull(restResponse);
        assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(errorMessage, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testGetUserAvatarsWithException() {
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get()).thenThrow(new RuntimeException());

        listRestClient.getUserAvatars(Arrays.asList(USER_GUID));
    }
}
