package com.macys.selection.xapi.list.data.converters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.response.User;

@SpringBootTest
public class JsonToUserMapperTest extends AbstractTestNGSpringContextTests {

	private static final String WISH_LISTS_JSON_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlists_response_multiplelists.json";
	
	private static final String WISH_LIST_JSON_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_response_withuser.json";
	
	private static final String EMPTY_USER_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_response_withemptyuser.json";
	
	private static final String NULL_USER_FIELDS_NODES_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlist_response_withuser_nullfields.json";
	
	private static final String EMPTY_WISHLISTS_FILE = "com/macys/selection/xapi/list/client/response/customermsp_wishlists_response_without_lists.json";
	
	private static final Long USER_ID = 2158516416L;
	private static final String USER_GUID = "de15eeab-1502-47b5-bf3e-b998d932a967";
	private static final String FIRST = "first";

	@Test
	public void parseTest() throws IOException {
		//TEST wishlists
		String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_JSON_FILE);
		User user = JsonToUserMapper.parse(wishlistsResponseJson, false);
		assertNotNull(user);
		assertTrue(user.getGuid().equals(USER_GUID));
		assertTrue(user.getId().equals(USER_ID));
		assertTrue(user.getProfile() != null && user.getProfile().getFirstName().equals(FIRST));
		assertTrue(!user.isGuestUser());
		
		//TEST wishlist only
		String wishlistsResponseJson2 = TestUtils.readFile(WISH_LIST_JSON_FILE);
		User user2 = JsonToUserMapper.parse(wishlistsResponseJson2, true);
		assertNotNull(user2);
		assertTrue(user2.getGuid().equals(USER_GUID));
		assertTrue(user2.getId().equals(USER_ID));
		assertTrue(user2.getProfile() != null && user2.getProfile().getFirstName().equals(FIRST));
		assertTrue(!user2.isGuestUser());
		
		//TEST wishlist only and all the user fields are null
		String wishlistsResponseJson3 = TestUtils.readFile(EMPTY_USER_FILE);
		User user3 = JsonToUserMapper.parse(wishlistsResponseJson3, true);
		assertNotNull(user3);
		assertNull(user3.getGuid());
		
		//TEST wishlist only and all the user fields nodes are available, but all values are null
		String wishlistsResponseJson4 = TestUtils.readFile(NULL_USER_FIELDS_NODES_FILE);
		User user4 = JsonToUserMapper.parse(wishlistsResponseJson4, true);
		assertNotNull(user4);
		assertNull(user4.getGuid());
		
		//TEST wishlists with empty wishlists
		String wishlistsResponseJson5 = TestUtils.readFile(EMPTY_WISHLISTS_FILE);
		User user5 = JsonToUserMapper.parse(wishlistsResponseJson5, false);
		assertNull(user5);
		
		//TEST wishlists in which the second lists node is empty
		String wishlistsResponseJson6 = "{\"lists\": {}}";
		User user6 = JsonToUserMapper.parse(wishlistsResponseJson6, false);
		assertNull(user6);

	}
	
	@Test(expectedExceptions = ListServiceException.class)
	public void parseOnIOExceptionTest() throws IOException {
		//TEST IOException
		String wishlistsResponseJson = "bad data";
		JsonToUserMapper.parse(wishlistsResponseJson, false);

	}
	
	//test private constructor
	 @Test(expectedExceptions = InvocationTargetException.class)
	  void testValidatesClassIsNotInstantiable() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		 Constructor<JsonToUserMapper> c = JsonToUserMapper.class.getDeclaredConstructor();
		 c.setAccessible(true);
		 c.newInstance();
	  }
}
