package cn.telbox.microarch.demo.mongo.geo.jurisdiction;

import com.google.common.collect.Lists;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.geo.GeoJsonGeometryCollection;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.util.List;

/**
 * Created by Dahui on 2016/12/1.
 */
public class GeoConverter {

    public static GeoJsonGeometryCollection convertCoordinateData2GeoCollection(List<Jurisdiction.CoordinateData> cds) {
        List<GeoJson<?>> geoJsonList = Lists.newArrayList();
        for (Jurisdiction.CoordinateData cd : cds) {
            if ("MultiPolygon".equalsIgnoreCase(cd.type)) {
                final GeoJsonMultiPolygon multiPolygon = parseAsMultiplyPolygon(cd.coordinatesJson);
                geoJsonList.add(multiPolygon);
            } else if ("Polygon".equalsIgnoreCase(cd.type)) {
                List<Object> closedLines = cd.coordinatesJson;
                for (Object cl : closedLines) {
                    final GeoJsonPolygon geoJsonPolygon = new GeoJsonPolygon(getLinePoints(cl));
                    geoJsonList.add(geoJsonPolygon);
                }
            }
        }
        return new GeoJsonGeometryCollection(geoJsonList);
    }

    private static List<Point> getLinePoints(Object p) {
        List<List<Double>> closedline = (List<List<Double>>) p;

        final int lastPointIndex = closedline.size() - 1;
        if ( ! (closedline.get(0).get(0).equals(closedline.get(lastPointIndex).get(0)))
                || ! (closedline.get(0).get(1).equals(closedline.get(lastPointIndex).get(1)) )) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$===> The first and last point must the same. ");
        }

        List<Point> points = Lists.newArrayList();
        for (List<Double> ps : closedline) {
            points.add(new Point(ps.get(0), ps.get(1)));
        }
        return points;
    }


    private static GeoJsonPolygon parseAsPolygon(List<List<Double>> coordinatesJson) {
        return new GeoJsonPolygon(getLinePoints(coordinatesJson));
    }

    private static GeoJsonMultiPolygon parseAsMultiplyPolygon(List<Object> coordinatesJson) {
        List<GeoJsonPolygon> geoJsonPolygons = Lists.newArrayList();
        for (Object p : coordinatesJson) {
            List<List<List<Double>>> polygons = (List<List<List<Double>>>) p;
            for (List<List<Double>> pl : polygons) {
                GeoJsonPolygon geoPolygon = parseAsPolygon(pl);
                geoJsonPolygons.add(geoPolygon);
            }
        }
        return new GeoJsonMultiPolygon(geoJsonPolygons);
    }

}
