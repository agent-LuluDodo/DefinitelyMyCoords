package de.luludodo.dmc.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.luludodo.dmc.log.Log;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

public class JsonConfig {
    private final Gson gson;
    private final File file;
    protected Map<String, Object> content;
    private final Config config;

    public JsonConfig(Config config) {
        this.config = config;
        gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(), config).create();
        file = FabricLoader.getInstance().getConfigDir().resolve(config.filename + ".json").toFile();
        try {
            content = read(file);
            Log.info("Read config " + config.filename + ".json!");
        } catch (InvalidFormatException e) {
            Log.warn("Couldn't read config " + config.filename + ".json, because the file is empty!");
            content = getDefaults();
        } catch (IOException e) {
            Log.warn("Couldn't read config " + config.filename + ".json!");
            content = getDefaults();
        }
    }

    private Map<String, Object> read(File file) throws IOException {
        String contentToParse = FileUtils.readFileToString(file, Charset.defaultCharset());
        if (contentToParse.trim().equals("")) {
            throw new InvalidFormatException();
        }
        return gson.fromJson(contentToParse, new TypeToken<Map<String, Object>>(){}.getType());
    }

    protected Map<String, Object> getDefaults() {
        return config.getDefaultConfig();
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(gson.toJson(content));
            writer.close();
        } catch (FileNotFoundException e) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e2) {
                Log.warn("Couldn't create file to save config " + file.getName() + "!");
                e.printStackTrace();
                e2.printStackTrace();
            }
        } catch (IOException e) {
            Log.warn("Couldn't save config " + file.getName() + "!");
            e.printStackTrace();
        }
    }

    public Object get(String key) {
        return content.get(key);
    }

    public void set(String key, Object value) {
        content.put(key, value);
        save();
    }
}

