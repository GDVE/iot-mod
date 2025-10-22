package ru.iot.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class GsonConfig extends Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public GsonConfig(Path path) {
        super(path);
    }

    @Override
    public void readFromBytes(byte[] bytes) throws IOException {
        Config config;
        try {
            config = GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), this.getClass());
        } catch (Throwable e) {
            throw new IOException("Failed to read from string", e);
        }

        this.replaceFields(config);
    }

    @Override
    public byte[] toBytes() {
        StringWriter writer = new StringWriter();
        GSON.toJson(GSON.toJsonTree(this, this.getClass()), writer);
        return writer.toString().getBytes(StandardCharsets.UTF_8);
    }
}
