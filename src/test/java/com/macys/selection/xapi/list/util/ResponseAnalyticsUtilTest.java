package com.macys.selection.xapi.list.util;

import static com.macys.selection.xapi.list.common.WishlistConstants.DECIMAL_FORMAT;
import static com.macys.selection.xapi.list.common.WishlistConstants.WISHLIST_ADD;
import static org.testng.Assert.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.DecimalFormat;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.rest.response.Analytics;
import com.macys.selection.xapi.list.rest.response.AnalyticsMeta;
import com.macys.selection.xapi.list.rest.response.Availability;
import com.macys.selection.xapi.list.rest.response.ResponseDigitalAnalytics;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Price;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.Promotion;
import com.macys.selection.xapi.list.rest.response.ReviewStatistics;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.rest.response.CustomerList;



public class ResponseAnalyticsUtilTest extends AbstractTestNGSpringContextTests {
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

    private WishList wishlist;
    private CustomerList customerList;

    @BeforeMethod
    public void setup() throws ParseException {
        wishlist = buildWishList();
        customerList = new CustomerList();
        customerList.setWishlist(Collections.singletonList(wishlist));
    }


    @Test
    public void testBuildAnalyticsMeta() {
        Analytics analytics = new Analytics();
        ResponseDigitalAnalytics  responseDigitalAnalytics = new ResponseDigitalAnalytics();
        CustomerList cList = new CustomerList();

        WishList list = new WishList();
        list.setListGuid(TEST_LIST_GUID);
        cList.setWishlist(Collections.singletonList(list));

        Item item = new Item();
        item.setQtyRequested(1);

        Price price = new Price();
        price.setOriginalPrice(TEST_PRICE);
        price.setRetailPrice(TEST_PRICE);
        price.setPriceTypeText(TEST);

        Product product = new Product();
        product.setId(TEST_PROD_ID);
        product.setName(PRODUCT_NAME);
        product.setPrice(price);

        Upc upc = new Upc();
        upc.setUpcNumber(TEST_LONG);
        upc.setPrice(price);
        upc.setColor(TEST_COLOR);
        upc.setSize(TEST_SIZE);
        upc.setProduct(product);

        final List<String> productIdList = new ArrayList<>();
        final List<String> productNameList = new ArrayList<>();
        final List<String> productPriceList = new ArrayList<>();
        final List<String> productOriginaPriceList = new ArrayList<>();
        final List<String> productSizeList = new ArrayList<>();
        final List<String> productColorList = new ArrayList<>();
        final List<String> productUPCList = new ArrayList<>();

         String priceText = new DecimalFormat(DECIMAL_FORMAT).format(TEST_PRICE);

        productIdList.add(String.valueOf(product.getId()));
        productNameList.add(product.getName());
        productOriginaPriceList.add(priceText);
        productPriceList.add(String.valueOf(priceText));
        productSizeList.add(upc.getSize());
        productColorList.add(upc.getColor());
        productUPCList.add(String.valueOf(upc.getUpcNumber()));


        responseDigitalAnalytics.setEventName(WISHLIST_ADD);
        responseDigitalAnalytics.setProductId(productIdList);
        responseDigitalAnalytics.setProductName(productNameList);
        responseDigitalAnalytics.setProductPrice(productPriceList);
        responseDigitalAnalytics.setProductOriginalPrice(productOriginaPriceList);
        responseDigitalAnalytics.setProductSize(productSizeList);
        responseDigitalAnalytics.setProductColor(productColorList);
        responseDigitalAnalytics.setProductUPC(productUPCList);


        analytics.setResponseDigitalAnalytics(responseDigitalAnalytics);
        AnalyticsMeta meta = ResponseAnalyticsUtil.buildResponseAnalyticsMeta(customerList);
        assertEquals(analytics, meta.getAnalytics());
    }

    @Test
    public void testBuildDigitalAnalytics() {
        AnalyticsMeta meta = ResponseAnalyticsUtil.buildResponseAnalyticsMeta(customerList);
        Analytics analytics = meta.getAnalytics();
        assertEquals("Analytics{data=ResponseDigitalAnalytics{" +
                             "event_name=wishlist add, " +
                             "product_id=[813310], " +
                             "product_name=[Clinique Moisture Surge CC Cream Colour Correcting Skin Protector Broad Spectrum SPF 30, 1.4 oz], " +
                             "product_original_price=[39.00], " +
                             "product_price=[39.00], " +
                             "product_size=[XS], " +
                             "product_color=[Very Light], " +
                             "product_upc=[20714656027]}}", analytics.toString());
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

}
