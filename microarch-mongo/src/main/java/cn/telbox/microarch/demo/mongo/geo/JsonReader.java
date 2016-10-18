package cn.telbox.microarch.demo.mongo.geo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;

import java.io.IOException;

/**
 * Created by dahui on 18/10/2016.
 */
public class JsonReader {
    private static final Logger logger = LoggerFactory.getLogger(JsonReader.class);

    private ObjectMapper mapper = new ObjectMapper();

    public JsonReader() {
        mapper.registerModule(new GeoJsonModule());  // TODO: ???
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public <T> T read(String jsonString, Class<T> t) {
        try {
            return mapper.readValue(jsonString, t);
        } catch (IOException e) {
            logger.warn("Can not convert to bean " + t, e);
        }
        return null;
    }

}
