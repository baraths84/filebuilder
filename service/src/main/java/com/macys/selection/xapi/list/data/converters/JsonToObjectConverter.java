package com.macys.selection.xapi.list.data.converters;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.macys.selection.xapi.list.exception.ListServiceException;

public class JsonToObjectConverter<T> {

	private Class<T> type;

	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
	}


	public Class<T> getType() {
		return type;
	}

	public JsonToObjectConverter(Class<T> type) {
		this.type = type;
	}


	public T parseJsonToObject(String jsonResponse) {
		T t;

		try {
			t = objectMapper.readValue(jsonResponse.getBytes("UTF-8"), getType());
			return t;
		} catch (IOException ex) {
			throw new ListServiceException(ex.getMessage());
		}
	}
}
