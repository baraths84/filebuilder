package com.macys.selection.xapi.list.client.response;

import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.rest.response.WishList;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class WishListsDTOTest extends AbstractTestNGSpringContextTests {
	
	private static final String WISH_LISTS_JSON_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlists_response.json";
	private static final int SIZE_ONE = 1;
	private static final int SIZE_TWO = 2;

	@BeforeMethod
	public void setup() throws ParseException {

	}

	@Test
	public void wishlistsReponseDeserializeTest() throws IOException {
		String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_JSON_FILE);
		JsonToObjectConverter<CustomerWishListsResponse> wishlistsConverter = new JsonToObjectConverter<>(CustomerWishListsResponse.class);
		CustomerWishListsResponse response = wishlistsConverter.parseJsonToObject(wishlistsResponseJson);
		assertNotNull(response);
		assertTrue(response.getLists() != null && response.getLists().size() == SIZE_ONE);
		
		WishList wishlist1 = new WishList();
		WishList wishlist2 = new WishList();
		List<WishList> newWishLists = new ArrayList<WishList>();
		newWishLists.add(wishlist1);
		newWishLists.add(wishlist2);
		response.setLists(newWishLists);
		assertTrue(response.getLists() != null && response.getLists().size() == SIZE_TWO);
	}
	 
}
