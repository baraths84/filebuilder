package com.macys.selection.xapi.list.client.request.converter;

import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.text.ParseException;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.data.converters.TestUtils;

@SpringBootTest(classes = {EmailShare.class})
@JsonTest
public class EmailShareTest extends AbstractTestNGSpringContextTests {
	  private EmailShare emailShare;
	  private ObjectMapper objectMapper;
	  
	  private static String TEST_EMAIL_JSON_FILE = "com/macys/selection/xapi/list/client/request/converter/email_share.json";
	  private static String TEST_FROM = "test@test.com";
	  private static String TEST_TO = "qa15@test.com,qa16@test.com";
	  private static String TEST_LINK = "http://www.qa16codemacys.fds.com/wishlist/guest?wid=11_yRjKJPusoRy74ha4nYulEgIA5WB6juHWQ1iWI5aySPI=&cm_mmc=wishlist-_-share-_-sil-_-n";
	  private static String TEST_MESSAGE = "Test Message";
	  private static String TEST_FIRST_NAME = "first name";
	  private static String TEST_LAST_NAME = "last name";
	  
	  
	  @BeforeMethod
	  public void setup() throws ParseException {
		  emailShare = expectedEmailShare(); 
		  objectMapper = new ObjectMapper();
		  objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
	  }
	  
	  @Test
	  public void emailShareDeserializeTest() throws ParseException, IOException {
	    String emailShareJson = TestUtils.readFile(TEST_EMAIL_JSON_FILE);
	    String testingJson = objectMapper.writeValueAsString(emailShare);
	    assertTrue(testingJson.equals(emailShareJson));
	  }
	  
	  @Test
	  public void emailshareSerializeTest() throws ParseException, IOException {
		objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
	    String emailShareJson = TestUtils.readFile(TEST_EMAIL_JSON_FILE);
	    EmailShare testingEmailShare = objectMapper.readValue(emailShareJson, EmailShare.class);
	    assertTrue(testingEmailShare != null);
	    assertTrue(testingEmailShare.getFrom().equals(TEST_FROM));
	    assertTrue(testingEmailShare.getTo().equals(TEST_TO));
	    assertTrue(testingEmailShare.getLink().equals(TEST_LINK));
	    assertTrue(testingEmailShare.getMessage().equals(TEST_MESSAGE));
	    assertTrue(testingEmailShare.getFirstName().equals(TEST_FIRST_NAME));
	    assertTrue(testingEmailShare.getLastName().equals(TEST_LAST_NAME));
	  }
	  
	  public static EmailShare expectedEmailShare() {
		  EmailShare emailShare = new EmailShare();
          emailShare.setFrom("test@test.com");
          emailShare.setTo("qa15@test.com,qa16@test.com");
          emailShare.setLink("http://www.qa16codemacys.fds.com/wishlist/guest?wid=11_yRjKJPusoRy74ha4nYulEgIA5WB6juHWQ1iWI5aySPI=&cm_mmc=wishlist-_-share-_-sil-_-n");
		  emailShare.setMessage("Test Message");
		  emailShare.setFirstName("first name");
		  emailShare.setLastName("last name");
		  return emailShare;
	   }
}
