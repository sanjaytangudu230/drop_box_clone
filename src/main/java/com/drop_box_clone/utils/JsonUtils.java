package com.drop_box_clone.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public JsonUtils() {
    }

    public static <T> String getJson(T data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T convertToPojo(String json, Class<T> t) throws Exception {
        return objectMapper.readValue(json, t);
    }


}