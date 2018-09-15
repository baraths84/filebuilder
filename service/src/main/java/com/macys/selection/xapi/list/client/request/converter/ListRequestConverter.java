package com.macys.selection.xapi.list.client.request.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.macys.selection.xapi.list.client.request.MergeListRequest;
import com.macys.selection.xapi.list.client.response.CollaboratorDTO;
import com.macys.selection.xapi.list.client.response.EmailShareDTO;
import com.macys.selection.xapi.list.client.response.ItemDTO;
import com.macys.selection.xapi.list.client.response.UserAvatarDTO;
import com.macys.selection.xapi.list.client.response.WishListDTO;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ListRequestConverter {

    private static ObjectMapper wrapRootObjectMapper = new ObjectMapper();
    private static ObjectMapper noRootObjectMapper = new ObjectMapper();

    static {
        wrapRootObjectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        noRootObjectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }

    public static String convert(WishListDTO wishList) throws JsonProcessingException {
        if (wishList == null) {
            wishList = new WishListDTO();
        }
        return wrapRootObjectMapper.writeValueAsString(wishList);
    }

    public static String convert(ItemDTO item) throws JsonProcessingException {
        if (item == null) {
            item = new ItemDTO();
        }
        return wrapRootObjectMapper.writeValueAsString(item);
    }

    public static String convert(EmailShareDTO emailShareDTO) throws JsonProcessingException {
        if (emailShareDTO == null) {
            emailShareDTO = new EmailShareDTO();
        }
        return wrapRootObjectMapper.writeValueAsString(emailShareDTO);
    }

    public static String convert(MergeListRequest mergeRequest) throws JsonProcessingException {
        if (mergeRequest == null) {
            mergeRequest = new MergeListRequest();
        }
        return wrapRootObjectMapper.writeValueAsString(mergeRequest);
    }

    public static String convert(CollaboratorDTO collaborator) throws JsonProcessingException {
        if (collaborator == null) {
            collaborator = new CollaboratorDTO();
        }
        return wrapRootObjectMapper.writeValueAsString(collaborator);
    }

    public static String convert(UserAvatarDTO userAvatarDTO) throws JsonProcessingException {
        if (userAvatarDTO == null) {
            userAvatarDTO = new UserAvatarDTO();
        }
        return wrapRootObjectMapper.writeValueAsString(userAvatarDTO);
    }

    public static String convert(Collection<?> items) throws JsonProcessingException {
        if (items == null) {
            items = Collections.emptyList();
        }
        return noRootObjectMapper.writeValueAsString(items);
    }

    public static String convert(Map<?, ?> items) throws JsonProcessingException {
        if (items == null) {
            items = Collections.emptyMap();
        }
        return noRootObjectMapper.writeValueAsString(items);
    }
}
