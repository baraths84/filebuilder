package com.macys.selection.xapi.list.rest.request.cookie;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 **/
@Component
public class CookieHandler {
    private static final String COOKIE_GROUP_DELIMITER = "3_87_";
    private static final String PROPERTY_ASSIGNER = "1_92_";

    @Autowired
    private CookieHandlerUtil cookieHandlerUtil;

    public ListCookies parseAndValidate(HttpServletRequest request) {
        Preconditions.checkArgument(request != null, "Could not retrive Cookies, Request is NULL");

        String token = cookieHandlerUtil.getSecureToken(request);
        String userId = cookieHandlerUtil.getMacysOnlineUserId(request);
        String userGuid = cookieHandlerUtil.getMacysOnlineGuid(request);
        Long id = null;
        if(userId != null) {
            id = Long.parseLong(userId);
        }

        return new ListCookies(id, userGuid, token);
    }

    public static String getGroupCookieValue(Cookie cookies, String value) {

        String result = null;
        if (cookies != null && cookies.getValue() != null) {
            String values = cookies.getValue();
            String[] groups = values.split(COOKIE_GROUP_DELIMITER);
            if (groups != null && groups.length > 0) {
                for (String part : groups) {
                    String pair[] = part.split(PROPERTY_ASSIGNER);
                    if (pair != null && pair.length > 1 && pair[0].trim().equals(value)) {
                        result = pair[1].trim();
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookie = getUserCookie(request, cookieName);
        if (cookie != null) {
            String value = cookie.getValue();
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    public static Cookie getUserCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }


}
