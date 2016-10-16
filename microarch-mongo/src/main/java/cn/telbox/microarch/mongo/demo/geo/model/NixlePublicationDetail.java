package cn.telbox.microarch.mongo.demo.geo.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.*;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Dahui on 2016/10/9.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class NixlePublicationDetail {

//    @JsonProperty("AgencyInfoExtended.general.id")
    @JsonProperty("AgencyInfoExtended")
    public AgencyInfoExtended agencyInfo;             // id

    @JsonProperty("IncidentLocation")
    public IncidentLocation location;

    @JsonProperty("Cap")
    public Cap cap;

    @JsonProperty("PubMedia")
    public PubMedia pubMedia;

    @JsonProperty("status")
    public Status status;

    public String headline;        // title

    public String text;            // body

    public String priority;        // priority:  Alert, Advisory, Community
    public String lastModified;           // startDate created  ???
    public String publishedTime;
    public String expires;           // endDate ????, Maybe not provided in the V1.
    public String eventType;               // eventType ==>priority:  Alert, Advisory, Community
    public String action;                  // action   ???, Conducted from statusCode.
    public String parentPubId;          // originalFeedId ????


    public String apply_geographic_filtering; //": 0,  # if 0, do not apply geographic filtering to agency defined groups, otherwise do apply geographic filtering to agency defined groups
    public String bc_accuracy;
    public String bc_addr;
    public String bc_addr_type;
    public String bc_all_direct;
    public String bc_city;
    public String bc_cross_st;
    public String bc_entered;
    public String bc_geocoded;
    public String bc_latitude;
    public String bc_longitude;
    public String bc_provider;
    public String bc_radius;
    public String bc_state;
    public String bc_zipcode;


//
//    public String distribution_areas;              // GeoJsonGeometryCollection geometry

    @JsonProperty("DistributionAreas")
    public List<CoordinateData> coordinateDatas;


//    @JsonProperty("DistributionAreas")
//    public List<GeoJson<?>> geometries;

//    public GeoJson geoJson;

    public Integer statusCode;
    public String StatusText;
    public String headLineSms;

    public GeoJsonGeometryCollection geometries;

    public GeoJsonMultiPoint points;

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


    public static class AgencyInfoExtended {
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


    public static class IncidentLocation {
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

    public static class PubMedia {

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


