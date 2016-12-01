package cn.telbox.microarch.demo.mongo.geo.jurisdiction;

import cn.telbox.microarch.demo.mongo.geo.JsonParser;
import com.mongodb.MongoClient;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.geo.GeoJsonGeometryCollection;

import java.io.InputStream;
import java.io.StringWriter;

/**
 *
 * This app is used to demo the Mongo GeoJsonData insert/query.
 *
 * https://docs.mongodb.com/manual/tutorial/geospatial-tutorial/
 *
 *
 * db.jurisdiction.createIndex( {"geoJsonCollection":"2dsphere"} );
 *
 * Created by Dahui on 2016/12/1.
 */

@SpringBootApplication
public class JurisdictionApp {

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(), "yourdb");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

    @Bean
    CommandLineRunner commandLineRunner(MongoTemplate mongoTemplate) {
        return arg -> {
            final String[] fName = { "jurisdiction2.json"};
            for (String fileName : fName) {
                final InputStream resourceAsStream = JurisdictionApp.class.getResourceAsStream("/jsonfiles/" + fileName);
                StringWriter writer = new StringWriter();
                IOUtils.copy(resourceAsStream, writer);
                String theString = writer.toString();

                JsonParser reader = new JsonParser();
                Jurisdiction jurisdiction = reader.read(theString, Jurisdiction.class);

                final GeoJsonGeometryCollection geoJsonGeometryCollection = GeoConverter.convertCoordinateData2GeoCollection(jurisdiction.getCoordinates());
                jurisdiction.setGeoJsonCollection(geoJsonGeometryCollection);

                mongoTemplate.save(jurisdiction);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(JurisdictionApp.class, args);
    }
}
