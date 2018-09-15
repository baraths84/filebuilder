package com.macys.selection.xapi.list.rest.v1.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.macys.platform.rest.core.RequestContext;
import com.macys.selection.xapi.list.client.CustomerServiceRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.client.request.CustomerListMerge;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.ListWebApplicationException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.CookieHandler;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.request.validator.ListItemExitsValidator;

import com.macys.selection.xapi.list.rest.response.*;
import com.macys.selection.xapi.list.services.CustomerService;
import com.macys.selection.xapi.list.services.PromotionService;
import com.macys.selection.xapi.list.services.WishlistService;
import com.macys.selection.xapi.list.util.CustomerRequestParamUtil;

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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@SpringBootTest
(classes = {
		ListItemExitsValidator.class})
public class CustomerListResourceTest extends AbstractTestNGSpringContextTests {

	public static final long USER_ID = 201306195;
	public static final String USER_GUID = "924de083-4ff5-401b-9108-af6654d5e7d8";
	public static final long LIST_ID = 10680033;
	public static final String LIST_GUID = "ee15b1e1-2a85-46ef-bb82-236b4068870b";
	public static final String ITEM_GUID = "af12b1e1-2a85-46ef-bb82-236b4068870b";
	private static final String TEST_STRING = "any string";
	private static final Long TEST_LONG = 111111111L;
	private static final int TIME_1 = 1;
	private static final double TEST_ZERO = 0;
	private static final double TEST_PRICE = 99.0;
	private static final int TEST_INT = 234432;
	private static final String TEST_DATE = "2017-09-14T18:27:54.201";
	private static final String TEST_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	private static final String WISH_LISTS_JSON_FILE = "com/macys/selection/xapi/list/client/response/wishlist_error_customer.json";

	@Mock
	private CustomerService customerService;

	@Mock
	private HttpServletRequest httpRequest;

	@Mock
	private WishlistService wishlistService;

	@Mock
	private PromotionService promotionService;

	@Mock
	private CookieHandler cookieHandler;

	@Mock
	private ListItemExitsValidator listItemExitsValidator;


	@Mock
	private KillSwitchPropertiesBean killswitchProperties;

	@Mock
	private CustomerServiceRestClient restClient;

	@Mock
	private RequestContext requestContext;

	@Mock
	private HttpServletRequest httpServletRequest;

	@Mock
    private CustomerRequestParamUtil customerRequestParamUtil;

	@InjectMocks
	private CustomerListResource listResource = new CustomerListResource();

	@BeforeMethod
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getListByUserIdTest() throws ParseException, IOException {

		List<WishList> wishlists = expectedWishList();
		User user = getUser();
		CustomerList customerList = new CustomerList();
		customerList.setWishlist(wishlists);
		customerList.setUser(user);
		when(customerService.getCustomerList(any(), any(), any())).thenReturn(customerList);
		Response response = listResource.getCustomerList(new UserQueryParam(), new ListQueryParam(),
				new PaginationQueryParam(), requestContext);
		assertResponse(response);
		CustomerList actualCustomerList = (CustomerList) response.getEntity();
		verify(customerService, times(TIME_1)).getCustomerList(any(), any(), any());
		verify(wishlistService, never()).getList(any(), any(), any(), any());
		verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());
		assertSuccessTest(actualCustomerList);
	}

	@Test
	public void getListByUserIdTestWithFinalPrice() throws ParseException, IOException {

		List<WishList> wishlists = expectedWishList();
		User user = getUser();
		CustomerList customerList = new CustomerList();
		customerList.setWishlist(wishlists);
		customerList.setUser(user);
		when(customerService.getCustomerList(any(), any(), any())).thenReturn(customerList);
		Response response = listResource.getCustomerList(new UserQueryParam(), new ListQueryParam(),
				new PaginationQueryParam(), requestContext);
		assertResponse(response);
		CustomerList actualCustomerList = (CustomerList) response.getEntity();
		verify(customerService, times(TIME_1)).getCustomerList(any(), any(), any());
		verify(wishlistService, never()).getList(any(), any(), any(), any());
		verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());
		assertSuccessTest(actualCustomerList);
	}



	// we expect the ListServiceException in-case an exception is thrown
	@Test(expectedExceptions = ListServiceException.class)
	public void getAllListByUserIdTestException() throws ParseException, IOException {
		when(customerService.getCustomerList(any(), any(), any())).thenThrow(new ListServiceException());
		Response response = listResource.getCustomerList(new UserQueryParam(), new ListQueryParam(),
				new PaginationQueryParam(), requestContext);
		assertResponse(response);
		CustomerList actualCustomerList = (CustomerList) response.getEntity();
		assertSuccessTest(actualCustomerList);
		verify(customerService, times(TIME_1)).getCustomerList(any(), any(), any());
		verify(wishlistService, never()).getList(any(), any(), any(), any());
		verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());

	}

	// we expect the ListServiceException in-case an exception is thrown
	// why does the serivce throws NullPointerException
	@Test(expectedExceptions = NullPointerException.class)
	public void getAllListByUserIdTestNullPointerException() throws JsonProcessingException, ParseException {    
		when(customerService.getCustomerList(any(), any(), any())).thenThrow(new NullPointerException());
		Response response = listResource.getCustomerList(new UserQueryParam(), new ListQueryParam(),
				new PaginationQueryParam(), requestContext);
		assertResponse(response);
		CustomerList actualCustomerList = (CustomerList) response.getEntity();
		assertSuccessTest(actualCustomerList);
		verify(customerService, times(TIME_1)).getCustomerList(any(), any(), any());
		verify(wishlistService, never()).getList(any(), any(), any(), any());
		verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());

	}

	@Test
	public void getListByGuidTest() throws ParseException, IOException {
		Long userId = 12345L;
		final String secureToken = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
		final String userGuid = "user_guid_123";

		List<WishList> customerList = expectedWishList();

		WishList listWithPromotions = buildListWithPromotions(customerList.get(0));
		CustomerList expectedCustomerList = new CustomerList();
		List<WishList> lists = new ArrayList<>();
		lists.add(listWithPromotions);
		expectedCustomerList.setWishlist(lists);
		expectedCustomerList.setUser(getUser());

		HttpServletRequest request = new MockHttpServletRequest();
		when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, secureToken));
		when(customerService.getCustomerListByGuid(any(), any(), any(), any())).thenReturn(expectedCustomerList);
		when(promotionService.getPromotions(any(), any())).thenReturn(listWithPromotions);

		Response response = listResource.getCustomerListByGuid(request, TEST_STRING, new ListQueryParam(), new PaginationQueryParam(), requestContext);

		assertResponse(response);        
		CustomerList actualCustomerList = (CustomerList) response.getEntity();
		verify(customerService, never()).getCustomerList(any(), any(), any());
		verify(wishlistService, never()).getListByGuid(any(), any(), any(), any());
		verify(customerService, times(TIME_1)).getCustomerListByGuid(any(), any(), any(), any());
		assertSuccessTest(actualCustomerList);
	}

	@Test
	public void moveItemToWishlistTest() {
		Long userId = 12345L;
		final String secureToken = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
		final String userGuid = "user_guid_123";

		CustomerList customerList = new CustomerList();
		User user = new User();
		WishList wishList = new WishList();
		user.setId(Long.parseLong(String.valueOf(TEST_LONG)));

		List<WishList> lists = new ArrayList<>();
		Item item = new Item();
		item.setItemGuid(TEST_STRING);
		List<Item> items = new ArrayList<>();
		items.add(item);
		wishList.setItems(items);
		lists.add(wishList);
		customerList.setUser(user);
		customerList.setWishlist(lists);

		HttpServletRequest request = new MockHttpServletRequest();
		when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, secureToken));

		Response response = listResource.moveItemToWishlist(request, TEST_STRING, customerList);
		assertEquals(response.getStatus(), Status.NO_CONTENT.getStatusCode());
		verify(customerService, times(TIME_1)).moveItemToWishlist(any(), any(), any());
	}

	@Test
	public void getListByGuidSrvcReturnsNullTest() throws ParseException, IOException {
		Long userId = 12345L;
		final String secureToken = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
		final String userGuid = "user_guid_123";

		WishList wishList = new WishList();
		List<WishList> lists = new ArrayList<>();
		lists.add(wishList);
		CustomerList customerList = new CustomerList();
		customerList.setWishlist(lists);

		HttpServletRequest request = new MockHttpServletRequest();
		when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, secureToken));
		when(customerService.getCustomerListByGuid(any(), any(), any(), any())).thenReturn(customerList);
		when(promotionService.getPromotions(any(), any())).thenReturn(new WishList());

		Response response = listResource.getCustomerListByGuid(request, TEST_STRING, new ListQueryParam(), new PaginationQueryParam(), requestContext);

		CustomerList listResponse = (CustomerList) response.getEntity();
		List<WishList> list = listResponse.getWishlist();
		assertNotNull(list);
		assertNull(list.get(0).getListGuid());
		assertNull(list.get(0).getName());
		assertNull(listResponse.getUser());

		verify(customerService, never()).getCustomerList(any(), any(), any());
		verify(wishlistService, never()).getListByGuid(any(), any(), any(), any());
		verify(customerService, times(TIME_1)).getCustomerListByGuid(any(), any(), any(), any());
	}

	@Test(expectedExceptions = ListServiceException.class)
	public void getListByListGuidTestException() throws ParseException, IOException {
		Long userId = 12345L;
		final String secureToken = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
		final String userGuid = "user_guid_123";

		HttpServletRequest request = new MockHttpServletRequest();
		when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, secureToken));
		when(customerService.getCustomerListByGuid(any(), any(), any(), any())).thenThrow(new ListServiceException());

		Response response = listResource.getCustomerListByGuid(request, TEST_STRING, new ListQueryParam(),
				new PaginationQueryParam(), requestContext);
		assertResponse(response);
		CustomerList actualCustomerList = (CustomerList) response.getEntity();
		assertSuccessTest(actualCustomerList);
		verify(customerService, never()).getCustomerList(any(), any(), any());
		verify(wishlistService, never()).getListByGuid(any(), any(), any(), any());
		verify(customerService, times(TIME_1)).getCustomerListByGuid(any(), any(), any(), any());
	}  

	@Test(expectedExceptions = NullPointerException.class)
	public void getListByListGuidTestNullPointerException() throws ParseException, IOException {
		Long userId = 12345L;
		final String secureToken = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
		final String userGuid = "user_guid_123";

		HttpServletRequest request = new MockHttpServletRequest();
		when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, secureToken));
		when(customerService.getCustomerListByGuid(any(), any(), any(), any())).thenThrow(new NullPointerException());

		Response response = listResource.getCustomerListByGuid(request, TEST_STRING, new ListQueryParam(),
				new PaginationQueryParam(), requestContext);
		assertResponse(response);
		CustomerList actualCustomerList = (CustomerList) response.getEntity();
		assertSuccessTest(actualCustomerList);
		verify(customerService, never()).getCustomerList(any(), any(), any());
		verify(wishlistService, never()).getListByGuid(any(), any(), any(), any());
		verify(customerService, times(TIME_1)).getCustomerListByGuid(any(), any(), any(), any());

	}

	private void assertResponse(Response response) {
		assertNotNull(response);
		assertEquals(response.getStatus(), Status.OK.getStatusCode());
	}

	private void assertSuccessTest(CustomerList customerList) {
		assertNotNull(customerList);
		assertTrue(CollectionUtils.isNotEmpty(customerList.getWishlist()));
		assertNotNull(customerList.getUser());
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

	/**
	 * setting up the expected customer list data 
	 **/
	public List<WishList> expectedWishList() throws ParseException {

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

	public List<WishList> expectedWishListFilterOptionsBcom() throws ParseException {
		List<WishList> wishlist = expectedWishList();
		List<Item> newItemsList = new ArrayList<>();

		//adding onsale item
        Product product = new Product();
        product.setId(4835927);
        product.setName("Salvatore Ferragamo Pebbled Calfskin Belt with Gancio Buckle");
        product.setActive(Boolean.TRUE);
        product.setPrimaryImage(TEST_STRING);
        product.setLive(Boolean.TRUE);
        product.setAvailable(Boolean.TRUE);

        Price price = new Price();
        price.setRetailPrice(TEST_PRICE);
        price.setOriginalPrice(TEST_PRICE);
        price.setIntermediateSalesValue(TEST_ZERO);
        price.setSalesValue(TEST_PRICE);
        price.setOnSale(Boolean.TRUE);
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

        Upc upc = new Upc();
        upc.setId(343434334);
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
        upc.setId(5555555);
        item.setUpc(upc);

		//adding onsale item
		Item item2 = new Item();
        price.setOnSale(true);
        product.setName("HUE Absolute Opaque Tights");
        Upc upc1 = new Upc();
        upc1.setId(343434334);
        upc1.setUpcNumber(TEST_LONG);
        upc1.setColor("Blue");
        upc1.setSize("L");
        upc1.setAvailability(avail);
        upc1.setPrice(price);
        upc1.setProduct(product);
		item2.setUpc(upc1);



        newItemsList.add(wishlist.get(0).getItems().get(0));
		newItemsList.add(item);
		newItemsList.add(item2);
		wishlist.get(0).setItems(newItemsList);
		return wishlist;
	}
	public WishList buildListWithPromotions(WishList list){
		Promotion promotion = new Promotion();
		promotion.setPromotionId(19873358L);
		promotion.setBadgeTextAttributeValue(TEST_STRING);

		List<Promotion> promotions = new ArrayList<>();
		promotions.add(promotion);

		if (list != null
				&& CollectionUtils.isNotEmpty(list.getItems())) {
			list.getItems().forEach(item -> {
				item.setPromotions(promotions);
			});
		}
		return list;  
	}

	private CustomerList buildMoveToListRequestBody(){
		Item item = new Item();
		item.setItemGuid(TEST_STRING);
		List<Item> items = new ArrayList<>();
		items.add(item);
		WishList wishlist = new WishList();
		wishlist.setItems(items);
		List<WishList> wishlists = new ArrayList<>();
		wishlists.add(wishlist);
		CustomerList customerList = new CustomerList();
		customerList.setWishlist(wishlists);
		User user = new User();
		user.setId(201306216L);
		customerList.setUser(user);

		return customerList;
	}

	@Test
	public void testMoveItemToWishlist() throws JsonProcessingException, ParseException {
		Long userId = 12345L;
		final String secureToken = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
		final String userGuid = "user_guid_123";

		HttpServletRequest request = new MockHttpServletRequest();
		when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, secureToken));

		Response response = listResource.moveItemToWishlist(request, TEST_STRING, buildMoveToListRequestBody());
		assertNotNull(response);
		assertEquals(response.getStatus(), Status.NO_CONTENT.getStatusCode());
		verify(customerService, never()).getCustomerList(any(), any(), any());
		verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());
		verify(customerService, never()).deleteItem(any(), any(), any());
		verify(customerService, never()).updateWishlist(any(), any(), any(),any());
		verify(customerService, times(TIME_1)).moveItemToWishlist(any(), any(), any());
	}

	@Test
	public void testDeleteItem() throws JsonProcessingException, ParseException {
		Long userId = 12345L;
		final String secureToken = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
		final String userGuid = "user_guid_123";

		HttpServletRequest request = new MockHttpServletRequest();
		when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, secureToken));

		Response response = listResource.deleteItem(request, TEST_STRING,TEST_STRING);
		assertNotNull(response);
		assertEquals(response.getStatus(), Status.NO_CONTENT.getStatusCode());
		verify(customerService, never()).getCustomerList(any(), any(), any());
		verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());
		verify(customerService, times(TIME_1)).deleteItem(any(), any(), any());
		verify(wishlistService, never()).deleteItem(any(), any());
	}

	@Test
	public void testManageWishlist() throws JsonProcessingException, ParseException {
		//String  requestBody = "{'list':{'onSaleNotify':'true'}}";
		CustomerList requestBody = new CustomerList();
		WishList wishList = new WishList();
		wishList.setOnSaleNotify(true);
		List<WishList> wishLists = new ArrayList<>();
		wishLists.add(wishList);
		requestBody.setWishlist(wishLists);
		Response response = listResource.manageWishlist(httpRequest, TEST_STRING,new UserQueryParam(),requestBody);
		assertNotNull(response);
		assertEquals(response.getStatus(), Status.NO_CONTENT.getStatusCode());
		verify(customerService, never()).getCustomerList(any(), any(), any());
		verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());
		verify(customerService, never()).deleteItem(any(), any(), any());
		verify(customerService, times(TIME_1)).updateWishlist(any(), any(), any(),any());
	}

	@Test
	public void testShareEmailWishList() {
		HttpServletRequest request = new MockHttpServletRequest();

		Response response = listResource.emailShareWishlist(request, TEST_STRING, new EmailShare());
		assertNotNull(response);
		assertEquals(response.getStatus(), Status.NO_CONTENT.getStatusCode());
		verify(customerService, times(TIME_1)).emailShareWishlist(any(), any(), any());
	}

	@Test
	public void testAddToDefaultWishlistByUpcID() {
		CustomerList customerList = new CustomerList();
		WishList wishList = new WishList();
		List<WishList> wishlists = new ArrayList<>();
		Item item = new Item();
		List<Item> items = new ArrayList<>();
		items.add(item);
		wishList.setItems(items);
		wishlists.add(wishList);
		customerList.setWishlist(wishlists);

		when(listItemExitsValidator.isValid(any())).thenReturn(Boolean.TRUE);

		Response response = listResource.addToDefaultWishlist(customerList);
		assertNotNull(response);
		assertEquals(response.getStatus(), Status.OK.getStatusCode());
		verify(listItemExitsValidator, times(TIME_1)).isValid(any());
		verify(customerService, times(TIME_1)).addToDefaultWishlist(any());
	}

	@Test
	public void testAddToDefaultWishlistByProductId() {
		CustomerList customerList = new CustomerList();
		WishList wishList = new WishList();
		List<WishList> wishlists = new ArrayList<>();
		Item item = new Item();
		List<Item> items = new ArrayList<>();
		items.add(item);
		wishList.setItems(items);
		wishlists.add(wishList);
		customerList.setWishlist(wishlists);

		when(listItemExitsValidator.isValid(any())).thenReturn(Boolean.TRUE);

		Response response = listResource.addToDefaultWishlist(customerList);
		assertNotNull(response);
		assertEquals(response.getStatus(), Status.OK.getStatusCode());

		verify(listItemExitsValidator, times(TIME_1)).isValid(any());
		verify(customerService, times(TIME_1)).addToDefaultWishlist(any());
	}
	
	@Test(expectedExceptions = ListWebApplicationException.class)
	public void testAddToDefaultWishlistFailed() {
		when(listItemExitsValidator.isValid(any())).thenReturn(Boolean.FALSE);
		CustomerList customerList = new CustomerList();
		listResource.addToDefaultWishlist(customerList);
	}

	@Test
	public void testCreateList() {
		CustomerList inputCustomerList = new CustomerList();
		HttpServletRequest request = new MockHttpServletRequest();
		Response response = listResource.createWishList(request, inputCustomerList);
		assertNotNull(response);
		assertEquals(response.getStatus(), Status.OK.getStatusCode());
		verify(customerService, times(TIME_1)).createWishList(any(), any());
	}

	@Test
	public void testAddItemToGivenListByUPC() {
		CustomerList inputWishlist = new CustomerList();
		HttpServletRequest request = new MockHttpServletRequest();

		when(listItemExitsValidator.isValid(any())).thenReturn(Boolean.TRUE);

		Response response = listResource.addItemToGivenListByUPC(request, new UserQueryParam(), TEST_STRING, inputWishlist);
		assertNotNull(response);
		assertEquals(response.getStatus(), Status.OK.getStatusCode());
		verify(listItemExitsValidator, times(TIME_1)).isValid(any());
		verify(customerService, times(TIME_1)).addItemToGivenListByUPC(any(), any(), any(), any());
	}

	@Test(expectedExceptions = ListWebApplicationException.class)
	public void testAddZeroItemToListByUPCShouldThrowAnException() {
		CustomerList inputWishlist = new CustomerList();
		HttpServletRequest request = new MockHttpServletRequest();

		when(listItemExitsValidator.isValid(any())).thenReturn(Boolean.FALSE);

		Response response = listResource.addItemToGivenListByUPC(request, new UserQueryParam(), "testingListGuid", inputWishlist);
		assertNotNull(response);
		assertEquals(response.getStatus(), Status.OK.getStatusCode());
		verify(listItemExitsValidator, times(1)).isValid(any());
		verify(customerService, times(1)).addItemToGivenListByUPC(any(), any(), any(), any());
	}

	@Test
	public void getListByInvalidUserIdTest() throws ParseException, IOException {
		CustomerList result = new CustomerList();
		List<WishList> wishlists = new ArrayList<>();
		result.setWishlist(wishlists);
		String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_JSON_FILE);
		RestResponse restResponse = new RestResponse();
		restResponse.setBody(wishlistsResponseJson);
		restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
		when(restClient.get(any(), any(), any())).thenReturn(restResponse);
		Response response = listResource.getCustomerList(new UserQueryParam(), new ListQueryParam(), new PaginationQueryParam(), requestContext);
		assertNotNull(response);
	}

	@Test
	public void testMergeList() {
		CustomerListMerge listMerge = new CustomerListMerge();
		Response response = listResource.mergeList(httpServletRequest, requestContext, listMerge);
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
		verify(customerService, times(TIME_1)).mergeList(listMerge);
	}

	@Test
	public void testUpdateItemPriority() {
		Item item = new Item();
		Response response = listResource.updateItemPriority(httpServletRequest, requestContext, LIST_GUID, ITEM_GUID, USER_ID, USER_GUID, item);
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
		verify(customerService, times(TIME_1)).updateItemPriority(LIST_GUID, ITEM_GUID, USER_ID, USER_GUID, item);
		verify(wishlistService, never()).updateItemPriority(any(), any(), any(), any(), any(), any());
	}
	@Test
	public void getListByGuidTest_Filter_Options_onSale_bcom() throws ParseException, IOException {
        ReflectionTestUtils.setField(listResource, "applicationName", "BCOM");
        ReflectionTestUtils.setField(listResource, "isBCOM", true);

        Long userId = 12345L;
		final String secureToken = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
		final String userGuid = "user_guid_123";

		List<WishList> customerList = expectedWishListFilterOptionsBcom();

		WishList listWithPromotions = buildListWithPromotions(customerList.get(0));
		CustomerList expectedCustomerList = new CustomerList();

		List<WishList> lists = new ArrayList<>();
		lists.add(listWithPromotions);
		expectedCustomerList.setWishlist(lists);
		expectedCustomerList.setUser(getUser());

		HttpServletRequest request = new MockHttpServletRequest();
		when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, secureToken));
		when(customerService.getCustomerListByGuid(any(), any(), any(), any())).thenReturn(expectedCustomerList);
		when(promotionService.getPromotions(any(), any())).thenReturn(listWithPromotions);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setFilter("onSale");

        List<Item> items = expectedCustomerList.getWishlist().get(0).getItems();
        List<Item> modifiedItemList = new ArrayList<>();
        for(Item item : items){
            if(item.getUpc().getPrice().getOnSale()==true){
                modifiedItemList.add(item);
            }
        }
        CustomerList  modifiedCustomerList = expectedCustomerList;
        modifiedCustomerList.getWishlist().get(0).setItems(modifiedItemList);

        //when(customerRequestParamUtil.buildResponseWithFilterOptions(any(), any())).thenReturn();
		//doNothing().when(customerRequestParamUtil.buildResponseWithFilterOptions(any(),any()));
		Response response = listResource.getCustomerListByGuid(request, TEST_STRING, listQueryParam, new PaginationQueryParam(), requestContext);

		assertResponse(response);
		CustomerList actualCustomerList = (CustomerList) response.getEntity();
		Assert.assertNotNull(actualCustomerList);
		Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().size(),2);
        Assert.assertTrue(actualCustomerList.getWishlist().get(0).getItems().get(0).getUpc().getPrice().getOnSale());
        Assert.assertTrue(actualCustomerList.getWishlist().get(0).getItems().get(1).getUpc().getPrice().getOnSale());

	}

    @Test
    public void getListByGuidTest_Filter_Options_promotions_bcom() throws ParseException, IOException {
        ReflectionTestUtils.setField(listResource, "applicationName", "BCOM");
        ReflectionTestUtils.setField(listResource, "isBCOM", true);

        Long userId = 12345L;
        final String secureToken = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
        final String userGuid = "user_guid_123";

        List<WishList> customerList = expectedWishListFilterOptionsBcom();

        WishList listWithPromotions = buildListWithPromotions(customerList.get(0));
        CustomerList expectedCustomerList = new CustomerList();

        List<WishList> lists = new ArrayList<>();
        lists.add(listWithPromotions);
        expectedCustomerList.setWishlist(lists);
        expectedCustomerList.setUser(getUser());

        HttpServletRequest request = new MockHttpServletRequest();
        when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, secureToken));
        when(customerService.getCustomerListByGuid(any(), any(), any(), any())).thenReturn(expectedCustomerList);
        when(promotionService.getPromotions(any(), any())).thenReturn(listWithPromotions);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setFilter("promotions");


        //when(customerRequestParamUtil.buildResponseWithFilterOptions(any(), any())).getMock();

        Response response = listResource.getCustomerListByGuid(request, TEST_STRING, listQueryParam, new PaginationQueryParam(), requestContext);

        assertResponse(response);
        CustomerList actualCustomerList = (CustomerList) response.getEntity();
        Assert.assertNotNull(actualCustomerList);
        Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().size(),3);
        Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().get(0).getPromotions().size(),1);
        Assert.assertFalse(actualCustomerList.getWishlist().get(0).getItems().get(0).getUpc().getPrice().getOnSale());

        Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().get(1).getPromotions().size(),1);
        Assert.assertTrue(actualCustomerList.getWishlist().get(0).getItems().get(1).getUpc().getPrice().getOnSale());

        Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().get(2).getPromotions().size(),1);
        Assert.assertTrue(actualCustomerList.getWishlist().get(0).getItems().get(2).getUpc().getPrice().getOnSale());
    }

    @Test
    public void getListByGuidTest_Filter_Options_onSale_promotions_bcom() throws ParseException, IOException {
		ReflectionTestUtils.setField(listResource, "applicationName", "BCOM");
		ReflectionTestUtils.setField(listResource, "isBCOM", true);

		Long userId = 12345L;
        final String secureToken = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
        final String userGuid = "user_guid_123";

        List<WishList> customerList = expectedWishListFilterOptionsBcom();

        WishList listWithPromotions = buildListWithPromotions(customerList.get(0));
        CustomerList expectedCustomerList = new CustomerList();

        List<WishList> lists = new ArrayList<>();
        lists.add(listWithPromotions);
        expectedCustomerList.setWishlist(lists);
        expectedCustomerList.setUser(getUser());

        HttpServletRequest request = new MockHttpServletRequest();
        when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, secureToken));
        when(customerService.getCustomerListByGuid(any(), any(), any(), any())).thenReturn(expectedCustomerList);
        when(promotionService.getPromotions(any(), any())).thenReturn(listWithPromotions);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setFilter("onSale,promotions");
        List<Item> items = expectedCustomerList.getWishlist().get(0).getItems();
        List<Item> modifiedItemList = new ArrayList<>();
        for(Item item : items){
            if(item.getUpc().getPrice().getOnSale()==true){
                modifiedItemList.add(item);
            }
        }
        List<Integer> upcids = new ArrayList<>();
        for(Item item : modifiedItemList){
            upcids.add(item.getUpc().getId());
        }
        for(Item item : items) {
            if (!upcids.contains(item.getUpc().getId())  && item.getPromotions() != null) {
                modifiedItemList.add(item);
            }
        }
        expectedCustomerList.getWishlist().get(0).setItems(modifiedItemList);
        //when(customerRequestParamUtil.buildResponseWithFilterOptions(any(), any())).thenReturn();

        Response response = listResource.getCustomerListByGuid(request, TEST_STRING, listQueryParam, new PaginationQueryParam(), requestContext);

        assertResponse(response);
        CustomerList actualCustomerList = (CustomerList) response.getEntity();
        Assert.assertNotNull(actualCustomerList);
        Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().size(),3);
        Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().get(0).getPromotions().size(),1);
        Assert.assertTrue(actualCustomerList.getWishlist().get(0).getItems().get(0).getUpc().getPrice().getOnSale());

        Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().get(1).getPromotions().size(),1);
        Assert.assertTrue(actualCustomerList.getWishlist().get(0).getItems().get(1).getUpc().getPrice().getOnSale());

        Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().get(2).getPromotions().size(),1);
        Assert.assertFalse(actualCustomerList.getWishlist().get(0).getItems().get(2).getUpc().getPrice().getOnSale());

    }
	@Test
	public void getListByGuidTest_Filter_Options_no_onSale_items_bcom() throws ParseException, IOException {
		ReflectionTestUtils.setField(listResource, "applicationName", "BCOM");
		ReflectionTestUtils.setField(listResource, "isBCOM", true);

		Long userId = 12345L;
		final String secureToken = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
		final String userGuid = "user_guid_123";

		List<WishList> customerList = expectedWishListFilterOptionsBcom();
        for(WishList list : customerList){
			List<Item> items = list.getItems();
			for(Item item : items){
				item.getUpc().getPrice().setOnSale(false);
			}
		}
		WishList listWithPromotions = buildListWithPromotions(customerList.get(0));
		CustomerList expectedCustomerList = new CustomerList();

		List<WishList> lists = new ArrayList<>();
		lists.add(listWithPromotions);
		expectedCustomerList.setWishlist(lists);
		expectedCustomerList.setUser(getUser());

		HttpServletRequest request = new MockHttpServletRequest();
		when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, secureToken));
		when(customerService.getCustomerListByGuid(any(), any(), any(), any())).thenReturn(expectedCustomerList);
		when(promotionService.getPromotions(any(),any())).thenReturn(listWithPromotions);
		ListQueryParam listQueryParam = new ListQueryParam();
		listQueryParam.setFilter("onSale");

		List<Item> items = expectedCustomerList.getWishlist().get(0).getItems();
		List<Item> modifiedItemList = new ArrayList<>();
		for(Item item : items){
			if(item.getUpc().getPrice().getOnSale()==true){
				modifiedItemList.add(item);
			}
		}
		CustomerList  modifiedCustomerList = expectedCustomerList;
		modifiedCustomerList.getWishlist().get(0).setItems(modifiedItemList);

		Response response = listResource.getCustomerListByGuid(request, TEST_STRING, listQueryParam, new PaginationQueryParam(), requestContext);

		assertResponse(response);
		CustomerList actualCustomerList = (CustomerList) response.getEntity();
		Assert.assertNotNull(actualCustomerList);
		//filter is onSale and there are no onsale items in the list expected to items size zero
		Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().size(),0);


	}
	@Test
	public void getListByUserIdTest_BCOM() throws ParseException, IOException {
		ReflectionTestUtils.setField(listResource, "applicationName", "BCOM");
		ReflectionTestUtils.setField(listResource, "isBCOM", true);
		List<WishList> customerList = expectedWishListFilterOptionsBcom();

		WishList listWithPromotions = buildListWithPromotions(customerList.get(0));

		List<WishList> lists = new ArrayList<>();
		lists.add(listWithPromotions);

		User user = getUser();
		CustomerList customerList1 = new CustomerList();
		customerList1.setWishlist(lists);
		customerList1.setUser(user);
		when(customerService.getCustomerList(any(), any(), any())).thenReturn(customerList1);
		when(promotionService.getPromotions(any(), any())).thenReturn(listWithPromotions);
		Response response = listResource.getCustomerList(new UserQueryParam(), new ListQueryParam(),
				new PaginationQueryParam(), requestContext);
		assertResponse(response);
		CustomerList actualCustomerList = (CustomerList) response.getEntity();
		assertSuccessTest(actualCustomerList);
		Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().size(),3);
		Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().get(0).getPromotions().size(),1);
		Assert.assertNotNull(actualCustomerList.getWishlist().get(0).getItems().get(0).getPromotions().get(0).getPromotionId());

		Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().get(1).getPromotions().size(),1);
		Assert.assertNotNull(actualCustomerList.getWishlist().get(0).getItems().get(1).getPromotions().get(0).getPromotionId());

		Assert.assertEquals(actualCustomerList.getWishlist().get(0).getItems().get(2).getPromotions().size(),1);
		Assert.assertNotNull(actualCustomerList.getWishlist().get(0).getItems().get(2).getPromotions().get(0).getPromotionId());
	}
}