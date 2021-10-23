package be.anouchka.exercise.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static List<Map<String, Object>> fromJsonList(String json) {
        try {
            return MAPPER.readValue(json, List.class);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
