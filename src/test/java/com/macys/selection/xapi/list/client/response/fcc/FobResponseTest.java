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
public class FobResponseTest extends AbstractTestNGSpringContextTests {

    private static final String JSON_FILE =
            "com/macys/selection/xapi/list/client/response/fcc/fcc_fob_response.json";

    @Autowired
    private JacksonTester<FobResponse> json;

    private FobResponse fob;

    @Test
    public void fobResponseDeserializeTest() throws IOException {
        String fobJson = TestUtils.readFile(JSON_FILE);
        assertThat(this.json.parse(fobJson)).isEqualToComparingFieldByField(fob);
    }

    @BeforeMethod
    public void setup() {
        this.fob = new FobResponse();
        this.fob.setId(112);
        this.fob.setName("Cosmetics & Fragrances");
    }

}
