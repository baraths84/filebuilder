package com.macys.selection.xapi.list.client;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxClient;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvoker;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.platform.rest.framework.client.exception.RestClientException;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
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
import javax.ws.rs.core.Response;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class DeleteFavoriteRestClientTest {

    private static String LOCAL_HOST = "http://localhost:8080";
    private static String BASH_PATH = "/xapi/v1/lists/";
    private static String ABC_LIST_GUID = "abc";
    private static final int TIME_1 = 1;
    private static String GET_CLIENT_EXP = "exception while getting clinet:";
    private static final int PRODUCT_ID = 86800;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerListRestClientTest.class);

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
    private ListQueryParam listQueryParam;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
        invocationBuilder = Mockito.mock(RxInvocationBuilder.class);
        listQueryParam = getListQueryParam();

        when(listClientPool.getHostName()).thenReturn(LOCAL_HOST);
        when(listClientPool.getBasePath()).thenReturn(BASH_PATH);
        try {
            when(listClientPool.getRxClient((String) any())).thenReturn(rxClient);
        } catch (RestClientException e) {
            LOGGER.error(GET_CLIENT_EXP, e);
        }

        when(rxClient.target((String) any())).thenReturn(rxWebTarget);
        when(rxWebTarget.path((String) any())).thenReturn(rxWebTarget);

    }

    private ListQueryParam getListQueryParam() {
        ListQueryParam sampleListQueryParam = new ListQueryParam();
        sampleListQueryParam.setProductId(PRODUCT_ID);
        return sampleListQueryParam;
    }

    @Test
    public void testDeleteFavorite() {
        Response response = Mockito.mock(Response.class);

        ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");

        when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, cookie.getToken())).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.NO_CONTENT.getStatusCode());
        when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);

        listRestClient.deleteFavorite(cookie, ABC_LIST_GUID, listQueryParam);
        verify(rxClient, times(TIME_1)).target((String) any());
        verify(rxWebTarget, times(TIME_1)).request();
    }

    @Test
    public void testDeleteFavoriteWithIncorrectRequest() {
        Response response = Mockito.mock(Response.class);

        ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");
        when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, cookie.getToken())).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);

        listRestClient.deleteFavorite(cookie, ABC_LIST_GUID, listQueryParam);
        verify(rxClient, times(TIME_1)).target((String) any());
        verify(rxWebTarget, times(TIME_1)).request();
    }

    @Test(expectedExceptions = RestException.class)
    public void testDeleteFavoriteException() {

        ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");

        when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.request()).thenReturn(invocationBuilder);
        when(invocationBuilder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, cookie.getToken())).thenReturn(invocationBuilder);
        when(invocationBuilder.delete()).thenThrow(new ArrayIndexOutOfBoundsException());
        when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);

        listRestClient.deleteFavorite(cookie, ABC_LIST_GUID, listQueryParam);
        verify(rxClient, times(TIME_1)).target((String) any());
        verify(rxWebTarget, times(TIME_1)).request();
    }

}
