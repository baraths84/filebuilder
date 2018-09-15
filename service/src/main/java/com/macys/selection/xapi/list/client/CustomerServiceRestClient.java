package com.macys.selection.xapi.list.client;

import com.macys.selection.xapi.list.client.request.CustomerListMerge;
import com.macys.selection.xapi.list.client.response.ItemDTO;
import com.macys.selection.xapi.list.util.CustomerQueryParameterEnum;
import org.springframework.beans.factory.annotation.Value;
import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxClient;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.client.request.converter.CustomerMSPRequestConverter;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.util.CustomerRequestParamUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * Rest client to talk to services. This class uses JaxRSClient from
 * platform-core which has RxJava and Hystrix support.
 **/
@Component
@PropertySource("classpath:application.properties")
public class CustomerServiceRestClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceRestClient.class);

	@Value("${ALLOW_SECURITY_TOKEN}")
	public Boolean ALLOW_SECURITY_TOKEN = true;

	@Autowired
	@Qualifier("customerListClientPool")
	private RestClientFactory.JaxRSClientPool customerListClientPool;

	@Autowired
	@Qualifier("customerWishlistClientPool")
	private RestClientFactory.JaxRSClientPool wishlistClientPool;

	@Autowired
	private CustomerRequestParamUtil requestParamUtil;

	public RestResponse get(UserQueryParam userQueryParam,
			ListQueryParam listQueryParam,
			PaginationQueryParam paginationQueryParam) {

		Response response = null;
		Map<CustomerQueryParameterEnum, String> queryParamMap = requestParamUtil.createGetListQueryParamMap(userQueryParam, listQueryParam,
				paginationQueryParam);

		try {

			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);
			RxWebTarget rxWebTarget = rxClient
					.target(customerListClientPool.getHostName())
					.path(customerListClientPool.getBasePath());

			setAllUrlParameters(rxWebTarget, queryParamMap);

			response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);

			if (response.getStatus() != Response.Status.OK.getStatusCode()) {
				LOGGER.error("Service errors received from service:{} for getAllLists, userId: {}", response.getStatus(), queryParamMap.get("user"));
			}

			return RestResponseBuilder.buildFromResponse(response);

		} catch (Exception e) {
			LOGGER.error("Exception in getting all lists ", e);
			throw new RestException("Service failure while getting AllLists : " + e.getMessage(), e);
		} finally {
			if (null != response) {
				response.close();
			}
		}

	}

	public RestResponse getListByGuid(String token, String listGuid,
			UserQueryParam userQueryParam, ListQueryParam listQueryParam,
			PaginationQueryParam paginationQueryParam) {

		LOGGER.debug("START :: getListByGuid");

		Response response = null;
		Map<CustomerQueryParameterEnum, String> queryParamMap = requestParamUtil
				.createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);

		try {

			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);
			RxWebTarget rxWebTarget = rxClient
					.target(customerListClientPool.getHostName())
					.path(customerListClientPool.getBasePath()).path(WishlistConstants.PATH_PARAMETER_LIST_GUID)
					.resolveTemplate(WishlistConstants.LIST_GUID, listGuid);
			
			//since MSPCustomerService does not support the filter now, so remove the filter from the query parameters
			queryParamMap.remove(CustomerQueryParameterEnum.FILTER);

			setAllUrlParameters(rxWebTarget, queryParamMap);

			RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);
			decorateWithSecurityHeader(builder, token);
			response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);

			if (response.getStatus() != Response.Status.OK.getStatusCode()) {
				LOGGER.error("Service errors received from service:{} for getListByListGuid, listGuid: {}", response.getStatus(), listGuid);
			}

			return RestResponseBuilder.buildFromResponse(response);

		} catch (Exception e) {
			LOGGER.error("Exception in getting list by list guid ", e);
			throw new RestException("Service failure while getting getListByListGuid : " + e.getMessage(), e);
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	public RestResponse moveItemToWishlist(String token, String listGuid, CustomerList customerList) {

		Response response = null;
		try {
			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);

			RxInvocationBuilder builder = rxClient.target(customerListClientPool.getHostName())
					.path(customerListClientPool.getBasePath())
					.path(listGuid + WishlistConstants.MOVE_ITEM_PATH)
					.queryParam(WishlistConstants.ITEM_GUID, customerList.getWishlist().get(0).getItems().get(0).getItemGuid())
					.queryParam(WishlistConstants.USER_ID, customerList.getUser().getId())
					.queryParam(WishlistConstants.USER_GUID, customerList.getUser().getGuid())
					.request();

			decorateWithSecurityHeader(builder, token);
			response = builder.post(null);

			if (response.getStatus() != WishlistConstants.STATUS_SUCCESS) {
				LOGGER.error("service returned errors with status{} while moving item from  wishlist :: {}  with itemGuid ::  {} ", response.getStatus(), listGuid, customerList.getWishlist().get(0).getItems().get(0).getItemGuid());
			}
			return RestResponseBuilder.buildFromResponse(response);

		} catch (Exception e) {
			LOGGER.error("Exception in moving item to  wishlist ", e);
			throw new RestException("Service failure while moving item to list : " + e.getMessage(), e);
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	public RestResponse delete(String token, String listGuid, String itemGuid) {
		Response response = null;

		try {

			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);
			RxInvocationBuilder builder = rxClient.target(customerListClientPool.getHostName())
					.path(customerListClientPool.getBasePath())
					.path(WishlistConstants.DELETE_ITEM_PATH)
					.resolveTemplate(WishlistConstants.LIST_GUID, listGuid)
					.resolveTemplate(WishlistConstants.ITEM_GUID, itemGuid)
							   .request();
			decorateWithSecurityHeader(builder, token);
			response = builder.delete();

			if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
				LOGGER.error("service returned errors with status{} while deleting item from  wishlist :: {}  with itemGuid ::  {} ", response.getStatus(), listGuid, itemGuid);
			}

			return RestResponseBuilder.buildFromResponse(response);

		} catch (Exception e) {
			LOGGER.error("Exception in deleting item from wishlist", e);
			throw new RestException("Service failure while deleting item : " + e.getMessage(), e);
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	/**
	 * delete list using list guid
	 **/
	public RestResponse deleteList(ListCookies cookie, String listGuid, Long userId) {
		Response response = null;

		try {

			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);
			RxInvocationBuilder builder = rxClient.target(customerListClientPool.getHostName())
					.path(customerListClientPool.getBasePath())
					.path(WishlistConstants.DELETE_LIST_PATH)
					.queryParam(WishlistConstants.USER_ID, userId)
					.resolveTemplate(WishlistConstants.LIST_GUID, listGuid)
							   .request();

			decorateWithSecurityHeader(builder, cookie.getToken());
			response = builder.delete();

			if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
				LOGGER.error("service returned errors with status{} while deleting list :: {} ", response.getStatus(), listGuid);
				throw new RestException("Http error status " + response.getStatus() + " returned from service when deleting list for :: {} " + listGuid);
			}

			// successful response
			return RestResponseBuilder.buildFromResponse(response);

		} catch (Exception e) {
			LOGGER.error("Exception in deleting item from wishlist", e);
			throw new RestException("Service failure while deleting item : " + e.getMessage(), e);
		}
	}

	public RestResponse updateWishlist(ListCookies cookies, String listGuid,
									   UserQueryParam userQueryParam, CustomerList inputJsonObj) {
		Response response = null;
		Map<CustomerQueryParameterEnum, String> queryParamMap = requestParamUtil
				.createGetListQueryParamMap(userQueryParam, null, null);
		try {

			String customerMSPRequestString = CustomerMSPRequestConverter.convert(inputJsonObj);

			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);
			RxWebTarget rxWebTarget = rxClient
					.target(customerListClientPool.getHostName())
					.path(customerListClientPool.getBasePath()).path(WishlistConstants.PATH_PARAMETER_LIST_GUID)
					.resolveTemplate(WishlistConstants.LIST_GUID, listGuid);

			// add all specified query parameters
			setAllUrlParameters(rxWebTarget, queryParamMap);

			RxInvocationBuilder builder = rxWebTarget.request();
			decorateWithSecurityHeader(builder, cookies.getToken());
			response = builder.method("PATCH", Entity.text(customerMSPRequestString));

			if (response.getStatus() != WishlistConstants.STATUS_SUCCESS) {
				LOGGER.error("service returned errors with status{} while updating  wishlist :: {}  with updatedetails ::  {} ", response.getStatus(), listGuid, inputJsonObj);
			}

			return RestResponseBuilder.buildFromResponse(response);

		} catch (Exception e) {
			LOGGER.error("Exception in updating   wishlist ", e);
			throw new RestException("Service failure while updating list : " + e.getMessage(), e);
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	public RestResponse createList(ListCookies cookie, CustomerList inputJsonObj) {

		Response response = null;

		try {
			String customerMSPRequestString = CustomerMSPRequestConverter.convert(inputJsonObj);

			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);

			RxInvocationBuilder builder = rxClient.target(customerListClientPool.getHostName()).path(customerListClientPool.getBasePath())
							   .request(MediaType.APPLICATION_JSON_TYPE);
			decorateWithSecurityHeader(builder, cookie.getToken());
			response = builder.post(Entity.json(customerMSPRequestString));

			if (response.getStatus() != Response.Status.OK.getStatusCode()) {
				LOGGER.error("Fail to create wishlist: {}, response status is: {}", inputJsonObj, response.getStatus());
			}

			return RestResponseBuilder.buildFromResponse(response);
		} catch (Exception e) {
			LOGGER.error("Exception in creating wishlist", e);
			throw new RestException("Exception in creating wishlist: " + e.getMessage());
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	/**
	 * Email share should always pass the security token.
	 * */
	public RestResponse emailShareWishlist(ListCookies cookie, String listGuid, EmailShare inputJsonObj) {
		Response response = null;

		try {
			String customerMSPRequestString = CustomerMSPRequestConverter.convertEmailShare(inputJsonObj);

			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);

			RxInvocationBuilder builder = rxClient.target(customerListClientPool.getHostName())
					.path(customerListClientPool.getBasePath()).path(WishlistConstants.SHARE_EMAIL_PATH_LIST_GUID)
					.resolveTemplate(WishlistConstants.LIST_GUID, listGuid)
							   .request(MediaType.APPLICATION_JSON_TYPE).header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, cookie.getToken());
			response = builder.post(Entity.json(customerMSPRequestString));

			if (response.getStatus() != Response.Status.OK.getStatusCode()) {
				LOGGER.error("Fail to email share wishlist: {}, response status is: {}", inputJsonObj, response.getStatus());
			}
			return RestResponseBuilder.buildFromResponse(response);
		} catch (Exception e) {
			LOGGER.error("Exception in email sharing wishlist", e);
			throw new RestException("Exception in email sharing wishlist: ", e);
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	public RestResponse addToDefaultWishlist(CustomerList inputJsonObj) {
		Response response = null;

		try {

			String customerMSPRequestString = CustomerMSPRequestConverter.convert(inputJsonObj);

			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);

			response = rxClient.target(customerListClientPool.getHostName())
					.path(customerListClientPool.getBasePath())
					.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(customerMSPRequestString));

			if (response.getStatus() != Response.Status.OK.getStatusCode()) {
				LOGGER.error("Fail to add item default wishlist by UPC ID: {}, response status is: {}", inputJsonObj, response.getStatus());
			}
			return RestResponseBuilder.buildFromResponse(response);
		} catch (Exception e) {
			LOGGER.error("Exception adding UPC ID to default wishlist", e);
			throw new RestException("Exception adding UPC ID to default wishlist", e);
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	public RestResponse addItemToGivenListByUPC(ListCookies cookie, UserQueryParam userQueryParam, String listGuid, List<Item> itemList) {
		Response response = null;
		Map<CustomerQueryParameterEnum, String> queryParamMap = requestParamUtil.createGetListQueryParamMap(userQueryParam, null, null);

		try {

			String customerMSPRequestString = CustomerMSPRequestConverter.convert(itemList);
			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);
			RxWebTarget rxWebTarget = rxClient.target(customerListClientPool.getHostName())
					.path(customerListClientPool.getBasePath()).path(WishlistConstants.ADD_TO_GIVEN_LIST_BY_UPC_PATH)
					.resolveTemplate(WishlistConstants.LIST_GUID, listGuid);

			setAllUrlParameters(rxWebTarget, queryParamMap);
			RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);
			decorateWithSecurityHeader(builder, cookie.getToken());
			response = builder.post(Entity.json(customerMSPRequestString));

			if (response.getStatus() != Response.Status.OK.getStatusCode()) {
				LOGGER.error("Fail to add item to a given wishlist by {}, response status is: {}", itemList, response.getStatus());
			}
			return RestResponseBuilder.buildFromResponse(response);
		} catch (Exception e) {
			LOGGER.error("Exception adding to a given wishlist by UPC: {}", e);
			throw new RestException("Failed on adding to a given wishlist by UPC", e);
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	/*
	 * to collect all the url parameters from UI and append them to the call to CustomerMSP
	 * @param rxWebTarget RxWebTarget
	 * @param queryParamMap  Map<CustomerQueryParameterEnum, String>
	 */
	private void setAllUrlParameters(RxWebTarget rxWebTarget, Map<CustomerQueryParameterEnum, String> queryParamMap) {
		// add all specified query parameters
		if (MapUtils.isNotEmpty(queryParamMap)) {
			for (Entry<CustomerQueryParameterEnum, String> queryParameter : queryParamMap
					.entrySet()) {
				rxWebTarget.queryParam(
						queryParameter.getKey().getParamName(),
						queryParameter.getValue());
			}
		}
	}

	public RestResponse deleteFavorite(ListCookies cookie, String listGuid, ListQueryParam listQueryParam) {
		Response response = null;

		Map<CustomerQueryParameterEnum, String> queryParamMap = requestParamUtil.createGetListQueryParamMap(null, listQueryParam, null);

		try {

			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);

			RxWebTarget rxWebTarget = rxClient.target(customerListClientPool.getHostName())
					.path(WishlistConstants.DELETE_FAVORITE)
					.resolveTemplate(WishlistConstants.LIST_GUID, listGuid);

			setAllUrlParameters(rxWebTarget, queryParamMap);

			RxInvocationBuilder builder = rxWebTarget.request();
			decorateWithSecurityHeader(builder, cookie.getToken());
			response = builder.delete();

			if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
				LOGGER.error("service returned errors with status{} while deleting favorite from wishlist :: {}  with product ID ::  {} ", response.getStatus(), listGuid, listQueryParam);
			}
			return RestResponseBuilder.buildFromResponse(response);

		} catch (Exception e) {
			LOGGER.error("Exception in deleting favorite from wishlist", e);
			throw new RestException("Service failure while deleting favorite : " + e.getMessage(), e);
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	public RestResponse addFavItemToGivenListByPID(ListCookies cookie, CustomerList inputJsonObj) {
		Response response = null;

		try {

			String customerMSPRequestString = CustomerMSPRequestConverter.convert(inputJsonObj);


			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);


			RxWebTarget rxWebTarget = rxClient.target(customerListClientPool.getHostName())
					.path(WishlistConstants.ADD_FAV_TO_GIVEN_LIST_BY_PID_PATH);


			if (inputJsonObj.getUser() != null && StringUtils.isNotEmpty(inputJsonObj.getUser().getGuid())) {

				rxWebTarget = rxWebTarget
						.queryParam(WishlistConstants.USER_GUID, inputJsonObj.getUser().getGuid());
			}

			RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);
			decorateWithSecurityHeader(builder, cookie.getToken());
			response = builder.post(Entity.json(customerMSPRequestString));

			if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
				LOGGER.error("Fail to add item to a given wishlist by {}, response status is: {}", inputJsonObj, response.getStatus());
			}
			return RestResponseBuilder.buildFromResponse(response);
		} catch (Exception e) {
			LOGGER.error("Exception adding to a given wishlist by UPC: {}", e);
			throw new RestException("Failed on adding to a given wishlist by UPC", e);
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	public RestResponse getFavItemFromListByGuid(ListCookies cookie, String userGuid) {

		LOGGER.debug("START :: getFavListByGuid");

		Response response = null;

		try {

			RxClient rxClient = customerListClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);
			RxWebTarget rxWebTarget = rxClient
					.target(customerListClientPool.getHostName())
					.path(WishlistConstants.ADD_FAV_TO_GIVEN_LIST_BY_PID_PATH)
					.queryParam(WishlistConstants.USER_GUID, userGuid);

			RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);
			decorateWithSecurityHeader(builder, cookie.getToken());
			response = builder.get(Response.class);

			if (response.getStatus() != Response.Status.OK.getStatusCode()) {
				LOGGER.error("Service errors received from service:{} for getFavListByGuid", response.getStatus(), userGuid);
			}

			return RestResponseBuilder.buildFromResponse(response);

		} catch (Exception e) {
			LOGGER.error("Exception in getting fav list by guid ", e);
			throw new RestException("Service failure while getting getFavListByGuid : " + e.getMessage(), e);
		} finally {
			if (null != response) {
				response.close();
			}
		}
	}

	public void decorateWithSecurityHeader(RxInvocationBuilder builder, String token) {
		if(ALLOW_SECURITY_TOKEN) {
			builder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, token);
		}
	}

	public Boolean getALLOW_SECURITY_TOKEN() {
		return ALLOW_SECURITY_TOKEN;
	}

	public void setALLOW_SECURITY_TOKEN(Boolean ALLOW_SECURITY_TOKEN) {
		this.ALLOW_SECURITY_TOKEN = ALLOW_SECURITY_TOKEN;
	}

	public RestResponse mergeList(CustomerListMerge listMerge) {

		Response response = null;
		try {
			RxClient rxClient = wishlistClientPool.getRxClient(WishlistConstants.CUSTOMER_WISH_LIST_CLIENT_POOL);
			String customerMSPRequestString = CustomerMSPRequestConverter.convertListMerge(listMerge);

			RxInvocationBuilder builder = rxClient.target(wishlistClientPool.getHostName())
					.path(wishlistClientPool.getBasePath())
					.path(WishlistConstants.MERGE_LIST_PATH)
					.request();

			response = builder.post(Entity.json(customerMSPRequestString));

			if (response.getStatus() != WishlistConstants.STATUS_SUCCESS) {
				LOGGER.error("service returned errors with status{} while merging lists guestUserId :: {}  and userId ::  {} ",
						response.getStatus(), listMerge.getGuestUserId(), listMerge.getUserId());
			}
			return RestResponseBuilder.buildFromResponse(response);

		} catch (Exception e) {
			LOGGER.error("Exception in merging lists ", e);
			throw new RestException("Service failure while merging lists : " + e.getMessage(), e);
		} finally {
			if (Objects.nonNull(response)) {
				response.close();
			}
		}
	}

	public RestResponse updateItemPriority(String listGuid, String itemGuid, Long userId, String userGuid, ItemDTO itemRequest) {
		Response response = null;
		try {
			RxClient rxClient = wishlistClientPool.getRxClient(WishlistConstants.CUSTOMER_LIST_CLIENT_POOL);
			String item = CustomerMSPRequestConverter.convertItem(itemRequest);
			RxInvocationBuilder builder = rxClient.target(customerListClientPool.getHostName())
					.path(customerListClientPool.getBasePath())
					.path(WishlistConstants.UPDATE_ITEM_PRIORITY_PATH)
					.resolveTemplate(WishlistConstants.LIST_GUID, listGuid)
					.resolveTemplate(WishlistConstants.ITEM_GUID, itemGuid)
					.queryParam(WishlistConstants.USER_ID, userId)
					.queryParam(WishlistConstants.USER_GUID, userGuid)
					.request();

			response = builder.buildPatch(Entity.json(item)).invoke();

			if (response.getStatus() != WishlistConstants.STATUS_SUCCESS) {
				LOGGER.error("service returned errors with status {} while updating item priority listGuid :: {} and itemGuid :: {} priority :: {} ",
						response.getStatus(), listGuid, itemGuid, itemRequest.getPriority());
			}
			return RestResponseBuilder.buildFromResponse(response);
		} catch (Exception e) {
			LOGGER.error("Exception in update item priority ", e);
			throw new RestException("Service failure while updating item priority : " + e.getMessage(), e);
		} finally {
			Optional.ofNullable(response).ifPresent(Response::close);
		}
	}

}
