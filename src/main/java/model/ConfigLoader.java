package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import entity.Config;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Load config file and map to the config.class
 */
public class ConfigLoader {
    public Config load() throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("config.json");
        if (inputStream == null){
            throw new FileNotFoundException("Config file was not found under resource file!");
        }
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(streamReader);
        return gson.fromJson(jsonReader, Config.class);
    }
}
