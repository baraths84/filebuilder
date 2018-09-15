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
public class ReviewStatisticsResponseTest extends AbstractTestNGSpringContextTests {

    private static final String JSON_FILE =
            "com/macys/selection/xapi/list/client/response/fcc/fcc_review_statistics_response.json";

    @Autowired
    private JacksonTester<ReviewStatisticsResponse> json;

    private ReviewStatisticsResponse statistics;

    @Test
    public void statisticsResponseDeserializeTest() throws IOException {
        String statisticsJson = TestUtils.readFile(JSON_FILE);
        assertThat(this.json.parse(statisticsJson)).isEqualToComparingFieldByField(statistics);
    }

    @BeforeMethod
    public void setup() {
        this.statistics = new ReviewStatisticsResponse();
        this.statistics.setAverageRating(4.2);
        this.statistics.setReviewCount(5);
    }
}
