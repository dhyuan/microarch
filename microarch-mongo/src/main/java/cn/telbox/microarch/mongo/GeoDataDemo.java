package cn.telbox.microarch.mongo;

import cn.telbox.microarch.mongo.model.model.UserLocation;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dahui on 10/8/16.
 */
@SpringBootApplication
public class GeoDataDemo {


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
    CommandLineRunner runner(MongoTemplate mongoTemplate, UserLocation userLocation) {
        return args -> {
            mongoTemplate.save(userLocation);
            List<UserLocation> userLocations = mongoTemplate.findAll(UserLocation.class);
            userLocations.stream().forEach(System.out::println);
        };
    }


    public static void main(String[] args) {
        SpringApplication.run(GeoDataDemo.class, args);


    }


    @Bean
    UserLocation userLocation() {
        UserLocation userLocation = new UserLocation();
        userLocation.setName("Location 1");

        // Point & GeoJsonPoint
        Point point = new Point(-2, 2);
        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(point);

        Point point1 = new Point(0, 0);
        Point point2 = new Point(2, 0);
        Point point3 = new Point(2, 2);
        Point point4 = new Point(0, 2);

        Point pointA1 = new Point(0, 0);
        Point pointA2 = new Point(20, 0);
        Point pointA3 = new Point(20, 20);
        Point pointA4 = new Point(0, 20);

        List<Point> squareLine = Lists.newArrayList(point1, point2, point3, point4);
        List<Point> squareLineA = Lists.newArrayList(pointA1, pointA2, pointA3, pointA4);

        // Polygon & GeoJsonPolygon
        Polygon polygon = new Polygon(squareLine);
        Polygon polygonA = new Polygon(squareLineA);

        GeoJsonPolygon geoJsonPolygon = new GeoJsonPolygon(squareLine);
        GeoJsonPolygon geoJsonPolygonA = new GeoJsonPolygon(squareLineA);

        List<GeoJsonPolygon> geoJsonPolygons = Lists.newArrayList(geoJsonPolygon, geoJsonPolygonA);

        // GeoJsonMultiPolygon
        GeoJsonMultiPolygon geoJsonMultiPolygon = new GeoJsonMultiPolygon(geoJsonPolygons);

        userLocation.setLocation(geoJsonPoint);
        userLocation.setLocPoint(point);
        userLocation.setPolygon(geoJsonPolygon);
        userLocation.setLocPolygon(polygon);
        userLocation.setMultiPolygon(geoJsonMultiPolygon);


        return userLocation;
    }

}

@RestController
class UserLocationController {
    @RequestMapping("userLocations")
    public List<UserLocation> userLocations() {
        return mongoTemplate.findAll(UserLocation.class);
    }

    @Autowired
    MongoTemplate mongoTemplate;
}
