package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.ListRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.request.CustomerListMerge;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.client.request.MergeListRequest;
import com.macys.selection.xapi.list.client.response.EmailItemDTO;
import com.macys.selection.xapi.list.client.response.EmailShareDTO;
import com.macys.selection.xapi.list.client.response.WishListDTO;
import com.macys.selection.xapi.list.client.response.user.LoginCredentialsResponse;
import com.macys.selection.xapi.list.client.response.user.ProfileResponse;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.comparators.ListSortingExecutor;
import com.macys.selection.xapi.list.comparators.SortByField;
import com.macys.selection.xapi.list.comparators.SortOrder;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.exception.ErrorConstants;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.mapping.MapperConfig;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.Availability;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.FavoriteList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.util.ListRequestParamUtil;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WishlistServiceTest {

    private static final String LIST_GUID = "testListGuid";
    private static final String ITEM_GUID = "testItemGuid";
    private static final String USER_GUID = "testUserGuid";
    private static final String USER_GUID_2 = "testUserGuid2";
    private static final String USER_FIRST_NAME = "testUserFirstName";
    private static final String USER_NAME = "testUserName";
    private static final long USER_ID = 123;
    private static final long USER_ID_2 = 456;
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String STATE = "state";
    private static final int TIME_1 = 1;
    private static final String WISH_LISTS_BY_USER_IDS_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_wishlist_response_by_user_ids.json";
    private static final String WISH_LISTS_BY_USER_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_wishlist_response_by_user.json";
    private static final String WISH_LIST_BY_LIST_GUID_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_wishlist_response_by_list_guid.json";
    private static final String WISH_LIST_CREATE_LIST_WITHOUT_ITEMS_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_wishlist_response_create_list_without_items.json";
    private static final String WISH_LIST_CREATE_LIST_WITH_ITEMS_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_wishlist_response_create_list_with_items.json";
    private static final String WISH_LIST_ADD_ITEM_TO_DEFAULT_LIST = "com/macys/selection/xapi/list/client/response/msp_wishlist_response_add_item_to_default_list.json";
    private static final String BASE_URI = "http://api.macys.com/";
    private static final String CUSTOMER_HOST = "http://111.11.11.111:8080/";
    private static final String IMAGE_URL = "7/optimized/0987_fpx.tif";
    private static final String EMPTY_LISTS = "{ \"lists\": { \"lists\": [ {} ] } }";
    private static final String EMPTY_LIST = "{ \"list\": {} }";
    private static final String LISTS_ONE_WITHOUT_LIST_GUID = "{ \"lists\": { \"lists\": [ {\"listGuid\": \"1\", \"name\": \"Wishlist One\"}, {\"name\": \"Wishlist No List Guid\"} ] } }";
    private static final String INVALID_EXPAND_OPTION_RESPONSE = "{ \"errors\": { \"error\": [ { \"errorCode\": \"10129\", \"message\": \"Invalid expand option\" } ] } }";
    private static final String ERROR10101_RESPONSE = "{ \"errors\": { \"error\": [ { \"errorCode\": \"10101\", \"message\": \"Invalid User ID\" } ] } }";
    private static final String TEST_ERROR_RESPONSE = "{ \"errors\": { \"error\": [ { \"errorCode\": \"99999\", \"message\": \"Test Error\" } ] } }";
    private static final String SECURE_TOKEN = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
    private static final String PRIORITY = "H";
    private static final ListServiceException invalidUserIdException = new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.INVALID_USER_ID);
    private static final long UPC_NUMBER = 123456L;
    private static final int PRODUCT_ID = 123;
    private static final int LIMIT = 100;
    private static final String PRODUCT_NAME = "testProductName";

    private WishlistService wishlistService;
    private WishlistService wishlistServiceCreateInvalidUserOff;
    private WishlistService wishlistServiceCreateUserOff;

    @Mock
    private ListRestClient listRestClient;

    @Mock
    private ListSortingExecutor listSortingExecutor;

    @Mock
    private ListCatalogService listCatalogService;

    @Mock
    private CustomerUserService customerUserService;

    private MapperFacade mapperFacade;

    public WishlistServiceTest() {
        MapperConfig mapperConfig = new MapperConfig();
        mapperFacade = mapperConfig.mapperFacade();
    }

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
        wishlistService = new WishlistService(listRestClient, listSortingExecutor, mapperFacade, listCatalogService, customerUserService, CUSTOMER_HOST, true, true, LIMIT);
        wishlistServiceCreateInvalidUserOff = new WishlistService(listRestClient, listSortingExecutor, mapperFacade, listCatalogService, customerUserService, CUSTOMER_HOST, false, true, LIMIT);
        wishlistServiceCreateUserOff = new WishlistService(listRestClient, listSortingExecutor, mapperFacade, listCatalogService, customerUserService, CUSTOMER_HOST, true, false, LIMIT);
    }

    @Test
    public void testFindList() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_BY_USER_IDS_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setFirstName(FIRST_NAME);
        listQueryParam.setLastName(LAST_NAME);
        listQueryParam.setState(STATE);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        UserResponse user1 = new UserResponse();
        user1.setId(USER_ID);
        UserResponse user2 = new UserResponse();
        user2.setId(USER_ID_2);
        List<UserResponse> users = Arrays.asList(user1, user2);
        Mockito.when(customerUserService.findUsers(any(), any(), any(), any())).thenReturn(users);
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        CustomerList actualWishList = wishlistService.findList(userQueryParam, listQueryParam, paginationQueryParam);
        verify(customerUserService, times(TIME_1)).findUsers(FIRST_NAME, LAST_NAME, STATE, LIMIT);
        Assert.assertEquals(Arrays.asList(USER_ID, USER_ID_2), userQueryParam.getUserIds());
        Assert.assertNotNull(actualWishList);
        Assert.assertEquals(3, actualWishList.getWishlist().size());
        Assert.assertNotNull(actualWishList.getUser());
    }

    @Test
    public void testFindListEmptyResult() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(EMPTY_LISTS);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setFirstName(FIRST_NAME);
        listQueryParam.setLastName(LAST_NAME);
        listQueryParam.setState(STATE);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(customerUserService.findUsers(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        CustomerList actualWishList = wishlistService.findList(userQueryParam, listQueryParam, paginationQueryParam);

        Mockito.verify(listRestClient, never()).getList(userQueryParam, listQueryParam, paginationQueryParam);
        Assert.assertNotNull(actualWishList);
        Assert.assertNotNull(actualWishList.getWishlist());
        Assert.assertTrue(actualWishList.getWishlist().isEmpty());
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testFindListValidateInputUserIdNotEmpty() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        try {
            wishlistService.findList(userQueryParam, new ListQueryParam(), new PaginationQueryParam());
        } catch (ListServiceException e) {
            assertListServiceErrorInvalidInput(e);
            throw e;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testFindListValidateInputUserGuidNotEmpty() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        try {
            wishlistService.findList(userQueryParam, new ListQueryParam(), new PaginationQueryParam());
        } catch (ListServiceException e) {
            assertListServiceErrorInvalidInput(e);
            throw e;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testFindListValidateInputFieldsNotEmpty() {
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setFields("fieldsValue");
        try {
            wishlistService.findList(new UserQueryParam(), listQueryParam, new PaginationQueryParam());
        } catch (ListServiceException e) {
            assertListServiceErrorInvalidInput(e);
            throw e;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testFindListValidateInputExpandNotEmpty() {
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_ITEMS);
        try {
            wishlistService.findList(new UserQueryParam(), listQueryParam, new PaginationQueryParam());
        } catch (ListServiceException e) {
            assertListServiceErrorInvalidInput(e);
            throw e;
        }
    }

    private void assertListServiceErrorInvalidInput(ListServiceException e) {
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
        Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_INPUT.getInternalCode(), e.getServiceErrorCode());
    }

    @Test
    public void testGetListByUserId() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_BY_USER_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setDefaultList(false);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(userResponse);
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        Mockito.doCallRealMethod().when(customerUserService).populateUserQueryParam(userQueryParam, userResponse, true);

        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Mockito.verify(customerUserService, times(TIME_1)).populateUserQueryParam(userQueryParam, userResponse, true);
        Assert.assertEquals(USER_ID, (long) userQueryParam.getUserId());
        Assert.assertEquals(USER_GUID, userQueryParam.getUserGuid());
        Assert.assertFalse(userQueryParam.getGuestUser());
        Assert.assertNotNull(actualWishList);
        Assert.assertEquals("Default list is always on the top of the list", "Wishlist Default", actualWishList.getWishlist().get(0).getName());
        Assert.assertEquals("Should be sorted by date created in descending order", "Wishlist Two", actualWishList.getWishlist().get(1).getName());
        Assert.assertEquals("Should be sorted by date created in descending order", "Wishlist One", actualWishList.getWishlist().get(2).getName());
        Assert.assertEquals(3, actualWishList.getWishlist().get(0).getLinks().size());
        Assert.assertEquals(3, actualWishList.getWishlist().get(1).getLinks().size());
        Assert.assertEquals(3, actualWishList.getWishlist().get(2).getLinks().size());
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testGetListInvalidInputWhenUserIdWithUserGuid() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setUserGuid(USER_GUID);
        ListQueryParam listQueryParam = new ListQueryParam();
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.doThrow(new ListServiceException()).when(customerUserService).validateNotBothUserIdAndGuidSpecified(USER_ID, USER_GUID);
        wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);
    }

    @Test
    public void testGetListByUserGuidShouldRetrieveUser() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(EMPTY_LISTS);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        ListQueryParam listQueryParam = new ListQueryParam();
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Mockito.verify(customerUserService).retrieveUser(null, USER_GUID, true);
    }

    @Test
    public void testGetListByUserIdNonDefaultShouldRetrieveUser() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(EMPTY_LISTS);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setDefaultList(false);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Mockito.verify(customerUserService).retrieveUser(USER_ID, null, false);
    }

    @Test
    public void testGetListByUserIdDefaultAndExpandUserShouldRetrieveUser() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(EMPTY_LISTS);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setDefaultList(true);
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_USER);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(customerUserService.retrieveUser(USER_ID, null, false)).thenReturn(createUserResponse(USER_ID, USER_GUID, false));
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        CustomerList wishlist = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);
        Assert.assertEquals(USER_ID, (long) wishlist.getUser().getId());
        Assert.assertEquals(USER_GUID, wishlist.getUser().getGuid());
        Assert.assertFalse(wishlist.getUser().isGuestUser());
        Assert.assertNotNull(wishlist.getUser().getProfile());
        Assert.assertEquals(USER_FIRST_NAME, wishlist.getUser().getProfile().getFirstName());

        Mockito.verify(customerUserService).retrieveUser(USER_ID, null, false);
    }

    @Test
    public void testGetListByUserIdDefaultAndNotExpandUserShouldNotRetrieveUser() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(EMPTY_LISTS);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setDefaultList(true);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Mockito.verify(customerUserService, never()).retrieveUser(USER_ID, null, false);
    }

    @Test(description = "wishlist.v1.create.invaliduser = false")
    public void testGetListByUserIdUserNotFoundException() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_USER);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenThrow(invalidUserIdException);

        CustomerList actualWishList = wishlistServiceCreateInvalidUserOff.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Mockito.verify(customerUserService).retrieveUser(USER_ID, null, true);
        Mockito.verify(listRestClient, never()).getList(any(), any(), any());
        Mockito.verify(customerUserService, never()).populateUserQueryParam(any(), any(), any());

        Assert.assertNotNull(actualWishList);
        Assert.assertTrue(CollectionUtils.isEmpty(actualWishList.getWishlist()));
    }

    @Test(description = "wishlist.v1.create.invaliduser = true")
    public void testGetListByUserIdUserNotFoundNoExceptionShouldSetGuestUser() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(EMPTY_LISTS);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(null);
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);

        wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Mockito.verify(customerUserService).populateUserQueryParam(userQueryParam, null, true);
    }

    @Test(description = "should never create the user if requested by userGuid (wishlist.v1.create.invaliduser = false/true)")
    public void testGetListByUserGuidUserNotFoundException() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        ListQueryParam listQueryParam = new ListQueryParam();
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenThrow(invalidUserIdException);
        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Mockito.verify(customerUserService).retrieveUser(null, USER_GUID, true);
        Mockito.verify(listRestClient, never()).getList(any(), any(), any());
        Mockito.verify(customerUserService, never()).populateUserQueryParam(any(), any(), any());

        Assert.assertNotNull(actualWishList);
        Assert.assertTrue(CollectionUtils.isEmpty(actualWishList.getWishlist()));
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testGetListByUserIdRetrieveUserException() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_USER);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenThrow(new ListServiceException());
        wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);
    }

    @Test
    public void testGetListWithExpandItems() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_BY_USER_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_ITEMS);
        listQueryParam.setDefaultList(false);
        listQueryParam.setSortBy(SortByField.RETAIL_PRICE.getField());
        listQueryParam.setSortOrder(SortOrder.DESC.getValue());
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(userResponse);
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        Mockito.doCallRealMethod().when(customerUserService).populateUserQueryParam(userQueryParam, userResponse, true);

        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Mockito.verify(listCatalogService, times(TIME_1)).populateListsItemDetails(actualWishList.getWishlist());
        Mockito.verify(listSortingExecutor, times(TIME_1)).validateSortField(listQueryParam.getSortBy());
        Mockito.verify(listSortingExecutor, times(TIME_1)).validateSortOrder(listQueryParam.getSortOrder());
        Mockito.verify(customerUserService, times(TIME_1)).validateEitherUserIdOrGuidSpecified(USER_ID, null);
        Mockito.verify(customerUserService, times(TIME_1)).validateNotBothUserIdAndGuidSpecified(USER_ID, null);
        Mockito.verify(listSortingExecutor, times(TIME_1)).sort(actualWishList, listQueryParam.getSortBy(), listQueryParam.getSortOrder());
        Mockito.verify(listCatalogService, never()).populateListItemImageUrlsList(any());
        Assert.assertEquals(3, actualWishList.getWishlist().get(0).getLinks().size());
        Assert.assertEquals(3, actualWishList.getWishlist().get(1).getLinks().size());
        Assert.assertEquals(3, actualWishList.getWishlist().get(2).getLinks().size());
        Optional<WishList> wishListWithItems = actualWishList.getWishlist().stream().filter(w -> "Wishlist Default".equals(w.getName())).findFirst();
        Assert.assertTrue(wishListWithItems.isPresent());
        Assert.assertTrue(CollectionUtils.isNotEmpty(wishListWithItems.get().getItems()));
    }

    @Test
    public void testGetListWithoutExpandItemsAndNonDefaultParam() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_BY_USER_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_USER);
        listQueryParam.setDefaultList(false);
        listQueryParam.setSortBy(SortByField.RETAIL_PRICE.getField());
        listQueryParam.setSortOrder(SortOrder.DESC.getValue());
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(userResponse);
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        Mockito.doCallRealMethod().when(customerUserService).populateUserQueryParam(userQueryParam, userResponse, true);

        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Mockito.verify(listCatalogService, never()).populateListsItemDetails(any());
        Mockito.verify(listSortingExecutor, never()).sort(any(CustomerList.class), anyString(), anyString());
        Mockito.verify(listCatalogService, times(TIME_1)).populateListItemImageUrlsList(actualWishList.getWishlist());
        Optional<WishList> wishListWithItems = actualWishList.getWishlist().stream().filter(w -> "Wishlist Default".equals(w.getName())).findFirst();
        Assert.assertTrue(wishListWithItems.isPresent());
        Assert.assertEquals(1, (int) wishListWithItems.get().getNumberOfItems());
        Assert.assertNull(wishListWithItems.get().getItems());
    }

    @Test
    public void testGetListWithoutExpandItemsAndDefaultParam() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_BY_USER_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_USER);
        listQueryParam.setDefaultList(true);
        listQueryParam.setSortBy(SortByField.RETAIL_PRICE.getField());
        listQueryParam.setSortOrder(SortOrder.DESC.getValue());
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(userResponse);
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        Mockito.doCallRealMethod().when(customerUserService).populateUserQueryParam(userQueryParam, userResponse, true);

        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Mockito.verify(listCatalogService, never()).populateListsItemDetails(any());
        Mockito.verify(listSortingExecutor, never()).sort(any(CustomerList.class), anyString(), anyString());
        Mockito.verify(listCatalogService, never()).populateListItemImageUrlsList(any());
        Optional<WishList> wishListWithItems = actualWishList.getWishlist().stream().filter(w -> "Wishlist Default".equals(w.getName())).findFirst();
        Assert.assertTrue(wishListWithItems.isPresent());
        Assert.assertEquals(1, (int) wishListWithItems.get().getNumberOfItems());
        Assert.assertNull(wishListWithItems.get().getItems());
    }

    @Test
    public void testExcludeUserFromResponse() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_BY_USER_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(null);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(userResponse);
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        Mockito.doCallRealMethod().when(customerUserService).populateUserQueryParam(userQueryParam, userResponse, true);

        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Assert.assertNull(actualWishList.getUser());
    }

    @Test
    public void testUserNotExcludedFromResponse() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_BY_USER_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_USER);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(userResponse);
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);

        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Assert.assertNotNull(actualWishList.getUser());
        Assert.assertEquals(USER_ID, (long) actualWishList.getUser().getId());
        Assert.assertEquals(USER_FIRST_NAME, actualWishList.getUser().getProfile().getFirstName());
    }

    @Test
    public void testExcludeItemsFromResponse() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_BY_USER_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(null);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);

        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Assert.assertTrue(CollectionUtils.isNotEmpty(actualWishList.getWishlist()));
        Optional<WishList> wishListWithItems = actualWishList.getWishlist().stream().filter(w -> "Wishlist Default".equals(w.getName())).findFirst();
        Assert.assertTrue(wishListWithItems.isPresent());
        Assert.assertNull(wishListWithItems.get().getItems());
    }

    @Test
    public void testItemsNotExcludedFromResponse() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_BY_USER_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_ITEMS);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);

        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);

        Assert.assertTrue(CollectionUtils.isNotEmpty(actualWishList.getWishlist()));
        Optional<WishList> wishListWithItems = actualWishList.getWishlist().stream().filter(w -> "Wishlist Default".equals(w.getName())).findFirst();
        Assert.assertTrue(wishListWithItems.isPresent());
        Assert.assertNotNull(wishListWithItems.get().getItems());
    }

    @Test
    public void testExcludeImageUrlsFromResponse() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(EMPTY_LISTS);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setDefaultList(true);
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_ITEMS);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        Mockito.doAnswer(this::mockWishListWithImageUrls).when(listCatalogService).populateListsItemDetails(any());
        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);
        Assert.assertNull(actualWishList.getWishlist().get(0).getImageUrlsList());
    }

    @Test
    public void testImageUrlsNotExcludedFromResponse() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(EMPTY_LISTS);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setDefaultList(false);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        Mockito.doAnswer(this::mockWishListWithImageUrls).when(listCatalogService).populateListItemImageUrlsList(any());
        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);
        Assert.assertTrue(CollectionUtils.isNotEmpty(actualWishList.getWishlist().get(0).getImageUrlsList()));
        Assert.assertEquals(IMAGE_URL, actualWishList.getWishlist().get(0).getImageUrlsList().get(0));
    }

    private Void mockWishListWithImageUrls(InvocationOnMock invocationOnMock) {
        List<WishList> list = invocationOnMock.getArgumentAt(0, List.class);
        WishList wishList = new WishList();
        wishList.setImageUrlsList(Collections.singletonList(IMAGE_URL));
        list.add(wishList);
        return null;
    }

    @Test
    public void testExcludeListsWithoutListGuid() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(LISTS_ONE_WITHOUT_LIST_GUID);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserQueryParam userQueryParam = new UserQueryParam();
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setDefaultList(false);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(listRestClient.getList(userQueryParam, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        CustomerList actualWishList = wishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);
        Assert.assertEquals(1, actualWishList.getWishlist().size());
        Assert.assertEquals("Wishlist One", actualWishList.getWishlist().get(0).getName());
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testGetListWithErrorResponse() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(INVALID_EXPAND_OPTION_RESPONSE);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.when(listRestClient.getList(any(), any(), any())).thenReturn(restResponse);
        try {
            wishlistService.getList(new UserQueryParam(), new ListQueryParam(), new PaginationQueryParam(), BASE_URI);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(restResponse.getBody(), e.getServiceError());
            throw e;
        }
    }

    @Test
    public void testGetListWith10101ErrorResponse() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(ERROR10101_RESPONSE);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.when(listRestClient.getList(any(), any(), any())).thenReturn(restResponse);
        CustomerList actualWishList = wishlistService.getList(new UserQueryParam(), new ListQueryParam(), new PaginationQueryParam(), BASE_URI);
        Assert.assertEquals(0, actualWishList.getWishlist().size());
    }

    @Test
    public void testGetListByListGuidWithExpandItems() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_ITEMS);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();

        Mockito.when(listRestClient.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam)).thenReturn(restResponse);

        CustomerList actualWishList = wishlistService.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam, BASE_URI);
        Mockito.verify(listCatalogService, times(TIME_1)).populateListsItemDetails(actualWishList.getWishlist());
        Mockito.verify(listSortingExecutor, times(TIME_1)).validateSortField(listQueryParam.getSortBy());
        Mockito.verify(listSortingExecutor, times(TIME_1)).validateSortOrder(listQueryParam.getSortOrder());
        Mockito.verify(listSortingExecutor, times(TIME_1)).sort(actualWishList, listQueryParam.getSortBy(), listQueryParam.getSortOrder());
        Mockito.verify(listCatalogService, never()).populateListItemImageUrlsList(any());
        Assert.assertNotNull(actualWishList);
        Assert.assertEquals(1, actualWishList.getWishlist().size());
        Assert.assertEquals(2, actualWishList.getWishlist().get(0).getItems().size());
        Assert.assertEquals(3, actualWishList.getWishlist().get(0).getLinks().size());
    }

    @Test
    public void testGetListByListGuidWithoutExpandItems() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(null);
        listQueryParam.setDefaultList(null);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();

        Mockito.when(listRestClient.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam)).thenReturn(restResponse);

        CustomerList actualWishList = wishlistService.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam, BASE_URI);
        Mockito.verify(listCatalogService, never()).populateListsItemDetails(any());
        Mockito.verify(listSortingExecutor, never()).sort(any(CustomerList.class), anyString(), anyString());
        Mockito.verify(listCatalogService, never()).populateListItemImageUrlsList(any());
        Assert.assertNotNull(actualWishList);
        Assert.assertEquals(1, actualWishList.getWishlist().size());
        Assert.assertNull(actualWishList.getWishlist().get(0).getItems());
        Assert.assertEquals(3, actualWishList.getWishlist().get(0).getLinks().size());
    }

    @Test
    public void testGetListByListGuidFilteredByUpcAvailability() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(EMPTY_LIST);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_ITEMS);
        listQueryParam.setFilter("availability");
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(listRestClient.getListByGuid(any(), any(), any())).thenReturn(restResponse);
        Mockito.doAnswer(this::mockWishListWithUpcsAvailability).when(listCatalogService).populateListsItemDetails(any());

        CustomerList actualWishList = wishlistService.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam, BASE_URI);
        Mockito.verify(listCatalogService, times(TIME_1)).populateListsItemDetails(actualWishList.getWishlist());
        List<Item> actualItems = actualWishList.getWishlist().get(0).getItems();
        Assert.assertEquals(1, actualItems.size());
        Assert.assertEquals(1, (long) actualItems.get(0).getUpc().getId());
    }

    @Test
    public void testGetListByListGuidNotFilteredByUpcAvailability() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(EMPTY_LIST);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_ITEMS);
        listQueryParam.setFilter(null);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(listRestClient.getListByGuid(any(), any(), any())).thenReturn(restResponse);
        Mockito.doAnswer(this::mockWishListWithUpcsAvailability).when(listCatalogService).populateListsItemDetails(any());

        CustomerList actualWishList = wishlistService.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam, BASE_URI);
        Mockito.verify(listCatalogService, times(TIME_1)).populateListsItemDetails(actualWishList.getWishlist());
        List<Item> actualItems = actualWishList.getWishlist().get(0).getItems();
        Assert.assertEquals(2, actualItems.size());
    }

    private Void mockWishListWithUpcsAvailability(InvocationOnMock invocationOnMock) {
        List<WishList> lists = invocationOnMock.getArgumentAt(0, List.class);
        Upc availableUpc = new Upc();
        availableUpc.setId(1);
        Availability available = new Availability();
        available.setAvailable(true);
        availableUpc.setAvailability(available);
        Item availableItem = new Item();
        availableItem.setUpc(availableUpc);
        Upc unAvailableUpc = new Upc();
        unAvailableUpc.setId(2);
        Availability unAvailable = new Availability();
        unAvailable.setAvailable(false);
        unAvailableUpc.setAvailability(unAvailable);
        Item unAvailableItem = new Item();
        unAvailableItem.setUpc(unAvailableUpc);
        lists.get(0).setItems(Arrays.asList(availableItem, unAvailableItem));
        return null;
    }

    @Test
    public void testGetListByListGuidWithExpandUser() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_USER);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(userResponse);
        Mockito.when(listRestClient.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam)).thenReturn(restResponse);

        CustomerList actualWishList = wishlistService.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam, BASE_URI);
        Assert.assertNotNull(actualWishList.getUser());
        Assert.assertEquals(USER_ID, (long) actualWishList.getUser().getId());
        Assert.assertEquals(USER_FIRST_NAME, actualWishList.getUser().getProfile().getFirstName());
    }

    @Test
    public void testGetListByListGuidWithoutExpandUser() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(null);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(userResponse);
        Mockito.when(listRestClient.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam)).thenReturn(restResponse);

        CustomerList actualWishList = wishlistService.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam, BASE_URI);
        Assert.assertNull(actualWishList.getUser());
    }

    @Test(description = "wishlist.v1.create.invaliduser = false", expectedExceptions = ListServiceException.class)
    public void testGetListByListGuidUserNotFound() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_USER);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenThrow(invalidUserIdException);
        Mockito.when(listRestClient.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam)).thenReturn(restResponse);
        try {
            wishlistServiceCreateInvalidUserOff.getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam, BASE_URI);
            Mockito.verify(customerUserService).retrieveUser(USER_ID, USER_GUID, true);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testGetListByListGuidWithErrorResponse() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(INVALID_EXPAND_OPTION_RESPONSE);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.when(listRestClient.getListByGuid(any(), any(), any())).thenReturn(restResponse);
        try {
            wishlistService.getListByGuid(LIST_GUID, new ListQueryParam(), new PaginationQueryParam(), BASE_URI);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(restResponse.getBody(), e.getServiceError());
            throw e;
        }
    }

    @Test
    public void testDeleteList() {

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setGuestUser(false);

        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());

        UserResponse userResponse = new UserResponse();
        Mockito.when(customerUserService.retrieveUser(userQueryParam.getUserId(), userQueryParam.getUserGuid(), false)).thenReturn(userResponse);
        Mockito.when(listRestClient.deleteList(SECURE_TOKEN, LIST_GUID, userQueryParam)).thenReturn(restResponse);

        wishlistService.deleteList(SECURE_TOKEN, LIST_GUID, userQueryParam);
        verify(customerUserService).validateNotBothUserIdAndGuidSpecified(null, USER_GUID);
        verify(customerUserService).retrieveUser(userQueryParam.getUserId(), userQueryParam.getUserGuid(), false);
        verify(customerUserService).populateUserQueryParam(userQueryParam, userResponse, false);
        Mockito.verify(listRestClient).deleteList(SECURE_TOKEN, LIST_GUID, userQueryParam);

    }

    @Test(description = "wishlist.v1.create.invaliduser = false", expectedExceptions = ListServiceException.class)
    public void testDeleteListUserNotFound() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setGuestUser(false);

        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenThrow(invalidUserIdException);

        try {
            wishlistServiceCreateInvalidUserOff.deleteList(SECURE_TOKEN, LIST_GUID, userQueryParam);
            verify(customerUserService).retrieveUser(userQueryParam.getUserId(), userQueryParam.getUserGuid(), true);
            verify(listRestClient, never()).deleteList(any(), any(), any());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testDeleteListWithInvalidStatus() {
        UserQueryParam userQueryParam = new UserQueryParam();

        String errorMessage = "{error: 'Some error happend!'}";
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        restResponse.setBody(errorMessage);

        Mockito.when(listRestClient.deleteList(SECURE_TOKEN, LIST_GUID, userQueryParam)).thenReturn(restResponse);

        try {
            wishlistService.deleteList(SECURE_TOKEN, LIST_GUID, userQueryParam);
            Assert.assertTrue("Invalid response status", false);
        } catch (ListServiceException ex) {
            Mockito.verify(listRestClient).deleteList(SECURE_TOKEN, LIST_GUID, userQueryParam);
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatusCode());
            Assert.assertEquals(errorMessage, ex.getServiceError());
        }
    }

    @Test
    public void testUpdateWishlist() {
        CustomerList requestBody = new CustomerList();
        WishList wishList = new WishList();
        wishList.setOnSaleNotify(true);
        wishList.setDefaultList(true);
        wishList.setName("testListName");
        requestBody.setWishlist(Arrays.asList(wishList));

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setGuestUser(true);

        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());

        Mockito.when(listRestClient.updateWishlist(SECURE_TOKEN, LIST_GUID, userQueryParam, requestBody)).thenReturn(restResponse);

        wishlistService.updateList(SECURE_TOKEN, LIST_GUID, userQueryParam, requestBody);
        verify(customerUserService).validateNotBothUserIdAndGuidSpecified(USER_ID, null);
        Mockito.verify(listRestClient).updateWishlist(SECURE_TOKEN, LIST_GUID, userQueryParam, requestBody);

    }

    @Test(description = "wishlist.v1.create.invaliduser = false", expectedExceptions = ListServiceException.class)
    public void testUpdateListUserNotFound() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setGuestUser(false);

        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenThrow(invalidUserIdException);

        try {
            wishlistServiceCreateInvalidUserOff.updateList(SECURE_TOKEN, LIST_GUID, userQueryParam, new CustomerList());
            verify(customerUserService).retrieveUser(userQueryParam.getUserId(), userQueryParam.getUserGuid(), true);
            verify(listRestClient, never()).updateWishlist(any(), any(), any(), any());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testUpdateWishlistWithWrongStatus() {

        CustomerList requestBody = new CustomerList();
        UserQueryParam userQueryParam = new UserQueryParam();

        String errorMessage = "{error: 'Some error happend!'}";
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        restResponse.setBody(errorMessage);

        Mockito.when(listRestClient.updateWishlist(SECURE_TOKEN, LIST_GUID, userQueryParam, requestBody)).thenReturn(restResponse);

        try {
            wishlistService.updateList(SECURE_TOKEN, LIST_GUID, userQueryParam, requestBody);
            Assert.assertTrue("Invalid response status", false);
        } catch (ListServiceException ex) {
            Mockito.verify(listRestClient).updateWishlist(SECURE_TOKEN, LIST_GUID, userQueryParam, requestBody);
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatusCode());
            Assert.assertEquals(errorMessage, ex.getServiceError());
        }
    }

    @Test
    public void testDeleteItem() throws IOException {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());
        WishListDTO wishListResponse = new WishListDTO();
        wishListResponse.setUserId(USER_ID);
        UserResponse userResponse = createUserResponse(USER_ID, null, true);

        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse wishlistRestResponse = new RestResponse();
        wishlistRestResponse.setBody(wishlistsResponseJson);
        wishlistRestResponse.setStatusCode(Response.Status.OK.getStatusCode());

        Mockito.when(listRestClient.getListByGuid(eq(LIST_GUID), any(), any())).thenReturn(wishlistRestResponse);
        Mockito.when(customerUserService.retrieveUserGuestInfo(USER_ID, null, false)).thenReturn(userResponse.isGuestUser());
        Mockito.when(listRestClient.deleteItem(LIST_GUID, ITEM_GUID, userResponse.isGuestUser())).thenReturn(restResponse);
        wishlistService.deleteItem(LIST_GUID, ITEM_GUID);
        Mockito.verify(listRestClient).deleteItem(LIST_GUID, ITEM_GUID, userResponse.isGuestUser());
    }

    @Test(description = "wishlist.v1.create.invaliduser = false", expectedExceptions = ListServiceException.class)
    public void testDeleteItemUserNotFound() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse wishlistRestResponse = new RestResponse();
        wishlistRestResponse.setBody(wishlistsResponseJson);
        wishlistRestResponse.setStatusCode(Response.Status.OK.getStatusCode());

        Mockito.when(customerUserService.retrieveUserGuestInfo(any(), any(), anyBoolean())).thenThrow(invalidUserIdException);
        Mockito.when(listRestClient.getListByGuid(any(), any(), any())).thenReturn(wishlistRestResponse);

        try {
            wishlistServiceCreateInvalidUserOff.deleteItem(LIST_GUID, ITEM_GUID);
            verify(customerUserService).retrieveUserGuestInfo(USER_ID, USER_GUID, true);
            verify(listRestClient, never()).deleteItem(any(), any(), any());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testDeleteItemWithInvalidStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(TEST_ERROR_RESPONSE);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.when(listRestClient.deleteItem(any(), any(), any())).thenReturn(restResponse);
        try {
            wishlistService.deleteItem(LIST_GUID, ITEM_GUID);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(restResponse.getBody(), e.getServiceError());
            throw e;
        }
    }

    @Test
    public void testDeleteFavoriteItem() throws IOException {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());
        WishListDTO wishListResponse = new WishListDTO();
        wishListResponse.setUserId(USER_ID);
        UserResponse userResponse = createUserResponse(USER_ID, null, true);

        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse wishlistRestResponse = new RestResponse();
        wishlistRestResponse.setBody(wishlistsResponseJson);
        wishlistRestResponse.setStatusCode(Response.Status.OK.getStatusCode());

        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setUpcId(111);
        listQueryParam.setProductId(222);
        Mockito.when(listRestClient.getListByGuid(eq(LIST_GUID), any(), any())).thenReturn(wishlistRestResponse);
        Mockito.when(customerUserService.retrieveUserGuestInfo(USER_ID, null, false)).thenReturn(userResponse.isGuestUser());
        Mockito.when(listRestClient.deleteFavoriteItem(LIST_GUID, userResponse.isGuestUser(), listQueryParam)).thenReturn(restResponse);
        wishlistService.deleteFavoriteItem(LIST_GUID, listQueryParam);
        Mockito.verify(listRestClient).deleteFavoriteItem(LIST_GUID, userResponse.isGuestUser(), listQueryParam);
        Mockito.verify(listCatalogService, never()).findUpcIdFromProduct(any());
    }

    @Test(description = "wishlist.v1.create.invaliduser = false", expectedExceptions = ListServiceException.class)
    public void testDeleteFavoriteItemUserNotFound() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse wishlistRestResponse = new RestResponse();
        wishlistRestResponse.setBody(wishlistsResponseJson);
        wishlistRestResponse.setStatusCode(Response.Status.OK.getStatusCode());

        Mockito.when(customerUserService.retrieveUserGuestInfo(any(), any(), anyBoolean())).thenThrow(invalidUserIdException);
        Mockito.when(listRestClient.getListByGuid(any(), any(), any())).thenReturn(wishlistRestResponse);

        try {
            wishlistServiceCreateInvalidUserOff.deleteFavoriteItem(LIST_GUID, new ListQueryParam());
            verify(customerUserService).retrieveUserGuestInfo(USER_ID, USER_GUID, true);
            verify(listRestClient, never()).deleteFavoriteItem(any(), any(), any());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testDeleteFavoriteItemNoUpcParam() throws IOException {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());
        WishListDTO wishListResponse = new WishListDTO();
        wishListResponse.setUserId(USER_ID);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(USER_ID);
        userResponse.setGuestUser(true);

        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse wishlistRestResponse = new RestResponse();
        wishlistRestResponse.setBody(wishlistsResponseJson);
        wishlistRestResponse.setStatusCode(Response.Status.OK.getStatusCode());

        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setProductId(222);
        Mockito.when(listCatalogService.findUpcIdFromProduct(listQueryParam.getProductId())).thenReturn(111);
        Mockito.when(listRestClient.getListByGuid(eq(LIST_GUID), any(), any())).thenReturn(wishlistRestResponse);
        Mockito.when(customerUserService.retrieveUserGuestInfo(USER_ID, null, false)).thenReturn(userResponse.isGuestUser());
        Mockito.when(listRestClient.deleteFavoriteItem(LIST_GUID, userResponse.isGuestUser(), listQueryParam)).thenReturn(restResponse);
        wishlistService.deleteFavoriteItem(LIST_GUID, listQueryParam);
        Mockito.verify(listRestClient).deleteFavoriteItem(LIST_GUID, userResponse.isGuestUser(), listQueryParam);
        Assert.assertEquals(Integer.valueOf(111), listQueryParam.getUpcId());
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testDeleteFavoriteItemWithInvalidStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(TEST_ERROR_RESPONSE);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.when(listRestClient.deleteFavoriteItem(any(), any(), any())).thenReturn(restResponse);
        try {
            wishlistService.deleteFavoriteItem(LIST_GUID, new ListQueryParam());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(restResponse.getBody(), e.getServiceError());
            throw e;
        }
    }

    @Test
    public void testUpdateItemPriority() {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);

        Item item = new Item();
        item.setPriority(PRIORITY);
        Mockito.when(customerUserService.retrieveUser(USER_ID, null, false)).thenReturn(userResponse);
        Mockito.when(listRestClient.updateItemPriority(any(), any(), any(), any(), any(), any(), any())).thenReturn(restResponse);
        wishlistService.updateItemPriority(SECURE_TOKEN, LIST_GUID, ITEM_GUID, USER_ID, null, item);
        verify(customerUserService).validateNotBothUserIdAndGuidSpecified(USER_ID, null);
        Mockito.verify(listRestClient).updateItemPriority(eq(SECURE_TOKEN), eq(LIST_GUID), eq(ITEM_GUID), eq(USER_ID), eq(USER_GUID), eq(false), any());
    }

    @Test(description = "wishlist.v1.create.invaliduser = false", expectedExceptions = ListServiceException.class)
    public void testUpdateItemPriorityUserNotFound() {
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenThrow(invalidUserIdException);
        try {
            wishlistServiceCreateInvalidUserOff.updateItemPriority(SECURE_TOKEN, LIST_GUID, ITEM_GUID, USER_ID, null, new Item());
            verify(customerUserService).retrieveUser(USER_ID, USER_GUID, true);
            verify(listRestClient, never()).updateItemPriority(any(), any(), any(), any(), any(), any(), any());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testUpdateItemPriorityWithInvalidStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(TEST_ERROR_RESPONSE);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());

        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(new UserResponse());
        Mockito.when(listRestClient.updateItemPriority(any(), any(), any(), any(), any(), anyBoolean(), any())).thenReturn(restResponse);
        try {
            wishlistService.updateItemPriority(SECURE_TOKEN, LIST_GUID, ITEM_GUID, USER_ID, USER_GUID, new Item());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(restResponse.getBody(), e.getServiceError());
            throw e;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testCreateWishListInvalidInputWhenUserIdWithUserGuid() {
        try {
            CustomerList requestBody = new CustomerList();
            User user = new User();
            user.setId(USER_ID);
            user.setGuid(USER_GUID);
            requestBody.setUser(user);
            WishList wishList = new WishList();
            wishList.setOnSaleNotify(true);
            wishList.setDefaultList(true);
            wishList.setName("testListName");
            requestBody.setWishlist(Arrays.asList(wishList));
            ListServiceException exception = new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.INVALID_INPUT);
            Mockito.doThrow(exception).when(customerUserService).validateNotBothUserIdAndGuidSpecified(any(), any());
            wishlistService.createWishList(SECURE_TOKEN, requestBody);
            Mockito.verify(customerUserService).validateNotBothUserIdAndGuidSpecified(USER_ID, USER_GUID);
        } catch (ListServiceException ex) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_INPUT.getInternalCode(), ex.getServiceErrorCode());
            throw ex;
        }
    }

    @Test
    public void testCreateWishListWithoutItems() throws IOException {
        CustomerList requestBody = new CustomerList();
        User userWithGuid = getUserWithGuid();
        requestBody.setUser(userWithGuid);

        WishList wishList = new WishList();
        wishList.setOnSaleNotify(true);
        wishList.setDefaultList(true);
        wishList.setName("testListName");
        requestBody.setWishlist(Arrays.asList(wishList));

        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);

        Mockito.when(customerUserService.retrieveUser(userWithGuid.getId(), userWithGuid.getGuid(), true)).thenReturn(userResponse);
        Mockito.doCallRealMethod().when(customerUserService).populateUserQueryParam(any(), any(), any());

        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_CREATE_LIST_WITHOUT_ITEMS_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        Mockito.when(listRestClient.createList(any(), any(), any())).thenReturn(restResponse);

        CustomerList createdList = wishlistService.createWishList(SECURE_TOKEN, requestBody);

        Assert.assertNotNull(createdList);
        Assert.assertEquals(USER_ID, createdList.getUser().getId().longValue());
        Assert.assertEquals(USER_GUID, createdList.getUser().getGuid());
        Assert.assertFalse(createdList.getUser().isGuestUser());
        Assert.assertNotNull(createdList.getWishlist().get(0));
        Assert.assertNull(createdList.getWishlist().get(0).getItems());
        Assert.assertNotNull(createdList.getMeta());
        Assert.assertNotNull(createdList.getMeta().getAnalytics());

        verify(customerUserService).retrieveUser(userWithGuid.getId(), userWithGuid.getGuid(), true);
        verify(listCatalogService).populateWishListItemDetailsForNewItems(requestBody.getWishlist().get(0));
        verify(listRestClient).createList(any(), any(), any());
        verify(listCatalogService).copyItemCatalogDetails(any(WishList.class), any(WishList.class));

    }

    @Test
    public void testCreateWishListWithItems() throws IOException {
        CustomerList requestBody = new CustomerList();
        User userWithGuid = getUserWithGuid();
        requestBody.setUser(userWithGuid);

        WishList wishList = new WishList();
        wishList.setOnSaleNotify(true);
        wishList.setDefaultList(true);
        wishList.setName("testListName");
        Item item = new Item();
        Upc upc = new Upc();
        upc.setId(123);
        item.setUpc(upc);
        wishList.setItems(Arrays.asList(item));
        requestBody.setWishlist(Arrays.asList(wishList));

        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);

        Mockito.when(customerUserService.retrieveUser(userWithGuid.getId(), userWithGuid.getGuid(), true)).thenReturn(userResponse);
        Mockito.doCallRealMethod().when(customerUserService).populateUserQueryParam(any(), any(), any());

        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_CREATE_LIST_WITH_ITEMS_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        Mockito.when(listRestClient.createList(any(), any(), any())).thenReturn(restResponse);

        CustomerList createdList = wishlistService.createWishList(SECURE_TOKEN, requestBody);

        Assert.assertNotNull(createdList);
        Assert.assertEquals(USER_ID, createdList.getUser().getId().longValue());
        Assert.assertEquals(USER_GUID, createdList.getUser().getGuid());
        Assert.assertFalse(createdList.getUser().isGuestUser());
        Assert.assertNotNull(createdList.getWishlist().get(0));
        Assert.assertNotNull(createdList.getWishlist().get(0).getItems());
        Assert.assertEquals(1, createdList.getWishlist().get(0).getItems().size());
        Assert.assertNotNull(createdList.getMeta());
        Assert.assertNotNull(createdList.getMeta().getAnalytics());


        verify(customerUserService).retrieveUser(any(), any(), anyBoolean());
        verify(listCatalogService).populateWishListItemDetailsForNewItems(requestBody.getWishlist().get(0));
        verify(listRestClient).createList(any(), any(), any());
        verify(listCatalogService).copyItemCatalogDetails(any(WishList.class), any(WishList.class));
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testCreateListWithWrongStatus() {

        CustomerList requestBody = new CustomerList();
        requestBody.setWishlist(Arrays.asList(new WishList()));
        requestBody.setUser(new User());

        String errorMessage = "{error: 'Some error happend!'}";
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        restResponse.setBody(errorMessage);

        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);
        Mockito.when(customerUserService.retrieveUser(requestBody.getUser().getId(), requestBody.getUser().getGuid(), true)).thenReturn(userResponse);
        Mockito.when(listRestClient.createList(any(), any(), any())).thenReturn(restResponse);

        try {
            wishlistService.createWishList(SECURE_TOKEN, requestBody);
        } catch (ListServiceException ex) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatusCode());
            Assert.assertEquals(errorMessage, ex.getServiceError());
            throw ex;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testCreateListUserInputNull() {
        CustomerList requestBody = new CustomerList();
        requestBody.setWishlist(Arrays.asList(new WishList()));
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenThrow(new ListServiceException());
        wishlistService.createWishList(SECURE_TOKEN, requestBody);
    }

    @Test(description = "should always return an error when user is not found", expectedExceptions = ListServiceException.class)
    public void testCreateListInvalidUser() {
        CustomerList requestBody = new CustomerList();
        User user = new User();
        user.setId(USER_ID);
        requestBody.setUser(user);
        requestBody.setWishlist(Arrays.asList(new WishList()));
        Mockito.when(customerUserService.retrieveUser(USER_ID, null, true)).thenThrow(invalidUserIdException);

        try {
            wishlistService.createWishList(SECURE_TOKEN, requestBody);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testCreateListWithItemWithUpcAndProduct() {
        CustomerList requestBody = createCustomerListWithUpcAndProduct();
        ListCookies cookies = new ListCookies(USER_ID, USER_GUID, SECURE_TOKEN);
        try {
            wishlistService.createWishList(SECURE_TOKEN, requestBody);
        } catch (ListServiceException ex) {
            Assert.assertEquals(ErrorConstants.INVALID_INPUT, ex.getMessage());
            throw ex;
        }
    }

    @Test(description = "user input is null, wishlist.create.user.enabled = true")
    public void testAddToDefaultWishlistCreateUser() {
        CustomerList requestBody = new CustomerList();
        requestBody.setUser(null);

        WishList wishList = new WishList();
        Item item = new Item();
        Upc upc = new Upc();
        upc.setId(123);
        item.setUpc(upc);
        wishList.setItems(Arrays.asList(item));
        requestBody.setWishlist(Arrays.asList(wishList));

        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);

        Mockito.when(customerUserService.createGuestUser()).thenReturn(userResponse);

        wishlistService.addItemToDefaultWishlist(requestBody);

        verify(customerUserService).createGuestUser();
        verify(listCatalogService).populateWishListItemDetailsForNewItems(requestBody.getWishlist().get(0));
        verify(listRestClient).addItemToDefaultWishlist(any(), any(), any());
        verify(listCatalogService).copyItemCatalogDetails(any(WishList.class), any(WishList.class));
    }

    @Test(description = "user input is null, wishlist.create.user.enabled = false")
    public void testAddToDefaultWishlistNotCreateUser() {
        CustomerList requestBody = new CustomerList();
        requestBody.setUser(null);

        WishList wishList = new WishList();
        Item item = new Item();
        Upc upc = new Upc();
        upc.setId(123);
        item.setUpc(upc);
        wishList.setItems(Arrays.asList(item));
        requestBody.setWishlist(Arrays.asList(wishList));

        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);
        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(userResponse);

        wishlistServiceCreateUserOff.addItemToDefaultWishlist(requestBody);

        verify(customerUserService, never()).createGuestUser();
        verify(listCatalogService).populateWishListItemDetailsForNewItems(requestBody.getWishlist().get(0));
        verify(listRestClient).addItemToDefaultWishlist(any(), any(), any());
        verify(listCatalogService).copyItemCatalogDetails(any(WishList.class), any(WishList.class));
    }

    @Test(description = "userId is NOT null, userGuid is null, wishlist.v1.create.invaliduser = true")
    public void testAddToDefaultWishlistCreateUserAfterEmptyUserResponseDueToKillswitch() throws IOException {
        CustomerList requestBody = new CustomerList();
        User user = new User();
        user.setId(USER_ID);
        user.setGuid(null);
        requestBody.setUser(user);

        WishList wishList = new WishList();
        Item item = new Item();
        Upc upc = new Upc();
        upc.setId(123);
        item.setUpc(upc);
        wishList.setItems(Arrays.asList(item));
        requestBody.setWishlist(Arrays.asList(wishList));

        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);

        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(null);
        Mockito.when(customerUserService.createGuestUser(any())).thenReturn(userResponse);

        wishlistService.addItemToDefaultWishlist(requestBody);

        verify(customerUserService).createGuestUser(USER_ID);
        verify(listCatalogService).populateWishListItemDetailsForNewItems(requestBody.getWishlist().get(0));
        verify(listRestClient).addItemToDefaultWishlist(any(), any(), any());
        verify(listCatalogService).copyItemCatalogDetails(any(WishList.class), any(WishList.class));
    }

    @Test(description = "userId is NOT null, userGuid is NOT null, wishlist.v1.create.invaliduser = true")
    public void testAddToDefaultWishlistNotCreateUserAfterEmptyUserResponseWhenBothUserParamsSpecified() {
        CustomerList requestBody = new CustomerList();
        User user = new User();
        user.setId(USER_ID);
        user.setGuid(USER_GUID);
        requestBody.setUser(user);

        WishList wishList = new WishList();
        Item item = new Item();
        Upc upc = new Upc();
        upc.setId(123);
        item.setUpc(upc);
        wishList.setItems(Arrays.asList(item));
        requestBody.setWishlist(Arrays.asList(wishList));

        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(null);

        wishlistService.addItemToDefaultWishlist(requestBody);

        verify(customerUserService, never()).createGuestUser();
        verify(customerUserService, never()).createGuestUser(USER_ID);
        verify(listCatalogService).populateWishListItemDetailsForNewItems(requestBody.getWishlist().get(0));
        verify(listRestClient).addItemToDefaultWishlist(any(), any(), any());
        verify(listCatalogService).copyItemCatalogDetails(any(WishList.class), any(WishList.class));
    }

    @Test
    public void testAddToDefaultWishlist() throws IOException {
        CustomerList requestBody = new CustomerList();
        User user = new User();
        user.setId(USER_ID);
        requestBody.setUser(user);

        WishList wishList = new WishList();
        Item item = new Item();
        Upc upc = new Upc();
        upc.setId(123);
        item.setUpc(upc);
        wishList.setItems(Arrays.asList(item));
        requestBody.setWishlist(Arrays.asList(wishList));

        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);

        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenReturn(userResponse);

        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_ADD_ITEM_TO_DEFAULT_LIST);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        Mockito.when(listRestClient.addItemToDefaultWishlist(any(), any(), any())).thenReturn(restResponse);

        CustomerList createdList = wishlistService.addItemToDefaultWishlist(requestBody);

        Assert.assertNotNull(createdList);
        Assert.assertEquals(USER_ID, createdList.getUser().getId().longValue());
        Assert.assertEquals(USER_GUID, createdList.getUser().getGuid());
        Assert.assertFalse(createdList.getUser().isGuestUser());
        Assert.assertNotNull(createdList.getWishlist().get(0));
        Assert.assertNotNull(createdList.getWishlist().get(0).getItems());
        Assert.assertEquals(2, createdList.getWishlist().get(0).getItems().size());
        Assert.assertEquals(USER_FIRST_NAME, createdList.getUser().getProfile().getFirstName());
        Assert.assertNotNull(createdList.getMeta());
        Assert.assertNotNull(createdList.getMeta().getAnalytics());

        verify(customerUserService).retrieveUser(USER_ID, null, false);
        verify(customerUserService).populateUserQueryParamWithFirstName(any(), any());
        verify(listCatalogService).populateWishListItemDetailsForNewItems(requestBody.getWishlist().get(0));
        verify(listRestClient).addItemToDefaultWishlist(any(), any(), any());
        verify(listCatalogService).copyItemCatalogDetails(any(WishList.class), any(WishList.class));
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testAddItemToDefaultListWithWrongStatus() {

        CustomerList requestBody = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(Arrays.asList(new Item()));
        requestBody.setWishlist(Arrays.asList(wishList));
        requestBody.setUser(new User());

        String errorMessage = "{error: 'Some error happend!'}";
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        restResponse.setBody(errorMessage);

        Mockito.when(listRestClient.addItemToDefaultWishlist(any(), any(), any())).thenReturn(restResponse);

        try {
            wishlistService.addItemToDefaultWishlist(requestBody);
        } catch (ListServiceException ex) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatusCode());
            Assert.assertEquals(errorMessage, ex.getServiceError());
            throw ex;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testAddItemToDefaultInvalidUserId() {

        CustomerList requestBody = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(Arrays.asList(new Item()));
        requestBody.setWishlist(Arrays.asList(wishList));
        User user = new User();
        user.setId(USER_ID);
        requestBody.setUser(user);

        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenThrow(invalidUserIdException);
        try {
            wishlistService.addItemToDefaultWishlist(requestBody);
        } catch (ListServiceException ex) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), ex.getServiceErrorCode());
            throw ex;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testAddItemToDefaultListWithUpcAndProduct() {
        CustomerList requestBody = createCustomerListWithUpcAndProduct();
        try {
            wishlistService.addItemToDefaultWishlist(requestBody);
        } catch (ListServiceException ex) {
            Assert.assertEquals(ErrorConstants.INVALID_INPUT, ex.getMessage());
            throw ex;
        }
    }

    @Test
    public void addItemToGivenList() throws IOException {
        CustomerList requestBody = new CustomerList();
        User userWithGuid = new User();
        userWithGuid.setGuid(USER_GUID);
        requestBody.setUser(userWithGuid);

        WishList wishList = new WishList();
        Item item = new Item();
        Upc upc = new Upc();
        upc.setId(123);
        item.setUpc(upc);
        wishList.setItems(Arrays.asList(item));
        requestBody.setWishlist(Arrays.asList(wishList));

        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);

        Mockito.when(customerUserService.retrieveUser(null, USER_GUID, false)).thenReturn(userResponse);
        Mockito.doCallRealMethod().when(customerUserService).populateUserQueryParam(any(), any(), any());

        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_ADD_ITEM_TO_DEFAULT_LIST);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        Mockito.when(listRestClient.addItemToGivenList(any(), any(), any())).thenReturn(restResponse);

        UserQueryParam userQueryParam = new UserQueryParam();
        CustomerList createdList = wishlistService.addItemToGivenList(LIST_GUID, requestBody, userQueryParam);

        Assert.assertNotNull(createdList);
        Assert.assertEquals(USER_ID, createdList.getUser().getId().longValue());
        Assert.assertEquals(USER_GUID, createdList.getUser().getGuid());
        Assert.assertFalse(createdList.getUser().isGuestUser());
        Assert.assertNotNull(createdList.getWishlist().get(0));
        Assert.assertNotNull(createdList.getWishlist().get(0).getItems());
        Assert.assertEquals(2, createdList.getWishlist().get(0).getItems().size());
        Assert.assertNotNull(createdList.getMeta());
        Assert.assertNotNull(createdList.getMeta().getAnalytics());

        verify(customerUserService).validateNotBothUserIdAndGuidSpecified(null, USER_GUID);
        verify(customerUserService).retrieveUser(null, USER_GUID, false);
        verify(customerUserService).populateUserQueryParam(userQueryParam, userResponse, false);
        verify(listCatalogService).populateWishListItemDetailsForNewItems(requestBody.getWishlist().get(0));
        verify(listRestClient).addItemToGivenList(any(), any(), any());
        verify(listCatalogService).copyItemCatalogDetails(any(WishList.class), any(WishList.class));
    }

    @Test(description = "wishlist.v1.create.invaliduser = false", expectedExceptions = ListServiceException.class)
    public void testAddItemToGivenListUserNotFound() {
        CustomerList requestBody = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(Arrays.asList(new Item()));
        User user = new User();
        user.setId(USER_ID);
        user.setGuid(USER_GUID);
        requestBody.setUser(user);
        requestBody.setWishlist(Arrays.asList(wishList));

        Mockito.when(customerUserService.retrieveUser(USER_ID, USER_GUID, true)).thenThrow(invalidUserIdException);

        try {
            wishlistServiceCreateInvalidUserOff.addItemToGivenList(LIST_GUID, requestBody, new UserQueryParam());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testAddItemToGivenListWithWrongStatus() {

        CustomerList requestBody = new CustomerList();
        requestBody.setWishlist(Arrays.asList(new WishList()));
        requestBody.setUser(new User());

        String errorMessage = "{error: 'Some error happend!'}";
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        restResponse.setBody(errorMessage);

        Mockito.when(listRestClient.addItemToGivenList(any(), any(), any())).thenReturn(restResponse);

        try {
            wishlistService.addItemToGivenList(LIST_GUID, requestBody, new UserQueryParam());
        } catch (ListServiceException ex) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatusCode());
            Assert.assertEquals(errorMessage, ex.getServiceError());
            throw ex;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testAddItemToGivenListWithUpcAndProduct() {
        CustomerList requestBody = createCustomerListWithUpcAndProduct();
        try {
            wishlistService.addItemToGivenList(LIST_GUID, requestBody, new UserQueryParam());
        } catch (ListServiceException ex) {
            Assert.assertEquals(ErrorConstants.INVALID_INPUT, ex.getMessage());
            throw ex;
        }
    }

    @Test
    public void testMoveItemToWishlist() {
        CustomerList requestBody = new CustomerList();
        User userWithGuid = new User();
        userWithGuid.setGuid(USER_GUID);
        requestBody.setUser(userWithGuid);

        WishList wishList = new WishList();
        Item item = new Item();
        wishList.setItems(Arrays.asList(item));
        requestBody.setWishlist(Arrays.asList(wishList));

        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);

        Mockito.when(customerUserService.retrieveUser(userWithGuid.getId(), userWithGuid.getGuid(), false)).thenReturn(userResponse);
        Mockito.doCallRealMethod().when(customerUserService).populateUserQueryParam(any(), any(), any());

        wishlistService.moveItemToWishlist(SECURE_TOKEN, LIST_GUID, requestBody);

        verify(customerUserService).validateNotBothUserIdAndGuidSpecified(null, USER_GUID);
        verify(customerUserService).retrieveUser(userWithGuid.getId(), userWithGuid.getGuid(), false);
        verify(customerUserService).populateUserQueryParam(any(), any(), any());
        verify(listRestClient).moveItemToWishlist(any(), any(), any(), any());
    }

    @Test(description = "wishlist.v1.create.invaliduser = false", expectedExceptions = ListServiceException.class)
    public void testMoveItemToWishListUserNotFound() {
        CustomerList requestBody = new CustomerList();
        User userWithGuid = new User();
        userWithGuid.setGuid(USER_GUID);
        requestBody.setUser(userWithGuid);

        WishList wishList = new WishList();
        Item item = new Item();
        wishList.setItems(Arrays.asList(item));
        requestBody.setWishlist(Arrays.asList(wishList));

        Mockito.when(customerUserService.retrieveUser(any(), any(), anyBoolean())).thenThrow(invalidUserIdException);
        try {
            wishlistServiceCreateInvalidUserOff.moveItemToWishlist(SECURE_TOKEN, LIST_GUID, requestBody);
            verify(customerUserService).retrieveUserGuestInfo(USER_ID, USER_GUID, true);
            verify(listRestClient, never()).updateItemPriority(any(), any(), any(), any(), any(), any(), any());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testMoveItemToWishlistWithWrongStatus() {
        CustomerList requestBody = new CustomerList();
        WishList wishList = new WishList();
        Item item = new Item();
        wishList.setItems(Arrays.asList(item));
        requestBody.setWishlist(Arrays.asList(wishList));
        requestBody.setUser(new User());

        String errorMessage = "{error: 'Some error happend!'}";
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        restResponse.setBody(errorMessage);

        Mockito.when(listRestClient.moveItemToWishlist(any(), any(), any(), any())).thenReturn(restResponse);

        ListCookies cookies = new ListCookies(USER_ID, USER_GUID, SECURE_TOKEN);

        try {
            wishlistService.moveItemToWishlist(SECURE_TOKEN, LIST_GUID, requestBody);
        } catch (ListServiceException ex) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatusCode());
            Assert.assertEquals(errorMessage, ex.getServiceError());
            throw ex;
        }
    }

    @Test
    public void testGetFavoriteItemsFromDefaultList() {
        String response = "{ \"list\": { \"listGuid\": \"testListGuid\", \"products\": [ { \"pid\": 111 }, { \"pid\": 222 } ] } }";
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(response);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        UserResponse userResponse = createUserResponse(USER_ID, USER_GUID, false);
        Mockito.when(customerUserService.retrieveUser(null, USER_GUID, false)).thenReturn(userResponse);
        Mockito.when(listRestClient.getFavoriteItemsFromDefaultList(USER_ID)).thenReturn(restResponse);
        FavoriteList favoriteList = wishlistService.getFavoriteItemsFromDefaultList(USER_GUID);
        Assert.assertNotNull(favoriteList);
        Assert.assertEquals(LIST_GUID, favoriteList.getListGuid());
        Assert.assertTrue(CollectionUtils.isNotEmpty(favoriteList.getProducts()));
        Assert.assertEquals(111, favoriteList.getProducts().get(0).getPid());
        Assert.assertEquals(222, favoriteList.getProducts().get(1).getPid());
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testGetFavoriteItemsFromDefaultListWithInvalidStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(TEST_ERROR_RESPONSE);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.when(listRestClient.getFavoriteItemsFromDefaultList(any())).thenReturn(restResponse);
        try {
            wishlistService.getFavoriteItemsFromDefaultList(USER_GUID);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(restResponse.getBody(), e.getServiceError());
            throw e;
        }
    }

    @Test
    public void testMergeList() {
        CustomerListMerge customerListMerge = new CustomerListMerge();
        customerListMerge.setGuestUserId(USER_ID_2);
        customerListMerge.setUserId(USER_ID);

        UserResponse guestUser = createUserResponse(USER_ID, USER_GUID, true);
        UserResponse user = createUserResponse(USER_ID_2, USER_GUID_2, false);

        Mockito.when(customerUserService.retrieveUser(USER_ID, null, false)).thenReturn(user);
        Mockito.when(customerUserService.retrieveUser(USER_ID_2, null, false)).thenReturn(guestUser);

        wishlistService.mergeList(SECURE_TOKEN, customerListMerge);

        verify(customerUserService).retrieveUser(USER_ID, null, false);
        verify(customerUserService).retrieveUser(USER_ID_2, null, false);
        verify(listRestClient).mergeList(eq(SECURE_TOKEN), any());
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testMergeListWithInvalidStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(TEST_ERROR_RESPONSE);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.when(listRestClient.mergeList(any(), any())).thenReturn(restResponse);
        try {
            wishlistService.mergeList(SECURE_TOKEN, new CustomerListMerge());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(restResponse.getBody(), e.getServiceError());
            throw e;
        }
    }

    @Test
    public void testPrepareMergeListRequestNoUserFound() {

        CustomerListMerge customerListMerge = new CustomerListMerge();
        MergeListRequest mergeRequest = wishlistService.prepareMergeListRequest(customerListMerge);

        Assert.assertNotNull(mergeRequest);
        Assert.assertNull(mergeRequest.getSrcUserId());
        Assert.assertNull(mergeRequest.getSrcGuestUser());
        Assert.assertNull(mergeRequest.getTargetUserId());
        Assert.assertNull(mergeRequest.getTargetGuestUser());
        Assert.assertNull(mergeRequest.getTargetUserFirstName());
        Assert.assertNull(mergeRequest.getTargetUserGuid());
    }

    @Test
    public void testPrepareMergeListRequestInvalidListMerge() {
        MergeListRequest mergeRequest = wishlistService.prepareMergeListRequest(null);

        Assert.assertNotNull(mergeRequest);
        Assert.assertNull(mergeRequest.getSrcUserId());
        Assert.assertNull(mergeRequest.getSrcGuestUser());
        Assert.assertNull(mergeRequest.getTargetUserId());
        Assert.assertNull(mergeRequest.getTargetGuestUser());
        Assert.assertNull(mergeRequest.getTargetUserFirstName());
        Assert.assertNull(mergeRequest.getTargetUserGuid());
    }

    @Test
    public void testPrepareMergeListRequestGUestUserSingedUser() {
        CustomerListMerge customerListMerge = new CustomerListMerge();
        customerListMerge.setGuestUserId(USER_ID_2);
        customerListMerge.setUserId(USER_ID);
        UserResponse guestUser = createUserResponse(USER_ID_2, USER_GUID_2, true);
        UserResponse user = createUserResponse(USER_ID, USER_GUID, false);

        Mockito.when(customerUserService.retrieveUser(USER_ID, null, false)).thenReturn(user);
        Mockito.when(customerUserService.retrieveUser(USER_ID_2, null, false)).thenReturn(guestUser);

        MergeListRequest mergeRequest = wishlistService.prepareMergeListRequest(customerListMerge);

        Assert.assertNotNull(mergeRequest);
        Assert.assertEquals(USER_ID_2, mergeRequest.getSrcUserId().intValue());
        Assert.assertEquals(true, mergeRequest.getSrcGuestUser());
        Assert.assertEquals(USER_ID, mergeRequest.getTargetUserId().intValue());
        Assert.assertEquals(false, mergeRequest.getTargetGuestUser());
        Assert.assertEquals(USER_FIRST_NAME, mergeRequest.getTargetUserFirstName());
        Assert.assertEquals(USER_GUID, mergeRequest.getTargetUserGuid());
    }

    @Test
    public void testPrepareMergeListRequestGuestUserGuestUser() {
        CustomerListMerge customerListMerge = new CustomerListMerge();
        customerListMerge.setGuestUserId(USER_ID_2);
        customerListMerge.setUserId(USER_ID);

        UserResponse srcUser = createUserResponse(USER_ID_2, USER_GUID_2, true);
        UserResponse targetUser = createUserResponse(USER_ID, USER_GUID, true);

        Mockito.when(customerUserService.retrieveUser(USER_ID_2, null, false)).thenReturn(srcUser);
        Mockito.when(customerUserService.retrieveUser(USER_ID, null, false)).thenReturn(targetUser);

        MergeListRequest mergeRequest = wishlistService.prepareMergeListRequest(customerListMerge);

        Assert.assertNotNull(mergeRequest);
        Assert.assertEquals(USER_ID_2, mergeRequest.getSrcUserId().intValue());
        Assert.assertEquals(true, mergeRequest.getSrcGuestUser());
        Assert.assertEquals(USER_ID, mergeRequest.getTargetUserId().intValue());
        Assert.assertEquals(true, mergeRequest.getTargetGuestUser());
        Assert.assertEquals(targetUser.getProfile().getFirstName(), mergeRequest.getTargetUserFirstName());
        Assert.assertEquals(USER_GUID, mergeRequest.getTargetUserGuid());
    }

    @Test
    public void testPrepareMergeListRequestUserNotFound() {
        CustomerListMerge customerListMerge = new CustomerListMerge();
        customerListMerge.setGuestUserId(USER_ID_2);
        customerListMerge.setUserId(USER_ID);

        MergeListRequest mergeRequest = wishlistService.prepareMergeListRequest(customerListMerge);

        Assert.assertNotNull(mergeRequest);
        Assert.assertNull(mergeRequest.getSrcUserId());
        Assert.assertNull(mergeRequest.getTargetUserId());
    }

    @Test
    public void testEmailShareWishlist() {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());

        EmailShare emailShare = new EmailShare();

        when(listRestClient.emailShareWishlist(any(), any(), any())).thenReturn(restResponse);

        wishlistService.emailShareWishlist(SECURE_TOKEN, LIST_GUID, emailShare);

       /* verify(listCatalogService).populateWishListItemDetails(anyList(), anyBoolean(), anyBoolean());
        verify(listCatalogService).getAvailableItems(anyList());
        verify(listSortingExecutor).sort(anyList(), eq(SortByField.TOP_PICK), eq(SortOrder.DESC));*/
        verify(listRestClient).emailShareWishlist(eq(SECURE_TOKEN), eq(LIST_GUID), any(EmailShareDTO.class));

    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testEmailShareWishlistInvalidStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(TEST_ERROR_RESPONSE);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        when(listRestClient.emailShareWishlist(any(), any(), any())).thenReturn(restResponse);

        EmailShare emailShare = new EmailShare();
        try {
            wishlistService.emailShareWishlist(SECURE_TOKEN, LIST_GUID, emailShare);
        } catch (ListServiceException ex) {
            Assert.assertEquals(TEST_ERROR_RESPONSE, ex.getServiceError());
            throw ex;
        }
    }

    @Test
    public void testPrepareEmailShareDTONullableEmailShare() throws UnsupportedEncodingException {
        EmailShareDTO emailShareDTO = wishlistService.prepareEmailShareDTO(LIST_GUID, null, SECURE_TOKEN);
        Assert.assertNull(emailShareDTO);
    }

    @Test
    public void testPrepareEmailShareDTO() throws IOException {
        EmailShare inputObj = new EmailShare();
        inputObj.setTo("test@gmail.com");
        inputObj.setMessage("testMessage");
        inputObj.setFirstName("testFirstName");
        inputObj.setLastName("testLastName");
        inputObj.setLink("http://test.com?userId=123&product=123");

        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());

        UserResponse user = createUserResponseWithCredentials(USER_ID, USER_GUID, USER_NAME);

        when(listRestClient.getListByGuid(any(), any(), any())).thenReturn(restResponse);
        when(customerUserService.retrieveUserProfile(SECURE_TOKEN,  USER_ID)).thenReturn(user);
        doAnswer(this::mockCatalogDataPopulation)
                .when(listCatalogService).populateWishListItemDetails(any(WishList.class), eq(true));

        EmailShareDTO emailShareDTO = wishlistService.prepareEmailShareDTO(LIST_GUID, inputObj, SECURE_TOKEN);

        Assert.assertNotNull(emailShareDTO);
        Assert.assertEquals(USER_NAME, emailShareDTO.getFrom());
        Assert.assertEquals("test@gmail.com", emailShareDTO.getTo());
        Assert.assertEquals("testMessage", emailShareDTO.getMessage());
        Assert.assertEquals("testFirstName", emailShareDTO.getFirstName());
        Assert.assertEquals("testLastName", emailShareDTO.getLastName());
        Assert.assertEquals("http://test.com?userId=123&product=123", emailShareDTO.getLink());

        Assert.assertNotNull(emailShareDTO.getItems());
        Assert.assertEquals(1, emailShareDTO.getItems().size());
        EmailItemDTO item = emailShareDTO.getItems().get(0);
        Assert.assertEquals(ITEM_GUID, item.getItemGuid());
        Assert.assertEquals(UPC_NUMBER, item.getUpcNumber().longValue());
        Assert.assertEquals(PRODUCT_ID, item.getProductId().intValue());
        Assert.assertEquals(PRODUCT_NAME, item.getProductName());
        Assert.assertEquals(IMAGE_URL, item.getImageUrl());
    }

    @Test
    public void testPrepareEmailShareDTOWithoutItems() throws IOException {
        EmailShare inputObj = new EmailShare();
        inputObj.setTo("test@gmail.com");
        inputObj.setMessage("testMessage");
        inputObj.setFirstName("testFirstName");
        inputObj.setLastName("testLastName");
        inputObj.setLink("http://test.com?userId=123&product=123");

        String wishlistsResponseJson = TestUtils.readFile(WISH_LIST_BY_LIST_GUID_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(wishlistsResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());

        UserResponse user = createUserResponseWithCredentials(USER_ID, USER_GUID, USER_NAME);

        when(listRestClient.getListByGuid(any(), any(), any())).thenReturn(restResponse);
        when(customerUserService.retrieveUserProfile(SECURE_TOKEN, USER_ID)).thenReturn(user);

        EmailShareDTO emailShareDTO = wishlistService.prepareEmailShareDTO(LIST_GUID, inputObj, SECURE_TOKEN);

        Assert.assertNotNull(emailShareDTO);
        Assert.assertEquals(USER_NAME, emailShareDTO.getFrom());
        Assert.assertEquals("test@gmail.com", emailShareDTO.getTo());
        Assert.assertEquals("testMessage", emailShareDTO.getMessage());
        Assert.assertEquals("testFirstName", emailShareDTO.getFirstName());
        Assert.assertEquals("testLastName", emailShareDTO.getLastName());
        Assert.assertEquals("http://test.com?userId=123&product=123", emailShareDTO.getLink());

        Assert.assertNull(emailShareDTO.getItems());
    }

    private Void mockCatalogDataPopulation(InvocationOnMock invocationOnMock) {
        WishList list = invocationOnMock.getArgumentAt(0, WishList.class);
        Item item = list.getItems().get(0);
        item.setItemGuid(ITEM_GUID);
        item.setUpc(new Upc());
        item.getUpc().setUpcNumber(UPC_NUMBER);
        item.getUpc().setAvailability(new Availability());
        item.getUpc().getAvailability().setAvailable(true);
        item.setProduct(new Product());
        item.getProduct().setId(PRODUCT_ID);
        item.getProduct().setName(PRODUCT_NAME);
        item.getProduct().setImageURL(IMAGE_URL);
        return null;
    }

    private User getUserWithGuid() {
        User user = new User();
        user.setGuid(USER_GUID);
        return user;
    }

    private CustomerList createCustomerListWithUpcAndProduct() {
        CustomerList requestBody = new CustomerList();
        WishList wishList = new WishList();
        Item item = new Item();
        Upc upc = new Upc();
        upc.setUpcNumber(123l);
        item.setUpc(upc);
        Product product = new Product();
        product.setId(124);
        item.setProduct(product);
        wishList.setItems(Arrays.asList(item));
        requestBody.setWishlist(Arrays.asList(wishList));
        return requestBody;
    }

    private UserResponse createUserResponse(Long userId, String userGuid, Boolean guestUser) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setGuid(userGuid);
        userResponse.setGuestUser(guestUser);
        ProfileResponse profile = new ProfileResponse();
        profile.setFirstName(USER_FIRST_NAME);
        userResponse.setProfile(profile);
        return userResponse;
    }

    private UserResponse createUserResponseWithCredentials(Long userId, String userGuid, String userName) {
        UserResponse userResponse = createUserResponse(userId, userGuid, false);
        userResponse.setLoginCredentials(new LoginCredentialsResponse());
        userResponse.getLoginCredentials().setUserName(userName);
        return userResponse;
    }
}
