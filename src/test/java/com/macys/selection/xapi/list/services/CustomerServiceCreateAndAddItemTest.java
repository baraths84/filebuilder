package com.macys.selection.xapi.list.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.macys.selection.xapi.list.client.CustomerServiceRestClient;
import com.macys.selection.xapi.list.client.PromotionsRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserPromotions;
import com.macys.selection.xapi.list.data.converters.JsonToPromotionConverter;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.WishList;

@SpringBootTest
public class CustomerServiceCreateAndAddItemTest extends AbstractTestNGSpringContextTests {

	  @Mock
	  private CustomerServiceRestClient restClient;

	  @Mock
	  private PromotionsRestClient promotionsRestClient;

	  @Mock
	  private JsonResponseParserPromotions promotions;

	  @Mock
	  private JsonToPromotionConverter promotionsConverter;

	  @InjectMocks
	  private CustomerService customerService;
	  
	  private static String SUCCESS_RESPONSE_FILE = "com/macys/selection/xapi/list/client/response/Success_response_on_createAndAdd_list.json";
	  private static String SUCCESS_RESPONSE_WITH_USER_ID_ONLY_FILE = "com/macys/selection/xapi/list/client/response/Success_response_on_createAndAdd_list_withUserIdOnly.json";
	  private static String SUCCESS_RESPONSE__DEFAULT_LIST_FILE = "com/macys/selection/xapi/list/client/response/Success_response_on_createAndAdd_default_list.json";
	  private static String SUCCESS_RESPONSE_ON_CREATE_LIST_ONLY = "com/macys/selection/xapi/wishlist/converters/Success_response_on_create_list.json";
	  private static long USER_ID = 2158577676L;
	  private static long USER_ID_TEST_ONLY = 2158575042L;
	  private static String SPECIFICAL_LIST_GUID = "33c93575-3725-47cd-ae3b-80628602b5ee";
	  private static String SPECIFICAL_LIST_NAME = "list 8";
	  private static final int UPC_ID = 37288131;
	  private static final int QTY = 1;
	  private static final String USER_GUID = "4496bbf1-f504-4c54-bd51-4fc9fcfb5396";
	  private static final int ZERO = 0;
	  private static final String SECURITY_TOKEN = "xxx-token";
	  private static final String LIST_GUID = "xxx-guid";
	  private static final Long USER_ID_COOKIES = 10L;



	@BeforeMethod
	  public void init() {
	    MockitoAnnotations.initMocks(this);
	  }
	  
	  private static WishList getWishList() {
		  WishList list = new WishList();
		  Upc upc = new Upc();
		  upc.setId(UPC_ID);
		  Item item = new Item();
		  item.setUpc(upc);
		  item.setQtyRequested(QTY);
		  list.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
		  list.setSearchable(Boolean.TRUE);
		  list.setDefaultList(Boolean.FALSE);
		  list.setName(SPECIFICAL_LIST_NAME);
		  list.setListGuid(SPECIFICAL_LIST_GUID);
		  
		  List<Item> itemList = new ArrayList<Item>();
		  itemList.add(item);
		  list.setItems(itemList);
		  return list;
	  }
	  
	  private static CustomerList getSampleInputCustomerList() {
		  CustomerList customerList = new CustomerList();
		  WishList list = getWishList();
		  
		  List<WishList> wishLists = new ArrayList<WishList>();
		  wishLists.add(list);
		  
		  customerList.setWishlist(wishLists);

		  customerList.setUser(getUser());

          return customerList;
	  }
	  
	  private static User getUser() {		  
		  User user = new User();
		  user.setId(USER_ID);
		  return user;
	  }
	  
	  @Test
	  public void createAndAddWishlistTest() throws IOException, ParseException {
		  
		  //case: new created list is not default list
		  String successCreateList = TestUtils.readFile(SUCCESS_RESPONSE_FILE);
		  RestResponse restResponse = new RestResponse();
		  restResponse.setBody(successCreateList);
		  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
		  when(restClient.createList(any(), any())).thenReturn(restResponse);
		  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenReturn(restResponse);

		  ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
		  CustomerList result = customerService.createWishList(cookie, getSampleInputCustomerList());
		  WishList wishList = result.getWishlist().get(ZERO);
		  
		  assertTrue(!wishList.isDefaultList());
		  verifyResult(wishList);
		  
		  //case: new created list is default list
		  String successCreateDefaultList = TestUtils.readFile(SUCCESS_RESPONSE__DEFAULT_LIST_FILE);
		  RestResponse restResponseOnDefaultList = new RestResponse();
		  restResponseOnDefaultList.setBody(successCreateDefaultList);
		  restResponseOnDefaultList.setStatusCode(Response.Status.OK.getStatusCode());
		  when(restClient.createList(any(), any())).thenReturn(restResponseOnDefaultList);
		  when(restClient.addToDefaultWishlist(any())).thenReturn(restResponseOnDefaultList);
		  
		  CustomerList testCustomerList2 = getSampleInputCustomerList();
		  testCustomerList2.getWishlist().get(ZERO).setDefaultList(Boolean.TRUE);
		  CustomerList result2 = customerService.createWishList(cookie, testCustomerList2);
		  WishList wishList2 = result2.getWishlist().get(ZERO);
		  
		  assertTrue(wishList2.isDefaultList());
		  verifyResult(wishList2);
		  
		  //case: use user guid instead of user id
		  User testUser = new User();
		  testUser.setGuid(USER_GUID);
		  testCustomerList2.setUser(testUser);
		  testCustomerList2.getWishlist().get(ZERO).setDefaultList(Boolean.FALSE);
		  WishList testWishList3 = getWishList();
		  testWishList3.setDefaultList(Boolean.FALSE);
		  CustomerList result3 = customerService.createWishList(cookie, testCustomerList2);
		  WishList wishList3 = result3.getWishlist().get(ZERO);
		  assertTrue(result3.getUser().getGuid().equals(USER_GUID));
		  verifyResult(wishList3);
		  
		  //case: use user id instead of user guid
		  String successCreateListWithUserId = TestUtils.readFile(SUCCESS_RESPONSE_WITH_USER_ID_ONLY_FILE);
		  RestResponse restResponseWithUserId = new RestResponse();
		  restResponseWithUserId.setBody(successCreateListWithUserId);
		  restResponseWithUserId.setStatusCode(Response.Status.OK.getStatusCode());
		  when(restClient.createList(any(), any())).thenReturn(restResponseWithUserId);
		  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenReturn(restResponseWithUserId);
		  User testUser2 = new User();
		  testUser2.setId(USER_ID_TEST_ONLY);
		  testCustomerList2.setUser(testUser2);
		  testCustomerList2.getWishlist().get(ZERO).setDefaultList(Boolean.FALSE);
		  CustomerList result4 = customerService.createWishList(cookie, testCustomerList2);
		  WishList wishList4 = result4.getWishlist().get(ZERO);
		  assertTrue(result4.getUser().getId().equals(USER_ID_TEST_ONLY));
		  verifyResult(wishList4);
	  }
	  
	  @Test
	  public void createListOnlyTest() throws IOException, ParseException {
		  //case: created wishlist has NULL items list
		  String successCreateList = TestUtils.readFile(SUCCESS_RESPONSE_ON_CREATE_LIST_ONLY);
		  RestResponse restResponse = new RestResponse();
		  restResponse.setBody(successCreateList);
		  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
		  when(restClient.createList(any(), any())).thenReturn(restResponse);

		  ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
		  CustomerList result2 = customerService.createWishList(cookie, new CustomerList());
		  assertTrue(result2 != null);
		  assertTrue(result2.getWishlist().get(ZERO).getItems().isEmpty());		  

	  }
	  
	  private void verifyResult(WishList list) {
		  assertNotNull(list);
		  assertTrue(SPECIFICAL_LIST_GUID.equals(list.getListGuid()));
		  assertTrue(SPECIFICAL_LIST_NAME.equals(list.getName()));
		  assertTrue(WishlistConstants.WISH_LIST_TYPE_VALUE.equals(list.getListType()));
		  assertTrue(list.getItems() != null && !list.getItems().isEmpty());
	  }

	  @Test
	  public void needAddItemToListTest() {
		  //case customerList = null;
		  boolean result = false;
		  result = customerService.needAddItemToList(null);
		  assertTrue(!result);
		  
		  //case customerList has null wishlists
		  CustomerList customerList = new CustomerList();
		  customerList.setWishlist(null);
		  result = customerService.needAddItemToList(customerList);
		  assertTrue(!result);
		  
		  //case customerList has null wishlists
		  customerList.setWishlist(new ArrayList<WishList>());
		  result = customerService.needAddItemToList(customerList);
		  assertTrue(!result);
		  
		  //case customerList null is in wishlists
		  List<WishList> wishLists = new ArrayList<WishList>();
		  wishLists.add(null);
		  customerList.setWishlist(wishLists);
		  result = customerService.needAddItemToList(customerList);
		  assertTrue(!result);
		  
		  //case wishlist has null items
		  WishList wishList = new WishList();
		  wishList.setItems(null);
		  wishLists = new ArrayList<WishList>();
		  wishLists.add(wishList);
		  customerList.setWishlist(wishLists);
		  result = customerService.needAddItemToList(customerList);
		  assertTrue(!result);
		  
		  //case wishlist has empty items
		  List<Item> items = new ArrayList<Item>();
		  wishList.setItems(items);
		  wishLists = new ArrayList<WishList>();
		  wishLists.add(wishList);
		  customerList.setWishlist(wishLists);
		  result = customerService.needAddItemToList(customerList);
		  assertTrue(!result);
		  
		  //case wishlist has null item
		  items.add(null);
		  wishList.setItems(items);
		  wishLists = new ArrayList<WishList>();
		  wishLists.add(wishList);
		  customerList.setWishlist(wishLists);
		  result = customerService.needAddItemToList(customerList);
		  assertTrue(!result);
		  
	  }

}
