package com.macys.selection.xapi.list.client;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxClient;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvoker;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.platform.rest.framework.client.exception.RestClientException;
import com.macys.selection.xapi.list.TestUtils;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.util.CustomerRequestParamUtil;
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
import java.io.IOException;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by m940030 on 11/7/17.
 */
public class GetFavoriteRestClientTest {

    private static String FAILED = "Failed";
    private static String FAV_LIST_JSON_FILE = "com/macys/selection/xapi/wishlist/converters/favList.json";
    private static final int TIME_1 = 1;
    private static String GET_CLIENT_EXP = "exception while getting clinet:";
    private static String SPEFIC_USER_GUID = "ddef96d4-d85e-48ed-bf99-686e6dfb1b72";
    private static final Logger LOGGER = LoggerFactory.getLogger(GetFavoriteRestClientTest.class);
    private static String LOCAL_HOST = "http://localhost:8080";


    @Mock
    private RestClientFactory.JaxRSClientPool listClientPool;

    @Mock
    private RxClient rxClient;
    @Mock
    private RxWebTarget rxWebTarget;

    @Mock
    private RxInvoker invoker;

    @Mock
    private CustomerRequestParamUtil requestParamUtil;

    @InjectMocks
    private CustomerServiceRestClient listRestClient;
    private RxInvocationBuilder invocationBuilder;


    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
        invocationBuilder = Mockito.mock(RxInvocationBuilder.class);

        when(listClientPool.getHostName()).thenReturn(LOCAL_HOST);
        try {
            when(listClientPool.getRxClient(any())).thenReturn(rxClient);
        } catch (RestClientException e) {
            LOGGER.error(GET_CLIENT_EXP, e);
        }

        when(rxClient.target((String) any())).thenReturn(rxWebTarget);
        when(rxWebTarget.path(any())).thenReturn(rxWebTarget);

    }


    @Test
    public void getFavItemFromListByGuidTest() throws IOException {
        Response response = Mockito.mock(Response.class);

        ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");

        when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
        when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, cookie.getToken())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        String responseBodyString = TestUtils.readFile(FAV_LIST_JSON_FILE);
        when(response.readEntity(String.class)).thenReturn(responseBodyString);
        RestResponse restResponse = listRestClient.getFavItemFromListByGuid(cookie, SPEFIC_USER_GUID);
        assertNotNull(restResponse);
        verify(rxWebTarget, times(TIME_1)).request(MediaType.APPLICATION_JSON_TYPE);
    }


    @Test
    public void getFavItemFromListByGuidOn400ErrorTest() {

        Response response = Mockito.mock(Response.class);
        ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");

        when(rxWebTarget.path(any())).thenReturn(rxWebTarget);
        when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, cookie.getToken())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(FAILED);
        RestResponse restResponse = listRestClient.getFavItemFromListByGuid(cookie, SPEFIC_USER_GUID);
        assertNotNull(restResponse);
        assertTrue(restResponse.getStatusCode() == Response.Status.BAD_REQUEST.getStatusCode());
        assertTrue(FAILED.equals(restResponse.getBody()));
    }

    @Test(expectedExceptions = RestException.class)
    public void getFavItemFromListByGuidOnExceptionTest() {

        RestException exception = new RestException(FAILED, new NullPointerException());
        ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");

        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, cookie.getToken())).thenReturn(invocationBuilder);
        when(invocationBuilder.get()).thenThrow(exception);
        listRestClient.getFavItemFromListByGuid(cookie, SPEFIC_USER_GUID);
    }


}
