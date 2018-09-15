package com.macys.selection.xapi.list.rest.response;

import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertTrue;

@SpringBootTest
@JsonTest
public class WishlistTest extends AbstractTestNGSpringContextTests {

	private static final String WISH_LIST_JSON_FILE = "com/macys/selection/xapi/list/rest/response/wishlist.json";	
	private static final double TEST_PRICE = 39.0;
	private static final int TEST_INT = 31277389;
	private static final long TEST_LONG = 20714656027L;
	private static final String TEST_LIST_GUID = "8396726c-68d0-4a1c-b123-ab7c4599bdb9";
	private static final String TEST_ITEM_GUID = "22ea0e5d-a1b2-476b-a647-f9b5a597a312";
	private static final String UPC_AVAILABLE_MESSAGE = "In Stock: Usually ships within 2 business days.";
	private static final String ORDER_METHOD = "POOL";
	private static final String TEST_COLOR = "Very Light";
	private static final String TEST_SIZE = "XS";
	private static final String TEST_PRIMARY_IMAGE = "1573855.fpx";
	private static final int TEST_REVIEW_COUNT = 274;
	private static final int TEST_PROD_ID = 813310;
	private static final int TEST_ITEM_ID = 10594035;
	private static final String IMAGE_URL = "5/optimized/365125_fpx.tif";
	private static final String PRODUCT_NAME = "Clinique Moisture Surge CC Cream Colour Correcting Skin Protector Broad Spectrum SPF 30, 1.4 oz";
	private static final String BADGE_TEXT = "FREE SHIPPING & RETURNS";
	private static final String TEST = "test";
	private static final long PROM_ID = 19873358L;
	private static final String GUEST_LIST = "Guest List";
	private static final String ANOTHER_IMAGE_URL = "5/optimized/1573855_fpx.tif";
	private static final String TEST_DATE = "2017-08-07 01:13:24.333";
	private static final String TEST_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS";

	@Autowired private JacksonTester <WishList> json; 

	private WishList wishlist;

	@BeforeMethod
	public void setup() throws ParseException {
		wishlist = expectedWishList(); 
	}

	@Test
	public void wishlistSerializeTest() throws ParseException, IOException {
		WishList wishlist = expectedWishList(); 
		assertThat(this.json.write(wishlist)).isEqualToJson("wishlist.json");  
	}

	@Test
	public void wishlistDeserializeTest() throws ParseException, IOException {
		String wishlistJson = TestUtils.readFile(WISH_LIST_JSON_FILE);
		assertThat(this.json.parse(wishlistJson)).isEqualTo(wishlist);
	}

	@Test
	public void wishlistEquaslsTest() {
		assertThat(wishlist.equals(null)).isFalse();
		assertThat(wishlist.equals(wishlist)).isTrue();    
	}

	@Test
	public void wishlistHashCodeTest() throws IOException {
		String wishlistJson = TestUtils.readFile(WISH_LIST_JSON_FILE);
		WishList wishlist = this.json.parseObject(wishlistJson);  
		assertThat(wishlist.hashCode()).isNotNull();    
	}

	@Test
	public void wishlistToStringTest() throws IOException {
		String wishlistJson = TestUtils.readFile(WISH_LIST_JSON_FILE);
		WishList wishlist = this.json.parseObject(wishlistJson);  
		assertThat(wishlist.toString()).isNotNull();    
	} 
	
	@Test
	public void wishlistCompareTest() throws IOException, ParseException {
		String wishlistJson = TestUtils.readFile(WISH_LIST_JSON_FILE);
	    SimpleDateFormat sdf = new SimpleDateFormat(TEST_DATE_FORMAT);
	    Date olderDate = sdf.parse(TEST_DATE);
		WishList wishlistWithEarlyCreatedDate = this.json.parseObject(wishlistJson);
		wishlistWithEarlyCreatedDate.setCreatedDate(olderDate);
		WishList wishlistWithLaterCreatedDate = this.json.parseObject(wishlistJson);
		wishlistWithLaterCreatedDate.setCreatedDate(new Date());
		wishlistWithEarlyCreatedDate.setDefaultList(Boolean.FALSE);
		wishlistWithLaterCreatedDate.setDefaultList(Boolean.FALSE);
		assertTrue(wishlistWithLaterCreatedDate.compareTo(wishlistWithEarlyCreatedDate) < 0);
		
		wishlistWithEarlyCreatedDate.setDefaultList(Boolean.FALSE);
		wishlistWithLaterCreatedDate.setDefaultList(Boolean.TRUE);
		assertTrue(wishlistWithEarlyCreatedDate.compareTo(wishlistWithLaterCreatedDate) > 0);
	}  


	public static WishList expectedWishList() throws ParseException {
		WishList list = new WishList();
		list.setId(1281186L);
		list.setListGuid(TEST_LIST_GUID);
		list.setName(GUEST_LIST);
		list.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
		list.setDefaultList(Boolean.TRUE);
		list.setOnSaleNotify(Boolean.FALSE);
		list.setSearchable(Boolean.FALSE);
		list.setNumberOfItems(1);
		list.setShowPurchaseInfo(Boolean.TRUE);

		Price price = new Price();
		price.setRetailPrice(TEST_PRICE);
		price.setOriginalPrice(TEST_PRICE);
		price.setIntermediateSalesValue(0.0);
		price.setSalesValue(0.0);
		price.setOnSale(Boolean.FALSE);
		price.setUpcOnSale(Boolean.FALSE);
		price.setPriceType(0);
		price.setBasePriceType(0);
		price.setRetailPriceHigh(0.0);
		price.setPriceTypeText(TEST);
		price.setOriginalPriceLabel(TEST);
		price.setRetailPriceLabel(TEST);
		price.setPricingPolicyText(TEST);
		price.setPricingPolicy(TEST);

		Availability avail = new Availability();
		avail.setAvailable(Boolean.TRUE);
		avail.setUpcAvailabilityMessage(UPC_AVAILABLE_MESSAGE);
		avail.setInStoreEligible(Boolean.TRUE);
		avail.setOrderMethod(ORDER_METHOD);

		Product product = new Product();
		product.setId(TEST_PROD_ID);
		product.setName(PRODUCT_NAME);
		product.setActive(Boolean.TRUE);
		product.setPrimaryImage(TEST_PRIMARY_IMAGE);
		product.setLive(Boolean.TRUE);
		product.setAvailable(Boolean.TRUE);
		product.setSuppressReviews(Boolean.FALSE);
		ReviewStatistics reviewStatistics = new ReviewStatistics();
		reviewStatistics.setAverageRating(Float.valueOf("4.6095"));
		reviewStatistics.setReviewCount(TEST_REVIEW_COUNT);
		product.setReviewStatistics(reviewStatistics);
		product.setImageURL(ANOTHER_IMAGE_URL);
		product.setPrimaryImage(TEST_PRIMARY_IMAGE);
		product.setClickToCall(TEST);
		product.setMultipleUpc(false);
		product.setPhoneOnly(TEST);
		product.setPrice(price);

		Upc upc = new Upc();
		upc.setId(TEST_INT);
		upc.setUpcNumber(TEST_LONG);
		upc.setColor(TEST_COLOR);
		upc.setSize(TEST_SIZE);
		upc.setPrice(price);
		upc.setAvailability(avail);
		upc.setProduct(product);

		// promotions
		Promotion promotion = new Promotion();    
		promotion.setBadgeTextAttributeValue(BADGE_TEXT);
		promotion.setPromotionId(PROM_ID);
		List<Promotion> promotions = new ArrayList<Promotion>();
		promotions.add(promotion);

		Item item = new Item();
		item.setId(TEST_ITEM_ID);
		item.setItemGuid(TEST_ITEM_GUID);    
		item.setRetailPriceWhenAdded(TEST_PRICE);        
		item.setRetailPriceDropAfterAddedToList(0.0);
		item.setRetailPriceDropPercentage(0);
		item.setQtyRequested(1);
		item.setQtyStillNeeded(0);
		item.setPromotions(promotions);
		item.setProduct(product);
		item.setUpc(upc);
		List<Item> items = new ArrayList<>();
		String imageUrl = IMAGE_URL;
		List<String> imageUrlList = new ArrayList<String>();
		imageUrlList.add(imageUrl);
		list.setImageUrlsList(imageUrlList);
		items.add(item);

		list.setItems(items);    
		return list;
	}

}
