package com.macys.selection.xapi.list.rest.request.cookie;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.stereotype.Component;

@Component
public class ListCookies {

    private Long userId;
    private String userGuid;
    private String token;

    public ListCookies() {}

    public ListCookies(Long id, String guid, String token) {
        this.userId = id;
        this.userGuid = guid;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }
    public String getUserGuid() {
        return userGuid;
    }
    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
