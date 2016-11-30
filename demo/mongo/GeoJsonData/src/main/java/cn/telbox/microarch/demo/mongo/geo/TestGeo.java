package cn.telbox.microarch.demo.mongo.geo;

import cn.telbox.microarch.demo.mongo.geo.model.DistrictInfo;

/**
 * Created by dahui on 10/9/16.
 */

public class TestGeo {

    public static void main(String[] args) {
        JsonParser reader = new JsonParser();
        DistrictInfo districtInfo = reader.read(GeoDataDemo.JSON, DistrictInfo.class);
        districtInfo.setQgisFeatures();
    }
}
