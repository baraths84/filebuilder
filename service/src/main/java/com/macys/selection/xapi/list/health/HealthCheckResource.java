/*******************************************************************************
 * Copyright (c) 2017 macys.com. All rights reserved.
 *******************************************************************************/
package com.macys.selection.xapi.list.health;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

/**
 * @author YH03933
 */
@Component
@Path("/healthcheck")
public class HealthCheckResource {

	@GET
	public String hello() {
		return "Health check passed!";
	}

}
