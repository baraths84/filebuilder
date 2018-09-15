package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.selection.xapi.list.exception.ErrorConstants;
import com.macys.selection.xapi.list.exception.ListWebApplicationException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.CookieHandler;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.request.validator.RequestValidators;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.FavoriteList;
import com.macys.selection.xapi.list.services.CustomerService;
import com.macys.selection.xapi.list.services.WishlistService;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by m940030 on 11/8/17.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
@Path("/wishlist/v1")
@Api(value = "List API's")
public class FavoriteRestResource {

    @Autowired
    CustomerService customerService;

    @Autowired
    private CookieHandler cookieHandler;

    @Autowired
    private RequestValidators<CustomerList, Boolean> customerListItemExistsRequestValidator;

    @Autowired
    private WishlistService mspWishlistService;

    @Autowired
    private KillSwitchPropertiesBean killswitchProperties;

    /**
     * To add favourites to a given wishlist
     *
     * @param inputJsonObj The Json format of CustomerList. At least one item is required.
     * @return Json string (CustomerList)
     */
    @ApiOperation(value = "Adding Favorite Products to a list as a Guest User or SignedIn User", notes = "Adding Favorite Products to a list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userGuid", value = "User Guid", required = false, dataType = "com.macys.selection.xapi.list.rest.response.User", paramType = "body"),
            @ApiImplicitParam(name = "productId", value = "Product ID", required = true, dataType = "com.macys.selection.xapi.list.rest.response.Product", paramType = "body")

    })
    @Path("/favorites")
    @POST
    public Response addFavItemToGivenListByPID(@Context HttpServletRequest request,
                                               @ApiParam(value = "customer list", required = true) CustomerList inputJsonObj) {

        ListCookies cookie = cookieHandler.parseAndValidate(request);
        CustomerList result;
        if (killswitchProperties.isMspListEnabled()) {
            if (!customerListItemExistsRequestValidator.isValid(inputJsonObj)) {
                throw new ListWebApplicationException(ErrorConstants.BAD_JASON_INPUT_ITMES_NOT_AVAILABLE, Response.Status.BAD_REQUEST.getStatusCode());
            }
            result = mspWishlistService.addItemToDefaultWishlist(inputJsonObj);
            if (CollectionUtils.isNotEmpty(result.getWishlist())) {
                result.getWishlist().get(0).setItems(null);
            }
        } else {
            result = customerService.addFavItemToGivenListByPID(cookie, inputJsonObj);
        }
        return Response.ok(result).build();
    }

    /**
     * To add favourites to a given wishlist by a listGuid in the url
     //* @param userGuid list guid
     * @return Json string (CustomerList)
     */
    @ApiOperation(value = "Get Favorite Products for a given User", notes = "Getting Favorite Products to a list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userGuid", value = "User Guid", required = true, dataType = "com.macys.selection.xapi.list.rest.response.User", paramType = "body")
    })
    @Path("/{userGuid}/favorites")
    @GET
    public Response getFavItemFromListByGuid(@Context HttpServletRequest request,
                                             @PathParam("userGuid") String userGuid) {
        ListCookies cookie = cookieHandler.parseAndValidate(request);
        FavoriteList result;
        if (killswitchProperties.isMspListEnabled()) {
            result = mspWishlistService.getFavoriteItemsFromDefaultList(userGuid);
        } else {
            result = customerService.getFavItemFromListByGuid(cookie, userGuid);
        }
        return Response.ok(result).build();
    }

    @Path("/favorites/{listGuid}")
    @DELETE
    public Response deleteFavorite(@Context HttpServletRequest request,
                                   @PathParam("listGuid") String listGuid,
                                   @BeanParam ListQueryParam listQueryParam) {

        ListCookies cookie = cookieHandler.parseAndValidate(request);
        if (killswitchProperties.isMspListEnabled()) {
            mspWishlistService.deleteFavoriteItem(listGuid, listQueryParam);
        } else {
            customerService.deleteFavorite(cookie, listGuid, listQueryParam);
        }
        return Response.noContent().build();
    }
}
