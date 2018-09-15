package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.CustomerServiceRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.FavoriteList;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by m940030 on 11/14/17.
 */
@SpringBootTest
public class CustomerServiceGetFavTest extends AbstractTestNGSpringContextTests {
    private static String SUCCESS_RESPONSE_FILE = "com/macys/selection/xapi/wishlist/converters/Success_response_on_get_fav.json";
    private static String ERROR_FAVLIST_FILE = "com/macys/selection/xapi/wishlist/converters/Error_FavList.json";
    private static String REST_EXCEP = "RestException from the RestClient";
    private static String SPECIFICAL_LIST_GUID = "164e9807-a337-4d6c-afa3-aa4ce08f6113";
    private static String SPEFIC_USER_GUID = "df0637cb-bc0d-4f84-93a5-55b649e5f73d";
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceGetFavTest.class);
    private static String GET_CLIENT_EXP = "exception while getting clinet:";
    private static final String SECURITY_TOKEN = "xxx-token";
    private static final String LIST_GUID = "xxx-guid";
    private static final Long USER_ID_COOKIES = 10L;


    @Mock
    private CustomerServiceRestClient restClient;

    @InjectMocks
    private CustomerService customerService;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    	JsonToObjectConverter<FavoriteList> favoriteListConverter = new JsonToObjectConverter<>(FavoriteList.class);
    	customerService.setFavoriteListConverter(favoriteListConverter);
    }

    @Test
    public void getFavItemFromListByGuidTest() throws IOException, ParseException {
        String successFavList = TestUtils.readFile(SUCCESS_RESPONSE_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(successFavList);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(restClient.getFavItemFromListByGuid(any(), any())).thenReturn(restResponse);

        ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
        FavoriteList customerFavList = customerService.getFavItemFromListByGuid(cookie, SPEFIC_USER_GUID);
        assertNotNull(customerFavList);
        assertTrue(SPECIFICAL_LIST_GUID.equals(customerFavList.getListGuid()));


    }


    @Test
    public void failedgetFavItemFromListByGuidTest() throws IOException {


        String mspCustomerResponseOnError = TestUtils.readFile(ERROR_FAVLIST_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponseOnError);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());

        when(restClient.getFavItemFromListByGuid(any(), any())).thenReturn(restResponse);
        try {
            ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
            customerService.getFavItemFromListByGuid(cookie, SPEFIC_USER_GUID);
        } catch (ListServiceException e) {
            LOGGER.error(GET_CLIENT_EXP, e);
            assertTrue(mspCustomerResponseOnError.equals(e.getServiceError()));
            assertTrue(e.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
        }
    }

    @Test
    public void testgetFavItemFromListByGuidOnNullCustomerList() throws IOException {
        ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
        customerService.getFavItemFromListByGuid(cookie,null);
    }


    @Test
    public void getFavItemFromListByGuidResponseNullTest() throws IOException {
        when(restClient.getFavItemFromListByGuid(any(), any())).thenReturn(null);
        ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
        customerService.getFavItemFromListByGuid(cookie, SPEFIC_USER_GUID);
    }


    @Test(expectedExceptions = ListServiceException.class)
    public void restExcpOnGetFavItemFromListByGuidTest() throws IOException {
        RestException restException = new RestException(REST_EXCEP);
        when(restClient.getFavItemFromListByGuid(any(), any())).thenThrow(restException);
        ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
        customerService.getFavItemFromListByGuid(cookie, SPEFIC_USER_GUID);
    }


}
