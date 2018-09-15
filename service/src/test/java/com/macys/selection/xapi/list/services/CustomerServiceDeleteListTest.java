package com.macys.selection.xapi.list.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.macys.selection.xapi.list.client.CustomerServiceRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.User;
import javax.ws.rs.core.Response;

/**
 *   
 **/
@SpringBootTest
public class CustomerServiceDeleteListTest extends AbstractTestNGSpringContextTests {

  private static final String LIST_GUID_ID_TO_BE_DELETED = "8db568b8-8d17-48b4-8bbb-23d655146fc9";
  private static final Long USER_ID = 12345L;
  private static final String EMPTY_STRING = "";
  private static final Integer ONE = 1;
  private static final String SECURITY_TOKEN = "xxx-token";
  private static final String LIST_GUID = "xxx-guid";
  private static final Long USER_ID_COOKIES = 10L;

  @Mock
  private CustomerServiceRestClient customerRestClient;

  @InjectMocks
  private CustomerService customerService;
  
  @BeforeMethod
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void deleteListTest() {  
    RestResponse restResponse = new RestResponse();
    restResponse.setBody(EMPTY_STRING);
    restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());
    
    CustomerList customerList = new CustomerList();
    User user = new User();
    user.setId(USER_ID);
    customerList.setUser(user);

    ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
    when(customerRestClient.deleteList(any(), any(), any())).thenReturn(restResponse);
    customerService.deleteList(cookie, LIST_GUID_ID_TO_BE_DELETED, USER_ID);
    verify(customerRestClient, times(ONE)).deleteList(any(), any(), any());
  }
  
  @Test(expectedExceptions = ListServiceException.class)
  public void serviceUnableToDeleteTheListGuidAndThrowsListServiceException() {
    RestResponse restResponse = new RestResponse();
    restResponse.setBody(EMPTY_STRING);
    restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
    
    CustomerList customerList = new CustomerList();
    User user = new User();
    user.setId(USER_ID);
    customerList.setUser(user);

    ListCookies cookie = new ListCookies(USER_ID_COOKIES, LIST_GUID, SECURITY_TOKEN);
    when(customerRestClient.deleteList(any(), any(), any())).thenReturn(restResponse);
    customerService.deleteList(cookie, LIST_GUID_ID_TO_BE_DELETED, USER_ID);
    verify(customerRestClient, times(ONE)).deleteList(any(), any(), any());
  }
  
}
