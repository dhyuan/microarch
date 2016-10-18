package cn.telbox.microarch.demo.mongo.geo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.*;

import java.util.List;

/**
 * Created by Dahui on 2016/10/9.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class DistrictInfo {

//    @JsonProperty("ManagerInfoExtended.general.id")
    @JsonProperty("ManagerInfoExtended")
    public ManagerInfoExtended agencyInfo;             // id

    @JsonProperty("EventLocation")
    public EventLocation location;

    @JsonProperty("Cap")
    public Cap cap;
    

    @JsonProperty("status")
    public Status status;

    public String title;

    public String description;



//
//    public String distribution_areas;              // GeoJsonGeometryCollection geometry

    @JsonProperty("DistributionAreas")
    public List<CoordinateData> coordinateDatas;


    public Integer statusCode;
    public String StatusText;
    public String headLineSms;

    public GeoJsonGeometryCollection geometries;
    public GeoJsonMultiPoint points;

    public QGISFeatureCollectionData qgisFeatures = new QGISFeatureCollectionData();

    public void setQgisFeatures() {
        int i = 0;
        for (CoordinateData cd: coordinateDatas) {
            QGISFeatureData qgisFeatureData = new QGISFeatureData();
            qgisFeatureData.type = "" + i;
            qgisFeatureData.geometry.coordinates = cd.coordinatesJson;
            qgisFeatureData.geometry.type = cd.type;

            qgisFeatures.addFeature(qgisFeatureData);

            i ++;
        }
    }

//    @JsonGetter("geometries")
    public GeoJsonGeometryCollection computeGeometriesData(Double c) {
        List<GeoJson<?>> geometries = Lists.newArrayList();
        for (CoordinateData cd: coordinateDatas) {

            if ("MultiPolygon".equalsIgnoreCase(cd.type)) {
                List<GeoJsonPolygon> geoJsonPolygons = Lists.newArrayList();
                List<Object> polygongs = cd.coordinatesJson;
                for (Object p : polygongs) {
                    List<Point> points = Lists.newArrayList();
                    List<List<List<Double>>> polygon = (List<List<List<Double>>>) p;
                    for (List<List<Double>> line : polygon) {
                        for (List<Double> ps : line) {
                            if (c != null) {
                                points.add(new Point(ps.get(0) + c, ps.get(1) + c));
                            }else {
                                points.add(new Point(ps.get(0), ps.get(1)));
                            }
                        }
                        if (this.points == null && line.size() >= 3 && c != null) {
                            this.points = new GeoJsonMultiPoint(
                                    new Point(line.get(0).get(0) + c, line.get(0).get(1) + c),
                                    new Point(line.get(1).get(0) + c, line.get(1).get(1) + c),
                                    new Point(line.get(2).get(0) + c, line.get(2).get(1) + c));
                        }
                    }
                    GeoJsonPolygon geoJsonPolygon = new GeoJsonPolygon(points);
                    geoJsonPolygons.add(geoJsonPolygon);
                }
                GeoJsonMultiPolygon geoJsonMultiPolygon = new GeoJsonMultiPolygon(geoJsonPolygons);
                geometries.add(geoJsonMultiPolygon);
            }else if ("Polygon".equalsIgnoreCase(cd.type)) {
                List<Point> points = Lists.newArrayList();
                List<Object> polygongLines = cd.coordinatesJson;
                for (Object l : polygongLines) {
                    List<List<Double>> line = (List<List<Double>>) l;
                    for (List<Double> ps : line) {
                        if (c != null) {
                            points.add(new Point(ps.get(0) + c, ps.get(1) + c));
                        }else {
                            points.add(new Point(ps.get(0), ps.get(1)));
                        }
                    }
                    if (this.points == null && line.size() >= 3 && c != null) {
                        this.points = new GeoJsonMultiPoint(
                                new Point(line.get(0).get(0) + c, line.get(0).get(1) + c),
                                new Point(line.get(1).get(0) + c, line.get(1).get(1) + c),
                                new Point(line.get(2).get(0) + c, line.get(2).get(1) + c));
                    }
                }
                GeoJsonPolygon geoJsonPolygon = new GeoJsonPolygon(points);
                geometries.add(geoJsonPolygon);
            }
        }
        this.geometries = new GeoJsonGeometryCollection(geometries);
        return this.geometries;
    }


    public static class ManagerInfoExtended {
        @JsonProperty("general")
        public General general;

        public static class General {
            public String id;
            public String everbridge_agency_id;
            public String logo;
            public String logo_lg;
            public String logo_md;
            public String logo_sm;
            public String timezone;
        }
    }


    public static class EventLocation {
        @JsonProperty("GeoLocation")
        public GeoLocation geoLocation;

        @JsonProperty("LocationAddress")
        public LocationAddress locationAddress;

        public static class GeoLocation {
            public String lat;
            public String lon;
        }

        public static class LocationAddress {
            public String address;
            public String city;
            public String state;
            public String zip;
        }
    }

    public static class Cap {
        public String category;
        public String certainty;
        public String event_description;
        public String instructions;
        public String severity;
        public String urgency;
    }
    

    public static class Status {
        public String status_code;
        public String status_text;
    }

    public static class CoordinateData {
        @JsonProperty("coordinates")
        public List<Object> coordinatesJson;
        public String type;
    }

}


