package com.macys.selection.xapi.list.client;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxClient;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.selection.xapi.list.client.request.MergeListRequest;
import com.macys.selection.xapi.list.client.request.converter.CustomerMSPRequestConverter;
import com.macys.selection.xapi.list.client.request.converter.ListRequestConverter;
import com.macys.selection.xapi.list.client.response.CollaboratorDTO;
import com.macys.selection.xapi.list.client.response.EmailShareDTO;
import com.macys.selection.xapi.list.client.response.ItemDTO;
import com.macys.selection.xapi.list.client.response.UserAvatarDTO;
import com.macys.selection.xapi.list.client.response.WishListDTO;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.SortQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.util.ListQueryParameterEnum;
import com.macys.selection.xapi.list.util.ListRequestParamUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

@Component
public class ListRestClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListRestClient.class);
    private Boolean allowSecurityToken;
    private final RestClientFactory.JaxRSClientPool listClientPool;
    private final ListRequestParamUtil requestParamUtil;

    @Autowired
    public ListRestClient(@Value("${msp.wishlist.security.enabled}") Boolean allowSecurityToken,
                          @Qualifier(WishlistConstants.LIST_CLIENT_POOL) RestClientFactory.JaxRSClientPool listClientPool,
                          ListRequestParamUtil requestParamUtil) {
        this.allowSecurityToken = allowSecurityToken;
        this.listClientPool = listClientPool;
        this.requestParamUtil = requestParamUtil;
    }

    public RestResponse getList(UserQueryParam userQueryParam, ListQueryParam listQueryParam,
                                PaginationQueryParam paginationQueryParam) {
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient
                    .target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH);

            requestParamUtil.addQueryParams(rxWebTarget, listQueryParam, userQueryParam, paginationQueryParam);

            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Service errors received from service:{} for getAllLists, userId: {}", response.getStatus(), userQueryParam.getUserId());
            }

            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception in getting all lists ", e);
            throw new RestException("Service failure while getting AllLists : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }

    }

    public RestResponse getUserCollaborativeLists(String userGuid, Integer listLimit, Integer collabLimit, boolean owner) {
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient
                    .target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.COLLABORATORS)
                    .path(owner ? WishlistConstants.OWNER : WishlistConstants.COLLABORATOR);

            requestParamUtil.addIfNotNull(rxWebTarget, ListQueryParameterEnum.USER_GUID, userGuid);
            requestParamUtil.addIfNotNull(rxWebTarget, ListQueryParameterEnum.LIST_LIMIT, listLimit);
            requestParamUtil.addIfNotNull(rxWebTarget, ListQueryParameterEnum.COLLAB_LIMIT, collabLimit);
            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Service errors received from service:{} for getUserListsByTypes, userGuid: {}", response.getStatus(), userGuid);
            }

            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception in getting user lists by types ", e);
            throw new RestException("Service failure while getting user lists by types: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse getListByGuid(String listGuid, ListQueryParam listQueryParam,
                                      PaginationQueryParam paginationQueryParam) {
        LOGGER.debug("START :: getListByGuid");
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient
                    .target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH)
                    .path(WishlistConstants.PATH_PARAMETER_LIST_GUID)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid);

            requestParamUtil.addQueryParams(rxWebTarget, listQueryParam, null, paginationQueryParam);

            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Service errors received from service:{} for getListByListGuid, listGuid: {}", response.getStatus(), listGuid);
            }
            return RestResponseBuilder.buildFromResponse(response);

        } catch (Exception e) {
            LOGGER.error("Exception in getting list by list guid ", e);
            throw new RestException("Service failure while getting getListByListGuid : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    private void decorateWithSecurityHeaderIfEnabled(RxInvocationBuilder builder, String token) {
        if (allowSecurityToken) {
            decorateWithSecurityHeader(builder, token);
        }
    }

    private void decorateWithSecurityHeader(RxInvocationBuilder builder, String token) {
        builder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, token);
    }

    public RestResponse deleteList(String token, String listGuid, UserQueryParam userQueryParam) {
        Response response = null;

        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH)
                    .path(WishlistConstants.PATH_PARAMETER_LIST_GUID)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid);

            requestParamUtil.addUserQueryParam(rxWebTarget, userQueryParam);

            RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);
            decorateWithSecurityHeaderIfEnabled(builder, token);
            response = builder.delete();
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            throw new RestException("Service failure while deleting list: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse updateWishlist(String token, String listGuid, UserQueryParam userQueryParam, CustomerList inputJsonObj) {
        Response response = null;
        try {
            String customerMSPRequestString = CustomerMSPRequestConverter.convert(inputJsonObj);

            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient
                    .target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH)
                    .path(WishlistConstants.PATH_PARAMETER_LIST_GUID)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid);

            requestParamUtil.addUserQueryParam(rxWebTarget, userQueryParam);

            RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);
            decorateWithSecurityHeaderIfEnabled(builder, token);
            response = builder.method("PATCH", Entity.text(customerMSPRequestString));

            return RestResponseBuilder.buildFromResponse(response);

        } catch (Exception e) {
            throw new RestException("Service failure while updating list: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse deleteItem(String listGuid, String itemGuid, Boolean guestUser) {
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxInvocationBuilder builder = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH)
                    .path(WishlistConstants.DELETE_ITEM_PATH)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid)
                    .resolveTemplate(WishlistConstants.ITEM_GUID, itemGuid)
                    .queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), guestUser)
                    .request();
            response = builder.delete();

            if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
                LOGGER.error("service returned errors with status{} while deleting item from  wishlist :: {}  with itemGuid ::  {} ", response.getStatus(), listGuid, itemGuid);
            }

            return RestResponseBuilder.buildFromResponse(response);

        } catch (Exception e) {
            LOGGER.error("Exception in deleting item from wishlist", e);
            throw new RestException("Service failure while deleting item : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse deleteFavoriteItem(String listGuid, Boolean guestUser, ListQueryParam listQueryParam) {
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);

            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.FAVORITES_PATH)
                    .path(WishlistConstants.PATH_PARAMETER_LIST_GUID)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid)
                    .queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), guestUser);

            requestParamUtil.addListQueryParam(rxWebTarget, listQueryParam);

            RxInvocationBuilder builder = rxWebTarget.request();
            response = builder.delete();

            if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
                LOGGER.error("service returned errors with status {} while deleting favorite from wishlist :: {}  with product ID ::  {} upc ID :: {}",
                        response.getStatus(), listGuid, listQueryParam.getProductId(), listQueryParam.getUpcId());
            }
            return RestResponseBuilder.buildFromResponse(response);

        } catch (Exception e) {
            LOGGER.error("Exception in deleting favorite from wishlist", e);
            throw new RestException("Service failure while deleting favorite : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse updateItemPriority(String token, String listGuid, String itemGuid, Long userId, String userGuid,
                                           Boolean guestUser, ItemDTO itemRequest) {
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            String item = CustomerMSPRequestConverter.convertItem(itemRequest);
            RxInvocationBuilder builder = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH)
                    .path(WishlistConstants.UPDATE_ITEM_PRIORITY_PATH)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid)
                    .resolveTemplate(WishlistConstants.ITEM_GUID, itemGuid)
                    .queryParam(ListQueryParameterEnum.USER_ID.getParamName(), userId)
                    .queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), userGuid)
                    .queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), guestUser)
                    .request();
            decorateWithSecurityHeaderIfEnabled(builder, token);
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

    public RestResponse moveItemToWishlist(String token, String listGuid, String itemGuid, UserQueryParam userQueryParam) {

        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);

            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH)
                    .path(listGuid + WishlistConstants.MOVE_ITEM_PATH)
                    .queryParam(WishlistConstants.ITEM_GUID, itemGuid);

            requestParamUtil.addUserQueryParam(rxWebTarget, userQueryParam);

            RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);

            decorateWithSecurityHeaderIfEnabled(builder, token);

            response = builder.post(null);

            if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
                LOGGER.error("service returned errors with status{} while moving item from  wishlist :: {}  with itemGuid ::  {} ",
                        response.getStatus(), listGuid, itemGuid);
            }
            return RestResponseBuilder.buildFromResponse(response);

        } catch (Exception e) {
            LOGGER.error("Exception in moving item to  wishlist ", e);
            throw new RestException("Service failure while moving item to list: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse createList(String token, WishListDTO wishListDTO, UserQueryParam userQueryParam) {

        Response response = null;

        try {
            String wishlistJson = ListRequestConverter.convert(wishListDTO);
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);

            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH);

            requestParamUtil.addUserQueryParam(rxWebTarget, userQueryParam);

            RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);

            decorateWithSecurityHeaderIfEnabled(builder, token);

            response = builder.post(Entity.json(wishlistJson));

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Fail to create wishlist: {}, response status is: {}", wishListDTO, response.getStatus());
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception in creating wishlist", e);
            throw new RestException("Exception in creating wishlist: " + e.getMessage());
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse addItemToDefaultWishlist(List<ItemDTO> itemList, UserQueryParam userQueryParam, Boolean onSaleNotify) {
        Response response = null;
        try {
            String itemJson = ListRequestConverter.convert(itemList);
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);

            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH)
                    .path(WishlistConstants.ADD_TO_DEFAULT_LIST_PATH);

            requestParamUtil.addUserQueryParam(rxWebTarget, userQueryParam);
            rxWebTarget.queryParam(ListQueryParameterEnum.ON_SALE_NOTIFY.getParamName(), BooleanUtils.toStringTrueFalse(onSaleNotify));

            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json(itemJson));

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Fail to add item default wishlist by UPC ID: {}, response status is: {}", itemList, response.getStatus());
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            throw new RestException("Service failure while adding item to default wishlist: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse addItemToGivenList(String listGuid, List<ItemDTO> itemList, UserQueryParam userQueryParam) {
        Response response = null;
        try {
            String itemListJson = ListRequestConverter.convert(itemList);
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH)
                    .path(WishlistConstants.ADD_TO_GIVEN_LIST_BY_UPC_PATH)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid);

            requestParamUtil.addUserQueryParam(rxWebTarget, userQueryParam);
            RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);

            response = builder.post(Entity.json(itemListJson));

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Fail to add item to a given wishlist by {}, response status is: {}", itemList, response.getStatus());
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            throw new RestException("Failed on adding to a given wishlist by UPC: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse getFavoriteItemsFromDefaultList(Long userId) {
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient
                    .target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.FAVORITES_PATH)
                    .queryParam(ListQueryParameterEnum.USER_ID.getParamName(), userId);

            RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);
            response = builder.get(Response.class);

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Service errors received from service: {} for getFavoriteItemsFromDefaultList :: userId :: {}",
                        response.getStatus(), userId);
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception in getting getFavoriteItemsFromDefaultList :: userId {}", userId, e);
            throw new RestException("Service failure while getting getFavoriteItemsFromDefaultList : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    /**
     * Email share should always pass the security token.
     */
    public RestResponse emailShareWishlist(String token, String listGuid, EmailShareDTO inputJsonObj) {
        Response response = null;
        try {
            String emailShareJson = ListRequestConverter.convert(inputJsonObj);
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);

            RxInvocationBuilder builder = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH)
                    .path(WishlistConstants.SHARE_EMAIL_PATH_LIST_GUID)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid)
                    .request(MediaType.APPLICATION_JSON_TYPE);

            decorateWithSecurityHeader(builder, token);

            response = builder.post(Entity.json(emailShareJson));

            if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
                LOGGER.error("Fail to email share wishlist: {}, response status is: {}", inputJsonObj, response.getStatus());
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception in email sharing wishlist", e);
            throw new RestException("Exception in email sharing wishlist: ", e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse mergeList(String token, MergeListRequest mergeRequest) {
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            String mergeRequestJson = ListRequestConverter.convert(mergeRequest);

            RxInvocationBuilder builder = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH)
                    .path(WishlistConstants.MERGE_LIST_PATH)
                    .request();

            decorateWithSecurityHeaderIfEnabled(builder, token);
            response = builder.post(Entity.json(mergeRequestJson));

            if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
                LOGGER.error("service returned errors with status{} while merging lists guestUserId :: {}  and userId ::  {} ",
                        response.getStatus(), mergeRequest.getSrcUserId(), mergeRequest.getTargetUserId());
            }
            return RestResponseBuilder.buildFromResponse(response);

        } catch (Exception e) {
            LOGGER.error("Exception in merging lists ", e);
            throw new RestException("Service failure while merging lists : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse getCollaboratorsByGuid(String listGuid) {
        LOGGER.debug("START :: getCollaboratorsByGuid");
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient
                    .target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.COLLABORATORS)
                    .path(WishlistConstants.PATH_PARAMETER_LIST_GUID)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid);

            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Service errors received from service:{} for getCollaboratorsByGuid, listGuid: {}", response.getStatus(), listGuid);
            }
            return RestResponseBuilder.buildFromResponse(response);

        } catch (Exception e) {
            LOGGER.error("Exception in getting collaborators by list guid ", e);
            throw new RestException("Service failure while collaborators getListByListGuid : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse getApprovals(UserQueryParam userQueryParam, PaginationQueryParam paginationQueryParam) {
        LOGGER.debug("START :: getApprovals");
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient
                .target(listClientPool.getHostName())
                .path(listClientPool.getBasePath())
                .path(WishlistConstants.COLLABORATORS)
                .path(WishlistConstants.APPROVALS);
            requestParamUtil.addUserQueryParam(rxWebTarget, userQueryParam);
            requestParamUtil.addPaginationQueryParam(rxWebTarget, paginationQueryParam);

            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);

            return RestResponseBuilder.buildFromResponse(
                response,
                Response.Status.OK,
                printLoggerError("Service errors received from service:{} for getApprovals", response.getStatus())
            );
        } catch (ListServiceException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exception in getting collaborators by list guid ", e);
            throw new RestException("Service failure while collaborators getApprovals : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse getRequests(UserQueryParam userQueryParam, PaginationQueryParam paginationQueryParam) {
        LOGGER.debug("START :: getRequests");
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient
                .target(listClientPool.getHostName())
                .path(listClientPool.getBasePath())
                .path(WishlistConstants.COLLABORATORS)
                .path(WishlistConstants.REQUESTS);
            requestParamUtil.addUserQueryParam(rxWebTarget, userQueryParam);
            requestParamUtil.addPaginationQueryParam(rxWebTarget, paginationQueryParam);

            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);

            return RestResponseBuilder.buildFromResponse(
                response,
                Response.Status.OK,
                printLoggerError("Service errors received from service:{} for getRequests", response.getStatus())
            );
        } catch (ListServiceException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exception in getting collaborators by list guid ", e);
            throw new RestException("Service failure while collaborators getRequests : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse addCollaborator(CollaboratorDTO collaborator) {
        LOGGER.debug("START :: addCollaborators");
        Response response = null;

        try {
            String collabJson = ListRequestConverter.convert(collaborator);
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);

            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.COLLABORATORS)
                    .path(WishlistConstants.PATH_PARAMETER_LIST_GUID)
                    .resolveTemplate(WishlistConstants.LIST_GUID, collaborator.getListGuid());

            RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);

            response = builder.post(Entity.json(collabJson));

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Fail to add collaborator");
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception in adding collaborator", e);
            throw new RestException("Exception in adding collaborator: " + e.getMessage());
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse updateCollaboratorPrivilege(String listGuid, CollaboratorDTO collaborator) {
        LOGGER.debug("START :: updateCollaboratorsPrivilege");
        Response response = null;

        try {
            String collabJson = ListRequestConverter.convert(collaborator);
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);

            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.COLLABORATORS)
                    .path(WishlistConstants.PATH_PARAMETER_LIST_GUID)
                    .path(WishlistConstants.PRIVILEGE)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid);

            RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);

            response = builder.post(Entity.json(collabJson));

            if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
                LOGGER.error("Fail to update privilege");
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception in updating privilege", e);
            throw new RestException("Exception in updating privilege: " + e.getMessage());
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse addItemFeedback(String listGuid, String itemGuid, String userGuid, String itemFeedback) {
        Response response = null;

        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.COLLABORATORS)
                    .path(WishlistConstants.ADD_ITEM_FEEDBACK_PATH)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid)
                    .resolveTemplate(WishlistConstants.ITEM_GUID, itemGuid)
                    .queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), userGuid)
                    .queryParam(WishlistConstants.ITEM_FEEDBACK, itemFeedback);

            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).post(null);
            if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
                LOGGER.error("Fail to add feedback  for listGuid : {} : itemGuid : {} : status : {}",
                        listGuid, itemGuid, response.getStatus());
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            throw new RestException("Service failure while adding feedback for item: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse getActivityLog(String listGuid, String userGuid,
                                       PaginationQueryParam paginationQueryParam, SortQueryParam sortQueryParam) {
        Response response = null;

        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.COLLABORATORS)
                    .path(WishlistConstants.GET_ACTIVITY_LOG_PATH)
                    .queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), userGuid)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid);

            requestParamUtil.addPaginationQueryParam(rxWebTarget, paginationQueryParam);
            requestParamUtil.addSortQueryParam(rxWebTarget, sortQueryParam);

            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Fail to get activity log  for userGuid : {} : listGuid : {} : status : {} ",
                        userGuid, listGuid, response.getStatus());
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            throw new RestException("Service failure while getting activityLog: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse getUserCollaborators(String userGuid, Set<String> excludeIds) {
        Response response = null;

        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.COLLABORATORS)
                    .path(WishlistConstants.USER_COLLABORATORS);

            requestParamUtil.addIfNotNull(rxWebTarget, ListQueryParameterEnum.USER_GUID, userGuid);
            requestParamUtil.addIfNotEmpty(rxWebTarget, ListQueryParameterEnum.USER_GUIDS, excludeIds);

            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Fail to get user collaborators  for userGuid : {} : status : {} ",
                        userGuid, response.getStatus());
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            throw new RestException("Service failure while getting user collaborators: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse getListFeedback(String listGuid, String userGuid, String viewerGuid) {
        Response response = null;

        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.COLLABORATORS)
                    .path(WishlistConstants.LIST_FEEDBACK_PATH)
                    .queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), userGuid)
                    .resolveTemplate(WishlistConstants.LIST_GUID, listGuid);

            requestParamUtil.addIfNotEmpty(rxWebTarget, ListQueryParameterEnum.VIEWER_GUID, viewerGuid);

            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Fail to get listFeedback  for userGuid : {} : listGuid : {} : status : {} ",
                        userGuid, listGuid, response.getStatus());
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            throw new RestException("Service failure while getting listFeedback: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse getAllListsByUserGuid(String userGuid) {
        Response response = null;

        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.WISHLISTS_PATH)
                    .path(WishlistConstants.ALL_PATH)
                    .queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), userGuid);

            response = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Fail to all lists for userGuid : {} : status : {} ",
                        userGuid, response.getStatus());
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            throw new RestException("Service failure while getting all lists: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse getUserAvatars(Collection<String> userGuids) {
        Response response = null;

        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.AVATAR);

            requestParamUtil.addIfNotEmpty(rxWebTarget, ListQueryParameterEnum.USER_GUIDS, userGuids);

            RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);

            response = builder.get();

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOGGER.error("Fail to get userAvatar  for userGuid : {} : status : {} ",
                        String.join(",", userGuids), response.getStatus());
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            throw new RestException("Service failure while getting userAvatar: " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse addUserAvatar(String token, UserAvatarDTO userAvatar) {
        LOGGER.debug("START :: addUserAvatar");

        Response response = null;

        try {
            String userAvatarJson = ListRequestConverter.convert(userAvatar);
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);

            RxWebTarget rxWebTarget = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.AVATAR);

            RxInvocationBuilder builder = rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE);

            decorateWithSecurityHeader(builder, token);

            response = builder.post(Entity.json(userAvatarJson));

            if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
                LOGGER.error("Fail to add avatar");
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception while adding avatar", e);
            throw new RestException("Exception while adding avatar: " + e.getMessage());
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse deleteUserAvatar(String token, String userGuid) {
        Response response = null;
        try {
            RxClient rxClient = listClientPool.getRxClient(WishlistConstants.LIST_CLIENT_POOL);
            RxInvocationBuilder builder = rxClient.target(listClientPool.getHostName())
                    .path(listClientPool.getBasePath())
                    .path(WishlistConstants.AVATAR)
                    .path(WishlistConstants.PATH_PARAMETER_USER_GUID)
                    .resolveTemplate(WishlistConstants.USER_GUID, userGuid)
                    .request();

            decorateWithSecurityHeader(builder, token);

            response = builder.delete();

            if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
                LOGGER.error("service returned errors with status {} while deleting avatar for userGuid :: {}",
                        response.getStatus(), userGuid);
            }

            return RestResponseBuilder.buildFromResponse(response);

        } catch (Exception e) {
            LOGGER.error("Exception while deleting avatar", e);
            throw new RestException("Service failure while deleting avatar : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    private Callable printLoggerError(String text, Object ...objects) {
        return  () -> {
            LOGGER.error(text, objects);
            return null;
        };
    }
}
