package com.macys.selection.xapi.list.client;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxClient;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvoker;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.platform.rest.framework.client.exception.RestClientException;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.util.CustomerRequestParamUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class CustomerServiceRestClientDeleteListWithErrorStatusCodeTest extends AbstractTestNGSpringContextTests {

  private static final String LIST_GUID = "ee15b1e1-2a85-46ef-bb82-236b4068870";
  private static final String BASE_PATH = "http://localhost:8080";
  private static final String GET_LIST_URL = "/xapi/v1/lists/";

  @Mock
  private RestClientFactory.JaxRSClientPool listClientPool;
  
  @Mock
  private RxClient rxClient;
  
  @Mock
  private RxWebTarget rxWebTarget;
  
  @Mock
  private RxInvoker invoker;
  
  @Mock
  private CustomerRequestParamUtil requestParamUtil;
  
  @InjectMocks
  private CustomerServiceRestClient restClient;

  private RxInvocationBuilder invocationBuilder;
  
  private static final long TEST_LONG = 12345L;

  @BeforeMethod
  public void init() throws RestClientException {
    MockitoAnnotations.initMocks(this);
    invocationBuilder = Mockito.mock(RxInvocationBuilder.class);
    
    when(listClientPool.getHostName()).thenReturn(BASE_PATH);
    when(listClientPool.getBasePath()).thenReturn(GET_LIST_URL);
    when(listClientPool.getRxClient((String) any())).thenReturn(rxClient);
    when(rxClient.target((String) any())).thenReturn(rxWebTarget);
    when(rxWebTarget.path((String) any())).thenReturn(rxWebTarget);
    when(rxWebTarget.path((String) any())).thenReturn(rxWebTarget);
    when(rxWebTarget.resolveTemplate(any(), any())).thenReturn(rxWebTarget);
  }

  @Test(expectedExceptions = RestException.class)
  public void deleteCustomerListExpectRestExceptionTest() {
    Response response = Mockito.mock(Response.class);
    when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
    when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
    when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
    when(rxWebTarget.request()).thenReturn(invocationBuilder);
    when(invocationBuilder.header(any(), any())).thenReturn(invocationBuilder);
    when(invocationBuilder.delete()).thenReturn(response);
    when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());

    ListCookies cookie = new ListCookies(10L, "xxx-guid", "xxx-token");
    RestResponse restResponse = restClient.deleteList(cookie, LIST_GUID, TEST_LONG);
    assertThat(restResponse.getStatusCode()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
  }
  
  @Test(expectedExceptions = RestException.class)
  public void deleteCustomerListPassingNullValues() {
    Response response = Mockito.mock(Response.class);
    when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
    when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
    when(rxWebTarget.queryParam(any(), any())).thenReturn(rxWebTarget);
    when(rxWebTarget.request()).thenReturn(invocationBuilder);
    when(invocationBuilder.delete()).thenReturn(response);
    when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());
    
    restClient.deleteList(null,null, null);
  }
  
  
}
