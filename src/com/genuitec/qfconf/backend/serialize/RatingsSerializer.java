package com.genuitec.qfconf.backend.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.genuitec.qfconf.backend.model.Rating;

public class RatingsSerializer extends JsonSerializer<Rating> {

	@Override
	public void serialize(Rating rating, JsonGenerator generator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		generator.writeNumber(rating.ordinal());
	}

}
