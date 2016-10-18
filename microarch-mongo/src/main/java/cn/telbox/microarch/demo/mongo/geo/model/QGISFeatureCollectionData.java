package cn.telbox.microarch.demo.mongo.geo.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by dahui on 18/10/2016.
 */
public class QGISFeatureCollectionData {
    public String type = "FeatureCollection";

    public List<QGISFeatureData> features = Lists.newArrayList();

    public void addFeature(QGISFeatureData featureData) {
        features.add(featureData);
    }

}
