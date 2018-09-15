package com.macys.selection.xapi.list.client;

import java.net.URI;
import java.util.concurrent.Callable;

import javax.ws.rs.core.Response;

import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Build internal xapi response from jax-rs rest Response.
 **/
public class RestResponseBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseBuilder.class);

	private static final int REDIRECT_CODE = 301;

	private RestResponseBuilder() {
		throw new IllegalStateException("RestResponseBuilder class");
	}

	public static RestResponse buildFromResponse(Response response) {
		RestResponse restResponse = buildRestResponse(response);

		try {
			restResponse.setBody(response.readEntity(String.class));
		} catch (Exception e) {
			LOGGER.error("error building rest response returned from service");
			throw new RestException(e.getMessage(), e);
		}

		return restResponse;
	}

  /**
   *
   * @param response - response
   * @param expectedStatus - expected response HTTP status
   * @param expression - some call method. Logger.error for example
   * @return restResponse
   */

	public static RestResponse buildFromResponse(Response response, Response.Status expectedStatus, Callable expression) {
		RestResponse restResponse = buildRestResponse(response);

		try {
			restResponse.setBody(response.readEntity(String.class));
			if (restResponse.getStatusCode() != expectedStatus.getStatusCode()) {
				expression.call();
				throw new ListServiceException(restResponse.getStatusCode(), restResponse.getBody());
			}
		} catch (ListServiceException e){
			throw e;
		} catch (Exception e) {
			LOGGER.error("error building rest response returned from service");
			throw new RestException(e.getMessage(), e);
		}

		return restResponse;
	}

	private static RestResponse buildRestResponse(Response response) {
		RestResponse restResponse = new RestResponse();

		final int statusCode = response.getStatus();
		restResponse.setStatusCode(statusCode);

		if(REDIRECT_CODE == statusCode) {
			URI locationUri = response.getLocation();
			if (null != locationUri) {
				restResponse.setLocation(locationUri.toString());
			}
		}
		return restResponse;
	}

}
