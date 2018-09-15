package com.macys.selection.xapi.list.rest.request.cookie;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

public class CookieHandlerUtilTest extends AbstractTestNGSpringContextTests {

    @InjectMocks
    private CookieHandlerUtil commonUtil;

    @Mock
    private HttpServletRequest req;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMacysOnlineUidCookie() {
        Cookie macysOnlineUid = new Cookie("macys_online_uid", "23233443441");
        Cookie[] cookies = {macysOnlineUid};
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(commonUtil.getMacysOnlineUserId(req), "23233443441");
    }
    @Test
    public void testBloomiesOnlineUidCookie() {
        Cookie bloomiesOnlineUid = new Cookie("bloomingdales_online_uid", "23233443442");
        Cookie[] cookies = {bloomiesOnlineUid};
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(commonUtil.getMacysOnlineUserId(req), "23233443442");
    }
    @Test
    public void testRemoveEmptySpace() {
        assertNotNull(CookieHandlerUtil.removeSpaces(""));
    }

    @Test
    public void testSignedInCookie() {
        Cookie signedIn = new Cookie("SignedIn", "1");
        Cookie[] cookies = {signedIn};
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(commonUtil.getSignedIn(req), 1);
    }

    @Test
    public void testNotSignedInCookie() {
        Cookie signedIn = new Cookie("SignedIn", "0");
        Cookie[] cookies = {signedIn};
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(commonUtil.getSignedIn(req), 0);
    }

    @Test
    public void testMacysOnlineGuidCookie() {
        Cookie macys_online_guid = new Cookie("macys_online_guid", "353545451");
        Cookie[] cookies = {macys_online_guid};
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(commonUtil.getMacysOnlineGuid(req), "353545451");
    }
    @Test
    public void testBloomiesOnlineGuidCookie() {
        Cookie bloomies_online_guid = new Cookie("bloomingdales_online_guid", "353545452");
        Cookie[] cookies = {bloomies_online_guid};
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(commonUtil.getMacysOnlineGuid(req), "353545452");
    }

    @Test
    public void testMissingAccessTokenValue() {
        Cookie sNSGCs = new Cookie("SNSGCs", "bypass_session_filter1_92_false");
        Cookie[] cookies = {sNSGCs};
        when(req.getCookies()).thenReturn(cookies);
        assertTrue(commonUtil.isSessionTimedOut(req));
    }

    @Test
    public void testAccessTokenCookie() {
        Cookie sNSGCs = new Cookie("SNSGCs", "bypass_session_filter1_92_false3_87_last_access_token1_92_1485243498789");
        Cookie[] cookies = {sNSGCs};
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(commonUtil.getAccessToken(req), "bypass_session_filter1_92_false3_87_last_access_token1_92_1485243498789");
    }

    @Test
    public void testIsSessionTimedOut() {
        Cookie sNSGCs = new Cookie("SNSGCs", "bypass_session_filter1_92_false3_87_last_access_token1_92_1485243498789");
        Cookie[] cookies = {sNSGCs};
        when(req.getCookies()).thenReturn(cookies);
        assertTrue(commonUtil.isSessionTimedOut(req));
    }

    @Test
    public void testSecureTokenPositveTest() {
        Cookie secureCookie = new Cookie("secure_user_token",
                                         "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoU2cg1YgbRvowCGAR3lMy7g==");
        Cookie[] cookies = {secureCookie};
        when(req.getCookies()).thenReturn(cookies);
        String cookieValue = null;
        try {
            cookieValue = commonUtil.getSecureToken(req);
            assertEquals(
                    "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoU2cg1YgbRvowCGAR3lMy7g==",
                    cookieValue);

        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSecureTokenUnsupportedErrorTest() {
        Cookie secureCookie = new Cookie("secure_user_token",
                                         "RM7cT4dR1hDSZLmddSuNqG4A5i5lYsDJGI6LZwqatRga2Bww4V7VDoU2cg1YgbRvowCGAR3lMy7g%%D%%D=");
        Cookie[] cookies = {secureCookie};
        when(req.getCookies()).thenReturn(cookies);
        assertNull(commonUtil.getSecureToken(req));
    }

    @Test
    public void testIsUserSignedIn() throws Exception {
        Cookie[] cookies = {new Cookie("SNSGCs", "bypass_session_filter1_92_false3_87_last_access_token1_92_" + System.currentTimeMillis())};
        when(req.getCookies()).thenReturn(cookies);
        assertTrue(commonUtil.isUserSignedIn(req, "123", "123", 1));
    }

    @Test
    public void testIsUserSignedInTimeout() throws Exception {
        Cookie[] cookies = {new Cookie("SNSGCs", "bypass_session_filter1_92_false3_87_last_access_token1_92_1")};
        when(req.getCookies()).thenReturn(cookies);
        assertFalse(commonUtil.isUserSignedIn(req, "123", "123", 1));
    }

    @Test
    public void testIsUserSignedWrongCookieValue() throws Exception {
        assertFalse(commonUtil.isUserSignedIn(req, "123", "123", 2));
    }

    @Test
    public void testIsUserSignedWrongToken() throws Exception {
        assertFalse(commonUtil.isUserSignedIn(req, "", "123", 1));
    }

    @Test
    public void testIsUserSignedWrongUserId() throws Exception {
        assertFalse(commonUtil.isUserSignedIn(req, "123", "", 1));
    }
}
