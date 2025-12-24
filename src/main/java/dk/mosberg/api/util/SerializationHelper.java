package dk.mosberg.api.util;

import java.util.Optional;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dk.mosberg.api.MosbergApi;

/**
 * Helper for Codec-based serialization
 */
public class SerializationHelper {

    public static <T> JsonObject toJson(T object, Codec<T> codec) {
        return codec.encodeStart(JsonOps.INSTANCE, object).resultOrPartial(MosbergApi.LOGGER::error)
                .map(JsonElement::getAsJsonObject).orElse(new JsonObject());
    }

    public static <T> Optional<T> fromJson(JsonObject json, Codec<T> codec) {
        return codec.parse(JsonOps.INSTANCE, json).resultOrPartial(MosbergApi.LOGGER::error);
    }
}
