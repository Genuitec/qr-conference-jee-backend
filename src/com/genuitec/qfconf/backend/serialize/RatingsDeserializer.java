package com.genuitec.qfconf.backend.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.genuitec.qfconf.backend.model.Rating;

public class RatingsDeserializer extends JsonDeserializer<Rating> {

	@Override
	public Rating deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return Rating.values()[Integer.parseInt(parser.getText())];
	}

}
