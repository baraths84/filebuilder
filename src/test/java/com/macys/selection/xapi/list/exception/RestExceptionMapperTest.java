package com.macys.selection.xapi.list.exception;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SpringBootTest
(classes = {ExceptionProperties.class})
public class RestExceptionMapperTest extends AbstractTestNGSpringContextTests {

	private static final int STATUS_500 = 500;
	@InjectMocks
	private RestExceptionMapper restExceptionMapper;

	@Mock
	private ExceptionProperties exceptionMessages;
	
	@BeforeMethod
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testExceptionMapper(){
		String message = "We had a temporary technical glitch. Please try again.";
		when(exceptionMessages.getTemporaryFailureMessage()).thenReturn(message);
		
		RestException ex = new RestException( "rest exception");
		assertEquals(message, restExceptionMapper.map(ex).getError().get(0).getMessage());
		assertEquals("50002", restExceptionMapper.map(ex).getError().get(0).getErrorCode());
		assertEquals(STATUS_500,restExceptionMapper.map(ex).getHttpStatus());
	}
	
	@Test
	public void testExceptionMapperException(){
		String message = "We had a temporary technical glitch. Please try again.";
		when(exceptionMessages.getTemporaryFailureMessage()).thenReturn(message);

		RestException ex = new RestException( "rest exception", new NullPointerException());
		assertEquals(message, restExceptionMapper.map(ex).getError().get(0).getMessage());
		assertEquals("50002", restExceptionMapper.map(ex).getError().get(0).getErrorCode());
		assertEquals(STATUS_500,restExceptionMapper.map(ex).getHttpStatus());
	}

}
