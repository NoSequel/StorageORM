package io.github.nosequel.storage.serialization.impl;

import com.google.gson.*;
import io.github.nosequel.storage.serialization.Serializer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GsonSerializer<T> implements Serializer<Object> {

    private final Class<T> clazz;
    private final JsonParser parser = new JsonParser();

    public final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Deserialize a {@link Object} object to a {@link String}
     *
     * @param source the string to deserialize
     * @return the object
     */
    @Override
    public Object deserialize(String source) {
        return gson.fromJson(source, clazz);
    }

    /**
     * Serialize a {@link Object} object to a {@link String}
     *
     * @param object the object to serialize
     * @return the serialized object
     */
    @Override
    public JsonElement serialize(Object object) {
        return this.parser.parse(gson.toJson(object));
    }
}