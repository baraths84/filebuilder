package com.macys.selection.xapi.list.data.converters;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * validate dates against different date formats parsers.
 * 
 * Noticed we are getting different date formats from MSP Customer services, we want to make sure
 * that our xapi service does not break with multiple differet date formats.
 **/
public class DateValidator {  
  private static final Logger LOGGER = LoggerFactory.getLogger(DateValidator.class);

  private static List<String> dateFormaterTypes = Arrays.asList(
      "yyyy-MM-dd'T'HH:mm:ss.SSS", 
      "yyyy-MM-dd HH:mm:ss.SSS",
      "yyyy-MM-dd HH:mm:ss.SSSSSS");
  
  private DateValidator(){}

  public static Optional<Date> parse(String dateToParse) {
    return Optional.ofNullable(dateToParse).map(dateString -> {
      for(String dateFormaterType : dateFormaterTypes) {
        try {
          
          SimpleDateFormat df = new SimpleDateFormat(dateFormaterType);
          df.applyPattern(dateFormaterType);
          return df.parse(dateString);

        } catch (Exception e) {
          LOGGER.info("expected exception while identifying the correct date format returned from the service", e.getMessage());
        }          
      }      
      return null;
    });    
  }
  
}
