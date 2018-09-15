package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.CustomerServiceRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.response.CustomerWishListsResponse;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.*;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.response.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertNotNull;

@SpringBootTest
public class CustomerServiceFindByFirstAndLastName extends AbstractTestNGSpringContextTests {

    public static final String FIRST_NAME = "myfirstname";
    public static final String LAST_NAME = "mylastname";
    public static final String STATE = "california";
    public static final Integer ONE = 1;
    private static String WISH_LIST_JSON_FILE = "com/macys/selection/xapi/wishlist/converters/wishlist.json";
    private static String TEST_LIST_GUID = "2b39e929-23ef-4d34-9874-5833550c1c5f";
    private static final long LIST_ID = 10680246L;
    private static final String TEST_LIST_NAME = "testName";
    private static final int QTY = 1;
    private static final int TEST_ID = 4835927;
    private static final double TEST_PRICE = 99.0;
    private static final long TEST_LONG = 3213253243L;

    @Mock
    private CustomerServiceRestClient restClient;

    @InjectMocks
    private CustomerService customerService;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    	JsonToObjectConverter<CustomerWishListsResponse> wishlistsConverter = new JsonToObjectConverter<>(CustomerWishListsResponse.class);
    	customerService.setWishlistsConverter(wishlistsConverter);
    }

    @Test
    public void testGetCustomerListByFirstAndLastName() throws IOException, ParseException {
        String mspCustomerResponse = TestUtils.readFile(WISH_LIST_JSON_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setBody(mspCustomerResponse);
        restResponse.setStatusCode(Response.Status.OK.getStatusCode());

        when(restClient.get(any(), any(), any())).thenReturn(restResponse);

        CustomerList customerList = customerService.getCustomerList(new UserQueryParam(), getListQueryParam(), new PaginationQueryParam());

        assertNotNull(customerList);
        verify(restClient, times(ONE)).get(any(), any(), any());
    }

    public ListQueryParam getListQueryParam() {
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setFirstName(FIRST_NAME);
        listQueryParam.setLastName(LAST_NAME);
        listQueryParam.setState(STATE);
        return listQueryParam;
    }

    public static List<WishList> expectedWishList() throws ParseException {
        List<WishList> lists = new ArrayList<>();
        WishList list = new WishList();
        list.setId(LIST_ID);
        list.setListGuid(TEST_LIST_GUID);
        list.setName(TEST_LIST_NAME);
        list.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
        list.setDefaultList(Boolean.TRUE);
        list.setOnSaleNotify(Boolean.FALSE);
        list.setSearchable(Boolean.FALSE);
        list.setNumberOfItems(1);
        list.setShowPurchaseInfo(Boolean.TRUE);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        list.setCreatedDate(df.parse("2017-09-14T18:27:54.201"));
        list.setLastModified(df.parse("2017-09-14T18:27:54.201"));

        User user = new User();
        user.setId(LIST_ID);
        user.setGuid(TEST_LIST_GUID);
        user.setGuestUser(Boolean.TRUE);

        Price price = new Price();
        price.setRetailPrice(TEST_PRICE);
        price.setOriginalPrice(TEST_PRICE);
        price.setIntermediateSalesValue(TEST_PRICE);
        price.setSalesValue(TEST_PRICE);
        price.setOnSale(Boolean.FALSE);
        price.setUpcOnSale(Boolean.FALSE);
        price.setPriceType(QTY);
        price.setBasePriceType(QTY);

        Availability avail = new Availability();
        avail.setAvailable(Boolean.TRUE);
        avail.setUpcAvailabilityMessage(TEST_LIST_GUID);
        avail.setInStoreEligible(Boolean.TRUE);
        avail.setOrderMethod(TEST_LIST_GUID);

        Product product = new Product();
        product.setId(TEST_ID);
        product.setName(TEST_LIST_NAME);
        product.setActive(Boolean.TRUE);
        product.setPrimaryImage(TEST_LIST_NAME);
        product.setLive(Boolean.TRUE);
        product.setAvailable(Boolean.TRUE);

        Upc upc = new Upc();
        upc.setId(TEST_ID);
        upc.setUpcNumber(TEST_LONG);
        upc.setColor(TEST_LIST_NAME);
        upc.setSize(TEST_LIST_NAME);
        upc.setPrice(price);
        upc.setAvailability(avail);
        upc.setProduct(product);

        Item item = new Item();
        item.setId(TEST_ID);
        item.setItemGuid(TEST_LIST_GUID);
        item.setRetailPriceWhenAdded(TEST_PRICE);
        item.setRetailPriceDropAfterAddedToList(TEST_PRICE);
        item.setRetailPriceDropPercentage(QTY);
        item.setQtyRequested(QTY);
        item.setQtyStillNeeded(QTY);

        List<Promotion> samplePromotions = new ArrayList<>();
        Promotion promotion = new Promotion();
        promotion.setBadgeTextAttributeValue(TEST_LIST_GUID);
        promotion.setPromotionId(TEST_LONG);
        samplePromotions.add(promotion);
        item.setPromotions(samplePromotions);

        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        item.setAddedDate(tempDate.parse("2017-09-14T18:27:54.203"));
        item.setLastModified(tempDate.parse("2017-09-14T18:27:54.204"));
        item.setUpc(upc);
        List<Item> items = new ArrayList<>();
        items.add(item);
        list.setItems(items);

        lists.add(list);
        return lists;
    }


}
