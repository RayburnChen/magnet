package org.nfa.athena.service;

import java.util.Arrays;

import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonReader;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObjectCodecProvider;

public abstract class AbstractObjectReader {

	private static final CodecRegistry CODEC_REGISTRY = CodecRegistries
			.fromProviders(Arrays.asList(new ValueCodecProvider(), new BsonValueCodecProvider(), new DBObjectCodecProvider()));

	protected static final Codec<BasicDBObject> CODEC = CODEC_REGISTRY.get(BasicDBObject.class);

	protected BasicDBObject parse(String json) {
		return CODEC.decode(new JsonReader(json), DecoderContext.builder().build());
	}

}
