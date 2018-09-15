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
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@JsonTest
public class ProductSetResponseTest extends AbstractTestNGSpringContextTests {

    private static final String JSON_FILE = "com/macys/selection/xapi/list/client/response/fcc/fcc_product_set_response.json";

    @Autowired
    private JacksonTester<ProductSetResponse> json;

    private ProductSetResponse productSetResponse;

    @Test
    public void productResponseDeserializeTest() throws IOException {
        String productJson = TestUtils.readFile(JSON_FILE);
        assertThat(this.json.parse(productJson)).isEqualToComparingFieldByField(this.productSetResponse);
    }

    @BeforeMethod
    public void setup() {

        this.productSetResponse = new ProductSetResponse();

        ProductResponse productResponse1 = new ProductResponse();
        productResponse1.setId(456);
        productResponse1.setName("Steel Bracelet Watch");
        productResponse1.setDefaultCategoryId(23);
        productResponse1.setActive(true);
        productResponse1.setPrimaryPortraitSource("test_source");
        productResponse1.setAdditionalImageSource(Collections.singletonList("testImage"));
        productResponse1.setLive(true);
        productResponse1.setAvailable(true);

        ProductResponse productResponse2 = new ProductResponse();
        productResponse2.setId(111);
        productResponse2.setName("test_name");
        productResponse2.setDefaultCategoryId(11);
        productResponse2.setActive(false);
        productResponse2.setPrimaryPortraitSource("test_source2");
        productResponse2.setAdditionalImageSource(Collections.singletonList("testImage2"));
        productResponse2.setLive(false);
        productResponse2.setAvailable(false);

        this.productSetResponse.setProduct(Arrays.asList(productResponse1, productResponse2));

    }
}
