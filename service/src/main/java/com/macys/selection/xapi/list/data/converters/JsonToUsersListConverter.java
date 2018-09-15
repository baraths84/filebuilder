package com.macys.selection.xapi.list.data.converters;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.exception.ListServiceException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class JsonToUsersListConverter {

	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
	}


	public List<UserResponse> parseJsonToObject(String jsonResponse) {
		List<UserResponse> t;

		try {
			t = Arrays.asList(objectMapper.readValue(jsonResponse.getBytes("UTF-8"), UserResponse[].class));
			return t;
		} catch (IOException ex) {
			throw new ListServiceException(ex.getMessage());
		}
	}
}
