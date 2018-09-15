package com.macys.selection.xapi.list.client.request.converter;

import com.macys.selection.xapi.list.client.request.CustomerListMerge;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.WishList;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CustomerMSPRequestConverterTest extends AbstractTestNGSpringContextTests {
	
	private static final long TEST_LONG = 1234L;
	private static final int TEST_INT = 813310;
	private static final int TEST_UPC_INT = 31277389;
	private static final long TEST_UPC_NUMBER = 20714656027L;

	  @Test
	  public void convertTest() throws IOException {
		  CustomerList customerList = getCustomerList();
		  String testJsonString = CustomerMSPRequestConverter.convert(customerList);
		  String expectedJsonString = TestUtils.readFile("com/macys/selection/xapi/list/client/request/converter/request_customerMSP.json");
		  assertTrue(testJsonString != null);
		  assertTrue(testJsonString.equals(expectedJsonString));
	  }
	  
	  private CustomerList getCustomerList() {
		  CustomerList customerList = new CustomerList();
		  User user = new User();
		  user.setId(new Long(TEST_LONG));
		  user.setGuid("dfsdfadsfasdsfs123423432");
		  customerList.setUser(user);
		  
		  WishList wishList = new WishList();
		  wishList.setListGuid("testwishlistguid");
		  wishList.setName("testing wishlist name");
		  wishList.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
		  wishList.setDefaultList(Boolean.TRUE);
		  wishList.setOnSaleNotify(Boolean.FALSE);
		  wishList.setSearchable(Boolean.FALSE);
		  wishList.setShowPurchaseInfo(Boolean.FALSE);
		  
		  Product product = new Product();
		  product.setId(TEST_INT);

		  Upc upc = new Upc();
		  upc.setId(TEST_UPC_INT);
		  upc.setUpcNumber(TEST_UPC_NUMBER);

		  Item item = new Item(); 
		  item.setQtyRequested(1);
		  item.setProduct(product);
		  item.setUpc(upc);
		  
		  List<Item> items = new ArrayList<Item>();
		  items.add(item);

		  wishList.setItems(items);
		  List<WishList> wishLists = new ArrayList<WishList>();
		  wishLists.add(wishList);
		  customerList.setWishlist(wishLists);

		  return customerList;
	  }
	  
	  @Test
	  public void convertOnNonHappyPathTest() throws IOException {
		  //case customerList is null
		  CustomerList customerList = null;
		  String testJsonString = CustomerMSPRequestConverter.convert(customerList);
		  String expectedJsonString = "{\"list\":{}}";
		  assertTrue(testJsonString != null);
		  assertTrue(testJsonString.equals(expectedJsonString));
		  
		  //case customerList has null WishList
		  CustomerList customerList2 = new CustomerList();
		  String testJsonString2 = CustomerMSPRequestConverter.convert(customerList2);
		  assertTrue(testJsonString2 != null);
		  assertTrue(testJsonString2.equals(expectedJsonString));
		  
		  //case customerList has an empty wishlists
		  //case customerList has null WishList
		  CustomerList customerList4 = new CustomerList();
		  List<WishList> emptyWishLists = new ArrayList<WishList>();
		  customerList4.setWishlist(emptyWishLists);
		  String testJsonString4 = CustomerMSPRequestConverter.convert(customerList4);
		  assertTrue(testJsonString4 != null);
		  assertTrue(testJsonString4.equals(expectedJsonString));
		  
		  //case customerList has a list of WishList, but null object inside the list
		  CustomerList customerList3 = new CustomerList();
		  List<WishList> wishLists = new ArrayList<WishList>();
		  wishLists.add(null);
		  customerList3.setWishlist(wishLists);
		  String testJsonString3 = CustomerMSPRequestConverter.convert(customerList3);
		  assertTrue(testJsonString3 != null);
		  assertTrue(testJsonString3.equals(expectedJsonString));
	  }
	  
	  @Test
	  public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	    Constructor<CustomerMSPRequestConverter> constructor = CustomerMSPRequestConverter.class.getDeclaredConstructor();
	    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	    constructor.setAccessible(true);
	    constructor.newInstance();
	  }
	  
	  @Test
	  public void convertItemListTest() throws IOException {
		  CustomerList customerList = getCustomerList();
		  List<Item> itemList = customerList.getWishlist().get(0).getItems();
		  String testJsonString = CustomerMSPRequestConverter.convert(itemList);
		  String expectedJsonString = "[{\"qtyRequested\":1,\"upc\":{\"id\":31277389,\"upcNumber\":20714656027},\"product\":{\"id\":813310}}]";
		  assertTrue(testJsonString != null);
		  assertTrue(testJsonString.equals(expectedJsonString));
	  }
	  
	  @Test
	  public void convertItemListUnhappyCasesTest() throws IOException {
		  List<Item> itemList = null;
		  String testJsonString = CustomerMSPRequestConverter.convert(itemList);
		  assertTrue(testJsonString == null);
		  
		  List<Item> itemList2 = new ArrayList<Item>();
		  String testJsonString2 = CustomerMSPRequestConverter.convert(itemList2);
		  assertTrue(testJsonString2 == null);
	  }
	  
	  @Test
	  public void convertEmailShareTest() throws IOException {
		  EmailShare emailShare = EmailShareTest.expectedEmailShare();
		  String testJsonString = CustomerMSPRequestConverter.convertEmailShare(emailShare);
		  String expectedJsonString = "{\"EmailShare\":{\"from\":\"test@test.com\",\"to\":\"qa15@test.com,qa16@test.com\",\"message\":\"Test Message\",\"link\":\"http://www.qa16codemacys.fds.com/wishlist/guest?wid=11_yRjKJPusoRy74ha4nYulEgIA5WB6juHWQ1iWI5aySPI=&cm_mmc=wishlist-_-share-_-sil-_-n\",\"firstName\":\"first name\",\"lastName\":\"last name\"}}";
		  assertTrue(testJsonString != null);
		  assertTrue(testJsonString.equals(expectedJsonString));
	  }

	  @Test
	  public void convertListMergeTest() throws IOException {
		  CustomerListMerge listMerge = new CustomerListMerge();
		  listMerge.setGuestUserId(123L);
		  listMerge.setUserId(456L);

		  String testJsonString = CustomerMSPRequestConverter.convertListMerge(listMerge);
		  String expectedJsonString = "{\"wishListRequestDTO\":{\"guestUserId\":123,\"userId\":456}}";
		  assertNotNull(testJsonString);
		  assertEquals(testJsonString, expectedJsonString);
	  }
	  
}
