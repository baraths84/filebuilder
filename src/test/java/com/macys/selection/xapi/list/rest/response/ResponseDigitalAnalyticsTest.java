package com.macys.selection.xapi.list.rest.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class ResponseDigitalAnalyticsTest extends AbstractTestNGSpringContextTests {
    private static final String JSON_RESPONSE_FILE = "com/macys/selection/xapi/list/rest/response/responseDigitalAnalytics.json";

    @Autowired
    private JacksonTester<ResponseDigitalAnalytics> json;

    private ResponseDigitalAnalytics responseDigitalAnalytics;

    @BeforeMethod
    public void setup() {
        responseDigitalAnalytics = new ResponseDigitalAnalytics();

        List<String> prodIdList = new ArrayList<String>();
        prodIdList.add("813310");
        List<String> prodNameList = new ArrayList<String>();
        prodNameList.add("Clinique Moisture Surge CC Cream Colour Correcting Skin Protector Broad Spectrum SPF 30, 1.4 oz");
        List <String> prodPriceList = new ArrayList<String>();
        prodPriceList.add("39.00");
        List <String> prodOriginalPriceList = new ArrayList<String>();
        prodOriginalPriceList.add("39.00");
        List <String> productSize = new ArrayList<String>();
        productSize.add("XL");
        List <String> productColor = new ArrayList<String>();
        productColor.add("Vanilla");
        List <String> prodUpcList = new ArrayList<String>();
        prodUpcList.add("20714656027");

        responseDigitalAnalytics.setEventName("wishlist_add");
        responseDigitalAnalytics.setProductId(prodIdList);
        responseDigitalAnalytics.setProductName(prodNameList);
        responseDigitalAnalytics.setProductPrice(prodPriceList);
        responseDigitalAnalytics.setProductOriginalPrice(prodOriginalPriceList);
        responseDigitalAnalytics.setProductSize(productSize);
        responseDigitalAnalytics.setProductColor(productColor);
        responseDigitalAnalytics.setProductUPC(prodUpcList);
    }

    @Test
    public void digitalAnalyticsSerializeTest() throws IOException {
        assertThat(this.json.write(responseDigitalAnalytics)).isEqualToJson("responseDigitalAnalytics.json");
    }

    @Test
    public void digitalAnalyticsDeserializeTest() throws IOException {
        String responseDigitalAnalyticsJson = TestUtils.readFile(JSON_RESPONSE_FILE);
        assertThat(this.json.parse(responseDigitalAnalyticsJson)).isEqualTo(responseDigitalAnalytics);
    }

    @Test
    public void digitalAnalyticsEquaslsTest() {
        assertThat(responseDigitalAnalytics.equals(null)).isFalse();
        assertThat(responseDigitalAnalytics.equals(responseDigitalAnalytics)).isTrue();
    }

    @Test
    public void digitalAnalyticsHashCodeTest() throws IOException {
        String digitalAnalyticsJson = TestUtils.readFile(JSON_RESPONSE_FILE);
        ResponseDigitalAnalytics responseDigitalAnalytics = this.json.parseObject(digitalAnalyticsJson);
        assertThat(responseDigitalAnalytics.hashCode()).isNotNull();
    }

    @Test
    public void digitalAnalyticsToStringTest() throws IOException {
        String digitalAnalyticsJson = TestUtils.readFile(JSON_RESPONSE_FILE);
        ResponseDigitalAnalytics responseDigitalAnalytics = this.json.parseObject(digitalAnalyticsJson);
        assertThat(responseDigitalAnalytics.toString()).isNotNull();
    }
}
