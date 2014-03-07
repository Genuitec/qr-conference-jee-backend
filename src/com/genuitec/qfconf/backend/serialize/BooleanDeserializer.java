package com.genuitec.qfconf.backend.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class BooleanDeserializer extends JsonDeserializer<Boolean> {

	@Override
	public Boolean deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return "1".equals(parser.getText());
	}
}
