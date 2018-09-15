package com.macys.selection.xapi.list.exception;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserCustomerLists;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserServiceErrors;

@SpringBootTest
(classes = {JsonResponseParserCustomerLists.class,
		JsonResponseParserServiceErrors.class})
public class ListServiceExceptionMapperTest extends AbstractTestNGSpringContextTests {

	private static final String UTF_8 = "UTF-8";
	private static final int STATUS_400 = 400;
	private static final int STATUS_500 = 500;
	
	@InjectMocks
	private ListServiceExceptionMapper listServiceExceptionMapper;

	@Mock
	private JsonResponseParserCustomerLists customerLists;
	
	@Mock
	private JsonResponseParserServiceErrors serviceErrors;
	
	@Mock
	private ExceptionProperties exceptionMessages;
	
	@BeforeMethod
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test 
    public void testExceptionMapper() throws IOException {
    	//Case 1
    	String message = "{\"error\":[{\"errorCode\":\"10147\",\"message\":\"Maximum lists per user reached\"}]}";
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode parsedErrorNode = mapper.readValue(message.getBytes(UTF_8), JsonNode.class);
    	
    	String errorMessage = "You've reached the maximum number of Lists you can create. ";
		when(exceptionMessages.getMessage10147()).thenReturn(errorMessage);
    	when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
        when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);
        
        ListServiceException ex = new ListServiceException(STATUS_400, message);
        assertEquals(errorMessage, listServiceExceptionMapper.map(ex).getError().get(0).getMessage());
        
        //Case : 2
        String newMessage = "{\"error\":[{\"errorCode\":\"10148\",\"message\":\"Maximum Items per list reached\"}]}";
    	mapper = new ObjectMapper();
    	parsedErrorNode = mapper.readValue(newMessage.getBytes(UTF_8), JsonNode.class);
    	
    	errorMessage = "You've reached the maximum number of items you can add to this List. ";
		when(exceptionMessages.getMessage10148()).thenReturn(errorMessage);
    	when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
        when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);
        
        ListServiceException exeception = new ListServiceException(STATUS_400, message);
        assertEquals(errorMessage, listServiceExceptionMapper.map(exeception).getError().get(0).getMessage());
        
        //case 3
        String errorMessage1 = "{\"error\":[{\"errorCode\":\"10101\",\"message\":\"Invalid User ID\"}]}";
    	mapper = new ObjectMapper();
    	parsedErrorNode = mapper.readValue(errorMessage1.getBytes(UTF_8), JsonNode.class);
    	
    	errorMessage = "We're experiencing a technical glitch. Please try again later or call us at 1-800-BUY-MACY (289-6229) for immediate assistance. ";
		when(exceptionMessages.getServiceFailureMessage()).thenReturn(errorMessage);
    	when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
        when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);
        
        ListServiceException exception = new ListServiceException(STATUS_400, message);
        assertEquals(errorMessage, listServiceExceptionMapper.map(exception).getError().get(0).getMessage());

		String newMessage500 = "{\"error\":[{\"errorCode\":\"10134\",\"message\":\"Wishlist with same name already exists.\"}]}";
		mapper = new ObjectMapper();
		parsedErrorNode = mapper.readValue(newMessage500.getBytes(UTF_8), JsonNode.class);

		String error5001Message = "Wishlist with same name already exists.";
		when(exceptionMessages.getMessage50001()).thenReturn(error5001Message);
		when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
		when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);

		ListServiceException listException = new ListServiceException(STATUS_400, newMessage500);
		assertEquals(error5001Message, listServiceExceptionMapper.map(listException).getError().get(0).getMessage());

		String newMessage10156 = "{\"error\":[{\"errorCode\":\"10156\",\"message\":\"A list or a style board with same name already exists.\"}]}";
		mapper = new ObjectMapper();
		parsedErrorNode = mapper.readValue(newMessage10156.getBytes(UTF_8), JsonNode.class);

		String error10156Message = "A list or a style board with same name already exists.";
		when(exceptionMessages.getMessage10156()).thenReturn(error10156Message);
		when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
		when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);

		ListServiceException listException10156 = new ListServiceException(STATUS_400, newMessage10156);
		assertEquals(error10156Message, listServiceExceptionMapper.map(listException10156).getError().get(0).getMessage());
    }
	
	@Test
    public void testExceptionMapperWithOutErrorCode() throws IOException {
    	String message = "{\"errors\":[{\"errorCode\":\"10147\",\"message\":\"Maximum lists per user reached\"}]}";
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode parsedErrorNode = mapper.readValue(message.getBytes(UTF_8), JsonNode.class);
    	
    	String errorMessage = "We're experiencing a technical glitch. Please try again later or call us at 1-800-BUY-MACY (289-6229) for immediate assistance. ";
		when(exceptionMessages.getServiceFailureMessage()).thenReturn(errorMessage);
    	when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
        when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);
        
        ListServiceException exception = new ListServiceException(STATUS_400, message);
        assertEquals(errorMessage, listServiceExceptionMapper.map(exception).getError().get(0).getMessage());
    }
    
	@Test
	public void testExceptionMapperWithException(){
		String  errorMessage = "We had a temporary technical glitch. Please try again. ";
		when(exceptionMessages.getTemporaryFailureMessage()).thenReturn(errorMessage);

		ListServiceException ex = new ListServiceException( "list service exception");
		assertEquals(errorMessage, listServiceExceptionMapper.map(ex).getError().get(0).getMessage());
		assertEquals("50002", listServiceExceptionMapper.map(ex).getError().get(0).getErrorCode());
		assertEquals(STATUS_500,listServiceExceptionMapper.map(ex).getHttpStatus());
	}
	@Test
	public void testExceptionMapper_Bcom() throws IOException {
        ReflectionTestUtils.setField(listServiceExceptionMapper, "applicationName", "BCOM");
        ReflectionTestUtils.setField(listServiceExceptionMapper, "isBCOM", true);
		//Case 1
		String message = "{\"error\":[{\"errorCode\":\"10147\",\"message\":\"Maximum lists per user reached\"}]}";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode parsedErrorNode = mapper.readValue(message.getBytes(UTF_8), JsonNode.class);

		String errorMessage = "You've reached the maximum number of Lists you can create. ";
		when(exceptionMessages.getMessage10147()).thenReturn(errorMessage);
		when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
		when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);

		ListServiceException ex = new ListServiceException(STATUS_400, message);
		assertEquals(errorMessage, listServiceExceptionMapper.map(ex).getError().get(0).getMessage());

		//Case : 2
		String newMessage = "{\"error\":[{\"errorCode\":\"10148\",\"message\":\"Maximum Items per list reached\"}]}";
		mapper = new ObjectMapper();
		parsedErrorNode = mapper.readValue(newMessage.getBytes(UTF_8), JsonNode.class);

		errorMessage = "You've reached the maximum number of items you can add to this List. ";
		when(exceptionMessages.getMessage10148()).thenReturn(errorMessage);
		when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
		when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);

		ListServiceException exeception = new ListServiceException(STATUS_400, message);
		assertEquals(errorMessage, listServiceExceptionMapper.map(exeception).getError().get(0).getMessage());

		//case 3
		String errorMessage1 = "{\"error\":[{\"errorCode\":\"10101\",\"message\":\"Invalid User ID\"}]}";
		mapper = new ObjectMapper();
		parsedErrorNode = mapper.readValue(errorMessage1.getBytes(UTF_8), JsonNode.class);

		errorMessage = "Wish Lists are temporarily unavailable. Please try again later. ";
		when(exceptionMessages.getServiceFailureMessage()).thenReturn(errorMessage);
		when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
		when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);

		ListServiceException exception = new ListServiceException(STATUS_400, message);
		assertEquals(errorMessage, listServiceExceptionMapper.map(exception).getError().get(0).getMessage());

		String newMessage500 = "{\"error\":[{\"errorCode\":\"10134\",\"message\":\"Wishlist with same name already exists.\"}]}";
		mapper = new ObjectMapper();
		parsedErrorNode = mapper.readValue(newMessage500.getBytes(UTF_8), JsonNode.class);

		String error5001Message = "A Wish List with this name already exists.";
		when(exceptionMessages.getMessage50001()).thenReturn(error5001Message);
		when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
		when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);

		ListServiceException listException = new ListServiceException(STATUS_400, newMessage500);
		assertEquals(error5001Message, listServiceExceptionMapper.map(listException).getError().get(0).getMessage());

		String newMessage10156 = "{\"error\":[{\"errorCode\":\"10156\",\"message\":\"A list or a style board with same name already exists.\"}]}";
		mapper = new ObjectMapper();
		parsedErrorNode = mapper.readValue(newMessage10156.getBytes(UTF_8), JsonNode.class);

		String error10156Message = "A list or a style board with same name already exists.";
		when(exceptionMessages.getMessage10156()).thenReturn(error10156Message);
		when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
		when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);

		ListServiceException listException10156 = new ListServiceException(STATUS_400, newMessage10156);
		assertEquals(error10156Message, listServiceExceptionMapper.map(listException10156).getError().get(0).getMessage());
	}
	@Test
	public void testExceptionMapperWithOutErrorCode_BCOM() throws IOException {
        ReflectionTestUtils.setField(listServiceExceptionMapper, "applicationName", "BCOM");
        ReflectionTestUtils.setField(listServiceExceptionMapper, "isBCOM", true);
		String message = "{\"errors\":[{\"errorCode\":\"10147\",\"message\":\"Maximum lists per user reached\"}]}";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode parsedErrorNode = mapper.readValue(message.getBytes(UTF_8), JsonNode.class);

		String errorMessage = "Wish Lists are temporarily unavailable. Please try again later. ";
		when(exceptionMessages.getServiceFailureMessage()).thenReturn(errorMessage);
		when(customerLists.parse(any())).thenReturn(mock(JsonNode.class));
		when(serviceErrors.readValue(any())).thenReturn(parsedErrorNode);

		ListServiceException exception = new ListServiceException(STATUS_400, message);
		assertEquals(errorMessage, listServiceExceptionMapper.map(exception).getError().get(0).getMessage());
	}
}
