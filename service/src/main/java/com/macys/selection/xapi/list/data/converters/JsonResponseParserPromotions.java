package com.macys.selection.xapi.list.data.converters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *  parses (promotions) json response 
 **/
@Component
public class JsonResponseParserPromotions extends AbstractJsonResponseParser {

  @Override
  public JsonNode readValue(JsonNode node) {
    if(null == node || node.isNull()) {
      return null;
    }
    return node.get("ProductUPCPromotionResponse");
  }

}
