package com.macys.selection.xapi.list.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.macys.selection.xapi.list.client.CustomerServiceRestClient;
import com.macys.selection.xapi.list.client.PromotionsRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.request.CustomerListMerge;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.client.response.CustomerWishListsResponse;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserPromotions;
import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.data.converters.JsonToPromotionConverter;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.*;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import com.macys.selection.xapi.list.util.ListUtil;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@SpringBootTest
public class CustomerServiceTest extends AbstractTestNGSpringContextTests {
	
  private static String SUCCESS = "Success";
  private static String TEST_SECURITY_TOKEN = "testingSecurityToken";
  private static String SUCCESS_RESPONSE_FILE = "com/macys/selection/xapi/wishlist/converters/Success_response_on_create_list.json";
  private static String SUCCESS_ANALYTICS_RESPONSE_FILE = "com/macys/selection/xapi/wishlist/converters/Success_analytics_response_add_to_list.json";
  private static String ERROR_CUSTOMERMSP_FILE = "com/macys/selection/xapi/wishlist/converters/Error_CustomerMSP.json";
  private static String WISH_LIST_JSON_FILE = "com/macys/selection/xapi/wishlist/converters/wishlist.json";
  private static String MULTIPLE_WISH_LISTS_JSON_FILE = "com/macys/selection/xapi/wishlist/converters/multiple_wishlists.json";
  private static String WISH_LIST_ONLY_JSON_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_response.json";
  private static String WISH_LIST_WITH_FINALPRICE_JSON_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_with_finalPrice_response.json";
  private static String WISH_LIST_WITH_FINALPRICE_PRODUCT_JSON_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_with_finalPrice_product_response.json";
  private static String WISH_LIST_WITH_FINALPRICE_PRODUCT_JSON_MASK_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_with_finalPrice_product_response_mask.json";
  private static String WISH_LIST_WITH_FINALPRICE__NO_DISPLAY_JSON_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_with_finalPrice_Display_No_response.json";
  private static String WISH_LIST_WITH_FINALPRICE__CONDITIONAL_DISPLAY_MAP_JSON_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_with_finalPrice_Display_Conditional_response_Map.json";
  private static String WISH_LIST_WITH_FINALPRICE__CONDITIONAL_DISPLAY_MASK_JSON_FILE= "com/macys/selection/xapi/list/client/response/customermsp_wishlist_with_finalPrice_Display_Conditional_response_Mask.json";
  private static String WISH_LIST_WITH_FINALPRICE__PRICE_DROP_JSON_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_with_finalPrice_price_drop_response.json";
  private static String GET_ALL_LISTS_ERROR_FILE = "com/macys/selection/xapi/wishlist/converters/getAllLists_Error.json";
  private static String DELETE_ITEM_ERROR_FILE = "com/macys/selection/xapi/wishlist/converters/deleteItem_Error.json";
  private static String UPDATE_LIST_ERROR_JSON = "com/macys/selection/xapi/wishlist/converters/updateList_Error.json";
  private static String TEST_LIST_GUID = "testingListGuid";
  private static String REST_EXCEP = "RestException from the RestClient";
  private static String SPECIFICAL_LIST_GUID = "8db568b8-8d17-48b4-8bbb-23d655146fc9";
  private static String SPECIFICAL_LIST_NAME = "The 12th list";
  private static final int TIME_1 = 1;
  private static final long LIST_ID = 10680246L;
  private static final String TEST_LIST_NAME = "testName";
  private static final int QTY = 1;
  private static final int TEST_ID = 4835927;
  private static final double TEST_PRICE = 99.0;
  private static final long TEST_LONG = 3213253243L;
  private static final String SECURITY_TOKEN = "xxx-token";
  private static final String LIST_GUID = "xxx-guid";
  private static final Long USER_ID_COOKIES = 10L;
  private static final String ANALYTICS_RESPONSE_PRODUCT_ID = "1483935";
  private static String ANALYTICS_RESPONSE_PRODUCT_NAME = "Badgley Mischka Kiara Embellished Peep-Toe Evening Pumps";
  private static String ANALYTICS_RESPONSE_PRODUCT_UPC = "749908798366";
  private static final String PRIORITY = "H";


  @Mock
  private CustomerServiceRestClient restClient;

  @Mock
  private PromotionsRestClient promotionsRestClient;

  @Mock
  private JsonResponseParserPromotions promotions;

  @Mock
  private JsonToPromotionConverter promotionsConverter;

  @Mock
  private KillSwitchPropertiesBean killswitchPropertiesBean;

  @InjectMocks
  private ListUtil listUtil;

  @InjectMocks
  private CustomerService customerService;



    @BeforeMethod
  public void init() {
    MockitoAnnotations.initMocks(this);
	JsonToObjectConverter<WishList> wishlistConverter = new JsonToObjectConverter<>(WishList.class);
	customerService.setWishListConverter(wishlistConverter);
	JsonToObjectConverter<CustomerWishListsResponse> wishlistsConverter = new JsonToObjectConverter<>(CustomerWishListsResponse.class);
	customerService.setWishlistsConverter(wishlistsConverter);
	customerService.setListUtil(listUtil);

  }

  @Test
  public void testGelAllWishlistsByUserId() throws IOException {
    String mspCustomerResponse = TestUtils.readFile(WISH_LIST_JSON_FILE);
    RestResponse restResponse = new RestResponse();
    restResponse.setBody(mspCustomerResponse);
    restResponse.setStatusCode(Response.Status.OK.getStatusCode());

    when(restClient.get(any(), any(), any())).thenReturn(restResponse);

    CustomerList customerList = customerService.getCustomerList(new UserQueryParam(), new ListQueryParam(), new PaginationQueryParam());

    assertNotNull(customerList);
    verify(restClient, times(TIME_1)).get(any(), any(), any());
  }
  
  /**
   * Test the cases that there are more than 1 wishlist in the result
   * @throws IOException
   */
  @Test
  public void testGetMultipleWishlistsByUserId() throws IOException {
    String mspCustomerResponse = TestUtils.readFile(MULTIPLE_WISH_LISTS_JSON_FILE);
    RestResponse restResponse = new RestResponse();
    restResponse.setBody(mspCustomerResponse);
    restResponse.setStatusCode(Response.Status.OK.getStatusCode());

    when(restClient.get(any(), any(), any())).thenReturn(restResponse);

    CustomerList customerList = customerService.getCustomerList(new UserQueryParam(), new ListQueryParam(), new PaginationQueryParam());

    assertNotNull(customerList);
    verify(restClient, times(TIME_1)).get(any(), any(), any());
  }
  
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testGelAllWishlistsByUserIdServiceError() throws IOException {
    String mspCustomerResponse = TestUtils.readFile(GET_ALL_LISTS_ERROR_FILE);
    RestResponse restResponse = new RestResponse();
    restResponse.setBody(mspCustomerResponse);
    restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());

    when(restClient.get(any(), any(), any())).thenReturn(restResponse);

    CustomerList customerList = customerService.getCustomerList(new UserQueryParam(), new ListQueryParam(), new PaginationQueryParam());

    assertNotNull(customerList);
    verify(restClient, times(TIME_1)).get(any(), any(), any());
  }
  
  

  /**
   *    there should be separate test cases for promotions
   **/
  @Test
  public void testGetListByGuid() throws IOException {
    // setting up test data
    String mspCustomerResponse = TestUtils.readFile(WISH_LIST_ONLY_JSON_FILE);
    RestResponse restResponse = new RestResponse();
    restResponse.setBody(mspCustomerResponse);
    restResponse.setStatusCode(Response.Status.OK.getStatusCode());

    when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);

    CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, new ListQueryParam(), new PaginationQueryParam());

    assertNotNull(customerList);
    verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testGetListByGuidServiceErrors() throws IOException, ParseException {
    // setting up test data
    String mspCustomerResponse = TestUtils.readFile(GET_ALL_LISTS_ERROR_FILE);
    RestResponse restResponse = new RestResponse();
    restResponse.setBody(mspCustomerResponse);
    restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());

    List<WishList> list = expectedWishList();

    when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);

    when(promotionsRestClient.getPromotions(any())).thenReturn(mock(RestResponse.class));
    when(promotions.parse(any())).thenReturn(mock(JsonNode.class));
    when(promotionsConverter.convert(any(), any(), any())).thenReturn(list.get(0));

    CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, new ListQueryParam(), new PaginationQueryParam());

    assertNotNull(customerList);
    verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
  }

  @Test
  public void testGetListByGuidEmptyServiceErrors() throws IOException, ParseException {
    RestResponse restResponse = new RestResponse();
    restResponse.setBody(null);
    restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());

    when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(null);

    CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, new ListQueryParam(), new PaginationQueryParam());

    assertNotNull(customerList);
    verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
  }

    @Test(description = "Test fianlPrice object at UPC level part of the customer response when finalPrice Killswitch is on")
    public void testGetListByGuidWithUpcFinalPrice() throws IOException {
        // setting up test data
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setCustomerState("SIGNED_IN");
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, listQueryParam, new PaginationQueryParam());
        assertNotNull(customerList);
        assertNotNull(customerList.getWishlist().get(0).getItems().get(0).getUpc().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test fianlPrice object at Product level part of the customer response when finalPrice Killswitch is on")
    public void testGetListByGuidWithProductFinalPrice() throws IOException {
        // setting up test data
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE_PRODUCT_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setCustomerState("SIGNED_IN");
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, listQueryParam, new PaginationQueryParam());
        assertNotNull(customerList);
        assertNotNull(customerList.getWishlist().get(0).getItems().get(0).getProduct().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test fianlPrice object at Product level part of the customer response when conditionally show guest")
    public void testGetListByGuidWithProductFinalPriceConditionallyShowMap() throws IOException {
        // setting up test data
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE_PRODUCT_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setCustomerState("GUEST");
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, listQueryParam, new PaginationQueryParam());
        assertNotNull(customerList);
        assertNull(customerList.getWishlist().get(0).getItems().get(0).getProduct().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test fianlPrice object at Product level part of the customer response when conditionally show MAP signed in")
    public void testGetListByGuidWithProductFinalPriceConditionallyShowMapSignedIn() throws IOException {
        // setting up test data
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE_PRODUCT_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setCustomerState("SIGNED_IN");
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, listQueryParam, new PaginationQueryParam());
        assertNotNull(customerList);
        assertNotNull(customerList.getWishlist().get(0).getItems().get(0).getProduct().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test fianlPrice object at Product level part of the customer response when conditionally MASK show signed in")
    public void testGetListByGuidWithProductFinalPriceConditionallyShowMaskSignedIn() throws IOException {
        // setting up test data
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE_PRODUCT_JSON_MASK_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setCustomerState("SIGNED_IN");
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, listQueryParam, new PaginationQueryParam());
        assertNotNull(customerList);
        assertNotNull(customerList.getWishlist().get(0).getItems().get(0).getProduct().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test fianlPrice object not returned at UPC level part of the customer response when finalPrice Killswitch is on but marked as Never Show")
    public void testGetListByGuidWithFinalPriceNeverShow() throws IOException {
        // setting up test data
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE__NO_DISPLAY_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, new ListQueryParam(), new PaginationQueryParam());
        assertNotNull(customerList);
        assertNull(customerList.getWishlist().get(0).getItems().get(0).getUpc().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test fianlPrice object not returned at UPC level part of the customer response when finalPrice Killswitch is on but marked as Conditionally Show")
    public void testGetListByGuidWithFinalPriceConditionallyShow() throws IOException {
        // setting up test data
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE__CONDITIONAL_DISPLAY_MAP_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, new ListQueryParam(), new PaginationQueryParam());
        assertNotNull(customerList);
        assertNull(customerList.getWishlist().get(0).getItems().get(0).getUpc().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }


    @Test(description = "Test fianlPrice object not returned at UPC level part of the customer response when finalPrice Killswitch but Item has priceDrop alert")
    public void testGetListByGuidWithFinalPricePriceDrop() throws IOException {
        // setting up test data
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE__PRICE_DROP_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, new ListQueryParam(), new PaginationQueryParam());
        assertNotNull(customerList);
        assertNull(customerList.getWishlist().get(0).getItems().get(0).getUpc().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test fianlPrice object not returned when finalPrice feature is off")
    public void testGetListByGuidWithFinalPriceFeatureOff() throws IOException {
        // setting up test data
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE_JSON_FILE);
        KillSwitchPropertiesBean killSwitchPropertiesBean = new KillSwitchPropertiesBean();
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(false);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, new ListQueryParam(), new PaginationQueryParam());
        assertNotNull(customerList);
        assertNull(customerList.getWishlist().get(0).getItems().get(0).getUpc().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }
    @Test(description = "Test UPC finalPrice is not displayed when productTypePromotion is MAP, displayFP is conditionally show and user is not signed in = GUEST")
    public void testGetListByGuidWithFinalPriceConditionallyShowMapGuest() throws IOException{
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setCustomerState("GUEST");
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE__CONDITIONAL_DISPLAY_MAP_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, listQueryParam, new PaginationQueryParam());
        assertNotNull(customerList);
        assertNull(customerList.getWishlist().get(0).getItems().get(0).getUpc().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test finalPrice is displayed when productTypePromotion is MAP, displayFP is conditionally show and user is signed in = SIGNED_IN")
    public void testGetListByGuidWithFinalPriceConditionallyShowMapSignedin() throws IOException{
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setCustomerState("SIGNED_IN");
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE__CONDITIONAL_DISPLAY_MAP_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, listQueryParam, new PaginationQueryParam());
        assertNotNull(customerList);
        assertNotNull(customerList.getWishlist().get(0).getItems().get(0).getUpc().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test UPC finalPrice is displayed when productTypePromotion is MAP, displayFP is conditionally show and user is soft signedin RECOGNIZED")
    public void testGetListByGuidWithFinalPriceConditionallyShowMapSoftsignIn() throws IOException{
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setCustomerState("RECOGNIZED");
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE__CONDITIONAL_DISPLAY_MAP_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, listQueryParam, new PaginationQueryParam());
        assertNotNull(customerList);
        assertNotNull(customerList.getWishlist().get(0).getItems().get(0).getUpc().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test UPC finalPrice is displayed when productTypePromotion is MASK, displayFP is conditionally show and user is soft signedin RECOGNIZED")
    public void testGetListByGuidWithFinalPriceConditionallyShowMaskSoftsignIn() throws IOException{
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setCustomerState("RECOGNIZED");
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE__CONDITIONAL_DISPLAY_MASK_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, listQueryParam, new PaginationQueryParam());
        assertNotNull(customerList);
        assertNotNull(customerList.getWishlist().get(0).getItems().get(0).getUpc().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test finalPrice isdisplayed when productTypePromotion is MASK, displayFP is conditionally show and user is signed in = SIGNED_IN")
    public void testGetListByGuidWithFinalPriceConditionallyShowMaskSignedin() throws IOException{
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setCustomerState("SIGNED_IN");
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE__CONDITIONAL_DISPLAY_MASK_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, listQueryParam, new PaginationQueryParam());
        assertNotNull(customerList);
        assertNotNull(customerList.getWishlist().get(0).getItems().get(0).getUpc().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

    @Test(description = "Test finalPrice isdisplayed when productTypePromotion is MASK, displayFP is conditionally show and user is signed in = SIGNED_IN")
    public void testGetListByGuidWithFinalPriceConditionallyShowMaskGuest() throws IOException{
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setCustomerState("GUEST");
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_WITH_FINALPRICE__CONDITIONAL_DISPLAY_MASK_JSON_FILE);
        when(killswitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listUtil.setKillswitchPropertiesBean(killswitchPropertiesBean);
        customerService.setListUtil(listUtil);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getListByGuid(any(), any(), any(), any(), any())).thenReturn(restResponse);
        CustomerList customerList = customerService.getCustomerListByGuid(null, TEST_LIST_GUID, listQueryParam, new PaginationQueryParam());
        assertNotNull(customerList);
        assertNotNull(customerList.getWishlist().get(0).getItems().get(0).getUpc().getFinalPrice());
        verify(restClient, times(TIME_1)).getListByGuid(any(), any(), any(), any(), any());
    }

  @Test
  public void testMoveItemToWishlist(){
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.moveItemToWishlist(any(), any(), any())).thenReturn(restResponse);
	  customerService.moveItemToWishlist(any(), any(), any());
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testMoveItemToWishlistServiceError() throws IOException {
	  String testingListGuid = TEST_LIST_GUID;
	  String mspCustomerResponseOnError = TestUtils.readFile(ERROR_CUSTOMERMSP_FILE);
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(mspCustomerResponseOnError);
	  restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());

      when(restClient.moveItemToWishlist(any(), any(), any())).thenReturn(restResponse);
	  CustomerList testCustomerList = new CustomerList();
	  customerService.moveItemToWishlist(SECURITY_TOKEN, testingListGuid, testCustomerList);
  }
  
  @Test(expectedExceptions = RestException.class)
  public void testMoveItemToWishlistExceptiom() throws IOException {
	  String testingListGuid = TEST_LIST_GUID;
	  RestException restException = new RestException(REST_EXCEP);

      when(restClient.moveItemToWishlist(any(), any(), any())).thenThrow(restException);

	  CustomerList testCustomerList = new CustomerList();
	  customerService.moveItemToWishlist(SECURITY_TOKEN, testingListGuid, testCustomerList);
  }

  @Test
  public void testDeleteItem() {
	  RestResponse restResponse = new RestResponse();
	  restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());

      when(restClient.delete(any(), any(), any())).thenReturn(restResponse);

      customerService.deleteItem(any(), any(), any());
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testDeleteItemServiceError() throws IOException {
	  String errorMessageString = TestUtils.readFile(DELETE_ITEM_ERROR_FILE);
	  RestResponse restResponse = new RestResponse();
	  restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
	  restResponse.setBody(errorMessageString);

      when(restClient.delete(any(), any(), any())).thenReturn(restResponse);
	  customerService.deleteItem(any(), any(), any());
  }
  
  @Test(expectedExceptions = RestException.class)
  public void testDeleteItemException() throws IOException {
      when(restClient.delete(any(), any(), any())).thenThrow(new RestException(REST_EXCEP));
	  customerService.deleteItem(any(), any(), any());
  }
  
  @Test
  public void testUpdateWishlist() {
	  RestResponse restResponse = new RestResponse();
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.updateWishlist(any(), any(), any(), any())).thenReturn(restResponse);
	  customerService.updateWishlist(any(), any(), any(), any());
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void testUpdateWishlistServiceErrors() throws IOException{
	  String errorMessageString = TestUtils.readFile(UPDATE_LIST_ERROR_JSON);
	  RestResponse restResponse = new RestResponse();
	  restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
	  restResponse.setBody(errorMessageString);
	  when(restClient.updateWishlist(any(), any(), any(), any())).thenReturn(restResponse);
	  customerService.updateWishlist(any(), any(), any(), any());
  }
  
  @Test(expectedExceptions = RestException.class)
  public void testUpdateWishlistException() throws IOException{
	  when(restClient.updateWishlist(any(), any(), any(), any())).thenThrow(new RestException(REST_EXCEP));
	  customerService.updateWishlist(any(), any(), any(), any());
  }

  /**
   * setting up the expected customer list data
   **/
  public static List<WishList> expectedWishList() throws ParseException {
    List<WishList> lists = new ArrayList<>();
    WishList list = new WishList();
    list.setId(LIST_ID);
    list.setListGuid(TEST_LIST_GUID);
    list.setName(TEST_LIST_NAME);
    list.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
    list.setDefaultList(Boolean.TRUE);
    list.setOnSaleNotify(Boolean.FALSE);
    list.setSearchable(Boolean.FALSE);
    list.setNumberOfItems(1);
    list.setShowPurchaseInfo(Boolean.TRUE);
    
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");    
    list.setCreatedDate(df.parse("2017-09-14T18:27:54.201"));
    list.setLastModified(df.parse("2017-09-14T18:27:54.201"));

    User user = new User();
    user.setId(LIST_ID);
    user.setGuid(TEST_LIST_GUID);
    user.setGuestUser(Boolean.TRUE);

    Price price = new Price();
    price.setRetailPrice(TEST_PRICE);
    price.setOriginalPrice(TEST_PRICE);
    price.setIntermediateSalesValue(TEST_PRICE);
    price.setSalesValue(TEST_PRICE);
    price.setOnSale(Boolean.FALSE);
    price.setUpcOnSale(Boolean.FALSE);
    price.setPriceType(QTY);
    price.setBasePriceType(QTY);

    Availability avail = new Availability();
    avail.setAvailable(Boolean.TRUE);
    avail.setUpcAvailabilityMessage(TEST_LIST_GUID);
    avail.setInStoreEligible(Boolean.TRUE);
    avail.setOrderMethod(TEST_LIST_GUID);

    Product product = new Product();
    product.setId(TEST_ID);
    product.setName(TEST_LIST_NAME);
    product.setActive(Boolean.TRUE);
    product.setPrimaryImage(TEST_LIST_NAME);
    product.setLive(Boolean.TRUE);
    product.setAvailable(Boolean.TRUE);

    Upc upc = new Upc();
    upc.setId(TEST_ID);
    upc.setUpcNumber(TEST_LONG);
    upc.setColor(TEST_LIST_NAME);
    upc.setSize(TEST_LIST_NAME);
    upc.setPrice(price);
    upc.setAvailability(avail);
    upc.setType(TEST_LIST_NAME);
    upc.setProduct(product);

    Item item = new Item();
    item.setId(TEST_ID);
    item.setItemGuid(TEST_LIST_GUID);
    item.setRetailPriceWhenAdded(TEST_PRICE);
    item.setRetailPriceDropAfterAddedToList(TEST_PRICE);
    item.setRetailPriceDropPercentage(QTY);
    item.setQtyRequested(QTY);
    item.setQtyStillNeeded(QTY);

    List<Promotion> samplePromotions = new ArrayList<>();
    Promotion promotion = new Promotion();
    promotion.setBadgeTextAttributeValue(TEST_LIST_GUID);
    promotion.setPromotionId(TEST_LONG);
    samplePromotions.add(promotion);
    item.setPromotions(samplePromotions);

    SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    item.setAddedDate(tempDate.parse("2017-09-14T18:27:54.203"));        
    item.setLastModified(tempDate.parse("2017-09-14T18:27:54.204"));
    item.setUpc(upc);
    List<Item> items = new ArrayList<>();
    items.add(item);
    list.setItems(items);

    lists.add(list);
    return lists;
  }
  
  @Test
  public void createWishlistTest() throws IOException {
	  String successCreateList = TestUtils.readFile(SUCCESS_RESPONSE_FILE);
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(successCreateList);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.createList(any(), any())).thenReturn(restResponse);
	  
	  CustomerList inputCustomerList = new CustomerList();
      ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
      CustomerList result = customerService.createWishList(cookie, inputCustomerList);
	  WishList wishList = result.getWishlist().get(0);
	  
	  assertNotNull(wishList);
	  assertTrue(SPECIFICAL_LIST_GUID.equals(wishList.getListGuid()));
	  assertTrue(SPECIFICAL_LIST_NAME.equals(wishList.getName()));
	  assertTrue(WishlistConstants.WISH_LIST_TYPE_VALUE.equals(wishList.getListType()));
	  assertTrue(!wishList.isDefaultList());
	  assertTrue(!wishList.isOnSaleNotify());
	  assertTrue(wishList.isSearchable());
	  
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void failedToCreateListTest() throws IOException {
	  
	  String mspCustomerResponseOnError = TestUtils.readFile(ERROR_CUSTOMERMSP_FILE);
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(mspCustomerResponseOnError);
	  restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
	  
	  when(restClient.createList(any(), any())).thenReturn(restResponse);
	  CustomerList inputCustomerList = new CustomerList();
      ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
      customerService.createWishList(cookie, inputCustomerList);
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void restExceptionOnCreateListTest() throws IOException {
	  RestException restException = new RestException(REST_EXCEP);

	  when(restClient.createList(any(), any())).thenThrow(restException);
	  CustomerList inputCustomerList = new CustomerList();
      ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
      customerService.createWishList(cookie, inputCustomerList);
  }
  
  @Test
  public void createListResponseNullTest() throws IOException, ParseException {
	  when(restClient.createList(any(), any())).thenReturn(null);
	  
	  CustomerList inputCustomerList = new CustomerList();
      ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
      customerService.createWishList(cookie, inputCustomerList);
  }
  
  @Test
  public void shareEmailWishlistTest(){
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(SUCCESS);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.emailShareWishlist(any(), any(), any())).thenReturn(restResponse);
	  customerService.emailShareWishlist(any(), any(), any());
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void failedToEmailShareListTest() throws IOException {
	  String testingListGuid = TEST_LIST_GUID;
      ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);

	  RestResponse restResponse = new RestResponse();
	  String mspCustomerResponseOnError = TestUtils.readFile(ERROR_CUSTOMERMSP_FILE);
	  restResponse.setBody(mspCustomerResponseOnError);
	  restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
	  
	  EmailShare emailShareJson = new EmailShare();
	  when(restClient.emailShareWishlist(any(), any(), any())).thenReturn(restResponse);
      customerService.emailShareWishlist(cookie, testingListGuid, emailShareJson);
  }
  
  @Test(expectedExceptions = {ListServiceException.class}, expectedExceptionsMessageRegExp="RestException from the RestClient")
  public void restExceptionOnEmailShareListTest() throws IOException {
	  String testingListGuid = TEST_LIST_GUID;
      ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
      RestException restException = new RestException(REST_EXCEP);

	  
	  when(restClient.emailShareWishlist(any(), any(), any())).thenThrow(restException);
	  EmailShare emailShareJson = new EmailShare();
      customerService.emailShareWishlist(cookie, TEST_LIST_GUID, emailShareJson);
  }

  /* --------test cases for AddItemToDefaultListByUpcId--------- */
  @Test
  public void addToDefaultWishlistByUpcIDTest() throws IOException, ParseException {
	  String successCreateList = TestUtils.readFile(SUCCESS_RESPONSE_FILE);
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(successCreateList);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.addToDefaultWishlist(any())).thenReturn(restResponse);
	  
	  CustomerList testCustomerList = customerService.addToDefaultWishlist(new CustomerList());
	  
	  assertNotNull(testCustomerList);
	  WishList wishList = testCustomerList.getWishlist().get(0);
	  assertTrue(SPECIFICAL_LIST_GUID.equals(wishList.getListGuid()));
	  assertTrue(SPECIFICAL_LIST_NAME.equals(wishList.getName()));
	  assertTrue(WishlistConstants.WISH_LIST_TYPE_VALUE.equals(wishList.getListType()));
	  assertTrue(!wishList.isDefaultList());
	  assertTrue(!wishList.isOnSaleNotify());
	  assertTrue(wishList.isSearchable());
  }
    @Test
    public void testWishListAnalyticsAddToListByUpcIDResponse() throws IOException, ParseException {
        String successCreateList = TestUtils.readFile(SUCCESS_ANALYTICS_RESPONSE_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(successCreateList);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.addToDefaultWishlist(any())).thenReturn(restResponse);

        CustomerList testCustomerList = customerService.addToDefaultWishlist(new CustomerList());
        AnalyticsMeta analyticsMeta = testCustomerList.getMeta();
        Analytics analytics = analyticsMeta.getAnalytics();
        ResponseDigitalAnalytics responseDigitalAnalytics = (ResponseDigitalAnalytics) analytics.getDigitalAnalytics();

        assertNotNull(analyticsMeta);
        assertTrue(ANALYTICS_RESPONSE_PRODUCT_ID.equals(responseDigitalAnalytics.getProductId().get(0)));
        assertTrue(ANALYTICS_RESPONSE_PRODUCT_NAME.equals(responseDigitalAnalytics.getProductName().get(0)));
        assertTrue(ANALYTICS_RESPONSE_PRODUCT_UPC.equals(responseDigitalAnalytics.getProductUPC().get(0)));

    }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void failedAddToDefaultWishlistByUpcIDTest() throws IOException {
	  String mspCustomerResponseOnError = TestUtils.readFile(ERROR_CUSTOMERMSP_FILE);
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(mspCustomerResponseOnError);
	  restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
	  when(restClient.addToDefaultWishlist(any())).thenReturn(restResponse);
      customerService.addToDefaultWishlist(new CustomerList());
  }
  
  @Test(expectedExceptions = {ListServiceException.class}, expectedExceptionsMessageRegExp="RestException from the RestClient")
  public void restExcpOnAddToDefaultWishlistByUpcIDTest() throws IOException {
	  RestException restException = new RestException(REST_EXCEP);
	  when(restClient.addToDefaultWishlist(any())).thenThrow(restException);
      customerService.addToDefaultWishlist(new CustomerList());
  }
    
  @Test
  public void addToDefaultWishlistByUpcIDResponseNullTest() throws IOException {
	  when(restClient.addToDefaultWishlist(any())).thenReturn(null);
	  customerService.addToDefaultWishlist(new CustomerList());
  }
  
  /* --------test cases for AddItemToDefaultListByProductId--------- */
  @Test
  public void addToDefaultWishlistByProductIdTest() throws IOException {
	  String successCreateList = TestUtils.readFile(SUCCESS_RESPONSE_FILE);
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(successCreateList);
	  restResponse.setStatusCode(Response.Status.OK.getStatusCode());
	  when(restClient.addToDefaultWishlist(any())).thenReturn(restResponse);
	 
	  CustomerList testCustomerList = customerService.addToDefaultWishlist(new CustomerList());
	  WishList wishList = testCustomerList.getWishlist().get(0);
	  
	  assertNotNull(wishList);
	  assertTrue(SPECIFICAL_LIST_GUID.equals(wishList.getListGuid()));
	  assertTrue(SPECIFICAL_LIST_NAME.equals(wishList.getName()));
	  assertTrue(WishlistConstants.WISH_LIST_TYPE_VALUE.equals(wishList.getListType()));
	  assertTrue(!wishList.isDefaultList());
	  assertTrue(!wishList.isOnSaleNotify());
	  assertTrue(wishList.isSearchable());
	  
  }
    
  @Test(expectedExceptions = ListServiceException.class)
  public void failedAddToDefaultWishlistByProductIdTest() throws IOException {
	  String mspCustomerResponseOnError = TestUtils.readFile(ERROR_CUSTOMERMSP_FILE);
	  RestResponse restResponse = new RestResponse();
	  restResponse.setBody(mspCustomerResponseOnError);
	  restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
	  
	  when(restClient.addToDefaultWishlist(any())).thenReturn(restResponse);
      customerService.addToDefaultWishlist(new CustomerList());
  }
    
  @Test(expectedExceptions = ListServiceException.class)
  public void restExcpOnAddToDefaultWishlistByProductIdTest() throws IOException {
	  RestException restException = new RestException(REST_EXCEP);
	  when(restClient.addToDefaultWishlist(any())).thenThrow(restException);
      customerService.addToDefaultWishlist(new CustomerList());
  }
  
  @Test
  public void addToDefaultWishlistByProductIdResponseNullTest() throws IOException {
	  when(restClient.addToDefaultWishlist(any())).thenReturn(null);
	  customerService.addToDefaultWishlist(new CustomerList()); 
  }

  @Test
  public void testMergeLists() {
    RestResponse response = new RestResponse();
    response.setStatusCode(Response.Status.OK.getStatusCode());
    when(restClient.mergeList(any())).thenReturn(response);
    customerService.mergeList(new CustomerListMerge());
  }

  @Test(expectedExceptions = ListServiceException.class)
  public void testMergeListsBadStatusCode() {
    RestResponse response = new RestResponse();
    response.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
    when(restClient.mergeList(any())).thenReturn(response);
    customerService.mergeList(new CustomerListMerge());
  }

    @Test
    public void testUpdateItemPriority() {
        String itemGuid = "itemGuid";
        String userGuid = "userGuid";
        long userId = 123;

        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        Item item = new Item();
        item.setPriority(PRIORITY);
        Mockito.when(restClient.updateItemPriority(eq(LIST_GUID), eq(itemGuid), eq(userId), eq(userGuid), any())).thenReturn(restResponse);
        customerService.updateItemPriority(LIST_GUID, itemGuid, userId, userGuid, item);
        Mockito.verify(restClient).updateItemPriority(eq(LIST_GUID), eq(itemGuid), eq(userId), eq(userGuid), any());
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testUpdateItemPriorityWithInvalidStatus() {
        String testError = "errors";
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(testError);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());

        Mockito.when(restClient.updateItemPriority(any(), any(), any(), any(), any())).thenReturn(restResponse);
        try {
            customerService.updateItemPriority(LIST_GUID, null, null, null, new Item());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(restResponse.getBody(), e.getServiceError());
            throw e;
        }
    }
}

