package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.platform.rest.core.RequestContext;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.response.CollaboratorDTO;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.common.CollaboratorPrivilegeEnum;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserAvatarRequest;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaborativeListManageRequest;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaborativeListRequest;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaboratorPrivilegeRequest;
import com.macys.selection.xapi.list.rest.request.cookie.CookieHandler;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.ActivityLogPage;
import com.macys.selection.xapi.list.rest.response.CollaborativeListResponse;
import com.macys.selection.xapi.list.rest.response.CollaborativeRequestResponse;
import com.macys.selection.xapi.list.rest.response.Collaborator;
import com.macys.selection.xapi.list.rest.response.CollaboratorApprovalResponse;
import com.macys.selection.xapi.list.rest.response.UserAvatar;
import com.macys.selection.xapi.list.services.CollaborativeService;
import com.macys.selection.xapi.list.services.CustomerUserService;
import com.macys.selection.xapi.list.services.ListsService;
import com.macys.selection.xapi.list.services.UserAvatarService;
import com.macys.selection.xapi.list.services.WishlistService;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CollaborativeListResourceTest {
    private static final String LIST_GUID = "testGuid";
    private static final String ITEM_GUID = "itemGuid";
    private static final long USER_ID = 123L;
    private static final String USER_GUID = "testUserGuid";
    private static final String ITEM_FEEDBACK = "like";
    private static final String TOKEN = "testToken";
    private static final String URI_HOST = "http://api.macys.com/";

    @InjectMocks
    private CollaborativeListResource collaborativeResource;
    @Mock
    private WishlistService wishlistService;
    @Mock
    private CookieHandler cookieHandler;
    @Mock
    private CollaborativeService collaborativeService;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private RequestContext restContext;
    @Mock
    private ListsService listsService;
    @Mock
    private CustomerUserService customerUserService;
    @Mock
    private UserAvatarService userAvatarService;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
        collaborativeResource = new CollaborativeListResource(
                cookieHandler,
                wishlistService,
                collaborativeService,
                listsService,
                customerUserService,
                userAvatarService);
        ListCookies cookies = new ListCookies(USER_ID, USER_GUID, TOKEN);
        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(cookies);
    }

    @Test
    public void testAddItemFeedback() {
        Response response = collaborativeResource.addItemFeedback(httpServletRequest, LIST_GUID, ITEM_GUID, USER_GUID, ITEM_FEEDBACK);
        Assert.assertNotNull(response);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        verify(cookieHandler).parseAndValidate(httpServletRequest);
        verify(collaborativeService).addItemFeedback(LIST_GUID, ITEM_GUID, USER_GUID, ITEM_FEEDBACK);
    }

    @Test
    public void testCreateList() {
        CollaborativeListRequest collaborativeList = new CollaborativeListRequest();
        Response response = collaborativeResource.createList(httpServletRequest, collaborativeList);
        Assert.assertNotNull(response);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(cookieHandler).parseAndValidate(httpServletRequest);
        verify(collaborativeService).createCollaborativeList(any(), any());
    }

    @Test()
    public void testManageList() {
        Response response = collaborativeResource.manageList(httpServletRequest, LIST_GUID, USER_GUID, new CollaborativeListManageRequest());
        Assert.assertNotNull(response);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(collaborativeService).manageList(eq(TOKEN), anyString(), any(), any());
    }

    @Test
    public void testDeleteList() {
        Response response = collaborativeResource.deleteList(httpServletRequest, LIST_GUID, USER_GUID);
        Assert.assertNotNull(response);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(wishlistService, times(1)).deleteList(eq(TOKEN), any(), any());
    }

/*
    @Test(expectedExceptions = ListWebApplicationException.class)
    public void testAddItemToGivenListByUPCThrowsException() {
        when(requestValidator.isValid(any())).thenReturn(false);
        collaborativeResource.addItemToGivenListByUPC(httpServletRequest, new UserQueryParam(), LIST_GUID, new CollaborativeListResponse());
    }

    @Test
    public void testAddItemToGivenListByUPC() {
        when(requestValidator.isValid(any())).thenReturn(true);
        Response response = collaborativeResource.addItemToGivenListByUPC(httpServletRequest, new UserQueryParam(), LIST_GUID, new CollaborativeListResponse());

        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(requestValidator, times(1)).isValid(any());
        verify(cookieHandler, times(1)).parseAndValidate(any());
        verify(collaborativeService, times(1)).addItemToGivenListByUPC(any(), any(), any(), any());
    }


    @Test
    public void testDeleteItemToGivenListByUPC() {
        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(new ListCookies());
        Response response = collaborativeResource.deleteItem(httpServletRequest, LIST_GUID, ITEM_GUID);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(cookieHandler, times(1)).parseAndValidate(any());
        verify(wishlistService, times(1)).deleteItem(any(), any(), any());
    }
*/

    @Test
    public void testAddCollaborators() {
        CollaboratorDTO expected = new CollaboratorDTO();
        Collaborator collaborator = new Collaborator();
        when(collaborativeService.addCollaborator(collaborator)).thenReturn(expected);
        Response response = collaborativeResource.addCollaborator(collaborator);
        assertNotNull(response);
        verify(collaborativeService, times(1)).addCollaborator(any());
    }

    @Test
    public void testDeleteCollaborators() {
        CollaboratorDTO expected = new CollaboratorDTO();
        expected.setPrivilege(CollaboratorPrivilegeEnum.REMOVED.name());
        CollaboratorPrivilegeRequest collaborator = new CollaboratorPrivilegeRequest();
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());
        when(collaborativeService.updateCollaboratorPrivilege(LIST_GUID, collaborator)).thenReturn(restResponse);
        Response response = collaborativeResource.updateCollaboratorPrivilege(LIST_GUID, collaborator);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(collaborativeService, times(1)).updateCollaboratorPrivilege(any(), any());
    }

    @Test
    public void testGetCollaborators() {
        ListCookies cookies = new ListCookies();
        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(cookies);
        CollaborativeListResponse expected = new CollaborativeListResponse();
        when(collaborativeService.getCollaboratorsByListGuid(LIST_GUID)).thenReturn(expected);
        Response response = collaborativeResource.getCollaborators(LIST_GUID);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(collaborativeService, times(1)).getCollaboratorsByListGuid(any());
    }

    @Test
    public void testGetApprovals() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        CollaboratorApprovalResponse expected = new CollaboratorApprovalResponse();

        UserResponse userResponse = new UserResponse();
        userResponse.setGuid(USER_GUID);
        when(customerUserService.retrieveUser(null, USER_GUID)).thenReturn(userResponse);
        when(collaborativeService.getApprovals(userQueryParam, paginationQueryParam)).thenReturn(expected);
        Response response = collaborativeResource.getApprovals(USER_GUID, paginationQueryParam);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(collaborativeService, times(1)).getApprovals(any(), any());
    }

    @Test
    public void testGetRequests() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        CollaborativeRequestResponse expected = new CollaborativeRequestResponse();

        UserResponse userResponse = new UserResponse();
        userResponse.setGuid(USER_GUID);
        when(customerUserService.retrieveUser(null, USER_GUID)).thenReturn(userResponse);
        when(collaborativeService.getRequests(userQueryParam, paginationQueryParam)).thenReturn(expected);
        Response response = collaborativeResource.getRequests(USER_GUID, paginationQueryParam);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(collaborativeService, times(1)).getRequests(any(), any());
    }

    @Test
    public void testGetUserCollaborators() {
        ListCookies cookies = new ListCookies();
        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(cookies);
        CollaborativeListResponse expected = new CollaborativeListResponse();
        when(collaborativeService.getUserCollaborators(USER_GUID, Collections.emptySet())).thenReturn(expected);
        Response response = collaborativeResource.getUserCollaborators(USER_GUID, Collections.emptySet());
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(collaborativeService, times(1)).getUserCollaborators(USER_GUID, Collections.emptySet());
    }

    @Test
    public void testGetCollaborativeLists() {
        Response response = collaborativeResource.getCollaborativeLists(USER_GUID, false, false);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(listsService, times(1)).getCollaborativeLists(any(), anyBoolean(), anyBoolean());

    }

    @Test
    public void testGetActivityLog() {
        PaginationQueryParam paginationQueryParam  =  new PaginationQueryParam();
        ActivityLogPage expectedActivityLogPage = new ActivityLogPage();
        UserResponse userResponse = new UserResponse();
        userResponse.setGuid(USER_GUID);
        when(customerUserService.retrieveUser(null, USER_GUID)).thenReturn(userResponse);
        when(collaborativeService.getActivityLog(any(), any(), any(), any(), anyBoolean())).thenReturn(expectedActivityLogPage);

        Response response = collaborativeResource.getActivityLog(httpServletRequest, LIST_GUID, USER_GUID, paginationQueryParam);
        Assert.assertNotNull(response);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(expectedActivityLogPage, response.getEntity());

        verify(cookieHandler).parseAndValidate(httpServletRequest);
        verify(collaborativeService).getActivityLog(Mockito.eq(LIST_GUID), Mockito.eq(USER_GUID),
                Mockito.eq(paginationQueryParam), any(), Mockito.eq(true));
    }

    @Test
    public void testGetCollaborativeListDetails() {
        ListCookies cookies = new ListCookies(USER_ID, USER_GUID, TOKEN);
        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(cookies);

        Response details = collaborativeResource.getCollaborativeListDetails(httpServletRequest, LIST_GUID, USER_GUID, restContext);

        Assert.assertNotNull(details);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), details.getStatus());

        verify(cookieHandler).parseAndValidate(httpServletRequest);
        verify(collaborativeService).getCollaborativeListDetails(TOKEN, LIST_GUID, USER_GUID, URI_HOST);
    }

    @Test
    public void testAddUserAvatar() {
        ListCookies cookies = new ListCookies(USER_ID, USER_GUID, TOKEN);
        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(cookies);

        UserAvatarRequest userAvatarRequest = new UserAvatarRequest(USER_GUID, "testAvatar");
        Response response = collaborativeResource.addUserAvatar(httpServletRequest, userAvatarRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        verify(cookieHandler).parseAndValidate(httpServletRequest);
        verify(userAvatarService).addUserAvatar(TOKEN, userAvatarRequest);
    }

    @Test
    public void testGetUserAvatar() {
        ListCookies cookies = new ListCookies(USER_ID, USER_GUID, TOKEN);
        UserAvatar expectedAvatar = new UserAvatar("test");
        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(cookies);
        when(userAvatarService.getUserAvatar(USER_GUID)).thenReturn(expectedAvatar);

        Response response = collaborativeResource.getUserAvatar(httpServletRequest, USER_GUID);

        Assert.assertNotNull(response);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(expectedAvatar, response.getEntity());

        verify(cookieHandler).parseAndValidate(httpServletRequest);
        verify(userAvatarService).getUserAvatar(USER_GUID);
    }

    @Test
    public void testDeleteUserAvatar() {
        ListCookies cookies = new ListCookies(USER_ID, USER_GUID, TOKEN);
        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(cookies);

        Response response = collaborativeResource.deleteUserAvatar(httpServletRequest, USER_GUID);

        Assert.assertNotNull(response);
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        verify(cookieHandler).parseAndValidate(httpServletRequest);
        verify(userAvatarService).deleteAvatar(TOKEN, USER_GUID);
    }
}
