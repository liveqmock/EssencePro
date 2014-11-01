package com.util;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();
    private static JsonFactory jsonFactory = new JsonFactory();

    public static <T> Object fromJson(String jsonAsString, Class<T> clazz) throws JsonMappingException, JsonParseException, IOException {
        return mapper.readValue(jsonAsString, clazz);
    }

    public static <T> Object fromJson(FileReader fr, Class<T> clazz) throws JsonParseException, IOException {
        return mapper.readValue(fr, clazz);
    }

    public static String toJson(Object obj) throws Exception {
        StringWriter sw = new StringWriter();
        JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
        jg.useDefaultPrettyPrinter();
        mapper.writeValue(jg, obj);
        return sw.toString();
    }

    public static void toJson(Object obj, FileWriter fw) throws Exception {
        JsonGenerator jg = jsonFactory.createJsonGenerator(fw);
        jg.useDefaultPrettyPrinter();
        mapper.writeValue(jg, obj);
    }
}