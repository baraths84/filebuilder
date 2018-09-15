package com.macys.selection.xapi.list.client;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxClient;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.platform.rest.framework.client.exception.RestClientException;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.WishList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by m940030 on 11/7/17.
 */
public class AddFavoriteRestClientTest {

    private static String SUCCESS = "Success";
    private static String FAILED = "Failed";
    private static final int QTY = 1;
    private static final int TEST_PRODUCT_ID = 22805;
    private static final long TEST_LONG = 3213253243L;
    private static String GET_CLIENT_EXP = "exception while getting clinet:";
    private static String SPEFIC_USER_GUID = "df0637cb-bc0d-4f84-93a5-55b649e5f73d";
    private static final Logger LOGGER = LoggerFactory.getLogger(AddFavoriteRestClientTest.class);
    private static String LOCAL_HOST = "http://localhost:8080";
    private static final String SECURITY_TOKEN = "xxx-token";
    private static final String LIST_GUID = "xxx-guid";
    private static final Long USER_ID = 10L;

    @Mock
    private RestClientFactory.JaxRSClientPool listClientPool;

    @Mock
    private RxClient rxClient;

    @Mock
    private RxWebTarget rxWebTarget;

    @Mock
    private RxInvocationBuilder invocationBuilder;


    @InjectMocks
    private CustomerServiceRestClient listRestClient;


    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(listClientPool.getHostName()).thenReturn(LOCAL_HOST);
        try {
            when(listClientPool.getRxClient((String) any())).thenReturn(rxClient);
        } catch (RestClientException e) {
            LOGGER.error(GET_CLIENT_EXP, e);
        }

        when(rxClient.target((String) any())).thenReturn(rxWebTarget);
        when(rxWebTarget.path((String) any())).thenReturn(rxWebTarget);

    }


    @Test
    public void addFavItemToGivenListByPIDTest() {

        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        User user = new User();
        user.setId(TEST_LONG);
        user.setGuid(SPEFIC_USER_GUID);
        List<Item> itemList = new ArrayList<Item>();
        Item item = new Item();
        item.setQtyRequested(QTY);
        Product prod = new Product();
        prod.setId(TEST_PRODUCT_ID);
        item.setProduct(prod);
        itemList.add(item);
        wishList.setItems(itemList);
        customerList.setUser(user);
        List<WishList> wishLists = new ArrayList<WishList>();
        wishLists.add(wishList);
        customerList.setWishlist(wishLists);
        customerList.setUser(user);

        Response response = Mockito.mock(Response.class);
        ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);

        when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
        when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        when(invocationBuilder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, cookie.getToken())).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.CREATED.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(SUCCESS);

        RestResponse restResponse = listRestClient.addFavItemToGivenListByPID(cookie, customerList);
        assertNotNull(restResponse);
        assertTrue(restResponse.getStatusCode() == Response.Status.CREATED.getStatusCode());
        assertTrue(SUCCESS.equals(restResponse.getBody()));
        customerList.setUser(null);
        RestResponse restResponse2 = listRestClient.addFavItemToGivenListByPID(cookie, customerList);
        assertNotNull(restResponse2);
        assertTrue(restResponse2.getStatusCode() == Response.Status.CREATED.getStatusCode());
        customerList.setUser(user);
        customerList.getUser().setGuid(null);

        RestResponse restResponse3 = listRestClient.addFavItemToGivenListByPID(cookie, customerList);
        assertNotNull(restResponse3);
        assertTrue(restResponse3.getStatusCode() == Response.Status.CREATED.getStatusCode());

    }


    @Test
    public void addFavItemToGivenListByPIDOn400ErrorTest() {

        Response response = Mockito.mock(Response.class);
        ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);

        when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
        when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, cookie.getToken())).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(FAILED);
        CustomerList inputCustomerList = new CustomerList();
        RestResponse restResponse = listRestClient.addFavItemToGivenListByPID(cookie, inputCustomerList);
        assertNotNull(restResponse);
        assertTrue(restResponse.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
        assertTrue(FAILED.equals(restResponse.getBody()));

    }

    @Test(expectedExceptions = RestException.class)
    public void addFavItemToGivenListByPIDOnExceptionTest() {

        ListCookies cookie = new ListCookies(USER_ID, LIST_GUID, SECURITY_TOKEN);
        RestException exception = new RestException(FAILED, new NullPointerException());

        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, cookie.getToken())).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any())).thenThrow(exception);
        CustomerList inputCustomerList = new CustomerList();

        listRestClient.addFavItemToGivenListByPID(cookie, inputCustomerList);
    }


}
