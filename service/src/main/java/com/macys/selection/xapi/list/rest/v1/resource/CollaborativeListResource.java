package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.platform.rest.core.RequestContext;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.response.CollaboratorDTO;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.comparators.SortOrder;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.SortQueryParam;
import com.macys.selection.xapi.list.rest.request.UserAvatarRequest;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaborativeListManageRequest;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaborativeListRequest;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaboratorPrivilegeRequest;
import com.macys.selection.xapi.list.rest.request.cookie.CookieHandler;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.ActivityLogPage;
import com.macys.selection.xapi.list.rest.response.CollaborativeListDetails;
import com.macys.selection.xapi.list.rest.response.CollaborativeListResponse;
import com.macys.selection.xapi.list.rest.response.CollaborativeRequestResponse;
import com.macys.selection.xapi.list.rest.response.Collaborator;
import com.macys.selection.xapi.list.rest.response.CollaboratorApprovalResponse;
import com.macys.selection.xapi.list.rest.response.ListsPresentation;
import com.macys.selection.xapi.list.rest.response.UserAvatar;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.services.CollaborativeService;
import com.macys.selection.xapi.list.services.CustomerUserService;
import com.macys.selection.xapi.list.services.ListsService;
import com.macys.selection.xapi.list.services.UserAvatarService;
import com.macys.selection.xapi.list.services.WishlistService;
import com.macys.selection.xapi.list.util.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * @author BH06684
 *         <p>
 *         Collaborative list resource for adding, deleting, and updating item from with-in a collaborative list.
 *         CollaborativeListResources comments.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
@Path("/collaborative/v1/lists")
@Api(value = "Collaborative list API's")
public class CollaborativeListResource {

    private final CookieHandler cookieHandler;

    private final WishlistService mspWishlistService;

    private final CustomerUserService customerUserService;

    private final CollaborativeService mspCollaborativeService;

    private final ListsService listsService;

    private final UserAvatarService userAvatarService;


    @Autowired
    public CollaborativeListResource(
            CookieHandler cookieHandler,
            WishlistService mspWishlistService,
            CollaborativeService mspCollaborativeService,
            ListsService listsService,
            CustomerUserService customerUserService,
            UserAvatarService userAvatarService) {
        this.cookieHandler = cookieHandler;
        this.mspWishlistService = mspWishlistService;
        this.mspCollaborativeService = mspCollaborativeService;
        this.listsService = listsService;
        this.customerUserService = customerUserService;
        this.userAvatarService = userAvatarService;
    }

    @POST
    @ApiOperation(value = "Creates collaborative list")
    public Response createList(@Context HttpServletRequest request, CollaborativeListRequest inputJsonObj) {
        // TODO: rework all service methods on receiving simple string token instead of passing cookie
        // security token validation
        ListCookies cookie = cookieHandler.parseAndValidate(request);
        String listGuid = mspCollaborativeService.createCollaborativeList(cookie, inputJsonObj);
        WishList list = new WishList();
        list.setListGuid(listGuid);
        return Response.ok(list).build();

    }

    @Path("/{listGuid}")
    @PUT
    public Response manageList(@Context HttpServletRequest request,
                               @PathParam("listGuid") String listGuid,
                               @QueryParam("userGuid") String userGuid,
                               CollaborativeListManageRequest inputJsonObj) {
        ListCookies cookie = cookieHandler.parseAndValidate(request);
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(userGuid);
        mspCollaborativeService.manageList(cookie.getToken(), listGuid, userQueryParam, inputJsonObj);
        return Response.noContent().build();
    }

   /* @GET
    @Path("/{listGuid}")
    @ApiOperation(value = "Gets collaborative list by guid")
    public Response getListByGuid(@Context HttpServletRequest request,
                                          @PathParam("listGuid") String listGuid,
                                          @Context RequestContext restContext) {
        // security token validation
        ListCookies cookie = cookieHandler.parseAndValidate(request);
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setListType(ListTypeEnum.COLLABORATIVE_LIST.getValue());
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_USER);
        CollaborativeListResponse response = mspCollaborativeService.getListByGuid(cookie.getToken(), listQueryParam, listGuid, restContext);
        return Response.ok(response).build();
    }*/

    @DELETE
    @Path("/{listGuid}")
    @ApiOperation(value = "Deletes a collaborative list using listGuid")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "")})
    public Response deleteList(@Context HttpServletRequest request,
                               @PathParam("listGuid") String listGuid,
                               @QueryParam("userGuid") String userGuid) {
        ListCookies cookie = cookieHandler.parseAndValidate(request);
        //TODO validate list type on delete collab or other
        UserQueryParam queryParam= new UserQueryParam();
        queryParam.setUserGuid(userGuid);
        mspWishlistService.deleteList(cookie.getToken(), listGuid, queryParam);
        return Response.noContent().build();
    }

/*    *//**
     * To add item to a given wishlist by a listGuid in the url
     *//*
    @POST
    @Path("/{listGuid}/items")
    public Response addItemToGivenListByUPC(@Context HttpServletRequest request,
                                            @BeanParam UserQueryParam userQueryParam,
                                            @PathParam("listGuid") String listGuid,
                                            @Valid @Nonnull CollaborativeListResponse list) {
        CustomerList customerList = CollaborativeListConverter.convertToCustomerList(list);

        if (!customerListItemExistsRequestValidator.isValid(customerList)) {
            throw new ListWebApplicationException(ErrorConstants.BAD_JASON_INPUT_ITMES_NOT_AVAILABLE, Response.Status.BAD_REQUEST.getStatusCode());
        }
        // security token validation
        ListCookies cookie = cookieHandler.parseAndValidate(request);
        mspCollaborativeService.addItemToGivenListByUPC(cookie, listGuid, customerList, userQueryParam);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{listGuid}/items/{itemGuid}")
    public Response deleteItem(@Context HttpServletRequest request,
                               @PathParam("listGuid") String listGuid,
                               @PathParam("itemGuid") String itemGuid) {
        // security token validation
        ListCookies cookie = cookieHandler.parseAndValidate(request);
        mspWishlistService.deleteItem(cookie.getToken(), listGuid, itemGuid);
        return Response.noContent().build();
    }

    @POST
    @Path("/{listGuid}/collaborators")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addCollaborators(@PathParam("listGuid") String listGuid,
                                     Set<Collaborator> collaborators) {
        RestResponse result = mspCollaborativeService.addCollaborators(listGuid, collaborators);
        return Response.status(result.getStatusCode()).build();
    }*/

    @POST
    @Path("/addcollaborator")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addCollaborator(Collaborator collaborator) {
        CollaboratorDTO result = mspCollaborativeService.addCollaborator(collaborator);
        return Response.ok(result).build();
    }

    @PUT
    @Path("/{listGuid}/privilege")
    public Response updateCollaboratorPrivilege(@PathParam("listGuid") String listGuid,
                                        CollaboratorPrivilegeRequest request) {
        RestResponse response = mspCollaborativeService.updateCollaboratorPrivilege(
            listGuid,
            request
        );
        return Response.status(response.getStatusCode()).build();
    }

    @GET
    @Path("/{listGuid}/collaborators")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCollaborators(@PathParam("listGuid") String listGuid) {
        CollaborativeListResponse result = mspCollaborativeService.getCollaboratorsByListGuid(listGuid);
        return Response.ok(result).build();
    }

    @GET
    @Path("/approvals")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getApprovals(@QueryParam("userGuid") String userGuid,
                                 @BeanParam PaginationQueryParam paginationQueryParam) {
        customerUserService.retrieveUser(null, userGuid);
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(userGuid);
        CollaboratorApprovalResponse response = mspCollaborativeService.getApprovals(userQueryParam, paginationQueryParam);
        return Response.ok(response).build();
    }

    @GET
    @Path("/requests")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRequests(@QueryParam("userGuid") String userGuid,
                                @BeanParam PaginationQueryParam paginationQueryParam) {
        customerUserService.retrieveUser(null, userGuid);
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(userGuid);
        CollaborativeRequestResponse response = mspCollaborativeService.getRequests(userQueryParam, paginationQueryParam);
        return Response.ok(response).build();
    }

    @GET
    @Path("/userCollaborators")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUserCollaborators(@QueryParam("userGuid") String userGuid, @QueryParam("exclude") final Set<String> excludeIds) {
        customerUserService.retrieveUser(null, userGuid);
        CollaborativeListResponse result = mspCollaborativeService.getUserCollaborators(userGuid, excludeIds);
        return Response.ok(result).build();
    }

    @GET
    @Path("/all")
    @ApiOperation(value = "Retrieves a customer lists grouped by listTypes", response = ListsPresentation.class)
    public Response getCollaborativeLists(@QueryParam("userGuid") String userGuid,
                                          @QueryParam("hideOwner") boolean hideOwner,
                                          @QueryParam("hideCollab") boolean hideCollab) {
        ListsPresentation response = listsService.getCollaborativeLists(userGuid, hideOwner,hideCollab);
        return Response.ok(response).build();
    }

    @Path("/{listGuid}/feedback/{itemGuid}")
    @POST
    public Response addItemFeedback(@Context HttpServletRequest request,
                                    @PathParam("listGuid") String listGuid,
                                    @PathParam("itemGuid") String itemGuid,
                                    @QueryParam("userGuid") String userGuid,
                                    @QueryParam("itemFeedback") String itemFeedback) {
        cookieHandler.parseAndValidate(request);
        mspCollaborativeService.addItemFeedback(listGuid, itemGuid, userGuid, itemFeedback);
        return Response.noContent().build();
    }

    @Path("/{listGuid}/activitylog")
    @GET
    public Response getActivityLog(@Context HttpServletRequest request,
                                    @PathParam("listGuid") String listGuid,
                                    @QueryParam("userGuid") String userGuid,
                                    @BeanParam PaginationQueryParam paginationQueryParam) {
        cookieHandler.parseAndValidate(request);
        UserResponse userResponse = customerUserService.retrieveUser(null, userGuid);
        SortQueryParam sortQueryParam = new SortQueryParam();
        sortQueryParam.setSortOrder(SortOrder.DESC.getValue());
        ActivityLogPage activityLogPage = mspCollaborativeService.getActivityLog(listGuid, userResponse.getGuid(),
                paginationQueryParam, sortQueryParam, true);
        return Response.ok(activityLogPage).build();
    }

    @Path("/{listGuid}/details")
    @GET
    public Response getCollaborativeListDetails(@Context HttpServletRequest request,
                                   @PathParam("listGuid") String listGuid,
                                   @QueryParam("userGuid") String viewerGuid,
                                   @Context RequestContext restContext) {
        ListCookies cookies = cookieHandler.parseAndValidate(request);
        CollaborativeListDetails details = mspCollaborativeService.getCollaborativeListDetails(cookies.getToken(),
                listGuid, viewerGuid, ListUtil.getUriHost(restContext));
        return Response.ok(details).build();
    }

    @Path("/avatar")
    @POST
    public Response addUserAvatar(@Context HttpServletRequest request,
                                  UserAvatarRequest userAvatarRequest) {
        ListCookies cookies = cookieHandler.parseAndValidate(request);
        userAvatarService.addUserAvatar(cookies.getToken(), userAvatarRequest);
        return Response.noContent().build();
    }

    @Path("/avatar/{userGuid}")
    @DELETE
    public Response deleteUserAvatar(@Context HttpServletRequest request,
                                  @PathParam("userGuid") String userGuid) {
        ListCookies cookies = cookieHandler.parseAndValidate(request);
        userAvatarService.deleteAvatar(cookies.getToken(), userGuid);
        return Response.noContent().build();
    }

    @Path("/avatar/{userGuid}")
    @GET
    public Response getUserAvatar(@Context HttpServletRequest request,
                                     @PathParam("userGuid") String userGuid) {
        cookieHandler.parseAndValidate(request);
        UserAvatar userAvatar = userAvatarService.getUserAvatar(userGuid);
        return Response.ok(userAvatar).build();
    }

}
