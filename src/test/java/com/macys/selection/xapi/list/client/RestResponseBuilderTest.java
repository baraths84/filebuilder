package com.macys.selection.xapi.list.client;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import com.macys.selection.xapi.list.exception.RestException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SpringBootTest
public class RestResponseBuilderTest extends AbstractTestNGSpringContextTests {
    
  @Mock private Response response;
  
  private static final String LOCALHOST_REDIRECT_URL = "http://localhost:8080/redirect";
  private static final String ERROR = "error";
  private static final String ERROR_MESSAGE = "error parsing response";
  private static final Integer ONE = 1;

  @BeforeMethod
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expectedExceptions = RestException.class)
  public void testRestResponseBuilderThrowsException() {
    when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
    when(response.readEntity(String.class)).thenThrow(new ProcessingException(ERROR_MESSAGE));

    RestResponse actualRestResponse = RestResponseBuilder.buildFromResponse(response);
  }
  
  @Test
  public void testHttpValidStatusReturnsValidRestResponse() {
    String expectedBody = "test";
    int expectedStatusCode = Response.Status.OK.getStatusCode();
    
    when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
    when(response.readEntity(String.class)).thenReturn(expectedBody);    
    
    RestResponse actualRestResponse = RestResponseBuilder.buildFromResponse(response);
    
    assertEquals(expectedStatusCode, actualRestResponse.getStatusCode());
    assertEquals(expectedBody, actualRestResponse.getBody());
  }
  
  @Test
  public void testHttpErrorStatusCodeReturnsAValidRestResponse() {
    String expectedBody = ERROR;
    int expectedStatusCode = Response.Status.BAD_REQUEST.getStatusCode();
    
    when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
    when(response.readEntity(String.class)).thenReturn(ERROR);    
    
    RestResponse actualRestResponse = RestResponseBuilder.buildFromResponse(response);
    
    assertEquals(expectedStatusCode, actualRestResponse.getStatusCode());
    assertEquals(expectedBody, actualRestResponse.getBody());
  }
  
  @Test
  public void testHttp301StatusCodeRedirectsAndReturnsValidRestResponse() throws URISyntaxException {
    String expectedBody = ERROR;
    String expectedLocation = LOCALHOST_REDIRECT_URL;
    int expectedStatusCode = Response.Status.MOVED_PERMANENTLY.getStatusCode();
    
    when(response.getStatus()).thenReturn(Response.Status.MOVED_PERMANENTLY.getStatusCode());
    when(response.readEntity(String.class)).thenReturn(ERROR);   
    when(response.getLocation()).thenReturn(new URI(LOCALHOST_REDIRECT_URL));
    
    RestResponse actualRestResponse = RestResponseBuilder.buildFromResponse(response);
    
    assertEquals(expectedStatusCode, actualRestResponse.getStatusCode());
    assertEquals(expectedBody, actualRestResponse.getBody());
    assertEquals(expectedLocation, actualRestResponse.getLocation());    
  }
  
  @Test
  public void testHttpErrorStatusCodeDoesNotRedirect() throws URISyntaxException {
    String expectedBody = "valid response";    
    int expectedStatusCode = Response.Status.BAD_REQUEST.getStatusCode();
    
    when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
    when(response.readEntity(String.class)).thenReturn(expectedBody);   
    when(response.getLocation()).thenReturn(new URI(LOCALHOST_REDIRECT_URL));
    
    RestResponse actualRestResponse = RestResponseBuilder.buildFromResponse(response);
    
    assertEquals(expectedStatusCode, actualRestResponse.getStatusCode());
    assertEquals(expectedBody, actualRestResponse.getBody());
    assertNull(actualRestResponse.getLocation());    
  }
  

}
