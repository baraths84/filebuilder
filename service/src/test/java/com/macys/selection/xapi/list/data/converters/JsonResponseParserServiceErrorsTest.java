package com.macys.selection.xapi.list.data.converters;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.io.IOException;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest
public class JsonResponseParserServiceErrorsTest extends AbstractTestNGSpringContextTests {

	 @Test
	  public void testParserReadingNullValue() {
		 JsonResponseParserServiceErrors jrcp = new JsonResponseParserServiceErrors();
	    assertNull(jrcp.readValue(null));
	  }
	 
	 @Test
	  public void testParserReadingValidServiceErrorResponse() throws IOException {
		 JsonResponseParserServiceErrors jrcp = new JsonResponseParserServiceErrors();

	    String serviceErrors = TestUtils.readFile("com/macys/selection/xapi/wishlist/converters/getAllLists_Error.json");
	    JsonNode actualNode = jrcp.readValue(jrcp.parse(serviceErrors));
	    assertNotNull(actualNode);    
	    
	  }
	  
}
