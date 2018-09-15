package com.macys.selection.xapi.list.data.converters;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import java.io.IOException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest
public class JsonResponseParserCustomerListsTest extends AbstractTestNGSpringContextTests {

  @Test
  public void testParserReadingNullValue() {
    JsonResponseParserCustomerLists jrcp = new JsonResponseParserCustomerLists();
    assertNull(jrcp.readValue(null));
  }
  
  @Test
  public void testParserReadingValidListResponse() throws IOException {
    JsonResponseParserCustomerLists jrcp = new JsonResponseParserCustomerLists();

    String customerList = TestUtils.readFile("com/macys/selection/xapi/wishlist/converters/wishlist_multiple_item.json");
    JsonNode actualArrayNode = jrcp.readValue(jrcp.parse(customerList));
    assertNotNull(actualArrayNode);    
    
    JsonNode actualNode = actualArrayNode.get(0);
    assertEquals(actualNode.get("listGuid").asText(), "71adefa2-183a-4a9e-aa26-a3b96c11db24");
    assertEquals(actualNode.get("name").asText(), "Guest List");
    assertEquals(actualNode.get("listType").asText(), "W");
    assertEquals(actualNode.get("defaultList").asBoolean(), true);
    assertEquals(actualNode.get("onSaleNotify").asBoolean(), false);
    assertEquals(actualNode.get("searchable").asBoolean(), false);
    assertEquals(actualNode.get("numberOfItems").asInt(), 1);
    assertEquals(actualNode.get("showPurchaseInfo").asBoolean(), true);
    assertEquals(actualNode.get("createdDate").asText(), "2017-08-07 01:13:24.399165");
    assertEquals(actualNode.get("lastModified").asText(), "2017-08-07 01:13:24.399165");
  }

}
