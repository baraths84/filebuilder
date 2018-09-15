package com.macys.selection.xapi.list.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.macys.platform.rest.framework.jaxb.Link;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import org.junit.Assert;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.rest.response.Analytics;
import com.macys.selection.xapi.list.rest.response.AnalyticsMeta;
import com.macys.selection.xapi.list.rest.response.Availability;
import com.macys.selection.xapi.list.rest.response.DigitalAnalytics;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Price;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.Promotion;
import com.macys.selection.xapi.list.rest.response.ReviewStatistics;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.WishList;

public class ListUtilTest extends AbstractTestNGSpringContextTests {

    private static int SIZE_2 = 2;
    private static final double TEST_PRICE = 39.0;
    private static final int TEST_INT = 31277389;
    private static final long TEST_LONG = 20714656027L;
    private static final String TEST_LIST_GUID = "8396726c-68d0-4a1c-b123-ab7c4599bdb9";
    private static final String TEST_ITEM_GUID = "22ea0e5d-a1b2-476b-a647-f9b5a597a312";
    private static final String UPC_AVAILABLE_MESSAGE = "In Stock: Usually ships within 2 business days.";
    private static final String ORDER_METHOD = "POOL";
    private static final String TEST_COLOR = "Very Light";
    private static final String TEST_SIZE = "XS";
    private static final String TEST_PRIMARY_IMAGE = "1573855.fpx";
    private static final int TEST_REVIEW_COUNT = 274;
    private static final int TEST_PROD_ID = 813310;
    private static final int TEST_ITEM_ID = 10594035;
    private static final String IMAGE_URL = "5/optimized/365125_fpx.tif";
    private static final String PRODUCT_NAME = "Clinique Moisture Surge CC Cream Colour Correcting Skin Protector Broad Spectrum SPF 30, 1.4 oz";
    private static final String BADGE_TEXT = "FREE SHIPPING & RETURNS";
    private static final String TEST = "test";
    private static final long PROM_ID = 19873358L;
    private static final String GUEST_LIST = "Guest List";
    private static final String ANOTHER_IMAGE_URL = "5/optimized/1573855_fpx.tif";
    private static final String BASE_URI = "http://111.11.11.111:8080/xapi/";
    private static final String CUSTOMER_HOST = "http://222.22.22.222:8080/";
    private static final String USER_GUID = "123-45dff-erer-434fee";
    private static final long USER_ID = 123L;
    private static final String LIST_GUID = "386afb38-36c9-409f-ab56-318b09ddf4da";
    private static final String LINK_USER_BY_GUID = "http://222.22.22.222:8080/customer/v1/users/userdetails/getuser?userguid=123-45dff-erer-434fee";
    private static final String LINK_USER_BY_ID = "http://222.22.22.222:8080/customer/v1/users/userdetails/getuser?userid=123";
    private static final String LINK_SELF_BY_GUID = "http://111.11.11.111:8080/xapi/wishlist/v1/lists?userGuid=123-45dff-erer-434fee";
    private static final String LINK_SELF_BY_ID = "http://111.11.11.111:8080/xapi/wishlist/v1/lists?userId=123";
    private static final String LINK_LIST_ITEMS = "http://111.11.11.111:8080/xapi/wishlist/v1/lists/386afb38-36c9-409f-ab56-318b09ddf4da?expand=items";
    private WishList wishlist;

    @BeforeMethod
    public void setup() throws ParseException {
        wishlist = buildWishList();
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ListUtil> constructor = ListUtil.class.getDeclaredConstructor();
        AssertJUnit.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testFilterListByAvailability() {

        List<Item> oldItems = new ArrayList<Item>();
        Item item1 = new Item();
        Availability avail1 = new Availability();
        avail1.setAvailable(Boolean.TRUE);
        Upc upc1 = new Upc();
        upc1.setAvailability(avail1);
        item1.setUpc(upc1);
        oldItems.add(item1);

        Item item2 = new Item();
        Availability avail2 = new Availability();
        avail2.setAvailable(Boolean.TRUE);
        Upc upc2 = new Upc();
        upc2.setAvailability(avail2);
        item2.setUpc(upc2);
        oldItems.add(item2);

        Item item3 = new Item();
        Availability avail3 = new Availability();
        avail3.setAvailable(Boolean.FALSE);
        Upc upc3 = new Upc();
        upc3.setAvailability(avail3);
        item3.setUpc(upc3);
        oldItems.add(item3);

        Item item4 = new Item();
        Upc upc4 = new Upc();
        upc4.setAvailability(null);
        item4.setUpc(upc4);
        oldItems.add(item4);

        List<Item> newItems = ListUtil.filterListByAvailability(oldItems);
        assertTrue(newItems.size() == SIZE_2);

    }

    @Test
    public void testBuildAnalyticsMeta() {
        Analytics analytics = new Analytics();
        DigitalAnalytics digitalAnalytics = new DigitalAnalytics();
        WishList list = new WishList();
        list.setListGuid(TEST_LIST_GUID);

        Item item = new Item();
        item.setQtyRequested(1);

        Price price = new Price();
        price.setRetailPrice(TEST_PRICE);
        price.setPriceTypeText(TEST);

        Product product = new Product();
        product.setId(TEST_PROD_ID);
        product.setName(PRODUCT_NAME);
        product.setPrice(price);

        Upc upc = new Upc();
        upc.setUpcNumber(TEST_LONG);
        upc.setPrice(price);
        upc.setProduct(product);

        final List<String> productIdList = new ArrayList<>();
        final List<String> productNameList = new ArrayList<>();
        final List<String> productPriceList = new ArrayList<>();
        final List<String> productPricingStateList = new ArrayList<>();
        final List<String> productQuantityList = new ArrayList<>();
        final List<String> productUPCList = new ArrayList<>();

        productIdList.add(String.valueOf(product.getId()));
        productNameList.add(product.getName());
        productPriceList.add(String.valueOf(price.getRetailPrice()));
        productPricingStateList.add(price.getPriceTypeText());
        productQuantityList.add(String.valueOf(item.getQtyRequested()));
        productUPCList.add(String.valueOf(upc.getUpcNumber()));

        digitalAnalytics.setProductId(productIdList);
        digitalAnalytics.setProductName(productNameList);
        digitalAnalytics.setProductPrice(productPriceList);
        digitalAnalytics.setProductPricingState(productPricingStateList);
        digitalAnalytics.setProductQuantity(productQuantityList);
        digitalAnalytics.setProductUPC(productUPCList);
        digitalAnalytics.setWishListId(String.valueOf(list.getListGuid()));
        analytics.setDigitalAnalytics(digitalAnalytics);
        AnalyticsMeta meta = ListUtil.buildAnalyticsMeta(wishlist);
        assertEquals(analytics, meta.getAnalytics());
    }

    @Test
    public void testBuildAnalytics() {
        DigitalAnalytics digitalAnalytics = new DigitalAnalytics();
        WishList list = new WishList();
        list.setListGuid(TEST_LIST_GUID);

        Item item = new Item();
        item.setQtyRequested(1);

        Price price = new Price();
        price.setRetailPrice(TEST_PRICE);
        price.setPriceTypeText(TEST);

        Product product = new Product();
        product.setId(TEST_PROD_ID);
        product.setName(PRODUCT_NAME);
        product.setPrice(price);

        Upc upc = new Upc();
        upc.setUpcNumber(TEST_LONG);
        upc.setPrice(price);
        upc.setProduct(product);

        final List<String> productIdList = new ArrayList<>();
        final List<String> productNameList = new ArrayList<>();
        final List<String> productPriceList = new ArrayList<>();
        final List<String> productPricingStateList = new ArrayList<>();
        final List<String> productQuantityList = new ArrayList<>();
        final List<String> productUPCList = new ArrayList<>();

        productIdList.add(String.valueOf(product.getId()));
        productNameList.add(product.getName());
        productPriceList.add(String.valueOf(price.getRetailPrice()));
        productPricingStateList.add(price.getPriceTypeText());
        productQuantityList.add(String.valueOf(item.getQtyRequested()));
        productUPCList.add(String.valueOf(upc.getUpcNumber()));

        digitalAnalytics.setProductId(productIdList);
        digitalAnalytics.setProductName(productNameList);
        digitalAnalytics.setProductPrice(productPriceList);
        digitalAnalytics.setProductPricingState(productPricingStateList);
        digitalAnalytics.setProductQuantity(productQuantityList);
        digitalAnalytics.setProductUPC(productUPCList);
        digitalAnalytics.setWishListId(String.valueOf(list.getListGuid()));

        Analytics analytics = ListUtil.buildAnalytics(wishlist);
        assertEquals(digitalAnalytics, analytics.getDigitalAnalytics());
    }

    @Test
    public void testBuildDigitalAnalytics() {
        DigitalAnalytics digitalAnalytics = ListUtil.buildDigitalAnalytics(wishlist);
        assertEquals("813310", digitalAnalytics.getProductId().get(0));
        assertEquals("Clinique Moisture Surge CC Cream Colour Correcting Skin Protector Broad Spectrum SPF 30, 1.4 oz", digitalAnalytics.getProductName().get(0));
        assertEquals("39.0", digitalAnalytics.getProductPrice().get(0));
        assertEquals("test", digitalAnalytics.getProductPricingState().get(0));
        assertEquals("1", digitalAnalytics.getProductQuantity().get(0));
        assertEquals("20714656027", digitalAnalytics.getProductUPC().get(0));
        assertEquals("8396726c-68d0-4a1c-b123-ab7c4599bdb9", digitalAnalytics.getWishListId());
    }

    @Test
    public void testNullProduct_DigitalAnalytics() {
        wishlist.getItems().get(0).getUpc().setProduct(null);
        DigitalAnalytics digitalAnalytics = ListUtil.buildDigitalAnalytics(wishlist);
        assertNull(digitalAnalytics.getProductId());
    }

    @Test
    public void testNullProductPrice_DigitalAnalytics() {
        wishlist.getItems().get(0).getUpc().setPrice(null);
        DigitalAnalytics digitalAnalytics = ListUtil.buildDigitalAnalytics(wishlist);
        assertNull(digitalAnalytics.getProductPrice());
    }

    @Test
    public void testNullUPC_DigitalAnalytics() {
        wishlist.getItems().get(0).setUpc(null);
        DigitalAnalytics digitalAnalytics = ListUtil.buildDigitalAnalytics(wishlist);
        assertNull(digitalAnalytics.getProductUPC());
    }

    @Test
    public void testNullItem_DigitalAnalytics() {
        wishlist.setItems(new ArrayList<>());
        wishlist.getItems().add(null);
        DigitalAnalytics digitalAnalytics = ListUtil.buildDigitalAnalytics(wishlist);
        assertNull(digitalAnalytics.getProductUPC());
        assertEquals("8396726c-68d0-4a1c-b123-ab7c4599bdb9", digitalAnalytics.getWishListId());
    }

    @Test
    public void testNullWishList_DigitalAnalytics() {
        wishlist = null;
        DigitalAnalytics digitalAnalytics = ListUtil.buildDigitalAnalytics(wishlist);
        assertEquals(null, digitalAnalytics.getWishListId());
    }

    public static WishList buildWishList() throws ParseException {
        WishList list = new WishList();
        list.setId(1281186L);
        list.setListGuid(TEST_LIST_GUID);
        list.setName(GUEST_LIST);
        list.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
        list.setDefaultList(Boolean.TRUE);
        list.setOnSaleNotify(Boolean.FALSE);
        list.setSearchable(Boolean.FALSE);
        list.setNumberOfItems(1);
        list.setShowPurchaseInfo(Boolean.TRUE);

        Price price = new Price();
        price.setRetailPrice(TEST_PRICE);
        price.setOriginalPrice(TEST_PRICE);
        price.setIntermediateSalesValue(0.0);
        price.setSalesValue(0.0);
        price.setOnSale(Boolean.FALSE);
        price.setUpcOnSale(Boolean.FALSE);
        price.setPriceType(0);
        price.setBasePriceType(0);
        price.setRetailPriceHigh(0.0);
        price.setPriceTypeText(TEST);

        Availability avail = new Availability();
        avail.setAvailable(Boolean.TRUE);
        avail.setUpcAvailabilityMessage(UPC_AVAILABLE_MESSAGE);
        avail.setInStoreEligible(Boolean.TRUE);
        avail.setOrderMethod(ORDER_METHOD);

        Product product = new Product();
        product.setId(TEST_PROD_ID);
        product.setName(PRODUCT_NAME);
        product.setActive(Boolean.TRUE);
        product.setPrimaryImage(TEST_PRIMARY_IMAGE);
        product.setLive(Boolean.TRUE);
        product.setAvailable(Boolean.TRUE);
        product.setSuppressReviews(Boolean.FALSE);
        ReviewStatistics reviewStatistics = new ReviewStatistics();
        reviewStatistics.setAverageRating(Float.valueOf("4.6095"));
        reviewStatistics.setReviewCount(TEST_REVIEW_COUNT);
        product.setReviewStatistics(reviewStatistics);
        product.setImageURL(ANOTHER_IMAGE_URL);
        product.setPrimaryImage(TEST_PRIMARY_IMAGE);
        product.setClickToCall(TEST);
        product.setMultipleUpc(false);
        product.setPhoneOnly(TEST);
        product.setPrice(price);

        Upc upc = new Upc();
        upc.setId(TEST_INT);
        upc.setUpcNumber(TEST_LONG);
        upc.setColor(TEST_COLOR);
        upc.setSize(TEST_SIZE);
        upc.setPrice(price);
        upc.setAvailability(avail);
        upc.setProduct(product);

        // promotions
        Promotion promotion = new Promotion();
        promotion.setBadgeTextAttributeValue(BADGE_TEXT);
        promotion.setPromotionId(PROM_ID);
        List<Promotion> promotions = new ArrayList<Promotion>();
        promotions.add(promotion);

        Item item = new Item();
        item.setId(TEST_ITEM_ID);
        item.setItemGuid(TEST_ITEM_GUID);
        item.setRetailPriceWhenAdded(TEST_PRICE);
        item.setRetailPriceDropAfterAddedToList(0.0);
        item.setRetailPriceDropPercentage(0);
        item.setQtyRequested(1);
        item.setQtyStillNeeded(0);
        item.setPromotions(promotions);
        item.setProduct(product);
        item.setUpc(upc);
        List<Item> items = new ArrayList<>();
        String imageUrl = IMAGE_URL;
        List<String> imageUrlList = new ArrayList<String>();
        imageUrlList.add(imageUrl);
        list.setImageUrlsList(imageUrlList);
        items.add(item);

        list.setItems(items);
        return list;
    }

    @Test
    public void testPopulateLinksWhenNullWishlists() {
        CustomerList customerList = new CustomerList();
        ListUtil.populateLinks(customerList, USER_ID, null, BASE_URI, CUSTOMER_HOST);
    }

    @Test
    public void testPopulateLinksWhenNoWishlists() {
        CustomerList customerList = new CustomerList();
        customerList.setWishlist(Collections.singletonList(new WishList()));
        ListUtil.populateLinks(customerList, USER_ID, null, BASE_URI, CUSTOMER_HOST);
        List<Link> actualLinks = customerList.getWishlist().get(0).getLinks();
        Assert.assertNotNull(actualLinks);
        Assert.assertEquals(3, actualLinks.size());
        Link user = null;
        Link self = null;
        Link items = null;
        for (Link actualLink : actualLinks) {
            if (LinkTypeEnum.USER.getValue().equals(actualLink.getRel())) {
                user = actualLink;
            } else if (LinkTypeEnum.SELF.getValue().equals(actualLink.getRel())) {
                self = actualLink;
            } else if (LinkTypeEnum.ITEMS.getValue().equals(actualLink.getRel())) {
                items = actualLink;
            }
        }

        Assert.assertEquals(LINK_USER_BY_ID, user.getRef());
        Assert.assertEquals(LINK_SELF_BY_ID, self.getRef());
        Assert.assertNull(items.getRef());
    }

    @Test
    public void testPopulateLinksUsingUserGuid() {
        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setListGuid(LIST_GUID);
        customerList.setWishlist(Collections.singletonList(wishList));
        ListUtil.populateLinks(customerList, USER_ID, USER_GUID, BASE_URI, CUSTOMER_HOST);
        List<Link> actualLinks = customerList.getWishlist().get(0).getLinks();
        Assert.assertNotNull(actualLinks);
        Assert.assertEquals(3, actualLinks.size());
        Link user = null;
        Link self = null;
        Link items = null;
        for (Link actualLink : actualLinks) {
            if (LinkTypeEnum.USER.getValue().equals(actualLink.getRel())) {
                user = actualLink;
            } else if (LinkTypeEnum.SELF.getValue().equals(actualLink.getRel())) {
                self = actualLink;
            } else if (LinkTypeEnum.ITEMS.getValue().equals(actualLink.getRel())) {
                items = actualLink;
            }
        }
        Assert.assertEquals(LINK_USER_BY_GUID, user.getRef());
        Assert.assertEquals(LINK_SELF_BY_GUID, self.getRef());
        Assert.assertEquals(LINK_LIST_ITEMS, items.getRef());
    }

    @Test
    public void testPopulateLinksUsingUserId() {
        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setListGuid(LIST_GUID);
        customerList.setWishlist(Collections.singletonList(wishList));
        ListUtil.populateLinks(customerList, USER_ID, null, BASE_URI, CUSTOMER_HOST);
        List<Link> actualLinks = customerList.getWishlist().get(0).getLinks();
        Assert.assertNotNull(actualLinks);
        Assert.assertEquals(3, actualLinks.size());
        Link user = null;
        Link self = null;
        Link items = null;
        for (Link actualLink : actualLinks) {
            if (LinkTypeEnum.USER.getValue().equals(actualLink.getRel())) {
                user = actualLink;
            } else if (LinkTypeEnum.SELF.getValue().equals(actualLink.getRel())) {
                self = actualLink;
            } else if (LinkTypeEnum.ITEMS.getValue().equals(actualLink.getRel())) {
                items = actualLink;
            }
        }

        Assert.assertEquals(LINK_USER_BY_ID, user.getRef());
        Assert.assertEquals(LINK_SELF_BY_ID, self.getRef());
        Assert.assertEquals(LINK_LIST_ITEMS, items.getRef());
    }

    @Test
    public void testPopulateLinksUriAddedSlash() {
        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setListGuid(LIST_GUID);
        customerList.setWishlist(Collections.singletonList(wishList));
        String baseUriNoSlash = BASE_URI.substring(0, BASE_URI.lastIndexOf("/"));
        String customerHostNoSlash = CUSTOMER_HOST.substring(0, CUSTOMER_HOST.lastIndexOf("/"));
        ListUtil.populateLinks(customerList, USER_ID, null, baseUriNoSlash, customerHostNoSlash);
        List<Link> actualLinks = customerList.getWishlist().get(0).getLinks();
        Assert.assertNotNull(actualLinks);
        Assert.assertEquals(3, actualLinks.size());
        Link user = null;
        Link self = null;
        Link items = null;
        for (Link actualLink : actualLinks) {
            if (LinkTypeEnum.USER.getValue().equals(actualLink.getRel())) {
                user = actualLink;
            } else if (LinkTypeEnum.SELF.getValue().equals(actualLink.getRel())) {
                self = actualLink;
            } else if (LinkTypeEnum.ITEMS.getValue().equals(actualLink.getRel())) {
                items = actualLink;
            }
        }

        Assert.assertEquals(LINK_USER_BY_ID, user.getRef());
        Assert.assertEquals(LINK_SELF_BY_ID, self.getRef());
        Assert.assertEquals(LINK_LIST_ITEMS, items.getRef());
    }

    @Test
    public void testGetAvailableItemsForNull() {
        List<Item> items = ListUtil.filterListByAvailability(null);
        Assert.assertNotNull(items);
        Assert.assertEquals(0, items.size());
    }

    @Test
    public void testGetAvailableItemsForEmptyList() {
        List<Item> items = ListUtil.filterListByAvailability(new ArrayList<>());
        Assert.assertNotNull(items);
        Assert.assertEquals(0, items.size());
    }

    @Test
    public void testGetAvailableItemsForItemWithoutUpc() {
        Item item = new Item();
        List<Item> allItems = Arrays.asList(item);
        List<Item> items = ListUtil.filterListByAvailability(allItems);
        Assert.assertNotNull(items);
        Assert.assertEquals(0, items.size());
    }

    @Test
    public void testGetAvailableItemsForItemWithoutAvailability() {
        Item item = new Item();
        item.setUpc(new Upc());
        List<Item> allItems = Arrays.asList(item);
        List<Item> items = ListUtil.filterListByAvailability(allItems);
        Assert.assertNotNull(items);
        Assert.assertEquals(0, items.size());
    }

    @Test
    public void testGetAvailableItems() {
        Item item1 = new Item();
        item1.setUpc(new Upc());
        item1.getUpc().setAvailability(new Availability());
        Item item2 = new Item();
        item2.setUpc(new Upc());
        item2.getUpc().setAvailability(new Availability());
        item2.getUpc().getAvailability().setAvailable(true);
        List<Item> allItems = Arrays.asList(item1, item2);
        List<Item> items = ListUtil.filterListByAvailability(allItems);
        Assert.assertNotNull(items);
        Assert.assertEquals(1, items.size());
        Assert.assertEquals(item2, items.get(0));
    }
}
