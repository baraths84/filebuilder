package com.macys.selection.xapi.list.data.converters;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.response.Profile;
import com.macys.selection.xapi.list.rest.response.User;

public class JsonToUserMapper {
	
	private static final String LISTS = "lists";
	
	private JsonToUserMapper() {
		throw new UnsupportedOperationException();
	}
	

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonToUserMapper.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public static User parse(String jsonResponse, boolean wishListOnly) {
		try {
			JsonNode rootNode = objectMapper.readValue(jsonResponse.getBytes("UTF-8"), JsonNode.class);
			if(rootNode != null && !rootNode.isNull()) {
				return readValue(rootNode, wishListOnly);
			} 
		} catch (IOException ex) {
			LOGGER.error("exception parsing json response", ex.getMessage());
			throw new ListServiceException(ex.getMessage());
		}

		return null;
	}

	private static User readValue(JsonNode node, boolean wishListOnly) {
		JsonNode userNode = null;

		if (wishListOnly) {
			userNode = node.get("list").get("user");
		} else {
			if (node.get(LISTS)!= null) {
				JsonNode listsNode = node.get(LISTS).get(LISTS);
				if (listsNode != null) {
					JsonNode listNode = listsNode.get(0);
					if (listNode != null) {
						userNode = listNode.get("user");
					}
				}
			}
		}

		if (null == userNode || userNode.isNull()) {
			return null;
		}

		JsonNode id = userNode.get("id");
		JsonNode guid = userNode.get("guid");
		JsonNode guestUser = userNode.get("guestUser");
		JsonNode profileAddress = userNode.get("profileAddress");

		User user = new User();
		user.setId( (id != null && !id.isNull()) ? id.asLong() : null);
		user.setGuid( (guid != null && !guid.isNull()) ? guid.asText() : null);
		user.setGuestUser( (guestUser != null && !guestUser.isNull()) ? guestUser.asBoolean() : null);

		JsonNode firstName = (profileAddress !=null && !profileAddress.isNull()) ? profileAddress.get("firstName") : null;
		Profile profile = new Profile();
		profile.setFirstName((firstName != null && !firstName.isNull()) ? firstName.asText() : null);

		user.setProfile(profile);
		return user;
	}

}
