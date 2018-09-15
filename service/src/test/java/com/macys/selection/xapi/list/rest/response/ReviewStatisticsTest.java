package com.macys.selection.xapi.list.rest.response;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.text.ParseException;
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
public class ReviewStatisticsTest extends AbstractTestNGSpringContextTests {

	@Autowired private JacksonTester <ReviewStatistics> json; 

	private ReviewStatistics reviewStatistics;
	private static final int TEST_REVIEW_COUNT = 274;
	private static final String JSON_FILE = "com/macys/selection/xapi/list/rest/response/review_statistics.json";

	@BeforeMethod
	public void setup() {
		reviewStatistics = new ReviewStatistics();
		reviewStatistics.setAverageRating(Float.valueOf("4.6095"));
		reviewStatistics.setReviewCount(TEST_REVIEW_COUNT);
	}

	@Test
	public void reviewStatisticsSerializeTest() throws ParseException, IOException {
		assertThat(this.json.write(reviewStatistics)).isEqualToJson("review_statistics.json");
	}  

	@Test
	public void reviewStatisticsDeserializeTest() throws ParseException, IOException {
		String reviewStatisticsJson = TestUtils.readFile(JSON_FILE);
		assertThat(this.json.parse(reviewStatisticsJson)).isEqualTo(reviewStatistics);
		assertThat(this.json.parse(reviewStatisticsJson)).isEqualToComparingFieldByField(reviewStatistics);
	}

	@Test
	public void reviewStatisticsEquaslsTest() {
		assertThat(reviewStatistics.equals(null)).isFalse();
		assertThat(reviewStatistics.equals(reviewStatistics)).isTrue();    
	}

	@Test
	public void productHashCodeTest() throws IOException {
		String reviewStatisticsJson = TestUtils.readFile(JSON_FILE);
		ReviewStatistics reviewStatistics = this.json.parseObject(reviewStatisticsJson);  
		assertThat(reviewStatistics.hashCode()).isNotNull();    
	}

	@Test
	public void productToStringTest() throws IOException {
		String reviewStatisticsJson = TestUtils.readFile(JSON_FILE);
		ReviewStatistics reviewStatistics = this.json.parseObject(reviewStatisticsJson);  
		assertThat(reviewStatistics.toString()).isNotNull();    
	}  



}
