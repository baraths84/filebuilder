package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.CustomerUserRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.response.user.ProfileResponse;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.class)
public class CustomerUserServiceTest {
    private static final Long USER_ID = 12345L;
    private static final String USER_GUID = "userGuidValue";
    private static final String USER_GUID_2 = "userGuidValue2";
    private static final String USER_FIRST_NAME = "TestQA";
    private static final String USER_LAST_NAME = "LastQA";
    private static final String USER_STATE = "CA";
    private static final String USER_EMAIL = "user@test.com";
    private static final String USER_RESPONSE_JSON_FILE = "com/macys/selection/xapi/list/client/response/user_response.json";
    private static final String USER_LOGIN_CREDETIALS_RESPONSE_JSON_FILE = "com/macys/selection/xapi/list/client/response/user_login_credetials_response.json";
    private static final String USERS_RESPONSE_JSON_FILE = "com/macys/selection/xapi/list/client/response/users_response.json";
    private static final String USERS_BY_GUID_RESPONSE_JSON_FILE = "com/macys/selection/xapi/list/client/response/users_by_guid_response.json";
    private static final String ERRORS = "errors";
    private static final String TOKEN = "token";
    private static final int LIMIT = 100;

    private CustomerUserService customerUserService;

    @Mock
    private CustomerUserRestClient userRestClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        customerUserService = new CustomerUserService(userRestClient);
    }

    @Test
    public void testPopulateUserQueryParam() {
        UserResponse userResponse = new UserResponse();
        userResponse.setGuid(USER_GUID);
        userResponse.setId(USER_ID);
        userResponse.setGuestUser(true);
        UserQueryParam userQueryParam = new UserQueryParam();
        customerUserService.populateUserQueryParam(userQueryParam, userResponse, null);
        Assert.assertEquals(USER_GUID, userQueryParam.getUserGuid());
        Assert.assertEquals(USER_ID, userQueryParam.getUserId());
        Assert.assertTrue(userQueryParam.getGuestUser());
    }

    @Test
    public void shouldNotPopulateUserQueryParamIfResponseIsNull() {
        UserQueryParam userQueryParam = new UserQueryParam();
        customerUserService.populateUserQueryParam(userQueryParam, null, null);
        Assert.assertNotNull(userQueryParam);
        Assert.assertNull(userQueryParam.getUserGuid());
        Assert.assertNull(userQueryParam.getUserId());
        Assert.assertNull(userQueryParam.getGuestUser());
    }

    @Test
    public void shouldPopulateGuestUserDefaultValueIfResponseIsNull() {
        UserQueryParam userQueryParam = new UserQueryParam();
        customerUserService.populateUserQueryParam(userQueryParam, null, false);
        Assert.assertNotNull(userQueryParam);
        Assert.assertNull(userQueryParam.getUserGuid());
        Assert.assertNull(userQueryParam.getUserId());
        Assert.assertFalse(userQueryParam.getGuestUser());
    }

    @Test
    public void testPopulateUserQueryParamWithFirstName() {
        UserResponse userResponse = new UserResponse();
        userResponse.setGuid(USER_GUID);
        userResponse.setId(USER_ID);
        userResponse.setGuestUser(true);
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setFirstName(USER_FIRST_NAME);
        userResponse.setProfile(profileResponse);
        UserQueryParam userQueryParam = new UserQueryParam();
        customerUserService.populateUserQueryParamWithFirstName(userQueryParam, userResponse);
        Assert.assertEquals(USER_GUID, userQueryParam.getUserGuid());
        Assert.assertEquals(USER_ID, userQueryParam.getUserId());
        Assert.assertTrue(userQueryParam.getGuestUser());
        Assert.assertEquals(USER_FIRST_NAME, userQueryParam.getFirstName());
    }

    @Test
    public void shouldNotPopulateUserQueryParamWithFirstNameIfResponseIsNull() {
        UserQueryParam userQueryParam = new UserQueryParam();
        customerUserService.populateUserQueryParamWithFirstName(userQueryParam, null);
        Assert.assertNotNull(userQueryParam);
        Assert.assertNull(userQueryParam.getUserGuid());
        Assert.assertNull(userQueryParam.getUserId());
        Assert.assertNull(userQueryParam.getGuestUser());
        Assert.assertNull(userQueryParam.getFirstName());
    }

    @Test
    public void shouldNotPopulateFirstNameQueryParamIfProfileIsNull() {
        UserQueryParam userQueryParam = new UserQueryParam();
        customerUserService.populateUserQueryParamWithFirstName(userQueryParam, new UserResponse());
        Assert.assertNotNull(userQueryParam);
        Assert.assertNull(userQueryParam.getFirstName());
    }

    @Test
    public void testRetrieveUserGuestInfo() throws IOException {
        String userResponseJson = TestUtils.readFile(USER_RESPONSE_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(userResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        Mockito.when(userRestClient.retrieveUser(any(), any())).thenReturn(restResponse);
        Boolean isGuestUser = customerUserService.retrieveUserGuestInfo(USER_ID, USER_GUID, false);
        Mockito.verify(userRestClient).retrieveUser(USER_ID, USER_GUID);
        Assert.assertFalse(isGuestUser);
    }

    @Test
    public void testRetrieveUserGuestInfoIfUserResponseNull() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(ERRORS);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.when(userRestClient.retrieveUser(any(), any())).thenReturn(restResponse);
        Boolean isGuestUser = customerUserService.retrieveUserGuestInfo(USER_ID, USER_GUID, false);
        Mockito.verify(userRestClient).retrieveUser(USER_ID, USER_GUID);
        Assert.assertNull(isGuestUser);
    }

    @Test
    public void testRetrieveUser() throws IOException {
        String userResponseJson = TestUtils.readFile(USER_RESPONSE_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(userResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        Mockito.when(userRestClient.retrieveUser(any(), any())).thenReturn(restResponse);
        UserResponse userResponse = customerUserService.retrieveUser(USER_ID, USER_GUID, false);
        Mockito.verify(userRestClient).retrieveUser(USER_ID, USER_GUID);
        Assert.assertNotNull(userResponse);
        Assert.assertEquals(USER_ID, userResponse.getId());
        Assert.assertEquals(USER_GUID, userResponse.getGuid());
        Assert.assertFalse(userResponse.isGuestUser());
        Assert.assertNotNull(userResponse.getProfile());
        Assert.assertEquals(USER_FIRST_NAME, userResponse.getProfile().getFirstName());
    }

    @Test(expected = ListServiceException.class)
    public void testRetrieveUserNoUserInput() {
        try {
            customerUserService.retrieveUser(null, null, false);
            Mockito.verify(userRestClient, never()).retrieveUser(any(), any());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.NO_USER_INFO.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testRetrieveUserWhoNotExistsUserNotRequired() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(ERRORS);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.when(userRestClient.retrieveUser(any(), any())).thenReturn(restResponse);
        UserResponse userResponse = customerUserService.retrieveUser(USER_ID, USER_GUID, false);
        Mockito.verify(userRestClient).retrieveUser(USER_ID, USER_GUID);
        Assert.assertNull(userResponse);
    }

    @Test(expected = ListServiceException.class)
    public void testRetrieveUserWhoNotExistsUserIsRequired() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(ERRORS);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.when(userRestClient.retrieveUser(any(), any())).thenReturn(restResponse);
        try {
            customerUserService.retrieveUser(USER_ID, USER_GUID, true);
            Mockito.verify(userRestClient).retrieveUser(USER_ID, USER_GUID);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test(expected = ListServiceException.class)
    public void testRetrieveUserInvalidResponseStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(ERRORS);
        restResponse.setStatusCode(Response.Status.BAD_GATEWAY.getStatusCode());
        Mockito.when(userRestClient.retrieveUser(any(), any())).thenReturn(restResponse);
        try {
            customerUserService.retrieveUser(USER_ID, USER_GUID, false);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_GATEWAY.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ERRORS, e.getServiceError());
            throw e;
        }
    }

    @Test(expected = ListServiceException.class)
    public void testRetrieveUserRestException() {
        Mockito.when(userRestClient.retrieveUser(any(), any())).thenThrow(new RestException(ERRORS));
        customerUserService.retrieveUser(USER_ID, USER_GUID, false);
    }

    @Test
    public void testRetrieveProfileInfo() throws IOException {
        String userResponseJson = TestUtils.readFile(USER_LOGIN_CREDETIALS_RESPONSE_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        restResponse.setBody(userResponseJson);
        Mockito.when(userRestClient.retrieveUserProfile(any(), any())).thenReturn(restResponse);
        UserResponse userResponse = customerUserService.retrieveUserProfile(TOKEN, USER_ID);
        Mockito.verify(userRestClient).retrieveUserProfile(TOKEN, USER_ID);
        Assert.assertNotNull(userResponse);
        Assert.assertEquals(USER_ID, userResponse.getId());
        Assert.assertEquals(USER_GUID, userResponse.getGuid());
        Assert.assertNotNull(userResponse.getLoginCredentials());
        Assert.assertEquals(USER_EMAIL, userResponse.getLoginCredentials().getUserName());
        Assert.assertFalse(userResponse.isGuestUser());
        Assert.assertNotNull(userResponse.getProfile());
        Assert.assertEquals(USER_FIRST_NAME, userResponse.getProfile().getFirstName());
    }

    @Test(expected = ListServiceException.class)
    public void testRetrieveProfileInfoNoUserInput() {
        try {
            customerUserService.retrieveUserProfile(TOKEN, null);
            Mockito.verify(userRestClient, never()).retrieveUserProfile(any(), any());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.NO_USER_INFO.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test(expected = ListServiceException.class)
    public void testRetrieveProfileInfoNoSecurityToken() {
        try {
            customerUserService.retrieveUserProfile(null, USER_ID);
            Mockito.verify(userRestClient, never()).retrieveUserProfile(any(), any());
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.NO_SECURE_TOKEN.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test(expected = ListServiceException.class)
    public void testValidateUserInfoSpecified() {
        try {
            customerUserService.validateEitherUserIdOrGuidSpecified(null, null);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.NO_USER_INFO.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testValidateUserInfoSpecifiedValidInput() {
        try {
            customerUserService.validateEitherUserIdOrGuidSpecified(USER_ID, null);
        } catch (ListServiceException e) {
            Assert.fail();
        }
    }

    @Test(expected = ListServiceException.class)
    public void testValidateUserInfoSpecifiedZeroIdEmptyGuid() {
        try {
            customerUserService.validateEitherUserIdOrGuidSpecified(0L, StringUtils.EMPTY);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.NO_USER_INFO.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testvalidateNotBothUserIdAndGuidSpecifiedValidInput() {
        try {
            customerUserService.validateNotBothUserIdAndGuidSpecified(USER_ID, null);
        } catch (ListServiceException e) {
            Assert.fail();
        }
    }

    @Test(expected = ListServiceException.class)
    public void testvalidateNotBothUserIdAndGuidSpecified() {
        try {
            customerUserService.validateNotBothUserIdAndGuidSpecified(USER_ID, USER_GUID);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_INPUT.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testCreateGuestUser() {
        RestResponse userIdRestResponse = new RestResponse();
        userIdRestResponse.setBody(USER_ID.toString());
        userIdRestResponse.setStatusCode(Response.Status.OK.getStatusCode());
        RestResponse userGuIdRestResponse = new RestResponse();
        userGuIdRestResponse.setBody(USER_GUID);
        userGuIdRestResponse.setStatusCode(Response.Status.OK.getStatusCode());
        Mockito.when(userRestClient.createUser(null)).thenReturn(userIdRestResponse);
        Mockito.when(userRestClient.retrieveUserGUID(USER_ID)).thenReturn(userGuIdRestResponse);
        UserResponse createdUser = customerUserService.createGuestUser();
        Assert.assertNotNull(createdUser);
        Assert.assertEquals(USER_ID, createdUser.getId());
        Assert.assertEquals(USER_GUID, createdUser.getGuid());
        Assert.assertTrue(createdUser.isGuestUser());
    }

    @Test
    public void testCreateGuestUserWithDefinedUserId() {
        RestResponse userIdRestResponse = new RestResponse();
        userIdRestResponse.setBody(USER_ID.toString());
        userIdRestResponse.setStatusCode(Response.Status.OK.getStatusCode());
        RestResponse userGuIdRestResponse = new RestResponse();
        userGuIdRestResponse.setBody(USER_GUID);
        userGuIdRestResponse.setStatusCode(Response.Status.OK.getStatusCode());
        Mockito.when(userRestClient.createUser(USER_ID)).thenReturn(userIdRestResponse);
        Mockito.when(userRestClient.retrieveUserGUID(USER_ID)).thenReturn(userGuIdRestResponse);
        UserResponse createdUser = customerUserService.createGuestUser(USER_ID);
        Assert.assertNotNull(createdUser);
        Assert.assertEquals(USER_ID, createdUser.getId());
        Assert.assertEquals(USER_GUID, createdUser.getGuid());
        Assert.assertTrue(createdUser.isGuestUser());
    }

    @Test(expected = ListServiceException.class)
    public void testCreateGuestUserInvalidResponseStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(ERRORS);
        restResponse.setStatusCode(Response.Status.BAD_GATEWAY.getStatusCode());
        Mockito.when(userRestClient.createUser(any())).thenReturn(restResponse);
        try {
            customerUserService.createGuestUser(USER_ID);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_GATEWAY.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ERRORS, e.getServiceError());
            throw e;
        }
    }

    @Test(expected = ListServiceException.class)
    public void testCreateUserGuidInvalidResponseStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(ERRORS);
        restResponse.setStatusCode(Response.Status.BAD_GATEWAY.getStatusCode());
        Mockito.when(userRestClient.retrieveUserGUID(any())).thenReturn(restResponse);
        try {
            customerUserService.createGuestUser(USER_ID);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_GATEWAY.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ERRORS, e.getServiceError());
            throw e;
        }
    }

    @Test(expected = ListServiceException.class)
    public void testCreateGuestUserRestException() {
        Mockito.when(userRestClient.createUser(USER_ID)).thenThrow(new RestException(ERRORS));
        customerUserService.createGuestUser(USER_ID);
    }

    @Test
    public void testFindUsers() throws IOException {
        String usersListResponseJson = TestUtils.readFile(USERS_RESPONSE_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(usersListResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        Mockito.when(userRestClient.findUsers(any(), any(), any(), any())).thenReturn(restResponse);
        List<UserResponse> users = customerUserService.findUsers(USER_FIRST_NAME, USER_LAST_NAME, USER_STATE, LIMIT);
        Mockito.verify(userRestClient).findUsers(USER_FIRST_NAME, USER_LAST_NAME, USER_STATE, LIMIT);
        Assert.assertEquals(2, users.size());
        Assert.assertEquals((Long) 2150919659L, users.get(0).getId());
        Assert.assertEquals((Long) 2150919662L, users.get(1).getId());
        Assert.assertEquals(USER_FIRST_NAME, users.get(0).getProfile().getFirstName());
        Assert.assertEquals(USER_FIRST_NAME, users.get(1).getProfile().getFirstName());
        Assert.assertEquals(USER_LAST_NAME, users.get(0).getProfile().getLastName());
        Assert.assertEquals(USER_LAST_NAME, users.get(1).getProfile().getLastName());
        Assert.assertEquals(USER_STATE, users.get(0).getProfile().getState());
        Assert.assertEquals(USER_STATE, users.get(1).getProfile().getState());
    }

    @Test(expected = ListServiceException.class)
    public void testFindUsersInvalidResponseStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(ERRORS);
        restResponse.setStatusCode(Response.Status.BAD_GATEWAY.getStatusCode());
        Mockito.when(userRestClient.findUsers(any(), any(), any(), any())).thenReturn(restResponse);
        try {
            customerUserService.findUsers(USER_FIRST_NAME, USER_LAST_NAME, USER_STATE, LIMIT);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_GATEWAY.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ERRORS, e.getServiceError());
            throw e;
        }
    }

    @Test(expected = ListServiceException.class)
    public void testFindUsersRestException() {
        Mockito.when(userRestClient.findUsers(any(), any(), any(), any())).thenThrow(new RestException(ERRORS));
        customerUserService.findUsers(USER_FIRST_NAME, USER_LAST_NAME, USER_STATE, LIMIT);
    }

    @Test
    public void testRetrieveUsersByGuids() throws IOException {
        String usersListResponseJson = TestUtils.readFile(USERS_BY_GUID_RESPONSE_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(usersListResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());

        String guid1 = "guid1";
        String guid2 = "guid2";
        List<String> userGuids = Arrays.asList(guid1, guid2);

        Mockito.when(userRestClient.retrieveUsersByGuids(userGuids)).thenReturn(restResponse);

        List<UserResponse> users = customerUserService.retrieveUsersByGuids(userGuids);

        Mockito.verify(userRestClient).retrieveUsersByGuids(userGuids);
        Assert.assertEquals(2, users.size());
        Assert.assertEquals(guid1, users.get(0).getGuid());
        Assert.assertEquals(guid2, users.get(1).getGuid());
        Assert.assertEquals(USER_FIRST_NAME, users.get(0).getProfile().getFirstName());
        Assert.assertEquals(USER_FIRST_NAME, users.get(1).getProfile().getFirstName());
        Assert.assertEquals(USER_LAST_NAME, users.get(0).getProfile().getLastName());
        Assert.assertEquals(USER_LAST_NAME, users.get(1).getProfile().getLastName());
        Assert.assertEquals(USER_STATE, users.get(0).getProfile().getState());
        Assert.assertEquals(USER_STATE, users.get(1).getProfile().getState());
    }

    @Test(expected = ListServiceException.class)
    public void testRetrieveUsersByIdsInvalidResponseStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(ERRORS);
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        List<String> userIds = Arrays.asList(USER_GUID);
        Mockito.when(userRestClient.retrieveUsersByGuids(userIds)).thenReturn(restResponse);
        try {
            customerUserService.retrieveUsersByGuids(userIds);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ERRORS, e.getServiceError());
            throw e;
        }
    }

    @Test(expected = ListServiceException.class)
    public void testRetrieveUsersByIdsRestException() {
        Mockito.when(userRestClient.retrieveUsersByGuids(anyList())).thenThrow(new RestException(ERRORS));
        customerUserService.retrieveUsersByGuids(Arrays.asList(USER_GUID));
    }
}
