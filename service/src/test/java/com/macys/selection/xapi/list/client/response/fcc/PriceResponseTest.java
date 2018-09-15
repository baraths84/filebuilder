package com.macys.selection.xapi.list.client.response.fcc;

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
public class PriceResponseTest  extends AbstractTestNGSpringContextTests {

    private static final String JSON_FILE = "com/macys/selection/xapi/list/client/response/fcc/fcc_price_response.json";

    @Autowired
    private JacksonTester<PriceResponse> json;

    private PriceResponse priceResponse;

    @Test
    public void fobResponseDeserializeTest() throws IOException {
        String priceJson = TestUtils.readFile(JSON_FILE);
        assertThat(this.json.parse(priceJson)).isEqualToComparingFieldByField(priceResponse);
    }

    @BeforeMethod
    public void setup() {
        this.priceResponse = new PriceResponse();
        this.priceResponse.setOriginalPrice(78.);
        this.priceResponse.setRetailPrice(78.);
        this.priceResponse.setPriceType(0);
        this.priceResponse.setSaleValue(10.);
        this.priceResponse.setIntermediateSaleValue(11.);
        this.priceResponse.setDisplayCode("dcode");
        this.priceResponse.setBasePriceType(1);
        this.priceResponse.setOnSale(true);
    }

}
