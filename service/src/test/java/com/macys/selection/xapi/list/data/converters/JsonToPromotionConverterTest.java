package com.macys.selection.xapi.list.data.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.response.*;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

@SpringBootTest(classes = {JsonToPromotionConverter.class,KillSwitchPropertiesBean.class,JsonResponseParserPromotions.class})
public class JsonToPromotionConverterTest extends AbstractTestNGSpringContextTests {
	private static final long LIST_ID = 10680021L;
    private static final int ITEM1_ID = 10594035;
    private static final int ITEM2_ID = 10594036;
    private static final int UPC1_ID = 36652740;
    private static final int UPC2_ID = 37218907;
    private static final long UPC_NUMBER = 191479341845L;
    private static final double RETAIL_PRICE = 99.0;
	private static final int PRODUCT_ID1 = 3543075;
    private static final int PRODUCT_ID2 = 3675506;
	private static final String TEST_PRIMARY_IMAGE = "8748361.fpx";
	private static final String PROM_JSON_FILE = "com/macys/selection/xapi/wishlist/converters/promotions.json";
	private static final String PROM_FINALPRICE_JSON_FILE = "com/macys/selection/xapi/wishlist/converters/promotions_finalPrice.json";
	private static final String TEST_PRODUCT_NAME = "The North Face Tamburello Insulated Ski Jacket";
	private static final String TEST_COLOR = "Petticoat";
	private static final String TEST_SIZE = "XS";
	private static final String ORDER_METHOD = "POOL";
	private static final String TEST_ITEM_GUID = "f4fa2368-1c07-4da6-8ae1-1eec7cef7b23";
	private static final String UPC_AVAILABLE_MESSAGE = "In Stock: Usually ships within 2 business days.";
	private static final String GUID = "a4fa2368-1c07-4da6-8ae1-1eec7cef7b23";
	private static final String GUID_2 = "71adefa2-183a-4a9e-aa26-a3b96c11db24";
	private static final String GUEST_LIST = "Guest List";
	private static final String bcom_promotion_response1 = "{\"ProductUPCPromotionResponse\":{\"productUPCPromotionIDs\":{\"132387\":{},\"3543075\":{\"productPromotionIDs\":{\"promotionIds\":[20725,22188,22189,22190,22191,22192,9160]}},\"2856721\":{\"productPromotionIDs\":{\"promotionIds\":[20725,22188,22189,22190,22191,22192,9160]}},\"1753361\":{\"productPromotionIDs\":{\"promotionIds\":[20726]}},\"608452\":{\"productPromotionIDs\":{\"promotionIds\":[22055]},\"upcPromotionIDs\":{\"1185254\":{\"promotionIds\":[22055]},\"1185255\":{\"promotionIds\":[22055]},\"1185252\":{\"promotionIds\":[22055]},\"1185253\":{\"promotionIds\":[22055]},\"1185258\":{\"promotionIds\":[22055]},\"1185259\":{\"promotionIds\":[22055]},\"1185256\":{\"promotionIds\":[22055]},\"1185257\":{\"promotionIds\":[22055]},\"1185260\":{\"promotionIds\":[22055]}}}},\"promotions\":{\"20726\":{\"global\":false,\"promotionType\":\"Loyalty Offer Multiplier\",\"offerDescription\":\"\",\"promotionAttribute\":[{\"promotionID\":20726,\"promotionAttributeName\":\"CHANNEL_ELIGIBLE\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"CHANNEL_ELIGIBLE\",\"attributeValue\":\"All_Channels\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"EVENT_TYPE\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"EVENT_TYPE\",\"attributeValue\":\"GENERIC\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"IS_OFFERS_ELIGIBLE\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"IS_OFFERS_ELIGIBLE\",\"attributeValue\":\"Y\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"LOYALLIST_FLAG\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"LOYALLIST_FLAG\",\"attributeValue\":\"Y\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"OFFER_CODE\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"OFFER_CODE\",\"attributeValue\":\"BLM000000103866\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"OFFER_DESCRIPTION\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"OFFER_DESCRIPTION\",\"attributeValue\":\"Get Double Points in shoes every day!\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"OFFER_FREQUENCY\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"OFFER_FREQUENCY\",\"attributeValue\":\"Valid Mult. Times\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"OFFER_NAME\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"OFFER_NAME\",\"attributeValue\":\"GET DOUBLE POINTS ON THIS ITEM\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"OFFER_TITLE\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"OFFER_TITLE\",\"attributeValue\":\"Double Points for Loyallists\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"PROMO_ASSOCIATED_FOB_ID\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"PROMO_ASSOCIATED_FOB_ID\",\"attributeValue\":\"1011\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"PROMO_CLICK_THROUGH_URL\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"PROMO_CLICK_THROUGH_URL\",\"attributeValue\":\"http://www1.bloomingdales.com/shop/shoes?id=16961\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"PROMO_HEADING\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"PROMO_HEADING\",\"attributeValue\":\"Double Points in Shoes\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"PROMO_SUB_DESCRIPTION\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"PROMO_SUB_DESCRIPTION\",\"attributeValue\":\"Loyallists get Double Points in shoes every day!\"}]},{\"promotionID\":20726,\"promotionAttributeName\":\"TIER_NAME\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20726,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"TIER_NAME\",\"attributeValue\":\"All\"}]}],\"validWithOtherPromotions\":true,\"gwp\":false},\"20725\":{\"global\":false,\"promotionType\":\"Loyalty Offer Multiplier\",\"offerDescription\":\"\",\"promotionAttribute\":[{\"promotionID\":20725,\"promotionAttributeName\":\"CHANNEL_ELIGIBLE\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"CHANNEL_ELIGIBLE\",\"attributeValue\":\"All_Channels\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"ELIGIBLE_FOR_FACET\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"ELIGIBLE_FOR_FACET\",\"attributeValue\":\"Y\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"EVENT_TYPE\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"EVENT_TYPE\",\"attributeValue\":\"GENERIC\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"IS_OFFERS_ELIGIBLE\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"IS_OFFERS_ELIGIBLE\",\"attributeValue\":\"Y\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"LOYALLIST_FLAG\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"LOYALLIST_FLAG\",\"attributeValue\":\"Y\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"OFFER_CODE\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"OFFER_CODE\",\"attributeValue\":\"BLM000000103867\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"OFFER_DESCRIPTION\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"OFFER_DESCRIPTION\",\"attributeValue\":\"Get Double Points in cosmetics and fragrances every day!\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"OFFER_FREQUENCY\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"OFFER_FREQUENCY\",\"attributeValue\":\"Valid Mult. Times\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"OFFER_NAME\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"OFFER_NAME\",\"attributeValue\":\"DOUBLE POINTS IN BEAUTY\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"OFFER_TITLE\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"OFFER_TITLE\",\"attributeValue\":\"Double Points For Loyallists\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"PROMO_ASSOCIATED_FOB_ID\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"PROMO_ASSOCIATED_FOB_ID\",\"attributeValue\":\"1011\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"PROMO_CLICK_THROUGH_URL\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"PROMO_CLICK_THROUGH_URL\",\"attributeValue\":\" http://www1.bloomingdales.com/shop/beauty?id=2921\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"PROMO_HEADING\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"PROMO_HEADING\",\"attributeValue\":\"Double Points In Beauty\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"PROMO_SUB_DESCRIPTION\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"PROMO_SUB_DESCRIPTION\",\"attributeValue\":\"Loyallists get Double Points in beauty every day!\"}]},{\"promotionID\":20725,\"promotionAttributeName\":\"TIER_NAME\",\"attributeSeqNumber\":1,\"attributeValue\":[{\"promotionID\":20725,\"attributeValueSeqNumber\":1,\"promotionAttributeName\":\"TIER_NAME\",\"attributeValue\":\"All\"}]}],\"validWithOtherPromotions\":true,\"gwp\":false},\"22188\":{\"global\":false,\"promotionType\":\"Bundled GWP\",\"offerDescription\":\"GIFT WITH PURCHASE\",\"validWithOtherPromotions\":true,\"gwp\":false},\"22189\":{\"global\":false,\"promotionType\":\"Bundled GWP\",\"offerDescription\":\"GIFT WITH PURCHASE\",\"validWithOtherPromotions\":true,\"gwp\":false},\"22190\":{\"global\":false,\"promotionType\":\"Bundled GWP\",\"offerDescription\":\"GIFT WITH PURCHASE\",\"validWithOtherPromotions\":true,\"gwp\":false},\"22191\":{\"global\":false,\"promotionType\":\"Bundled GWP\",\"offerDescription\":\"GIFT WITH PURCHASE\",\"validWithOtherPromotions\":true,\"gwp\":false},\"22192\":{\"global\":false,\"promotionType\":\"Bundled GWP\",\"offerDescription\":\"GIFT WITH PURCHASE\",\"validWithOtherPromotions\":true,\"gwp\":false},\"9160\":{\"global\":false,\"promotionType\":\"Free Shipping\",\"offerDescription\":\"\",\"validWithOtherPromotions\":true,\"gwp\":false},\"22055\":{\"global\":false,\"promotionType\":\"Promotional Pricing\",\"offerDescription\":\"Buy 2 HUE Tights for $16\",\"validWithOtherPromotions\":true,\"gwp\":false}}}}";


	@Autowired
	private JsonToPromotionConverter jsonToPromotionConverter;

	@Autowired
	private JsonResponseParserPromotions promotions;


	@Mock
	KillSwitchPropertiesBean killSwitchPropertiesBeanMock ;


	@BeforeMethod
	public void init() {
		MockitoAnnotations.initMocks(this);
		jsonToPromotionConverter = new JsonToPromotionConverter();
		KillSwitchPropertiesBean killswitchPropertiesBean = new KillSwitchPropertiesBean();
		jsonToPromotionConverter.setKillswitchPropertiesBean(killswitchPropertiesBean);
	}

	@Test
	public void testConvertErrorConditions() throws ParseException, IOException {
		WishList wishlist = jsonToPromotionConverter.convert(null, null, null);
		assertNull(wishlist);

		String serviceResponse =
				TestUtils.readFile(PROM_JSON_FILE);
		JsonNode nodes = promotions.parse(serviceResponse);
		JsonNode promotionsNode = promotions.readValue(nodes);

		wishlist = jsonToPromotionConverter.convert(null, promotionsNode, new ListQueryParam());
		assertNull(wishlist);

		wishlist = jsonToPromotionConverter.convert(new WishList(), promotionsNode, new ListQueryParam());
		assertNotNull(wishlist);

		wishlist = new WishList();
		List<Item> items = new ArrayList<>();
		items.add(null);
		wishlist.setItems(items);
		wishlist = jsonToPromotionConverter.convert(wishlist, promotionsNode, new ListQueryParam());
		assertNotNull(wishlist);

		items = new ArrayList<>();
		items.add(new Item());
		wishlist.setItems(items);
		wishlist = jsonToPromotionConverter.convert(wishlist, promotionsNode, new ListQueryParam());
		assertNotNull(wishlist);

		items = new ArrayList<>();
		Item item = new Item();
		item.setUpc(new Upc());
		items.add(item);
		wishlist.setItems(items);
		wishlist = jsonToPromotionConverter.convert(wishlist, promotionsNode, new ListQueryParam());
		assertNotNull(wishlist);

		items = new ArrayList<>();
		item = new Item();
		Upc upc = new Upc();
		upc.setProduct(new Product());
		item.setUpc(upc);
		items.add(item);
		wishlist.setItems(items);
		wishlist = jsonToPromotionConverter.convert(wishlist, promotionsNode, new ListQueryParam());
		assertNotNull(wishlist);
	}

	@Test
	public void testConvertSuccess() throws ParseException, IOException {

		String serviceResponse =
				TestUtils.readFile(PROM_JSON_FILE);
		JsonNode nodes = promotions.parse(serviceResponse);
		JsonNode promotionsNode = promotions.readValue(nodes);

		WishList wishlist = jsonToPromotionConverter.convert(buildWishlist(), promotionsNode, new ListQueryParam());
		assertNotNull(wishlist);
		assertNotNull(wishlist.getItems());
		assertNotNull(wishlist.getItems().get(0).getPromotions());
		assertEquals("EXTRA 25% OFF USE: FRESH",
				wishlist.getItems().get(0).getPromotions().get(0).getBadgeTextAttributeValue());

	}

	@Test
	public void testConvertSuccessWithFinalPriceFeatureOn() throws ParseException, IOException {

		String serviceResponse =
				TestUtils.readFile(PROM_FINALPRICE_JSON_FILE);
		JsonNode nodes = promotions.parse(serviceResponse);
		JsonNode promotionsNode = promotions.readValue(nodes);

		WishList wishlist = jsonToPromotionConverter.convert(buildWishlistWithFinalPrice(), promotionsNode, new ListQueryParam());
		assertNotNull(wishlist);
		assertNotNull(wishlist.getItems());
		assertNotNull(wishlist.getItems().get(0).getPromotions());
		assertEquals(wishlist.getItems().get(0).getPromotions().size(),3);
		assertEquals("EXTRA 15% OFF",
				wishlist.getItems().get(0).getPromotions().get(0).getBadgeTextAttributeValue());

		assertEquals("EXTRA 25% OFF USE: FRESH",
				wishlist.getItems().get(0).getPromotions().get(1).getBadgeTextAttributeValue());

		assertEquals("coupon excluded",
				wishlist.getItems().get(0).getPromotions().get(2).getBadgeTextAttributeValue());
	}

	@Test
	public void testConvertSuccessWithFinalPriceFeatureOnMASK() throws ParseException, IOException {

		String serviceResponse =
				TestUtils.readFile(PROM_FINALPRICE_JSON_FILE);
		JsonNode nodes = promotions.parse(serviceResponse);
		JsonNode promotionsNode = promotions.readValue(nodes);
		ListQueryParam listQueryParam = new ListQueryParam();
		listQueryParam.setCustomerState("GUEST");
		WishList wishlist = jsonToPromotionConverter.convert(buildWishlistWithFinalPriceMask(), promotionsNode, listQueryParam);
		assertNotNull(wishlist);
		assertNotNull(wishlist.getItems());
		assertNotNull(wishlist.getItems().get(0).getPromotions());
		assertEquals(wishlist.getItems().get(0).getPromotions().size(),3);
		assertEquals("See Bag for price",
				wishlist.getItems().get(0).getPromotions().get(0).getBadgeTextAttributeValue());

		assertEquals("See Bag for price",
				wishlist.getItems().get(0).getPromotions().get(1).getBadgeTextAttributeValue());

		assertEquals("coupon excluded",
				wishlist.getItems().get(0).getPromotions().get(2).getBadgeTextAttributeValue());
	}

	@Test
	public void testConvertSuccessWithFinalPriceFeatureOff() throws ParseException, IOException {
		when(killSwitchPropertiesBeanMock.isFinalPriceDisplayEnabled()).thenReturn(false);

		jsonToPromotionConverter.setKillswitchPropertiesBean(killSwitchPropertiesBeanMock);

		String serviceResponse =
				TestUtils.readFile(PROM_FINALPRICE_JSON_FILE);
		JsonNode nodes = promotions.parse(serviceResponse);
		JsonNode promotionsNode = promotions.readValue(nodes);

		WishList wishlist = jsonToPromotionConverter.convert(buildWishlistWithFinalPrice(), promotionsNode, new ListQueryParam());
		assertNotNull(wishlist);
		assertNotNull(wishlist.getItems());
		assertNotNull(wishlist.getItems().get(0).getPromotions());
		assertEquals(wishlist.getItems().get(0).getPromotions().size(),3);
		assertEquals("coupon excluded",
				wishlist.getItems().get(0).getPromotions().get(0).getBadgeTextAttributeValue());

		assertEquals("EXTRA 25% OFF USE: FRESH",
				wishlist.getItems().get(0).getPromotions().get(1).getBadgeTextAttributeValue());

		assertEquals("EXTRA 15% OFF",
				wishlist.getItems().get(0).getPromotions().get(2).getBadgeTextAttributeValue());
	}



	@Test
	public void testGetPromotionsIdsWithOutPromotions() throws JsonProcessingException, ParseException {
		String serviceResponse = "{\"ProductUPCPromotionResponse\":{}}";
		JsonNode nodes = promotions.parse(serviceResponse);
		JsonNode promotionsNode = promotions.readValue(nodes);

		WishList wishlist = jsonToPromotionConverter.convert(buildWishlist(), promotionsNode, new ListQueryParam());
		verifyWishlist(wishlist);
	}

	@Test
	public void testGetPromotionsIdsWithoutPromotions() throws JsonProcessingException, ParseException {

		String serviceResponse =
				"{\"ProductUPCPromotionResponse\":{\"productUPCPromotionIDs\":{\"3543075\":{\"productPromotionIDs\":{\"promotionIds\":[19876644]},\"upcPromotionIDs\":{\"36652740\":{\"promotionIds\":[19876644]}}}}}}";
		JsonNode nodes = promotions.parse(serviceResponse);
		JsonNode promotionsNode = promotions.readValue(nodes);

		WishList wishlist = jsonToPromotionConverter.convert(buildWishlist(), promotionsNode, new ListQueryParam());
		verifyWishlist(wishlist);

	}

	@Test
	public void testGetPromotionsIdsWithEmptyPromotions() throws JsonProcessingException, ParseException {

		String serviceResponse =
				"{\"ProductUPCPromotionResponse\":{\"productUPCPromotionIDs\":{\"3543075\":{\"productPromotionIDs\":{\"promotionIds\":[19876644]},\"upcPromotionIDs\":{\"36652740\":{\"promotionIds\":[19876644]}}}},\"promotions\":{\"19876644\":{}}}}";
		JsonNode nodes = promotions.parse(serviceResponse);
		JsonNode promotionsNode = promotions.readValue(nodes);

		WishList wishlist = jsonToPromotionConverter.convert(buildWishlist(), promotionsNode, new ListQueryParam());
		verifyWishlist(wishlist);

	}

	private void verifyWishlist(WishList wishlist){
		assertNotNull(wishlist);
		assertNotNull(wishlist.getItems());
		assertNotNull(wishlist.getItems().get(0).getPromotions());
		assertEquals(wishlist.getItems().get(0).getPromotions().size(), 0);
	}

	private WishList buildWishlist() {
		WishList list = new WishList();
		list.setId(LIST_ID);
		list.setListGuid(GUID_2);
		list.setName(GUEST_LIST);
		list.setItems(buildItems());

		return list;
	}

	private WishList buildWishlistWithFinalPrice() {
		WishList list = new WishList();
		list.setId(LIST_ID);
		list.setListGuid(GUID_2);
		list.setName(GUEST_LIST);
		list.setItems(buildFinalPriceItems());

		return list;
	}

	private List<Item> buildFinalPriceItems() {

		List<Item> items = buildItems();
		FinalPrice finalPrice = new FinalPrice();
		List<FinalPricePromotionDO> finalPricePromotionDOList = new ArrayList<>();
		FinalPricePromotionDO finalPricePromotionDO1 = new FinalPricePromotionDO();
		finalPricePromotionDO1.setGlobal(true);
		finalPricePromotionDO1.setPromotionId(19876645);
		finalPricePromotionDO1.setPromotionName("EXTRA 15% OFF");
		FinalPricePromotionDO finalPricePromotionDO2 = new FinalPricePromotionDO();
		finalPricePromotionDO2.setGlobal(false);
		finalPricePromotionDO2.setPromotionId(19876644);
		finalPricePromotionDO2.setPromotionName("EXTRA 25% OFF USE: FRESH");
		finalPrice.setFinalPrice(30);
		finalPrice.setDisplayFinalPrice(WishlistConstants.FINAL_PRICE_ALWAYS_SHOW);
		finalPrice.setProductTypePromotion("NONE");
		finalPrice.setFinalPriceHigh(0);
		finalPricePromotionDOList.add(finalPricePromotionDO1);
		finalPricePromotionDOList.add(finalPricePromotionDO2);
		finalPrice.setPromotions(finalPricePromotionDOList);
		items.get(0).getUpc().setFinalPrice(finalPrice);

		return items;
	}

	private WishList buildWishlistWithFinalPriceMask() {
		WishList list = new WishList();
		list.setId(LIST_ID);
		list.setListGuid(GUID_2);
		list.setName(GUEST_LIST);
		list.setItems(buildFinalPriceItemsMask());

		return list;
	}

	private List<Item> buildFinalPriceItemsMask() {

		List<Item> items = buildItems();
		FinalPrice finalPrice = new FinalPrice();
		List<FinalPricePromotionDO> finalPricePromotionDOList = new ArrayList<>();
		FinalPricePromotionDO finalPricePromotionDO1 = new FinalPricePromotionDO();
		finalPricePromotionDO1.setGlobal(true);
		finalPricePromotionDO1.setPromotionId(19876645);
		finalPricePromotionDO1.setPromotionName("EXTRA 15% OFF");
		FinalPricePromotionDO finalPricePromotionDO2 = new FinalPricePromotionDO();
		finalPricePromotionDO2.setGlobal(false);
		finalPricePromotionDO2.setPromotionId(19876644);
		finalPricePromotionDO2.setPromotionName("EXTRA 25% OFF USE: FRESH");
		finalPrice.setFinalPrice(30);
		finalPrice.setDisplayFinalPrice(WishlistConstants.FINAL_PRICE_CONDITIONAL_SHOW);
		finalPrice.setProductTypePromotion("MASK");
		finalPrice.setFinalPriceHigh(0);
		finalPricePromotionDOList.add(finalPricePromotionDO1);
		finalPricePromotionDOList.add(finalPricePromotionDO2);
		finalPrice.setPromotions(finalPricePromotionDOList);
		items.get(0).getUpc().setFinalPrice(finalPrice);

		return items;
	}

	private List<Item> buildItems() {

		Item item = new Item();
		item.setId(ITEM1_ID);
		item.setItemGuid(TEST_ITEM_GUID);
		item.setRetailPriceWhenAdded(RETAIL_PRICE);
		item.setRetailPriceDropAfterAddedToList(0.0);
		item.setRetailPriceDropPercentage(0);
		item.setQtyRequested(1);
		item.setQtyStillNeeded(0);

		Item item1 = new Item();
		item1.setId(ITEM2_ID);
		item1.setItemGuid(GUID);
		item1.setRetailPriceWhenAdded(RETAIL_PRICE);
		item1.setRetailPriceDropAfterAddedToList(0.0);
		item1.setRetailPriceDropPercentage(0);
		item1.setQtyRequested(1);
		item1.setQtyStillNeeded(0);

		item.setUpc(buildUPCObject(UPC1_ID, PRODUCT_ID1));
		item1.setUpc(buildUPCObject(UPC2_ID, PRODUCT_ID2));

		Product product = new Product();
		product.setId(PRODUCT_ID2);
		product.setName(TEST_PRODUCT_NAME);
		product.setActive(Boolean.TRUE);
		product.setPrimaryImage(TEST_PRIMARY_IMAGE);
		product.setLive(Boolean.TRUE);
		product.setAvailable(Boolean.TRUE);

		item1.setProduct(product);
		List<Item> items = new ArrayList<>();
		items.add(item);
		items.add(item1);
		return items;
	}

	private Upc buildUPCObject(Integer upcId, Integer productId) {
		Price price = new Price();
		price.setRetailPrice(RETAIL_PRICE);
		price.setOriginalPrice(RETAIL_PRICE);
		price.setIntermediateSalesValue(0.0);
		price.setSalesValue(0.0);
		price.setOnSale(Boolean.FALSE);
		price.setUpcOnSale(Boolean.FALSE);
		price.setPriceType(0);
		price.setBasePriceType(0);

		Availability avail = new Availability();
		avail.setAvailable(Boolean.TRUE);
		avail.setUpcAvailabilityMessage(UPC_AVAILABLE_MESSAGE);
		avail.setInStoreEligible(Boolean.TRUE);
		avail.setOrderMethod(ORDER_METHOD);

		Product product = new Product();
		product.setId(productId);
		product.setName(TEST_PRODUCT_NAME);
		product.setActive(Boolean.TRUE);
		product.setPrimaryImage(TEST_PRIMARY_IMAGE);
		product.setLive(Boolean.TRUE);
		product.setAvailable(Boolean.TRUE);

		Upc upc = new Upc();
		upc.setId(upcId);
		upc.setUpcNumber(UPC_NUMBER);
		upc.setColor(TEST_COLOR);
		upc.setSize(TEST_SIZE);
		upc.setPrice(price);
		upc.setAvailability(avail);
		upc.setProduct(product);

		return upc;
	}
	@Test
	public void testConvertSuccess_BCOM() throws ParseException, IOException {
		ReflectionTestUtils.setField(jsonToPromotionConverter, "applicationName", "BCOM");
		ReflectionTestUtils.setField(jsonToPromotionConverter, "isBCOM", true);
		JsonNode nodes = promotions.parse(bcom_promotion_response1);
		JsonNode promotionsNode = promotions.readValue(nodes);

		WishList wishlist = jsonToPromotionConverter.convert(buildWishlist(), promotionsNode, new ListQueryParam());
		assertNotNull(wishlist);
		assertNotNull(wishlist.getItems());
		assertEquals(wishlist.getItems().get(0).getPromotions().size(),6);
        assertEquals(wishlist.getItems().get(0).getPromotions().get(0).getPromotionId(),Long.valueOf(22188));
        assertEquals(wishlist.getItems().get(0).getPromotions().get(1).getPromotionId(),Long.valueOf(22189));
        assertEquals(wishlist.getItems().get(0).getPromotions().get(2).getPromotionId(),Long.valueOf(22190));
        assertEquals(wishlist.getItems().get(0).getPromotions().get(3).getPromotionId(),Long.valueOf(22191));
        assertEquals(wishlist.getItems().get(0).getPromotions().get(4).getPromotionId(),Long.valueOf(22192));
        assertEquals(wishlist.getItems().get(0).getPromotions().get(5).getPromotionId(),Long.valueOf(20725));
		assertEquals("GIFT WITH PURCHASE",
				wishlist.getItems().get(0).getPromotions().get(0).getBadgeTextAttributeValue());
        assertEquals("GIFT WITH PURCHASE",
                wishlist.getItems().get(0).getPromotions().get(1).getBadgeTextAttributeValue());
        assertEquals("GIFT WITH PURCHASE",
                wishlist.getItems().get(0).getPromotions().get(2).getBadgeTextAttributeValue());
        assertEquals("GIFT WITH PURCHASE",
                wishlist.getItems().get(0).getPromotions().get(3).getBadgeTextAttributeValue());
        assertEquals("GIFT WITH PURCHASE",
                wishlist.getItems().get(0).getPromotions().get(4).getBadgeTextAttributeValue());
        assertEquals("Get Double Points in cosmetics and fragrances every day!",
                wishlist.getItems().get(0).getPromotions().get(5).getBadgeTextAttributeValue());
        assertEquals(wishlist.getItems().get(1).getPromotions().size(),0);

	}
}
