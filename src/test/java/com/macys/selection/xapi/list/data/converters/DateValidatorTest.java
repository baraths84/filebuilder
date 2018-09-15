package com.macys.selection.xapi.list.data.converters;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.macys.selection.xapi.list.data.converters.DateValidator;

/**
 *  Date parser test 
 **/
public class DateValidatorTest extends AbstractTestNGSpringContextTests {
	
  private static final String TEST_DATE = "2017-08-07 01:13:24.333";
  private static final String TEST_DATE_FOR_PARSE = "2017-08-07 01:13:24.333654";

  @Test
  public void test_date_with_three_digits_in_milliseconds() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    Date expectedDate = sdf.parse(TEST_DATE);       
    
    Optional<Date> actualDate = DateValidator.parse(TEST_DATE);    
    assertNotNull(actualDate);    
    assertEquals(actualDate.orElse(null).compareTo(expectedDate), 0);    
  }
  
  // invalid date format
  @Test
  public void test_invalid_six_digit_format() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
    Date expectedDate = sdf.parse(TEST_DATE);
    
    Optional<Date> actualDate = DateValidator.parse(TEST_DATE_FOR_PARSE);
    assertNotNull(actualDate);        
    assertEquals(actualDate.orElse(null).compareTo(expectedDate), 1);    
  }

  // null value passed as date
  @Test
  public void test_date_is_set_to_null() throws ParseException {
    Optional<Date> actualDate = DateValidator.parse(null);
    assertNotNull(actualDate);
    assertFalse(actualDate.isPresent());
  }

  // empty string passed as date
  @Test
  public void test_empty_value_passed_as_date() {
    Optional<Date> actualDate = DateValidator.parse("");
    assertNotNull(actualDate);
    assertFalse(actualDate.isPresent());
  }
  
  // special character passed as date
  @Test
  public void test_special_character_passed_as_date() {
    Optional<Date> actualDate = DateValidator.parse("$#%%^9897");
    assertNotNull(actualDate);
    assertFalse(actualDate.isPresent());
  }
  
  @Test
  public void test_date_with_six_digits_in_milliseconds() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
    Date expectedDate = sdf.parse(TEST_DATE_FOR_PARSE);
    
    Optional<Date> actualDate = DateValidator.parse(TEST_DATE_FOR_PARSE);
    assertNotNull(actualDate);    
    assertEquals(actualDate.orElse(null).compareTo(expectedDate), 0);    
  }

  @Test
  public void test_date_with_t_and_milliseconds() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    Date expectedDate = sdf.parse("2017-09-14T18:27:54.203");

    Optional<Date> actualDate = DateValidator.parse("2017-09-14T18:27:54.203");
    assertNotNull(actualDate);    
    assertEquals(actualDate.orElse(null).compareTo(expectedDate), 0);    
  }
  
  

}
