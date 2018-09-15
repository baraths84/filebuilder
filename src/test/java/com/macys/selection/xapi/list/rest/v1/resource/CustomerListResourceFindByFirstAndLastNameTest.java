package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.platform.rest.core.internal.server.context.request.DefaultRequestContext;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.validator.ListItemExitsValidator;
import com.macys.selection.xapi.list.services.CustomerService;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SpringBootTest(classes = {ListItemExitsValidator.class})
public class CustomerListResourceFindByFirstAndLastNameTest extends AbstractTestNGSpringContextTests {

    public static final String FIRST_NAME = "myfirstname";
    public static final String LAST_NAME = "mylastname";
    public static final String STATE = "california";

    @Mock
    private KillSwitchPropertiesBean killswitchProperties;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerListResource listResource = new CustomerListResource();

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindListByFirstAndLastName() {
        ListQueryParam listQueryParam = getListQueryParam();
        listResource.getCustomerList(new UserQueryParam(), listQueryParam, null, new DefaultRequestContext());
        Mockito.verify(customerService).getCustomerList(Mockito.any(UserQueryParam.class), Mockito.eq(listQueryParam), Mockito.any(PaginationQueryParam.class));
    }

    public ListQueryParam getListQueryParam() {
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setFirstName(FIRST_NAME);
        listQueryParam.setLastName(LAST_NAME);
        listQueryParam.setState(STATE);
        return listQueryParam;
    }
}
