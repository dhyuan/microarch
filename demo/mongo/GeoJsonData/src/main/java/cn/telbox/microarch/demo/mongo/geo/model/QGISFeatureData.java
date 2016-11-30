package cn.telbox.microarch.demo.mongo.geo.model;

import java.util.List;
import java.util.Map;

/**
 * Created by dahui on 18/10/2016.
 */
public class QGISFeatureData {

    public Map properties;
    public String type;
    public QGISGeometry geometry = new QGISGeometry();



    public static class QGISGeometry {
        public List<Object> coordinates;
        public String type;
    }
}


