package com.macys.selection.xapi.list.util;

import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class CustomerRequestParamUtilFirstLastNameTest extends AbstractTestNGSpringContextTests {

    public static final long USER_ID = 201306195;
    public static final int TEST_QUERY_PARAM_SIZE = 4;
    public static final String FIRST_NAME = "myNameisFirst";
    public static final String LAST_NAME = "myNameIsLast";
    public static final String STATE = "myState";

    private CustomerRequestParamUtil requestParamUtil = new CustomerRequestParamUtil();
    private UserQueryParam userQueryParam;
    private ListQueryParam listQueryParam;
    private PaginationQueryParam paginationQueryParam;

    @BeforeMethod
    public void setup() {
        userQueryParam = getUserQueryParam();
        listQueryParam = getListQueryParam();
        paginationQueryParam = getPaginationQueryParam();
    }

    @Test
    public void testFirstLastNameAndStateParameters() {

        Map<CustomerQueryParameterEnum, String> listQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);

        assertEquals(listQueryParamMap.size(), TEST_QUERY_PARAM_SIZE);
        assertTrue(listQueryParamMap.containsKey(CustomerQueryParameterEnum.FIRSTNAME));
        assertTrue(listQueryParamMap.containsKey(CustomerQueryParameterEnum.LASTNAME));
        assertTrue(listQueryParamMap.containsKey(CustomerQueryParameterEnum.STATE));

        assertTrue(listQueryParamMap.containsValue(FIRST_NAME));
        assertTrue(listQueryParamMap.containsValue(LAST_NAME));
        assertTrue(listQueryParamMap.containsValue(STATE));

    }

    /**
     * @return UserQueryParam
     */
    public static UserQueryParam getUserQueryParam() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        return userQueryParam;
    }

    /**
     * @return ListQueryParam
     */
    public static ListQueryParam getListQueryParam() {
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setFirstName(FIRST_NAME);
        listQueryParam.setLastName(LAST_NAME);
        listQueryParam.setState(STATE);
        return listQueryParam;
    }

    /**
     * @return PaginationQueryParam
     */
    public static PaginationQueryParam getPaginationQueryParam() {
        return new PaginationQueryParam();
    }


}
