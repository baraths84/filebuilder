package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.selection.xapi.list.exception.ListWebApplicationException;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.CookieHandler;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.services.CustomerService;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 *  Deletes a list given a list guid and userid.
 **/
@SpringBootTest
        (classes = {})
//@WebMvcTest(CustomerListResource.class)
public class CustomerListResourceDeleteListTest extends AbstractTestNGSpringContextTests {

    private static final String LIST_GUID_TO_BE_DELETED = "18c7557f-264c-490d-8e9e-956830d55622";
    private static final Long USER_ID_FOR_WHICH_LIST_IS_DELETED = 1234L;
    private static final Integer ONE = 1;

    @Mock
    private CustomerService customerService;

    @Mock
    private CookieHandler cookieHandler;

    @Mock
    private KillSwitchPropertiesBean killswitchProperties;

    @InjectMocks
    private CustomerListResource listResource = new CustomerListResource();

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeletingCustomerList() {

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID_FOR_WHICH_LIST_IS_DELETED);

        HttpServletRequest request = new MockHttpServletRequest();

        ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");
        when(cookieHandler.parseAndValidate(any())).thenReturn(cookie);
        Response response = listResource.deleteList(request, userQueryParam, LIST_GUID_TO_BE_DELETED);
        assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        verify(customerService, times(ONE)).deleteList(any(), any(), any());
    }

    @Test(expectedExceptions = ListWebApplicationException.class)
    public void testDeletingListWhenNoUserIdQueryParam() {
        UserQueryParam userQueryParam = new UserQueryParam();

        HttpServletRequest request = new MockHttpServletRequest();
        listResource.deleteList(request, userQueryParam, LIST_GUID_TO_BE_DELETED);
    }

    @Test(expectedExceptions = ListWebApplicationException.class)
    public void testDeletingListWhenUserQueryParamNull() {
    	UserQueryParam userQueryParam = null;

        HttpServletRequest request = new MockHttpServletRequest();
        listResource.deleteList(request, userQueryParam, LIST_GUID_TO_BE_DELETED);
    }

}