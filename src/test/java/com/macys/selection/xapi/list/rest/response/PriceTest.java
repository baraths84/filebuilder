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
public class PriceTest extends AbstractTestNGSpringContextTests {

  @Autowired private JacksonTester <Price> json; 
  
  private static final Boolean ON_SALE = Boolean.FALSE;
  private static final Boolean UPC_ON_SALE = Boolean.FALSE;
  private static final Integer BASE_PRICE_TYPE = 0;
  private static final Double  INTERMEDIATE_SALE_VALUE = 0.0;
  private static final Double  ORIGINAL_PRICE = 39.0;
  private static final Integer PRICE_TYPE = 11;
  private static final String  PRICE_TYPE_TEXT = "test";
  private static final Double  RETAIL_PRICE = 39.0;
  private static final Double  RETAIL_PRICE_HIGH = 0.0;
  private static final Double  SALES_VALUE = 0.0;
  private static final String  ORIGINAL_PRICE_LABEL = "test";
  private static final String  RETAIL_PRICE_LABEL = "test";
  private static final String  PRICING_POLICY_TEXT = "test";
  private static final String  PRICING_POLICY = "test";

  private Price price;
  
  @BeforeMethod
  public void setup() {
    price = new Price();
    price.setBasePriceType(BASE_PRICE_TYPE);
    price.setIntermediateSalesValue(INTERMEDIATE_SALE_VALUE);
    price.setOnSale(ON_SALE);
    price.setOriginalPrice(ORIGINAL_PRICE);
    price.setPriceType(PRICE_TYPE);
    price.setPriceTypeText(PRICE_TYPE_TEXT);
    price.setRetailPrice(RETAIL_PRICE);
    price.setRetailPriceHigh(RETAIL_PRICE_HIGH);
    price.setSalesValue(SALES_VALUE);
    price.setUpcOnSale(UPC_ON_SALE);
    price.setOriginalPriceLabel(ORIGINAL_PRICE_LABEL);
    price.setRetailPriceLabel(RETAIL_PRICE_LABEL);
    price.setPricingPolicyText(PRICING_POLICY_TEXT);
    price.setPricingPolicy(PRICING_POLICY);
  }

  @Test
  public void priceSerializeTest() throws IOException {
    assertThat(this.json.write(price)).isEqualToJson("price.json");
  }
  
  @Test
  public void priceDeserializeTest() throws IOException {
    String priceJson = TestUtils.readFile("com/macys/selection/xapi/list/rest/response/price.json");
    assertThat(this.json.parse(priceJson)).isEqualTo(price);
  }
  
  @Test
  public void priceEquaslsTest() {
    assertThat(price.equals(null)).isFalse();
    assertThat(price.equals(price)).isTrue();    
  }
  
  @Test
  public void priceHashCodeTest() throws IOException {
    String priceJson = TestUtils.readFile("com/macys/selection/xapi/list/rest/response/price.json");
    Price price = this.json.parseObject(priceJson);  
    assertThat(price.hashCode()).isNotNull();    
  }

  @Test
  public void priceToStringTest() throws IOException {
    String priceJson = TestUtils.readFile("com/macys/selection/xapi/list/rest/response/price.json");
    Price price = this.json.parseObject(priceJson);  
    assertThat(price.toString()).isNotNull();    
  }  

  
}
