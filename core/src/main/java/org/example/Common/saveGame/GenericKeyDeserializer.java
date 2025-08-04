package org.example.Common.saveGame;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class GenericKeyDeserializer extends KeyDeserializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        String[] parts = key.split("\\|", 2);
        if (parts.length != 2) {
            throw new IOException("Invalid key format: " + key);
        }

        String className = parts[0];
        String json = parts[1];

        try {
            Class<?> clazz = Class.forName(className);
            return mapper.readValue(json, clazz);
        } catch (ClassNotFoundException e) {
            throw new IOException("Unknown class: " + className, e);
        }
    }
}
