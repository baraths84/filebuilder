package com.macys.selection.xapi.list.client.response.fcc;

import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@JsonTest
public class AttributeResponseTest extends AbstractTestNGSpringContextTests {

    private static final String JSON_FILE =
            "com/macys/selection/xapi/list/client/response/fcc/fcc_attribute_response.json";

    @Autowired
    private JacksonTester<AttributeResponse> json;

    private JsonToObjectConverter<AttributeResponse> converter =
            new JsonToObjectConverter<>(AttributeResponse.class);

    private AttributeResponse attribute;

    @Test
    public void colorwayImageDeserializeTest() throws IOException {
        String attributeJson = TestUtils.readFile(JSON_FILE);
        assertThat(this.json.parse(attributeJson)).isEqualToComparingFieldByField(attribute);
    }


    @BeforeMethod
    public void setup() {
        this.attribute = new AttributeResponse();
        this.attribute.setName("PROMOTION_ID");
        this.attribute.setVisible(true);
        this.attribute.setUnary(true);

        AttributeValueResponse value1 = new AttributeValueResponse();
        value1.setValue("value1");
        AttributeValueResponse value2 = new AttributeValueResponse();
        value2.setValue("value2");
        this.attribute.setAttributeValues(Arrays.asList(value1, value2));
    }
}
