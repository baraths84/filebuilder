package com.macys.selection.xapi.list.comparators;

import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.response.Availability;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Price;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.ReviewStatistics;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.WishList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ListSortingExecutorTest {

    @Autowired
    private ListSortingExecutor listSortingExecutor;

    @Configuration
    @ComponentScan(basePackages = "com.macys.selection.xapi.list.comparators")
    static class Config {

    }

    @Test(expected = ListServiceException.class)
    public void testValidateSortBy() {
        try {
            listSortingExecutor.validateSortField("invalid_sort_by_field");
        } catch(ListServiceException e) {
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_SORT_BY_FIELD.getInternalCode(), e.getServiceErrorCode());
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            throw e;
        }
    }
    @Test(expected = ListServiceException.class)
    public void testValidateSortOrder() {
        try {
            listSortingExecutor.validateSortOrder("invalid_sort_order");
        } catch(ListServiceException e) {
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_SORT_BY_ORDER.getInternalCode(), e.getServiceErrorCode());
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            throw e;
        }
    }

    @Test
    public void testGetItemsSortedByUnitPriceAsc() {
        Item itemPriceNullAvailableNull = new Item();
        itemPriceNullAvailableNull.setId(1);
        Item itemPrice100AvailableNull = prepareItemForUnitPriceSorting(2, 100d, null);
        Item itemPrice100IsAvailable = prepareItemForUnitPriceSorting(3, 100d, true);
        Item itemPrice100NotAvailable = prepareItemForUnitPriceSorting(4, 100d, false);
        Item itemPrice200AvailableNull = prepareItemForUnitPriceSorting(5, 200d, null);
        Item itemPrice200IsAvailable = prepareItemForUnitPriceSorting(6, 200d, true);
        Item itemPrice200NotAvailable = prepareItemForUnitPriceSorting(7, 200d, false);
        List<Item> items = new ArrayList<>();

        items.add(itemPriceNullAvailableNull);
        items.add(itemPrice100AvailableNull);
        items.add(itemPrice100NotAvailable);
        items.add(itemPrice100IsAvailable);
        items.add(itemPrice200AvailableNull);
        items.add(itemPrice200NotAvailable);
        items.add(itemPrice200IsAvailable);

        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(items);
        customerList.setWishlist(Collections.singletonList(wishList));

        String sortOrder = SortOrder.ASC.getValue();
        String sortBy = SortByField.RETAIL_PRICE.getField();

        listSortingExecutor.sort(customerList, sortBy, sortOrder);
        List<Item> actualItems = customerList.getWishlist().get(0).getItems();
        Assert.assertEquals(itemPrice100IsAvailable.getId(), actualItems.get(0).getId());
        Assert.assertEquals(itemPrice200IsAvailable.getId(), actualItems.get(1).getId());
        Assert.assertEquals(itemPrice100NotAvailable.getId(), actualItems.get(2).getId());
        Assert.assertEquals(itemPrice200NotAvailable.getId(), actualItems.get(3).getId());
        Assert.assertEquals(itemPriceNullAvailableNull.getId(), actualItems.get(4).getId());
        Assert.assertEquals(itemPrice100AvailableNull.getId(), actualItems.get(5).getId());
        Assert.assertEquals(itemPrice200AvailableNull.getId(), actualItems.get(6).getId());

    }

    @Test
    public void testGetItemsSortedByUnitPriceDesc() {
        Item itemPriceNullAvailableNull = new Item();
        itemPriceNullAvailableNull.setId(1);
        Item itemPrice100AvailableNull = prepareItemForUnitPriceSorting(2, 100d, null);
        Item itemPrice100IsAvailable = prepareItemForUnitPriceSorting(3, 100d, true);
        Item itemPrice100NotAvailable = prepareItemForUnitPriceSorting(4, 100d, false);
        Item itemPrice200AvailableNull = prepareItemForUnitPriceSorting(5, 200d, null);
        Item itemPrice200IsAvailable = prepareItemForUnitPriceSorting(6, 200d, true);
        Item itemPrice200NotAvailable = prepareItemForUnitPriceSorting(7, 200d, false);
        List<Item> items = new ArrayList<>();

        items.add(itemPriceNullAvailableNull);
        items.add(itemPrice100AvailableNull);
        items.add(itemPrice100NotAvailable);
        items.add(itemPrice100IsAvailable);
        items.add(itemPrice200AvailableNull);
        items.add(itemPrice200NotAvailable);
        items.add(itemPrice200IsAvailable);

        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(items);
        customerList.setWishlist(Collections.singletonList(wishList));

        String sortOrder = SortOrder.DESC.getValue();
        String sortBy = SortByField.RETAIL_PRICE.getField();

        listSortingExecutor.sort(customerList, sortBy, sortOrder);
        List<Item> actualItems = customerList.getWishlist().get(0).getItems();
        Assert.assertEquals(itemPrice200IsAvailable.getId(), actualItems.get(0).getId());
        Assert.assertEquals(itemPrice100IsAvailable.getId(), actualItems.get(1).getId());
        Assert.assertEquals(itemPrice200NotAvailable.getId(), actualItems.get(2).getId());
        Assert.assertEquals(itemPrice100NotAvailable.getId(), actualItems.get(3).getId());
        Assert.assertEquals(itemPrice200AvailableNull.getId(), actualItems.get(4).getId());
        Assert.assertEquals(itemPrice100AvailableNull.getId(), actualItems.get(5).getId());
        Assert.assertEquals(itemPriceNullAvailableNull.getId(), actualItems.get(6).getId());

    }

    @Test
    public void testGetListItemsSortedByUnitPriceAsc() {
        Item itemPriceNullAvailableNull = new Item();
        itemPriceNullAvailableNull.setId(1);
        Item itemPrice100AvailableNull = prepareItemForUnitPriceSorting(2, 100d, null);
        Item itemPrice100IsAvailable = prepareItemForUnitPriceSorting(3, 100d, true);
        Item itemPrice100NotAvailable = prepareItemForUnitPriceSorting(4, 100d, false);
        Item itemPrice200AvailableNull = prepareItemForUnitPriceSorting(5, 200d, null);
        Item itemPrice200IsAvailable = prepareItemForUnitPriceSorting(6, 200d, true);
        Item itemPrice200NotAvailable = prepareItemForUnitPriceSorting(7, 200d, false);
        List<Item> items = Arrays.asList(itemPriceNullAvailableNull,
                                        itemPrice100AvailableNull,
                                        itemPrice100NotAvailable,
                                        itemPrice100IsAvailable,
                                        itemPrice200AvailableNull,
                                        itemPrice200NotAvailable,
                                        itemPrice200IsAvailable);

        listSortingExecutor.sort(items, SortByField.RETAIL_PRICE, SortOrder.ASC);

        Assert.assertEquals(itemPrice100IsAvailable.getId(), items.get(0).getId());
        Assert.assertEquals(itemPrice200IsAvailable.getId(), items.get(1).getId());
        Assert.assertEquals(itemPrice100NotAvailable.getId(), items.get(2).getId());
        Assert.assertEquals(itemPrice200NotAvailable.getId(), items.get(3).getId());
        Assert.assertEquals(itemPriceNullAvailableNull.getId(), items.get(4).getId());
        Assert.assertEquals(itemPrice100AvailableNull.getId(), items.get(5).getId());
        Assert.assertEquals(itemPrice200AvailableNull.getId(), items.get(6).getId());

    }

    @Test
    public void testGetListItemsSortedByUnitPriceDesc() {
        Item itemPriceNullAvailableNull = new Item();
        itemPriceNullAvailableNull.setId(1);
        Item itemPrice100AvailableNull = prepareItemForUnitPriceSorting(2, 100d, null);
        Item itemPrice100IsAvailable = prepareItemForUnitPriceSorting(3, 100d, true);
        Item itemPrice100NotAvailable = prepareItemForUnitPriceSorting(4, 100d, false);
        Item itemPrice200AvailableNull = prepareItemForUnitPriceSorting(5, 200d, null);
        Item itemPrice200IsAvailable = prepareItemForUnitPriceSorting(6, 200d, true);
        Item itemPrice200NotAvailable = prepareItemForUnitPriceSorting(7, 200d, false);
        List<Item> items = Arrays.asList(itemPriceNullAvailableNull,
                                        itemPrice100AvailableNull,
                                        itemPrice100NotAvailable,
                                        itemPrice100IsAvailable,
                                        itemPrice200AvailableNull,
                                        itemPrice200NotAvailable,
                                        itemPrice200IsAvailable);

        listSortingExecutor.sort(items, SortByField.RETAIL_PRICE, SortOrder.DESC);
        Assert.assertEquals(itemPrice200IsAvailable.getId(), items.get(0).getId());
        Assert.assertEquals(itemPrice100IsAvailable.getId(), items.get(1).getId());
        Assert.assertEquals(itemPrice200NotAvailable.getId(), items.get(2).getId());
        Assert.assertEquals(itemPrice100NotAvailable.getId(), items.get(3).getId());
        Assert.assertEquals(itemPrice200AvailableNull.getId(), items.get(4).getId());
        Assert.assertEquals(itemPrice100AvailableNull.getId(), items.get(5).getId());
        Assert.assertEquals(itemPriceNullAvailableNull.getId(), items.get(6).getId());

    }

    private Item prepareItemForUnitPriceSorting(Integer itemId, Double retailPrice, Boolean available) {
        Item item = new Item();
        Product product = new Product();
        Price price = new Price();
        price.setRetailPrice(retailPrice);
        product.setPrice(price);
        product.setAvailable(available);
        item.setId(itemId);
        item.setProduct(product);
        return item;
    }

    @Test
    public void testGetItemsSortedByUnitPriceInUpcLevelAsc() {
        Item itemPriceNullAvailableNull = new Item();
        itemPriceNullAvailableNull.setId(1);
        Item itemPrice100AvailableNull = prepareUpcLevelItemForUnitPriceSorting(2, 100d, null);
        Item itemPrice100IsAvailable = prepareUpcLevelItemForUnitPriceSorting(3, 100d, true);
        Item itemPrice100NotAvailable = prepareUpcLevelItemForUnitPriceSorting(4, 100d, false);
        Item itemPrice200AvailableNull = prepareUpcLevelItemForUnitPriceSorting(5, 200d, null);
        Item itemPrice200IsAvailable = prepareUpcLevelItemForUnitPriceSorting(6, 200d, true);
        Item itemPrice200NotAvailable = prepareUpcLevelItemForUnitPriceSorting(7, 200d, false);
        List<Item> items = new ArrayList<>();

        items.add(itemPriceNullAvailableNull);
        items.add(itemPrice100AvailableNull);
        items.add(itemPrice100NotAvailable);
        items.add(itemPrice100IsAvailable);
        items.add(itemPrice200AvailableNull);
        items.add(itemPrice200NotAvailable);
        items.add(itemPrice200IsAvailable);

        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(items);
        customerList.setWishlist(Collections.singletonList(wishList));

        String sortOrder = SortOrder.ASC.getValue();
        String sortBy = SortByField.RETAIL_PRICE.getField();

        listSortingExecutor.sort(customerList, sortBy, sortOrder);
        List<Item> actualItems = customerList.getWishlist().get(0).getItems();
        Assert.assertEquals(itemPrice100IsAvailable.getId(), actualItems.get(0).getId());
        Assert.assertEquals(itemPrice200IsAvailable.getId(), actualItems.get(1).getId());
        Assert.assertEquals(itemPrice100NotAvailable.getId(), actualItems.get(2).getId());
        Assert.assertEquals(itemPrice200NotAvailable.getId(), actualItems.get(3).getId());
        Assert.assertEquals(itemPriceNullAvailableNull.getId(), actualItems.get(4).getId());
        Assert.assertEquals(itemPrice100AvailableNull.getId(), actualItems.get(5).getId());
        Assert.assertEquals(itemPrice200AvailableNull.getId(), actualItems.get(6).getId());

    }

    private Item prepareUpcLevelItemForUnitPriceSorting(Integer itemId, Double retailPrice, Boolean available) {
        Item item = new Item();
        Upc upc = new Upc();
        Price price = new Price();
        price.setRetailPrice(retailPrice);
        upc.setPrice(price);
        Availability availability = new Availability();
        availability.setAvailable(available);
        upc.setAvailability(availability);
        item.setId(itemId);
        item.setUpc(upc);
        return item;
    }

    @Test
    public void testGetItemsSortedByAvgReviewRatingAsc() {
        Item itemUpcNull = new Item();
        itemUpcNull.setId(1);
        Item itemUpcProductNull = new Item();
        itemUpcProductNull.setId(2);
        Item itemRatingNullSuppressReviewNull = prepareItemForAvgReviewRatingSorting(3, null, null);
        Item itemRating1IsSuppressReview = prepareItemForAvgReviewRatingSorting(4, 1F, true);
        Item itemRating1NotSuppressReview = prepareItemForAvgReviewRatingSorting(5, 1F, false);
        Item itemRating08SuppressReviewNull = prepareItemForAvgReviewRatingSorting(6, 0.8F, null);
        Item itemRating08IsSuppressReview = prepareItemForAvgReviewRatingSorting(7, 0.8F, true);
        Item itemRating08NotSuppressReview = prepareItemForAvgReviewRatingSorting(8, 0.8F, false);

        List<Item> items = new ArrayList<>();

        items.add(itemUpcNull);
        items.add(itemUpcProductNull);
        items.add(itemRatingNullSuppressReviewNull);
        items.add(itemRating1IsSuppressReview);
        items.add(itemRating1NotSuppressReview);
        items.add(itemRating08SuppressReviewNull);
        items.add(itemRating08IsSuppressReview);
        items.add(itemRating08NotSuppressReview);

        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(items);
        customerList.setWishlist(Collections.singletonList(wishList));

        String sortOrder = SortOrder.ASC.getValue();
        String sortBy = SortByField.AVG_REVIEW_RATING.getField();

        listSortingExecutor.sort(customerList, sortBy, sortOrder);
        List<Item> actualItems = customerList.getWishlist().get(0).getItems();
        Assert.assertEquals(itemRating08IsSuppressReview.getId(), actualItems.get(0).getId());
        Assert.assertEquals(itemRating1IsSuppressReview.getId(), actualItems.get(1).getId());
        Assert.assertEquals(itemRating08NotSuppressReview.getId(), actualItems.get(2).getId());
        Assert.assertEquals(itemRating1NotSuppressReview.getId(), actualItems.get(3).getId());
        Assert.assertEquals(itemUpcNull.getId(), actualItems.get(4).getId());
        Assert.assertEquals(itemUpcProductNull.getId(), actualItems.get(5).getId());
        Assert.assertEquals(itemRatingNullSuppressReviewNull.getId(), actualItems.get(6).getId());
        Assert.assertEquals(itemRating08SuppressReviewNull.getId(), actualItems.get(7).getId());
    }

    @Test
    public void testGetItemsSortedByAvgReviewRatingDesc() {
        Item itemUpcNull = new Item();
        itemUpcNull.setId(1);
        Item itemUpcProductNull = new Item();
        itemUpcProductNull.setId(2);
        Item itemRatingNullSuppressReviewNull = prepareItemForAvgReviewRatingSorting(3, null, null);
        Item itemRating1IsSuppressReview = prepareItemForAvgReviewRatingSorting(4, 1F, true);
        Item itemRating1NotSuppressReview = prepareItemForAvgReviewRatingSorting(5, 1F, false);
        Item itemRating08SuppressReviewNull = prepareItemForAvgReviewRatingSorting(6, 0.8F, null);
        Item itemRating08IsSuppressReview = prepareItemForAvgReviewRatingSorting(7, 0.8F, true);
        Item itemRating08NotSuppressReview = prepareItemForAvgReviewRatingSorting(8, 0.8F, false);

        List<Item> items = new ArrayList<>();

        items.add(itemUpcNull);
        items.add(itemUpcProductNull);
        items.add(itemRatingNullSuppressReviewNull);
        items.add(itemRating1IsSuppressReview);
        items.add(itemRating1NotSuppressReview);
        items.add(itemRating08SuppressReviewNull);
        items.add(itemRating08IsSuppressReview);
        items.add(itemRating08NotSuppressReview);

        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(items);
        customerList.setWishlist(Collections.singletonList(wishList));

        String sortOrder = SortOrder.DESC.getValue();
        String sortBy = SortByField.AVG_REVIEW_RATING.getField();

        listSortingExecutor.sort(customerList, sortBy, sortOrder);
        List<Item> actualItems = customerList.getWishlist().get(0).getItems();
        Assert.assertEquals(itemRating08SuppressReviewNull.getId(), actualItems.get(0).getId());
        Assert.assertEquals(itemUpcNull.getId(), actualItems.get(1).getId());
        Assert.assertEquals(itemUpcProductNull.getId(), actualItems.get(2).getId());
        Assert.assertEquals(itemRatingNullSuppressReviewNull.getId(), actualItems.get(3).getId());
        Assert.assertEquals(itemRating1NotSuppressReview.getId(), actualItems.get(4).getId());
        Assert.assertEquals(itemRating08NotSuppressReview.getId(), actualItems.get(5).getId());
        Assert.assertEquals(itemRating1IsSuppressReview.getId(), actualItems.get(6).getId());
        Assert.assertEquals(itemRating08IsSuppressReview.getId(), actualItems.get(7).getId());
    }

    private Item prepareItemForAvgReviewRatingSorting(Integer itemId, Float averageRating, Boolean suppressReview) {
        Item item = new Item();
        item.setId(itemId);
        Upc upc = new Upc();
        Product product = new Product();
        ReviewStatistics reviewStatistics = new ReviewStatistics();
        reviewStatistics.setAverageRating(averageRating);
        product.setSuppressReviews(suppressReview);
        product.setReviewStatistics(reviewStatistics);
        upc.setProduct(product);
        item.setUpc(upc);
        return item;
    }

    @Test
    public void testGetSortedItemsByTopPickAsc() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        Item itemPriorityNull = prepareItemForTopPickSorting(1, null, null);
        Item itemPriorityHAddedLater = prepareItemForTopPickSorting(2, "H", today);
        Item itemPriorityHAddedEarlier = prepareItemForTopPickSorting(3, "H", yesterday);
        List<Item> items = new ArrayList<>();
        items.add(itemPriorityHAddedLater);
        items.add(itemPriorityHAddedEarlier);
        items.add(itemPriorityNull);

        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(items);
        customerList.setWishlist(Collections.singletonList(wishList));

        String sortOrder = SortOrder.ASC.getValue();
        String sortBy = SortByField.TOP_PICK.getField();

        listSortingExecutor.sort(customerList, sortBy, sortOrder);
        List<Item> actualItems = customerList.getWishlist().get(0).getItems();
        Assert.assertEquals(itemPriorityNull.getId(), actualItems.get(0).getId());
        Assert.assertEquals(itemPriorityHAddedEarlier.getId(), actualItems.get(1).getId());
        Assert.assertEquals(itemPriorityHAddedLater.getId(), actualItems.get(2).getId());
    }

    @Test
    public void testGetSortedItemsByTopPickDesc() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        Item itemPriorityNull = prepareItemForTopPickSorting(1, null, null);
        Item itemPriorityHAddedLater = prepareItemForTopPickSorting(2, "H", today);
        Item itemPriorityHAddedEarlier = prepareItemForTopPickSorting(3, "H", yesterday);
        List<Item> items = new ArrayList<>();
        items.add(itemPriorityNull);
        items.add(itemPriorityHAddedEarlier);
        items.add(itemPriorityHAddedLater);

        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(items);
        customerList.setWishlist(Collections.singletonList(wishList));

        String sortOrder = SortOrder.DESC.getValue();
        String sortBy = SortByField.TOP_PICK.getField();

        listSortingExecutor.sort(customerList, sortBy, sortOrder);
        List<Item> actualItems = customerList.getWishlist().get(0).getItems();
        Assert.assertEquals(itemPriorityHAddedLater.getId(), actualItems.get(0).getId());
        Assert.assertEquals(itemPriorityHAddedEarlier.getId(), actualItems.get(1).getId());
        Assert.assertEquals(itemPriorityNull.getId(), actualItems.get(2).getId());
    }

    private Item prepareItemForTopPickSorting(Integer itemId, String priority, Date addedDate) {
        Item item = new Item();
        item.setPriority(priority);
        item.setId(itemId);
        item.setAddedDate(addedDate);
        return item;
    }

    @Test
    public void testGetSortedItemsByAddedDateAsc() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        Item itemAddedToday = prepareItemForSortingByAddedDate(1, today);
        Item itemAddedYesterday = prepareItemForSortingByAddedDate(2, yesterday);
        Item itemUnknownWhenAdded = prepareItemForSortingByAddedDate(3, null);

        List<Item> items = new ArrayList<>();
        items.add(itemAddedToday);
        items.add(itemAddedYesterday);
        items.add(itemUnknownWhenAdded);

        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(items);
        customerList.setWishlist(Collections.singletonList(wishList));

        String sortOrder = SortOrder.ASC.getValue();
        String sortBy = SortByField.ADDED_DATE.getField();

        listSortingExecutor.sort(customerList, sortBy, sortOrder);
        List<Item> actualItems = customerList.getWishlist().get(0).getItems();
        Assert.assertEquals(itemUnknownWhenAdded.getId(), actualItems.get(0).getId());
        Assert.assertEquals(itemAddedYesterday.getId(), actualItems.get(1).getId());
        Assert.assertEquals(itemAddedToday.getId(), actualItems.get(2).getId());
    }

    @Test
    public void testGetSortedItemsByAddedDateDesc() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        Item itemAddedToday = prepareItemForSortingByAddedDate(1, today);
        Item itemAddedYesterday = prepareItemForSortingByAddedDate(2, yesterday);
        Item itemUnknownWhenAdded = prepareItemForSortingByAddedDate(3, null);

        List<Item> items = new ArrayList<>();
        items.add(itemUnknownWhenAdded);
        items.add(itemAddedToday);
        items.add(itemAddedYesterday);

        CustomerList customerList = new CustomerList();
        WishList wishList = new WishList();
        wishList.setItems(items);
        customerList.setWishlist(Collections.singletonList(wishList));

        String sortOrder = SortOrder.DESC.getValue();
        String sortBy = SortByField.ADDED_DATE.getField();

        listSortingExecutor.sort(customerList, sortBy, sortOrder);
        List<Item> actualItems = customerList.getWishlist().get(0).getItems();
        Assert.assertEquals(itemAddedToday.getId(), actualItems.get(0).getId());
        Assert.assertEquals(itemAddedYesterday.getId(), actualItems.get(1).getId());
        Assert.assertEquals(itemUnknownWhenAdded.getId(), actualItems.get(2).getId());
    }

    private Item prepareItemForSortingByAddedDate(Integer itemId, Date addedDate) {
        Item item = new Item();
        item.setId(itemId);
        item.setAddedDate(addedDate);
        return item;
    }
}
