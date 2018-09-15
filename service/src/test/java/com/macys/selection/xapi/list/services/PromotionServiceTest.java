package com.macys.selection.xapi.list.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.macys.selection.xapi.list.client.PromotionsRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserPromotions;
import com.macys.selection.xapi.list.data.converters.JsonToPromotionConverter;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.response.Availability;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Price;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.Promotion;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.WishList;

@SpringBootTest
(classes = {JsonResponseParserPromotions.class,KillSwitchPropertiesBean.class,
		JsonToPromotionConverter.class})
public class PromotionServiceTest extends AbstractTestNGSpringContextTests  {
	
	private static final String PROMOTIONS_FILE_PATH= "com/macys/selection/xapi/wishlist/converters/promotions.json";
	private static final String TEST_STRING ="anything";
	private static final double TEST_PRICE = 99.0;
	private static final int TEST_INT = 37609138;
	private static final long TEST_LONG = 10680021L;
	private static final long PROM_ID_1 = 19876644L;
	private static final long PROM_ID_2 = 19873015L;

	@Mock
	private JsonResponseParserPromotions promotions;

	@Mock
	private JsonToPromotionConverter promotionsConverter;

	@Mock
	private PromotionsRestClient promotionsRestClient;

	@InjectMocks
	private PromotionService promotionService;


	@Mock
	private KillSwitchPropertiesBean killswitchProperties;

	@BeforeMethod
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetPromotionsErrorScenerios() {
			//Case 1
			WishList wishlist = promotionService.getPromotions(null, null);
			assertNull(wishlist);

			//case 2
			WishList wishlistWithoutItems = promotionService.getPromotions(new WishList(), new ListQueryParam());
			assertNotNull(wishlistWithoutItems);
			assertNull(wishlistWithoutItems.getItems());

			//case 3
			WishList wishlistWithEmptyItems = new WishList();
			wishlistWithEmptyItems.setItems(new ArrayList<>());
			WishList wishlistWithEmptyItemsResponse = promotionService.getPromotions(wishlistWithEmptyItems, new ListQueryParam());
			assertNotNull(wishlistWithEmptyItemsResponse);
			assertNotNull(wishlistWithEmptyItemsResponse.getItems());

			//case 4
			WishList wishlistWithItems = new WishList();
			Item item1 = new Item();
			item1.setUpc(null);
			List<Item> items = new ArrayList<>();
			items.add(item1);
			wishlistWithItems.setItems(items);
			WishList wishlistWithItemsResponse = promotionService.getPromotions(wishlistWithItems, new ListQueryParam());
			assertNotNull(wishlistWithItemsResponse);
			assertNotNull(wishlistWithItemsResponse.getItems());
			assertNull(wishlistWithItemsResponse.getItems().get(0).getPromotions());

			//case 5
			Upc upc = new Upc();
			upc.setProduct(null);
			item1.setUpc(upc);
			items = new ArrayList<>();
			items.add(item1);
			wishlistWithItems.setItems(items);
 		when(killswitchProperties.isResponsiveWishlistPromotionsEnabled()).thenReturn(true);

		wishlistWithItemsResponse = promotionService.getPromotions(wishlistWithItems, new ListQueryParam());
			assertNotNull(wishlistWithItemsResponse);
			assertNotNull(wishlistWithItemsResponse.getItems());
			assertNull(wishlistWithItemsResponse.getItems().get(0).getPromotions());

			//case 5
			upc = new Upc();
			upc.setProduct(new Product());
			item1.setUpc(upc);
			items = new ArrayList<>();
			items.add(item1);
			wishlistWithItems.setItems(items);
		when(killswitchProperties.isResponsiveWishlistPromotionsEnabled()).thenReturn(true);

		wishlistWithItemsResponse = promotionService.getPromotions(wishlistWithItems, new ListQueryParam());
			assertNotNull(wishlistWithItemsResponse);
			assertNotNull(wishlistWithItemsResponse.getItems());
			assertNull(wishlistWithItemsResponse.getItems().get(0).getPromotions());

			//case 6
		when(killswitchProperties.isResponsiveWishlistPromotionsEnabled()).thenReturn(true);

		when(promotionsRestClient.getPromotions(any())).thenReturn(null);
			WishList wishlist1 = buildWishlist();
			wishlist1 = promotionService.getPromotions(wishlist1, new ListQueryParam());
			assertNotNull(wishlist1);
			assertNotNull(wishlist1.getItems());
			assertNull(wishlist1.getItems().get(0).getPromotions());

	} 

	@Test
	public void testGetPromotions() {
		WishList wishlist = buildWishlist();
		try {

			String serviceResponse = TestUtils.readFile(PROMOTIONS_FILE_PATH);
			RestResponse restResponse = new RestResponse();
			restResponse.setBody(serviceResponse);
			restResponse.setStatusCode(Response.Status.OK.getStatusCode());
			when(promotionsRestClient.getPromotions(any())).thenReturn(restResponse);
			when(promotions.parse(any())).thenReturn(mock(JsonNode.class));
			when(promotions.readValue(any())).thenReturn(mock(JsonNode.class));
			when(promotionsConverter.convert(any(),any(), any())).thenReturn(buildWishlistWithPromotions());

             when(killswitchProperties.isResponsiveWishlistPromotionsEnabled()).thenReturn(true);
			wishlist = promotionService.getPromotions(wishlist, new ListQueryParam());
			assertNotNull(wishlist);
			assertNotNull(wishlist.getItems());
			assertNotNull(wishlist.getItems().get(0).getPromotions());
		} catch (IOException  e) {
			e.printStackTrace();
		}catch (ParseException e) {
			e.printStackTrace();
		}
	} 

	@Test
	public void testGetPromotionsException() {
		WishList wishlist = buildWishlist();
		try {

			String serviceResponse = TestUtils.readFile(PROMOTIONS_FILE_PATH);
			RestResponse restResponse = new RestResponse();
			restResponse.setBody(serviceResponse);
			restResponse.setStatusCode(Response.Status.OK.getStatusCode());
			when(promotionsRestClient.getPromotions(any())).thenReturn(restResponse);
			when(promotions.parse(any())).thenReturn(mock(JsonNode.class));
			when(promotions.readValue(any())).thenReturn(mock(JsonNode.class));
			when(promotionsConverter.convert(any(),any(), any())).thenThrow(new ParseException("",0));

			when(killswitchProperties.isResponsiveWishlistPromotionsEnabled()).thenReturn(true);
			wishlist = promotionService.getPromotions(wishlist, new ListQueryParam());
			assertNotNull(wishlist);
			assertNotNull(wishlist.getItems());
			assertNotNull(wishlist.getItems().get(0).getPromotions());
		} catch (Exception  ex) {
			assertTrue(ex instanceof ListServiceException);
		}
	}

	//@Test
	public void testGetPromotionsException1() {
		WishList wishlist = buildWishlist();
		try {

			String serviceResponse = TestUtils.readFile(PROMOTIONS_FILE_PATH);
			RestResponse restResponse = new RestResponse();
			restResponse.setBody(serviceResponse);
			restResponse.setStatusCode(Response.Status.OK.getStatusCode());
			when(promotionsRestClient.getPromotions(any())).thenReturn(restResponse);
			when(promotions.parse(any())).thenThrow(new IOException());
			when(promotions.readValue(any())).thenReturn(mock(JsonNode.class));
			when(promotionsConverter.convert(any(),any(),any())).thenReturn(buildWishlistWithPromotions());

			when(killswitchProperties.isResponsiveWishlistPromotionsEnabled()).thenReturn(true);
			wishlist = promotionService.getPromotions(wishlist, new ListQueryParam());
			assertNotNull(wishlist);
			assertNotNull(wishlist.getItems());
			assertNotNull(wishlist.getItems().get(0).getPromotions());
		} catch (Exception  ex) {
			assertTrue(ex instanceof ListServiceException);
		}
	}


	private WishList buildWishlistWithPromotions() {
		WishList list = buildWishlist();

		List<Promotion> samplePromotions = new ArrayList<>();
		Promotion promotion = new Promotion();
		promotion.setBadgeTextAttributeValue(TEST_STRING);
		promotion.setPromotionId(PROM_ID_1);

		Promotion promotion1 = new Promotion();
		promotion1.setBadgeTextAttributeValue(TEST_STRING);
		promotion1.setPromotionId(PROM_ID_2);

		samplePromotions.add(promotion);
		samplePromotions.add(promotion1);

		list.getItems().forEach(item -> {
			if (item != null ) {
				item.setPromotions(samplePromotions);
			}
		});

		return list;
	}

	private WishList buildWishlist() {
		WishList list = new WishList();
		list.setId(TEST_LONG);
		list.setListGuid(TEST_STRING);
		list.setName(TEST_STRING);
		list.setItems(buildItems());

		return list;
	}

	private List<Item> buildItems(){

		Item item = new Item();
		item.setId(TEST_INT);
		item.setItemGuid(TEST_STRING);
		item.setRetailPriceWhenAdded(TEST_PRICE);
		item.setRetailPriceDropAfterAddedToList(TEST_PRICE);
		item.setRetailPriceDropPercentage(TEST_INT);
		item.setQtyRequested(TEST_INT);
		item.setQtyStillNeeded(TEST_INT);

		Item item1 = new Item();
		item1.setId(TEST_INT);
		item1.setItemGuid(TEST_STRING);
		item1.setRetailPriceWhenAdded(TEST_PRICE);
		item1.setRetailPriceDropAfterAddedToList(TEST_PRICE);
		item1.setRetailPriceDropPercentage(TEST_INT);
		item1.setQtyRequested(TEST_INT);
		item1.setQtyStillNeeded(TEST_INT);

		item.setUpc(buildUPCObject(TEST_INT, TEST_INT));
		item1.setUpc(buildUPCObject(TEST_INT, TEST_INT));
		List<Item> items = new ArrayList<>();
		items.add(item);
		return items;
	}


	private Upc buildUPCObject(Integer upcId, Integer productId){
		Price price = new Price();
		price.setRetailPrice(TEST_PRICE);
		price.setOriginalPrice(TEST_PRICE);
		price.setIntermediateSalesValue(TEST_PRICE);
		price.setSalesValue(TEST_PRICE);
		price.setOnSale(Boolean.FALSE);
		price.setUpcOnSale(Boolean.FALSE);
		price.setPriceType(TEST_INT);
		price.setBasePriceType(TEST_INT);

		Availability avail = new Availability();
		avail.setAvailable(Boolean.TRUE);
		avail.setUpcAvailabilityMessage(TEST_STRING);
		avail.setInStoreEligible(Boolean.TRUE);
		avail.setOrderMethod(TEST_STRING);

		Product product = new Product();
		product.setId(productId);
		product.setName(TEST_STRING);
		product.setActive(Boolean.TRUE);
		product.setPrimaryImage(TEST_STRING);
		product.setLive(Boolean.TRUE);
		product.setAvailable(Boolean.TRUE);

		Upc upc = new Upc();
		upc.setId(upcId);
		upc.setUpcNumber(TEST_LONG);
		upc.setColor(TEST_STRING);
		upc.setSize(TEST_STRING);
		upc.setPrice(price);
		upc.setAvailability(avail);
		upc.setProduct(product);

		return upc;
	}




}
