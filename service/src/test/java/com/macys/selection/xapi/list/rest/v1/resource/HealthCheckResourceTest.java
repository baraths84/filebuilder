package com.macys.selection.xapi.list.rest.v1.resource;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.macys.selection.xapi.list.health.HealthCheckResource;

@SpringBootTest
public class HealthCheckResourceTest extends AbstractTestNGSpringContextTests {
  
  @InjectMocks
  private HealthCheckResource healthResourceResource = new HealthCheckResource();

  @BeforeMethod
  public void init() {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void testHello() {
  	String response = healthResourceResource.hello();
    assertNotNull(response);
    assertTrue(response.equals("Health check passed!"));
  }

}
