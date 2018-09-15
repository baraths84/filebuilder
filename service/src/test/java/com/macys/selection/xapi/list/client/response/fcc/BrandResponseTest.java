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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@JsonTest
public class BrandResponseTest extends AbstractTestNGSpringContextTests {

    private static final String JSON_FILE =
            "com/macys/selection/xapi/list/client/response/fcc/fcc_brand_response.json";

    @Autowired
    private JacksonTester<BrandResponse> json;

    private BrandResponse brand;

    @Test
    public void brandDeserializeTest() throws IOException {
        String brandJson = TestUtils.readFile(JSON_FILE);
        assertThat(this.json.parse(brandJson)).isEqualToComparingFieldByField(brand);
    }

    @BeforeMethod
    public void setup() {
        this.brand = new BrandResponse();
        this.brand.setOzBrandId(336);
        this.brand.setBrandName("testBrand");
    }
}
