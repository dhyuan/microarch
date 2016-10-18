package cn.telbox.microarch.demo.mongo.geo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;
import org.springframework.data.mongodb.core.geo.GeoJsonGeometryCollection;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by dahui on 10/1/16.
 */

@Document
@NoArgsConstructor
@Getter
@Setter
public class UserLocation {

    private String name;

    private GeoJsonPoint location;
    private Point locPoint;

    private GeoJsonPolygon polygon;
    private Polygon locPolygon;

    private GeoJsonMultiPolygon multiPolygon;

    private GeoJsonGeometryCollection places;


    @Override
    public String toString() {
        return name + " " + location;
    }

}
