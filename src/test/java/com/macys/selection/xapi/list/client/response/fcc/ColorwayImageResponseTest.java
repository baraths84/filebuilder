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
public class ColorwayImageResponseTest extends AbstractTestNGSpringContextTests {

    private static final String JSON_FILE =
            "com/macys/selection/xapi/list/client/response/fcc/fcc_colorway_image_response.json";

    @Autowired
    private JacksonTester<ColorwayImageResponse> json;

    private ColorwayImageResponse colorwayImage;

    @Test
    public void colorwayImageDeserializeTest() throws IOException {
        String imageJson = TestUtils.readFile(JSON_FILE);
        assertThat(this.json.parse(imageJson)).isEqualTo(colorwayImage);
        assertThat(this.json.parse(imageJson)).isEqualToComparingFieldByField(colorwayImage);
    }

    @BeforeMethod
    public void setup() {
        this.colorwayImage = new ColorwayImageResponse();
        colorwayImage.setImageName("111222333.fpx");
        colorwayImage.setSeqNumber(1);
    }
}
