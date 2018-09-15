package com.macys.selection.xapi.list.data.converters;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class JsonResponseParserServiceErrors extends AbstractJsonResponseParser{

	@Override
	  public JsonNode readValue(JsonNode node) {
	    if(null == node || node.isNull()) {
	      return null;
	    }
	    return node.get("errors");
	  }
}
