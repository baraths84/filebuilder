package com.macys.selection.xapi.list.data.converters;

import com.macys.selection.xapi.list.client.response.CustomerWishListsResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@SpringBootTest
public class JsonToObjectConverterTest extends AbstractTestNGSpringContextTests {

	private static final String WISH_LISTS_JSON_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlists_response.json";
	private static final int SIZE_ONE = 1;
	private static final String CLASS_TYPE_NAME = "com.macys.selection.xapi.list.client.response.CustomerWishListsResponse";

	@Test
	public void parseJsonToObjectTest() throws IOException {
		String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_JSON_FILE);
		JsonToObjectConverter<CustomerWishListsResponse> wishlistsConverter = new JsonToObjectConverter<>(CustomerWishListsResponse.class);
		CustomerWishListsResponse response = wishlistsConverter.parseJsonToObject(wishlistsResponseJson);
		assertNotNull(response);
		assertTrue(response.getLists() != null && response.getLists().size() == SIZE_ONE);
	}


	@Test
	public void getTypeTest() {
		JsonToObjectConverter<CustomerWishListsResponse> wishlistsConverter = new JsonToObjectConverter<>(CustomerWishListsResponse.class);
		assertTrue(wishlistsConverter.getType().getTypeName().equals(CLASS_TYPE_NAME));
	}
}
