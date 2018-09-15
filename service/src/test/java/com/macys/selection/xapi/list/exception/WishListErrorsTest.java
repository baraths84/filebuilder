package com.macys.selection.xapi.list.exception;

import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by Narasim Bayanaboina on 1/18/18.
 */

@SpringBootTest
public class WishListErrorsTest extends AbstractTestNGSpringContextTests {

    private static final String WISH_LISTS_JSON_FILE = "com/macys/selection/xapi/list/client/response/wishlist_error_customer.json";
    private static final int SIZE_ONE = 1;
    private static final int SIZE_TWO = 2;

    @BeforeMethod
    public void setup() throws ParseException {

    }

    @Test
    public void wishlistsErrorsDeserializeTest() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_JSON_FILE);
        JsonToObjectConverter<WishListErrors> wishlistsConverter = new JsonToObjectConverter<>(WishListErrors.class);
        WishListErrors response = wishlistsConverter.parseJsonToObject(wishlistsResponseJson);
        assertNotNull(response);
        assertTrue(response.getError() != null && response.getError().size() == SIZE_ONE);

        WishListError wishlist1 = new WishListError();
        WishListError wishlist2 = new WishListError();
        List<WishListError> newWishLists = new ArrayList<WishListError>();
        newWishLists.add(wishlist1);
        newWishLists.add(wishlist2);
        response.setError(newWishLists);
        assertTrue(response.getError() != null && response.getError().size() == SIZE_TWO);
    }

}

