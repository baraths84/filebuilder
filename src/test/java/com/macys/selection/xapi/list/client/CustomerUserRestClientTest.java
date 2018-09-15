package com.macys.selection.xapi.list.client;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxClient;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocation;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.platform.rest.framework.client.exception.RestClientException;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.util.CustomerQueryParameterEnum;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerUserRestClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerUserRestClientTest.class);
    private static final String HOST_NAME = "http://localhost:8080";
    private static final String USER_V1_BASE_PATH = "/api/customer/v1/profile";
    private static final String USER_V2_BASE_PATH = "/api/customer/v2/users";
    private static final Long USER_ID = 1234L;
    private static final String USER_GUID = "userGuid";
    private static final String USER_GUID_2 = "userGuid2";
    private static final String FIRST_NAME = "firstNameValue";
    private static final String LAST_NAME = "lastNameValue";
    private static final String STATE = "state";
    private static final String ERRORS = "errors";
    private static final String TOKEN = "token";
    private static final int LIMIT = 100;

    private CustomerUserRestClient customerUserRestClient;

    @Mock
    private RestClientFactory.JaxRSClientPool userProfileV1ClientPool;

    @Mock
    private RestClientFactory.JaxRSClientPool userProfileV2ClientPool;

    @Mock
    private RxClient rxClient;

    @Mock
    private RxWebTarget rxWebTarget;

    @Mock
    private RxInvocationBuilder invocationBuilder;

    @Mock
    private RxInvocation rxInvocation;

    @BeforeMethod
    public void init() {

        MockitoAnnotations.initMocks(this);

        customerUserRestClient = Mockito.spy(new CustomerUserRestClient(userProfileV1ClientPool, userProfileV2ClientPool));

        try {
            when(userProfileV1ClientPool.getRxClient(WishlistConstants.CUSTOMER_USER_PROFILE_V1_CLIENT_POOL)).thenReturn(rxClient);
            when(userProfileV2ClientPool.getRxClient(WishlistConstants.CUSTOMER_USER_PROFILE_V2_CLIENT_POOL)).thenReturn(rxClient);
        } catch (RestClientException e) {
            LOGGER.error("Exception while getting clinet:", e);
        }

        when(userProfileV1ClientPool.getHostName()).thenReturn(HOST_NAME);
        when(userProfileV1ClientPool.getBasePath()).thenReturn(USER_V1_BASE_PATH);

        when(userProfileV2ClientPool.getHostName()).thenReturn(HOST_NAME);
        when(userProfileV2ClientPool.getBasePath()).thenReturn(USER_V2_BASE_PATH);

        when(rxWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(rxWebTarget);

        when(rxClient.target(anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
    }

    @Test
    public void testRetrieveUser() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        RestResponse restResponse = customerUserRestClient.retrieveUser(USER_ID, USER_GUID);
        verify(rxClient).target(userProfileV1ClientPool.getHostName());
        verify(rxWebTarget).path(userProfileV1ClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.RETRIEVE_USER_PATH);
        verify(rxWebTarget).queryParam(WishlistConstants.USER_ID, USER_ID);
        verify(rxWebTarget).queryParam(WishlistConstants.USER_GUID.toLowerCase(), USER_GUID);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testRetrieveUserErrorCode() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(ERRORS);
        RestResponse restResponse = customerUserRestClient.retrieveUser(USER_ID, USER_GUID);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        Assert.assertEquals(ERRORS, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testRetrieveUserException() {
        when(rxWebTarget.request((MediaType) any())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenThrow(new RuntimeException());
        customerUserRestClient.retrieveUser(USER_ID, USER_GUID);
    }

    @Test
    public void testRetrieveUserProfile() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        RestResponse restResponse = customerUserRestClient.retrieveUserProfile(TOKEN, USER_ID);
        verify(rxClient).target(userProfileV1ClientPool.getHostName());
        verify(rxWebTarget).path(userProfileV1ClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.RETRIEVE_USER_PROFILE_PATH);
        verify(rxWebTarget).resolveTemplate("userId", USER_ID);
        verify(invocationBuilder).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, TOKEN);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testRetrieveUserProfileErrorCode() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(ERRORS);
        RestResponse restResponse = customerUserRestClient.retrieveUserProfile(TOKEN, USER_ID);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        Assert.assertEquals(ERRORS, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testRetrieveUserProfileException() {
        when(rxWebTarget.request((MediaType) any())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenThrow(new RuntimeException());
        customerUserRestClient.retrieveUserProfile(TOKEN, USER_ID);
    }

    @Test
    public void testCreateUser() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        RestResponse restResponse = customerUserRestClient.createUser(USER_ID);
        verify(rxClient).target(userProfileV1ClientPool.getHostName());
        verify(rxWebTarget).path(userProfileV1ClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.CREATE_USER_PATH);
        verify(rxWebTarget).queryParam(WishlistConstants.USER_ID, USER_ID);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testCreateUserErrorCode() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(ERRORS);
        RestResponse restResponse = customerUserRestClient.createUser(USER_ID);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        Assert.assertEquals(ERRORS, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testCreateUserException() {
        when(rxWebTarget.request((MediaType) any())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenThrow(new RuntimeException());
        customerUserRestClient.createUser(USER_ID);
    }

    @Test
    public void testRetrieveUserGUID() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        RestResponse restResponse = customerUserRestClient.retrieveUserGUID(USER_ID);
        verify(rxClient).target(userProfileV1ClientPool.getHostName());
        verify(rxWebTarget).path(userProfileV1ClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.RETRIEVE_USER_GUID_PATH);
        verify(rxWebTarget).resolveTemplate(WishlistConstants.USER_ID, USER_ID);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testRetrieveUserGUIDErrorCode() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(ERRORS);
        RestResponse restResponse = customerUserRestClient.retrieveUserGUID(USER_ID);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        Assert.assertEquals(ERRORS, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testRetrieveUserGUIDException() {
        when(rxWebTarget.request((MediaType) any())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenThrow(new RuntimeException());
        customerUserRestClient.retrieveUserGUID(USER_ID);
    }

    @Test
    public void testFindUsers() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        RestResponse restResponse = customerUserRestClient.findUsers(FIRST_NAME, LAST_NAME, STATE, LIMIT);
        verify(rxClient).target(userProfileV2ClientPool.getHostName());
        verify(rxWebTarget).path(userProfileV2ClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.FIND_USERS_PATH);
        verify(rxWebTarget).queryParam(WishlistConstants.FIRST_NAME, FIRST_NAME);
        verify(rxWebTarget).queryParam(WishlistConstants.LAST_NAME, LAST_NAME);
        verify(rxWebTarget).queryParam(WishlistConstants.STATE, STATE);
        verify(rxWebTarget).queryParam(CustomerQueryParameterEnum.LIMIT.getParamName(), LIMIT);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testFindUsersErrorCode() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(ERRORS);
        RestResponse restResponse = customerUserRestClient.findUsers(FIRST_NAME, LAST_NAME, STATE, LIMIT);
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        Assert.assertEquals(ERRORS, restResponse.getBody());
    }

    @Test(expectedExceptions = RestException.class)
    public void testFindUsersException() {
        when(rxWebTarget.request((MediaType) any())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenThrow(new RuntimeException());
        customerUserRestClient.findUsers(FIRST_NAME, LAST_NAME, STATE, LIMIT);
    }

    @Test
    public void testRetrieveUsersByIds() throws RestClientException {
        Response response = Mockito.mock(Response.class);

        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        RestResponse restResponse = customerUserRestClient.retrieveUsersByGuids(Arrays.asList(USER_GUID, USER_GUID_2));

        verify(userProfileV2ClientPool).getRxClient(WishlistConstants.CUSTOMER_USER_PROFILE_V2_CLIENT_POOL);
        verify(rxClient).target(userProfileV2ClientPool.getHostName());
        verify(rxWebTarget).path(userProfileV2ClientPool.getBasePath());
        verify(rxWebTarget).path(WishlistConstants.RETRIEVE_USERS_BY_IDS_PATH);
        verify(rxWebTarget).queryParam(WishlistConstants.USER_GUIDS, USER_GUID + "," + USER_GUID_2);
        verify(response).close();
        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testRetrieveUsersByIdsErrorCode() {
        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
        when(response.readEntity(String.class)).thenReturn(ERRORS);

        RestResponse restResponse = customerUserRestClient.retrieveUsersByGuids(Arrays.asList(USER_GUID));

        Assert.assertNotNull(restResponse);
        Assert.assertEquals(restResponse.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        Assert.assertEquals(ERRORS, restResponse.getBody());
        verify(response).close();
    }

    @Test(expectedExceptions = RestException.class)
    public void testRetrieveUsersByIdsException() {
        when(rxWebTarget.request((MediaType) any())).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenThrow(new RuntimeException());
        customerUserRestClient.retrieveUsersByGuids(Arrays.asList(USER_GUID));
    }
}
