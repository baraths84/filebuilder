package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.ListRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.response.UserAvatarDTO;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.request.UserAvatarRequest;
import com.macys.selection.xapi.list.rest.response.UserAvatar;
import ma.glasnost.orika.MapperFacade;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserAvatarServiceTest {

    private static final String SECURE_TOKEN = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";
    private static final String USER_GUID = "testUserGuid";
    private static final String AVATAR = "testAvatar";
    private static final String GET_AVATAR_RESPONSE = "{\"profilePictures\":{\"profilePicture\":[{\"userGuid\":\"testUserGuid\",\"avatar\":\"testAvatar\"}]}}";
    @Mock
    private ListRestClient listRestClient;
    @Mock
    private CustomerUserService customerUserService;
    @Mock
    private MapperFacade mapperFacade;

    private UserAvatarService userAvatarService;

    @Before
    public void init() {
        userAvatarService = new UserAvatarService(listRestClient, customerUserService, mapperFacade);

        UserResponse userResponse = new UserResponse();
        userResponse.setGuid(USER_GUID);
        Mockito.when(customerUserService.retrieveUser(Mockito.any(), Mockito.any())).thenReturn(userResponse);

    }

    @Test
    public void testDeleteAvatar() {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());
        Mockito.when(listRestClient.deleteUserAvatar(Mockito.any(), Mockito.any())).thenReturn(restResponse);

        userAvatarService.deleteAvatar(SECURE_TOKEN, USER_GUID);

        Mockito.verify(customerUserService).retrieveUser(null, USER_GUID);
        Mockito.verify(listRestClient).deleteUserAvatar(SECURE_TOKEN, USER_GUID);
    }

    @Test(expected = ListServiceException.class)
    public void testDeleteAvatarWithInvalidStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        restResponse.setBody("Some error");
        Mockito.when(listRestClient.deleteUserAvatar(Mockito.any(), Mockito.any())).thenReturn(restResponse);

        try {
            userAvatarService.deleteAvatar(SECURE_TOKEN, USER_GUID);
        } catch (ListServiceException e)  {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals("Some error", e.getServiceError());
            throw e;
        }
    }

    @Test
    public void testAddUserAvatar() {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());
        Mockito.when(listRestClient.addUserAvatar(Mockito.any(), Mockito.any())).thenReturn(restResponse);

        UserAvatarDTO userAvatarDTO = new UserAvatarDTO(USER_GUID, AVATAR);
        Mockito.when(mapperFacade.map(Mockito.any(), Mockito.any())).thenReturn(userAvatarDTO);


        UserAvatarRequest userAvatarRequest = new UserAvatarRequest(USER_GUID, AVATAR);
        userAvatarService.addUserAvatar(SECURE_TOKEN, userAvatarRequest);

        Mockito.verify(customerUserService).retrieveUser(null, USER_GUID);
        Mockito.verify(listRestClient).addUserAvatar(SECURE_TOKEN, userAvatarDTO);
    }

    @Test(expected = ListServiceException.class)
    public void testAddUserAvatarWithInvalidStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        restResponse.setBody("Some error");
        Mockito.when(listRestClient.addUserAvatar(Mockito.any(), Mockito.any())).thenReturn(restResponse);

        UserAvatarDTO userAvatarDTO = new UserAvatarDTO(USER_GUID, AVATAR);
        Mockito.when(mapperFacade.map(Mockito.any(), Mockito.any())).thenReturn(userAvatarDTO);
        try {
            UserAvatarRequest userAvatarRequest = new UserAvatarRequest(USER_GUID, AVATAR);
            userAvatarService.addUserAvatar(SECURE_TOKEN, userAvatarRequest);
        } catch (ListServiceException ex) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatusCode());
            Assert.assertEquals("Some error", ex.getServiceError());
            throw ex;
        }
    }

    @Test
    public void testGetUserAvatar() {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        restResponse.setBody(GET_AVATAR_RESPONSE);
        Mockito.when(listRestClient.getUserAvatars(Mockito.any())).thenReturn(restResponse);

        UserAvatar userAvatar = new UserAvatar(AVATAR);

        Mockito.when(mapperFacade.map(Mockito.any(), Mockito.any())).thenReturn(userAvatar);

        UserAvatar result = userAvatarService.getUserAvatar(USER_GUID);

        Assert.assertNotNull(result);
        Assert.assertEquals(AVATAR, result.getAvatar());
    }

    @Test
    public void testGetUserAvatars() {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        restResponse.setBody(GET_AVATAR_RESPONSE);
        Mockito.when(listRestClient.getUserAvatars(Mockito.any())).thenReturn(restResponse);

        List<String> userGuids = Arrays.asList(USER_GUID);
        List<UserAvatarDTO> result = userAvatarService.getUserAvatars(userGuids);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(AVATAR, result.get(0).getAvatar());
        Assert.assertEquals(USER_GUID, result.get(0).getUserGuid());

        Mockito.verify(listRestClient).getUserAvatars(userGuids);
    }

    @Test(expected = ListServiceException.class)
    public void testGetUserAvatarsWithInvalidStatus() {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        restResponse.setBody("Some error");
        Mockito.when(listRestClient.getUserAvatars(Mockito.any())).thenReturn(restResponse);

        List<String> userGuids = Arrays.asList(USER_GUID);
        try {
            userAvatarService.getUserAvatars(userGuids);
        } catch (ListServiceException ex) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatusCode());
            Assert.assertEquals("Some error", ex.getServiceError());
            throw ex;
        }
    }
}
