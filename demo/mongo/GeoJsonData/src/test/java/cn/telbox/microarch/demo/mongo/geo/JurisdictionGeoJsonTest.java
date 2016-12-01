package cn.telbox.microarch.demo.mongo.geo;

import cn.telbox.microarch.demo.mongo.geo.jurisdiction.GeoConverter;
import cn.telbox.microarch.demo.mongo.geo.jurisdiction.Jurisdiction;
import org.apache.commons.io.IOUtils;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.geo.GeoJsonGeometryCollection;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by dahui on 18/10/2016.
 */
public class JurisdictionGeoJsonTest {
    @Test
    public void testPolygonLineIsClosedWithoutDuplicatedPoints() throws Exception {
        final String[] fName = {"jurisdiction1.json", "jurisdiction2.json"};
        final JsonParser reader = new JsonParser();

        for (String fileName : fName) {
            final InputStream resourceAsStream = JurisdictionGeoJsonTest.class.getResourceAsStream("/data/" + fileName);
            StringWriter writer = new StringWriter();
            IOUtils.copy(resourceAsStream, writer);
            String jsonString = writer.toString();

            Jurisdiction jurisdiction = reader.read(jsonString, Jurisdiction.class);
            final GeoJsonGeometryCollection geoJsonGeometryCollection = GeoConverter.convertCoordinateData2GeoCollection(jurisdiction.getCoordinates());
            verifyGeoPolygonData(geoJsonGeometryCollection);
        }
    }

    private void verifyGeoPolygonData(GeoJsonGeometryCollection geometryCollection) {
        final Iterable<GeoJson<?>> coordinates = geometryCollection.getCoordinates();
        coordinates.forEach(c -> {
            if (c instanceof GeoJsonPolygon) {
                GeoJsonPolygon polygon = (GeoJsonPolygon) c;
                final List<GeoJsonLineString> lines = polygon.getCoordinates();
                lines.stream().forEach(line -> {
                    final Map<String, List<Point>> collect = line.getCoordinates().stream().collect(Collectors.groupingBy(o -> o.getX() + "_" + o.getY()));
                    final List<Point> duplicatedPoints = collect.values().stream()
                            .filter(ps -> ps.size() > 1)
                            .map(ps -> ps.get(0))
                            .collect(Collectors.toList());

                    final Map<String, Point> verifiedPointsMap = line.getCoordinates().stream().collect(Collectors.toMap(
                            p -> p.getX() + "_" + p.getY(),
                            p -> p,
                            (p1, p2) -> p1,
                            LinkedHashMap::new)
                    );

                    assertTrue("There are some points duplicated", duplicatedPoints.size() == 1);
                    assertTrue("The polygon is not closed.", duplicatedPoints.get(0).getX() == duplicatedPoints.get(duplicatedPoints.size() - 1).getX());
                    assertTrue("The polygon is not closed.", duplicatedPoints.get(0).getY() == duplicatedPoints.get(duplicatedPoints.size() - 1).getY());
                });
            }

        });
    }
}
