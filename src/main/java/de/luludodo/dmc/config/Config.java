package de.luludodo.dmc.config;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.util.Map;

public abstract class Config implements JsonSerializer<Map<String, Object>>, JsonDeserializer<Map<String, Object>> {
    protected final String filename;
    public Config(String filename) {
        this.filename = filename;
    }

    abstract Map<String, Object> getDefaultConfig();
}
