package com.macys.selection.xapi.list.rest.response;

import static org.assertj.core.api.Assertions.*;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.macys.selection.xapi.list.data.converters.TestUtils;

@SpringBootTest(classes = {Availability.class})
@JsonTest
public class AvailabilityTest extends AbstractTestNGSpringContextTests {
  
  @Autowired private JacksonTester <Availability> json; 

  private static final Boolean AVAILABLE = Boolean.TRUE;
  private static final String UPC_AVAILABILITY_MESSAGE = "In Stock: Usually ships within 2 business days.";
  private static final Boolean IN_STORE_ELIGIBLE = Boolean.TRUE;
  private static final String ORDER_METHOD = "POOL";
  private static final String AVAILABILITY_JSON_FILE = "com/macys/selection/xapi/list/rest/response/availability.json";
  
  private Availability availability;
  
  @BeforeMethod
  public void setup() {
    availability = new Availability();
    availability.setAvailable(AVAILABLE);
    availability.setUpcAvailabilityMessage(UPC_AVAILABILITY_MESSAGE);
    availability.setInStoreEligible(IN_STORE_ELIGIBLE);
    availability.setOrderMethod(ORDER_METHOD);
  }
  
  @Test
  public void availableSerializeTest() throws IOException {
    assertThat(this.json.write(availability)).extractingJsonPathBooleanValue("@.available").isEqualTo(true);
    assertThat(this.json.write(availability)).extractingJsonPathBooleanValue("@.inStoreEligible").isEqualTo(true);
    assertThat(this.json.write(availability)).extractingJsonPathStringValue("@.upcAvailabilityMessage").isEqualTo(UPC_AVAILABILITY_MESSAGE);
    assertThat(this.json.write(availability)).extractingJsonPathStringValue("@.orderMethod").isEqualTo(ORDER_METHOD);    
  }
  
  @Test
  public void availableDeserializeTest() throws IOException {
    String availabilityJson = TestUtils.readFile(AVAILABILITY_JSON_FILE);
    assertThat(this.json.parse(availabilityJson)).isEqualTo(availability);
    assertThat(this.json.parseObject(availabilityJson).getOrderMethod()).isEqualTo("POOL");
  }
  
  @Test
  public void availabilityEquaslsTest() {
    assertThat(availability.equals(null)).isFalse();
    assertThat(availability.equals(availability)).isTrue();    
  }
  
  @Test
  public void availabilityHashCodeTest() throws IOException {
    String availabilityJson = TestUtils.readFile(AVAILABILITY_JSON_FILE);
    Availability testAvailability = this.json.parseObject(availabilityJson);  
    assertThat(testAvailability.hashCode()).isNotNull();    
  }

  @Test
  public void availabilityToStringTest() throws IOException {
    String availabilityJson = TestUtils.readFile(AVAILABILITY_JSON_FILE);
    Availability testAvailability = this.json.parseObject(availabilityJson);  
    assertThat(testAvailability.toString()).isNotNull();    
  }  
  
  
}
