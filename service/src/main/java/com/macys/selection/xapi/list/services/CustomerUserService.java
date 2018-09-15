package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.CustomerUserRestClient;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.JsonToUsersListConverter;
import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.exception.InputProfileDataInvalidException;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;

@Component
public class CustomerUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerUserService.class);

    private final CustomerUserRestClient userRestClient;

    private JsonToObjectConverter<UserResponse> userConverter = new JsonToObjectConverter<>(UserResponse.class);
    private JsonToUsersListConverter usersListConverter = new JsonToUsersListConverter();

    @Autowired
    public CustomerUserService(CustomerUserRestClient userRestClient) {
        this.userRestClient = userRestClient;
    }

    public void populateUserQueryParam(UserQueryParam userQueryParam, UserResponse userResponse, Boolean guestUserDefaultValue) {
        if (userResponse != null) {
            userQueryParam.setUserGuid(userResponse.getGuid());
            userQueryParam.setUserId(userResponse.getId());
            userQueryParam.setGuestUser(userResponse.isGuestUser());
        } else {
            userQueryParam.setGuestUser(guestUserDefaultValue);
        }
    }

    public void populateUserQueryParamWithFirstName(UserQueryParam userQueryParam, UserResponse userResponse) {
        if (userResponse != null) {
            populateUserQueryParam(userQueryParam, userResponse, null);
            if (userResponse.getProfile() != null) {
                userQueryParam.setFirstName(userResponse.getProfile().getFirstName());
            }
        }
    }

    public Boolean retrieveUserGuestInfo(Long userId, String userGuid, boolean isExistingUserRequired) {
        UserResponse userResponse = retrieveUser(userId, userGuid, isExistingUserRequired);
        return userResponse != null ? userResponse.isGuestUser() : null;
    }

    public UserResponse retrieveUser(Long userId, String userGuid) {
        return retrieveUser(userId, userGuid, true);
    }

    public UserResponse retrieveUser(Long userId, String userGuid, boolean isExistingUserRequired) {
        validateEitherUserIdOrGuidSpecified(userId, userGuid);
        UserResponse userResponse = null;
        try {
            RestResponse restResponse = userRestClient.retrieveUser(userId, userGuid);
            userResponse = getUserResponse(restResponse);
        } catch (InputProfileDataInvalidException e) {
            LOGGER.error("User not found with userId {} userGuid {}", userId, userGuid, e);
            if (isExistingUserRequired) {
                throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.INVALID_USER_ID);
            }
        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        }
        return userResponse;
    }

    private UserResponse getUserResponse(RestResponse restResponse) throws InputProfileDataInvalidException {
        UserResponse result = null;
        if (restResponse != null) {
            String response = restResponse.getBody();

            if (restResponse.getStatusCode() == WishlistConstants.STATUS_SUCCESS) {
                result = userConverter.parseJsonToObject(response);
            } else {
                if (restResponse.getStatusCode() == WishlistConstants.BAD_REQUEST) {
                    throw new InputProfileDataInvalidException();
                } else {
                    throw new ListServiceException(restResponse.getStatusCode(), response);
                }
            }
        }
        return result;
    }

    public UserResponse retrieveUserProfile(String token, Long userId) {
        validateSecurityTokenSpecified(token);
        validateEitherUserIdOrGuidSpecified(userId, null);
        RestResponse restResponse = userRestClient.retrieveUserProfile(token, userId);
        try {
            return getUserInfoResponse(restResponse);
        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        }
    }

    private void validateSecurityTokenSpecified(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.NO_SECURE_TOKEN);
        }
    }

    private UserResponse getUserInfoResponse(RestResponse restResponse) {
        UserResponse result = null;
        if (restResponse != null) {
            String response = restResponse.getBody();
            if (restResponse.getStatusCode() == WishlistConstants.STATUS_SUCCESS) {
                result = userConverter.parseJsonToObject(response);
            } else {
                throw new ListServiceException(restResponse.getStatusCode(), response);
            }
        }
        return result;
    }

    public void validateEitherUserIdOrGuidSpecified(Long userId, String userGuid) {
        if ((userId == null || userId <= 0) && StringUtils.isEmpty(userGuid)) {
            throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.NO_USER_INFO);
        }
    }

    public void validateNotBothUserIdAndGuidSpecified(Long userId, String userGuid) {
        boolean isRequestedByUserId = userId != null && userId > 0;
        boolean isRequestedByUserGuid = org.apache.commons.lang3.StringUtils.isNotEmpty(userGuid);
        if (isRequestedByUserId && isRequestedByUserGuid) {
            throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.INVALID_INPUT);
        }
    }

    public UserResponse createGuestUser() {
        return createGuestUser(null);
    }

    public UserResponse createGuestUser(Long userId) {
        try {
            UserResponse userResponse = new UserResponse();
            Long createdUserId = getUserIdResponse(userRestClient.createUser(userId));
            userResponse.setId(createdUserId);
            String createdUserGuid = getUserGuidResponse(userRestClient.retrieveUserGUID(createdUserId));
            userResponse.setGuid(createdUserGuid);
            userResponse.setGuestUser(true);
            return userResponse;
        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        }
    }

    private Long getUserIdResponse(RestResponse restResponse) {
        Long userId = null;
        if (restResponse != null) {
            String response = restResponse.getBody();
            if (restResponse.getStatusCode() == WishlistConstants.STATUS_SUCCESS) {
                userId = StringUtils.isNumeric(response) ? Long.valueOf(response) : null;
            } else {
                throw new ListServiceException(restResponse.getStatusCode(), response);
            }
        }
        return userId;
    }

    private String getUserGuidResponse(RestResponse restResponse) {
        try {
            String userGuid = null;
            if (restResponse != null) {
                String response = restResponse.getBody();
                if (restResponse.getStatusCode() == WishlistConstants.STATUS_SUCCESS) {
                    userGuid = response;
                } else {
                    throw new ListServiceException(restResponse.getStatusCode(), response);
                }
            }
            return userGuid;
        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        }
    }

    public List<UserResponse> findUsers(String firstName, String lastName, String state, Integer limit) {
        try {
            RestResponse restResponse = userRestClient.findUsers(firstName, lastName, state, limit);
            return getUsersListResponse(restResponse);
        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        }
    }

    public List<UserResponse> retrieveUsersByGuids(Collection<String> userGuids) {
        try {
            RestResponse restResponse = userRestClient.retrieveUsersByGuids(userGuids);
            return getUsersListResponse(restResponse);
        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        }
    }

    private List<UserResponse> getUsersListResponse(RestResponse restResponse) {
        List<UserResponse> result = null;
        if (restResponse != null) {
            String response = restResponse.getBody();
            if (restResponse.getStatusCode() == WishlistConstants.STATUS_SUCCESS) {
                result = usersListConverter.parseJsonToObject(response);
            } else {
                throw new ListServiceException(restResponse.getStatusCode(), response);
            }
        }
        return result;
    }
}
