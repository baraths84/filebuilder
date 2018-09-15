package com.macys.selection.xapi.list.util;

import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Profile;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.rest.v1.resource.CustomerListResourceTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class CustomerRequestParamUtilTest extends AbstractTestNGSpringContextTests {

    public static final long USER_ID = 201306195;
    public static final String USER_GUID = "924de083-4ff5-401b-9108-af6654d5e7d8";
    public static final long LIST_ID = 10680033;
    public static final String LIST_GUID = "ee15b1e1-2a85-46ef-bb82-236b4068870b";
    private static final String TEST_STRING = "any string";
    private static final int TEST_INT = 40;
    private static final int TEST_QUERY_PARAM_SIZE = 13;
    private static final String AVAILABLE = "available";
    private static final String ADDED_DATE = "addedDate";
    private static final String NAME = "name";
    private static final String DESC = "desc";
    private static final int TWO = 2;
    private static final int FOUR = 4;
    private static final int TWENTY = 20;
    private static final int SIX = 6;
    private static final int NINE = 9;
    private static final int ONE = 1;
    private static final int ZERO = 0;
    public static final int PRODUCT_ID = 86800;

    private CustomerRequestParamUtil requestParamUtil = new CustomerRequestParamUtil();
    private UserQueryParam userQueryParam;
    private ListQueryParam listQueryParam;
    private PaginationQueryParam paginationQueryParam;
    private CustomerListResourceTest customerListResourceTest;

    @BeforeMethod
    public void setup() {
        userQueryParam = getUserQueryParam();
        listQueryParam = getListQueryParam();
        paginationQueryParam = getPaginationQueryParam();
    }

    @Test
    public void queryParamTest() {
        userQueryParam.setUserGuid(USER_GUID);
        listQueryParam.setFields(NAME);
        listQueryParam.setFilter(AVAILABLE);
        listQueryParam.setListLimit(TEST_INT);
        listQueryParam.setSortBy(ADDED_DATE);
        listQueryParam.setSortOrder(DESC);
        paginationQueryParam.setLimit(TEST_INT);
        paginationQueryParam.setOffset(TEST_INT);
        listQueryParam.setProductId(PRODUCT_ID);

        Map<CustomerQueryParameterEnum, String> listQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);

        assertEquals(listQueryParamMap.size(), TEST_QUERY_PARAM_SIZE);
    }

    @Test
    public void emptyQueryParamTest() {
        userQueryParam = new UserQueryParam();
        listQueryParam = new ListQueryParam();
        paginationQueryParam = new PaginationQueryParam();
        Map<CustomerQueryParameterEnum, String> listQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);

        assertEquals(listQueryParamMap.size(), ZERO);
    }

    @Test
    public void userQueryParamTest() {
        userQueryParam.setUserGuid(USER_GUID);
        listQueryParam = new ListQueryParam();
        paginationQueryParam = new PaginationQueryParam();

        Map<CustomerQueryParameterEnum, String> wishListQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);
        assertEquals(wishListQueryParamMap.size(), TWO);
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.USERID));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.USERGUID));
    }

    @Test
    public void userQueryParamUserIdTest() {
        listQueryParam = null;
        paginationQueryParam = null;

        Map<CustomerQueryParameterEnum, String> wishListQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);
        assertEquals(wishListQueryParamMap.size(), ONE);
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.USERID));
        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.USERGUID));
    }

    @Test
    public void userQueryParamUserGuidTest() {
        userQueryParam.setUserId(null);
        userQueryParam.setUserGuid(USER_GUID);
        listQueryParam = null;
        paginationQueryParam = null;

        Map<CustomerQueryParameterEnum, String> wishListQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);
        assertEquals(wishListQueryParamMap.size(), ONE);
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.USERGUID));
        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.USERID));
    }

    @Test
    public void listQueryParamTest() {
        listQueryParam.setFields(NAME);
        listQueryParam.setFilter(AVAILABLE);
        listQueryParam.setListLimit(TEST_INT);
        listQueryParam.setSortBy(ADDED_DATE);
        listQueryParam.setSortOrder(DESC);
        listQueryParam.setProductId(PRODUCT_ID);
        paginationQueryParam = null;
        userQueryParam = null;

        Map<CustomerQueryParameterEnum, String> wishListQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);
        assertEquals(wishListQueryParamMap.size(), NINE);

        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.DEFAULT));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.LISTTYPE));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.SORTBY));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.SORTORDER));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.LISTLIMIT));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.FILTER));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.FIELDS));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.EXPAND));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.PRODUCTID));
    }

    @Test
    public void listQueryParamMainParamsTest() {
        paginationQueryParam = null;
        userQueryParam = null;

        Map<CustomerQueryParameterEnum, String> wishListQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);
        assertEquals(wishListQueryParamMap.size(), FOUR);
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.DEFAULT));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.LISTTYPE));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.EXPAND));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.PRODUCTID));

        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.SORTBY));
        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.SORTORDER));
        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.LISTLIMIT));
        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.FILTER));
        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.FIELDS));
    }

    @Test
    public void listQueryParamSecondaryParamsTest() {
        listQueryParam.setDefaultList(null);
        listQueryParam.setListGuid("");
        listQueryParam.setListType(null);
        listQueryParam.setExpand("");
        listQueryParam.setFields(NAME);
        listQueryParam.setFilter(AVAILABLE);
        listQueryParam.setListLimit(TEST_INT);
        listQueryParam.setSortBy(ADDED_DATE);
        listQueryParam.setSortOrder(DESC);

        paginationQueryParam = new PaginationQueryParam();
        userQueryParam = new UserQueryParam();

        Map<CustomerQueryParameterEnum, String> wishListQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);
        assertEquals(wishListQueryParamMap.size(), SIX);

        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.SORTBY));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.SORTORDER));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.LISTLIMIT));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.FILTER));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.FIELDS));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.PRODUCTID));

        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.LISTGUID));
        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.DEFAULT));
        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.LISTTYPE));
        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.EXPAND));
    }

    @Test
    public void paginationQueryParamTest() {
        paginationQueryParam.setLimit(TWENTY);
        paginationQueryParam.setOffset(SIX);
        userQueryParam = null;
        listQueryParam = null;

        Map<CustomerQueryParameterEnum, String> wishListQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);
        assertEquals(wishListQueryParamMap.size(), TWO);
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.OFFSET));
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.LIMIT));
    }

    @Test
    public void paginationQueryParamLimitParamTest() {
        paginationQueryParam.setLimit(TWENTY);
        userQueryParam = null;
        listQueryParam = null;

        Map<CustomerQueryParameterEnum, String> wishListQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);
        assertEquals(wishListQueryParamMap.size(), ONE);
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.LIMIT));
        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.OFFSET));
    }

    @Test
    public void paginationQueryParamOffsetParamTest() {
        paginationQueryParam.setOffset(SIX);
        userQueryParam = new UserQueryParam();
        listQueryParam = new ListQueryParam();

        Map<CustomerQueryParameterEnum, String> wishListQueryParamMap = requestParamUtil
                .createGetListQueryParamMap(userQueryParam, listQueryParam, paginationQueryParam);
        assertEquals(wishListQueryParamMap.size(), ONE);
        assertTrue(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.OFFSET));
        assertFalse(wishListQueryParamMap.containsKey(CustomerQueryParameterEnum.LIMIT));
    }

    @Test
    public void testFirstLastNameAndStateParameters() {

    }

    @Test
    public void test_buildResponseWithFilterOptions_onSale_promotions(){
        String[] tokens = new String[2];
        tokens[0] = "onSale";
        tokens[1] = "promotions";
        customerListResourceTest = new CustomerListResourceTest();
        CustomerList response = new CustomerList();
        response = setCustpmerListDataForBcom(tokens,response);
            assertNotNull(response);
            Assert.assertEquals(response.getWishlist().get(0).getItems().size(),3);
            Assert.assertEquals(response.getWishlist().get(0).getItems().get(0).getPromotions().size(),1);
            Assert.assertTrue(response.getWishlist().get(0).getItems().get(0).getUpc().getPrice().getOnSale());

            Assert.assertEquals(response.getWishlist().get(0).getItems().get(1).getPromotions().size(),1);
            Assert.assertTrue(response.getWishlist().get(0).getItems().get(1).getUpc().getPrice().getOnSale());

            Assert.assertEquals(response.getWishlist().get(0).getItems().get(2).getPromotions().size(),1);
            Assert.assertFalse(response.getWishlist().get(0).getItems().get(2).getUpc().getPrice().getOnSale());
    }
    @Test
    public void test_buildResponseWithFilterOptions_onSale(){
        String[] tokens = new String[2];
        tokens[0] = "onSale";

        customerListResourceTest = new CustomerListResourceTest();
        CustomerList response = new CustomerList();
        response = setCustpmerListDataForBcom(tokens,response);


        assertNotNull(response);
            Assert.assertEquals(response.getWishlist().get(0).getItems().size(),2);
            Assert.assertEquals(response.getWishlist().get(0).getItems().get(0).getPromotions().size(),1);
            Assert.assertTrue(response.getWishlist().get(0).getItems().get(0).getUpc().getPrice().getOnSale());

            Assert.assertEquals(response.getWishlist().get(0).getItems().get(1).getPromotions().size(),1);
            Assert.assertTrue(response.getWishlist().get(0).getItems().get(1).getUpc().getPrice().getOnSale());
   }
    @Test
    public void test_buildResponseWithFilterOptions_promotions(){
        String[] tokens = new String[2];
        tokens[0] = "promotions";

        customerListResourceTest = new CustomerListResourceTest();
        CustomerList response = new CustomerList();
        response =  setCustpmerListDataForBcom(tokens,response);


        assertNotNull(response);
        Assert.assertEquals(response.getWishlist().get(0).getItems().size(),3);
        Assert.assertEquals(response.getWishlist().get(0).getItems().get(0).getPromotions().size(),1);
        Assert.assertFalse(response.getWishlist().get(0).getItems().get(0).getUpc().getPrice().getOnSale());

        Assert.assertEquals(response.getWishlist().get(0).getItems().get(1).getPromotions().size(),1);
        Assert.assertTrue(response.getWishlist().get(0).getItems().get(1).getUpc().getPrice().getOnSale());

        Assert.assertEquals(response.getWishlist().get(0).getItems().get(2).getPromotions().size(),1);
        Assert.assertTrue(response.getWishlist().get(0).getItems().get(2).getUpc().getPrice().getOnSale());
    }

   public CustomerList setCustpmerListDataForBcom(String[] tokens,CustomerList response){

       try {
           List<WishList> customerList = customerListResourceTest.expectedWishListFilterOptionsBcom();
           WishList listWithPromotions = customerListResourceTest.buildListWithPromotions(customerList.get(0));
           CustomerList expectedCustomerList = new CustomerList();

           List<WishList> lists = new ArrayList<>();
           lists.add(listWithPromotions);
           response.setWishlist(lists);
           response.setUser(getUser());
           requestParamUtil.buildResponseWithFilterOptions(response,tokens);

       } catch (ParseException e) {

       }
       return response;
   }
    public static User getUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setGuid(USER_GUID);
        user.setGuestUser(true);
        Profile profile = new Profile();
        profile.setFirstName(TEST_STRING);
        user.setProfile(profile);
        return user;
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
        listQueryParam.setDefaultList(true);
        listQueryParam.setListGuid(TEST_STRING);
        listQueryParam.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
        listQueryParam.setExpand(TEST_STRING);
        listQueryParam.setProductId(PRODUCT_ID);

        return listQueryParam;
    }

    /**
     * @return PaginationQueryParam
     */
    public static PaginationQueryParam getPaginationQueryParam() {
        return new PaginationQueryParam();
    }


}
