package com.macys.selection.xapi.list.data.converters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *  parses (lists.lists) json response 
 **/
@Component
public class JsonResponseParserCustomerLists extends AbstractJsonResponseParser {

  @Override
  public JsonNode readValue(JsonNode node) {
    if(null == node || node.isNull()) {
      return null;
    }    
    return node.get("lists").get("lists");
  }
  

}
