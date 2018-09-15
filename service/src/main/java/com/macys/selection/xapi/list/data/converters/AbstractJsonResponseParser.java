package com.macys.selection.xapi.list.data.converters;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macys.selection.xapi.list.exception.ListServiceException;

public abstract class AbstractJsonResponseParser {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJsonResponseParser.class);

  public abstract JsonNode readValue(JsonNode node);
  
  public JsonNode parse(String jsonResponse) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    try {
      JsonNode rootNode = objectMapper.readValue(jsonResponse.getBytes("UTF-8"), JsonNode.class);
      if(rootNode != null && !rootNode.isNull()) {
          return rootNode;
      } 
      
    } catch (IOException ex) {
      LOGGER.error("exception parsing json response", ex.getMessage());
      throw new ListServiceException(ex.getMessage());
    }
    
    return null;
  }
  
}
