package com.macys.selection.xapi.list.rest.request.validator;

import com.macys.selection.xapi.list.client.CustomerListRestClientTest;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@SpringBootTest(classes = { ListItemExitsValidator.class})
public class ListItemExitsValidatorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ListItemExitsValidator listItemExitsValidator;

    @Test
    public void testIsWishListIsSetToNullInCustomerList() {
        CustomerList customerList = new CustomerList();
        customerList.setWishlist(null);
        Boolean result = listItemExitsValidator.isValid(customerList);
        assertFalse(result);
    }

    @Test
    public void testIsWishListEmptyInCustomerList() {
        CustomerList customerList = new CustomerList();
        List<WishList> wishlists = new ArrayList<>();
        customerList.setWishlist(wishlists);
        Boolean result = listItemExitsValidator.isValid(customerList);
        assertFalse(result);
    }

    @Test
    public void testItemsIsSetToEmptyInCustomerList() {
        assertFalse(listItemExitsValidator.isValid(new CustomerList()));
    }

    @Test
    public void testItemsIsSetToNullInCustomerList() {
        assertFalse(listItemExitsValidator.isValid(null));
    }

    @Test
    public void testItemsExitsInCustomerList() {
        CustomerList customerList = getCustomerList();

        Boolean isValid = listItemExitsValidator.isValid(customerList);
        assertTrue(isValid);
    }

    @Test
    public void testItemsNullInCustomerList() {
        CustomerList customerList = getCustomerList();
        WishList wishlist = new WishList();
        wishlist.setItems(null);
        ArrayList<WishList> wishlists = new ArrayList<>();
        wishlists.add(wishlist);
        customerList.setWishlist(wishlists);

        Boolean isValid = listItemExitsValidator.isValid(customerList);
        assertFalse(isValid);
    }
    
    private CustomerList getCustomerList() {
        CustomerList customerList = new CustomerList();
        List<Item> itemList = CustomerListRestClientTest.getTestItemList();
        WishList wishList = new WishList();
        wishList.setItems(itemList);
        List<WishList> wishLists = new ArrayList<WishList>();
        wishLists.add(wishList);
        customerList.setWishlist(wishLists);
        return customerList;
    }


}
