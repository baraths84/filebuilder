package com.macys.selection.xapi.list.rest.request.cookie;

import static com.macys.selection.xapi.list.rest.request.cookie.CookieHandlerUtil.*; // com.macys.selection.xapi.list.rest.request.cookie.CookieHandlerUtil.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import javax.servlet.http.HttpServletRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class CookieHandlerTest extends AbstractTestNGSpringContextTests {

    @InjectMocks
    private CookieHandler cookieHandler;

    @Mock
    private HttpServletRequest req;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        FieldUtils.writeField(cookieHandler, "cookieHandlerUtil", new CookieHandlerUtil(), true);
    }

    @Test
    public void parseAndValidateCookies_PositiveCase() {
        when(req.getCookies()).thenReturn(prepareCookies("100500"));

        ListCookies cookies = cookieHandler.parseAndValidate(req);

        assertNotNull(cookies);
        assertEquals(100500L, cookies.getUserId().longValue());
        assertEquals("user_guid", cookies.getUserGuid());

    }

    @Test(expected = IllegalArgumentException.class)
    public void parseAndValidateCookies_NegativeCase_NullAsRequestComing() {
        cookieHandler.parseAndValidate(null);
    }


//    @Test(expected = NotAuthorizedException.class)
//    public void parseAndValidateCookies_NegativeCase_NotAuthorized() {
//        cookieHandler.parseAndValidate(req);
//    }

    @Test(expected = IllegalArgumentException.class)
    public void parseAndValidateCookies_NegativeCase_InllegalUserIdPassed() {
        when(req.getCookies()).thenReturn(prepareCookies("12345_invalid_user_id"));
        cookieHandler.parseAndValidate(req);
    }

    private javax.servlet.http.Cookie[] prepareCookies(String userId) {
        javax.servlet.http.Cookie[] cookies = new javax.servlet.http.Cookie[10];

        cookies[0] = new javax.servlet.http.Cookie(MACYS_ONLINE_UID, userId);
        cookies[1] = new javax.servlet.http.Cookie(MACYS_ONLINE_GUID, "user_guid");
        cookies[2] = new javax.servlet.http.Cookie(SIGNED_IN, "1");
        cookies[3] = new javax.servlet.http.Cookie(SECURE_TOKEN, "secure_token");
        cookies[4] = new javax.servlet.http.Cookie(GC_COOKIE_SNS, "bypass_session_filter1_92_false3_87_last_access_token1_92_" + System.currentTimeMillis());

        return cookies;
    }


    @Test
    public void check_secure_token_positve_test() {

        javax.servlet.http.Cookie secureCookie = new javax.servlet.http.Cookie("secure_user_token",
                                                                               "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoU2cg1YgbRvowCGAR3lMy7g==");
        javax.servlet.http.Cookie[] cookies = {secureCookie};
        Mockito.when(req.getCookies()).thenReturn(cookies);
        String cookieValue = null;
        cookieValue = CookieHandler.getCookieValue(req, "secure_user_token");
        assertEquals(
                "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoU2cg1YgbRvowCGAR3lMy7g==",
                cookieValue);
    }

    @Test
    public void check_secure_token_null_test() {
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("secure_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nR");
        javax.servlet.http.Cookie[] cookies = {cookie};
        Mockito.when(req.getCookies()).thenReturn(cookies);
        CookieHandler.getCookieValue(req, "secure_user_token");
    }

    @Test
    public void check_get_user_cookies_null_test() {
        Assert.assertNull(CookieHandler.getUserCookie(req, "secure_user_token"));
    }

    @Test
    public void get_cookie_value_from_group_cookie() {

        javax.servlet.http.Cookie sNSGCs = new javax.servlet.http.Cookie("SNSGCs", "bypass_session_filter1_92_false3_87_last_access_token1_92_1485243498789");
        javax.servlet.http.Cookie[] cookies = {sNSGCs};
        Mockito.when(req.getCookies()).thenReturn(cookies);
        Assert.assertEquals(CookieHandler.getGroupCookieValue(CookieHandler.getUserCookie(req, "SNSGCs"), "last_access_token"), "1485243498789");
    }

}
