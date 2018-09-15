package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.platform.rest.core.RequestContext;
import com.macys.platform.rest.core.annotation.PATCH;
import com.macys.selection.xapi.list.client.request.CustomerListMerge;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.ErrorConstants;
import com.macys.selection.xapi.list.exception.ListWebApplicationException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.CookieHandler;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.request.validator.RequestValidators;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.services.CustomerService;
import com.macys.selection.xapi.list.services.CustomerUserService;
import com.macys.selection.xapi.list.services.PromotionService;
import com.macys.selection.xapi.list.services.WishlistService;
import com.macys.selection.xapi.list.util.CustomerRequestParamUtil;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import com.macys.selection.xapi.list.util.ListUtil;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author m785440
 *         <p>
 *         Customer list resource for adding, deleting, and updating item from with-in a customer wishlist.
 *         CustomerListRerouces comments.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
@Path("/wishlist/v1/lists")
@Api(value = "List API's")
public class CustomerListResource {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private PromotionService promotionService;

	@Autowired
	private KillSwitchPropertiesBean killswitchProperties;

	@Autowired
	private CookieHandler cookieHandler;

	@Autowired
	private RequestValidators<CustomerList, Boolean> customerListItemExistsRequestValidator;

	@Autowired
	private WishlistService mspWishlistService;

	@Autowired
	private CustomerUserService customerUserService;

	@Autowired
	private CustomerRequestParamUtil customerRequestParamUtil;

	@Value("${application.name}")
	private String applicationName;

	@Value("#{'${application.name}' == 'BCOM'}")
	private boolean isBCOM;

	@Value("${responsive.shareemail.enabled}")
	private boolean responsiveShareEmailEnabled;



	@ApiOperation(value = "Retrieves a customer list passing-in query parameters to filter out the list see list of optional query parameters." +
			"Supports find list by first and last name, see query parameters", response = CustomerList.class)
	@GET
	public Response getCustomerList(@ApiParam(value = "user query parameter", required = false) @BeanParam UserQueryParam userQueryParam,
									@ApiParam(value = "user query parameter", required = false) @BeanParam ListQueryParam listQueryParam,
									@ApiParam(value = "pagination query parameter", required = false) @BeanParam PaginationQueryParam paginationQueryParam,
									@Context RequestContext restContext) {

		CustomerList response;

		if (killswitchProperties.isMspListEnabled()) {
			if (isFindList(listQueryParam.getFirstName(), listQueryParam.getLastName(), listQueryParam.getState())) {
				if (killswitchProperties.isSeparateFindUsersEnabled()) {
					response = mspWishlistService.findList(userQueryParam, listQueryParam, paginationQueryParam);
				} else {
					response = customerService.getCustomerList(userQueryParam, listQueryParam, paginationQueryParam);
				}
			} else {
				response = mspWishlistService.getList(userQueryParam, listQueryParam, paginationQueryParam, ListUtil.getUriHost(restContext));
			}
		} else {
			response = customerService.getCustomerList(userQueryParam, listQueryParam, paginationQueryParam);
		}

		if (response != null) {
			response.setKillswitches(ListUtil.updateProperties(killswitchProperties));
			if(isBCOM) {
				List<WishList> wishlists = new ArrayList<>();
				if(!CollectionUtils.isEmpty(response.getWishlist())) {
					for (WishList list : response.getWishlist()) {
						WishList wishlistWithPromotion = promotionService.getPromotions(list, listQueryParam);
						wishlists.add(wishlistWithPromotion);
					}
					response.setWishlist(wishlists);
				}
			}
		}
		return Response.ok(response).build();
	}

	private boolean isFindList(String firstName, String lastName, String state) {
		return (firstName != null || lastName != null || state != null);
	}

	@Path("/{listGuid}")
	@GET
	public Response getCustomerListByGuid(@Context HttpServletRequest request,
										  @PathParam("listGuid") String listGuid, @BeanParam ListQueryParam listQueryParam,
										  @BeanParam PaginationQueryParam paginationQueryParam,
										  @Context RequestContext restContext) {

		// security token validation
		ListCookies cookie = cookieHandler.parseAndValidate(request);
		String token = cookie.getToken();

		List<WishList> wishlists = new ArrayList<>();
		CustomerList response;
		if (killswitchProperties.isMspListEnabled()) {
			response = mspWishlistService.getListByGuid(listGuid, listQueryParam, paginationQueryParam, ListUtil.getUriHost(restContext));
		} else {
			response = customerService.getCustomerListByGuid(token, listGuid, listQueryParam, paginationQueryParam);
		}

		WishList wishlistWithPromotion = promotionService.getPromotions(response.getWishlist().get(0), listQueryParam);
		wishlists.add(wishlistWithPromotion);
		response.setWishlist(wishlists);

		if (isBCOM) {
			// for Bcom there are some more filters onSale,promotions,justfewleft based on which the response need to be sent.
			if (listQueryParam != null && !StringUtils.isEmpty(listQueryParam.getFilter())
					&& (listQueryParam.getFilter().contains(WishlistConstants.ON_SALE_FILTER) || listQueryParam.getFilter().contains(WishlistConstants.PROMOTIONS_FILTER))) {
				String[] tokens = StringUtils.commaDelimitedListToStringArray(listQueryParam.getFilter());
				//modifying the response based on filter options
				customerRequestParamUtil.buildResponseWithFilterOptions(response, tokens);
			}
		}

		response.setKillswitches(ListUtil.updateProperties(killswitchProperties));

		return Response.ok(response).build();
	}

	@Path("/{listGuid}/item/move")
	@PUT
	public Response moveItemToWishlist(@Context HttpServletRequest request,
									   @PathParam("listGuid") String listGuid, CustomerList customerList) {
		// security token validation
		ListCookies cookie = cookieHandler.parseAndValidate(request);
		if (killswitchProperties.isMspListEnabled()) {
            if (!customerListItemExistsRequestValidator.isValid(customerList)) {
                throw new ListWebApplicationException(ErrorConstants.BAD_JASON_INPUT_ITMES_NOT_AVAILABLE, Response.Status.BAD_REQUEST.getStatusCode());
            }
           	mspWishlistService.moveItemToWishlist(cookie.getToken(), listGuid, customerList);
		} else {
			customerService.moveItemToWishlist(cookie.getToken(), listGuid, customerList);
		}
		return Response.noContent().build();
	}

	@Path("/{listGuid}/items/{itemGuid}")
	@DELETE
	public Response deleteItem(@Context HttpServletRequest request,
							   @PathParam("listGuid") String listGuid, @PathParam("itemGuid") String itemGuid) {

		// security token validation
		ListCookies cookie = cookieHandler.parseAndValidate(request);
		if (killswitchProperties.isMspListEnabled()) {
			mspWishlistService.deleteItem(listGuid, itemGuid);
		} else {
			customerService.deleteItem(cookie.getToken(), listGuid, itemGuid);
		}
		return Response.noContent().build();
	}

	@Path("/{listGuid}")
	@PUT
	public Response manageWishlist(@Context HttpServletRequest request,
								   @PathParam("listGuid") String listGuid, @BeanParam UserQueryParam userQueryParam, CustomerList inputJsonObj) {
		ListCookies cookie = cookieHandler.parseAndValidate(request);
		if (killswitchProperties.isMspListEnabled()) {
			mspWishlistService.updateList(cookie.getToken(), listGuid, userQueryParam, inputJsonObj);
		} else {
			customerService.updateWishlist(cookie, listGuid, userQueryParam, inputJsonObj);
		}
		return Response.noContent().build();
	}

	@Path("/{listGuid}")
	@PATCH
	public Response manageWishlistByPatch(@Context HttpServletRequest request, @PathParam("listGuid") String listGuid,
										  @BeanParam UserQueryParam userQueryParam, CustomerList inputJsonObj) {
		ListCookies cookie = cookieHandler.parseAndValidate(request);
		if (killswitchProperties.isMspListEnabled()) {
			mspWishlistService.updateList(cookie.getToken(), listGuid, userQueryParam, inputJsonObj);
		} else {
			customerService.updateWishlist(cookie, listGuid, userQueryParam, inputJsonObj);
		}
		return Response.noContent().build();
	}

	@Path("/{listGuid}/shareemail")
	@POST
	public Response emailShareWishlist(@Context HttpServletRequest request,
									   @PathParam("listGuid") String listGuid,
									   EmailShare inputJsonObj ) {
		// security token validation
		ListCookies cookie = cookieHandler.parseAndValidate(request);
		if (killswitchProperties.isMspListEnabled() && responsiveShareEmailEnabled) {
			mspWishlistService.emailShareWishlist(cookie.getToken(), listGuid, inputJsonObj);
		} else {
			customerService.emailShareWishlist(cookie, listGuid, inputJsonObj);
		}
		return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
	}

	@POST
	public Response createWishList(@Context HttpServletRequest request, CustomerList inputJsonObj) {
		// security token validation
		ListCookies cookie = cookieHandler.parseAndValidate(request);
		CustomerList result;
		if (killswitchProperties.isMspListEnabled()) {
            result = mspWishlistService.createWishList(cookie.getToken(), inputJsonObj);
        } else {
            result = customerService.createWishList(cookie, inputJsonObj);
        }
		return Response.ok(result).build();

	}
	/**
	 *	Not protected using security token
	 * */
	@Path("/items")
	@POST
	public Response addToDefaultWishlist(@Valid @Nonnull CustomerList customerList) {
		if (!customerListItemExistsRequestValidator.isValid(customerList)) {
			throw new ListWebApplicationException(ErrorConstants.BAD_JASON_INPUT_ITMES_NOT_AVAILABLE, Response.Status.BAD_REQUEST.getStatusCode());
		}
		CustomerList result;
		if (killswitchProperties.isMspListEnabled()) {
			result = mspWishlistService.addItemToDefaultWishlist(customerList);
		} else {
			result = customerService.addToDefaultWishlist(customerList);
		}
		return Response.ok(result).build();
	}

	/**
	 * To add item to a given wishlist by a listGuid in the url
	 *
	 * @param userQueryParam userId or userGuid
	 * @param listGuid       list guid
	 * @param customerList   The Json format of CustomerList. At least one item is required.
	 * @return Json string (CustomerList)
	 */
	@Path("/{listGuid}/items")
	@POST
	public Response addItemToGivenListByUPC(@Context HttpServletRequest request,
											@BeanParam UserQueryParam userQueryParam,
											@PathParam("listGuid") String listGuid,
											@Valid @Nonnull CustomerList customerList) {
		if (!customerListItemExistsRequestValidator.isValid(customerList)) {
			throw new ListWebApplicationException(ErrorConstants.BAD_JASON_INPUT_ITMES_NOT_AVAILABLE, Response.Status.BAD_REQUEST.getStatusCode());
		}
		// security token validation
		ListCookies cookie = cookieHandler.parseAndValidate(request);
		CustomerList result;
		if (killswitchProperties.isMspListEnabled()) {
			result = mspWishlistService.addItemToGivenList(listGuid, customerList, userQueryParam);
		} else {
			// security token validation
			result = customerService.addItemToGivenListByUPC(cookie, userQueryParam, listGuid, customerList);
		}
		return Response.ok(result).build();
	}

	@ApiOperation(value = "Deletes a customer list using listGuid", notes = "The user id should be passed-in as a body")
	@ApiResponses(value = {@ApiResponse(code = 204, message = "")})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "User ID", required = true, dataType = "com.macys.selection.xapi.list.rest.response.User", paramType = "body")
	})
	@Path("/{listGuid}")
	@DELETE
	public Response deleteList(@Context HttpServletRequest request,
							   @BeanParam UserQueryParam userQueryParam,
							   @ApiParam(value = "list guid for customer list", required = true) @PathParam("listGuid") String listGuid) {
		ListCookies cookie = cookieHandler.parseAndValidate(request);
		if (killswitchProperties.isMspListEnabled()) {
			mspWishlistService.deleteList(cookie.getToken(), listGuid, userQueryParam);
		} else {
			if (userQueryParam == null || userQueryParam.getUserId() == null) {
				throw new ListWebApplicationException(ErrorConstants.USERID_IS_NULL, Response.Status.BAD_REQUEST.getStatusCode());
			}
			customerService.deleteList(cookie, listGuid, userQueryParam.getUserId());
		}
		return Response.noContent().build();
	}

	@Path("/merge")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response mergeList(@Context HttpServletRequest request,
							  @Context RequestContext restContext,
							  CustomerListMerge listMerge) {
		ListCookies cookie = cookieHandler.parseAndValidate(request);
		if (killswitchProperties.isMspListEnabled()) {
			mspWishlistService.mergeList(cookie.getToken(), listMerge);
		} else {
			customerService.mergeList(listMerge);
		}
		return Response.ok().build();
	}

	@Path("/{listGuid}/items/{itemGuid}")
	@PATCH
	public Response updateItemPriority(@Context HttpServletRequest request,
									   @Context RequestContext restContext,
									   @PathParam("listGuid") String listGuid,
									   @PathParam("itemGuid") String itemGuid,
									   @QueryParam("userId") Long userId,
									   @QueryParam("userGuid") String userGuid,
									   Item item) {
		ListCookies cookie = cookieHandler.parseAndValidate(request);
		if (killswitchProperties.isMspListEnabled()) {
			mspWishlistService.updateItemPriority(cookie.getToken(), listGuid, itemGuid, userId, userGuid, item);
		} else {
			customerService.updateItemPriority(listGuid, itemGuid, userId, userGuid, item);
		}
		return Response.ok().build();
	}
}