package com.macys.selection.xapi.list.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.request.validator.ListItemExitsValidator;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import com.macys.selection.xapi.list.util.ListUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.macys.selection.xapi.list.client.CustomerListRestClientTest;
import com.macys.selection.xapi.list.client.CustomerServiceRestClient;
import com.macys.selection.xapi.list.client.PromotionsRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserPromotions;
import com.macys.selection.xapi.list.data.converters.JsonToPromotionConverter;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.request.validator.ListItemExitsValidator;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.WishList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@SpringBootTest
public class CustomerService2Test extends AbstractTestNGSpringContextTests {
	
  private static String TEST_LIST_GUID = "testingListGuid";
  private static String TEST_SECURITY_TOKEN = "testingSecurityToken";
  private static String SUCCESS = "Success";
  private static String SUCCESS_RESPONSE_FILE = "com/macys/selection/xapi/wishlist/converters/Success_response_on_create_list.json";
  private static String SPECIFICAL_LIST_GUID = "8db568b8-8d17-48b4-8bbb-23d655146fc9";
  private static String SPECIFICAL_LIST_NAME = "The 12th list";
  private static String ERROR_CUSTOMERMSP_FILE = "com/macys/selection/xapi/wishlist/converters/Error_CustomerMSP.json";
  private static final int TIME_1 = 1;
  private static String WISH_LIST_ONLY_JSON_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_response.json";
  private static String AVAILABILITY = "availability";
  private static final String SECURITY_TOKEN = "xxx-token";
  private static final String LIST_GUID = "xxx-guid";
  private static final Long USER_ID = 10L;
  private static final String WISH_LISTS_JSON_FILE = "com/macys/selection/xapi/list/client/response/wishlist_error_customer.json";

  @Mock
  private CustomerServiceRestClient restClient;

  @Mock
  private PromotionsRestClient promotionsRestClient;

  @Mock
  private JsonResponseParserPromotions promotions;

  @Mock
  private JsonToPromotionConverter promotionsConverter;

  @Mock
  private ListItemExitsValidator listItemExitsValidator;

  @Mock
  private KillSwitchPropertiesBean killswitchPropertiesBean;

  @InjectMocks
  private ListUtil listUtil;

  @InjectMocks
  private CustomerService customerService;

  @BeforeMethod
  public void init() {

  	MockitoAnnotations.initMocks(this);

  	customerService.setListUtil(listUtil);

	  when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(false);
	  listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
	  customerService.setListUtil(listUtil);
  }
  
  /* --------test cases for addItemToGivenListByUP--------- */
  
  private CustomerList getCustomerListOnAddToGivenList() {
	  CustomerList inputCustomerList = new CustomerList();
	  List<Item> itemList = CustomerListRestClientTest.getTestItemList();
	  WishList wishList = new WishList();
	  wishList.setItems(itemList);
	  List<WishList> wishLists = new ArrayList<WishList>();
	  wishLists.add(wishList);
	  inputCustomerList.setWishlist(wishLists);
	  
	  User user = new User();
	  user.setId(12345L);
	  user.setGuid("test12345");
	  inputCustomerList.setUser(user);
	  return inputCustomerList;
  }
  
  @Test
  public void addItemToGivenListByUPCTest() throws IOException, ParseException {
	  UserQueryParam testingUserQueryParam = new UserQueryParam();
	  String successCreateList = TestUtils.readFile(SUCCESS_RESPONSE_FILE);
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(successCreateList);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenReturn(restResponse);
	  when(listItemExitsValidator.isValid(any())).thenReturn(true);

	  ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
	  CustomerList customerList = customerService.addItemToGivenListByUPC(cookie, testingUserQueryParam, TEST_LIST_GUID, getCustomerListOnAddToGivenList());
	  WishList wishList = customerList.getWishlist().get(0);
	  assertNotNull(wishList);
	  assertTrue(SPECIFICAL_LIST_GUID.equals(wishList.getListGuid()));
	  assertTrue(SPECIFICAL_LIST_NAME.equals(wishList.getName()));
	  assertTrue(WishlistConstants.WISH_LIST_TYPE_VALUE.equals(wishList.getListType()));
	  assertTrue(!wishList.isDefaultList());
	  assertTrue(!wishList.isOnSaleNotify());
	  assertTrue(wishList.isSearchable());
	  
  }
  
  @Test
  public void failedAddItemToGivenListByUPCTest() throws IOException {
	  
	  UserQueryParam testingUserQueryParam = new UserQueryParam();
	  
	  String mspCustomerResponseOnError = TestUtils.readFile(ERROR_CUSTOMERMSP_FILE);
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(mspCustomerResponseOnError);
	  restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
	  when(listItemExitsValidator.isValid(any())).thenReturn(true);
	  
	  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenReturn(restResponse);
      try {
		  ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
    	  customerService.addItemToGivenListByUPC(cookie, testingUserQueryParam, TEST_LIST_GUID, getCustomerListOnAddToGivenList());
      } catch (ListServiceException e) {
    	  assertTrue(mspCustomerResponseOnError.equals(e.getServiceError()));
    	  assertTrue(e.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
      }
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void restExcpOnAddItemToGivenListByUPCTest() throws IOException {
	  UserQueryParam testingUserQueryParam = new UserQueryParam();
	  RestException restException = new RestException("RestException from the RestClient");
	  when(listItemExitsValidator.isValid(any())).thenReturn(true);
	  
	  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenThrow(restException);

	  ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
	  CustomerList inputCustomerList = getCustomerListOnAddToGivenList();
      customerService.addItemToGivenListByUPC(cookie, testingUserQueryParam, TEST_LIST_GUID, inputCustomerList);
  }
  
  @Test
  public void addItemToGivenListByUPCResponseNullTest() throws IOException, ParseException {
	  UserQueryParam testingUserQueryParam = new UserQueryParam();
	  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenReturn(null);
	  
	  CustomerList customerList = new CustomerList();
	  WishList wishList = new WishList();
	  List<WishList> wishLists = new ArrayList<>();
	  wishLists.add(wishList);
	  Item item = new Item();
	  List<Item> items = new ArrayList<>();
	  items.add(item);
	  customerList.setWishlist(wishLists);

	  ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
	  customerService.addItemToGivenListByUPC(cookie, testingUserQueryParam, TEST_LIST_GUID, customerList);
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void addItemToGivenListByUPCOnJsonExceptionsTest() throws IOException, ParseException {
	  UserQueryParam testingUserQueryParam = new UserQueryParam();

	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenReturn(restResponse);
	  
	  CustomerList customerList = new CustomerList();
	  WishList wishList = new WishList();
	  List<WishList> wishLists = new ArrayList<>();
	  wishLists.add(wishList);
	  Item item = new Item();
	  List<Item> items = new ArrayList<>();
	  items.add(item);
	  customerList.setWishlist(wishLists);

	  ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
	  customerService.addItemToGivenListByUPC(cookie, testingUserQueryParam, TEST_LIST_GUID, customerList);
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testGetCustomerListByGuidOnParseException() throws IOException, ParseException {
	  ListQueryParam testingListQueryParam = new ListQueryParam();
	  PaginationQueryParam testingPaginationQueryParam = new PaginationQueryParam();
	  
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
	  
	  customerService.getCustomerListByGuid(null, TEST_LIST_GUID, testingListQueryParam, testingPaginationQueryParam);
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testGetCustomerListByGuidOnIOException() throws IOException, ParseException {
	  ListQueryParam testingListQueryParam = new ListQueryParam();
	  PaginationQueryParam testingPaginationQueryParam = new PaginationQueryParam();
	  
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
	  
	  customerService.getCustomerListByGuid(null, TEST_LIST_GUID, testingListQueryParam, testingPaginationQueryParam);
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testCreateWishListOnIOException() throws IOException, ParseException {
	  
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.createList(any(), any())).thenReturn(restResponse);
	  
	  CustomerList inputCustomerList = new CustomerList();
	  ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
	  customerService.createWishList(cookie, inputCustomerList);
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testAddToDefaultWishlistByUpcIDOnIOException() throws IOException, ParseException {
	  
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.addToDefaultWishlist(any())).thenReturn(restResponse);
	  
	  customerService.addToDefaultWishlist(new CustomerList());
  }

  @Test(expectedExceptions = ListServiceException.class)
  public void testAddToDefaultWishlistByProductIdOnIOException() throws IOException, ParseException {
	  
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.addToDefaultWishlist(any())).thenReturn(restResponse);
	  when(listItemExitsValidator.isValid(any())).thenReturn(true);
	  
	  customerService.addToDefaultWishlist(new CustomerList());
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testAddItemToGivenListByUPCOnIOException() throws IOException, ParseException {
	  
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(listItemExitsValidator.isValid(any())).thenReturn(true);
	  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenReturn(restResponse);
	  when(listItemExitsValidator.isValid(any())).thenReturn(true);
	  
	  CustomerList inputCustomerList = getCustomerListOnAddToGivenList();
	  ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
	  customerService.addItemToGivenListByUPC(cookie, new UserQueryParam(), TEST_LIST_GUID, inputCustomerList);
  }

  @Test(expectedExceptions = ListServiceException.class)
  public void testEmailShareWishlistOnNullResponse() throws IOException, ParseException {
	  ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");

	  when(restClient.emailShareWishlist(any(), any(), any())).thenReturn(null);
	  EmailShare emailShareJson = new EmailShare();
	  customerService.emailShareWishlist(cookie, TEST_LIST_GUID, emailShareJson);
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testAddToGivenListOnParseException() throws IOException, ParseException {
	  
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());

	  when(listItemExitsValidator.isValid(any())).thenReturn(true);
	  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenReturn(restResponse);

	  ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
	  customerService.addItemToGivenListByUPC(cookie, new UserQueryParam(), TEST_LIST_GUID, getCustomerListOnAddToGivenList());
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testAddToGivenListOnBadRequest() throws IOException, ParseException {
	  
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());

	  when(listItemExitsValidator.isValid(any())).thenReturn(true);
	  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenReturn(restResponse);

	  ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
	  customerService.addItemToGivenListByUPC(cookie, new UserQueryParam(), TEST_LIST_GUID, getCustomerListOnAddToGivenList());
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testAddToGivenListOnResponse400() throws IOException, ParseException {
	  
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());

	  when(listItemExitsValidator.isValid(any())).thenReturn(true);
	  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenReturn(restResponse);

	  ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
	  customerService.addItemToGivenListByUPC(cookie, new UserQueryParam(), TEST_LIST_GUID, getCustomerListOnAddToGivenList());
  }
  
  @Test
  public void testAddToGivenListOnNullResponse() throws IOException, ParseException {

	  when(listItemExitsValidator.isValid(any())).thenReturn(true);
	  when(restClient.addItemToGivenListByUPC(any(), any(), any(), any())).thenReturn(null);

	  ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
	  customerService.addItemToGivenListByUPC(cookie, new UserQueryParam(), TEST_LIST_GUID, getCustomerListOnAddToGivenList());
  }
  
  @Test
  public void testGetListByGuidFilterByAvailability() throws IOException, ParseException {
    // setting up test data
    String mspCustomerResponse = TestUtils.readFile(WISH_LIST_ONLY_JSON_FILE);
    RestResponse restResponse = new RestResponse();
    restResponse.setBody(mspCustomerResponse);
    restResponse.setStatusCode(Response.Status.OK.getStatusCode());

    List<WishList> list = CustomerServiceTest.expectedWishList();

    when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);

    when(promotionsRestClient.getPromotions(any())).thenReturn(mock(RestResponse.class));
    when(promotions.parse(any())).thenReturn(mock(JsonNode.class));
    when(promotionsConverter.convert(any(), any(), any())).thenReturn(list.get(0));
    
    ListQueryParam filterSetListQueryParam = new ListQueryParam();
    filterSetListQueryParam.setFilter(AVAILABILITY);

    CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, filterSetListQueryParam, new PaginationQueryParam());

    assertNotNull(customerList);
    verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
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
		CustomerList response = customerService.getCustomerList(new UserQueryParam(), new ListQueryParam(), new PaginationQueryParam());
		assertNotNull(response);
	}

}
