package cn.telbox.microarch.demo.mongo.geo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dahui on 18/10/2016.
 */
public class JsonParser {
    private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);

    private ObjectMapper mapper = new ObjectMapper();

    public JsonParser() {
        mapper.registerModule(new GeoJsonModule());  // TODO: ???
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.IGNORE_UNDEFINED, true);
        mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public <T> T read(String jsonString, Class<T> t) {
        try {
            return mapper.readValue(jsonString, t);
        } catch (IOException e) {
            logger.warn("Can not convert to bean " + t, e);
        }
        return null;
    }

    public <T> T readFromClassPath(String resPath, Class<T> t) {
        try {
            InputStream resourceStream = JsonParser.class.getResourceAsStream(resPath);
            return mapper.readValue(resourceStream, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T read(File file, Class<T> t) {
        try {
            return mapper.readValue(file, t);
        } catch (IOException e) {
            logger.warn("Can not convert to bean " + t, e);
        }
        return null;
    }

    public <T> void write(File file, T t) {
        try {
            mapper.writeValue(file, t);
        } catch (IOException e) {
            logger.warn("Can not write " + t + " to file " + t, e);
        }
    }
}
