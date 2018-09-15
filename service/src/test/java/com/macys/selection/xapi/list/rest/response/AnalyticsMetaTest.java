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
public class AnalyticsMetaTest extends AbstractTestNGSpringContextTests {

    private static final String JSON_FILE = "com/macys/selection/xapi/list/rest/response/analyticsMeta.json";

    @Autowired
    private JacksonTester<AnalyticsMeta> json;

    private AnalyticsMeta meta;

    @BeforeMethod
    public void setup() {

        Analytics analytics = new Analytics();
        DigitalAnalytics digitalAnalytics = new DigitalAnalytics();

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

        meta = new AnalyticsMeta();
        analytics.setDigitalAnalytics(digitalAnalytics);
        meta.setAnalytics(analytics);
    }

    @Test
    public void analyticsMetaSerializeTest() throws IOException {
        assertThat(this.json.write(meta)).isEqualToJson("analyticsMeta.json");
    }

    @Test
    public void analyticsMetaDeserializeTest() throws IOException {
        String analyticsJson = TestUtils.readFile(JSON_FILE);
        assertThat(this.json.parse(analyticsJson)).isEqualTo(meta);
    }

    @Test
    public void analyticsMetaEquaslsTest() {
        assertThat(meta.equals(null)).isFalse();
        assertThat(meta.equals(meta)).isTrue();
    }

    @Test
    public void analyticsMetaHashCodeTest() throws IOException {
        String analyticsMetaJson = TestUtils.readFile(JSON_FILE);
        AnalyticsMeta meta = this.json.parseObject(analyticsMetaJson);
        assertThat(meta.hashCode()).isNotNull();
    }

    @Test
    public void analyticsMetaToStringTest() throws IOException {
        String analyticsMetaJson = TestUtils.readFile(JSON_FILE);
        AnalyticsMeta meta = this.json.parseObject(analyticsMetaJson);
        assertThat(meta.toString()).isNotNull();
    }

}
