package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.ListRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.response.UserAvatarDTO;
import com.macys.selection.xapi.list.client.response.UserAvatarSetDTO;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.request.UserAvatarRequest;
import com.macys.selection.xapi.list.rest.response.UserAvatar;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class UserAvatarService {

    private final ListRestClient listRestClient;
    private final CustomerUserService customerUserService;
    private final MapperFacade mapperFacade;

    private JsonToObjectConverter<UserAvatarSetDTO> avatarSetConverter = new JsonToObjectConverter<>(UserAvatarSetDTO.class);

    public UserAvatarService(ListRestClient listRestClient,
                             CustomerUserService customerUserService,
                             MapperFacade mapperFacade) {
        this.listRestClient = listRestClient;
        this.customerUserService = customerUserService;
        this.mapperFacade = mapperFacade;
    }

    public void deleteAvatar(String token, String userGuid) {
        UserResponse userResponse = customerUserService.retrieveUser(null, userGuid);
        RestResponse response = listRestClient.deleteUserAvatar(token, userResponse.getGuid());
        if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new ListServiceException(response.getStatusCode(), response.getBody());
        }
    }

    public void addUserAvatar(String token, UserAvatarRequest userAvatar) {
        UserAvatarDTO userAvatarDTO = mapperFacade.map(userAvatar, UserAvatarDTO.class);
        if (userAvatarDTO != null) {
            UserResponse userResponse = customerUserService.retrieveUser(null, userAvatarDTO.getUserGuid());
            userAvatarDTO.setUserGuid(userResponse.getGuid());
        }
        RestResponse response = listRestClient.addUserAvatar(token, userAvatarDTO);
        if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new ListServiceException(response.getStatusCode(), response.getBody());
        }
    }

    public UserAvatar getUserAvatar(String userGuid) {
        UserResponse userResponse = customerUserService.retrieveUser(null, userGuid);
        List<UserAvatarDTO> avatarDTOS = getUserAvatars(Arrays.asList(userResponse.getGuid()));
        return CollectionUtils.isNotEmpty(avatarDTOS) ? mapperFacade.map(avatarDTOS.get(0), UserAvatar.class) : null;
    }

    public List<UserAvatarDTO> getUserAvatars(Collection<String> userGuids) {
        RestResponse restResponse = listRestClient.getUserAvatars(userGuids);
        if (restResponse != null) {
            String response = restResponse.getBody();
            if (restResponse.getStatusCode() == WishlistConstants.STATUS_SUCCESS) {
                UserAvatarSetDTO userAvatarSetDTO = avatarSetConverter.parseJsonToObject(response);
                if (userAvatarSetDTO != null && CollectionUtils.isNotEmpty(userAvatarSetDTO.getProfilePicture())) {
                    return userAvatarSetDTO.getProfilePicture();
                }
            } else {
                throw new ListServiceException(restResponse.getStatusCode(), response);
            }
        }
        return Collections.EMPTY_LIST;
    }

}
