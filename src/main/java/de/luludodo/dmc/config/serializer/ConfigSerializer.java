package de.luludodo.dmc.config.serializer;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;

public class ConfigSerializer<K, V> implements JsonSerializer<HashMap<K, V>>, JsonDeserializer<HashMap<K, V>> {
    public interface KeySerializer<K> {
        K parse(String s);
        String parse(K key);
    }
    public interface JsonDeAndSerializer<K> extends JsonSerializer<K>, JsonDeserializer<K> {}
    private final KeySerializer<K> keySerializer;
    private final JsonDeAndSerializer<V> valueSerializer;
    public ConfigSerializer(KeySerializer<K> keySerializer, JsonDeAndSerializer<V> valueSerializer) {
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }
    @Override
    public HashMap<K, V> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        HashMap<K, V> config = new HashMap<>(jsonObject.size());
        for (String key:jsonObject.keySet()) {
            config.put(keySerializer.parse(key), valueSerializer.deserialize(jsonObject.get(key), type, jsonDeserializationContext));
        }
        return config;
    }

    @Override
    public JsonElement serialize(HashMap<K, V> config, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        config.forEach((key, value) -> {
            jsonObject.add(keySerializer.parse(key), valueSerializer.serialize(value, type, jsonSerializationContext));
        });
        return jsonObject;
    }
}
