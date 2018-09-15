package com.macys.selection.xapi.list.client.request.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.macys.selection.xapi.list.client.request.CustomerListRequest;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.User;

@SpringBootTest
@JsonTest
public class CustomerListRequestTest extends AbstractTestNGSpringContextTests {
	  @Autowired private JacksonTester <CustomerListRequest> json;
	  
	  private CustomerListRequest listRequest;
	  private ObjectMapper objectMapper;
	  
	  private static String REQUEST_TO_CUSTOMERMSP = "com/macys/selection/xapi/list/client/request/converter/request_customerMSP.json";
	  private static String TESTING_USER_GUID = "dfsdfadsfasdsfs123423432";
	  private static final Long TESTING_USER_ID = new Long(1234);
	  private static String TEST_LIST_GUID = "testwishlistguid";
	  private static String TEST_LIST_NAME = "testing wishlist name";
	  private static final int TEST_PRODUCT_ID = 813310;
	  private static final Long TEST_UPC_NUMBER = 20714656027L;
	  private static final int TEST_UPC_ID = 31277389;
	  private static final int TEST_QUANTITY = 1;
	  
	  @BeforeMethod
	  public void setup() throws ParseException {
		  listRequest = expectedListRequest(); 
		  objectMapper = new ObjectMapper();
		  objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
	  }
	  
	  @Test
	  public void listRequestDeserializeTest() throws ParseException, IOException {
	    String listRequestJson = TestUtils.readFile(REQUEST_TO_CUSTOMERMSP);
	    String testingJson = objectMapper.writeValueAsString(listRequest);
	    assertTrue(testingJson.equals(listRequestJson));
	  }
	  
	  @Test
	  public void listRequestSerializeTest() throws ParseException, IOException {
		objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
	    String listRequestJson = TestUtils.readFile(REQUEST_TO_CUSTOMERMSP);
	    CustomerListRequest testingListRequest = objectMapper.readValue(listRequestJson, CustomerListRequest.class);
	    assertTrue(testingListRequest != null);
	    assertTrue(testingListRequest.isDefaultList());
	    assertTrue(!testingListRequest.isOnSaleNotify());
	    assertTrue(!testingListRequest.isSearchable());
	    assertTrue(!testingListRequest.isShowPurchaseInfo());
	  }
	  
	  @Test
	  public void listRequestEquaslsTest() {
	    assertThat(listRequest.equals(null)).isFalse();
	    assertThat(listRequest.equals(listRequest)).isTrue();    
	  }
	  
	  @Test
	  public void listRequestEquasls2Test() {
	    assertThat(listRequest.equals(new User())).isFalse();
	    assertThat(listRequest.equals(anOtherListRequest())).isFalse();    
	  }
	  
	  @Test
	  public void listRequestHashCodeTest() throws IOException {
	    String listRequestJson = TestUtils.readFile(REQUEST_TO_CUSTOMERMSP);
	    CustomerListRequest testListRequest = this.json.parseObject(listRequestJson);
	    assertThat(testListRequest.hashCode()).isNotNull();    
	  }

	  @Test
	  public void listRequestToStringTest() throws IOException {
	    String listRequestJson = TestUtils.readFile(REQUEST_TO_CUSTOMERMSP);
	    CustomerListRequest testListRequest = this.json.parseObject(listRequestJson);
	    assertThat(testListRequest.toString()).isNotNull();    
	  } 
	  
	  private CustomerListRequest expectedListRequest() {
		    CustomerListRequest list = new CustomerListRequest();
		    User user = new User();
		    user.setGuid(TESTING_USER_GUID);
		    user.setId(TESTING_USER_ID);
		    list.setUser(user);
		    
		    list.setGuid(TEST_LIST_GUID);
		    list.setName(TEST_LIST_NAME);
		    list.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
		    list.setDefaultList(Boolean.TRUE);
		    list.setOnSaleNotify(Boolean.FALSE);
		    list.setSearchable(Boolean.FALSE);
		    list.setShowPurchaseInfo(Boolean.FALSE);
		    
		    Product product = new Product();
		    product.setId(TEST_PRODUCT_ID);

		    Upc upc = new Upc();
		    upc.setId(TEST_UPC_ID);
		    upc.setUpcNumber(TEST_UPC_NUMBER);
		    
		    Item item = new Item();
		    item.setProduct(product);
		    item.setUpc(upc);
		    item.setQtyRequested(TEST_QUANTITY);
		    List<Item> items = new ArrayList<>();
		    items.add(item);
		    
		    list.setItems(items);    
		    return list;
	   }
	  
	  private CustomerListRequest anOtherListRequest() {
		    CustomerListRequest list = new CustomerListRequest();
		    User user = new User();
		    user.setGuid(TESTING_USER_GUID);
		    user.setId(TESTING_USER_ID);
		    list.setUser(user);
		    
		    list.setGuid(TEST_LIST_GUID);
		    list.setName(TEST_LIST_NAME);
		    list.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
		    list.setDefaultList(Boolean.FALSE);
		    list.setOnSaleNotify(Boolean.TRUE);
		    list.setSearchable(Boolean.TRUE);
		    list.setShowPurchaseInfo(Boolean.TRUE);
		    
		    Product product = new Product();
		    product.setId(TEST_PRODUCT_ID);

		    Upc upc = new Upc();
		    upc.setId(TEST_UPC_ID);
		    upc.setUpcNumber(TEST_UPC_NUMBER);
		    
		    Item item = new Item();
		    item.setProduct(product);
		    item.setUpc(upc);
		    item.setQtyRequested(TEST_QUANTITY);
		    List<Item> items = new ArrayList<>();
		    items.add(item);
		    
		    list.setItems(items);    
		    return list;
	   }

}
