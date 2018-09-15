package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.CookieHandler;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.KillSwitches;
import com.macys.selection.xapi.list.services.CustomerService;
import com.macys.selection.xapi.list.services.PromotionService;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import com.macys.selection.xapi.list.util.ListUtil;
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
public class DKSPropertyResourceTest extends AbstractTestNGSpringContextTests {


    @Mock
    private KillSwitchPropertiesBean killswitchProperties;


    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetDKSInfo() {

        KillSwitches response = ListUtil.updateProperties(killswitchProperties);
        assertNotNull(response);
    }
}
