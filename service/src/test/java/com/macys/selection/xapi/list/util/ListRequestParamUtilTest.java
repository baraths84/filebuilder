package com.macys.selection.xapi.list.util;

import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListRequestParamUtilTest extends AbstractTestNGSpringContextTests {

    public static final long USER_ID = 201306195;
    public static final long USER_ID_2 = 201806000;
    public static final String USER_GUID = "924de083-4ff5-401b-9108-af6654d5e7d8";
    public static final int UPC_ID = 123400;
    public static final int PRODUCT_ID = 86800;

    private ListRequestParamUtil requestParamUtil = new ListRequestParamUtil();

    @Mock
    private RxWebTarget rxWebTarget;


    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
    }

    @Test
    public void testAddUserQueryParamWithAllParams() {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserId(USER_ID);
        userQueryParam.setUserGuid(USER_GUID);
        userQueryParam.setGuestUser(true);
        List<Long> userIds = Arrays.asList(USER_ID, USER_ID_2);
        userQueryParam.setUserIds(userIds);

        requestParamUtil.addUserQueryParam(rxWebTarget, userQueryParam);

        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_GUID.getParamName(), USER_GUID);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_ID.getParamName(), String.valueOf(USER_ID));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.USER_IDS.getParamName(), USER_ID + "," + USER_ID_2);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.GUEST_USER.getParamName(), "true");
        verify(rxWebTarget, times(4)).queryParam(anyString(), any());
    }

    @Test
    public void testAddUserQueryParamWithoutParams() {
        UserQueryParam userQueryParam = new UserQueryParam();

        requestParamUtil.addUserQueryParam(rxWebTarget, userQueryParam);

        verify(rxWebTarget, never()).queryParam(anyString(), any());
    }

    @Test
    public void testAddListQueryParam() {
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setDefaultList(true);
        listQueryParam.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
        listQueryParam.setSortBy("name");
        listQueryParam.setSortOrder("asc");
        listQueryParam.setListLimit(5);
        listQueryParam.setFilter("testFilter");
        listQueryParam.setFields("testField");
        listQueryParam.setExpand("testExpand");
        listQueryParam.setUpcId(UPC_ID);
        listQueryParam.setProductId(PRODUCT_ID);
        listQueryParam.setFirstName("userFirstName");
        listQueryParam.setLastName("userLastName");
        listQueryParam.setState("testState");

        requestParamUtil.addListQueryParam(rxWebTarget, listQueryParam);

        verify(rxWebTarget).queryParam(ListQueryParameterEnum.DEFAULT.getParamName(), "true");
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.LIST_TYPE.getParamName(), WishlistConstants.WISH_LIST_TYPE_VALUE);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.LIST_LIMIT.getParamName(), "5");
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.FILTER.getParamName(), "testFilter");
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.FIELDS.getParamName(), "testField");
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.EXPAND.getParamName(), "testExpand");
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.UPC_ID.getParamName(), String.valueOf(UPC_ID));
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.PRODUCT_ID.getParamName(), String.valueOf(PRODUCT_ID));
        verify(rxWebTarget, times(8)).queryParam(anyString(), any());
    }

    @Test
    public void testAddListQueryParamExcludeExtraExpand() {
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_USER + ListRequestParamUtil.DIVIDER + ListRequestParamUtil.EXPAND_ITEMS);
        requestParamUtil.addListQueryParam(rxWebTarget, listQueryParam);
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.EXPAND.getParamName(), ListRequestParamUtil.EXPAND_ITEMS);
    }

    @Test
    public void testAddListQueryParamWithEmptyParams() {
        ListQueryParam listQueryParam = new ListQueryParam();

        requestParamUtil.addListQueryParam(rxWebTarget, listQueryParam);

        verify(rxWebTarget, never()).queryParam(anyString(), any());
    }

    @Test
    public void testAddPaginationQueryParam() {
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        paginationQueryParam.setLimit(23);
        paginationQueryParam.setOffset(34);

        requestParamUtil.addPaginationQueryParam(rxWebTarget, paginationQueryParam);

        verify(rxWebTarget).queryParam(ListQueryParameterEnum.LIMIT.getParamName(), "23");
        verify(rxWebTarget).queryParam(ListQueryParameterEnum.OFFSET.getParamName(), "34");
        verify(rxWebTarget, times(2)).queryParam(anyString(), any());
    }

    @Test
    public void testAddPaginationQueryParamWithEmptyParams() {
        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();

        requestParamUtil.addPaginationQueryParam(rxWebTarget, paginationQueryParam);

        verify(rxWebTarget, never()).queryParam(anyString(), any());
    }

}
