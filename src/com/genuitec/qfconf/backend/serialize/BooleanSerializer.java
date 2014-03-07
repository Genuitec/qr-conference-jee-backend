package com.genuitec.qfconf.backend.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class BooleanSerializer extends JsonSerializer<Boolean> {

	@Override
	public void serialize(Boolean value, JsonGenerator generator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		generator.writeNumber(Boolean.TRUE.equals(value) ? 1 : 0);
	}

}
