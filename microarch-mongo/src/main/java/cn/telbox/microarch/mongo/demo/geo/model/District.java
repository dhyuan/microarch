package cn.telbox.microarch.mongo.demo.geo.model;

import org.springframework.data.geo.Polygon;
import org.springframework.data.mongodb.core.geo.GeoJsonGeometryCollection;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

/**
 * Created by dahui on 10/14/16.
 */
public class District {

    private String name;
    private String code;

    private GeoJsonPolygon polygon;

    private Long cityCode;
}
