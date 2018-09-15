package com.macys.selection.xapi.list.client;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.util.CustomerQueryParameterEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Optional;

@Component
public class CustomerUserRestClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerUserRestClient.class);

    private final RestClientFactory.JaxRSClientPool userProfileV1ClientPool;
    private final RestClientFactory.JaxRSClientPool userProfileV2ClientPool;

    @Autowired
    public CustomerUserRestClient(@Qualifier(WishlistConstants.CUSTOMER_USER_PROFILE_V1_CLIENT_POOL) RestClientFactory.JaxRSClientPool userProfileV1ClientPool,
                                  @Qualifier(WishlistConstants.CUSTOMER_USER_PROFILE_V2_CLIENT_POOL) RestClientFactory.JaxRSClientPool userProfileV2ClientPool) {
        this.userProfileV1ClientPool = userProfileV1ClientPool;
        this.userProfileV2ClientPool = userProfileV2ClientPool;
    }

    public RestResponse retrieveUser(Long userId, String userGuid) {
        LOGGER.debug("START :: retrieveUser : userId - {} userGuid - {}", userId, userGuid);
        Response response = null;
        try {

            response = userProfileV1ClientPool.getRxClient(WishlistConstants.CUSTOMER_USER_PROFILE_V1_CLIENT_POOL)
                    .target(userProfileV1ClientPool.getHostName())
                    .path(userProfileV1ClientPool.getBasePath())
                    .path(WishlistConstants.RETRIEVE_USER_PATH)
                    .queryParam(WishlistConstants.USER_ID, userId)
                    .queryParam(WishlistConstants.USER_GUID.toLowerCase(), userGuid)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class);


            if (response.getStatus() != WishlistConstants.STATUS_SUCCESS) {
                LOGGER.error("exception retrieving the user for userId {} userGuid {} with responseStatus {} ", userId, userGuid, response.getStatus());
            }

            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception in retrieving the user userId {} userGuid {}", userId, userGuid, e);
            throw new RestException("Service failure while retrieving the user : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse retrieveUserProfile(String token, Long userId) {
        LOGGER.debug("START :: retrieveUserProfile : userId - {} ", userId);
        Response response = null;
        try {

            RxInvocationBuilder builder = userProfileV1ClientPool.getRxClient(WishlistConstants.CUSTOMER_USER_PROFILE_V1_CLIENT_POOL)
                    .target(userProfileV1ClientPool.getHostName())
                    .path(userProfileV1ClientPool.getBasePath())
                    .path(WishlistConstants.RETRIEVE_USER_PROFILE_PATH)
                    .resolveTemplate("userId", userId)
                    .request(MediaType.APPLICATION_JSON_TYPE);

            builder.header(WishlistConstants.SECURITY_TOKEN_HEADER_NAME, token);
            response = builder.get(Response.class);
            if (response.getStatus() != WishlistConstants.STATUS_SUCCESS) {
                LOGGER.error("exception retrieving user profile for userId {} with responseStatus {} ", userId, response.getStatus());
            }

            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception in retrieving user profile userId {}", userId, e);
            throw new RestException("Service failure while retrieving user profile : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    /**
     * Creates user with empty profile (guest user) and returns its userID.
     */
    public RestResponse createUser(Long userId) {
        LOGGER.debug("START :: createUser");
        Response response = null;
        try {
            response = userProfileV1ClientPool.getRxClient(WishlistConstants.CUSTOMER_USER_PROFILE_V1_CLIENT_POOL)
                    .target(userProfileV1ClientPool.getHostName())
                    .path(userProfileV1ClientPool.getBasePath())
                    .path(WishlistConstants.CREATE_USER_PATH)
                    .queryParam(WishlistConstants.USER_ID, userId)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class);
            if (response.getStatus() != WishlistConstants.STATUS_SUCCESS) {
                LOGGER.error("exception creating a user ", response.getStatus());
            }

            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception creating a user ", e);
            throw new RestException("Service failure while creating the user : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    /**
     * This method returns userGUID based on userID. If Valid userGUID for the
     * userID exists then it is returned. If Valid userGUID for the userID does
     * not exist then a new userGUID is generated and persisted and its is
     * returned.
     */
    public RestResponse retrieveUserGUID(Long userId) {
        LOGGER.debug("START :: retrieveUserGUID : userId {} ", userId);
        Response response = null;
        try {
            response = userProfileV1ClientPool.getRxClient(WishlistConstants.CUSTOMER_USER_PROFILE_V1_CLIENT_POOL)
                    .target(userProfileV1ClientPool.getHostName())
                    .path(userProfileV1ClientPool.getBasePath())
                    .path(WishlistConstants.RETRIEVE_USER_GUID_PATH)
                    .resolveTemplate(WishlistConstants.USER_ID, userId)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class);
            if (response.getStatus() != WishlistConstants.STATUS_SUCCESS) {
                LOGGER.error("exception retrieving userGuid for userId {} ", userId, response.getStatus());
            }

            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception retrieving userGuid for userId {} ", userId, e);
            throw new RestException("Service failure while retrieving user guid : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse findUsers(String firstName, String lastName, String state, Integer limit) {
        LOGGER.debug("START :: findUsers : firstName {} lastName {} state {}", firstName, lastName, state);
        Response response = null;
        try {
            response = userProfileV2ClientPool.getRxClient(WishlistConstants.CUSTOMER_USER_PROFILE_V2_CLIENT_POOL)
                    .target(userProfileV2ClientPool.getHostName())
                    .path(userProfileV2ClientPool.getBasePath())
                    .path(WishlistConstants.FIND_USERS_PATH)
                    .queryParam(WishlistConstants.FIRST_NAME, firstName)
                    .queryParam(WishlistConstants.LAST_NAME, lastName)
                    .queryParam(WishlistConstants.STATE, state)
                    .queryParam(CustomerQueryParameterEnum.LIMIT.getParamName(), limit)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class);
            if (response.getStatus() != WishlistConstants.STATUS_SUCCESS) {
                LOGGER.error("exception finding the users by firstName {} lastName {} state {} limit {}", firstName, lastName, state, limit, response.getStatus());
            }

            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception finding the users by firstName {} lastName {} state {} limit {}", firstName, lastName, state, limit, e);
            throw new RestException("Service failure while finding the users : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }

    public RestResponse retrieveUsersByGuids(Collection<String> userGuids) {
        LOGGER.debug("START :: retrieveUsersByGuids : {}", userGuids);
        Response response = null;
        try {
            response = userProfileV2ClientPool.getRxClient(WishlistConstants.CUSTOMER_USER_PROFILE_V2_CLIENT_POOL)
                    .target(userProfileV2ClientPool.getHostName())
                    .path(userProfileV2ClientPool.getBasePath())
                    .path(WishlistConstants.RETRIEVE_USERS_BY_IDS_PATH)
                    .queryParam(WishlistConstants.USER_GUIDS, StringUtils.join(userGuids.toArray(), ","))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class);
            if (response.getStatus() != WishlistConstants.STATUS_SUCCESS) {
                LOGGER.error("Exception while retrieving the users by ids {} : status {} ", userGuids, response.getStatus());
            }
            return RestResponseBuilder.buildFromResponse(response);
        } catch (Exception e) {
            LOGGER.error("Exception while retrieving the users by ids {} ", userGuids, e);
            throw new RestException("Service failure while retrieving the users : " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(Response::close);
        }
    }
}
