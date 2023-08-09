package de.luludodo.dmc.config;

import com.google.gson.*;
import de.luludodo.dmc.coords.Mode;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DMCConfig extends Config {
    public DMCConfig() {
        super("dmc/config");
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("mode", Mode.RELATIVE);
        defaults.put("offset-x", 0L);
        defaults.put("offset-y", 0L);
        defaults.put("offset-z", 0L);
        defaults.put("obscure-rotations", false);
        return defaults;
    }

    @Override
    public Map<String, Object> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Map<String, Object> config = new HashMap<>();
        JsonObject configObject = jsonElement.getAsJsonObject();
        config.put("mode", Mode.valueOf(configObject.get("mode").getAsString()));
        config.put("offset-x", configObject.get("offset-x").getAsLong());
        config.put("offset-y", configObject.get("offset-y").getAsLong());
        config.put("offset-z", configObject.get("offset-z").getAsLong());
        config.put("obscure-rotations", configObject.get("obscure-rotations").getAsBoolean());
        return config;
    }

    @Override
    public JsonElement serialize(Map<String, Object> config, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject configObject = new JsonObject();
        configObject.add("mode", new JsonPrimitive(config.get("mode").toString()));
        configObject.add("offset-x", new JsonPrimitive((long) config.get("offset-x")));
        configObject.add("offset-y", new JsonPrimitive((long) config.get("offset-y")));
        configObject.add("offset-z", new JsonPrimitive((long) config.get("offset-z")));
        configObject.add("obscure-rotations", new JsonPrimitive((boolean) config.get("obscure-rotations")));
        return configObject;
    }
}
