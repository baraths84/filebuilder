/*******************************************************************************
 * Copyright (c) 2017 macys.com. All rights reserved.
 *******************************************************************************/
package com.macys.selection.xapi.list.rest.request;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * 
 * @author m785440
 *
 */

public class UserQueryParam {

	@QueryParam("userId")
	private Long userId;

	@QueryParam("userIds")
	private List<Long> userIds;

	@QueryParam("userGuid")
	private String userGuid;

	@QueryParam("guestUser")
	private Boolean guestUser;

	@QueryParam("firstName")
	private String firstName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public String getUserGuid() {
		return userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	public Boolean getGuestUser() {
		return guestUser;
	}

	public void setGuestUser(Boolean guestUser) {
		this.guestUser = guestUser;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
