package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.platform.rest.core.RequestContext;
import com.macys.selection.xapi.list.client.request.CustomerListMerge;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.CookieHandler;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.request.validator.ListItemExitsValidator;
import com.macys.selection.xapi.list.rest.response.Availability;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Price;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.Profile;
import com.macys.selection.xapi.list.rest.response.Promotion;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.services.CustomerService;
import com.macys.selection.xapi.list.services.PromotionService;
import com.macys.selection.xapi.list.services.WishlistService;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.apache.commons.collections.CollectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class CustomerListResourceMspWishlistEnabledTest {

    private static final String LIST_GUID = "testGuid";
    private static final String ITEM_GUID = "itemGuid";
    private static final String USER_GUID = "userGuid";
    private static final long USER_ID = 123L;
    private static final String TEST_STRING = "any string";
    private static final Long TEST_LONG = 111111111L;
    private static final int TIME_1 = 1;
    private static final double TEST_ZERO = 0;
    private static final double TEST_PRICE = 99.0;
    private static final int TEST_INT = 234432;
    private static final String TEST_DATE = "2017-09-14T18:27:54.201";
    private static final String TEST_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String BASE_URI = "http://api.macys.com/";
    private static final String SECURE_TOKEN = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";

    @InjectMocks
    private CustomerListResource listResource = new CustomerListResource();

    @Mock
    private WishlistService wishlistService;
    @Mock
    private CustomerService customerService;
    @Mock
    private CookieHandler cookieHandler;
    @Mock
    private PromotionService promotionService;
    @Mock
    private RequestContext requestContext;
    @Mock
    private KillSwitchPropertiesBean killswitchProperties;
    @Mock
    private ListItemExitsValidator listItemExitsValidator;
    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
        ListCookies cookies = new ListCookies(USER_ID, USER_GUID, SECURE_TOKEN);
        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(cookies);
    }

    @Test
    public void testGetListsByUserId() throws ParseException {
        List<WishList> wishlists = expectedWishList();
        User user = getUser();
        CustomerList customerList = new CustomerList();
        customerList.setWishlist(wishlists);
        customerList.setUser(user);
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(wishlistService.getList(any(), any(), any(), any())).thenReturn(customerList);
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        ListQueryParam listQueryParam = new ListQueryParam();
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();

        Response response = listResource.getCustomerList(userQueryParam, listQueryParam, paginationQueryParam, requestContext);
        assertResponse(response);
        CustomerList actualCustomerList = (CustomerList) response.getEntity();
        verify(wishlistService, times(TIME_1)).getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);
        verify(wishlistService, never()).findList(any(), any(), any());
        verify(customerService, never()).getCustomerList(any(), any(), any());
        assertSuccessTest(actualCustomerList);
    }

    @Test
    public void testGetListsByUserGuid() throws ParseException {
        List<WishList> wishlists = expectedWishList();
        User user = getUser();
        CustomerList customerList = new CustomerList();
        customerList.setWishlist(wishlists);
        customerList.setUser(user);
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(wishlistService.getList(any(), any(), any(), any())).thenReturn(customerList);
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        ListQueryParam listQueryParam = new ListQueryParam();
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();

        Response response = listResource.getCustomerList(userQueryParam, listQueryParam, paginationQueryParam, requestContext);
        assertResponse(response);
        CustomerList actualCustomerList = (CustomerList) response.getEntity();
        verify(wishlistService, times(TIME_1)).getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);
        verify(wishlistService, never()).findList(any(), any(), any());
        verify(customerService, never()).getCustomerList(any(), any(), any());
        assertSuccessTest(actualCustomerList);
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testListServiceExceptionOnGetListsByUser() {
        when(wishlistService.getList(any(), any(), any(), any())).thenThrow(new ListServiceException());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setUserGuid(USER_GUID);
        ListQueryParam listQueryParam = new ListQueryParam();
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Response response = listResource.getCustomerList(userQueryParam, listQueryParam, paginationQueryParam, requestContext);
        assertResponse(response);
        CustomerList actualCustomerList = (CustomerList) response.getEntity();
        assertSuccessTest(actualCustomerList);
        verify(wishlistService, times(TIME_1)).getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void shouldNotWrapIntoListServiceExceptionOnGetListsByUser() {
        when(wishlistService.getList(any(), any(), any(), any())).thenThrow(new NullPointerException());
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setUserGuid(USER_GUID);
        ListQueryParam listQueryParam = new ListQueryParam();
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Response response = listResource.getCustomerList(userQueryParam, listQueryParam, paginationQueryParam, requestContext);
        assertResponse(response);
        CustomerList actualCustomerList = (CustomerList) response.getEntity();
        assertSuccessTest(actualCustomerList);
        verify(wishlistService, times(TIME_1)).getList(userQueryParam, listQueryParam, paginationQueryParam, BASE_URI);
    }

    @Test
    public void testFindLists() throws ParseException {
        List<WishList> wishlists = expectedWishList();
        User user = getUser();
        CustomerList customerList = new CustomerList();
        customerList.setWishlist(wishlists);
        customerList.setUser(user);
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(killswitchProperties.isSeparateFindUsersEnabled()).thenReturn(true);
        when(wishlistService.findList(any(), any(), any())).thenReturn(customerList);
        UserQueryParam userQueryParam = new UserQueryParam();
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setFirstName(TEST_STRING);
        listQueryParam.setLastName(TEST_STRING);
        listQueryParam.setState(TEST_STRING);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();

        Response response = listResource.getCustomerList(userQueryParam, listQueryParam, paginationQueryParam, requestContext);
        assertResponse(response);
        CustomerList actualCustomerList = (CustomerList) response.getEntity();
        verify(wishlistService, times(TIME_1)).findList(userQueryParam, listQueryParam, paginationQueryParam);
        verify(wishlistService, never()).getList(any(), any(), any(), any());
        verify(customerService, never()).getCustomerList(any(), any(), any());
        assertSuccessTest(actualCustomerList);
    }

    @Test
    public void testFindListsResponsiveWishlistGetListsByUserIdsDisabled() throws ParseException {
        List<WishList> wishlists = expectedWishList();
        User user = getUser();
        CustomerList customerList = new CustomerList();
        customerList.setWishlist(wishlists);
        customerList.setUser(user);
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(killswitchProperties.isSeparateFindUsersEnabled()).thenReturn(false);
        when(customerService.getCustomerList(any(), any(), any())).thenReturn(customerList);
        UserQueryParam userQueryParam = new UserQueryParam();
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setFirstName(TEST_STRING);
        listQueryParam.setLastName(TEST_STRING);
        listQueryParam.setState(TEST_STRING);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();

        Response response = listResource.getCustomerList(userQueryParam, listQueryParam, paginationQueryParam, requestContext);
        assertResponse(response);
        CustomerList actualCustomerList = (CustomerList) response.getEntity();
        verify(wishlistService, never()).findList(any(), any(), any());
        verify(wishlistService, never()).getList(any(), any(), any(), any());
        verify(customerService, times(TIME_1)).getCustomerList(userQueryParam, listQueryParam, paginationQueryParam);
        assertSuccessTest(actualCustomerList);
    }

    @Test
    public void testGetCustomerListByGuid() throws ParseException, URISyntaxException {
        List<WishList> customerList = expectedWishList();

        WishList listWithPromotions = buildListWithPromotions(customerList.get(0));
        CustomerList expectedCustomerList = new CustomerList();
        List<WishList> lists = new ArrayList<>();
        lists.add(listWithPromotions);
        expectedCustomerList.setWishlist(lists);
        expectedCustomerList.setUser(getUser());

        HttpServletRequest request = new MockHttpServletRequest();
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        Mockito.when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(USER_ID, USER_GUID, SECURE_TOKEN));
        Mockito.when(wishlistService.getListByGuid(any(), any(), any(), any())).thenReturn(expectedCustomerList);
        Mockito.when(promotionService.getPromotions(any(), any())).thenReturn(listWithPromotions);
        Mockito.when(requestContext.getBaseUri()).thenReturn(new URI(BASE_URI));
        ListQueryParam listQueryParam = new ListQueryParam();
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Response response = listResource.getCustomerListByGuid(request, LIST_GUID, listQueryParam, paginationQueryParam, requestContext);
        assertResponse(response);
        CustomerList actualCustomerList = (CustomerList) response.getEntity();
        verify(customerService, never()).getCustomerList(any(), any(), any());
        verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());
        verify(wishlistService, Mockito.times(TIME_1)).getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam, BASE_URI);
        assertSuccessTest(actualCustomerList);
    }

    @Test
    public void getListByGuidSrvcReturnsNullTest() {
        WishList wishList = new WishList();
        List<WishList> lists = new ArrayList<>();
        lists.add(wishList);
        CustomerList customerList = new CustomerList();
        customerList.setWishlist(lists);

        HttpServletRequest request = new MockHttpServletRequest();
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(USER_ID, USER_GUID, SECURE_TOKEN));
        when(wishlistService.getListByGuid(any(), any(), any(), any())).thenReturn(customerList);
        when(promotionService.getPromotions(any(), any())).thenReturn(new WishList());
        ListQueryParam listQueryParam = new ListQueryParam();
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Response response = listResource.getCustomerListByGuid(request, LIST_GUID, listQueryParam, paginationQueryParam, requestContext);

        CustomerList listResponse = (CustomerList) response.getEntity();
        List<WishList> list = listResponse.getWishlist();
        assertNotNull(list);
        assertNull(list.get(0).getListGuid());
        assertNull(list.get(0).getName());
        assertNull(listResponse.getUser());

        verify(customerService, never()).getCustomerList(any(), any(), any());
        verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());
        verify(wishlistService, times(TIME_1)).getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam, BASE_URI);
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void getListByListGuidTestException() {
        HttpServletRequest request = new MockHttpServletRequest();
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(USER_ID, USER_GUID, SECURE_TOKEN));
        when(wishlistService.getListByGuid(any(), any(), any(), any())).thenThrow(new ListServiceException());
        ListQueryParam listQueryParam = new ListQueryParam();
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Response response = listResource.getCustomerListByGuid(request, TEST_STRING, listQueryParam, paginationQueryParam, requestContext);
        assertResponse(response);
        CustomerList actualCustomerList = (CustomerList) response.getEntity();
        assertSuccessTest(actualCustomerList);
        verify(customerService, never()).getCustomerList(any(), any(), any());
        verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());
        verify(wishlistService, times(TIME_1)).getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam, BASE_URI);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void getListByListGuidTestNullPointerException() {
        HttpServletRequest request = new MockHttpServletRequest();
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(USER_ID, USER_GUID, SECURE_TOKEN));
        when(wishlistService.getListByGuid(any(), any(), any(), any())).thenThrow(new NullPointerException());
        ListQueryParam listQueryParam = new ListQueryParam();
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        Response response = listResource.getCustomerListByGuid(request, TEST_STRING, listQueryParam, paginationQueryParam, requestContext);
        assertResponse(response);
        CustomerList actualCustomerList = (CustomerList) response.getEntity();
        assertSuccessTest(actualCustomerList);
        verify(customerService, never()).getCustomerList(any(), any(), any());
        verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());
        verify(wishlistService, times(TIME_1)).getListByGuid(LIST_GUID, listQueryParam, paginationQueryParam, BASE_URI);
    }

    private List<WishList> expectedWishList() throws ParseException {

        List<WishList> lists = new ArrayList<>();
        WishList list = new WishList();
        list.setId(TEST_LONG);
        list.setListGuid(TEST_STRING);
        list.setName(TEST_STRING);
        list.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
        list.setDefaultList(Boolean.TRUE);
        list.setOnSaleNotify(Boolean.FALSE);
        list.setSearchable(Boolean.FALSE);
        list.setNumberOfItems(TIME_1);
        list.setShowPurchaseInfo(Boolean.TRUE);

        SimpleDateFormat df = new SimpleDateFormat(TEST_DATE_FORMAT);
        list.setCreatedDate(df.parse(TEST_DATE));
        list.setLastModified(df.parse(TEST_DATE));

        User user = new User();
        user.setId(TEST_LONG);
        user.setGuid(TEST_STRING);
        user.setGuestUser(Boolean.TRUE);

        Price price = new Price();
        price.setRetailPrice(TEST_PRICE);
        price.setOriginalPrice(TEST_PRICE);
        price.setIntermediateSalesValue(TEST_ZERO);
        price.setSalesValue(TEST_PRICE);
        price.setOnSale(Boolean.FALSE);
        price.setUpcOnSale(Boolean.FALSE);
        price.setPriceType(TIME_1);
        price.setBasePriceType(TIME_1);
        price.setRetailPriceHigh(TEST_PRICE);
        price.setOriginalPriceLabel(TEST_STRING);
        price.setRetailPriceLabel(TEST_STRING);

        Availability avail = new Availability();
        avail.setAvailable(Boolean.TRUE);
        avail.setUpcAvailabilityMessage(TEST_STRING);
        avail.setInStoreEligible(Boolean.TRUE);
        avail.setOrderMethod(TEST_STRING);

        Product product = new Product();
        product.setId(4835927);
        product.setName(TEST_STRING);
        product.setActive(Boolean.TRUE);
        product.setPrimaryImage(TEST_STRING);
        product.setLive(Boolean.TRUE);
        product.setAvailable(Boolean.TRUE);

        Upc upc = new Upc();
        upc.setId(TEST_INT);
        upc.setUpcNumber(TEST_LONG);
        upc.setColor(TEST_STRING);
        upc.setSize(TEST_STRING);
        upc.setPrice(price);
        upc.setAvailability(avail);
        upc.setProduct(product);

        Item item = new Item();
        item.setId(10594035);
        item.setItemGuid(TEST_STRING);
        item.setRetailPriceWhenAdded(TEST_PRICE);
        item.setRetailPriceDropAfterAddedToList(TEST_PRICE);
        item.setRetailPriceDropPercentage(TIME_1);
        item.setQtyRequested(TIME_1);
        item.setQtyStillNeeded(TIME_1);

        SimpleDateFormat tempDate = new SimpleDateFormat(TEST_DATE_FORMAT);
        item.setAddedDate(tempDate.parse(TEST_DATE));
        item.setLastModified(tempDate.parse(TEST_DATE));
        item.setUpc(upc);
        List<Item> items = new ArrayList<>();
        items.add(item);

        list.setItems(items);
        lists.add(list);

        return lists;
    }

    private WishList buildListWithPromotions(WishList list){
        Promotion promotion = new Promotion();
        promotion.setPromotionId(19873358L);
        promotion.setBadgeTextAttributeValue(TEST_STRING);

        List<Promotion> promotions = new ArrayList<>();
        promotions.add(promotion);

        if (list != null
                && CollectionUtils.isNotEmpty(list.getItems())) {
            list.getItems().forEach(item -> item.setPromotions(promotions));
        }
        return list;
    }

    public static User getUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setGuid(USER_GUID);
        user.setGuestUser(true);
        Profile profile = new Profile();
        profile.setFirstName(TEST_STRING);
        user.setProfile(profile);
        return user;
    }

    @Test
    public void testDeletingCustomerList() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);

        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);

        Response response = listResource.deleteList(httpServletRequest, userQueryParam, LIST_GUID);

        assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        verify(wishlistService).deleteList(SECURE_TOKEN, LIST_GUID, userQueryParam);
        verify(customerService, never()).deleteList(any(), any(), any());
    }

    @Test
    public void testManageWishlist() {
        //String  requestBody = "{'list':{'onSaleNotify':'true'}}";
        CustomerList requestBody = new CustomerList();
        WishList wishList = new WishList();
        wishList.setOnSaleNotify(true);
        requestBody.setWishlist(Collections.singletonList(wishList));

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setGuestUser(false);

        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);

        Response response = listResource.manageWishlist(httpServletRequest, LIST_GUID, userQueryParam, requestBody);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        verify(wishlistService).updateList(SECURE_TOKEN, LIST_GUID, userQueryParam, requestBody);
        verify(customerService, never()).updateWishlist(any(), any(), any(),any());
    }

    @Test
    public void testManageWishlistByPatch() {
        //String  requestBody = "{'list':{'defaultList':'true'}}";
        CustomerList requestBody = new CustomerList();
        WishList wishList = new WishList();
        wishList.setDefaultList(true);
        requestBody.setWishlist(Collections.singletonList(wishList));

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setGuestUser(false);

        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);

        Response response = listResource.manageWishlistByPatch(httpServletRequest, LIST_GUID, userQueryParam, requestBody);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        verify(wishlistService).updateList(SECURE_TOKEN, LIST_GUID, userQueryParam, requestBody);
        verify(customerService, never()).updateWishlist(any(), any(), any(),any());
    }

    @Test
    public void testCreateWishList() {
        CustomerList requestBody = new CustomerList();
        WishList wishList = new WishList();
        wishList.setDefaultList(true);
        requestBody.setWishlist(Collections.singletonList(wishList));

        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);

        Response response = listResource.createWishList(httpServletRequest, requestBody);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(wishlistService).createWishList(any(), Matchers.eq(requestBody));
        verify(customerService, never()).createWishList(any(), any());
    }

    @Test
    public void testAddItemToGivenList() {
        CustomerList inputWishlist = new CustomerList();
        HttpServletRequest request = new MockHttpServletRequest();
        UserQueryParam userQueryParam = new UserQueryParam();

        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(listItemExitsValidator.isValid(any())).thenReturn(Boolean.TRUE);

        Response response = listResource.addItemToGivenListByUPC(request, userQueryParam, LIST_GUID, inputWishlist);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(listItemExitsValidator).isValid(any());
        verify(customerService, never()).addItemToGivenListByUPC(any(), any(), any(), any());
        verify(wishlistService).addItemToGivenList(LIST_GUID, inputWishlist, userQueryParam);
    }

    @Test
    public void testAddToDefaultList() {
        CustomerList customerList = new CustomerList();

        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(listItemExitsValidator.isValid(any())).thenReturn(Boolean.TRUE);

        Response response = listResource.addToDefaultWishlist(customerList);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(listItemExitsValidator).isValid(any());
        verify(customerService, never()).addToDefaultWishlist(any());
        verify(wishlistService).addItemToDefaultWishlist(customerList);
    }

    @Test
    public void moveItemToWishlistTest() {
        Long userId = 12345L;
        final String userGuid = "user_guid_123";

        CustomerList customerList = new CustomerList();

        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(listItemExitsValidator.isValid(any())).thenReturn(Boolean.TRUE);

        Response response = listResource.moveItemToWishlist(httpServletRequest, LIST_GUID, customerList);
        assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        verify(customerService, never()).moveItemToWishlist(any(), any(), any());
        verify(wishlistService).moveItemToWishlist(SECURE_TOKEN, LIST_GUID, customerList);
    }

    private void assertResponse(Response response) {
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    private void assertSuccessTest(CustomerList customerList) {
        assertNotNull(customerList);
        assertTrue(CollectionUtils.isNotEmpty(customerList.getWishlist()));
        assertNotNull(customerList.getUser());
    }

    @Test
    public void testDeleteItem() {
        HttpServletRequest request = new MockHttpServletRequest();
        when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(null, null, SECURE_TOKEN));

        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        Response response = listResource.deleteItem(request, LIST_GUID, ITEM_GUID);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        verify(customerService, never()).deleteItem(any(), any(), any());
        verify(wishlistService, times(TIME_1)).deleteItem(LIST_GUID, ITEM_GUID);
    }

    @Test
    public void testUpdateItemPriority() {
        Item item = new Item();
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        Response response = listResource.updateItemPriority(httpServletRequest, requestContext, LIST_GUID, ITEM_GUID, USER_ID, USER_GUID, item);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(wishlistService, times(TIME_1)).updateItemPriority(SECURE_TOKEN, LIST_GUID, ITEM_GUID, USER_ID, USER_GUID, item);
        verify(customerService, never()).updateItemPriority(any(), any(), any(), any(), any());
    }

    @Test
    public void testMergeList() {
        CustomerListMerge customerListMerge = new CustomerListMerge();
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        Response response = listResource.mergeList(httpServletRequest, requestContext, customerListMerge);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(wishlistService, times(TIME_1)).mergeList(SECURE_TOKEN, customerListMerge);
        verify(customerService, never()).mergeList(any());
    }

    @Test
    public void testShareEmailResponsive() {
        ReflectionTestUtils.setField(listResource, "responsiveShareEmailEnabled", true);
        EmailShare emailShare = new EmailShare();
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        Response response = listResource.emailShareWishlist(httpServletRequest, LIST_GUID, emailShare);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        verify(wishlistService, times(TIME_1)).emailShareWishlist(any(), eq(LIST_GUID), eq(emailShare));
        verify(customerService, never()).emailShareWishlist(any(), any(), any());
    }

    @Test
    public void testShareEmailNotResponsive() {
        ReflectionTestUtils.setField(listResource, "responsiveShareEmailEnabled", false);
        EmailShare emailShare = new EmailShare();
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        Response response = listResource.emailShareWishlist(any(), LIST_GUID, emailShare);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        verify(wishlistService, never()).emailShareWishlist(any(), any(), any());
        verify(customerService, times(TIME_1)).emailShareWishlist(any(), eq(LIST_GUID), eq(emailShare));
    }
}
