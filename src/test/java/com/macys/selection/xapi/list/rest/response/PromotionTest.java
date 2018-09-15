package com.macys.selection.xapi.list.rest.response;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.macys.selection.xapi.list.data.converters.TestUtils;

@SpringBootTest
@JsonTest
public class PromotionTest extends AbstractTestNGSpringContextTests {
	
	@Autowired private JacksonTester <Promotion> json; 
	
	private static final Long PROMOTION_ID = 19876644L;
    private static final String BADGETEXT_ATTRIBUTE_VALUE = "EXTRA 25% OFF USE: FRESH";
    private static final String PROMOTION_JSON_FILE = "com/macys/selection/xapi/list/rest/response/promotion.json";

    private Promotion promotion;
    
    @BeforeMethod
    public void setup() {
    	promotion = new Promotion();
    	promotion.setPromotionId(PROMOTION_ID);
    	promotion.setBadgeTextAttributeValue(BADGETEXT_ATTRIBUTE_VALUE);
    }
    
    @Test
    public void promotionSerializeTest() throws IOException {
      assertThat(this.json.write(promotion)).extractingJsonPathNumberValue("@.promotionId").isEqualTo(PROMOTION_ID.intValue());
      assertThat(this.json.write(promotion)).extractingJsonPathStringValue("@.badgeTextAttributeValue").isEqualTo(BADGETEXT_ATTRIBUTE_VALUE);
    }
    
    @Test
    public void promotionDeserializeTest() throws IOException {
      String promotionJson = TestUtils.readFile(PROMOTION_JSON_FILE);
      assertThat(this.json.parse(promotionJson)).isEqualTo(promotion);
      assertThat(this.json.parseObject(promotionJson).getBadgeTextAttributeValue()).isEqualTo(BADGETEXT_ATTRIBUTE_VALUE);
    }
    
    @Test
    public void promotionHashCodeTest() throws IOException {
      String promotionJson = TestUtils.readFile(PROMOTION_JSON_FILE);
      Promotion promotion = this.json.parseObject(promotionJson);  
      assertThat(promotion.hashCode()).isNotNull();    
    }

    @Test
    public void promotionToStringTest() throws IOException {
      String promotionJson = TestUtils.readFile(PROMOTION_JSON_FILE);
      Promotion promotion = this.json.parseObject(promotionJson);  
      assertThat(promotion.toString()).isNotNull();    
    } 
    
}
