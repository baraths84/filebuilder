package com.macys.selection.xapi.list.exception;

import static org.testng.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SpringBootTest(classes = {ExceptionProperties.class})
public class ExceptionPropertiesTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private ExceptionProperties exceptionMessages;
	

	ExceptionProperties exceptionProperties;

	@BeforeMethod
	public void setup() {
		exceptionProperties = new ExceptionProperties();
		exceptionProperties.setMessage10147("You've reached the maximum number of Lists you can create. ");
		exceptionProperties.setMessage10148("You've reached the maximum number of items you can add to this List. ");
		exceptionProperties.setServiceFailureMessage("We're experiencing a technical glitch. Please try again later or call us at 1-800-BUY-MACY (289-6229) for immediate assistance. ");
		exceptionProperties.setTemporaryFailureMessage("We had a temporary technical glitch. Please try again. ");
		exceptionProperties.setMessage50001("Wishlist with same name already exists.");
	}
	
	 @Test
	 public void beanIsSerializable() {
		 assertEquals(exceptionProperties.getMessage10147(), exceptionMessages.getMessage10147());
		 assertEquals(exceptionProperties.getMessage10148(), exceptionMessages.getMessage10148());
		 assertEquals(exceptionProperties.getServiceFailureMessage(), exceptionMessages.getServiceFailureMessage());
		 assertEquals(exceptionProperties.getTemporaryFailureMessage(), exceptionMessages.getTemporaryFailureMessage());
		 assertEquals(exceptionProperties.getMessage50001(), exceptionMessages.getMessage50001());
	 }

	
}
