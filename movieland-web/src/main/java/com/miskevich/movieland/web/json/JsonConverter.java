package com.miskevich.movieland.web.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class JsonConverter {

    private static final Logger LOG = LoggerFactory.getLogger(JsonConverter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(List<?> list) {
        try {
            LOG.info("Object's list was converted to Json");
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            LOG.error("ERROR: ", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> String toJson(T value) {
        try {
            LOG.info("Object was converted to Json");
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            LOG.error("ERROR: ", e);
            throw new RuntimeException(e);
        }
    }
}
