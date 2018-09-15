package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.ListRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.response.CollaboratorDTO;
import com.macys.selection.xapi.list.client.response.UserAvatarDTO;
import com.macys.selection.xapi.list.client.response.fcc.ProductResponse;
import com.macys.selection.xapi.list.client.response.fcc.UpcResponse;
import com.macys.selection.xapi.list.client.response.user.ProfileResponse;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.common.CollaboratorPrivilegeEnum;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.mapping.MapperConfig;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.SortQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaborativeListManageRequest;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaboratorPrivilegeRequest;
import com.macys.selection.xapi.list.rest.response.ActivityLog;
import com.macys.selection.xapi.list.rest.response.ActivityLogPage;
import com.macys.selection.xapi.list.rest.response.CollaborativeItem;
import com.macys.selection.xapi.list.rest.response.CollaborativeListDetails;
import com.macys.selection.xapi.list.rest.response.CollaborativeListResponse;
import com.macys.selection.xapi.list.rest.response.CollaborativeRequestResponse;
import com.macys.selection.xapi.list.rest.response.Collaborator;
import com.macys.selection.xapi.list.rest.response.CollaboratorApprovalResponse;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.UserProfile;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.util.ListRequestParamUtil;
import ma.glasnost.orika.MapperFacade;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CollaborativeServiceTest {

    private static final String LIST_GUID = "testListGuid";
    private static final String GUID_631116 = "guid_631116";
    private static final String ITEM_GUID = "testItemGuid";
    private static final String USER_GUID = "guid_123";
    private static final String USER_GUID_2 = "guid_345";
    private static final ListServiceException invalidUserIdException = new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.INVALID_USER_ID);
    private static final String ITEM_FEEDBACK = "itemFeedback";
    private static final String TOKEN = "token";
    private static final Integer UPC_ID = 345;
    private static final Integer PRODUCT_ID = 678;
    private static final String ACTIVITY_LOG_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_activity_log_response.json";
    private static final String LIST_FEEDBACK_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_list_feedback_response.json";
    private static final String COLLABORATORS_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_collaborators.json";
    private static final String COLLABORATOR_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_collaborator.json";
    private static final String COLLABORATIVE_REQUESTS_LISTS_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_collaborative_requests.json";
    private static final String COLLABORATIVE_APPROVAL_LISTS_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_collaborative_approvals.json";


    @Mock
    private ListRestClient listRestClient;
    @Mock
    private WishlistService wishlistService;
    @Mock
    private CustomerUserService customerUserService;
    @Mock
    private UpcService upcService;
    @Mock
    private ProductService productService;
    @Mock
    PromotionService promotionService;
    @Mock
    private UserAvatarService userAvatarService;

    private MapperFacade mapperFacade;

    private CollaborativeService collaborativeService;

    private DateFormat dateFormat;

    public CollaborativeServiceTest() {
        MapperConfig mapperConfig = new MapperConfig();
        mapperFacade = mapperConfig.mapperFacade();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
        collaborativeService = new CollaborativeService(wishlistService, listRestClient, customerUserService,
                upcService, productService, promotionService, userAvatarService, mapperFacade);
    }

/*    @Test
    public void testCreateList() {
        CollaborativeListRequest request = new CollaborativeListRequest();
        ListCookies listCookies = new ListCookies();

        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setListGuid(LIST_GUID);
        PowerMockito
        customerList.setWishlist(Collections.singletonList(wishList));
        when(CollaborativeListConverter.convertToCustomerList(request)).thenReturn(customerList);
        when(wishlistService.createWishList(listCookies, customerList)).thenReturn(customerList);

        collaborativeService.createCollaborativeList(listCookies, request);

        verify(wishlistService, times(1)).createWishList(any(), any());
        verify(listRestClient, times(1)).addCollaborator(any());
    }*/

    @Test
    public void testManageList() {
        CollaborativeListManageRequest request = new CollaborativeListManageRequest();
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        collaborativeService.manageList(TOKEN, LIST_GUID, userQueryParam, request);

        verify(wishlistService, times(1)).updateList(eq(TOKEN), any(), any(), any());
    }

    @Test
    public void testAddCollaborator() throws IOException {
        Collaborator collaborator = new Collaborator();
        collaborator.setUserGuid(USER_GUID);

        UserResponse userResponse = new UserResponse();
        userResponse.setGuid(USER_GUID);
        List<UserResponse> userResponses = Collections.singletonList(userResponse);

        String collResponse = TestUtils.readFile(COLLABORATOR_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(collResponse);

        CollaboratorDTO collaboratorDTO = new CollaboratorDTO();
        collaboratorDTO.setUserGuid(USER_GUID);
        when(customerUserService.retrieveUsersByGuids(Collections.singleton(USER_GUID))).thenReturn(userResponses);
        when(listRestClient.addCollaborator(any())).thenReturn(restResponse);
        CollaboratorDTO dto = collaborativeService.addCollaborator(collaborator);
        Assert.assertEquals(CollaboratorPrivilegeEnum.PENDING.name(), dto.getPrivilege());
        verify(customerUserService, times(1)).retrieveUsersByGuids(any());
        verify(listRestClient, times(1)).addCollaborator(any());
    }

    @Test
    public void testGetCollaboratorsByListGuid() throws IOException {
        String collResponse = TestUtils.readFile(COLLABORATORS_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(collResponse);
        when(listRestClient.getCollaboratorsByGuid(LIST_GUID)).thenReturn(restResponse);
        List<Collaborator> collaborators = collaborativeService.getCollaborators(LIST_GUID);

        Assert.assertEquals(1, collaborators.size());
        verify(listRestClient, times(1)).getCollaboratorsByGuid(LIST_GUID);
    }

    @Test
    public void testGetCollaborators() throws IOException {
        String collResponse = TestUtils.readFile(COLLABORATORS_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(collResponse);
        when(listRestClient.getCollaboratorsByGuid(LIST_GUID)).thenReturn(restResponse);
        List<Collaborator> result = collaborativeService.getCollaborators(LIST_GUID);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        verify(listRestClient, times(1)).getCollaboratorsByGuid(LIST_GUID);
    }

    @Test
    public void testGetUserCollaborators() throws IOException {
        String collResponse = TestUtils.readFile(COLLABORATORS_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(collResponse);
        Set<String> excludeIds = Collections.emptySet();
        when(listRestClient.getUserCollaborators(USER_GUID, excludeIds)).thenReturn(restResponse);
        CollaborativeListResponse result = collaborativeService.getUserCollaborators(USER_GUID, excludeIds);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.getCollaborators().size());
        verify(listRestClient, times(1)).getUserCollaborators(USER_GUID, excludeIds);
    }

    @Test
    public void testUpdateCollaboratorPrivilege() {
        RestResponse expected = new RestResponse();
        expected.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());
        CollaboratorPrivilegeRequest request = new CollaboratorPrivilegeRequest();
        CollaboratorPrivilegeRequest.Collaborator collaboratorPrivilege = new CollaboratorPrivilegeRequest.Collaborator();
        request.setCollaborator(collaboratorPrivilege);
        when(listRestClient.updateCollaboratorPrivilege(anyString(), any())).thenReturn(expected);
        RestResponse response = collaborativeService.updateCollaboratorPrivilege(LIST_GUID, request);

        Assert.assertEquals(expected.getStatusCode(), response.getStatusCode());
        verify(listRestClient, times(1)).updateCollaboratorPrivilege(anyString(), any());
    }

    @Test
    public void testGetApprovals() throws IOException {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();

        String collResponse = TestUtils.readFile(COLLABORATIVE_APPROVAL_LISTS_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        restResponse.setBody(collResponse);
        when(listRestClient.getApprovals(userQueryParam, paginationQueryParam)).thenReturn(restResponse);

        CollaboratorApprovalResponse response = collaborativeService.getApprovals(userQueryParam, paginationQueryParam);

        Assert.assertEquals(2, response.getList().size());
        verify(listRestClient, times(1)).getApprovals(userQueryParam, paginationQueryParam);
    }
    @Test
    public void testGetRequests() throws IOException {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(USER_GUID);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();

        String collResponse = TestUtils.readFile(COLLABORATIVE_REQUESTS_LISTS_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        restResponse.setBody(collResponse);
        when(listRestClient.getRequests(userQueryParam, paginationQueryParam)).thenReturn(restResponse);

        CollaborativeRequestResponse response = collaborativeService.getRequests(userQueryParam, paginationQueryParam);

        Assert.assertEquals(1, response.getRequests().size());
        verify(listRestClient, times(1)).getRequests(userQueryParam, paginationQueryParam);
    }

/*    @Test
    public void testGetListByGuid() throws IOException {
        CollaborativeListRequest request = new CollaborativeListRequest();
        request.setUserGuid(USER_GUID);

        WishList wishList = new WishList();
        wishList.setListType("C");
        wishList.setListGuid(LIST_GUID);

        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setListType(ListTypeEnum.COLLABORATIVE_LIST.getValue());
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_USER);
        String collResponse = TestUtils.readFile(COLLABORATORS_JSON_FILE);

        RestResponse restResponse = new RestResponse();
        restResponse.setBody(collResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        CustomerList customerList = CollaborativeListConverter.convertToCustomerList(request);
        customerList.setWishlist(Collections.singletonList(wishList));
        when(wishlistService.getListByGuid(LIST_GUID, listQueryParam, null, ListUtil.getUriHost(restContext)))
                .thenReturn(customerList);
        when(promotionService.getPromotions(any(), any())).thenReturn(wishList);
        when(listRestClient.getCollaboratorsByGuid(LIST_GUID)).thenReturn(restResponse);

        CollaborativeListResponse collaborativeListResponse = collaborativeService.getListByGuid(listQueryParam, LIST_GUID, restContext);

        Assert.assertEquals(ListTypeEnum.COLLABORATIVE_LIST.getValue(), collaborativeListResponse.getWishlist().getListType());
    }*/

    @Test
    public void testAddItemFeedback() {

        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());

        UserResponse userResponse = new UserResponse();
        when(customerUserService.retrieveUser(null, USER_GUID)).thenReturn(userResponse);
        Mockito.doCallRealMethod().when(customerUserService).populateUserQueryParam(any(), any(), anyBoolean());
        when(listRestClient.addItemFeedback(eq(LIST_GUID), eq(ITEM_GUID), any(), eq(ITEM_FEEDBACK))).thenReturn(restResponse);

        collaborativeService.addItemFeedback(LIST_GUID, ITEM_GUID, USER_GUID, ITEM_FEEDBACK);
        verify(customerUserService).retrieveUser(null, USER_GUID);
        verify(customerUserService).populateUserQueryParam(any(), eq(userResponse), eq(false));
        verify(listRestClient).addItemFeedback(eq(LIST_GUID), eq(ITEM_GUID), any(), eq(ITEM_FEEDBACK));

    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testAddItemFeedbackUserNotFound() {
        Mockito.when(customerUserService.retrieveUser(any(), any())).thenThrow(invalidUserIdException);
        try {
            collaborativeService.addItemFeedback(LIST_GUID, ITEM_GUID, USER_GUID, ITEM_FEEDBACK);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode(), e.getServiceErrorCode());
            verify(customerUserService).retrieveUser(null, USER_GUID);
            verify(listRestClient, never()).addItemFeedback(any(), any(), any(), any());
            throw e;
        }
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testAddItemFeedbackWithInvalidStatus() {
        String errorMessage = "{error: 'Some error happend!'}";
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        restResponse.setBody(errorMessage);

        Mockito.when(listRestClient.addItemFeedback(eq(LIST_GUID), eq(ITEM_GUID), any(), eq(ITEM_FEEDBACK))).thenReturn(restResponse);

        try {
            collaborativeService.addItemFeedback(LIST_GUID, ITEM_GUID, USER_GUID, ITEM_FEEDBACK);
            Assert.fail("Invalid response status");
        } catch (ListServiceException ex) {
            Mockito.verify(listRestClient).addItemFeedback(eq(LIST_GUID), eq(ITEM_GUID), any(), eq(ITEM_FEEDBACK));
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex.getStatusCode());
            Assert.assertEquals(errorMessage, ex.getServiceError());
            throw ex;
        }
    }

    @Test
    public void testGetActivityLog() throws IOException {
        String activityLogResponseJson = TestUtils.readFile(ACTIVITY_LOG_JSON_FILE);
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        SortQueryParam sortQueryParam = new SortQueryParam();
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(activityLogResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(listRestClient.getActivityLog(any(), any(), any(), any())).thenReturn(restResponse);

        UpcResponse upc = new UpcResponse();
        upc.setId(345);
        upc.setProductId(1111);
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1111);
        productResponse.setUpcs(Arrays.asList(upc));
        productResponse.setName("Some product");
        when(upcService.getUpcsByUpcIds(any())).thenReturn(Arrays.asList(upc));
        when(productService.getProductsByProdIds(any())).thenReturn(Arrays.asList(productResponse));

        UserResponse userResponse1 = getUserResponse(USER_GUID, "TestName1", "TestLastName1");
        UserResponse userResponse2 = getUserResponse(USER_GUID_2, "TestName2", "TestLastName2");
        when(customerUserService.retrieveUsersByGuids(any())).thenReturn(Arrays.asList(userResponse1, userResponse2));

        ActivityLogPage activityLogPage = collaborativeService.getActivityLog(LIST_GUID, USER_GUID, paginationQueryParam, sortQueryParam, true);

        Assert.assertNotNull(activityLogPage);
        Assert.assertNotNull(activityLogPage.getActivityLog());
        Assert.assertEquals(10, activityLogPage.getTotalNumberOfLogs().intValue());
        Assert.assertEquals(2, activityLogPage.getActivityLog().size());

        ActivityLog activityLog1 = activityLogPage.getActivityLog().get(0);
        Assert.assertNotNull(activityLog1);
        Assert.assertEquals(USER_GUID, activityLog1.getUserGuid());
        Assert.assertEquals("guid_631116", activityLog1.getListGuid());
        Assert.assertEquals("LIKE", activityLog1.getActivityType());
        Assert.assertEquals("TestName1", activityLog1.getUserFirstName());
        Assert.assertEquals("TestLastName1", activityLog1.getUserLastName());
        Assert.assertEquals("Some product", activityLog1.getProductName());

        ActivityLog activityLog2 = activityLogPage.getActivityLog().get(1);
        Assert.assertNotNull(activityLog2);
        Assert.assertEquals(USER_GUID_2, activityLog2.getUserGuid());
        Assert.assertEquals("guid_631116", activityLog2.getListGuid());
        Assert.assertEquals("DISLIKE", activityLog2.getActivityType());
        Assert.assertEquals("TestName2", activityLog2.getUserFirstName());
        Assert.assertEquals("TestLastName2", activityLog2.getUserLastName());
        Assert.assertEquals("Some product", activityLog2.getProductName());
    }

    @Test
    public void testGetCollaborativeListDetails() throws IOException {
        UserResponse user1 = getUserResponse(USER_GUID,"FirstName1", "LastName1");
        UserResponse user2 = getUserResponse(USER_GUID_2,"FirstName2", "LastName2");
        when(customerUserService.retrieveUser(null, USER_GUID)).thenReturn(user1);
        when(customerUserService.retrieveUser(null, USER_GUID_2)).thenReturn(user2);

        CustomerList expectedList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setListGuid(GUID_631116);
        Item expectedItem = new Item();
        expectedItem.setItemGuid(ITEM_GUID);
        expectedItem.setUpc(new Upc());
        expectedItem.getUpc().setId(UPC_ID);
        expectedItem.setProduct(new Product());
        expectedItem.getProduct().setId(PRODUCT_ID);
        wishList.setItems(Arrays.asList(expectedItem));
        wishList.setNumberOfItems(1);
        expectedList.setWishlist(Arrays.asList(wishList));
        User user = new User();
        user.setGuid(USER_GUID_2);
        expectedList.setUser(user);

        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_ITEMS);


        when(wishlistService.getListByGuid(eq(GUID_631116), any(), anyString())).thenReturn(expectedList);
        when(promotionService.getPromotions(any(), any())).thenReturn(wishList);

        String collaboratorsResponseJson = TestUtils.readFile(COLLABORATORS_JSON_FILE);
        RestResponse collaboratorsResponse = new RestResponse();
        collaboratorsResponse.setBody(collaboratorsResponseJson);
        collaboratorsResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(listRestClient.getCollaboratorsByGuid(GUID_631116)).thenReturn(collaboratorsResponse);

        String listFeedbackResponseJson = TestUtils.readFile(LIST_FEEDBACK_JSON_FILE);
        RestResponse listFeedbackResponse = new RestResponse();
        listFeedbackResponse.setBody(listFeedbackResponseJson);
        listFeedbackResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(listRestClient.getListFeedback(GUID_631116, USER_GUID_2, USER_GUID)).thenReturn(listFeedbackResponse);

        String activityLogResponseJson = TestUtils.readFile(ACTIVITY_LOG_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(activityLogResponseJson);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());
        when(listRestClient.getActivityLog(any(), any(), any(), any())).thenReturn(restResponse);

        List<UserResponse> expectedUsers = Arrays.asList(user1, user2);
        when(customerUserService.retrieveUsersByGuids(any())).thenReturn(expectedUsers);

        UpcResponse upcResponse = new UpcResponse();
        upcResponse.setId(UPC_ID);
        upcResponse.setProductId(PRODUCT_ID);
        when(upcService.getUpcsByUpcIds(anyList())).thenReturn(Arrays.asList(upcResponse));

        ProductResponse productResponse = new ProductResponse();
        productResponse.setUpcs(Arrays.asList(upcResponse));
        productResponse.setId(PRODUCT_ID);
        productResponse.setName("testProduct");
        when(productService.getProductsByProdIds(anyList())).thenReturn(Arrays.asList(productResponse));

        UserAvatarDTO userAvatar1 = new UserAvatarDTO(USER_GUID, "avatar1");
        UserAvatarDTO userAvatar2 = new UserAvatarDTO(USER_GUID_2, "avatar2");
        when(userAvatarService.getUserAvatars(anyList())).thenReturn(Arrays.asList(userAvatar1, userAvatar2));

        CollaborativeListDetails details = collaborativeService.getCollaborativeListDetails(TOKEN, GUID_631116, USER_GUID, "");

        Assert.assertNotNull(details);
        Assert.assertEquals(GUID_631116, details.getListGuid());
        Assert.assertEquals(1, details.getNumberOfItems().intValue());
        Assert.assertEquals(1, details.getNumberOfCollaborators().intValue());

        Assert.assertNotNull(details.getOwner());
        UserProfile owner = details.getOwner();
        Assert.assertEquals(USER_GUID_2, owner.getGuid());
        Assert.assertEquals("FirstName2", owner.getFirstName());
        Assert.assertEquals("LastName2", owner.getLastName());
        Assert.assertEquals("avatar2", owner.getProfilePicture());

        Assert.assertNotNull(details.getViewer());
        UserProfile viewer = details.getViewer();
        Assert.assertEquals(USER_GUID, viewer.getGuid());
        Assert.assertEquals("FirstName1", viewer.getFirstName());
        Assert.assertEquals("LastName1", viewer.getLastName());
        Assert.assertEquals("avatar1", viewer.getProfilePicture());

        Assert.assertNotNull(details.getRecentActivity());
        ActivityLog activityLog = details.getRecentActivity();
        Assert.assertEquals(GUID_631116, activityLog.getListGuid());
        Assert.assertEquals(USER_GUID, activityLog.getUserGuid());
        Assert.assertEquals("FirstName1", activityLog.getUserFirstName());
        Assert.assertEquals("LastName1", activityLog.getUserLastName());
        Assert.assertEquals("avatar1", activityLog.getProfilePictureUrl());
        Assert.assertEquals("testProduct", activityLog.getProductName());
        Assert.assertEquals("LIKE", activityLog.getActivityType());

        List<CollaborativeItem> items = details.getItems();
        Assert.assertNotNull(items);
        Assert.assertEquals(1, items.size());
        CollaborativeItem item = items.get(0);
        Assert.assertNotNull(item);
        Assert.assertEquals(ITEM_GUID, item.getItemGuid());
        Assert.assertEquals(3, item.getLikes());
        Assert.assertEquals(2, item.getDislikes());
        Assert.assertEquals("LIKE", item.getFeedback());

        List<Collaborator> collaborators = details.getCollaborators();
        Assert.assertNotNull(collaborators);
        Assert.assertEquals(1, collaborators.size());
        Collaborator collaborator = collaborators.get(0);
        Assert.assertEquals(GUID_631116, collaborator.getListGuid());
        Assert.assertEquals(USER_GUID, collaborator.getUserGuid());
        Assert.assertEquals("LIKE", collaborator.getPrivilege());
        Assert.assertEquals("FirstName1", collaborator.getFirstName());
        Assert.assertEquals("LastName1", collaborator.getLastName());
        Assert.assertEquals("avatar1", collaborator.getProfilePicture());
        Assert.assertNotNull(collaborator.getCreatedDate());
        Assert.assertNotNull(collaborator.getLastModifiedDate());
    }

    private UserResponse getUserResponse(String userGuid, String firstName, String lastName) {
        UserResponse userResponse = new UserResponse();
        userResponse.setGuid(userGuid);
        userResponse.setProfile(new ProfileResponse());
        userResponse.getProfile().setFirstName(firstName);
        userResponse.getProfile().setLastName(lastName);
        return userResponse;
    }
}
