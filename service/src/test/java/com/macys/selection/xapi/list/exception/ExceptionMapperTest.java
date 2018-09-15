package com.macys.selection.xapi.list.exception;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ExceptionMapperTest extends AbstractTestNGSpringContextTests {
	private static final int STATUS_CODE = 500;
	@InjectMocks
	private ExceptionMapper exceptionMapper;

	@Mock
	private ExceptionProperties exceptionMessages;
	
	@BeforeMethod
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExceptionMapperNullPointer(){
		String message = "We had a temporary technical glitch. Please try again.";
		when(exceptionMessages.getTemporaryFailureMessage()).thenReturn(message);

		Exception ex = new NullPointerException( "null pointer exception");
		assertEquals(message, exceptionMapper.map(ex).getError().get(0).getMessage());
		assertEquals("50002", exceptionMapper.map(ex).getError().get(0).getErrorCode());
		assertEquals(STATUS_CODE,exceptionMapper.map(ex).getHttpStatus());
	}
	
	@Test
	public void testExceptionMapperArrayIndexOutOfBounds(){
		String message = "We had a temporary technical glitch. Please try again.";
		when(exceptionMessages.getTemporaryFailureMessage()).thenReturn(message);
		
		Exception ex = new ArrayIndexOutOfBoundsException();
		assertEquals(message, exceptionMapper.map(ex).getError().get(0).getMessage());
		assertEquals("50002", exceptionMapper.map(ex).getError().get(0).getErrorCode());
		assertEquals(STATUS_CODE,exceptionMapper.map(ex).getHttpStatus());
	}

}
