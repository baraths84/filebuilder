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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@JsonTest
public class UpcTest extends AbstractTestNGSpringContextTests {
	
	private static final String REVIEW_JSON_FILE = "com/macys/selection/xapi/list/rest/response/review_statistics.json";
	private static final String UPC_JSON = "com/macys/selection/xapi/list/rest/response/upc.json";
	private static final double TEST_PRICE = 39.0;
	private static final int TEST_INT = 31277389;
	private static final long TEST_LONG = 20714656027L;
	private static final String UPC_AVAILABLE_MESSAGE = "In Stock: Usually ships within 2 business days.";
	private static final String ORDER_METHOD = "POOL";
	private static final String TEST_COLOR = "Very Light";
	private static final String TEST_SIZE = "XS";
	private static final String TEST_PRIMARY_IMAGE = "1573855.fpx";
	private static final int TEST_REVIEW_COUNT = 274;
	private static final int TEST_PROD_ID = 813310;
	private static final String PRODUCT_NAME = "Clinique Moisture Surge CC Cream Colour Correcting Skin Protector Broad Spectrum SPF 30, 1.4 oz";
	private static final String TEST = "test";
	private static final String ANOTHER_IMAGE_URL = "5/optimized/1573855_fpx.tif";

  @Autowired private JacksonTester <Upc> json; 
  
  private Upc upc;
  
  @Test
  public void reviewStatisticsSerializeTest() throws ParseException, IOException {
    assertThat(this.json.write(upc)).isEqualToJson("upc.json");
  }  
  
  @Test
  public void reviewStatisticsDeserializeTest() throws ParseException, IOException {
    String upcJson = TestUtils.readFile(UPC_JSON);
    assertThat(this.json.parse(upcJson)).isEqualTo(upc);
    assertThat(this.json.parse(upcJson)).isEqualToComparingFieldByField(upc);
  }
  
  @Test
  public void reviewStatisticsEquaslsTest() {
    assertThat(upc.equals(null)).isFalse();
    assertThat(upc.equals(upc)).isTrue();    
  }

  @Test
  public void productHashCodeTest() throws IOException {
    String upcJson = TestUtils.readFile(REVIEW_JSON_FILE);
    Upc upc = this.json.parseObject(upcJson);  
    assertThat(upc.hashCode()).isNotNull();    
  }

  @Test
  public void productToStringTest() throws IOException {
    String upcJson = TestUtils.readFile(REVIEW_JSON_FILE);
    Upc upc = this.json.parseObject(upcJson);  
    assertThat(upc.toString()).isNotNull();    
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

    upc = new Upc();
    upc.setId(TEST_INT);
    upc.setUpcNumber(TEST_LONG);
    upc.setColor(TEST_COLOR);
    upc.setSize(TEST_SIZE);
    upc.setPrice(price);
    upc.setAvailability(avail);
    upc.setProduct(product);  
   }


}
