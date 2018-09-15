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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@JsonTest
public class ProductTest extends AbstractTestNGSpringContextTests {

  @Autowired private JacksonTester <Product> json; 

  private static final Integer   PROD_ID = 813310;
  private static final String    PROD_NAME = "Clinique Moisture Surge CC Cream Colour Correcting Skin Protector Broad Spectrum SPF 30, 1.4 oz";
  private static final Boolean   PROD_ACTIVE = Boolean.TRUE;
  private static final String    PROD_PRIMARY_IMAGES = "1573855.fpx";
  private static final Boolean   PROD_LIVE = Boolean.TRUE;
  private static final Boolean   PROD_AVAILABLE = Boolean.TRUE;
  private static final Boolean   PROD_SUPPRESS_REVIEWS = Boolean.FALSE;
  private static final int TEST_REVIEW_COUNT = 274;
	private static final String TEST = "test";
	private static final double TEST_PRICE = 39.0;
	private static final String JSON_FILE = "com/macys/selection/xapi/list/rest/response/product.json";
	private static final String TEST_IMAGE_URL = "5/optimized/1573855_fpx.tif";
	private static final String PRIMARY_IMAGE = "1573855.fpx";
  
  private Product product;
  
  @BeforeMethod
  public void setup() {
    product = new Product();
    product.setId(PROD_ID);
    product.setName(PROD_NAME);
    product.setActive(PROD_ACTIVE);
    product.setPrimaryImage(PROD_PRIMARY_IMAGES);
    product.setLive(PROD_LIVE);
    product.setAvailable(PROD_AVAILABLE);
    product.setSuppressReviews(PROD_SUPPRESS_REVIEWS);
    ReviewStatistics reviewStatistics = new ReviewStatistics();
    reviewStatistics.setAverageRating(Float.valueOf("4.6095"));
    reviewStatistics.setReviewCount(TEST_REVIEW_COUNT);
    product.setReviewStatistics(reviewStatistics);
    product.setImageURL(TEST_IMAGE_URL);
    product.setPrimaryImage(PRIMARY_IMAGE);
    product.setClickToCall(TEST);
    product.setMultipleUpc(false);
    product.setPhoneOnly(TEST);
    
    Price price = new Price();
    price.setBasePriceType(0);
    price.setIntermediateSalesValue(0.0);
    price.setOnSale(false);
    price.setOriginalPrice(TEST_PRICE);
    price.setPriceType(11);
    price.setPriceTypeText(TEST);
    price.setRetailPrice(TEST_PRICE);
    price.setRetailPriceHigh(0.0);
    price.setSalesValue(0.0);
    price.setUpcOnSale(false);
    price.setPriceTypeText(TEST);
    price.setOriginalPriceLabel(TEST);
    price.setRetailPriceLabel(TEST);
    price.setPricingPolicyText(TEST);
    price.setPricingPolicy(TEST);
    product.setPrice(price);
  }
  
  @Test
  public void productSerializeTest() throws IOException {
    assertThat(this.json.write(product)).isEqualToJson("product.json");
  }
  
  @Test
  public void productDeserializeTest() throws IOException {
    String productJson = TestUtils.readFile(JSON_FILE);
    assertThat(this.json.parse(productJson)).isEqualTo(product);
  }
  
  @Test
  public void productEquaslsTest() {
    assertThat(product.equals(null)).isFalse();
    assertThat(product.equals(product)).isTrue();    
  }

  @Test
  public void productHashCodeTest() throws IOException {
    String productJson = TestUtils.readFile(JSON_FILE);
    Product price = this.json.parseObject(productJson);  
    assertThat(price.hashCode()).isNotNull();    
  }

  @Test
  public void productToStringTest() throws IOException {
    String productJson = TestUtils.readFile(JSON_FILE);
    Product price = this.json.parseObject(productJson);  
    assertThat(price.toString()).isNotNull();    
  }  
  
  
}
