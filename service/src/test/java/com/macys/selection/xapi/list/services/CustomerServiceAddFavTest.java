package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.CustomerServiceRestClient;
import com.macys.selection.xapi.list.client.PromotionsRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.response.CustomerWishListsResponse;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserPromotions;
import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.data.converters.JsonToPromotionConverter;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.WishList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by m940030 on 11/7/17.
 */

@SpringBootTest
public class CustomerServiceAddFavTest extends AbstractTestNGSpringContextTests {

    private static String SUCCESS_CUSTOMERMSP_RESPONSE_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_hearts_success_response.json";
    private static String ERROR_FAVLIST_FILE = "com/macys/selection/xapi/wishlist/converters/Error_FavList.json";
    private static String REST_EXCEP = "RestException from the RestClient";
    private static String SPECIFICAL_LIST_GUID = "8db568b8-8d17-48b4-8bbb-23d655146fc9";
    private static final int QTY = 1;
    private static final int TEST_PRODUCT_ID = 22805;
    private static final long TEST_LONG = 3213253243L;
    private static String SPEFIC_USER_GUID = "df0637cb-bc0d-4f84-93a5-55b649e5f73d";
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceAddFavTest.class);
    private static String GET_CLIENT_EXP = "exception while getting clinet:";
    private static final String SECURITY_TOKEN = "xxx-token";
    private static final String LIST_GUID = "xxx-guid";
    private static final Long USER_ID = 10L;


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

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    	JsonToObjectConverter<WishList> wishlistConverter = new JsonToObjectConverter<>(WishList.class);
    	customerService.setWishListConverter(wishlistConverter);
    	JsonToObjectConverter<CustomerWishListsResponse> wishlistsConverter = new JsonToObjectConverter<>(CustomerWishListsResponse.class);
    	customerService.setWishlistsConverter(wishlistsConverter);
    }

    private CustomerList getCustomerListOnAddToGivenList() {
        CustomerList inputCustomerList = new CustomerList();
        User user = new User();
        user.setId(TEST_LONG);
        user.setGuid(SPEFIC_USER_GUID);
        List<Item> itemList = getTestItemList();
        WishList wishList = new WishList();
        wishList.setItems(itemList);
        List<WishList> wishLists = new ArrayList<WishList>();
        wishLists.add(wishList);
        inputCustomerList.setWishlist(wishLists);
        inputCustomerList.setUser(user);
        return inputCustomerList;
    }

    @Test
    public void addFavItemToGivenListByPID() throws IOException, ParseException {
        String successFavList = TestUtils.readFile(SUCCESS_CUSTOMERMSP_RESPONSE_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(successFavList);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.addFavItemToGivenListByPID(any(), any())).thenReturn(restResponse);

        ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
        CustomerList customerFavList = customerService.addFavItemToGivenListByPID(cookie, getCustomerListOnAddToGivenList());
        WishList wishList = customerFavList.getWishlist().get(0);
        assertNotNull(wishList);
        assertTrue(SPECIFICAL_LIST_GUID.equals(wishList.getListGuid()));
        assertTrue(wishList.isDefaultList());
        assertTrue(!wishList.isOnSaleNotify());
        assertTrue(!wishList.isSearchable());

    }


    @Test
    public void failedaddFavItemToGivenListByPIDTest() throws IOException {


        String mspCustomerResponseOnError = TestUtils.readFile(ERROR_FAVLIST_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponseOnError);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());

        when(restClient.addFavItemToGivenListByPID(any(), any())).thenReturn(restResponse);
        try {
            ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
            customerService.addFavItemToGivenListByPID(cookie, getCustomerListOnAddToGivenList());
        } catch (ListServiceException e) {
            LOGGER.error(GET_CLIENT_EXP, e);
            assertTrue(mspCustomerResponseOnError.equals(e.getServiceError()));
            assertTrue(e.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
        }
    }

    @Test
    public void testaddFavItemToGivenListOnNullCustomerList() throws IOException, ParseException {
        ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
        customerService.addFavItemToGivenListByPID(cookie,null);
    }


    @Test
    public void addFavItemToGivenListByPIDResponseNullTest() throws IOException, ParseException {
        when(restClient.addFavItemToGivenListByPID(any(), any())).thenReturn(null);

        CustomerList inputCustomerList = new CustomerList();
        ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
        customerService.addFavItemToGivenListByPID(cookie, inputCustomerList);

    }


    @Test(expectedExceptions = ListServiceException.class)
    public void restExcpOnAddFacItemToGivenListByPIDTest() throws IOException {
        RestException restException = new RestException(REST_EXCEP);

        when(restClient.addFavItemToGivenListByPID(any(), any())).thenThrow(restException);
        CustomerList inputCustomerList = getCustomerListOnAddToGivenList();

        ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
        customerService.addFavItemToGivenListByPID(cookie, inputCustomerList);
    }


    public static List<Item> getTestItemList() {
        List<Item> itemList = new ArrayList<Item>();
        Item item = new Item();
        item.setQtyRequested(QTY);
        Product prod = new Product();
        prod.setId(TEST_PRODUCT_ID);
        item.setProduct(prod);
        itemList.add(item);
        return itemList;
    }

}
