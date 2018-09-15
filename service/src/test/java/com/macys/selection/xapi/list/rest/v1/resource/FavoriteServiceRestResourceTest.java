package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.CookieHandler;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.request.validator.ListItemExitsValidator;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.services.CustomerService;
import com.macys.selection.xapi.list.services.PromotionService;
import com.macys.selection.xapi.list.services.WishlistService;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by m940030 on 11/8/17.
 */
public class FavoriteServiceRestResourceTest extends AbstractTestNGSpringContextTests {


    private static final int TIME_1 = 1;
    public static final String USER_GUID = "924de083-4ff5-401b-9108-af6654d5e7d8";
    private static final String TEST_STRING = "any string";
    private static final String SECURE_TOKEN = "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==";;

    @Mock
    private CustomerService customerService;
    @Mock
    private WishlistService wishlistService;
    @Mock
    PromotionService promotionService;
    @Mock
    private KillSwitchPropertiesBean killswitchProperties;

    @Mock
    private CookieHandler cookieHandler;
    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private FavoriteRestResource favResource = new FavoriteRestResource();

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
        ListCookies cookies = new ListCookies(null, USER_GUID, SECURE_TOKEN);
        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(cookies);
    }


    @Test
    public void testAddFavItemToGivenListByPID() {
        Long userId = 12345L;
        final String userGuid = "user_guid_123";

        CustomerList inputWishlist = new CustomerList();
        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(new ListCookies(userId, userGuid, SECURE_TOKEN));
        Response response = favResource.addFavItemToGivenListByPID(httpServletRequest, inputWishlist);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(customerService, times(TIME_1)).addFavItemToGivenListByPID(any(), any());
        verify(wishlistService, never()).addItemToDefaultWishlist(any());
    }

    @Test
    public void testGetFavItemFromListByGuid() {
        Long userId = 12345L;
        final String userGuid = "user_guid_123";

        HttpServletRequest request = new MockHttpServletRequest();
        when(cookieHandler.parseAndValidate(request)).thenReturn(new ListCookies(userId, userGuid, SECURE_TOKEN));
        Response response = favResource.getFavItemFromListByGuid(request, USER_GUID);
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(customerService, times(TIME_1)).getFavItemFromListByGuid(any(), any());
        verify(wishlistService, never()).getFavoriteItemsFromDefaultList(any());
    }

    @Test
    public void testDeleteFavorite() {
        Long userId = 12345L;
        final String userGuid = "user_guid_123";

        when(cookieHandler.parseAndValidate(httpServletRequest)).thenReturn(new ListCookies(userId, userGuid, SECURE_TOKEN));
        Response response = favResource.deleteFavorite(httpServletRequest, TEST_STRING, new ListQueryParam());
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        verify(customerService, never()).getCustomerList(any(), any(), any());
        verify(customerService, never()).getCustomerListByGuid(any(), any(), any(), any());
        verify(customerService, times(TIME_1)).deleteFavorite(any(), any(), any());
        verify(wishlistService, never()).deleteFavoriteItem(any(), any());
    }
}
