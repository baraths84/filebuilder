package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.selection.xapi.list.exception.ErrorConstants;
import com.macys.selection.xapi.list.exception.ListWebApplicationException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.CookieHandler;
import com.macys.selection.xapi.list.rest.request.validator.ListItemExitsValidator;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.FavoriteList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.services.CustomerService;
import com.macys.selection.xapi.list.services.WishlistService;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FavoriteRestResourceMspWishlistEnabledTest {
    private static final String USER_GUID = "userGuid";
    private static final String LIST_GUID = "listGuid";
    @InjectMocks
    private FavoriteRestResource favoriteRestResource = new FavoriteRestResource();
    @Mock
    private WishlistService wishlistService;
    @Mock
    private CustomerService customerService;
    @Mock
    private ListItemExitsValidator listItemExitsValidator;
    @Mock
    private CookieHandler cookieHandler;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private KillSwitchPropertiesBean killswitchProperties;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddFavItemToGivenListByPID() {
        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(Collections.singletonList(new Item()));
        customerList.setWishlist(Collections.singletonList(wishList));
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(listItemExitsValidator.isValid(customerList)).thenReturn(Boolean.TRUE);
        when(wishlistService.addItemToDefaultWishlist(customerList)).thenReturn(customerList);
        Response response = favoriteRestResource.addFavItemToGivenListByPID(httpServletRequest, customerList);
        verify(customerService, never()).addFavItemToGivenListByPID(any(), any());
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getEntity());
        Assert.assertNotNull(((CustomerList) response.getEntity()).getWishlist());
        Assert.assertNull(((CustomerList) response.getEntity()).getWishlist().get(0).getItems());
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test(expectedExceptions = ListWebApplicationException.class)
    public void testAddFavItemToGivenListByPIDInvalidInput() {
        try {
            CustomerList customerList = new CustomerList();
            when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
            when(listItemExitsValidator.isValid(customerList)).thenReturn(Boolean.FALSE);
            favoriteRestResource.addFavItemToGivenListByPID(httpServletRequest, customerList);
            verify(wishlistService, never()).addItemToDefaultWishlist(any());
        } catch (ListWebApplicationException e) {
            Assert.assertEquals(ErrorConstants.BAD_JASON_INPUT_ITMES_NOT_AVAILABLE, e.getMessage());
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
            throw e;
        }
    }

    @Test
    public void testGetFavItemFromListByGuid() {
        FavoriteList favoriteList = new FavoriteList();
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        when(wishlistService.getFavoriteItemsFromDefaultList(USER_GUID)).thenReturn(favoriteList);
        Response response = favoriteRestResource.getFavItemFromListByGuid(httpServletRequest, USER_GUID);
        verify(customerService, never()).getFavItemFromListByGuid(any(), any());
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getEntity());
        Assert.assertEquals(response.getEntity(), favoriteList);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testDeleteFavorite() {
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setUpcId(111);
        listQueryParam.setProductId(222);
        when(killswitchProperties.isMspListEnabled()).thenReturn(Boolean.TRUE);
        Response response = favoriteRestResource.deleteFavorite(httpServletRequest, LIST_GUID, listQueryParam);
        verify(wishlistService).deleteFavoriteItem(LIST_GUID, listQueryParam);
        verify(customerService, never()).deleteFavorite(any(), any(), any());
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
    }
}
