package com.macys.selection.xapi.list.rest.request.cookie;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Calendar;

@Component
public class CookieHandlerUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CookieHandlerUtil.class);

    public static final String MACYS_ONLINE_GUID = "macys_online_guid";
    public static final String MACYS_ONLINE_UID = "macys_online_uid";
    public static final String BLOOMIES_ONLINE_GUID = "bloomingdales_online_guid";
    public static final String BLOOMIES_ONLINE_UID = "bloomingdales_online_uid";
    public static final String SIGNED_IN = "SignedIn";
    public static final String SECURE_TOKEN = "secure_user_token";
    public static final String GC_COOKIE_SNS = "SNSGCs";
    public static final String LAST_ACCESS = "last_access_token";
    public static final int MAX_INACTIVE_INTERVAL = 30;

    public static String removeSpaces(String token) {
        if (StringUtils.isNotBlank(token)) {
            return token.replaceAll("\\s+", "").replace("\r", "").replace("\n", "");
        }
        return "";
    }

    public int getSignedIn(HttpServletRequest request) {
        String signInCookieValue;
        signInCookieValue = CookieHandler.getCookieValue(request, SIGNED_IN);
        if (signInCookieValue != null) {
            return Integer.parseInt(signInCookieValue);
        }
        return 0;
    }

    public String getMacysOnlineUserId(HttpServletRequest request) {
        if(isBloomiesCookie(request,BLOOMIES_ONLINE_UID)){
            return CookieHandler.getCookieValue(request, BLOOMIES_ONLINE_UID);
        }
        return CookieHandler.getCookieValue(request, MACYS_ONLINE_UID);
    }

    public String getAccessToken(HttpServletRequest request) {
        return CookieHandler.getCookieValue(request, GC_COOKIE_SNS);
    }

    public String getMacysOnlineGuid(HttpServletRequest request) {
        if(isBloomiesCookie(request,BLOOMIES_ONLINE_GUID)){
            return CookieHandler.getCookieValue(request, BLOOMIES_ONLINE_GUID);
        }
        return CookieHandler.getCookieValue(request, MACYS_ONLINE_GUID);
    }

    public boolean isBloomiesCookie(HttpServletRequest request, String name){
        if(request.getCookies()!=null){
            for(Cookie cookie : request.getCookies()){
                if(cookie!=null && name.equals(cookie.getName())){
                    return true;
                }
            }
        }
        return false;
    }

    public String getSecureToken(HttpServletRequest request) {
        String secureToken;
        secureToken = CookieHandler.getCookieValue(request, SECURE_TOKEN);
        if (StringUtils.isNotBlank(secureToken)) {
            try {
                secureToken = URLDecoder.decode(secureToken, "UTF-8");
                LOGGER.debug("Secure Token Before decoding" + secureToken);
            } catch (Exception e) {
                LOGGER.error("Exception caught, returning null.");
                return null;
            }
            secureToken = secureToken.replaceAll("\n", "").replaceAll("\r", "").trim().replace(" ", "+");
            LOGGER.debug("Secure Token after decoding" + secureToken);
        }
        return secureToken;
    }

    public boolean isUserSignedIn(HttpServletRequest request, String token, String macysOnlineUserId, int
            signInCookieValue) {
        return isNotBlank(token) && isNotBlank(macysOnlineUserId) && signInCookieValue == 1 && !isSessionTimedOut(request);
    }

    public boolean isSessionTimedOut(HttpServletRequest request) {
        String lastAccessTime = CookieHandler.getGroupCookieValue(CookieHandler.getUserCookie(request, GC_COOKIE_SNS), LAST_ACCESS);
        if (StringUtils.isNotBlank(lastAccessTime)) {
            long presentTime = getCurrentDateWithTimeStamp();
            long lastTime = Long.valueOf(lastAccessTime);
            int diffTime = (int) Math.abs((presentTime - lastTime) / 60000);
            return diffTime > MAX_INACTIVE_INTERVAL;
        }
        return true;
    }

    public long getCurrentDateWithTimeStamp() {
        Calendar currentDate = Calendar.getInstance();
        return currentDate.getTimeInMillis();
    }


}
