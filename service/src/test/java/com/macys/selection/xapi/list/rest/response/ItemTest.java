package com.macys.selection.xapi.list.rest.response;

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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@JsonTest
public class ItemTest extends AbstractTestNGSpringContextTests {
	
	private static final String ITEM_JSON_FILE = "com/macys/selection/xapi/list/rest/response/item.json";
	private static final double TEST_PRICE = 39.0;
	private static final int TEST_INT = 31277389;
	private static final long TEST_LONG = 20714656027L;
	private static final int TEST_PRICE_TYPE = 11;
	private static final String TEST_ITEM_GUID = "22ea0e5d-a1b2-476b-a647-f9b5a597a312";
	private static final String UPC_AVAILABLE_MESSAGE = "In Stock: Usually ships within 2 business days.";
	private static final String ORDER_METHOD = "POOL";
	private static final String TEST_COLOR = "Very Light";
	private static final String TEST_SIZE = "XS";
	private static final String TEST_PRIMARY_IMAGE = "1573855.fpx";
	private static final int TEST_REVIEW_COUNT = 274;
	private static final int TEST_PROD_ID = 813310;
	private static final int TEST_ITEM_ID = 10594035;
	private static final String IMAGE_URL = "5/optimized/1573855_fpx.tif";
	private static final String PRODUCT_NAME = "Clinique Moisture Surge CC Cream Colour Correcting Skin Protector Broad Spectrum SPF 30, 1.4 oz";
	private static final String BADGE_TEXT = "FREE SHIPPING & RETURNS";
	private static final String TEST = "test";
	private static final long PROM_ID = 19873358L;

  @Autowired private JacksonTester <Item> json; 

  private Item item;
  
  @Test
  public void itemSerializeTest() throws ParseException, IOException {
    assertThat(this.json.write(item)).isEqualToJson("item.json");
  }  
  
  @Test
  public void itemDeserializeTest() throws ParseException, IOException {
    String itemJson = TestUtils.readFile(ITEM_JSON_FILE);
    assertThat(this.json.parse(itemJson)).isEqualTo(item);
    assertThat(this.json.parse(itemJson)).isEqualToComparingFieldByField(item);
  }
  
  @Test
  public void itemEquaslsTest() {
    assertThat(item.equals(null)).isFalse();
    assertThat(item.equals(item)).isTrue();    
  }

  @Test
  public void itemHashCodeTest() throws IOException {
    String itemJson = TestUtils.readFile(ITEM_JSON_FILE);
    Item item = this.json.parseObject(itemJson);  
    assertThat(item.hashCode()).isNotNull();    
  }

  @Test
  public void itemToStringTest() throws IOException {
    String itemJson = TestUtils.readFile(ITEM_JSON_FILE);
    Item item = this.json.parseObject(itemJson);  
    assertThat(item.toString()).isNotNull();    
  }  
  
  @BeforeMethod
  public void setup() {
    Price price = new Price();
    price.setRetailPrice(TEST_PRICE);
    price.setOriginalPrice(TEST_PRICE);
    price.setIntermediateSalesValue(0.0);
    price.setSalesValue(0.0);
    price.setOnSale(Boolean.FALSE);
    price.setUpcOnSale(Boolean.FALSE);
    price.setPriceType(0);
    price.setBasePriceType(0);
    price.setPriceType(TEST_PRICE_TYPE);
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
    product.setImageURL(IMAGE_URL);
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
    
    item = new Item();
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
    items.add(item);  
    }

}
