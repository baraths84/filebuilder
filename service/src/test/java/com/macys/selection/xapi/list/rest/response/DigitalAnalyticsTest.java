package com.macys.selection.xapi.list.rest.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;

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
public class DigitalAnalyticsTest extends AbstractTestNGSpringContextTests {
    private static final String JSON_FILE = "com/macys/selection/xapi/list/rest/response/digitalAnalytics.json";

    @Autowired
    private JacksonTester<DigitalAnalytics> json;

    private DigitalAnalytics digitalAnalytics;

    @BeforeMethod
    public void setup() {
        digitalAnalytics = new DigitalAnalytics();

        ArrayList<String> prodIdList = new ArrayList<String>();
        prodIdList.add("813310");
        ArrayList<String> prodNameList = new ArrayList<String>();
        prodNameList.add("Clinique Moisture Surge CC Cream Colour Correcting Skin Protector Broad Spectrum SPF 30, 1.4 oz");
        ArrayList<String> prodPriceList = new ArrayList<String>();
        prodPriceList.add("39.0");
        ArrayList<String> prodPriceStateList = new ArrayList<String>();
        prodPriceStateList.add("test");
        ArrayList<String> prodQuantityList = new ArrayList<String>();
        prodQuantityList.add("1");
        ArrayList<String> prodUpcList = new ArrayList<String>();
        prodUpcList.add("20714656027");

        digitalAnalytics.setProductId(prodIdList);
        digitalAnalytics.setProductName(prodNameList);
        digitalAnalytics.setProductPrice(prodPriceList);
        digitalAnalytics.setProductPricingState(prodPriceStateList);
        digitalAnalytics.setProductQuantity(prodQuantityList);
        digitalAnalytics.setProductUPC(prodUpcList);
        digitalAnalytics.setWishListId("8396726c-68d0-4a1c-b123-ab7c4599bdb9");
    }

    @Test
    public void digitalAnalyticsSerializeTest() throws IOException {
        assertThat(this.json.write(digitalAnalytics)).isEqualToJson("digitalAnalytics.json");
    }

    @Test
    public void digitalAnalyticsDeserializeTest() throws IOException {
        String digitalAnalyticsJson = TestUtils.readFile(JSON_FILE);
        assertThat(this.json.parse(digitalAnalyticsJson)).isEqualTo(digitalAnalytics);
    }

    @Test
    public void digitalAnalyticsEquaslsTest() {
        assertThat(digitalAnalytics.equals(null)).isFalse();
        assertThat(digitalAnalytics.equals(digitalAnalytics)).isTrue();
    }

    @Test
    public void digitalAnalyticsHashCodeTest() throws IOException {
        String digitalAnalyticsJson = TestUtils.readFile(JSON_FILE);
        DigitalAnalytics digitalAnalytics = this.json.parseObject(digitalAnalyticsJson);
        assertThat(digitalAnalytics.hashCode()).isNotNull();
    }

    @Test
    public void digitalAnalyticsToStringTest() throws IOException {
        String digitalAnalyticsJson = TestUtils.readFile(JSON_FILE);
        DigitalAnalytics digitalAnalytics = this.json.parseObject(digitalAnalyticsJson);
        assertThat(digitalAnalytics.toString()).isNotNull();
    }
}
